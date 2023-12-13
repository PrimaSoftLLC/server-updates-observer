package by.aurorasoft.outagetracker.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class ServerOutageControllerMockMvc {

    private static final String CONTROLLER_URL = "/serverOutage";
    private static final String REQUEST_PARAM_NAME_SERVER_NAME = "serverName";
    private static final String REQUEST_PARAM_NAME_DOWNTIME = "downtimeMinutes";
    private static final String REQUEST_PARAM_NAME_EXTRA_LIFETIME = "extraLifetimeMinutes";

    private final MockMvc mockMvc;

    public void postExpectNoContent(String serverName, long downtimeMinutes, long extraLifetimeMinutes) {
        performPostRequest(serverName, downtimeMinutes, extraLifetimeMinutes, StatusResultMatchers::isNoContent);
    }

    public String postExpectNotAcceptable(String serverName, long downtimeMinutes, long extraLifetimeMinutes) {
        return performPostRequest(serverName, downtimeMinutes, extraLifetimeMinutes, StatusResultMatchers::isNotAcceptable);
    }

    public String getExpectOk(String serverName) {
        return performGetRequest(serverName, StatusResultMatchers::isOk);
    }

    public void getExpectNoContent(String serverName) {
        performGetRequest(serverName, StatusResultMatchers::isNoContent);
    }

    public String getExpectNotAcceptable(String serverName) {
        return performGetRequest(serverName, StatusResultMatchers::isNotAcceptable);
    }

    public void deleteExpectNoContent(String serverName) {
        performDeleteRequest(serverName, StatusResultMatchers::isNoContent);
    }

    public String deleteExpectNotAcceptable(String serverName) {
        return performDeleteRequest(serverName, StatusResultMatchers::isNotAcceptable);
    }

    private String performPostRequest(String serverName, Long downtimeMinutes, Long extraLifetimeMinutes, ResultHttpStatusConfigurer statusExpectation) {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CONTROLLER_URL)
                .contentType(APPLICATION_JSON)
                .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName)
                .param(REQUEST_PARAM_NAME_DOWNTIME, downtimeMinutes != null ? Long.toString(downtimeMinutes) : null)
                .param(REQUEST_PARAM_NAME_EXTRA_LIFETIME, extraLifetimeMinutes != null ? Long.toString(extraLifetimeMinutes) : null);

        return performRequest(requestBuilder, statusExpectation);
    }

    private String performGetRequest(String serverName, ResultHttpStatusConfigurer statusExpectation) {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTROLLER_URL)
                .contentType(APPLICATION_JSON)
                .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName);

        return performRequest(requestBuilder, statusExpectation);
    }

    private String performDeleteRequest(String serverName, ResultHttpStatusConfigurer statusExpectation) {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CONTROLLER_URL)
                .contentType(APPLICATION_JSON)
                .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName);

        return performRequest(requestBuilder, statusExpectation);
    }

    @SneakyThrows
    private String performRequest(MockHttpServletRequestBuilder requestBuilder, ResultHttpStatusConfigurer statusExpectation) {
        return mockMvc.perform(requestBuilder)
                .andExpect(statusExpectation.configure(status()))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    @FunctionalInterface
    private interface ResultHttpStatusConfigurer {
        ResultMatcher configure(StatusResultMatchers matchers);
    }
}
