//package by.aurorasoft.updatesobserver.service;
//
//import by.aurorasoft.updatesobserver.model.ServerUpdate;
//import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//import static java.time.Instant.now;
//import static java.time.Instant.parse;
//import static java.util.Optional.empty;
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ServerUpdateServiceTest {
//
//    @Mock
//    private ServerUpdateStorage mockedUpdateStorage;
//
//    private ServerUpdateService updateService;
//
//    @Before
//    public void initializeUpdateService() {
//        this.updateService = new ServerUpdateService(this.mockedUpdateStorage);
//    }
//
//    @Test
//    public void updateShouldBeSaved() {
//        final ServerUpdate givenUpdate = new ServerUpdate(
//                "server",
//                parse("2023-11-03T10:15:30.00Z"),
//                10,
//                15
//        );
//
//        this.updateService.save(givenUpdate);
//
//        verify(this.mockedUpdateStorage, times(1)).save(same(givenUpdate));
//    }
//
//    @Test
//    public void aliveUpdateShouldBeFound() {
//        final String givenServerName = "server";
//        final ServerUpdate givenUpdate = new ServerUpdate(
//                givenServerName,
//                now(),
//                10,
//                15
//        );
//
//        when(this.mockedUpdateStorage.findByServerName(same(givenServerName))).thenReturn(Optional.of(givenUpdate));
//
//        final Optional<ServerUpdate> optionalActual = this.updateService.findAliveUpdate(givenServerName);
//        assertTrue(optionalActual.isPresent());
//        final ServerUpdate actual = optionalActual.get();
//        assertSame(givenUpdate, actual);
//    }
//
//    @Test
//    public void aliveUpdateShouldNotBeFoundBecauseOfNoUpdateWithGivenServerName() {
//        final String givenServerName = "server";
//
//        when(this.mockedUpdateStorage.findByServerName(same(givenServerName))).thenReturn(empty());
//
//        final Optional<ServerUpdate> optionalActual = this.updateService.findAliveUpdate(givenServerName);
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void aliveUpdateShouldNotBeFoundBecauseOfExistingUpdateWithGivenServerNameIsNotAlive() {
//        final String givenServerName = "server";
//        final ServerUpdate givenUpdate = new ServerUpdate(
//                givenServerName,
//                parse("2023-11-13T10:57:30.00Z"),
//                10,
//                15
//        );
//
//        when(this.mockedUpdateStorage.findByServerName(same(givenServerName))).thenReturn(Optional.of(givenUpdate));
//
//        final Optional<ServerUpdate> optionalActual = this.updateService.findAliveUpdate(givenServerName);
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void allUpdatesShouldBeFound() {
//        final ServerUpdate firstGivenUpdate = new ServerUpdate(
//                "first-server",
//                parse("2023-11-13T10:15:30.00Z"),
//                5,
//                10
//        );
//        final ServerUpdate secondGivenUpdate = new ServerUpdate(
//                "second-server",
//                parse("2023-11-13T11:15:30.00Z"),
//                6,
//                11
//        );
//        final ServerUpdate thirdGivenUpdate = new ServerUpdate(
//                "third-server",
//                parse("2023-11-13T12:15:30.00Z"),
//                7,
//                12
//        );
//        final List<ServerUpdate> givenUpdates = List.of(firstGivenUpdate, secondGivenUpdate, thirdGivenUpdate);
//
//        when(this.mockedUpdateStorage.findAll()).thenReturn(givenUpdates);
//
//        final Collection<ServerUpdate> actual = this.updateService.findAll();
//        assertSame(givenUpdates, actual);
//    }
//}
