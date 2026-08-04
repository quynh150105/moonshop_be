package quynh.ecommerce.moonshop.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;
import quynh.ecommerce.moonshop.user.dto.request.ChangePasswordRequest;
import quynh.ecommerce.moonshop.user.dto.request.PatchUserRequest;
import quynh.ecommerce.moonshop.user.dto.request.UpdateUserRequest;
import quynh.ecommerce.moonshop.user.dto.response.UpdateUserResponse;
import quynh.ecommerce.moonshop.user.dto.response.UserResponse;
import quynh.ecommerce.moonshop.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestApiV1
public class UserController {
    private final UserService userService;

    @GetMapping(UrlConstant.User.GET_PROFILE)
    public ResponseEntity<UserResponse> getProfile(){
        return ResponseEntity.ok(userService.getCurrentProfile());
    }

    @PutMapping(UrlConstant.User.UPDATE_PROFILE)
    public ResponseEntity<UpdateUserResponse> updateProfile(@Valid @RequestBody UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok(userService.updateUser(updateUserRequest));
    }

    @PatchMapping(UrlConstant.User.UPDATE_PROFILE)
    public ResponseEntity<UpdateUserResponse> patchProfile(@Valid @RequestBody PatchUserRequest patchUserRequest){
        return ResponseEntity.ok(userService.patchUser(patchUserRequest));
    }

    @PostMapping(UrlConstant.User.CHANGE_PASSWORD)
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.noContent().build();
    }
}
