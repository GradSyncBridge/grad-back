package backend.util;

import java.lang.reflect.Field;
import java.util.*;

public class RequestFieldsGenerator {
    public static Map<String, Boolean> requestFields(Class<?> className, List<String> requirements){
        Field[] fields = className.getDeclaredFields();

        Map<String, Boolean> requirementMap = new HashMap<>();
        for(Field field: fields) requirementMap.put(field.getName(), false);

        for(String varName: requirements) requirementMap.put(varName, true);

        return requirementMap;
    }

}
