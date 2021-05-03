package api;

public class Message {

    private Message() {}

    public static final String ITEM_NOT_FOUND = "Item not found";
    public static final String QUERY_NO_RESULT = "No items found with a given query";

    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_DUPLICATED_EMAIL = "User with such an email already exists";
    public static final String BOOKS_BY_GENRE_NOT_FOUND = "Books of a given genre not found";
    public static final String BOOKS_BY_AUTHOR_NOT_FOUND = "Books of a given author not found";
    public static final String PRODUCTS_NOT_FOUND = "Products with a given criteria not found";
    public static final String ORDER_NOT_FOUND = "Order not found";

    public static final String MISSING_AUTH_HEADER = "Missing authorization header";
    public static final String NON_UNIQUE_RESULT = "Query returned non-unique result";
    public static final String INVALID_PRODUCT = "Product data is invalid";
    public static final String INVALID_METHOD = "Invalid method name (must be POST, PUT, GET, or DELETE";
    public static final String USERNAME_MISMATCH = "Username doesn't match the one from authentication header";
}
