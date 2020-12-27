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
public class SBSS_08_StartHeistTests {

	@Autowired
	private RequestHelper requestHelper;
	
	@Test
	public void startHeist_startValidHeist_shouldReturnOkStatusWithLocationHeader() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/2/start";
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void startHeist_startNonExistingHeist_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/99/start";
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void startHeist_startHeistWhichDoesntHaveReadyStatus_shouldReturnMethodNotAllowed() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/1/start";	
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.METHOD_NOT_ALLOWED;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist status is not READY";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	
}
