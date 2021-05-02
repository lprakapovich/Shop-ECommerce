package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;


@RequiredArgsConstructor
public abstract class Handler {

    private final ObjectMapper objectMapper;
    private final GlobalExceptionHandler exceptionHandler;

    public void handle(HttpExchange exchange) {
        try {
            execute(exchange);
        } catch (Exception t) {
            exceptionHandler.handle(t, exchange);
        }
    }

    protected abstract void execute(HttpExchange exchange) throws Exception;

    protected <T> T readRequestBody(InputStream body, Class<T> type) {
        T mappedObject;
        try {
            mappedObject = objectMapper.readValue(body, type);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return mappedObject;
    }

    protected <T> byte[] writeResponse(T response) {
        byte[] responseBytes;
        try {
            String s = objectMapper.writeValueAsString(response);
            System.out.println(s);
            responseBytes = objectMapper.writeValueAsBytes(response);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return responseBytes;
    }

    protected static Headers getHeaders() {
        Headers headers = new Headers();
        headers.set(util.Constants.CONTENT_TYPE, util.Constants.APPLICATION_JSON);
        return headers;
    }

    protected  <T> byte[] getResponseBodyAsBytes(Response<T> response, HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().putAll(response.getHeaders());
        exchange.sendResponseHeaders(response.getStatus().getCode(), 0);
        return writeResponse(response.getBody());
    }
}
