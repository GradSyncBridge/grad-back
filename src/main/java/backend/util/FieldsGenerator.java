package backend.util;

import java.lang.reflect.Field;
import java.util.*;

public class FieldsGenerator {
    public static Map<String, Boolean> generateFields(Class<?> className, List<String> requirements){
        Field[] fields = className.getDeclaredFields();

        Map<String, Boolean> requirementMap = new HashMap<>();
        for(Field field: fields) requirementMap.put(field.getName(), false);

        for(String varName: requirements) requirementMap.put(varName, true);

        return requirementMap;
    }

    public static Map<String, Boolean> generateFieldsBindValues(Class<?> className, List<String> requirements, Object object){
        Map<String, Boolean> requirementMap = generateFields(className, requirements);
        Map<String, Object> valueMap = new HashMap<>();

        for(String varName: requirements){
            try{
                Field field = className.getDeclaredField(varName);
                field.setAccessible(true);
                valueMap.put(varName, field.get(object));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return requirementMap;
    }

}
