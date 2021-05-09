package util;

import org.apache.maven.shared.utils.StringUtils;
import org.bson.types.ObjectId;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import static org.apache.commons.lang3.StringUtils.*;
import static util.Constants.*;
import static java.util.stream.Collectors.*;

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

        return Pattern.compile(URI_PARAM_SEPARATOR).splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split(EQUALS), PARAM_ARRAY_SIZE))
                .collect(Collectors.toMap(p -> decode(p[0]), p -> decode(p[1])));
    }

    public static String getIdFromPath(String path) {
        if (!StringUtils.isNotBlank(path)) {
            return EMPTY;
        }
        String[] pathElements = path.split(PATH_SEPARATOR);
        String optionalId = pathElements[pathElements.length - 1];
        return ObjectId.isValid(optionalId) ? optionalId : EMPTY;
    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, UTF_8_ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
}
