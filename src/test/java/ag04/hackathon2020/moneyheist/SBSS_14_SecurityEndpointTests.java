package ag04.hackathon2020.moneyheist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMethod;

import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SBSS_14_SecurityEndpointTests {

	@Autowired
	private RequestHelper requestHelper;
	
	private HttpHeaders organiserAuthHeaders;
	
	private HttpHeaders memberAuthHeaders;
	
	@Before
	public void setup() {
		this.organiserAuthHeaders = new HttpHeaders();
		this.organiserAuthHeaders.add("Authorization", "Basic " + Base64Utils.encodeToString("Helsinki:ag04heist".getBytes()));
		this.memberAuthHeaders = new HttpHeaders();
		this.memberAuthHeaders.add("Authorization", "Basic " + Base64Utils.encodeToString("Bruno:ag04heist".getBytes()));
	}
	
	@Test
	public void getMember_getExistingMemberWithMemberAuth_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/member/1";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, memberAuthHeaders);
	}
	
	@Test
	public void getHeist_getExistingHeistWithHeistAuth_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, organiserAuthHeaders);
	}
	
	@Test
	public void getHeist_getExistingHeistWithMemberAuth_shouldReturnForbidden() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.FORBIDDEN;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, memberAuthHeaders);
	}
	
	@Test
	public void getHeist_getExistingHeistWithNoAuth_shouldReturnUnauthorized() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.UNAUTHORIZED;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	
}
