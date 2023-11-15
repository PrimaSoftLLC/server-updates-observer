//package by.aurorasoft.updatesobserver.storage.factory;
//
//import by.aurorasoft.updatesobserver.model.ServerUpdate;
//import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.List;
//import java.util.Map;
//
//import static by.aurorasoft.updatesobserver.util.ReflectionUtil.findProperty;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ServerUpdateStorageFactoryTest {
//    private static final String FIELD_NAME_UPDATES_BY_SERVER_NAMES = "updatesByServerNames";
//
//    @Mock
//    private ServerUpdateLoader mockedUpdateLoader;
//
//    private ServerUpdateStorageFactory updateStorageFactory;
//
//    @Before
//    public void initializeUpdateStorageFactory() {
//        this.updateStorageFactory = new ServerUpdateStorageFactory(this.mockedUpdateLoader);
//    }
//
//    @Test
//    public void storageShouldBeCreated() {
//        final ServerUpdate firstGivenUpdate = createUpdate("first-server");
//        final ServerUpdate secondGivenUpdate = createUpdate("second-server");
//        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);
//        when(this.mockedUpdateLoader.load()).thenReturn(givenUpdates);
//
//        final ServerUpdateStorage actual = this.updateStorageFactory.create();
//        final Map<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(actual);
//        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(
//                firstGivenUpdate.getServerName(), firstGivenUpdate,
//                secondGivenUpdate.getServerName(), secondGivenUpdate
//        );
//        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);
//    }
//
//    private static ServerUpdate createUpdate(final String serverName) {
//        return ServerUpdate.builder()
//                .serverName(serverName)
//                .build();
//    }
//
//    @SuppressWarnings("unchecked")
//    private static Map<String, ServerUpdate> findUpdatesByServerNames(final ServerUpdateStorage storage) {
//        return findProperty(
//                storage,
//                FIELD_NAME_UPDATES_BY_SERVER_NAMES,
//                Map.class
//        );
//    }
//}
