package util;

import org.apache.maven.shared.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import static util.Constants.*;
import static java.util.stream.Collectors.*;

public class Utils {
    public static Map<String, List<String>> splitQueryList(String query) {
        if (!StringUtils.isNotBlank(query)) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> collect = Pattern.compile(URI_PARAM_SEPARATOR)
                .splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split(EQUALS), PARAM_ARRAY_SIZE))
                .collect(groupingBy(s -> decode(s[0]),
                        mapping(s -> decode(s[1]), toList())));

        return collect;

    }

    public static Map<String, String> splitQuery(String query) {
        if (!StringUtils.isNotBlank(query)) {
            return Collections.emptyMap();
        }
        return Pattern.compile(URI_PARAM_SEPARATOR).splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split(EQUALS), PARAM_ARRAY_SIZE))
                .collect(Collectors.toMap(p -> p[0], p -> p[1]));
    }

    public static String getIdFromPath(String path) {
        if (StringUtils.isNotBlank(path)) {
            return null;
        }
        String[] pathElements = path.split(PATH_SEPARATOR);
        return pathElements[pathElements.length - 1];
    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, UTF_8_ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
}
