package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.stream.Stream.generate;

@UtilityClass
public final class InputStreamUtil {

    public static ObjectInputStream createObjectInputStream(final String filePath) {
        try {
            final FileInputStream fileInputStream = new FileInputStream(filePath);
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            return new ObjectInputStream(bufferedInputStream);
        } catch (final IOException cause) {
            throw new InputStreamException(cause);
        }
    }

    public static void closeStream(final InputStream inputStream) {
        try {
            inputStream.close();
        } catch (final IOException cause) {
            throw new InputStreamException(cause);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static <T> List<T> readObjects(final ObjectInputStream inputStream, final Class<T> objectType) {
        return generate(() -> readObjectIfExist(inputStream, objectType))
                .takeWhile(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public static <T> Optional<T> readObjectIfExist(final ObjectInputStream inputStream, final Class<T> objectType) {
        try {
            final Object object = inputStream.readObject();
            final T concreteObject = objectType.cast(object);
            return Optional.of(concreteObject);
        } catch (final EOFException endDeserializationException) {
            return empty();
        } catch (final IOException | ClassNotFoundException cause) {
            throw new InputStreamException(cause);
        }
    }

    private static final class InputStreamException extends RuntimeException {

        @SuppressWarnings("unused")
        public InputStreamException() {

        }

        @SuppressWarnings("unused")
        public InputStreamException(final String description) {
            super(description);
        }

        public InputStreamException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public InputStreamException(final String description, final Exception cause) {
            super(description, cause);
        }
    }

}
