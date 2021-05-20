package util;

import exception.BadRequestException;
import org.apache.maven.shared.utils.StringUtils;
import org.bson.types.ObjectId;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static util.Constants.*;

public class Utils {
    public static Map<String, List<String>> splitQueryList(String query) {
        if (!StringUtils.isNotBlank(query)) {
            return Collections.emptyMap();
        }
        return Pattern.compile(URI_PARAM_SEPARATOR)
                .splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split(EQUALS), PARAM_ARRAY_SIZE))
                .collect(groupingBy(s -> decode(s[0]),
                        mapping(s -> decode(s[1]), toList())));
    }

    public static Map<String, String> splitQuery(String query) {
        if (!StringUtils.isNotBlank(query)) {
            return Collections.emptyMap();
        }
        return Pattern.compile(URI_PARAM_SEPARATOR)
                .splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split(EQUALS), PARAM_ARRAY_SIZE))
                .collect(Collectors.toMap(p -> decode(p[0]), p -> decode(p[1])));
    }

    public static String getIdFromPath(String path) {
        if (StringUtils.isBlank(path)) {
            return EMPTY;
        }
        String[] pathComponents = path.split(PATH_SEPARATOR);
        String optionalId = pathComponents[pathComponents.length - 1];
        return ObjectId.isValid(optionalId) ? optionalId : EMPTY;
    }

    public static boolean containsInPath(String path, String pathComponent) {
        if (StringUtils.isBlank(path)) {
            throw new BadRequestException("Invalid url");
        }
        return Arrays.asList(path.split(PATH_SEPARATOR)).contains(pathComponent);
    }

    private static String decode(final String encoded) {
        return encoded == null ? null : URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }
}
