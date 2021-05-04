package model.product.book;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.product.Product;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BsonDiscriminator(value = Book.TYPE, key = Product.DISCRIMINATOR_KEY)
public class Book extends Product {

    public static final String TYPE = "book";

    private String author;

    public Book(String name, double price,  int quantity, String author) {
        super(name, price, quantity);
        this.author = author;
    }
}
