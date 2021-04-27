package handler;

import api.Handler;
import api.Method;
import api.QueryParamValue;
import api.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import service.ProductService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static api.QueryParamName.PRODUCT_ID;
import static api.QueryParamName.PRODUCT_TYPE;
import static util.Utils.splitQuery;

public class ProductHandler extends Handler {

    private final ProductService productService;

    public ProductHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler, ProductService service) {
        super(objectMapper, exceptionHandler);
        this.productService = service;
    }

    @Override
    protected void execute(HttpExchange exchange) throws Exception {
        byte[] response = resolveRequest(exchange);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private byte[] resolveRequest(HttpExchange exchange) throws IOException {

        byte[] response = null;
        Map<String, String> params = splitQuery(exchange.getRequestURI().getRawQuery());
        QueryParamValue productType = QueryParamValue.valueOf(params.get(PRODUCT_TYPE.getName()));

        switch (productType) {
            case BOOK:
                handleBookRequests(exchange, params);
                break;
            case GAME:

                break;
            default:
                throw new BadRequestException(StatusCode.BAD_REQUEST.getCode(), "Can't parse params");
        }

        return exchange.getRequestBody().readAllBytes();
    }

    private void handleBookRequests(HttpExchange exchange, Map<String, String> params) {
        Method method = Method.valueOf(exchange.getRequestMethod());

        switch (method) {
            case GET:
                String bookId = params.get(PRODUCT_ID.getName());
                break;
            case POST:

                break;
            case PUT:

                break;
            case DELETE:

                break;
            default:
                throw new BadRequestException(StatusCode.BAD_REQUEST.getCode(), "Couldn't resolve a method");
        }
    }
}
