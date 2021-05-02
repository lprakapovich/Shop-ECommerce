package model.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import model.DBObject;
import model.product.book.Book;
import model.product.game.BoardGame;

import static com.fasterxml.jackson.annotation.JsonSubTypes.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

// TODO temporary solution to polymorphic deserialization,
//  enforce front to send additional type info with json

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @Type(value = Book.class, name = "book"),
        @Type(value = BoardGame.class, name = "gameBoard")
})
public abstract class Product extends DBObject {

    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        super();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
