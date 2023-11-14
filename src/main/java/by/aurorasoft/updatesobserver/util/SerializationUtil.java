package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;

@UtilityClass
public final class SerializationUtil {

    public static void writeObjects(final String filePath, final Collection<?> objects) {
        try (final ObjectOutputStream outputStream = createObjectOutputStream(filePath)) {
            objects.forEach(object -> writeObject(outputStream, object));
        } catch (final IOException cause) {
            throw new SerializationException(cause);
        }
    }

    private static ObjectOutputStream createObjectOutputStream(final String filePath)
            throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        return new ObjectOutputStream(bufferedOutputStream);
    }

    private static void writeObject(final ObjectOutputStream outputStream, final Object object) {
        try {
            outputStream.writeObject(object);
        } catch (final IOException cause) {
            throw new SerializationException(cause);
        }
    }

    private static final class SerializationException extends RuntimeException {

        @SuppressWarnings("unused")
        public SerializationException() {

        }

        @SuppressWarnings("unused")
        public SerializationException(final String description) {
            super(description);
        }

        public SerializationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public SerializationException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
