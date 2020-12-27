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
import ag04.hackathon2020.moneyheist.dto.HeistSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillDto;
import ag04.hackathon2020.moneyheist.util.HeistDtoUtility;
import ag04.hackathon2020.moneyheist.util.HeistSkillDtoUtility;
import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SBSS_05_UpdateHeistSkillsTests {

	@Autowired
	private RequestHelper requestHelper;
		
	@Test
	public void updateHeist_updateHeistWithValidData_shouldReturnNoContentStatusWithContentLocationHeader() throws Exception {
		List<HeistSkillDto> heistSkillDtos = HeistSkillDtoUtility.getRandomHeistSkillDtos();
		RequestMethod method = RequestMethod.PATCH;
		String path = "/heist/1/skills";
		Object body = new HeistSkillArrayDto(heistSkillDtos);
		HttpStatus expectedResponseStatus = HttpStatus.NO_CONTENT;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String locationExpected = "/heist/1/skills";
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Content-Location");
		Assert.assertEquals(locationExpected, locationActual);
	}
	
	@Test
	public void updateHeist_updateNonExistingHeist_shouldReturnNotFound() throws Exception {
		List<HeistSkillDto> heistSkillDtos = HeistSkillDtoUtility.getRandomHeistSkillDtos();
		RequestMethod method = RequestMethod.PATCH;
		String path = "/heist/99/skills";
		Object body = new HeistSkillArrayDto(heistSkillDtos);
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void updateHeist_updateHeistWithDuplicateSkills_shouldReturnBadRequest() throws Exception {
		List<HeistSkillDto> heistSkillDtos = HeistSkillDtoUtility.getRandomHeistSkillDtos();
		heistSkillDtos.add(new HeistSkillDto("DRIVING", "****", 1));
		heistSkillDtos.add(new HeistSkillDto("DRIVING", "****", 1));
		RequestMethod method = RequestMethod.PATCH;
		String path = "/heist/1/skills";
		Object body = new HeistSkillArrayDto(heistSkillDtos);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate heist skills";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void updateHeist_updateHeistWhichIsAlreadyInProgress_shouldReturnMethodNotAllowed() throws Exception {
		HeistDto body = HeistDtoUtility.getPreparedHeistDto();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		ZonedDateTime now = ZonedDateTime.now();
		String startTime = now.plusSeconds(3L).format(formatter);
		String endTime = now.plusSeconds(7L).format(formatter);
		body.setStartTime(startTime);
		body.setEndTime(endTime);
		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		HttpStatus expectedResponseStatus = HttpStatus.CREATED;
		MvcResult mvcCreateResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String createLocation = mvcCreateResult.getResponse().getHeader("Location");
		Object membersToConfirm = Collections.singletonMap("members", List.of("Helsinki"));
		requestHelper.sendRequest(RequestMethod.PUT, createLocation + "/members", membersToConfirm, HttpStatus.NO_CONTENT, null);
		Thread.sleep(5000);
		requestHelper.sendRequest(RequestMethod.PUT, createLocation + "/members", membersToConfirm, HttpStatus.METHOD_NOT_ALLOWED, null);
	}
	
}
