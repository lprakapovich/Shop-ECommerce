package model.user;

import lombok.*;
import model.DBObject;
import model.order.Address;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends DBObject {

    private String name;
    private String surname;
    private String email;
    private String password;
    private Address address;
    private Role role;
}
