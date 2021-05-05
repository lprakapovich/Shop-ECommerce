import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpServer;
import exception.GlobalExceptionHandler;
import handler.BookHandler;
import handler.OrderHandler;
import handler.RegistrationHandler;
import model.order.Order;
import model.product.Product;
import model.product.book.Book;
import model.user.User;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import service.BookService;
import service.OrderService;
import service.ProductService;
import service.UserService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static util.Constants.*;


public class Application {

    private static MongoDatabase database;

    public static void main(String[] args) throws IOException {
        configMongo();
    }

    private static void configMongo() throws IOException {

        ConnectionString connectionString = new ConnectionString("mongodb://localhost");

        // direct serialization of POJOs to and from BSON
        // Book.class is registered explicitly because codes is not found otherwise
        CodecRegistry pojoCodecRegistry = fromProviders(
                PojoCodecProvider.builder().register(model.product.book.Book.class).automatic(true).build());

        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        PojoCodecProvider.builder().register(Product.class);

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(clientSettings);
        database = mongoClient.getDatabase(DATABASE);
        configAPI();
    }

    private static void configAPI() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT),0);

        ObjectMapper mapper = Configuration.getObjectMapper();
        GlobalExceptionHandler exceptionHandler = Configuration.getExceptionHandler();

        UserService userService = new UserService(database.getCollection(USERS_COLLECTION, User.class));
        BookService bookService = new BookService(database.getCollection(BOOKS_COLLECTION, Book.class));
        OrderService orderService = new OrderService(database.getCollection(ORDERS_COLLECTION, Order.class), userService);

        RegistrationHandler registrationHandler = new RegistrationHandler(mapper, exceptionHandler, userService);
        server.createContext("/api/users/register", registrationHandler::handle);

        BookHandler productHandler = new BookHandler(mapper, exceptionHandler, bookService);
        server.createContext("/api/products/books", productHandler::handle);

        OrderHandler orderHandler = new OrderHandler(mapper, exceptionHandler, orderService);
        server.createContext("/api/orders", orderHandler::handle);

        server.setExecutor(null);
        server.start();
    }
}
