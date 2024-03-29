package com.pengsoft.support.util;

import java.lang.reflect.ParameterizedType;

/**
 * The {@link Class} utility methods.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class ClassUtils extends org.apache.commons.lang3.ClassUtils {

    /**
     * Returns the generic type of the class's superclass.
     *
     * @param clazz the class
     * @param index the index of arguments.
     * @return The generic type.
     */
    public static Class<?> getSuperclassGenericType(final Class<?> clazz, final int index) {
        final var genericSuperclass = clazz.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            return null;
        }
        final var typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (index >= typeArguments.length || index < 0) {
            return null;
        }
        if (!(typeArguments[index] instanceof Class)) {
            return null;
        }
        return (Class<?>) typeArguments[index];
    }

}
