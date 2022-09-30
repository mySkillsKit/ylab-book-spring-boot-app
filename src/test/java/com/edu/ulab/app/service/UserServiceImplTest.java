package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;
    @Mock
    BookRepository bookRepository;

    @Mock
    UserMapper userMapper;

    private Person personFromRepo;

    @BeforeEach
    void setUp() {
        personFromRepo = Person.builder()
                .id(1001L).age(55).fullName("default uer")
                .title("reader").build();
    }

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void createPerson_Test() {
        //given
        UserDto userDto = UserDto.builder().age(11)
                .fullName("test name").title("test title").build();

        Person person = Person.builder().age(11)
                .fullName("test name").title("test title").build();

        Person savedPerson = Person.builder().id(1L).age(11)
                .fullName("test name").title("test title").build();

        UserDto result = UserDto.builder().id(1L).age(11)
                .fullName("test name").title("test title").build();
        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    @Test
    @DisplayName("Обновить пользователя. Должно пройти успешно.")
    void updatePerson_Test() {
        //given
        UserDto userDto = UserDto.builder().age(30)
                .fullName("default uer").title("test title").build();

        Person person = Person.builder().age(30)
                .fullName("default uer").title("test title").build();

        personFromRepo = Person.builder().id(1001L).age(55)
                .fullName("default uer").title("reader").build();

        Person updateUser = Person.builder().id(1001L).age(30)
                .fullName("test newName").title("test title").build();

        UserDto result = UserDto.builder().id(1001L).age(30).
                fullName("test newName").title("test title").build();
        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.findByFullName(person.getFullName())).thenReturn(personFromRepo);
        when(userRepository.save(person)).thenReturn(updateUser);
        when(userMapper.personToUserDto(updateUser)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.updateUser(userDto);
        assertEquals(30, userDtoResult.getAge());
        assertEquals("test newName", userDtoResult.getFullName());
        assertEquals("test title", userDtoResult.getTitle());

    }

    @Test
    @DisplayName("Получить пользователя по id. " +
            "Должно пройти успешно.")
    void getPersonById_Test() {
        //given
        personFromRepo = Person.builder()
                .id(1001L).age(55).fullName("default uer")
                .title("reader").build();

        UserDto result = UserDto.builder()
                .id(1001L).age(55).fullName("default uer")
                .title("reader").build();
        //when
        when(userRepository.findById(1001L)).thenReturn(Optional.of(personFromRepo));
        when(userMapper.personToUserDto(personFromRepo)).thenReturn(result);
        //then

        UserDto userDtoResult = userService.getUserById(1001L);
        assertEquals("default uer", userDtoResult.getFullName());
    }

    @Test
    @DisplayName("Удалить пользователя по id. " +
            "Должно пройти успешно.")
    void deletePersonById_Test() {
        //given
        personFromRepo = Person.builder()
                .id(1001L).age(55).fullName("default uer")
                .title("reader").build();

        //when
        when(userRepository.findById(1001L)).thenReturn(Optional.of(personFromRepo));
        //then
        userService.deleteUserById(1001L);
        assertEquals(0, userRepository.count());
        verify(userRepository, times(1)).findById(1001L);
        verify(userRepository, times(1)).deleteById(1001L);
    }

    @Test
    @DisplayName("Получить книги у пользователя по id. " +
            "Должно пройти успешно.")
    void getUserBooksById_Test() {
        //given
        List<Long> bookListId = List.of(2002L, 3003L);

        //when
        when(userRepository.getUserBooks(1001L)).thenReturn(bookListId);

        //then
        List<Long> userBooks = userService.getUserBooks(1001L);
        assertEquals(bookListId, userBooks);
    }

    @Test
    @DisplayName("Проверка исключения." +
            "Получить не существующего пользователя по id.")
    public void whenExceptionThrown_thenAssertionSucceeds() {
        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.getUserById(200L);
        });

        String expectedMessage = "User not found by userId: 200";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
