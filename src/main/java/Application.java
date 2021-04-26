import auth.RegistrationHandler;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.client.methods.HttpPost;
import org.bson.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_METHOD_NOT_ALLOWED;
import static org.apache.http.HttpStatus.SC_OK;
import static util.Constants.*;
import static util.Utils.splitQuery;

public class Application {

    public static void main(String[] args) throws IOException {
        configMongo();
        configAPI();
    }

    private static void configMongo() {

        MongoClient mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> usersCollection = database.getCollection(USERS_COLLECTION);
        MongoCollection<Document> ordersCollection = database.getCollection(ORDERS_COLLECTION);
        MongoCollection<Document> booksCollection = database.getCollection(BOOKS_COLLECTION);
        MongoCollection<Document> gamesCollection = database.getCollection(GAMES_COLLECTION);
    }

    private static void configAPI() throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT),0);

        RegistrationHandler registrationHandler = new RegistrationHandler(Configuration.getUserService(),
                Configuration.getObjectMapper(), Configuration.getExceptionHandler());

        server.createContext("/api/users/register", registrationHandler::handle);

        server.createContext("api/products/books/", (exchange -> {})).setAuthenticator(new BasicAuthenticator(MONGO_REALM) {
            @Override
            public boolean checkCredentials(String username, String password) {
                return true;
            }
        });

        server.createContext("api/products/games", (exchange -> {})).setAuthenticator(new BasicAuthenticator(MONGO_REALM) {
            @Override
            public boolean checkCredentials(String username, String password) {
                return true;
            }
        });

        server.createContext("api/orders", (exchange -> {})).setAuthenticator(new BasicAuthenticator(MONGO_REALM) {
            @Override
            public boolean checkCredentials(String username, String password) {
                return true;
            }
        });

        HttpContext context = server.createContext("/api/hello", (exchange -> {
            if (HttpPost.METHOD_NAME.equals(exchange.getRequestMethod())) {
                Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
                String noName = "anon";
                String name = params.getOrDefault("name", List.of(noName)).stream().findFirst().orElse(noName);
                String response = String.format("Hello %s", name);

                exchange.sendResponseHeaders(SC_OK,  response.getBytes().length);

                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(SC_METHOD_NOT_ALLOWED, -1);
            }

            exchange.close();
        }));


        context.setAuthenticator(new BasicAuthenticator(MONGO_REALM) {
            @Override
            public boolean checkCredentials(String username, String password) {
                return true;
            }
        });

        server.setExecutor(null);
        server.start();
    }
}
