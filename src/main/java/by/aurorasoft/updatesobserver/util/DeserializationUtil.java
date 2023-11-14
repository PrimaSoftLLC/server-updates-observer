package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.stream.Stream.generate;

@UtilityClass
public final class DeserializationUtil {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static <T> List<T> readObjects(final String filePath, final Class<T> objectType) {
        try (final ObjectInputStream inputStream = createObjectInputStream(filePath)) {
            return generate(() -> readObjectIfExist(inputStream, objectType))
                    .takeWhile(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (final EOFException noObjectsInFileException) {
            return emptyList();
        } catch (final IOException cause) {
            throw new DeserializationException(cause);
        }
    }

    private static ObjectInputStream createObjectInputStream(final String filePath)
            throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(filePath);
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        return new ObjectInputStream(bufferedInputStream);
    }

    private static <T> Optional<T> readObjectIfExist(final ObjectInputStream inputStream, final Class<T> objectType) {
        try {
            final Object object = inputStream.readObject();
            final T concreteObject = objectType.cast(object);
            return Optional.of(concreteObject);
        } catch (final EOFException endDeserializationException) {
            return empty();
        } catch (final IOException | ClassNotFoundException cause) {
            throw new DeserializationException(cause);
        }
    }

    static final class DeserializationException extends RuntimeException {

        @SuppressWarnings("unused")
        public DeserializationException() {

        }

        @SuppressWarnings("unused")
        public DeserializationException(final String description) {
            super(description);
        }

        public DeserializationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public DeserializationException(final String description, final Exception cause) {
            super(description, cause);
        }
    }

}
