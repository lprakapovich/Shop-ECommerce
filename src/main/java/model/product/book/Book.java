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

    public Book(String name, double price,  int quantity, String author) {
        super(name, price, quantity);
        this.author = author;
    }
}
