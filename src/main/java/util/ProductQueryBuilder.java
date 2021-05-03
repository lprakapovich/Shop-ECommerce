package util;

import lombok.AllArgsConstructor;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;
import static util.ProductQueryBuilder.QueryParam.*;

public class ProductQueryBuilder {

    static List<Bson> filters;

    public interface FilterApplier {
        void apply(Map<String, String> params);
    }

    public static Bson buildQuery(Map<String, String> requestParams) {
        filters = new ArrayList<>();
        Set<String> keys = requestParams.keySet();

        if (!keys.isEmpty()) {
            if (keys.contains(PriceGte.paramName) && keys.contains(PriceLte.paramName)) {
                addPriceRangeFilter(requestParams);
                keys.remove(PriceGte.paramName);
                keys.remove(PriceLte.paramName);
            }

            for (String key : keys) {
                for (QueryParam param : QueryParam.values()) {
                    if (param.paramName.equals(key)) {
                        param.filterApplier.apply(requestParams);
                    }
                }
            }
        }

        return combine(filters);
    }

    private static void addPriceRangeFilter(Map<String, String> requestParams) {
        String minValue = requestParams.get(PriceGte.paramName);
        String maxValue = requestParams.get(PriceLte.paramName);
        filters.add(and(gte(PriceGte.fieldName, Double.valueOf(minValue)), lte(PriceLte.fieldName, Double.valueOf(maxValue))));
    }

    @AllArgsConstructor
     enum QueryParam {
        PriceGte("price[gte]", "price", PriceGteApplier),
        PriceLte("price[lte]", "price", PriceLteApplier),
        Author("author", "author", AuthorApplier),
        Price("price", "price", PriceApplier),
        Genre("genre", "genre", GenreApplier),
        Name("name", "name", NameApplier),
        Id("id", "_id", IdApplier);

        private final String paramName;
        private final String fieldName;
        private final FilterApplier filterApplier;
    }

    private static final FilterApplier PriceGteApplier = (params) -> {
        filters.add(gte(PriceGte.fieldName, Double.valueOf(params.get(PriceGte.paramName))));
    };

    private static final FilterApplier PriceLteApplier = (params) -> {
        filters.add(lte(PriceLte.fieldName, Double.valueOf(params.get(PriceLte.paramName))));
    };

    private static final FilterApplier PriceApplier = (params) -> {
        filters.add(eq(Price.fieldName, Double.valueOf(params.get(Price.paramName))));
    };

    private static final FilterApplier AuthorApplier = (params) -> {
        filters.add(eq(Author.fieldName, params.get(Author.paramName)));
    };

    private static final FilterApplier NameApplier = (params) -> {
        filters.add(eq(Name.fieldName, params.get(Name.paramName)));
    };

    private static final FilterApplier GenreApplier = (params) -> {
        filters.add(eq(Genre.fieldName, params.get(Genre.paramName)));
    };

    private static final FilterApplier IdApplier = (params) -> {
        filters.add(eq(Id.fieldName, new ObjectId(params.get(Id.paramName))));
    };
}
