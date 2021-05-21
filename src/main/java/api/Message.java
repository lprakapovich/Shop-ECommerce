package api;

public class Message {

    // TODO rearrange
    private Message() {}

    public static final String ITEM_NOT_FOUND = "Item not found";
    public static final String QUERY_NO_RESULT = "No items found with a given query";

    public static final String USER_NOT_FOUND = "User not found";
    public static final String BOOKS_BY_GENRE_NOT_FOUND = "Books of a given genre not found";
    public static final String BOOKS_BY_AUTHOR_NOT_FOUND = "Books of a given author not found";
    public static final String PRODUCTS_NOT_FOUND = "Products with a given criteria not found";
    public static final String PRODUCT_NOT_AVAILABLE = "Product is not available (quantity = 0)";
    public static final String ORDER_NOT_FOUND = "Order not found";

    public static final String USER_DUPLICATED_EMAIL = "User with such an email already exists";
    public static final String BOOK_DUPLICATED_TITLE_AND_AUTHOR = "Book with such author and title already exists";

    public static final String CANT_RESOLVE_HTTP_METHOD = "Couldn't resolve a HTTP method";
    public static final String MISSING_AUTH_HEADER = "Missing authorization header";
    public static final String NON_UNIQUE_RESULT = "Query returned non-unique result";
    public static final String INVALID_PRODUCT = "Product data is invalid";
    public static final String INVALID_ORDER = "Order data is invalid or missing";
    public static final String INVALID_ORDER_NON_EXISTING_PRODUCTS = "Order contains products that are not present in the system";
    public static final String INVALID_ORDER_WRONG_PRODUCT_QUANTITY = "Product quantity is invalid";
    public static final String INVALID_ORDER_REQUESTED_QUANTITY_EXCEEDS_ACTUAL = "Requested product quantity exceeds the one available at the stock";
    public static final String INVALID_METHOD = "Invalid method name (must be POST, PUT, GET, or DELETE";
    public static final String INVALID_REQUEST = "Invalid http request";
    public static final String INVALID_USER_CREDENTIALS = "Invalid user credentials";
    public static final String USERNAME_MISMATCH = "Username doesn't match the one from authentication header";
}
