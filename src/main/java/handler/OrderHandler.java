package handler;

import api.*;
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

    private Response<?> handleGet(HttpExchange exchange) {
        // TODO add user validation
        String user = HeaderDecoder.getBasicAuthUsername(exchange);
        Map<String, List<String>> params = splitQueryList(exchange.getRequestURI().getRawQuery());
        List<Order> orders = orderService.get(params);
        return Response.<List<Order>>builder()
                .body(orders)
                .headers(getHeaders())
                .status(StatusCode.OK)
                .build();
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
