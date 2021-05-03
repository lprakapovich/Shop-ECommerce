package repository;

import com.mongodb.client.MongoCollection;
import lombok.AllArgsConstructor;
import model.DBObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return collection.findOneAndDelete(eq(DATABASE_ID, new ObjectId(id)));
    }

    public List<T> get(String id) {
       return collection.find(eq(DATABASE_ID, new ObjectId(id))).into(new ArrayList<>());
    }

    public List<T> get(List<String> ids) {
        return collection.find(in(DATABASE_ID, ids)).into(new ArrayList<>());
    }

    public List<T> find(Bson query) { return collection.find(query).into(new ArrayList<>()); }

    public T update(T t) {
        return collection.findOneAndReplace(eq(DATABASE_ID, t.getId()), t);
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
