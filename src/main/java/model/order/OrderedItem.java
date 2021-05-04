package model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.product.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedItem {

    private Product product;
    private int orderedQuantity;
}
