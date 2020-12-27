package ag04.hackathon2020.moneyheist;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestMethod;

import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SBSS_07_ConfirmHeistMembersTests {

	@Autowired
	private RequestHelper requestHelper;
	
	@Test
	public void confirmHeistMembers_confirmValidHeistMembers_shouldReturnNoContent() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/1/members";
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.NO_CONTENT;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String locationExpected = path;
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Content-Location");
		Assert.assertEquals(locationExpected, locationActual);
	}

	@Test
	public void confirmHeistMembers_confirmNonExistingHeist_shouldReturnNotFound() throws Exception {	
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/99/members";
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void confirmHeistMembers_confirmInvalidMembers_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/1/members";
		Object body = Collections.singletonMap("members", List.of("BrunoInvalid"));
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void confirmHeistMembers_confirmMembersForHeistNotInPlanningStatus_shouldReturnMethodNotAllowed() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/2/members";	
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.METHOD_NOT_ALLOWED;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist status is not PLANNING";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	
}
