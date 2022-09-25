package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);

        Person userFromRepo = userRepository.findByFullName(user.getFullName());
        if (userFromRepo != null) {
            throw new NotFoundException("User has already been created." +
                    "This user matches {userId: " + userFromRepo.getId() + "}. " +
                    "Change full name!");
        }

        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);

        Person userInRepo = userRepository.findByFullName(user.getFullName());
        if (userInRepo == null) {
            throw new NotFoundException("This User is not found in Database. " +
                    "Check full name");
        }
        user.setId(userInRepo.getId());
        Person updateUser = userRepository.save(user);
        log.info("Update user: {}", updateUser);
        return userMapper.personToUserDto(updateUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        Person getUserById = getPersonById(id);
        log.info("Get user by id: {}", getUserById);
        return userMapper.personToUserDto(getUserById);
    }


    @Override
    public void deleteUserById(Long id) {
        Person deleteUser = getPersonById(id);
        log.info("Delete user by id: {}", deleteUser);
        userRepository.deleteById(id);
    }


    private Person getPersonById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found by userId: " + id));
    }

    public List<Long> getUserBooks(Long id) {
        log.info("Get List books user by userId: {}", id);
        return userRepository.getUserBooks(id);
    }
}
