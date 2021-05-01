package service;

import com.mongodb.client.MongoCollection;
import exception.ResourceNotFoundException;
import model.product.Product;
import repository.ProductRepository;

import java.util.List;

public class ProductService <T extends Product> {

    private final ProductRepository<T> productRepository;

    public ProductService(MongoCollection<T> collection) {
        this.productRepository = new ProductRepository<>(collection);
    }

    public String create(T t) {
        return null;
    }

    public T get(String bookId) {

        return null;
    }

    public void delete(String id) {
    }

    public List<T> findByPriceInRange(double max, double min) {
        List<T> products = productRepository.findByPriceRange(max, min);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Products in a given price range could not be found");
        }
        return products;
    }

    public List<T> findByPriceHigherThan(double min) {
        List<T> products = productRepository.findByPriceHigherThan(min);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Products in a given price range could not be found");
        }
        return products;
    }

    public List<T> findByPriceLowerThan(double max) {
        List<T> products = productRepository.findByPriceLowerThan(max);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Products in a given price range could not be found");
        }
        return products;
    }
}
