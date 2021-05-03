package model.product.book;

import lombok.*;
import model.product.Product;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Book extends Product {

    private String author;

    public Book(String name, double price,  int quantity, String author) {
        super(name, price, quantity);
        this.author = author;
    }
}
