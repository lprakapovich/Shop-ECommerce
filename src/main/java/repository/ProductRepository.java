package repository;

import com.mongodb.client.MongoCollection;
import model.product.Product;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class ProductRepository<T extends Product> extends MongoRepository<T> {

    public ProductRepository(MongoCollection<T> collection) {
        super(collection);
    }

    public List<T> findByPriceRange(double max, double min) {
        return collection.find(and(gte("price", min), lte("price", max))).into(new ArrayList<>());
    }

    public List<T> findByPriceHigherThan(double min) {
        return collection.find((gte("price", min))).into(new ArrayList<>());
    }

    public List<T> findByPriceLowerThan(double max) {
        return collection.find((lte("price", max))).into(new ArrayList<>());
    }
}

