package model.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Order {
    private String id;
    private String userId;
    private Order orderState;
    private Address shippingAddress;
    private List<OrderedItem> products;
}
