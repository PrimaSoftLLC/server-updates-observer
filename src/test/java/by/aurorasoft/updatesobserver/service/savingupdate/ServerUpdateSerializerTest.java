package by.aurorasoft.updatesobserver.service.savingupdate;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.util.OutputStreamUtil;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.io.ObjectOutputStream;
import java.util.List;

import static by.aurorasoft.updatesobserver.util.OutputStreamUtil.createObjectOutputStream;
import static by.aurorasoft.updatesobserver.util.OutputStreamUtil.writeObjects;
import static by.aurorasoft.updatesobserver.util.ReflectionUtil.findProperty;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public final class ServerUpdateSerializerTest {
    private static final String FIELD_NAME_OUTPUT_STREAM = "outputStream";

    @Test
    public void serializerShouldBeCreated() {
        try (final MockedStatic<OutputStreamUtil> mockedStreamUtil = mockStatic(OutputStreamUtil.class)) {
            final String givenFilePath = "file-path";

            final ObjectOutputStream givenOutputStream = mock(ObjectOutputStream.class);
            mockedStreamUtil.when(() -> createObjectOutputStream(same(givenFilePath))).thenReturn(givenOutputStream);

            final ServerUpdateSerializer actual = new ServerUpdateSerializer(givenFilePath);
            final ObjectOutputStream actualOutputStream = findOutputStream(actual);
            assertSame(givenOutputStream, actualOutputStream);
        }
    }

    @Test
    public void updatesShouldBeSerialized() {
        try (final MockedStatic<OutputStreamUtil> mockedStreamUtil = mockStatic(OutputStreamUtil.class)) {
            final ObjectOutputStream givenOutputStream = mock(ObjectOutputStream.class);
            final ServerUpdateSerializer givenSerializer = createSerializer(givenOutputStream, mockedStreamUtil);

            final List<ServerUpdate> givenUpdates = List.of(
                    createUpdate("first-server"),
                    createUpdate("second-server")
            );
            givenSerializer.serialize(givenUpdates);

            mockedStreamUtil.verify(() -> writeObjects(same(givenOutputStream), same(givenUpdates)));
        }
    }

    private static ObjectOutputStream findOutputStream(final ServerUpdateSerializer serializer) {
        return findProperty(
                serializer,
                FIELD_NAME_OUTPUT_STREAM,
                ObjectOutputStream.class
        );
    }

    private static ServerUpdateSerializer createSerializer(final ObjectOutputStream outputStream,
                                                           final MockedStatic<OutputStreamUtil> mockedStreamUtil) {
        final String givenFilePath = "file-path";
        mockedStreamUtil.when(() -> createObjectOutputStream(same(givenFilePath))).thenReturn(outputStream);
        return new ServerUpdateSerializer(givenFilePath);
    }

    private static ServerUpdate createUpdate(final String serverName) {
        return ServerUpdate.builder()
                .serverName(serverName)
                .build();
    }
}
