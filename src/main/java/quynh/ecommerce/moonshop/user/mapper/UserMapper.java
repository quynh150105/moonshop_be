package quynh.ecommerce.moonshop.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import quynh.ecommerce.moonshop.user.dto.request.CreateUserRequest;
import quynh.ecommerce.moonshop.user.dto.request.PatchUserRequest;
import quynh.ecommerce.moonshop.user.dto.request.UpdateUserRequest;
import quynh.ecommerce.moonshop.user.dto.response.CreateUserResponse;
import quynh.ecommerce.moonshop.user.dto.response.UpdateUserResponse;
import quynh.ecommerce.moonshop.user.dto.response.UserResponse;
import quynh.ecommerce.moonshop.user.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "avatar", source = "avatarUrl")
    CreateUserResponse toCreateUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "avatarPublicId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(CreateUserRequest request);

    @Mapping(target = "avatar", source = "avatarUrl")
    UserResponse toUserResponse(User user);

    @Mapping(target = "avatar", source = "avatarUrl")
    UpdateUserResponse toUpdateUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "avatarUrl", source = "avatar")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "avatarUrl", source = "avatar")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void patchUserFromRequest(PatchUserRequest request, @MappingTarget User user);

    List<UserResponse> toListUserResponse(List<User> users);
}
