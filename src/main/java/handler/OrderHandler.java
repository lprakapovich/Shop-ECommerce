package handler;

import api.Handler;
import api.Method;
import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import model.product.book.Book;
import service.OrderService;

import java.io.OutputStream;

import static api.Message.INVALID_METHOD;

public class OrderHandler extends Handler {

    private OrderService orderService;

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

    private byte[] resolveResponse(HttpExchange exchange) {
        byte[] response;

        Method method = Method.valueOf(exchange.getRequestMethod());
        switch(method) {
            case POST:
                Response<String> post = handlePost(exchange);
                response = getResponseBodyAsBytes(exchange, post);
                break;

            case GET:
                Response<Book> get = handleGet(exchange);
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

    private Response<Book> handleGet(HttpExchange exchange) {
        return null;
    }

    private byte[] getResponseBodyAsBytes(HttpExchange exchange, Response<?> post) {
        return null;
    }

    private Response<String> handlePost(HttpExchange exchange) {
        return null;
    }
}
