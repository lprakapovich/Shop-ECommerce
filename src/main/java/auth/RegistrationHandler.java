package auth;

import api.Handler;
import api.StatusCode;
import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import model.NewUser;
import exception.Exceptions;
import exception.GlobalExceptionHandler;
import org.apache.http.client.methods.HttpPost;
import service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static util.Constants.CONTENT_TYPE;
import static util.Constants.APPLICATION_JSON;

public class RegistrationHandler extends Handler {

    private final UserService userService;

    public RegistrationHandler(UserService userService, ObjectMapper objectMapper, GlobalExceptionHandler handler) {
        super(objectMapper, handler);
        this.userService = userService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response = resolveRequest(exchange);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private byte[] resolveRequest(HttpExchange exchange) throws IOException {
        byte[] response;
        if (HttpPost.METHOD_NAME.equals(exchange.getRequestMethod())) {
            Response e = registerUser(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatus().getCode(), 0);
            response = super.writeResponse(e.getBody());
        } else {
            throw Exceptions.forbidden("Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        return response;
    }

    private Response<RegistrationResponse> registerUser(InputStream requestBody) {
        RegistrationRequest request = super.readRequest(requestBody, RegistrationRequest.class);

        NewUser user = NewUser.builder()
                .login(request.getLogin())
                .password(request.getPassword())
                .build();

        String newUserId = userService.create(user);

        RegistrationResponse response = new RegistrationResponse(newUserId);

        return new Response<>(response, getHeaders(CONTENT_TYPE, APPLICATION_JSON), StatusCode.CREATED);
    }
}
