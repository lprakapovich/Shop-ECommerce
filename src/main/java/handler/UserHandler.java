package handler;

import api.HeaderDecoder;
import api.Method;
import api.Response;
import api.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.GlobalExceptionHandler;
import model.user.User;
import org.apache.maven.shared.utils.StringUtils;
import service.UserService;

import java.io.IOException;

import static api.Message.CANT_RESOLVE_HTTP_METHOD;
import static api.Message.INVALID_URL;
import static util.Utils.getIdFromPath;

public class UserHandler extends Handler {
    private final UserService userService;
    public UserHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler, UserService userService) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }

    @Override
    protected byte[] resolveRequest(HttpExchange exchange) throws IOException {
        byte[] response;
        Method method = Method.valueOf(exchange.getRequestMethod());
        switch(method) {
            case PUT:
                Response<User> put = resolvePut(exchange);
                response = getResponseBodyAsBytes(put, exchange);
                break;
            case GET:
                Response<User> get = resolveGet(exchange);
                response = getResponseBodyAsBytes(get, exchange);
                break;
            default:
                throw new BadRequestException(CANT_RESOLVE_HTTP_METHOD);
        }

        return response;
    }

    private Response<User> resolveGet(HttpExchange exchange) {
        String userId = getIdFromPath(exchange.getRequestURI().getRawPath());
        if (StringUtils.isBlank(userId)) {
            throw new BadRequestException(INVALID_URL);
        }
        User user = userService.get(userId, HeaderDecoder.decryptHeaderUsername(exchange));
        return Response.<User>builder().status(StatusCode.OK).headers(getHeaders()).body(user).build();
    }

    private Response<User> resolvePut(HttpExchange exchange) {
        String email = HeaderDecoder.decryptHeaderUsername(exchange);
        User updated = userService.update(readRequestBody(exchange.getRequestBody(), User.class), email);
        return Response.<User>builder().status(StatusCode.OK).headers(getHeaders()).body(updated).build();
    }
}
