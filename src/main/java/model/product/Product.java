package model.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import model.DBObject;
import model.product.book.Book;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Book.class, name = "book")})
@BsonDiscriminator
public abstract class Product extends DBObject {

    private String name;
    private double price;
    private int availableQuantity;

    public Product(String name, double price, int quantity) {
        super();
        this.name = name;
        this.price = price;
        this.availableQuantity = quantity;
    }
}
