package ag04.hackathon2020.moneyheist;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

import ag04.hackathon2020.moneyheist.dto.HeistDto;
import ag04.hackathon2020.moneyheist.util.HeistDtoUtility;
import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SBSS_11_ViewHeistOutcomeTests {

	@Autowired
	private RequestHelper requestHelper;

	@Test
	public void getHeistOutcome_createStartAndWaitForFinish_shouldReturnOk() throws Exception {		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		ZonedDateTime now = ZonedDateTime.now();
		HeistDto body = HeistDtoUtility.getPreparedHeistDto();
		String startTime = now.plusSeconds(3).format(formatter);
		String endTime = now.plusSeconds(6).format(formatter);
		body.setStartTime(startTime);
		body.setEndTime(endTime);
		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		HttpStatus expectedResponseStatus = HttpStatus.CREATED;
		MvcResult mvcCreateResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String createLocation = mvcCreateResult.getResponse().getHeader("Location");
		Object membersToConfirm = Collections.singletonMap("members", List.of("Helsinki"));
		requestHelper.sendRequest(RequestMethod.PUT, createLocation + "/members", membersToConfirm, HttpStatus.NO_CONTENT, null);
		Thread.sleep(10000);
		MvcResult mvc = requestHelper.sendRequest(RequestMethod.GET, createLocation + "/outcome", null, HttpStatus.OK, null);
		String res = mvc.getResponse().getContentAsString();
		Assert.assertTrue(res.contains("SUCCEEDED") || res.contains("FAILED"));
	}
	
	@Test
	public void getHeistOutcome_getOutcomeOfNonExistingHeist_shouldReturnNotFound() throws Exception {		
		requestHelper.sendRequest(RequestMethod.GET, "/heist/99/outcome", null, HttpStatus.NOT_FOUND, null);
	}
	
}
