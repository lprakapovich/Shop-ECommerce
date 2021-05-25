package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import model.order.Order;
import model.product.book.Book;

import static api.Message.BOOK_DUPLICATED_TITLE_AND_AUTHOR;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static util.Constants.AUTHOR;
import static util.Constants.NAME;

public class BookService extends ProductService<Book> {

    public BookService(MongoCollection<Book> collection, MongoCollection<Order> orderCollection) {
        super(collection, orderCollection);
    }

    @Override
    public String create(Book book) {
        validateBook(book);
        return super.create(book);
    }

    private void validateBook(Book book) {
        if (productRepository.existsByQuery(and(eq(NAME, book.getName()), eq(AUTHOR, book.getAuthor())))) {
            throw new BadRequestException(BOOK_DUPLICATED_TITLE_AND_AUTHOR);
        }
    }
}
