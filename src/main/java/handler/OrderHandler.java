package handler;

import api.Handler;
import api.Method;
import api.Response;
import api.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import model.order.Order;
import service.OrderService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static api.Message.INVALID_METHOD;
import static util.Constants.APPLICATION_JSON;
import static util.Constants.CONTENT_TYPE;
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
    // 1. create order POST
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
                response = getResponseBodyAsBytes(exchange, post);
                break;

            case GET:
                Response<Order> get = handleGet(exchange);
                response = getResponseBodyAsBytes(exchange, get);
                break;

            case DELETE:
                Response<Object> delete = handleDelete(exchange);
                response = getResponseBodyAsBytes(exchange, delete);
                break;

            default:
                throw new BadRequestException(INVALID_METHOD);
        }

        return response;
    }

    private Response<Object> handleDelete(HttpExchange exchange) {
        return null;
    }

    // TODO change all gets so that product id is sent as path variable (not parameter)
    private Response<Order> handleGet(HttpExchange exchange) {
        Map<String, List<String>> params = splitQueryList(exchange.getRequestURI().getRawQuery());
        System.out.println(params);
        return null;
    }

    private byte[] getResponseBodyAsBytes(HttpExchange exchange, Response<?> response) throws IOException {
        exchange.getResponseHeaders().putAll(response.getHeaders());
        exchange.sendResponseHeaders(response.getStatus().getCode(), 0);
        return super.writeResponse(response.getBody());
    }

    private Response<String> handlePost(HttpExchange exchange) {
        String orderId = orderService.create(readRequestBody(exchange.getRequestBody(), Order.class));
        return Response.<String>builder()
                .body(orderId)
                .headers(getHeaders(CONTENT_TYPE, APPLICATION_JSON))
                .status(StatusCode.CREATED)
                .build();
    }
}
