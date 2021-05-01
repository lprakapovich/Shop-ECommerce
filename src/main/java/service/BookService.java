package service;

import com.mongodb.client.MongoCollection;
import model.product.book.Book;
import model.product.book.Genre;

import java.util.List;

public class BookService extends ProductService<Book> {

    public BookService(MongoCollection<Book> collection) {
        super(collection);
    }

    public List<Book> findByGenre(Genre genre) {
        return null;
    }

    public List<Book> findByAuthor(String author) {
        return null;
    }
}
