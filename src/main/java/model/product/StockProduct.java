package model.product;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Additional data about the quantity available for customers, stored in products db
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StockProduct extends Product {
    private int availableQuantity;
}

