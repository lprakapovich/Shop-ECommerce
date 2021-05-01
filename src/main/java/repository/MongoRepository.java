package repository;

import com.mongodb.client.MongoCollection;
import exception.NonUniqueQueryResultException;
import exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static api.Message.ITEM_NOT_FOUND;
import static api.Message.NON_UNIQUE_RESULT;
import static com.mongodb.client.model.Filters.eq;

@AllArgsConstructor
public class MongoRepository<T> {

    protected final MongoCollection<T> collection;

    public String create(T t) {
        return collection.insertOne(t).getInsertedId().asObjectId().getValue().toString();
    }

    public void delete(String id) {
        T item = collection.findOneAndDelete(eq("_id", new ObjectId(id)));
        if (item == null) {
            throw new ResourceNotFoundException(ITEM_NOT_FOUND);
        }
    }

    public T get(String id) {
        List<T> products = collection.find(eq("_id", new ObjectId(id))).into(new ArrayList<>());
        if (products.isEmpty()) {
            throw new ResourceNotFoundException(ITEM_NOT_FOUND);
        } else if (products.size() > 1) {
            throw new NonUniqueQueryResultException(NON_UNIQUE_RESULT);
        }
        return products.get(0);
    }

    public T update(T t) {
        return null;
    }

    public boolean existsByFieldValue(String field, String value) {
        return collection.find(eq(field, value)).first() != null;
    }
}
