package util;

import java.util.List;
import java.util.Map;

public interface FilterApplier {

    void apply(Map<String, List<String>> params);
}
