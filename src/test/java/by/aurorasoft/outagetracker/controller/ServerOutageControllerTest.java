package by.aurorasoft.outagetracker.controller;

import by.aurorasoft.outagetracker.model.ServerOutage;
import by.aurorasoft.outagetracker.service.ServerOutageService;
import by.aurorasoft.outagetracker.service.factory.ServerOutageFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static by.aurorasoft.outagetracker.controller.RestErrorResponse.Fields.httpStatus;
import static by.aurorasoft.outagetracker.controller.RestErrorResponse.Fields.message;
import static java.time.Instant.parse;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ServerOutageController.class)
public class ServerOutageControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String GIVEN_SERVER_NAME = "server";
    private static final String GIVEN_EMPTY_SERVER_NAME = "    ";
    private static final long GIVEN_DOWNTIME = 10;
    private static final long GIVEN_EXTRA_LIFETIME = 15;

    private static final ServerOutage givenUpdate = ServerOutage.builder()
            .serverName(GIVEN_SERVER_NAME)
            .downtime(parse("2023-11-16T10:48:30Z"))
            .lifetime(parse("2023-11-16T10:50:30Z"))
            .build();


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServerOutageFactory factory;

    @MockBean
    private ServerOutageService service;

    private ServerOutageControllerMockMvc mockHttp;

    @Before
    public void setUp() {
        this.mockHttp = new ServerOutageControllerMockMvc(mockMvc);
    }

    @Test
    public void create_ok() {
        mockFactory();

        mockHttp.postExpectNoContent(GIVEN_SERVER_NAME, GIVEN_DOWNTIME, GIVEN_EXTRA_LIFETIME);

        verify(service, times(1)).save(same(givenUpdate));
    }

    @Test
    public void create_blankServerName_shouldResponseWithErrorMessage() {
        String actual = mockHttp.postExpectNotAcceptable(GIVEN_EMPTY_SERVER_NAME, GIVEN_DOWNTIME, GIVEN_EXTRA_LIFETIME);

        assertNotAcceptableResponse(actual, "create.serverName: must not be blank");

        verifyNoInteractions(factory);
        verifyNoInteractions(service);
    }

    @Test
    public void create_notValidDowntime_shouldResponseWithErrorMessage() {
        int givenDowntime = 0;

        String actual = mockHttp.postExpectNotAcceptable(GIVEN_SERVER_NAME, givenDowntime, GIVEN_EXTRA_LIFETIME);

        assertNotAcceptableResponse(actual, "create.downtimeMinutes: must be greater than or equal to 1");

        verifyNoInteractions(factory);
        verifyNoInteractions(service);
    }

    @Test
    public void create_notValidExtraLifetime_shouldResponseWithErrorMessage() {
        int givenExtraLifetime = 0;

        String actual = mockHttp.postExpectNotAcceptable(GIVEN_SERVER_NAME, GIVEN_DOWNTIME, givenExtraLifetime);

        assertNotAcceptableResponse(actual, "create.extraLifetimeMinutes: must be greater than or equal to 1");

        verifyNoInteractions(this.factory);
        verifyNoInteractions(this.service);
    }

    @Test
    public void get_ok() {
        Instant givenDowntime = parse("2023-11-16T10:50:30Z");

        when(service.getDowntime(eq(GIVEN_SERVER_NAME))).thenReturn(Optional.of(givenDowntime));

        String actual = mockHttp.getExpectOk(GIVEN_SERVER_NAME);

        assertEquals("\"2023-11-16T10:50:30Z\"", actual);
    }

    @Test
    public void get_serverNameNotExist_shouldResponseWithErrorMessage() {
        String notExistServerName = "not-exist";
        
        when(service.getDowntime(eq(notExistServerName))).thenReturn(empty());

        mockHttp.getExpectNoContent(notExistServerName);
    }

    @Test
    public void get_serverNameIsBlank_shouldResponseWithErrorMessage() {
        String actual = mockHttp.getExpectNotAcceptable(GIVEN_EMPTY_SERVER_NAME);

        assertNotAcceptableResponse(actual, "getDowntime.serverName: must not be blank");

        verifyNoInteractions(this.service);
    }

    @Test
    public void delete_ok() {
        mockHttp.deleteExpectNoContent(GIVEN_SERVER_NAME);
    }


    @Test
    public void delete_serverNameIsBlank_shouldResponseWithErrorMessage() {
        String actual = mockHttp.deleteExpectNotAcceptable(GIVEN_EMPTY_SERVER_NAME);

        assertNotAcceptableResponse(actual, "remove.serverName: must not be blank");

        verifyNoInteractions(service);
    }


    @SneakyThrows
    private void assertNotAcceptableResponse(String actualResponse, String expectedMessage) {
        //noinspection unchecked
        Map<String, Object> responseMap = objectMapper.readValue(actualResponse, Map.class);

        assertEquals("NOT_ACCEPTABLE", responseMap.get(httpStatus));
        assertEquals(expectedMessage, responseMap.get(message));
        String time = responseMap.get(RestErrorResponse.Fields.time).toString();
        assertEquals(Instant.now().getEpochSecond(), Instant.parse(time).getEpochSecond(), 30);
    }


    private void mockFactory() {
        when(factory.create(anyString(), anyLong(), anyLong())).thenReturn(givenUpdate);
    }
}
