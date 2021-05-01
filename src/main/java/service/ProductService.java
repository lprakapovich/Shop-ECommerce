package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.product.Product;
import repository.ProductRepository;

import java.util.HashMap;
import java.util.List;

import static api.Message.*;

public class ProductService <T extends Product> {

    protected final ProductRepository<T> productRepository;

    public ProductService(MongoCollection<T> collection) {
        this.productRepository = new ProductRepository<>(collection);
    }

    public String create(T t) {
        validateProduct(t);
        return productRepository.create(t);
    }

    public T get(String bookId) {
        return productRepository.get(bookId);
    }

    public void delete(String id) {
        if (productRepository.delete(id) == null) {
            throw new BadRequestException(ITEM_NOT_FOUND);
        }
    }

    public T update(T t) {
        T updated = productRepository.update(t);
        if (updated == null) {
            throw new BadRequestException(ITEM_NOT_FOUND);
        }
        return updated;
    }

    public List<T> findByCriteria(HashMap<String, String> criteria) {
        List<T> products = productRepository.findByFieldValues(criteria);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException(PRODUCTS_CRITERIA_NOT_FOUND);
        }
        return products;
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

    private void validateProduct(T t) {
        if (t.getQuantity() <= 0 || t.getPrice() <= 0) {
            throw new BadRequestException(INVALID_PRODUCT);
        }
    }
}
