package quynh.ecommerce.moonshop.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.exception.AppException;
import quynh.ecommerce.moonshop.user.dto.request.ChangePasswordRequest;
import quynh.ecommerce.moonshop.user.dto.request.CreateUserRequest;
import quynh.ecommerce.moonshop.user.dto.request.PatchUserRequest;
import quynh.ecommerce.moonshop.user.dto.request.UpdateUserRequest;
import quynh.ecommerce.moonshop.user.dto.response.CreateUserResponse;
import quynh.ecommerce.moonshop.user.dto.response.UpdateUserResponse;
import quynh.ecommerce.moonshop.user.dto.response.UserResponse;
import quynh.ecommerce.moonshop.user.entity.User;
import quynh.ecommerce.moonshop.user.mapper.UserMapper;
import quynh.ecommerce.moonshop.user.repository.UserRepository;
import quynh.ecommerce.moonshop.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponse register(CreateUserRequest createUserRequest) {
        boolean isExists = userRepository.existsByEmail(createUserRequest.getEmail());

        if(isExists){
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        userRepository.save(user);
        CreateUserResponse createUserResponse = userMapper.toCreateUserResponse(user);
        return createUserResponse;
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponseList = userMapper.toListUserResponse(userList);
        return userResponseList;
    }

    @Override
    public UserResponse getCurrentProfile() {
        User currentUser = getAuthenticatedUser();
        return userMapper.toUserResponse(currentUser);
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
        User user = getAuthenticatedUser();

        validateEmailAvailableForCurrentUser(updateUserRequest.getEmail(), user);

        userMapper.updateUserFromRequest(updateUserRequest, user);

        User savedUser = userRepository.save(user);
        return userMapper.toUpdateUserResponse(savedUser);
    }

    @Override
    public UpdateUserResponse patchUser(PatchUserRequest patchUserRequest) {
        User user = getAuthenticatedUser();

        if (patchUserRequest.getEmail() != null) {
            String newEmail = patchUserRequest.getEmail().trim().toLowerCase();

            if (!user.getEmail().equalsIgnoreCase(newEmail)
                    && userRepository.existsByEmail(newEmail)) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }

            user.setEmail(newEmail);
        }

        if (patchUserRequest.getFullName() != null) {
            user.setFullName(patchUserRequest.getFullName());
        }

        if (patchUserRequest.getPhone() != null) {
            user.setPhone(patchUserRequest.getPhone());
        }

        if (patchUserRequest.getAddress() != null) {
            user.setAddress(patchUserRequest.getAddress());
        }

        if (patchUserRequest.getAvatar() != null) {
            user.setAvatarUrl(patchUserRequest.getAvatar());
        }

        if (patchUserRequest.getAvatarPublicId() != null) {
            user.setAvatarPublicId(patchUserRequest.getAvatarPublicId());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toUpdateUserResponse(savedUser);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User currentUser = getAuthenticatedUser();
        boolean matched = passwordEncoder.matches(changePasswordRequest.getCurrent(), currentUser.getPassword());
        if(!matched){
            throw new AppException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        currentUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNext()));
        userRepository.save(currentUser);
    }

    private User getAuthenticatedUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateEmailAvailableForCurrentUser(String email, User user) {
        if (email != null && !user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
