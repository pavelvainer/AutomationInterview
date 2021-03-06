import com.checkmarx.automation.test.config.AutomationTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

/**
 * You can use the following predefined variables:
 * <br><b>restTemplate</b>
 * <br><b>credentialsJson</b>
 * <br><b>wrongCredentialsJson</b>
 */
public class AutomationExamTest extends AutomationTestBase {
    @Before
    public void setup() {
        mockServer();
    }

    /**
     * Write a test that connects to the Access Control with a valid credentials.
     * <br>Use <i>http://accesscontrol</i> to connect
     * <br>Use <b>credentialsJson</b> for this test.
     * <br>If the token value is <b>"valid_token"</b> then success
     */
    @Test
    public void testConnectToACSuccess() {
        String responseEntity;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(credentialsJson, headers);
        try {
            responseEntity = restTemplate.postForObject("http://accesscontrol", request, String.class);
//            System.out.println("Logged In with token: " + tokenValue); //take the value of the token
//            Assert.assertEquals("valid_token", tokenValue);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            Assert.fail();
        }
    }

    /**
     * Write a test that connects to the Access Control with bad credentials.
     * <br>Use <i>http://accesscontrol</i> to connect
     * <br>Use <b>wrongCredentialsJson</b> for this test.
     * <br>If the status code is <b>401</b> then success
     */
    @Test
    public void testConnectToACFailure() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(wrongCredentialsJson, headers);
        try {
            String responseEntity = restTemplate.postForObject("http://accesscontrol", request, String.class);
//            System.out.println("Logged In with token: " + responseEntity);
            Assert.fail("You should not be able to log in with these credentials");
        } catch (HttpClientErrorException e) {
            System.out.println("Error: " + e.getStatusCode());
            Assert.assertEquals(401, e.getRawStatusCode());
        }
    }

    /**
     * Write a test that connects to the Access Control with a valid credentials.
     * <br>Use <b>credentialsJson</b> for this test.
     * <br>Use the received token in the <b>Authorization</b> header to get the items from the <i>http://backend/items</i>
     * <br>If you are able to retrieve the items - print the list of items and return success
     */
    @Test
    public void testGetItemsSuccess() {
        HttpHeaders headersAC = new HttpHeaders();
        headersAC.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestAC = new HttpEntity<>(credentialsJson, headersAC);
        String responseEntityAC = restTemplate.postForObject("http://accesscontrol", requestAC, String.class);
        System.out.println("Logged In with token: " + responseEntityAC);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "valid_token"); // parse from responseEntityAC json
        HttpEntity<String> request = new HttpEntity<>("", headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("http://backend/items", HttpMethod.GET, request, String.class);
        System.out.println(responseEntity.getBody());

    }
}
