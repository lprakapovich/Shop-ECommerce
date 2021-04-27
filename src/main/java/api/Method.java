package api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Method {

    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE"),
    GET("GET");

    private final String name;
}
