package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.NonUniqueQueryResultException;
import exception.ResourceNotFoundException;
import model.product.Product;
import repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<T> items = productRepository.get(bookId);
        validateSingletonList(items);
        return items.get(0);
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

    public List<T> find(Map<String, String> criteria) {
        List<T> products = productRepository.findByFieldValues(criteria);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException(PRODUCTS_NOT_FOUND);
        }
        return products;
    }

    private void validateProduct(T t) {
        if (t.getQuantity() <= 0 || t.getPrice() <= 0) {
            throw new BadRequestException(INVALID_PRODUCT);
        }
    }

    private void validateSingletonList(List<?> items) {
        if (items.isEmpty()) {
            throw new ResourceNotFoundException(ITEM_NOT_FOUND);
        } else if (items.size() > 1) {
            throw new NonUniqueQueryResultException(NON_UNIQUE_RESULT);
        }
    }
}
