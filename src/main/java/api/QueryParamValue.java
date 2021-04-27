package api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QueryParamValue {

    BOOK("book"),
    GAME("game");

    private String value;
}
