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
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ServerUpdateStorageFactoryTest {
//    private static final String FIELD_NAME_UPDATES_BY_SERVER_NAMES = "updatesByServerNames";
//
//    private static final int GIVEN_STORAGE_MAX_SIZE = 10;
//
//    private static final long GIVEN_REMAINING_LIFETIME_IN_MILLIS = 500;
//
//    @Mock
//    private ServerUpdateLoader mockedUpdateLoader;
//
//    private ServerUpdateStorageFactory updateStorageFactory;
//
//    @Before
//    public void initializeUpdateStorageFactory() {
//        this.updateStorageFactory = new ServerUpdateStorageFactory(this.mockedUpdateLoader, GIVEN_STORAGE_MAX_SIZE);
//    }
//
//    @Test
//    public void storageShouldBeCreated() {
//        final String firstGivenUpdateServerName = "first-server";
//        final ServerUpdate firstGivenUpdate = createAliveUpdate(firstGivenUpdateServerName);
//
//        final String secondGivenUpdateServerName = "second-server";
//        final ServerUpdate secondGivenUpdate = createAliveUpdate(secondGivenUpdateServerName);
//
//        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate);
//        when(this.mockedUpdateLoader.load()).thenReturn(givenUpdates);
//
//        final ServerUpdateStorage actual = this.updateStorageFactory.create();
//        final Map<String, ServerUpdate> actualUpdatesByServerNames = findUpdatesByServerNames(actual);
//        final Map<String, ServerUpdate> expectedUpdatesByServerNames = Map.of(
//                firstGivenUpdateServerName, firstGivenUpdate,
//                secondGivenUpdateServerName, secondGivenUpdate
//        );
//        assertEquals(expectedUpdatesByServerNames, actualUpdatesByServerNames);
//    }
//
//    private static ServerUpdate createAliveUpdate(final String serverName) {
//        final ServerUpdate update = mock(ServerUpdate.class);
//        when(update.getServerName()).thenReturn(serverName);
//        when(update.isAlive()).thenReturn(true);
//        when(update.findRemainingLifetimeInMillisIfAlive()).thenReturn(GIVEN_REMAINING_LIFETIME_IN_MILLIS);
//        return update;
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
