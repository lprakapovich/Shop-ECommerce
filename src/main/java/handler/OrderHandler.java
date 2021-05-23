package handler;

import api.HeaderDecoder;
import api.Method;
import api.Response;
import api.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import model.order.Order;
import org.apache.commons.lang3.StringUtils;
import service.OrderService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static api.Message.INVALID_METHOD;
import static util.Utils.*;

public class OrderHandler extends Handler {

    private final OrderService orderService;

    public OrderHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler, OrderService orderService) {
        super(objectMapper, exceptionHandler);
        this.orderService = orderService;
    }

    @Override
    protected byte[] resolveRequest(HttpExchange exchange) throws IOException {
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
            case DELETE:
                Response delete = handleDelete(exchange);
                response = getResponseBodyAsBytes(delete, exchange);
                break;
            default:
                throw new BadRequestException(INVALID_METHOD);
        }
        return response;
    }

    private Response handleDelete(HttpExchange exchange) {
        String orderId = getIdFromPath(exchange.getRequestURI().getRawPath());
        String username = HeaderDecoder.decryptHeaderUsername(exchange);
        orderService.delete(orderId, username);
        return Response.builder()
                .headers(getHeaders())
                .status(StatusCode.NO_CONTENT)
                .build();
    }

    private Response<Order> handlePut(HttpExchange exchange) {
        String username = HeaderDecoder.decryptHeaderUsername(exchange);
        Order updated = orderService.update(readRequestBody(exchange.getRequestBody(), Order.class), username);
        return Response.<Order>builder()
                .body(updated)
                .headers(getHeaders())
                .status(StatusCode.OK)
                .build();
    }

    private Response<?> handleGet(HttpExchange exchange) {
        String path = exchange.getRequestURI().getRawPath();
        String orderId = getIdFromPath(path);
        String username = HeaderDecoder.decryptHeaderUsername(exchange);
        Map<String, List<String>> params = splitQueryList(exchange.getRequestURI().getRawQuery());
        return Response.builder()
                .body(containsInPath(path, "cart") ? orderService.getCart(params) :
                        containsInPath(path, "all") ? orderService.getAll(username) :
                        StringUtils.isNotBlank(orderId) ? orderService.get(orderId, username) :
                                orderService.find(params))
                .headers(getHeaders())
                .status(StatusCode.OK)
                .build();
    }

    private Response<String> handlePost(HttpExchange exchange) {
        String user = HeaderDecoder.decryptHeaderUsername(exchange);
        String orderId = orderService.create(readRequestBody(exchange.getRequestBody(), Order.class), user);
        return Response.<String>builder()
                .body(orderId)
                .headers(getHeaders())
                .status(StatusCode.CREATED)
                .build();
    }
}
