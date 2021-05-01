package handler;

import api.Handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.GlobalExceptionHandler;

import java.io.OutputStream;

public class OrderHandler extends Handler {

    public OrderHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
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
        return null;
    }
}
