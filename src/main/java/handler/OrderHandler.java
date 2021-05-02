package handler;

import api.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import model.order.Order;
import org.apache.maven.shared.utils.StringUtils;
import service.OrderService;
import util.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static api.Message.INVALID_METHOD;
import static util.Utils.splitQueryList;

public class OrderHandler extends Handler {

    private final OrderService orderService;

    public OrderHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler, OrderService orderService) {
        super(objectMapper, exceptionHandler);
        this.orderService = orderService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws Exception {
        byte[] response = resolveResponse(exchange);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    //TODO
    // 1. create order POST - d
    // 2. delete order DELETE
    // 3. (MUST) update order PUT
    // 4. get order by id GET
    // 5. get user -> read orderIds -> get all user orders
    // 6. get product -> read orderIds -> get all orders in which the product appeared

    private byte[] resolveResponse(HttpExchange exchange) throws IOException {
        byte[] response;

        Method method = Method.valueOf(exchange.getRequestMethod());
        switch(method) {
            case POST:
                Response<String> post = handlePost(exchange);
                response = getResponseBodyAsBytes(post, exchange);
                break;

            case GET:
                Response<?> get = handleGet(exchange);
                response = getResponseBodyAsBytes(get, exchange);
                break;

            case PUT:
                Response<Order> put = handlePut(exchange);
                response = getResponseBodyAsBytes(put, exchange);
                break;

            default:
                throw new BadRequestException(INVALID_METHOD);
        }

        return response;
    }

    private Response<Order> handlePut(HttpExchange exchange) {
        String user = HeaderDecoder.getBasicAuthUsername(exchange);
        Order updated = orderService.update(readRequestBody(exchange.getRequestBody(), Order.class), user);
        return Response.<Order>builder()
                .body(updated)
                .headers(getHeaders())
                .status(StatusCode.OK)
                .build();
    }

    // TODO change all gets so that product id is sent as path variable (not parameter)
    private Response<?> handleGet(HttpExchange exchange) {
        Response<?> response;
        String user = HeaderDecoder.getBasicAuthUsername(exchange);

        String path = exchange.getRequestURI().getPath();
        String uri = exchange.getRequestURI().getRawQuery();
        Map<String, List<String>> params = splitQueryList(exchange.getRequestURI().getRawQuery());

        String orderId = getIdFromPath(path);

        if (!StringUtils.isNotBlank(orderId)) {
            response = getOrderById(orderId, user);
        } else if (params.containsKey("ids")) {
            response = getOrdersByIds(params.get("ids"), user);
        } else {
            response = getOrdersByQuery(params, user);
        }

        return response;
    }

    private String getIdFromPath(String path) {
        return Utils.getIdFromPath(path);
    }

    private Response<Order> getOrderById(String id, String issuer) {
        Order order = orderService.get(id, issuer);
        return Response.<Order>builder()
                .body(order)
                .headers(getHeaders())
                .status(StatusCode.OK)
                .build();
    }

    private Response<List<Order>> getOrdersByQuery(Map<String, List<String>> params, String issuer) {
        return null;
    }

    private Response<?> getOrdersByIds(List<String> ids, String issuer) {
        List<Order> orders = orderService.get(ids, issuer);
        return null;
    }

    private Response<String> handlePost(HttpExchange exchange) {
        String user = HeaderDecoder.getBasicAuthUsername(exchange);
        String orderId = orderService.create(readRequestBody(exchange.getRequestBody(), Order.class), user);
        return Response.<String>builder()
                .body(orderId)
                .headers(getHeaders())
                .status(StatusCode.CREATED)
                .build();
    }
}
