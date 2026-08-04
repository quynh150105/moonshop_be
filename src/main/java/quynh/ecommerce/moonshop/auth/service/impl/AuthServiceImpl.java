package quynh.ecommerce.moonshop.auth.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quynh.ecommerce.moonshop.auth.dto.request.IntrospectRequest;
import quynh.ecommerce.moonshop.auth.dto.request.LoginRequest;
import quynh.ecommerce.moonshop.auth.dto.request.LogoutRequest;
import quynh.ecommerce.moonshop.auth.dto.request.RefreshTokenRequest;
import quynh.ecommerce.moonshop.auth.dto.response.AuthUserResponse;
import quynh.ecommerce.moonshop.auth.dto.response.IntrospectResponse;
import quynh.ecommerce.moonshop.auth.dto.response.LoginResponse;
import quynh.ecommerce.moonshop.auth.dto.response.RefreshTokenResponse;
import quynh.ecommerce.moonshop.auth.entity.InvalidatedToken;
import quynh.ecommerce.moonshop.auth.repository.InvalidatedTokenRepository;
import quynh.ecommerce.moonshop.auth.service.AuthService;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.enums.UserStatus;
import quynh.ecommerce.moonshop.common.exception.AppException;
import quynh.ecommerce.moonshop.user.entity.User;
import quynh.ecommerce.moonshop.user.repository.UserRepository;
import quynh.ecommerce.moonshop.user.dto.response.UserResponse;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.secret}")
    private String SignerKey;

    @NonFinal
    @Value("${jwt.access.expiration_time}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refresh.expiration_time}")
    protected long REFRESHABLE_DURATION;

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        var token = introspectRequest.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        var user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        boolean authenticated = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!authenticated)  throw new AppException(ErrorCode.INVALID_CREDENTIALS);

        if (user.getUserStatus() == UserStatus.BLOCKED) {
            throw new AppException(ErrorCode.ACCOUNT_BLOCKED);
        }

        Date accessExpiry = Date.from(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS));
        Date refreshExpiry = Date.from(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS));

        return LoginResponse.builder()
                .user(toAuthUserResponse(user))
                .token(generateToken(user, accessExpiry))
                .refreshToken(generateToken(user, refreshExpiry))
                .expiresAt(accessExpiry.getTime())
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("name: {}", userName);
        try {
            var signToken = verifyToken(logoutRequest.getRefreshToken());

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException | JOSEException | ParseException exception) {
            log.info(ErrorCode.TOKEN_EXPIRED.getMessage());
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        JWTClaimsSet claimsSet;
        try {
            SignedJWT signedJWT = verifyToken(refreshTokenRequest.getRefreshToken());
            claimsSet = signedJWT.getJWTClaimsSet();
        } catch (AppException exception) {
            if (exception.getErrorCode() == ErrorCode.TOKEN_EXPIRED) {
                throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
            }
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        } catch (JOSEException | ParseException exception) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        var jit = claimsSet.getJWTID();
        var expiryTime = claimsSet.getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var userId = claimsSet.getSubject();

        var user =
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getUserStatus() == UserStatus.BLOCKED) {
            throw new AppException(ErrorCode.ACCOUNT_BLOCKED);
        }

        Date accessExpiry = Date.from(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS));
        Date refreshExpiry = Date.from(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS));

        return RefreshTokenResponse.builder()
                .token(generateToken(user, accessExpiry))
                .refreshToken(generateToken(user, refreshExpiry))
                .expiresAt(accessExpiry.getTime())
                .build();
    }

    @Override
    public UserResponse getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avatar(user.getAvatarUrl())
                .role(user.getRole())
                .build();
    }

    private String generateToken(User user, Date expiryTime) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("quynhAdmin")
                .issueTime(new Date())
                .expirationTime(expiryTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("email", user.getEmail())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SignerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SignerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!verified) throw new AppException(ErrorCode.INVALID_TOKEN);

        if (Objects.isNull(expiryTime) || !expiryTime.after(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.TOKEN_ALREADY_INVALIDATED);

        return signedJWT;
    }

    private AuthUserResponse toAuthUserResponse(User user) {
        return AuthUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .avatar(user.getAvatarUrl())
                .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!Objects.isNull(user.getRole()))
            stringJoiner.add("ROLE_" + user.getRole().name());
        return stringJoiner.toString();
    }
}
