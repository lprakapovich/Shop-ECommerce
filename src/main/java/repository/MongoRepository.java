package repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import exception.NonUniqueQueryResultException;
import exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import model.DBObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static api.Message.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static util.Constants.DATABASE_ID;

@AllArgsConstructor
public class MongoRepository<T extends DBObject> {

    protected final MongoCollection<T> collection;

    public String create(T t) {
        return collection.insertOne(t).getInsertedId().asObjectId().getValue().toString();
    }

    public T delete(String id) {
        return collection.findOneAndDelete(eq("_id", new ObjectId(id)));
    }

    public List<T> get(String id) {
       return collection.find(eq("_id", new ObjectId(id))).into(new ArrayList<>());
    }

    public List<T> get(List<String> ids) {
        return collection.find(in(DATABASE_ID, ids)).into(new ArrayList<>());
    }

    public T update(T t) {
        return collection.findOneAndReplace(eq("_id", t.getId()), t);
    }

    public List<T> findByFieldValue(String field, Object value) {
        return collection.find(eq(field, value)).into(new ArrayList<>());
    }

    public List<T> findByFieldValues(Map<String, String> queryParams) {
        ArrayList<T> items = collection.find(generateQuery(queryParams)).into(new ArrayList<>());
        if (items.isEmpty()) {
            throw new ResourceNotFoundException(QUERY_NO_RESULT);
        }
        return items;
    }

    public boolean existsByFieldValue(String field, String value) {
        return collection.find(eq(field, value)).first() != null;
    }

    public boolean existsByFieldValues(Map<String, String> queryParams) {
        return collection.find(generateQuery(queryParams)).first() != null;
    }

    private Document generateQuery(Map<String, String> queryParams) {
        Document query = new Document();
        queryParams.keySet().forEach(key -> query.append(key, queryParams.get(key)));
        return query;
    }
}
