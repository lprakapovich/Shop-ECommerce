package api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QueryParamName {

    PRODUCT_TYPE("type"),
    PRODUCT_ID("id");

    private String name;
}
