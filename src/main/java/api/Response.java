package api;

import api.StatusCode;
import com.sun.net.httpserver.Headers;
import lombok.Value;

@Value
public class Response<T> {
    T body;
    Headers headers;
    StatusCode status;
}
