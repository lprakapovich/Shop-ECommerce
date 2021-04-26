package model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.product.Product;

@Data
@AllArgsConstructor
public class OrderedItem {
    private Product product;
    private int orderedQuantity;
}
