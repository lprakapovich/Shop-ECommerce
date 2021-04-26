import com.fasterxml.jackson.databind.ObjectMapper;
import exception.GlobalExceptionHandler;
import repository.InMemoryUserRepository;
import repository.UserRepository;
import service.UserService;

public class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    public static final UserService USER_SERVICE = new UserService(USER_REPOSITORY);
    public static final GlobalExceptionHandler EXCEPTION_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static UserService getUserService() {
        return USER_SERVICE;
    }

    public static UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    public static GlobalExceptionHandler getExceptionHandler() {
        return EXCEPTION_HANDLER;
    }
}
