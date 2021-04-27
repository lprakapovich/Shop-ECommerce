package service;

import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import model.product.Product;
import org.bson.Document;


@RequiredArgsConstructor
public class ProductService {

    private final MongoCollection<Document> collection;

    public void createProduct(Product product) {
    }
}
