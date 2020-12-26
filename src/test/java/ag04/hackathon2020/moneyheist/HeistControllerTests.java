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

import ag04.hackathon2020.moneyheist.dto.HeistDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillDto;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HeistControllerTests {

	@Autowired
	private RequestHelper requestHelper;

	@Test
	public void createHeist_addValidHeistObject_shouldReturnCreatedStatusWithLocationHeader() throws Exception {
		String name = "Test heist 2";
		String location = "Croatia";
		String startTime = "2020-12-27T22:00:00.000Z";
		String endTime = "2020-12-28T18:00:00.000Z";
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("combat", "*****", 1),
			new HeistSkillDto("combat", "**", 3)
		);
		HeistStatus heistStatus = HeistStatus.PLANNING;

		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		Object body = new HeistDto(name, location, startTime, endTime, heistSkillDtos, heistStatus);
		HttpStatus expectedResponseStatus = HttpStatus.CREATED;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	
		String locationExpected = "/heist/4";
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Location");
		Assert.assertEquals(locationExpected, locationActual);
	}

	@Test
	public void createHeist_addHeistWithNameThatAlreadyExists_shouldReturnBadRequest() throws Exception {
		String name = "Test heist";
		String location = "Croatia";
		String startTime = "2020-12-27T22:00:00.000Z";
		String endTime = "2020-12-28T18:00:00.000Z";
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("combat", "*****", 1),
			new HeistSkillDto("combat", "**", 3)
		);
		HeistStatus heistStatus = HeistStatus.PLANNING;

		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		Object body = new HeistDto(name, location, startTime, endTime, heistSkillDtos, heistStatus);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist already exists";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void createHeist_addHeistWithDuplicateSkills_shouldReturnBadRequest() throws Exception {
		String name = "Test heist";
		String location = "Croatia";
		String startTime = "2020-12-27T22:00:00.000Z";
		String endTime = "2020-12-28T18:00:00.000Z";
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("combat", "*****", 1),
			new HeistSkillDto("combat", "*****", 3)
		);
		HeistStatus heistStatus = HeistStatus.PLANNING;

		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		Object body = new HeistDto(name, location, startTime, endTime, heistSkillDtos, heistStatus);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate heist skills";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void createHeist_addHeistWithInvalidDates_shouldReturnBadRequest() throws Exception {
		String name = "Test heist 2";
		String location = "Croatia";
		String startTime = "2019-12-27T22:00:00.000Z";
		String endTime = "2020-12-28T18:00:00.000Z";
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("combat", "*****", 1),
			new HeistSkillDto("combat", "*****", 3)
		);
		HeistStatus heistStatus = HeistStatus.PLANNING;

		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		Object body = new HeistDto(name, location, startTime, endTime, heistSkillDtos, heistStatus);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Invalid dates";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void updateHeist_updateHeistWithValidData_shouldReturnNoContentStatusWithContentLocationHeader() throws Exception {
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("driving", "****", 1),
			new HeistSkillDto("driving", "**", 3)
		);
		
		RequestMethod method = RequestMethod.PATCH;
		String path = "/heist/1/skills";
		Object body = new HeistSkillArrayDto(heistSkillDtos);
		HttpStatus expectedResponseStatus = HttpStatus.NO_CONTENT;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	
		String locationExpected = "/heist/1/skills";
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Content-Location");
		Assert.assertEquals(locationExpected, locationActual);
	}
	
	@Test
	public void updateHeist_updateNonExistingHeist_shouldReturnNotFound() throws Exception {
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("driving", "****", 1),
			new HeistSkillDto("driving", "**", 3)
		);

		RequestMethod method = RequestMethod.PATCH;
		String path = "/heist/99/skills";
		Object body = new HeistSkillArrayDto(heistSkillDtos);
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist not found";
		Assert.assertTrue(response.contains(problemTitleExpected));

	}

	@Test
	public void updateHeist_updateHeistWithDuplicateSkills_shouldReturnBadRequest() throws Exception {
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("driving", "****", 1),
			new HeistSkillDto("driving", "****", 3)
		);
		
		RequestMethod method = RequestMethod.PATCH;
		String path = "/heist/1/skills";
		Object body = new HeistSkillArrayDto(heistSkillDtos);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate heist skills";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void viewEligibleMembers_findEligibleMembersOfNonExistingHeist_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99/eligible_members";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);
		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void confirmHeistMembers_confirmValidHeistMembers_shouldReturnNoContent() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/2/members";
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.NO_CONTENT;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);	
		
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
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);	
		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void confirmHeistMembers_confirmInvalidMembers_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/2/members";
		Object body = Collections.singletonMap("members", List.of("Bruno"));
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);
		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void confirmHeistMembers_confirmMembersForHeistNotInPlanningStatus_shouldReturnMethodNotAllowed() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/1/members";	
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.METHOD_NOT_ALLOWED;
		requestHelper.sendRequest(method, path, body, HttpStatus.NO_CONTENT);
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);
			
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist status is not PLANNING";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void startHeist_startValidHeist_shouldReturnOkStatusWithLocationHeader() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/3/start";
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String locationExpected = "/heist/3/status";
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Location");
		Assert.assertEquals(locationExpected, locationActual);
	}
	
	@Test
	public void startHeist_startNonExistingHeist_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.PUT;
		String path = "/heist/99/start";
		Object body = Collections.singletonMap("members", List.of("Helsinki"));
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);
		
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
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist status is not READY";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void getHeist_getExistingHeist_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}

	@Test
	public void getHeist_getNonExistingHeist_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
	@Test
	public void getHeistMembers_getExistingAndValidHeistMembers_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/3/members";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
	@Test
	public void getHeistMembers_getNonExistingHeistMembers_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99/members";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
	@Test
	public void getHeistMembers_getExistingAndInvalidHeistMembers_shouldReturnMethodNotAllowed() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1/members";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.METHOD_NOT_ALLOWED;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
	@Test
	public void getHeistSkills_getExistingHeistSkills_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1/skills";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
	@Test
	public void getHeistSkills_getNonExistingHeistSkills_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99/skills";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
	@Test
	public void getHeistStatus_getExistingHeistSkills_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1/status";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
	@Test
	public void getHeistStatus_getNonExistingHeistSkills_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99/status";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
}