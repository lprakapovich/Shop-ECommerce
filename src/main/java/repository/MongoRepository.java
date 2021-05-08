package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import lombok.AllArgsConstructor;
import model.DBObject;
import model.product.Product;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static util.Constants.DATABASE_ID;

// TODO refactor getting by id (wither string or ObjectId)
@AllArgsConstructor
public class MongoRepository<T extends DBObject> {

    protected final MongoCollection<T> collection;

    public String create(T t) {
        return collection.insertOne(t).getInsertedId().asObjectId().getValue().toString();
    }

    public T delete(String id) {
        return collection.findOneAndDelete(eq(DATABASE_ID, new ObjectId(id)));
    }

    public List<T> get(ObjectId id) { return collection.find(eq(DATABASE_ID, id)).into(new ArrayList<>()); }

    public List<T> get(String id) {
       return collection.find(eq(DATABASE_ID, new ObjectId(id))).into(new ArrayList<>());
    }

    public List<T> get(List<String> ids) {
        return collection.find(in(DATABASE_ID, ids)).into(new ArrayList<>());
    }

    public List<T> find(Bson query) { return collection.find(query).into(new ArrayList<>()); }

    public T update(T t) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);
        return collection.findOneAndReplace(eq(DATABASE_ID, t.getId()), t, options);
    }

    public boolean exists(Bson query) {
        return collection.find(query).first() != null;
    }

    public boolean existsById(ObjectId id) {
        return collection.find(eq(DATABASE_ID, id)).first() != null;
    }

    public boolean existsByFieldValue(String field, String value) {
        return collection.find(eq(field, value)).first() != null;
    }

    public T update(ObjectId id, String field, Object value) {
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);
        return collection.findOneAndUpdate(eq(DATABASE_ID, id), Updates.set(field, value), options);
    }
}
