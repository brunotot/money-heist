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
public class SBSS_10_AutomaticHeistStartAndEndTests {

	@Autowired
	private RequestHelper requestHelper;

	@Test
	public void testAutomaticHeistStartAndFinish() throws Exception {
		long delay = 2L;
		long delayForMails = 2L;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		ZonedDateTime now = ZonedDateTime.now();
		HeistDto body = HeistDtoUtility.getRandomHeistDto();
		String startTime = now.plusSeconds(delay).format(formatter);
		String endTime = now.plusSeconds(delay * 2).format(formatter);
		body.setStartTime(startTime);
		body.setEndTime(endTime);
		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		HttpStatus expectedResponseStatus = HttpStatus.CREATED;
		MvcResult mvcCreateResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String createLocation = mvcCreateResult.getResponse().getHeader("Location");
		Object membersToConfirm = Collections.singletonMap("members", List.of("Helsinki"));
		requestHelper.sendRequest(RequestMethod.PUT, createLocation + "/members", membersToConfirm, HttpStatus.NO_CONTENT, null);
		Thread.sleep(delay * 1000);
		MvcResult mvcResult = requestHelper.sendRequest(RequestMethod.GET, createLocation + "/status", null, HttpStatus.OK, null);
		String responseInProgress = mvcResult.getResponse().getContentAsString();
		Thread.sleep((delay + delayForMails) * 1000);
		MvcResult mvcResultFinished = requestHelper.sendRequest(RequestMethod.GET, createLocation + "/status", null, HttpStatus.OK, null);
		String responseFinished = mvcResultFinished.getResponse().getContentAsString();
		Assert.assertTrue(responseInProgress.contains("IN_PROGRESS"));
		Assert.assertTrue(responseFinished.contains("FINISHED"));
	}
	
}
