package quynh.ecommerce.moonshop.user.service;

import quynh.ecommerce.moonshop.user.dto.request.ChangePasswordRequest;
import quynh.ecommerce.moonshop.user.dto.request.CreateUserRequest;
import quynh.ecommerce.moonshop.user.dto.request.PatchUserRequest;
import quynh.ecommerce.moonshop.user.dto.request.UpdateUserRequest;
import quynh.ecommerce.moonshop.user.dto.response.CreateUserResponse;
import quynh.ecommerce.moonshop.user.dto.response.UpdateUserResponse;
import quynh.ecommerce.moonshop.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    CreateUserResponse register(CreateUserRequest createUserRequest);
    List<UserResponse> getAllUser();
    UserResponse getCurrentProfile();
    UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);
    UpdateUserResponse patchUser(PatchUserRequest patchUserRequest);
    void changePassword(ChangePasswordRequest changePasswordRequest);
}
