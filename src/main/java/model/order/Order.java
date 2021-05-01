package model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import model.DBObject;
import model.user.User;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Order extends DBObject {

    private User issuer;
    private Order orderState;
    private Address shippingAddress;
    private List<OrderedItem> products;
}
