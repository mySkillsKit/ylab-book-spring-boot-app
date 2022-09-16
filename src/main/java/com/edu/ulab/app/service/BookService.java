package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

public interface BookService {
    BookDto createBook(BookDto userDto);

    BookDto updateBook(BookDto userDto);

    BookDto getBookById(Long id);

    void deleteBookById(Long id);
}
