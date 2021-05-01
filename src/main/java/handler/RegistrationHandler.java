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

import static util.Constants.APPLICATION_JSON;
import static util.Constants.CONTENT_TYPE;

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

    // TODO extend with switch as more endpoints come int
    private byte[] resolveRequest(HttpExchange exchange) throws IOException {
        Response<String> post = handlePost(exchange);
        return sendResponseBackAsBytes(post, exchange);
    }

    private byte[] sendResponseBackAsBytes(Response<?> response, HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().putAll(response.getHeaders());
        // send response back to client, terminated by closing an output stream
        exchange.sendResponseHeaders(response.getStatus().getCode(), 0);
        return super.writeResponse(response.getBody());
    }

    private Response<String> handlePost(HttpExchange exchange) {
        String userId = userService.create(readRequestBody(exchange.getRequestBody(), User.class));
        return Response.<String>builder()
                .headers(getHeaders(CONTENT_TYPE, APPLICATION_JSON))
                .status(StatusCode.CREATED)
                .body(userId)
                .build();
    }
}
