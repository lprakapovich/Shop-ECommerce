package model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Base class with all the general information about the product
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    protected String id;
    protected String sku;
    protected String slug;
    protected String name;
    protected String description;
    protected double price;
    protected Set<String> categoryIds;
}
