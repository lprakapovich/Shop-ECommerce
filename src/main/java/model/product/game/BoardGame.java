package model.product.game;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.product.Product;

@Data
@EqualsAndHashCode(callSuper = true)
public class BoardGame extends Product {
    private GameCategory category;
}
