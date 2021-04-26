package model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.order.Address;

@Data
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String surname;
    private String email;
    private Address address;
    private String clubMembershipNumber;
}
