package handler;

import api.Handler;
import api.PreflightResponder;
import api.Response;
import api.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import model.user.User;
import service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import static api.Message.INVALID_REQUEST;
import static api.Method.OPTIONS;

public class RegistrationHandler extends Handler {

    private final UserService userService;

    public RegistrationHandler(ObjectMapper objectMapper, GlobalExceptionHandler handler, UserService userService) {
        super(objectMapper, handler);
        this.userService = userService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase(OPTIONS.getName())) {
            PreflightResponder.sendPreflightCheckResponse(exchange);
        } else {
            byte[] response = resolveRequest(exchange);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    private byte[] resolveRequest(HttpExchange exchange) throws IOException {
        Response<String> post = handlePost(exchange);
        return getResponseBodyAsBytes(post, exchange);
    }

    private Response<String> handlePost(HttpExchange exchange) {
        Object body = readRequestBody(exchange.getRequestBody(), Object.class);

        String userId = userService.create(readRequestBody(exchange.getRequestBody(), User.class));
        return Response.<String>builder()
                .headers(getHeaders())
                .status(StatusCode.CREATED)
                .body(userId)
                .build();
    }
}
