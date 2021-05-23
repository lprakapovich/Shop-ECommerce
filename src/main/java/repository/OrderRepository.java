package repository;

import com.mongodb.client.MongoCollection;
import model.order.Order;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static util.Constants.DATABASE_ID;
import static util.Constants.ISSUER_EMAIL;

public class OrderRepository extends MongoRepository<Order> {

    public OrderRepository(MongoCollection<Order> collection) {
        super(collection);
    }

    public Order get(ObjectId id, String issuer) {
        return collection.find(and(eq(DATABASE_ID, id), eq(ISSUER_EMAIL, issuer))).first();
    }

    public List<Order> get(String issuer) {
        return collection.find(eq(ISSUER_EMAIL, issuer)).into(new ArrayList<>());
    }

    public void updateProductQuantitiesInCart(ObjectId productId, int availableQuantity) {
        super.findManyAndUpdate((and(
                eq("orderedItems.product._id", productId),
                eq("status", "Cart"))),
                "orderedItems.$.product.availableQuantity", availableQuantity);
    }

    public void updateCartOrderedQuantityIfExceeds(ObjectId productId, int availableQuantity) {
            super.findManyAndUpdate((and(
                    eq("orderedItems.product._id", productId),
                    eq("status", "Cart"),
                    gt("orderedItems.orderedQuantity", availableQuantity))),
                    "orderedItems.$.orderedQuantity", availableQuantity);
    }

    public void deleteProductFromCart(ObjectId productId) {
        super.findManyAndDelete(and(eq("orderedItems.product._id", productId), eq("status", "Cart")));
    }
}

