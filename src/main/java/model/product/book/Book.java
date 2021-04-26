package model.product.book;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.product.Product;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends Product {
    private String author;
    private Genre genre;
    private String isbn;
    private Date publishDate;
}
