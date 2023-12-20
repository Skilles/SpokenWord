package net.spokenword;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class ReflectionUtil {

    public static Class<?> getInnerClass(Field field) {
        return (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }
}
