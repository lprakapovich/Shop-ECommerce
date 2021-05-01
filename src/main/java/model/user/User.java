package model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.order.Address;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @BsonProperty(value = "_id")
    private ObjectId id;
    private String password;
    private String name;
    private String surname;
    private String email;
    private Address address;
    private Role role;
}
