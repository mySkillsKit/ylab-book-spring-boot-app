package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImplTemplate implements BookService {

    private final JdbcTemplate jdbcTemplate;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        final String INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        return ps;
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        if (bookDto.getId() == null) {
            createBook(bookDto);
        } else {
            Book book = bookMapper.bookDtoToBook(bookDto);
            log.info("Book to update: {}", book);
            final String UPDATE_BOOK = "UPDATE BOOK SET TITLE=?, AUTHOR=?, PAGE_COUNT=?, USER_ID=? WHERE ID=?";
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps =
                                    connection.prepareStatement(UPDATE_BOOK, new String[]{"id"});
                            ps.setString(1, bookDto.getTitle());
                            ps.setString(2, bookDto.getAuthor());
                            ps.setLong(3, bookDto.getPageCount());
                            ps.setLong(4, bookDto.getUserId());
                            ps.setLong(5, bookDto.getId());
                            return ps;
                        }
                    });
        }
        return bookDto;
    }


    @Override
    public BookDto getBookById(Long id) {
        final String GET_BOOK = "SELECT * FROM BOOK WHERE ID = ?";
        Book getBook;
        try {
            getBook = jdbcTemplate
                    .queryForObject(GET_BOOK, new BeanPropertyRowMapper<>(Book.class), id);
        } catch (EmptyResultDataAccessException empty) {
            log.error(empty.getMessage(), empty);
            throw new NotFoundException("Book not found by id: " + id);
        }
        log.info("Get book by id : {}", getBook);
        return bookMapper.bookToBookDto(getBook);
    }

    @Override
    public void deleteBookById(Long id) {
        BookDto deletebookById = getBookById(id);
        log.info("Delete book by id: {}", deletebookById);
        final String DELETE_BOOK = "DELETE FROM BOOK WHERE ID = ?";
        jdbcTemplate.update(DELETE_BOOK, id);
    }
}
