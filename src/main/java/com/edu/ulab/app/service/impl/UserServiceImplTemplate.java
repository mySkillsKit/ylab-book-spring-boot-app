package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplTemplate implements UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        UserDto userInRepo = getPersonFromRepo(userDto);
        if (userInRepo != null) {
            throw new NotFoundException("User has already been created." +
                    "This user matches {userId: " + userInRepo.getId() + "}. " +
                    "Change full name!");
        }

        final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Saved user: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        // реализовать недстающие методы
        UserDto userFromRepo = getPersonFromRepo(userDto);
        if (userFromRepo == null) {
            throw new NotFoundException("This User is not found in Database. " +
                    "Check full name");
        }
        userDto.setId(userFromRepo.getId());

        final String INSERT_SQL =
                "UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE = ? WHERE ID = ?";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    ps.setLong(4, userDto.getId());
                    return ps;
                });

        log.info("Updated user: {}", userDto);
        return userDto;
    }

    private UserDto getPersonFromRepo(UserDto userDto) {
        final String USER_IN_REPO =
                "SELECT * FROM PERSON WHERE LOWER(FULL_NAME) = LOWER('" + userDto.getFullName() + "')";
        try {
            Person person = jdbcTemplate.queryForObject(USER_IN_REPO, new BeanPropertyRowMapper<>(Person.class));
            return userMapper.personToUserDto(person);
        } catch (IncorrectResultSizeDataAccessException e) {
            //User not found in Repository
            return null;
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        final String GET_USER = "SELECT * FROM PERSON WHERE ID = ?";
        Person getUser;
        try {
            getUser = jdbcTemplate
                    .queryForObject(GET_USER, new BeanPropertyRowMapper<>(Person.class), id);
        } catch (EmptyResultDataAccessException empty) {
            log.error(empty.getMessage(), empty);
            throw new NotFoundException("User not found by userId: " + id);
        }
        log.info("Get user by id: {}", id);

        return userMapper.personToUserDto(getUser);
    }

    @Override
    public void deleteUserById(Long id) {
        UserDto deleteUserById = getUserById(id);
        log.info("Delete user by id: {}", deleteUserById);
        final String DELETE_USER_ID = "DELETE FROM PERSON WHERE ID = ?";
        jdbcTemplate.update(DELETE_USER_ID, id);
    }


    public List<Long> getUserBooks(Long id) {
        log.info("Get List books user by userId: {}", id);
        final String LIST_BOOK = "SELECT ID FROM BOOK WHERE USER_ID = ?";
        return jdbcTemplate.query(LIST_BOOK,
                new RowMapper<Long>() {
                    public Long mapRow(ResultSet result, int rowNum) throws SQLException {
                        return result.getLong(1);
                    }
                }, id);
    }
}
