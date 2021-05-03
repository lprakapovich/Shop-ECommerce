package repository;

import com.mongodb.client.MongoCollection;
import model.product.Product;

public class ProductRepository<T extends Product> extends MongoRepository<T> {

    public ProductRepository(MongoCollection<T> collection) {
        super(collection);
    }
}
