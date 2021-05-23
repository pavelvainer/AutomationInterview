import com.checkmarx.automation.test.config.AutomationTestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;

/**
 * Created by PavelV on 5/19/2021
 */
public class AutomationExamTest extends AutomationTestBase {
    @Before
    public void setup() {
        mockServer();
    }

    @Test
    public void testConnectToAcSuccess() {
        String responseEntity;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(credentialsJson, headers);
        try {
            responseEntity = restTemplate.postForObject("http://accesscontrol", request, String.class);
            System.out.println("Logged In with token: " + responseEntity);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Test
//    @Test(expected = AssertionError.class)
    public void testConnectToAcFailure() {
        String responseEntity;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(wrongCredentialsJson, headers);
        try {
            responseEntity = restTemplate.postForObject("http://accesscontrol", request, String.class);
            System.out.println("Logged In with token: " + responseEntity);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

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
