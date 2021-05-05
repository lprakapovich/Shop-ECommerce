package model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import model.DBObject;
import model.user.User;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Order extends DBObject {
    private User issuer;
    private OrderState orderState;
    private List<OrderedItem> products;
    private LocalDate date;
    private LocalDate lastModifiedDate;
    private Address shippingAddress;
}
