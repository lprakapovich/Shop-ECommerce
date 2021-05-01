package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import model.product.book.Book;
import model.product.book.Genre;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookService extends ProductService<Book> {

    public BookService(MongoCollection<Book> collection) {
        super(collection);
    }

    @Override
    public String create(Book book) {
        validateBook(book);
        return super.create(book);
    }

    // TODO check whether a book with the same name and author exists
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
        return null;
    }

    public List<Book> findByAuthor(String author) {
        return null;
    }
}
