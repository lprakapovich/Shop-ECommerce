package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.product.Product;
import org.bson.types.ObjectId;
import repository.ProductRepository;
import util.ProductQueryBuilder;

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

    public T get(ObjectId id) {
        return productRepository.get(id);
    }

    public void delete(String id) {
        if (productRepository.delete(id) == null) {
            throw new BadRequestException(ITEM_NOT_FOUND);
        }
    }

    public T update(T t) {
        validateProduct(t);
        T updated = productRepository.update(t);
        if (updated == null) {
            throw new BadRequestException(ITEM_NOT_FOUND);
        }
        return updated;
    }

    public T update(ObjectId id, String field, Object value) {
        return productRepository.update(id, field, value);
    }

    public List<T> find(Map<String, List<String>> criteria) {
        List<T> products = productRepository.find(ProductQueryBuilder.buildQuery(criteria));
        if (products.isEmpty()) {
            throw new ResourceNotFoundException(PRODUCTS_NOT_FOUND);
        }
        return products;
    }

    public boolean existsById(ObjectId id) {
        return productRepository.existsById(id);
    }

    private void validateProduct(T t) {
        if (t.getAvailableQuantity() < 0 || t.getPrice() <= 0) {
            throw new BadRequestException(INVALID_PRODUCT);
        }
    }
}
