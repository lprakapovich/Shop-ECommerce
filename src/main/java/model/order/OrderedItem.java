package model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.product.Product;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@BsonDiscriminator
public class OrderedItem {

     // Bson is a binary-encoded serialization of JSON-like documents
    @BsonProperty(useDiscriminator = true)
    private Product product;
    private int orderedQuantity;
}
