package model.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import model.DBObject;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@BsonDiscriminator(key = Product.DISCRIMINATOR_KEY)

public abstract class Product extends DBObject {

    public static final String DISCRIMINATOR_KEY = "type";

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
