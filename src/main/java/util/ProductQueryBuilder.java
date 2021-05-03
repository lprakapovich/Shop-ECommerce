package util;

import lombok.AllArgsConstructor;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;
import static util.ProductQueryBuilder.QueryParam.*;

public class ProductQueryBuilder {

    static List<Bson> filters;

    @AllArgsConstructor
    enum QueryParam {
        PriceGte("price[gte]", "price"),
        PriceLte("price[lte]", "price"),
        Price("price", "price"),
        Author("author", "author"),
        Genre("genre", "genre"),
        Name("name", "name");
        private final String paramName;
        private final String fieldName;
    }

    public static Bson buildQuery(Map<String, String> requestParams) {

        filters = new ArrayList<>();

        Set<String> keys = requestParams.keySet();
        if (!keys.isEmpty()) {
            if (keys.contains(PriceGte.paramName) && keys.contains(PriceLte.paramName)) {
                addPriceRangeFilter(requestParams);
            } else if (keys.contains(PriceLte.paramName)) {
                addPriceLessThanFilter(requestParams);
            } else if (keys.contains(PriceGte.paramName)) {
                addPriceHigherThanFilter(requestParams);
            } else if (keys.contains(Price.paramName)) {
                addPriceFilter(requestParams);
            }

            keys.remove(PriceGte.paramName);
            keys.remove(PriceLte.paramName);
            keys.remove(Price.paramName);

            for(String key : keys) {
                for (QueryParam param : QueryParam.values()) {
                    if (param.paramName.equals(key)) {
                        addFilter(param, requestParams.get(key));
                    }
                }
            }
        }

        return combine(filters);
    }

    private static void addPriceFilter(Map<String, String> requestParams) {
        filters.add(eq(Price.fieldName, Double.valueOf(requestParams.get(Price.paramName))));
    }

    private static void addPriceLessThanFilter(Map<String, String> requestParams) {
        filters.add(lte(PriceLte.fieldName, Double.valueOf(requestParams.get(PriceLte.paramName))));
    }

    private static void addPriceHigherThanFilter(Map<String, String> requestParams) {
        filters.add(gte(PriceGte.fieldName, Double.valueOf(requestParams.get(PriceGte.paramName))));
    }

    private static void addPriceRangeFilter(Map<String, String> requestParams) {
        Object minValue = requestParams.get(PriceGte.paramName);
        Object maxValue = requestParams.get(PriceLte.paramName);
        filters.add(and(gte(PriceGte.fieldName, minValue), lte(PriceLte.fieldName, maxValue)));
    }

    private static void addFilter(QueryParam param, String value) {
        filters.add(eq(param.fieldName, value));
    }
}
