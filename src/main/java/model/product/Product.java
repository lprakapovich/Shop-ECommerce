package model.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class Product {

    @BsonProperty("_id")
    private ObjectId id;
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
