package quynh.ecommerce.moonshop.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import quynh.ecommerce.moonshop.auth.dto.request.LoginRequest;
import quynh.ecommerce.moonshop.auth.dto.request.LogoutRequest;
import quynh.ecommerce.moonshop.auth.dto.request.RefreshTokenRequest;
import quynh.ecommerce.moonshop.auth.dto.response.LoginResponse;
import quynh.ecommerce.moonshop.auth.dto.response.RefreshTokenResponse;
import quynh.ecommerce.moonshop.auth.service.AuthService;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;
import quynh.ecommerce.moonshop.user.dto.request.CreateUserRequest;
import quynh.ecommerce.moonshop.user.dto.response.CreateUserResponse;
import quynh.ecommerce.moonshop.user.dto.response.UserResponse;
import quynh.ecommerce.moonshop.user.service.UserService;

@RequiredArgsConstructor
@RestApiV1
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping(UrlConstant.Auth.REGISTER)
    public ResponseEntity<CreateUserResponse> register(@Valid @RequestBody CreateUserRequest createUserRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(createUserRequest));
    }

    @PostMapping(UrlConstant.Auth.LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(UrlConstant.Auth.LOGOUT)
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest logoutRequest){
        authService.logout(logoutRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(UrlConstant.Auth.REFRESH_TOKEN)
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @GetMapping(UrlConstant.Auth.ME)
    public ResponseEntity<UserResponse> me(){
        return ResponseEntity.ok(authService.getCurrentUser());
    }

}
