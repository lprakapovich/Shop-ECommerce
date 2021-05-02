package model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.product.Product;
import model.product.book.Book;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedItem {
    // TODO: change to Product whet polymorphic deserialization is fixed
    private Product product;
    private int orderedQuantity;
}
