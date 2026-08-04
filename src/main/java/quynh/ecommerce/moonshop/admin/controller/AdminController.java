package quynh.ecommerce.moonshop.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import quynh.ecommerce.moonshop.auth.dto.request.LoginRequest;
import quynh.ecommerce.moonshop.auth.dto.response.LoginResponse;
import quynh.ecommerce.moonshop.auth.service.AuthService;
import quynh.ecommerce.moonshop.common.ApiResponse;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;
import quynh.ecommerce.moonshop.user.dto.response.UserResponse;
import quynh.ecommerce.moonshop.user.service.UserService;

import java.util.List;

@RestApiV1
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping(UrlConstant.Admin.LOGIN)
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Admin Login successful")
                        .data(authService.login(loginRequest))
                        .build()
        );
    }

    @GetMapping(UrlConstant.Admin.ALL_USER)
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.ok(userService.getAllUser());
    }

}
