package ag04.hackathon2020.moneyheist;

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
public class SBSS_06_ViewEligibleHeistMembersTests {

	@Autowired
	private RequestHelper requestHelper;
	
	@Test
	public void viewEligibleMembers_findEligibleMembersOfNonExistingHeist_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99/eligible_members";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void viewEligibleMembers_findEligibleMembersOfHeistWithConfirmedMembers_shouldReturnMethodNotAllowed() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/2/eligible_members";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.METHOD_NOT_ALLOWED;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Members are already confirmed";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void viewEligibleMembers_findEligibleMembersOfValidHeist_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1/eligible_members";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
}
