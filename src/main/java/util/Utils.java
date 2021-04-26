package util;

import org.apache.maven.shared.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


import static util.Constants.*;
import static java.util.stream.Collectors.*;

public class Utils {

    public static Map<String, List<String>> splitQuery(String query) {
        if (!StringUtils.isNotBlank(query)) {
            return Collections.emptyMap();
        }
        return Pattern.compile(URI_PARAM_SEPARATOR).splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split(EQUALS), PARAM_ARRAY_SIZE))
                .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));

    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, UTF_8_ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
}
