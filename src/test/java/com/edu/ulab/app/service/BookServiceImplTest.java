package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;
    @Mock
    UserRepository userRepository;

    @Mock
    BookMapper bookMapper;

    private Person person;
    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        person = Person.builder()
                .id(1L).build();
        bookDto = BookDto.builder()
                .title("test title").author("test author")
                .pageCount(1000).userId(1L)
                .build();
        book = Book.builder()
                .title("test title").author("test author")
                .pageCount(1000).person(person)
                .build();
    }

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Book savedBook = Book.builder()
                .id(1L).title("test title").author("test author")
                .pageCount(1000).person(person).build();

        BookDto result = BookDto.builder()
                .id(1L).title("test title").author("test author")
                .pageCount(1000).userId(1L).build();

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(userRepository.findById(1L)).thenReturn(Optional.of(person));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Обновить книги. Должно пройти успешно.")
    void updateBook_Test() {
        //given
        Book updateBook = Book.builder()
                .id(1L).title("test title").author("test author")
                .pageCount(1000).person(person).build();

        BookDto result = BookDto.builder()
                .id(1L).title("test newTitle").author("test newAuthor")
                .pageCount(300).userId(1L).build();

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(userRepository.findById(1L)).thenReturn(Optional.of(person));
        when(bookRepository.save(book)).thenReturn(updateBook);
        when(bookMapper.bookToBookDto(updateBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.updateBook(bookDto);
        assertEquals(300, bookDtoResult.getPageCount());
        assertEquals("test newTitle", bookDtoResult.getTitle());

    }

    @Test
    @DisplayName("Получить книгу по id. Должно пройти успешно.")
    void getBook_Test() {
        //given
        Book bookFromRepo = Book.builder()
                .id(1L).title("test title").author("test author")
                .pageCount(1000).person(person).build();
        BookDto result = BookDto.builder()
                .id(1L).title("test title").author("test author")
                .pageCount(1000).userId(1L).build();
        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookFromRepo));
        when(bookMapper.bookToBookDto(bookFromRepo)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.getBookById(1L);
        assertEquals("test title", bookDtoResult.getTitle());
        assertEquals("test author", bookDtoResult.getAuthor());

    }

    @Test
    @DisplayName("Удалить книгу по id. Должно пройти успешно.")
    void deleteBook_Test() {
        //given
        Book bookFromRepo = Book.builder()
                .id(1L).title("test title").author("test author")
                .pageCount(1000).person(person).build();

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookFromRepo));

        //then
        bookService.deleteBookById(1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).deleteById(1L);

    }

    @Test
    @DisplayName("Проверка исключения." +
            "Получить не существующую книгу по id.")
    public void whenExceptionThrown_thenAssertionSucceeds() {
        Exception exception = assertThrows(NotFoundException.class, () -> {
            bookService.getBookById(120L);
        });

        String expectedMessage = "Book not found by id: 120";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
