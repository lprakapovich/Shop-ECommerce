package model;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Data
public class DBObject {

    @BsonProperty(value = "_id")
    protected ObjectId id;
}
