package exception;

import api.ErrorResponse;
import api.ErrorResponse.ErrorResponseBuilder;
import api.PreflightResponder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

import static api.Method.OPTIONS;
import static api.StatusCode.*;
import static util.Constants.APPLICATION_JSON;
import static util.Constants.CONTENT_TYPE;

@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public void handle(Throwable throwable, HttpExchange exchange) {
        try {
            throwable.printStackTrace();

            if (exchange.getRequestMethod().equals(OPTIONS.getName())) {
                PreflightResponder.sendPreflightCheckResponse(exchange);
            } else {
                exchange.getResponseHeaders().set(CONTENT_TYPE, APPLICATION_JSON);
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                ErrorResponse errorResponse = getErrorResponse(throwable, exchange);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(objectMapper.writeValueAsBytes(errorResponse));
                responseBody.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ErrorResponse getErrorResponse(Throwable throwable, HttpExchange exchange) throws IOException {
        ErrorResponseBuilder responseBuilder = ErrorResponse.builder();
        if (throwable instanceof BadRequestException) {
            BadRequestException ex = (BadRequestException) throwable;
            responseBuilder.message(ex.getMessage()).code(ex.getCode());
            exchange.sendResponseHeaders(BAD_REQUEST.getCode(), 0);
        }
        else if (throwable instanceof ResourceNotFoundException) {
            ResourceNotFoundException ex = (ResourceNotFoundException) throwable;
            responseBuilder.message(ex.getMessage()).code(ex.getCode());
            exchange.sendResponseHeaders(NOT_FOUND.getCode(), 0);
        }
        else if (throwable instanceof MethodNotAllowedException) {
            MethodNotAllowedException ex = (MethodNotAllowedException) throwable;
            responseBuilder.message(ex.getMessage()).code(ex.getCode());
            exchange.sendResponseHeaders(FORBIDDEN.getCode(), 0);
        }
        else {
            responseBuilder.code(INTERNAL_SERVER_ERROR.getCode()).message(throwable.getMessage());
            exchange.sendResponseHeaders(INTERNAL_SERVER_ERROR.getCode(), 0);
        }
        return responseBuilder.build();
    }
}
