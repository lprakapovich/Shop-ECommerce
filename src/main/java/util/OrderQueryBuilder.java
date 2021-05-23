package util;

import lombok.AllArgsConstructor;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Filter;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Updates.combine;
import static util.OrderQueryBuilder.QueryParam.*;

public class OrderQueryBuilder {

   private static List<Bson> filters;

    public static Bson buildQuery(Map<String, List<String>> requestParams) {
        filters = new ArrayList<>();
        Set<String> keys = requestParams.keySet();

        if (!keys.isEmpty()) {
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

    @AllArgsConstructor
    enum QueryParam {
        Id("id", "_id", IdApplier),
        Status("status", "status", StatusApplier),
        Issuer("issuer", "issuer.email", IssuerApplier);

        private final String paramName;
        private final String fieldName;
        private final FilterApplier filterApplier;
    }

    public static final FilterApplier IdApplier = (params) -> {
        filters.add(in(Id.fieldName, params.get(Id.paramName).stream().map(ObjectId::new).collect(Collectors.toList())));
    };

    public static final FilterApplier StatusApplier = (params) -> {
        filters.add(in(Status.fieldName, params.get(Status.paramName)));
    };

    public static final FilterApplier IssuerApplier = (params) -> {
        filters.add(eq(Issuer.fieldName, params.get(Issuer.paramName).get(0)));
    };
}
