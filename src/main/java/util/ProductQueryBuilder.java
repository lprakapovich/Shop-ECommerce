package util;

import lombok.AllArgsConstructor;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;
import static util.ProductQueryBuilder.QueryParam.*;

public class ProductQueryBuilder {

   private static List<Bson> filters;

    public static Bson buildQuery(Map<String, List<String>> requestParams) {
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

    private static void addPriceRangeFilter(Map<String, List<String>> requestParams) {
        String minValue = requestParams.get(PriceGte.paramName).get(0);
        String maxValue = requestParams.get(PriceLte.paramName).get(0);
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
        filters.add(gte(PriceGte.fieldName, Double.valueOf(params.get(PriceGte.paramName).get(0))));
    };

    private static final FilterApplier PriceLteApplier = (params) -> {
        filters.add(lte(PriceLte.fieldName, Double.valueOf(params.get(PriceLte.paramName).get(0))));
    };

    private static final FilterApplier PriceApplier = (params) -> {
        filters.add(in(Price.fieldName, mapToDoubleList(params.get(Price.paramName))));
    };

    private static final FilterApplier AuthorApplier = (params) -> {
        filters.add(in(Author.fieldName, params.get(Author.paramName)));
    };

    private static final FilterApplier NameApplier = (params) -> {
        filters.add(in(Name.fieldName, params.get(Name.paramName)));
    };

    private static final FilterApplier GenreApplier = (params) -> {
        filters.add(in(Genre.fieldName, params.get(Genre.paramName)));
    };

    private static final FilterApplier IdApplier = (params) -> {
        filters.add(in(Id.fieldName, params.get(Id.paramName)
                .stream().map(ObjectId::new).collect(Collectors.toList())));
    };

    private static List<Double> mapToDoubleList(List<String> items) {
        return items.stream().map(Double::valueOf).collect(Collectors.toList());
    }
}
