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

import ag04.hackathon2020.moneyheist.dto.MemberDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;
import ag04.hackathon2020.moneyheist.util.MemberDtoUtility;
import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SBSS_01_CreateMemberTests {

	@Autowired
	private RequestHelper requestHelper;
	
	@Test
	public void createMember_addValidMemberObject_shouldReturnCreated() throws Exception {
		RequestMethod method = RequestMethod.POST;
		String path = "/member";
		MemberDto body = MemberDtoUtility.getRandomMemberDto();
		HttpStatus expectedResponseStatus = HttpStatus.CREATED;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void createMember_addMemberWithInvalidMainSkill_shouldReturnBadRequest() throws Exception {
		MemberDto body = MemberDtoUtility.getRandomMemberDto();
		body.setMainSkill("INVALID");
		RequestMethod method = RequestMethod.POST;
		String path = "/member";
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "mainSkill doesn't reference any skill from skills array";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void createMember_addExistingMember_shouldReturnBadRequest() throws Exception {
		MemberDto body = MemberDtoUtility.getRandomMemberDto();
		body.setEmail("brunotot10000temp@gmail.com");
		RequestMethod method = RequestMethod.POST;
		String path = "/member";
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member already exists";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void createMember_addMemberWithDuplicateSkills_shouldReturnBadRequest() throws Exception {
		MemberDto body = MemberDtoUtility.getRandomMemberDto();
		body.getMemberSkillDtos().add(new MemberSkillDto("COMBAT", "***", 0));
		body.getMemberSkillDtos().add(new MemberSkillDto("COMBAT", "***", 0));
		RequestMethod method = RequestMethod.POST;
		String path = "/member";
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate member skill names";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

}
