package model.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import model.DBObject;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Product extends DBObject {

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
