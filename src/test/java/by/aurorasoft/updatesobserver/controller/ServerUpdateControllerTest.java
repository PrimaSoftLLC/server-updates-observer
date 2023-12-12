package by.aurorasoft.updatesobserver.controller;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import by.aurorasoft.updatesobserver.service.factory.ServerUpdateFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.parse;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServerUpdateController.class)
public class ServerUpdateControllerTest {
    private static final String CONTROLLER_URL = "/serverUpdate";
    private static final String REQUEST_PARAM_NAME_SERVER_NAME = "serverName";
    private static final String REQUEST_PARAM_NAME_DOWNTIME = "downtime";
    private static final String REQUEST_PARAM_NAME_EXTRA_LIFETIME = "extraLifetime";

    private static final Charset RESPONSE_CHARSET = UTF_8;

    private static final long EXPECTED_DEFAULT_EXTRA_LIFETIME = 10;

    @MockBean
    private ServerUpdateFactory mockedUpdateFactory;

    @MockBean
    private ServerUpdateService mockedUpdateService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void updateShouldBeCreatedAndSaved() {
        final String givenServerName = "server";
        final long givenDowntimeInMinutes = 10;
        final long givenExtraLifetimeInMinutes = 15;

        final ServerUpdate givenUpdate = createUpdate(
                givenServerName,
                "2023-11-16T10:48:30Z",
                "2023-11-16T10:50:30Z"
        );
        when(
                this.mockedUpdateFactory.create(
                        eq(givenServerName),
                        eq(givenDowntimeInMinutes),
                        eq(givenExtraLifetimeInMinutes)
                )
        ).thenReturn(givenUpdate);

        final String actual = this.doRequestToCreateAndSaveUpdateExpectingNoContentStatus(
                givenServerName,
                givenDowntimeInMinutes,
                givenExtraLifetimeInMinutes
        );
        final String expected = "";
        assertEquals(expected, actual);

        verify(this.mockedUpdateService, times(1)).saveIfAlive(same(givenUpdate));
    }

    @Test
    public void updateShouldBeCreatedAndSavedIfAliveWithDefaultExtraLifetime() {
        final String givenServerName = "server";
        final long givenDowntimeInMinutes = 10;

        final ServerUpdate givenUpdate = createUpdate(
                givenServerName,
                "2023-11-16T10:48:30Z",
                "2023-11-16T10:50:30Z"
        );
        when(
                this.mockedUpdateFactory.create(
                        eq(givenServerName),
                        eq(givenDowntimeInMinutes),
                        eq(EXPECTED_DEFAULT_EXTRA_LIFETIME)
                )
        ).thenReturn(givenUpdate);

        final String actual = this.doRequestToCreateAndSaveUpdateExpectingNoContentStatus(
                givenServerName,
                givenDowntimeInMinutes
        );
        final String expected = "";
        assertEquals(expected, actual);

        verify(this.mockedUpdateService, times(1)).saveIfAlive(same(givenUpdate));
    }

    @Test
    public void updateShouldNotBeCreatedAndSavedIfAliveBecauseOfBlankServerName() {
        final String givenServerName = "     ";
        final int givenDowntimeInMinutes = 10;
        final int givenExtraLifetimeInMinutes = 15;

        final String actual = this.doRequestToCreateAndSaveUpdateExpectingNotAcceptableStatus(
                givenServerName,
                givenDowntimeInMinutes,
                givenExtraLifetimeInMinutes
        );
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"createAndSaveIfAlive\\.serverName: must not be blank\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actual.matches(expectedRegex));

        verifyNoInteractions(this.mockedUpdateFactory);
        verifyNoInteractions(this.mockedUpdateService);
    }

    @Test
    public void updateShouldNotBeCreatedAndSavedIfAliveBecauseOfNotValidDowntimeInMinutes() {
        final String givenServerName = "server";
        final int givenDowntimeInMinutes = 0;
        final int givenExtraLifetimeInMinutes = 15;

        final String actual = this.doRequestToCreateAndSaveUpdateExpectingNotAcceptableStatus(
                givenServerName,
                givenDowntimeInMinutes,
                givenExtraLifetimeInMinutes
        );
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"createAndSaveIfAlive\\.downtimeInMinutes: must be greater than or equal to 1\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actual.matches(expectedRegex));

        verifyNoInteractions(this.mockedUpdateFactory);
        verifyNoInteractions(this.mockedUpdateService);
    }

    @Test
    public void updateShouldNotBeCreatedAndSavedIfAliveBecauseOfNotValidExtraLifetimeInMinutes() {
        final String givenServerName = "server";
        final int givenDowntimeInMinutes = 10;
        final int givenExtraLifetimeInMinutes = 0;

        final String actual = this.doRequestToCreateAndSaveUpdateExpectingNotAcceptableStatus(
                givenServerName,
                givenDowntimeInMinutes,
                givenExtraLifetimeInMinutes
        );
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"createAndSaveIfAlive\\.extraLifetimeInMinutes: must be greater than or equal to 1\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actual.matches(expectedRegex));

        verifyNoInteractions(this.mockedUpdateFactory);
        verifyNoInteractions(this.mockedUpdateService);
    }

    @Test
    public void updateDowntimeShouldBeFound() {
        final String givenServerName = "server";

        final Instant givenDowntime = parse("2023-11-16T10:50:30Z");
        when(this.mockedUpdateService.findUpdateDowntime(eq(givenServerName))).thenReturn(Optional.of(givenDowntime));

        final String actual = this.doRequestToFindUpdateDowntimeExpectingOkStatus(givenServerName);
        final String expected = "\"2023-11-16T10:50:30Z\"";
        assertEquals(expected, actual);
    }

    @Test
    public void updateDowntimeShouldNotBeFound() {
        final String givenServerName = "server";

        when(this.mockedUpdateService.findUpdateDowntime(eq(givenServerName))).thenReturn(empty());

        final String actual = this.doRequestToFindUpdateDowntimeExpectingNoContentStatus(givenServerName);
        final String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void updateDowntimeShouldNotBeFoundBecauseOfBlankServerName() {
        final String givenServerName = "   ";

        final String actual = this.doRequestToFindUpdateDowntimeExpectingNotAcceptableStatus(givenServerName);
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"findUpdateDowntime\\.serverName: must not be blank\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actual.matches(expectedRegex));

        verifyNoInteractions(this.mockedUpdateService);
    }

    @Test
    public void updateShouldBeRemovedByServerName() {
        final String givenServerName = "server";

        final ServerUpdate givenUpdate = createUpdate(
                givenServerName,
                "2023-11-16T10:48:30Z",
                "2023-11-16T10:50:30Z"
        );

        when(this.mockedUpdateService.removeByServerName(same(givenServerName))).thenReturn(Optional.of(givenUpdate));

        final String actual = this.doRequestToRemoveUpdateExpectingOkStatus(givenServerName);
        final String expected = "{\"serverName\":\"server\","
                + "\"downtime\":\"2023-11-16T10:48:30Z\","
                + "\"lifetime\":\"2023-11-16T10:50:30Z\"}";
        assertEquals(expected, actual);
    }

    @Test
    public void updateShouldNotBeRemovedByServerName() {
        final String givenServerName = "server";

        when(this.mockedUpdateService.removeByServerName(same(givenServerName))).thenReturn(empty());

        final String actual = this.doRequestToRemoveUpdateExpectingNoContentStatus(givenServerName);
        final String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void updateShouldNotBeRemovedByServerNameBecauseOfBlankServerName() {
        final String givenServerName = "   ";

        final String actual = this.doRequestToRemoveUpdateExpectingNotAcceptableStatus(givenServerName);
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"removeByServerName\\.serverName: must not be blank\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actual.matches(expectedRegex));

        verifyNoInteractions(this.mockedUpdateService);
    }


    @SuppressWarnings("SameParameterValue")
    private static ServerUpdate createUpdate(final String serverName,
                                             final String downtimeAsString,
                                             final String lifetimeAsString) {
        final Instant downtime = parse(downtimeAsString);
        final Instant lifetime = parse(lifetimeAsString);
        return ServerUpdate.builder()
                .serverName(serverName)
                .downtime(downtime)
                .lifetime(lifetime)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private String doRequestToCreateAndSaveUpdateExpectingNoContentStatus(final String serverName,
                                                                          final long downtimeInMinutes,
                                                                          final long extraLifetimeInMinutes) {
        return this.doRequestToCreateAndSaveUpdate(
                serverName,
                downtimeInMinutes,
                extraLifetimeInMinutes,
                StatusResultMatchers::isNoContent
        );
    }

    private String doRequestToCreateAndSaveUpdateExpectingNotAcceptableStatus(final String serverName,
                                                                              final long downtimeInMinutes,
                                                                              final long extraLifetimeInMinutes) {
        return this.doRequestToCreateAndSaveUpdate(
                serverName,
                downtimeInMinutes,
                extraLifetimeInMinutes,
                StatusResultMatchers::isNotAcceptable
        );
    }

    @SuppressWarnings("SameParameterValue")
    private String doRequestToCreateAndSaveUpdateExpectingNoContentStatus(final String serverName,
                                                                          final long downtimeInMinutes) {
        final String downtimeInMinutesAsString = Long.toString(downtimeInMinutes);
        return this.doRequest(
                () -> post(CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName)
                        .param(REQUEST_PARAM_NAME_DOWNTIME, downtimeInMinutesAsString),
                StatusResultMatchers::isNoContent
        );
    }

    private String doRequestToCreateAndSaveUpdate(final String serverName,
                                                  final long downtimeInMinutes,
                                                  final long extraLifetimeInMinutes,
                                                  final ResultHttpStatusConfigurer resultHttpStatusConfigurer) {
        final String downtimeInMinutesAsString = Long.toString(downtimeInMinutes);
        final String extraLifetimeInMinutesAsString = Long.toString(extraLifetimeInMinutes);
        return this.doRequest(
                () -> post(CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName)
                        .param(REQUEST_PARAM_NAME_DOWNTIME, downtimeInMinutesAsString)
                        .param(REQUEST_PARAM_NAME_EXTRA_LIFETIME, extraLifetimeInMinutesAsString),
                resultHttpStatusConfigurer
        );
    }

    @SuppressWarnings("SameParameterValue")
    private String doRequestToFindUpdateDowntimeExpectingOkStatus(final String serverName) {
        return this.doRequestToFindUpdateDowntime(serverName, StatusResultMatchers::isOk);
    }

    @SuppressWarnings("SameParameterValue")
    private String doRequestToFindUpdateDowntimeExpectingNoContentStatus(final String serverName) {
        return this.doRequestToFindUpdateDowntime(serverName, StatusResultMatchers::isNoContent);
    }

    @SuppressWarnings("SameParameterValue")
    private String doRequestToFindUpdateDowntimeExpectingNotAcceptableStatus(final String serverName) {
        return this.doRequestToFindUpdateDowntime(serverName, StatusResultMatchers::isNotAcceptable);
    }

    private String doRequestToFindUpdateDowntime(final String serverName,
                                                 final ResultHttpStatusConfigurer resultHttpStatusConfigurer) {
        return this.doRequest(
                () -> get(CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName),
                resultHttpStatusConfigurer
        );
    }

    @SuppressWarnings("SameParameterValue")
    private String doRequestToRemoveUpdateExpectingOkStatus(final String serverName) {
        return this.doRequestToRemoveUpdate(serverName, StatusResultMatchers::isOk);
    }

    @SuppressWarnings("SameParameterValue")
    private String doRequestToRemoveUpdateExpectingNoContentStatus(final String serverName) {
        return this.doRequestToRemoveUpdate(serverName, StatusResultMatchers::isNoContent);
    }

    @SuppressWarnings("SameParameterValue")
    private String doRequestToRemoveUpdateExpectingNotAcceptableStatus(final String serverName) {
        return this.doRequestToRemoveUpdate(serverName, StatusResultMatchers::isNotAcceptable);
    }

    private String doRequestToRemoveUpdate(final String serverName,
                                           final ResultHttpStatusConfigurer resultHttpStatusConfigurer) {
        return this.doRequest(
                () -> delete(CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName),
                resultHttpStatusConfigurer
        );
    }

    private String doRequest(final RequestBuilderSupplier requestBuilderSupplier,
                             final ResultHttpStatusConfigurer resultHttpStatusConfigurer) {
        try {
            final MockHttpServletRequestBuilder requestBuilder = requestBuilderSupplier.get();
            return this.mockMvc.perform(requestBuilder)
                    .andExpect(resultHttpStatusConfigurer.configure(status()))
                    .andReturn()
                    .getResponse()
                    .getContentAsString(RESPONSE_CHARSET);
        } catch (final Exception cause) {
            throw new RuntimeException(cause);
        }
    }

    @FunctionalInterface
    private interface ResultHttpStatusConfigurer {
        ResultMatcher configure(final StatusResultMatchers matchers);
    }

    @FunctionalInterface
    private interface RequestBuilderSupplier extends Supplier<MockHttpServletRequestBuilder> {

    }
}
