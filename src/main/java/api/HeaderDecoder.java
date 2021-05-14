package api;

import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import org.bson.internal.Base64;

import java.nio.charset.StandardCharsets;

import static api.Message.MISSING_AUTH_HEADER;
import static util.Constants.AUTHORIZATION;
import static util.Constants.HEADER_SEPARATOR;

public class HeaderDecoder {

    public static String decryptHeaderUsername(HttpExchange exchange) {
        String credentials = getDecodedCredentials(exchange);
        return credentials.split(HEADER_SEPARATOR)[0];
    }

    public static String decryptHeaderPassword(HttpExchange exchange) {
        String credentials = getDecodedCredentials(exchange);
        return credentials.split(HEADER_SEPARATOR)[1];
    }

    private static String getDecodedCredentials(HttpExchange exchange) {
        String basicAuthHeader = exchange.getRequestHeaders().get(AUTHORIZATION)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BadRequestException(MISSING_AUTH_HEADER));

        String encodedCredentials = basicAuthHeader.split("\\s")[1];
        byte[] decodedCredentials = Base64.decode(encodedCredentials);
        return new String(decodedCredentials, StandardCharsets.UTF_8);
    }
}

