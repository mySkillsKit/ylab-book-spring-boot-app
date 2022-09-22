package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final Storage storage;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
        User user = storage.saveUser(userMapper.toUser(userDto));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = storage.updateUser(userMapper.toUser(userDto));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        if (storage.getUserById(id).isEmpty()) {
            throw new NotFoundException("The user is not found in Storage" +
                    " {userId:" + id + "}");
        }
        return userMapper.toDto(storage.getUserById(id).get());
    }

    @Override
    public void deleteUserById(Long id) {
        storage.removeUserById(id);
    }

    @Override
    public List<Long> getUserBooks(Long id) {
        return storage.getUserBooks(id);
    }
}
