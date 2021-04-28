package api;

import com.sun.net.httpserver.Headers;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Response<T> {
    T body;
    Headers headers;
    StatusCode status;
}
