package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.iterate;

@UtilityClass
public final class ReflectionUtil {

    public static <T> T createObject(final Class<T> objectType,
                                     final Class<?>[] constructorParameterTypes,
                                     final Object[] constructorParameterValues) {
        try {
            final Constructor<T> constructor = objectType.getDeclaredConstructor(constructorParameterTypes);
            constructor.setAccessible(true);
            try {
                return constructor.newInstance(constructorParameterValues);
            } finally {
                constructor.setAccessible(false);
            }
        } catch (final Exception cause) {
            throw new RuntimeException(cause);
        }
    }

    public static <T> void setProperty(final T object,
                                       final String fieldName,
                                       final Object propertyValue) {
        try {
            final Field field = findField(object, fieldName);
            field.setAccessible(true);
            try {
                field.set(object, propertyValue);
            } finally {
                field.setAccessible(false);
            }
        } catch (final Exception cause) {
            throw new RuntimeException(cause);
        }
    }

    public static void setStaticFieldValue(final Class<?> owner, final String fieldName, final Object value) {
        try {
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            try {
                final Unsafe unsafe = (Unsafe) unsafeField.get(null);
                final Field ourField = owner.getDeclaredField(fieldName);
                final Object staticFieldBase = unsafe.staticFieldBase(ourField);
                final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
                unsafe.putObject(staticFieldBase, staticFieldOffset, value);
            } finally {
                unsafeField.setAccessible(false);
            }
        } catch (final Exception cause) {
            throw new RuntimeException(cause);
        }
    }

    public static <S, P> P findProperty(final S source, final String fieldName, final Class<P> propertyType) {
        return findFieldValue(
                name -> findField(source, fieldName),
                fieldName,
                field -> findFieldValue(source, field),
                propertyType
        );
    }

    public static <V> V findStaticFieldValue(final Class<?> type, final String fieldName, final Class<V> valueType) {
        return findFieldValue(
                name -> findDeclaredField(type, name),
                fieldName,
                ReflectionUtil::findStaticFieldValue,
                valueType
        );
    }

    private static <V> V findFieldValue(final Function<String, Field> fieldByNameExtractor,
                                        final String fieldName,
                                        final Function<Field, Object> valueByFieldExtractor,
                                        final Class<V> valueType) {
        final Field field = fieldByNameExtractor.apply(fieldName);
        field.setAccessible(true);
        try {
            final Object value = valueByFieldExtractor.apply(field);
            return valueType.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }

    private static Field findField(final Object source, final String fieldName) {
        final Class<?> sourceType = source.getClass();
        return findInheritanceStream(sourceType)
                .flatMap(ReflectionUtil::findDeclaredFields)
                .filter(field -> Objects.equals(field.getName(), fieldName))
                .findAny()
                .orElseThrow(
                        () -> new NoSuchFieldException(
                                "%s doesn't have field %s".formatted(sourceType, fieldName)
                        )
                );
    }

    private static Stream<Class<?>> findInheritanceStream(final Class<?> root) {
        return iterate(
                root,
                type -> type != Object.class,
                Class::getSuperclass
        );
    }

    private static Stream<Field> findDeclaredFields(final Class<?> type) {
        final Field[] fields = type.getDeclaredFields();
        return stream(fields);
    }

    private static Field findDeclaredField(final Class<?> type, final String fieldName) {
        try {
            return type.getDeclaredField(fieldName);
        } catch (final java.lang.NoSuchFieldException exception) {
            throw new NoSuchFieldException(exception);
        }
    }

    private static Object findStaticFieldValue(final Field field) {
        return findFieldValue(null, field);
    }

    private static Object findFieldValue(final Object source, final Field field) {
        try {
            return field.get(source);
        } catch (final IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static final class NoSuchFieldException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoSuchFieldException() {

        }

        public NoSuchFieldException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoSuchFieldException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoSuchFieldException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
