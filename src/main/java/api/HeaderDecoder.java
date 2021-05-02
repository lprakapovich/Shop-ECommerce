package api;

import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import org.bson.internal.Base64;

import java.nio.charset.StandardCharsets;

import static api.Message.MISSING_AUTH_HEADER;
import static util.Constants.AUTHORIZATION;
import static util.Constants.HEADER_SEPARATOR;

public class HeaderDecoder {

    public static String getBasicAuthUsername(HttpExchange exchange) {

        String basicAuthHeader = exchange.getRequestHeaders().get(AUTHORIZATION)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BadRequestException(MISSING_AUTH_HEADER));

        String encodedCredentials = basicAuthHeader.split("\\s")[1];
        byte[] decodedCredentials = Base64.decode(encodedCredentials);
        String credentials = new String(decodedCredentials, StandardCharsets.UTF_8);
        return credentials.split(HEADER_SEPARATOR)[0];
    }
}
