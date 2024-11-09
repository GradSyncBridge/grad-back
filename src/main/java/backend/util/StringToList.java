package backend.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringToList {
    public static List<Integer> convert(String jsonList) {
        String[] list = jsonList.substring(1, jsonList.length() - 1).split(",");
        return Arrays.stream(list)
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
