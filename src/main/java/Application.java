import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpServer;
import handler.ProductHandler;
import handler.RegistrationHandler;
import org.apache.http.client.methods.HttpPost;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import service.ProductService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static api.StatusCode.FORBIDDEN;
import static api.StatusCode.OK;
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
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            database = mongoClient.getDatabase(DATABASE);
            configAPI();
        }
    }

    private static void configAPI() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT),0);

        RegistrationHandler registrationHandler = new RegistrationHandler(
                Configuration.getUserService(),
                Configuration.getObjectMapper(),
                Configuration.getExceptionHandler());
        server.createContext("/api/users/register", registrationHandler::handle);

        ProductHandler productHandler = new ProductHandler(
                Configuration.getObjectMapper(),
                Configuration.getExceptionHandler(),
                new ProductService(database.getCollection(BOOKS_COLLECTION)));
        server.createContext("/api/products", productHandler::handle);

        server.createContext("/api/hello", (exchange -> {
            if (HttpPost.METHOD_NAME.equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(OK.getCode(),  "Hello".getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write("Hello".getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(FORBIDDEN.getCode(), -1);
            }
            exchange.close();
        }));
        server.setExecutor(null);
        server.start();
    }
}
