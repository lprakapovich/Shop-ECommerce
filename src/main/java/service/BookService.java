package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.product.book.Book;
import model.product.book.Genre;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static api.Message.*;

public class BookService extends ProductService<Book> {

    public BookService(MongoCollection<Book> collection) {
        super(collection);
    }

    @Override
    public String create(Book book) {
        validateBook(book);
        return super.create(book);
    }

    private void validateBook(Book book) {
        Map<String, String> params = Stream.of(new String[][] {
                { "name", book.getName() },
                { "author", book.getAuthor() },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        if (productRepository.existsByFieldValues(params)) {
            throw new BadRequestException("Repeated book name / author");
        }
    }

    public List<Book> findByGenre(Genre genre) {
        List<Book> books = productRepository.findByFieldValue("genre", genre);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException(BOOKS_BY_GENRE_NOT_FOUND);
        }
        return books;
    }

    public List<Book> findByAuthor(String author) {
        List<Book> books = productRepository.findByFieldValue("author", author);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException(BOOKS_BY_AUTHOR_NOT_FOUND);
        }
        return books;
    }
}
