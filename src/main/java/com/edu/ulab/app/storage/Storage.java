package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public interface Storage {
    //создать хранилище в котором будут содержаться данные
    // 43:50 сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранении юзера или книги, должен генерироваться идентификатор
    // продумать что у юзера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают другой тип данных - учесть это в абстракции

    Map<Long, User> mapUsers = new ConcurrentHashMap<>();
    AtomicLong userCount = new AtomicLong(0);

    Map<Long, Book> mapBooks = new ConcurrentHashMap<>();
    AtomicLong bookCount = new AtomicLong(0);

    Map<Long, List<Long>> mapUserBooks = new ConcurrentHashMap<>();


    Optional<User> getUserById(Long id);

    User saveUser(User user);

    void removeUserById(Long id);

    User updateUser(User user);


    Optional<Book> getBookById(Long id);

    Book saveBook(Book book);

    void removeBookById(Long id);

    Book updateBook(Book book);


    List<Long> getUserBooks(Long id);
}
