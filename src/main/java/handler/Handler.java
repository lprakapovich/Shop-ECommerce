package handler;

import api.PreflightResponder;
import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static api.Method.OPTIONS;
import static util.Constants.*;


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

    /**
     * Execute HTTP request
     * @param exchange gives control over incoming request (input stream) and output response (output stream)
     * @throws IOException I/O error
     */

    protected void execute(HttpExchange exchange) throws Exception {
        if (exchange.getRequestMethod().equalsIgnoreCase(OPTIONS.getName())) {
            PreflightResponder.sendPreflightCheckResponse(exchange);
        } else {
            byte[] response = resolveRequest(exchange);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.flush();
            os.close();
        }
    }

    protected abstract byte[] resolveRequest(HttpExchange exchange) throws IOException;

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
            responseBytes = objectMapper.writeValueAsBytes(response);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return responseBytes;
    }

    protected static Headers getHeaders() {
        Headers headers = new Headers();
        headers.set(ALLOW_ORIGIN, ALL);
        headers.set(ALLOW_HEADERS, ALLOWED_HEADERS);
        headers.set(ALLOW_METHODS, "GET, HEAD, OPTIONS, POST, PUT");
        headers.set(CONTENT_TYPE, APPLICATION_JSON);
        return headers;
    }

    protected  <T> byte[] getResponseBodyAsBytes(Response<T> response, HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().putAll(response.getHeaders());
            exchange.sendResponseHeaders(response.getStatus().getCode(), 0);
            return writeResponse(response.getBody());
    }
}
