package handler;

import api.Handler;
import api.Response;
import api.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.GlobalExceptionHandler;
import model.user.User;
import service.UserService;

import java.io.IOException;
import java.io.OutputStream;

public class RegistrationHandler extends Handler {

    private final UserService userService;

    public RegistrationHandler(ObjectMapper objectMapper, GlobalExceptionHandler handler, UserService userService) {
        super(objectMapper, handler);
        this.userService = userService;
    }

    /**
     * Execute HTTP request
     * @param exchange gives control over incoming request (input stream) and output response (output stream)
     * @throws IOException I/O error
     */
    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response = resolveRequest(exchange);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
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
