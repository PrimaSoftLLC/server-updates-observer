//package by.aurorasoft.updatesobserver.storage.factory;
//
//import by.aurorasoft.updatesobserver.model.ServerUpdate;
//import by.aurorasoft.updatesobserver.util.DeserializationUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.List;
//
//import static by.aurorasoft.updatesobserver.util.DeserializationUtil.readObjects;
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.mockStatic;
//import static org.mockito.Mockito.same;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ServerUpdateLoaderTest {
//    private static final String GIVEN_FILE_PATH = "file-path";
//
//    @Mock
//    private ObjectMapper mockedObjectMapper;
//
//
//
//    private final ServerUpdateLoader loader = new ServerUpdateLoader(GIVEN_FILE_PATH);
//
//    @Test
//    public void updatesShouldBeLoaded() {
//        try (final MockedStatic<DeserializationUtil> mockedDeserializationUtil = mockStatic(DeserializationUtil.class)) {
//            final List<ServerUpdate> givenUpdates = List.of(
//                    createUpdate("first-server"),
//                    createUpdate("second-server")
//            );
//            mockedDeserializationUtil.when(() -> readObjects(same(GIVEN_FILE_PATH), same(ServerUpdate.class)))
//                    .thenReturn(givenUpdates);
//
//            final List<ServerUpdate> actual = this.loader.load();
//            assertSame(givenUpdates, actual);
//        }
//    }
//
//    private static ServerUpdate createUpdate(final String serverName) {
//        return ServerUpdate.builder()
//                .serverName(serverName)
//                .build();
//    }
//}
