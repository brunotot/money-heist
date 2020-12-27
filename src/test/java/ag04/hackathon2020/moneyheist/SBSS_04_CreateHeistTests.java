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

import ag04.hackathon2020.moneyheist.dto.HeistDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillDto;
import ag04.hackathon2020.moneyheist.util.HeistDtoUtility;
import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SBSS_04_CreateHeistTests {

	@Autowired
	private RequestHelper requestHelper;

	@Test
	public void createHeist_addValidHeistObject_shouldReturnCreatedStatusWithLocationHeader() throws Exception {
		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		Object body = HeistDtoUtility.getRandomHeistDto();
		HttpStatus expectedResponseStatus = HttpStatus.CREATED;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void createHeist_addHeistWithNameThatAlreadyExists_shouldReturnBadRequest() throws Exception {
		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		HeistDto body = HeistDtoUtility.getRandomHeistDto();
		body.setName("Heist1");
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist already exists";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void createHeist_addHeistWithDuplicateSkills_shouldReturnBadRequest() throws Exception {
		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		HeistDto body = HeistDtoUtility.getRandomHeistDto();
		body.getHeistSkillDtos().add(new HeistSkillDto("COMBAT", "****", 1));
		body.getHeistSkillDtos().add(new HeistSkillDto("COMBAT", "****", 1));
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate heist skills";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void createHeist_addHeistWithInvalidDates_shouldReturnBadRequest() throws Exception {
		RequestMethod method = RequestMethod.POST;
		String path = "/heist";
		HeistDto body = HeistDtoUtility.getRandomHeistDto();
		body.setStartTime("2019-12-27T22:00:00.000Z");
		body.setEndTime("2020-12-28T18:00:00.000Z");
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Invalid dates";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
}
