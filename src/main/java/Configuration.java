import com.fasterxml.jackson.databind.ObjectMapper;
import exception.GlobalExceptionHandler;

public class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final GlobalExceptionHandler EXCEPTION_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
    public static GlobalExceptionHandler getExceptionHandler() {
        return EXCEPTION_HANDLER;
    }
}
