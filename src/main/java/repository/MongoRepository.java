package repository;

import com.mongodb.client.MongoCollection;
import exception.NonUniqueQueryResultException;
import exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import model.DBObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static api.Message.ITEM_NOT_FOUND;
import static api.Message.NON_UNIQUE_RESULT;
import static com.mongodb.client.model.Filters.eq;

@AllArgsConstructor
public class MongoRepository<T extends DBObject> {

    protected final MongoCollection<T> collection;

    public String create(T t) {
        return collection.insertOne(t).getInsertedId().asObjectId().getValue().toString();
    }

    public T delete(String id) {
        return collection.findOneAndDelete(eq("_id", new ObjectId(id)));
    }

    // TODO move exceptions to service
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
        return collection.findOneAndReplace(eq("_id", t.getId()), t);
    }

    public boolean existsByFieldValue(String field, String value) {
        return collection.find(eq(field, value)).first() != null;
    }

    public boolean existsByFieldValues(Map<String, String> queryParams) {
        Document query = new Document();
        for (String key: queryParams.keySet()) {
            query.append(key, queryParams.get(key));
        }
        return collection.find(query).first() != null;
    }
}
