package exception;

import java.util.function.Function;
import java.util.function.Supplier;

import static api.StatusCode.*;

public class Exceptions {

    public static Function<? super Throwable, RuntimeException> badRequest() {
        return thr -> new BadRequestException(thr.getMessage());
    }

    public static Supplier<RuntimeException> forbidden(String message) {
        return () -> new MethodNotAllowedException(FORBIDDEN.getCode(), message);
    }

    public static Supplier<RuntimeException> notFound(String message) {
        return () -> new ResourceNotFoundException(message);
    }
}
