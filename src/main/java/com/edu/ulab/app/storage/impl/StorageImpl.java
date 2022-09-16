package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class StorageImpl implements Storage {

    @Override
    public Optional<User> getUserById(Long id) {
        for (User usr : mapUsers.values()) {
            if (Objects.equals(usr.getId(), id)) {
                return Optional.of(usr);
            }
        }
        return Optional.empty();
    }

    @Override
    public User saveUser(User user) {
        String title = user.getTitle();
        user = userInStorageEquals(user);
        user.setTitle(title);

        if (user.getId() != null) {
            throw new NotFoundException("User has already been created." +
                    "This user matches {userId: " + user.getId() + "}");
        } else {
            // if user.getId() == null -> create id
            long id = userCount.incrementAndGet();
            user.setId(id);
            mapUsers.put(id, user);
            mapUserBooks.put(user.getId(), new ArrayList<>());
        }
        return user;
    }


    @Override
    public User updateUser(User user) {

        if (mapUsers.containsKey(user.getId())) {
            mapUsers.put(user.getId(), user);
            mapUserBooks.put(user.getId(), new ArrayList<>());
        } else {
            throw new NotFoundException("The User is not found in Storage" +
                    " {UserId:" + user.getId() + "}");
        }
        return user;

    }

    private User userInStorageEquals(User user) {
        //user search in storage
        for (User entry : mapUsers.values()) {
            if (user.getFullName().equalsIgnoreCase(entry.getFullName())
                    && user.getAge() == entry.getAge()) {
                return entry;
            }
        }
        return user;
    }

    @Override
    public void removeUserById(Long id) {
        if (mapUsers.containsKey(id)) {
            mapUsers.remove(id);
            mapUserBooks.remove(id);
        } else {
            throw new NotFoundException("The user was not found in the storage" +
                    " {UserId: " + id + "}.Please check the id and try again.");
        }
    }


    @Override
    public Optional<Book> getBookById(Long id) {

        for (Book book : mapBooks.values()) {
            if (Objects.equals(book.getId(), id)) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    @Override
    public Book saveBook(Book book) {
        Long userId = book.getUserId();
        book = bookInStorageEquals(book);
        book.setUserId(userId);

        if (book.getId() != null) {
            if ((mapBooks.containsKey(book.getId()))) {
                List<Long> value = mapUserBooks.get(book.getUserId());
                value.add(book.getId());
                mapUserBooks.put(book.getUserId(), value);
            } else {
                throw new NotFoundException("The book is not saved {id:" + book.getId() + "}");
            }
        } else {
            // if book.getId() == null create bookId
            createBookId(book);
        }
        return book;
    }


    @Override
    public Book updateBook(Book book) {

        Long userId = book.getUserId();
        book = bookInStorageEquals(book);
        book.setUserId(userId);

        if (book.getId() != null) {
            if ((mapBooks.containsKey(book.getId()))) {
                List<Long> value = new ArrayList<>(mapUserBooks.get(book.getUserId()));
                value.add(book.getId());
                mapUserBooks.put(book.getUserId(), value);
            } else {
                throw new NotFoundException("The book is not saved {BookId: " + book.getId() + "}");
            }
        } else {
            createBookId(book);
        }
        return book;
    }

    private Book bookInStorageEquals(Book book) {
        //book search in storage
        for (Book entry : mapBooks.values()) {
            if (book.getAuthor().equalsIgnoreCase(entry.getAuthor()) &&
                    book.getTitle().equalsIgnoreCase(entry.getTitle())
                    && book.getPageCount() == entry.getPageCount()) {
                return entry;
            }
        }
        return book;
    }

    private void createBookId(Book book) {
        // if book.getId() == null create bookId
        long id = bookCount.incrementAndGet();
        book.setId(id);
        mapBooks.put(id, book);

        List<Long> value = new ArrayList<>(mapUserBooks.get(book.getUserId()));
        value.add(id);
        mapUserBooks.put(book.getUserId(), value);
    }

    @Override
    public void removeBookById(Long id) {
        if (mapBooks.containsKey(id)) {
            mapBooks.remove(id);
        } else {
            throw new NotFoundException("The book was not found in the storage {BookId: " + id + "}." +
                    "Please check the id and try again.");
        }
    }


    @Override
    public List<Long> getUserBooks(Long id) {
        return mapUserBooks.get(id);
    }

}
