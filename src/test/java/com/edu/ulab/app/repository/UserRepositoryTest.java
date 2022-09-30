package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        Person person = Person.builder()
                .id(100L).age(25).title("reader test")
                .fullName("Test Test").build();
        //When
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(25);
        assertThat(result.getTitle()).isEqualTo("reader test");
        assertThat(result.getFullName()).isEqualTo("Test Test");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {
        //Given
        //Person - values (1001, 'default uer', 'reader', 55);
        //When
        Person personFromRepo = userRepository.findById(1001L).get();
        personFromRepo.setAge(60);
        personFromRepo.setTitle("reader test");
        personFromRepo.setFullName("Test Test");

        Person result = userRepository.save(personFromRepo);

        //Then
        assertThat(result.getAge()).isEqualTo(60);
        assertThat(result.getTitle()).isEqualTo("reader test");
        assertThat(result.getFullName()).isEqualTo("Test Test");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Найти юзера по id. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getPersonById_thenAssertDmlCount() {
        //Given
        //Person - values (1001, 'default uer', 'reader', 55);
        //When
        Person result = userRepository.findById(1001L).get();
        //Then
        assertThat(result).isNotNull();
        assertThat(result.getAge()).isEqualTo(55);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить юзера по id. Число select должно равняться 5")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePersonById_thenAssertDmlCount() {
        //Given
        //Person - values (1001, 'default uer', 'reader', 55);
        //When
        List<Long> userBooks = userRepository.getUserBooks(1001L);
        userBooks.forEach(bookRepository::deleteById);
        userRepository.deleteById(1001L);
        //Then
        assertThat(userRepository.count()).isEqualTo(0);
        assertSelectCount(5);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(3);
    }

    @Test
    @DisplayName("При удалении пользователя" +
            " необходимо удалить также книги")
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"})
    public void whenException_thenAssertionSucceeds() {

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.deleteById(1001L);
            assertThat(userRepository.count()).isEqualTo(0);
        });

        String expectedMessage = "could not execute statement; SQL";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
