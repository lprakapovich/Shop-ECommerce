package model.product.book;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.product.Product;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Book extends Product {

    private String author;
    private Genre genre;

    public Book(String name, double price,  int quantity, String author, Genre genre) {
        super(name, price, quantity);
        this.author = author;
        this.genre = genre;
    }
}
