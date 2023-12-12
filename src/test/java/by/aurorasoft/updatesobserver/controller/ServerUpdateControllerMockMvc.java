package by.aurorasoft.updatesobserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class ServerUpdateControllerMockMvc {

    private static final String CONTROLLER_URL = "/serverUpdate";
    private static final String REQUEST_PARAM_NAME_SERVER_NAME = "serverName";
    private static final String REQUEST_PARAM_NAME_DOWNTIME = "downtime";
    private static final String REQUEST_PARAM_NAME_EXTRA_LIFETIME = "extraLifetime";

    private final MockMvc mockMvc;

    @SuppressWarnings("SameParameterValue")
    public String postExpectNoContent(String serverName, long downtimeInMinutes, long extraLifetimeInMinutes) {
        return post(
                serverName,
                downtimeInMinutes,
                extraLifetimeInMinutes,
                StatusResultMatchers::isNoContent
        );
    }

    public String postExpectNotAcceptable(String serverName, long downtimeInMinutes, long extraLifetimeInMinutes) {
        return post(
                serverName,
                downtimeInMinutes,
                extraLifetimeInMinutes,
                StatusResultMatchers::isNotAcceptable
        );
    }

    @SuppressWarnings("SameParameterValue")
    public String postExpectNoContent(String serverName, long downtimeInMinutes) {
        String downtimeInMinutesAsString = Long.toString(downtimeInMinutes);
        return this.doRequest(
                () -> MockMvcRequestBuilders.post(CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName)
                        .param(REQUEST_PARAM_NAME_DOWNTIME, downtimeInMinutesAsString),
                StatusResultMatchers::isNoContent
        );
    }

    private String post(String serverName, long downtimeInMinutes, long extraLifetimeInMinutes,
                        ResultHttpStatusConfigurer resultHttpStatusConfigurer) {
        return this.doRequest(
                () -> MockMvcRequestBuilders.post(CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName)
                        .param(REQUEST_PARAM_NAME_DOWNTIME, Long.toString(downtimeInMinutes))
                        .param(REQUEST_PARAM_NAME_EXTRA_LIFETIME, Long.toString(extraLifetimeInMinutes)),
                resultHttpStatusConfigurer
        );
    }

    public String getExpectOk(String serverName) {
        return this.get(serverName, StatusResultMatchers::isOk);
    }


    public void getExpectNoContent(String serverName) {
        this.get(serverName, StatusResultMatchers::isNoContent);
    }

    public String getExpectNotAcceptable(String serverName) {
        return this.get(serverName, StatusResultMatchers::isNotAcceptable);
    }

    public String get(String serverName, ResultHttpStatusConfigurer resultHttpStatusConfigurer) {
        return this.doRequest(
                () -> MockMvcRequestBuilders.get(CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName),
                resultHttpStatusConfigurer);
    }

    public String deleteExpectNoContent(String serverName) {
        return this.delete(serverName, StatusResultMatchers::isNoContent);
    }

    public String deleteExpectNotAcceptable(String serverName) {
        return this.delete(serverName, StatusResultMatchers::isNotAcceptable);
    }

    private String delete(String serverName, ResultHttpStatusConfigurer resultHttpStatusConfigurer) {
        return this.doRequest(
                () -> MockMvcRequestBuilders.delete(CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .param(REQUEST_PARAM_NAME_SERVER_NAME, serverName),
                resultHttpStatusConfigurer);
    }

    private String doRequest(RequestBuilderSupplier requestBuilderSupplier, ResultHttpStatusConfigurer resultHttpStatusConfigurer) {
        try {
            MockHttpServletRequestBuilder requestBuilder = requestBuilderSupplier.get();
            return this.mockMvc.perform(requestBuilder)
                    .andExpect(resultHttpStatusConfigurer.configure(status()))
                    .andReturn()
                    .getResponse()
                    .getContentAsString(UTF_8);
        } catch (Exception cause) {
            throw new RuntimeException(cause);
        }
    }

    @FunctionalInterface
    private interface RequestBuilderSupplier extends Supplier<MockHttpServletRequestBuilder> {

    }

    @FunctionalInterface
    private interface ResultHttpStatusConfigurer {
        ResultMatcher configure(final StatusResultMatchers matchers);
    }
}
