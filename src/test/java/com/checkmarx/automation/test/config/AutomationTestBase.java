package com.checkmarx.automation.test.config;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

/**
 * Created by PavelV on 5/19/2021
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppContext.class)
@WebAppConfiguration
public class AutomationTestBase {
    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private WebApplicationContext wac;

    protected MockRestServiceServer mockServer;

    protected static final String credentialsJson = "{\n" +
            "  username: \"sa\",\n" +
            "  password: \"sa\"\n" +
            "}";

    protected static final String wrongCredentialsJson = "{\n" +
            "  username: \"sa\",\n" +
            "  password: \"sa1\"\n" +
            "}";

    private static final String items = "{\n" +
            "  \"items\": [\n" +
            "    \"phone\",\n" +
            "    \"charger\",\n" +
            "    \"headphones\"\n" +
            "  ]\n" +
            "}";

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
    }

    protected void mockServer() {
        mockServer.expect(manyTimes(), requestTo("http://accesscontrol"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(credentialsJson))
                .andRespond(withSuccess("{token : \"valid_token\"}", MediaType.APPLICATION_JSON));

        mockServer.expect(manyTimes(), requestTo("http://accesscontrol"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(wrongCredentialsJson))
                .andRespond(withUnauthorizedRequest());

        mockServer.expect(manyTimes(), requestTo("http://backend/items"))
                .andExpect(method(HttpMethod.GET)).andExpect(header("Authorization", "valid_token"))
                .andRespond(withSuccess(items, MediaType.APPLICATION_JSON));
    }

    @After
    public void tearDown() {
    }
}
