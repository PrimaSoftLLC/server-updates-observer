package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.util.InputStreamUtil;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.io.ObjectInputStream;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.InputStreamUtil.*;
import static by.aurorasoft.updatesobserver.util.ReflectionUtil.findProperty;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class ServerUpdateDeserializerTest {
    private static final String FIELD_NAME_INPUT_STREAM = "inputStream";

    @Test
    public void deserializerShouldBeCreated() {
        try (final MockedStatic<InputStreamUtil> mockedStreamUtil = mockStatic(InputStreamUtil.class)) {
            final String givenFilePath = "file-path";

            final ObjectInputStream givenInputStream = mock(ObjectInputStream.class);
            mockedStreamUtil.when(() -> createObjectInputStream(same(givenFilePath))).thenReturn(givenInputStream);

            final ServerUpdateDeserializer actual = new ServerUpdateDeserializer(givenFilePath);
            final ObjectInputStream actualInputStream = findInputStream(actual);
            assertSame(givenInputStream, actualInputStream);
        }
    }

    @Test
    public void updatesShouldBeDeserialized() {
        try (final MockedStatic<InputStreamUtil> mockedStreamUtil = mockStatic(InputStreamUtil.class)) {
            final ObjectInputStream givenInputStream = mock(ObjectInputStream.class);
            final ServerUpdateDeserializer givenDeserializer = createDeserializer(givenInputStream, mockedStreamUtil);

            final List<ServerUpdate> givenUpdates = List.of(
                    createUpdate("first-server"),
                    createUpdate("second-server")
            );
            mockedStreamUtil.when(() -> readObjects(same(givenInputStream), same(ServerUpdate.class)))
                    .thenReturn(givenUpdates);

            final List<ServerUpdate> actual = givenDeserializer.deserialize();
            assertSame(givenUpdates, actual);
        }
    }

    @Test
    public void deserializerShouldBeClosed() {
        try (final MockedStatic<InputStreamUtil> mockedStreamUtil = mockStatic(InputStreamUtil.class)) {
            final ObjectInputStream givenInputStream = mock(ObjectInputStream.class);
            final ServerUpdateDeserializer givenDeserializer = createDeserializer(givenInputStream, mockedStreamUtil);

            givenDeserializer.close();

            mockedStreamUtil.verify(() -> closeStream(same(givenInputStream)), times(1));
        }
    }

    private static ObjectInputStream findInputStream(final ServerUpdateDeserializer deserializer) {
        return findProperty(
                deserializer,
                FIELD_NAME_INPUT_STREAM,
                ObjectInputStream.class
        );
    }

    private static ServerUpdateDeserializer createDeserializer(final ObjectInputStream inputStream,
                                                               final MockedStatic<InputStreamUtil> mockedStreamUtil) {
        final String givenFilePath = "file-path";
        mockedStreamUtil.when(() -> createObjectInputStream(same(givenFilePath))).thenReturn(inputStream);
        return new ServerUpdateDeserializer(givenFilePath);
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }
}
