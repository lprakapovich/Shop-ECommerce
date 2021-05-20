package handler;

import api.PreflightResponder;
import api.Response;
import api.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.GlobalExceptionHandler;
import model.user.User;
import service.UserService;

import java.io.IOException;
import java.io.OutputStream;

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
        String userId = userService.create(readRequestBody(exchange.getRequestBody(), User.class));
        return Response.<String>builder()
                .headers(getHeaders())
                .status(StatusCode.CREATED)
                .body(userId)
                .build();
    }
}
