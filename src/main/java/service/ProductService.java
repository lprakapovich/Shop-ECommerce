package service;

import com.mongodb.client.MongoCollection;
import exception.NonUniqueQueryResultException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import model.product.Product;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

@RequiredArgsConstructor
public class ProductService {

    protected final MongoCollection<Product> collection;

    public ObjectId create(Product product) {
        collection.insertOne(product);

        System.out.println("Inserted object id: " + product.getId().toString());
        return product.getId();
    }

    public Product getProductById(String id) {

        List<Product> products = collection.find(eq("_id", new ObjectId(id))).into(new ArrayList<>());

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        } else if (products.size() > 1) {
            throw new NonUniqueQueryResultException("More than one product matching this id found");
        }
        return products.get(0);
    }

    public void delete(String id) {
        Product deletedProduct = collection.findOneAndDelete(eq("_id", new ObjectId(id)));
        if (deletedProduct == null) {
            throw new ResourceNotFoundException("Product could not be found");
        }
    }

    public List<Product> findByPriceInRange(double max, double min) {
        List<Product> products = collection.find(and(gte("price", min), lte("price", max))).into(new ArrayList<>());
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Products in a given price range could not be found");
        }
        return products;
    }

    public List<Product> findByPriceHigherThan(double min) {
        List<Product> products = collection.find((gte("price", min))).into(new ArrayList<>());
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Products in a given price range could not be found");
        }
        return products;
    }

    public List<Product> findByPriceLowerThan(double max) {

        return null;
    }
}
