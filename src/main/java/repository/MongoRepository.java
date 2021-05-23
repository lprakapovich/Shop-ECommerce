package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import lombok.AllArgsConstructor;
import model.DBObject;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static util.Constants.DATABASE_ID;

@AllArgsConstructor
public class MongoRepository<T extends DBObject> {

    protected final MongoCollection<T> collection;

    public String create(T t) {
        return collection.insertOne(t).getInsertedId().asObjectId().getValue().toString();
    }

    public void delete(ObjectId id) {
        collection.deleteOne(eq(DATABASE_ID, id));
    }

    public T delete(String id) {
        return collection.findOneAndDelete(eq(DATABASE_ID, new ObjectId(id)));
    }

    public T get(ObjectId id) {
        return collection.find(eq(DATABASE_ID, id)).first();
    }

    public List<T> getAll() {
        return collection.find().into(new ArrayList<>());
    }

    public List<T> findMany(Bson query) {
        return collection.find(query).into(new ArrayList<>());
    }

    public T findOne(Bson query) {
        return collection.find(query).first();
    }

    public T update(T t) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);
        return collection.findOneAndReplace(eq(DATABASE_ID, t.getId()), t, options);
    }

    public T findOneAndUpdate(ObjectId id, String field, Object value) {
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);
        return collection.findOneAndUpdate(eq(DATABASE_ID, id), Updates.set(field, value), options);
    }

    public void findManyAndUpdate(Bson query, String field, Object value) {
        collection.updateMany(query, Updates.set(field, value));
    }

    public boolean exists(Bson query) {
        return collection.find(query).first() != null;
    }

    public boolean existsById(ObjectId id) {
        return collection.find(eq(DATABASE_ID, id)).first() != null;
    }
}
