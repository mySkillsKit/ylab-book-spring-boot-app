package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.NoSuchElementException;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу и автора. Число select должно равняться 4")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void saveBook_thenAssertDmlCount() {
        //Given
        Person person = Person.builder()
                .id(100L).age(25).title("reader").fullName("Ivan Ivanov").build();

        Book book = Book.builder()
                .id(202L).person(person).title("The best book")
                .author("author").pageCount(340).build();

        Person savedPerson = userRepository.save(person);
        book.setPerson(savedPerson);

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(340);
        assertThat(result.getTitle()).isEqualTo("The best book");
        assertThat(result.getPerson().getTitle()).isEqualTo("reader");
        assertSelectCount(4);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить книгу и автора. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        //Given
                //book with id = 2002 and id = 3003
                //values (3003, 1001, 'more default book', 'on more author', 6655);

        //When
        Book bookFromRepo = bookRepository.findById(3003L).get();
        bookFromRepo.setTitle("New book");
        bookFromRepo.setPageCount(500);
        bookFromRepo.setAuthor("newAuthor");

        Book result = bookRepository.save(bookFromRepo);
        //Then
        assertThat(result.getPageCount()).isEqualTo(500);
        assertThat(result.getTitle()).isEqualTo("New book");
        assertThat(result.getAuthor()).isEqualTo("newAuthor");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Найти книгу и автора по id. " +
            "Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBookById_thenAssertDmlCount() {
        //Given
            //book with id = 2002 and id = 3003
            //values (2002, 1001, 'default book', 'author', 5500);

        //When
        Book result = bookRepository.findById(2002L).get();

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getPageCount()).isEqualTo(5500);
        assertThat(result.getTitle()).isEqualTo("default book");
        assertThat(result.getAuthor()).isEqualTo("author");
        assertThat(result.getPerson().getId()).isEqualTo(1001);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить книгу и автора по id. " +
            "Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBookById_thenAssertDmlCount() {
        //Given
             //book with id = 2002 and id = 3003
        //When
        bookRepository.deleteById(2002L);

        //Then
        assertThat(bookRepository.count()).isEqualTo(1);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    @Test
    @DisplayName("Проверка исключения." +
            "Найти книгу если нет в Репозитории.")
    public void whenExceptionThrown_thenAssertionSucceeds() {

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            bookRepository.findById(150L).get();
        });
        String expectedMessage = "No value present";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
