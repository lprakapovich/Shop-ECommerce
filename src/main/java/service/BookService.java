package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import model.product.book.Book;
import util.ProductQueryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static api.Message.BOOK_DUPLICATED_TITLE_AND_AUTHOR;
import static java.util.Collections.singletonList;
import static util.Constants.AUTHOR;
import static util.Constants.NAME;

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
        Map<String, List<String>> params = new HashMap<>();
        params.put(NAME, singletonList(book.getName()));
        params.put(AUTHOR, singletonList(book.getAuthor()));

        if (productRepository.exists(ProductQueryBuilder.buildQuery(params))) {
            throw new BadRequestException(BOOK_DUPLICATED_TITLE_AND_AUTHOR);
        }
    }
}
