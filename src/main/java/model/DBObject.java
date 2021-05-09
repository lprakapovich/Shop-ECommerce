package model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import serialization.ObjectIdSerializer;

@Data
public class DBObject {

    @BsonProperty(value = "_id")
    @JsonSerialize(using = ObjectIdSerializer.class)
    protected ObjectId id;
}
