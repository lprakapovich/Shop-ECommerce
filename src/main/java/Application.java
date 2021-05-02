import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpServer;
import handler.BookHandler;
import handler.OrderHandler;
import handler.RegistrationHandler;
import model.order.Order;
import model.order.OrderedItem;
import model.product.Product;
import model.product.book.Book;
import model.user.User;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import service.BookService;
import service.OrderService;
import service.UserService;

import java.io.IOException;
import java.net.InetSocketAddress;

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
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

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

        UserService userService = new UserService(database.getCollection(USERS_COLLECTION, User.class));

        RegistrationHandler registrationHandler = new RegistrationHandler(
                Configuration.getObjectMapper(),
                Configuration.getExceptionHandler(),
                userService);
        server.createContext("/api/users/register", registrationHandler::handle);

        BookHandler productHandler = new BookHandler(
                Configuration.getObjectMapper(),
                Configuration.getExceptionHandler(),
                new BookService(database.getCollection(BOOKS_COLLECTION, Book.class)));
        server.createContext("/api/products/books", productHandler::handle);

        OrderHandler orderHandler = new OrderHandler(
                Configuration.getObjectMapper(),
                Configuration.getExceptionHandler(),
                new OrderService(database.getCollection(ORDERS_COLLECTION, Order.class), userService));
        server.createContext("/api/orders", orderHandler::handle);
        server.setExecutor(null);
        server.start();
    }
}
