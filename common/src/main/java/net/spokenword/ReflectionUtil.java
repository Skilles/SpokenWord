package net.spokenword;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ReflectionUtil {

    public static Class<?> getInnerClass(Field field) {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length != 1 || !(actualTypeArguments[0] instanceof Class<?> listType)) {
            throw new IllegalArgumentException("Unable to determine the type of list since generic is erased");
        }

        return listType;
    }

    public static boolean isValidForList(Object obj, List<?> list) {
        if (list.isEmpty()) {
            return true; // Can't determine type from an empty list
        }

        var firstObj = list.get(0);
        if (firstObj instanceof Block && obj instanceof Block) {
            return true;
        }

        if (firstObj instanceof Item && obj instanceof Item) {
            return true;
        }

        if (firstObj.getClass().equals(obj.getClass())) {
            return true;
        }

        return firstObj.getClass().isAssignableFrom(obj.getClass()) || obj.getClass().isAssignableFrom(firstObj.getClass());
    }
}
