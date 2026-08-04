package quynh.ecommerce.moonshop.auth.service;

import quynh.ecommerce.moonshop.auth.dto.request.IntrospectRequest;
import quynh.ecommerce.moonshop.auth.dto.request.LoginRequest;
import quynh.ecommerce.moonshop.auth.dto.request.LogoutRequest;
import quynh.ecommerce.moonshop.auth.dto.request.RefreshTokenRequest;
import quynh.ecommerce.moonshop.auth.dto.response.IntrospectResponse;
import quynh.ecommerce.moonshop.auth.dto.response.LoginResponse;
import quynh.ecommerce.moonshop.auth.dto.response.RefreshTokenResponse;
import quynh.ecommerce.moonshop.user.dto.response.UserResponse;

public interface AuthService {
    IntrospectResponse introspect(IntrospectRequest introspectRequest);
    LoginResponse login(LoginRequest loginRequest);
    void logout(LogoutRequest logoutRequest);
    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    UserResponse getCurrentUser();
}
