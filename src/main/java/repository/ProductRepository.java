package repository;

import com.mongodb.client.MongoCollection;
import model.product.Product;
import org.bson.conversions.Bson;
import util.ProductQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductRepository<T extends Product> extends MongoRepository<T> {

    public ProductRepository(MongoCollection<T> collection) {
        super(collection);
    }

    @Override
    public List<T> findByFieldValues(Map<String, String> params) {
        Bson bson = ProductQueryBuilder.buildQuery(params);
        return collection.find(bson).into(new ArrayList<>());
    }
}

