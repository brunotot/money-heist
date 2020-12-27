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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestMethod;

import ag04.hackathon2020.moneyheist.dto.HeistDto;
import ag04.hackathon2020.moneyheist.util.HeistDtoUtility;
import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations="classpath:test.properties")
public class SBSS_13_MemberImprovementsTests {

	@Autowired
	private RequestHelper requestHelper;

	@Test
	public void createAndStartHeist_waitUntilFinishedAndCompareValues_memberShouldHaveCombatSkillLevelExtended() throws Exception {
		MvcResult res1 = requestHelper.sendRequest(RequestMethod.GET, "/member/1", null, HttpStatus.OK, null);
		String resp1 = res1.getResponse().getContentAsString();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		ZonedDateTime now = ZonedDateTime.now();
		HeistDto body = HeistDtoUtility.getPreparedHeistDto();
		String startTime = now.plusSeconds(2).format(formatter);
		String endTime = now.plusSeconds(5).format(formatter);
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
		MvcResult res2 = requestHelper.sendRequest(RequestMethod.GET, "/member/1", null, HttpStatus.OK, null);
		String resp2 = res2.getResponse().getContentAsString();
		Assert.assertTrue(resp1.contains("\"COMBAT\",\"level\":\"******\""));
		Assert.assertTrue(resp2.contains("\"COMBAT\",\"level\":\"*********\""));
	}
	
	
}
