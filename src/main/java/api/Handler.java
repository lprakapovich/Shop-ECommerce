package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

import static api.StatusCode.BAD_REQUEST;


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

    protected <T> T readRequest(InputStream is, Class<T> type) {
        T mappedObject = null;
        try {
            mappedObject = objectMapper.readValue(is, type);
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST.getCode(), e.getMessage());
        }
        return mappedObject;
    }

    protected <T> byte[] writeResponse(T response) {
        byte[] responseBytes = null;
        try {
            responseBytes = objectMapper.writeValueAsBytes(response);
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST.getCode(), e.getMessage());
        }
        return responseBytes;
    }

    protected static Headers getHeaders(String key, String value) {
        Headers headers = new Headers();
        headers.set(key, value);
        return headers;
    }
}
