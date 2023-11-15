//package by.aurorasoft.updatesobserver.service.factory;
//
//import org.junit.Test;
//import org.mockito.MockedStatic;
//
//import java.time.Instant;
//
//public final class ServerUpdateFactoryTest {
//    private final ServerUpdateFactory factory = new ServerUpdateFactory();
//
//    @Test
//    public void updateShouldBeCreated() {
//        try (final MockedStatic<Instant> mockedInstant = Mockito.mockStatic(StaticUtils.class)) {
//            utilities.when(StaticUtils::name).thenReturn("Eugen");
//            assertThat(StaticUtils.name()).isEqualTo("Eugen");
//        }
//    }
//}
