package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor

public class BookServiceImpl implements BookService {

    private final Storage storage;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = storage.saveBook(bookMapper.toBook(bookDto));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = storage.updateBook(bookMapper.toBook(bookDto));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto getBookById(Long id) {

        if (storage.getBookById(id).isEmpty()) {
            throw new NotFoundException("The book is not found in Storage" +
                    " {bookId:" + id + "}");
        }
        return bookMapper.toDto(storage.getBookById(id).get());
    }

    @Override
    public void deleteBookById(Long id) {
        storage.removeBookById(id);

    }
}
