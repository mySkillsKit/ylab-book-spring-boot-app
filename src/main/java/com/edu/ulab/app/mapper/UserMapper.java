package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.web.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userRequestToUserDto(UserRequest userRequest);

    UserRequest userDtoToUserRequest(UserDto userDto);

    default UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .title(user.getTitle())
                .age(user.getAge())
                .build();
    }


    default User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .fullName(userDto.getFullName())
                .title(userDto.getTitle())
                .age(userDto.getAge())
                .build();
    }

}
