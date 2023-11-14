package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.Collection;

@UtilityClass
public final class OutputStreamUtil {

    public static ObjectOutputStream createObjectOutputStream(final String filePath) {
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            return new ObjectOutputStream(bufferedOutputStream);
        } catch (final IOException cause) {
            throw new OutputStreamException(cause);
        }
    }

    public static void writeObjects(final ObjectOutputStream outputStream, final Collection<?> objects) {
        objects.forEach(object -> writeObject(outputStream, object));
    }

    public static void writeObject(final ObjectOutputStream outputStream, final Object object) {
        try {
            outputStream.writeObject(object);
        } catch (final IOException cause) {
            throw new OutputStreamException(cause);
        }
    }

    public static void closeStream(final OutputStream outputStream) {
        try {
            outputStream.close();
        } catch (final IOException cause) {
            throw new OutputStreamException(cause);
        }
    }

    private static final class OutputStreamException extends RuntimeException {

        @SuppressWarnings("unused")
        public OutputStreamException() {

        }

        @SuppressWarnings("unused")
        public OutputStreamException(final String description) {
            super(description);
        }

        public OutputStreamException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public OutputStreamException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
