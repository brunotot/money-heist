package ag04.hackathon2020.moneyheist;

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

import ag04.hackathon2020.moneyheist.dto.MemberDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;
import ag04.hackathon2020.moneyheist.entity.Sex;
import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTests {

	@Autowired
	private RequestHelper requestHelper;

	@Test
	public void createMember_addValidMemberObject_shouldReturnCreatedStatusWithLocationHeader() throws Exception {
		Long id = null;
		String email = "bruno@ag04.com";
		String name = "Bruno";
		Sex sex = Sex.M;
		List<MemberSkillDto> memberSkillDtos = List.of(
			new MemberSkillDto("combat", "********"),
			new MemberSkillDto("running", "******")
		);
		String mainSkill = "combat";
		String status = "AVAILABLE";

		RequestMethod method = RequestMethod.POST;
		String path = "/member";
		Object body = new MemberDto(id, email, name, sex, memberSkillDtos, mainSkill, status);
		HttpStatus expectedResponseStatus = HttpStatus.CREATED;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String locationExpected = "/member/2";
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Location");
		Assert.assertEquals(locationExpected, locationActual);
	}
	
	@Test
	public void createMember_addMemberWithInvalidMainSkill_shouldReturnBadRequest() throws Exception {
		Long id = null;
		String email = "bruno@ag04.com";
		String name = "Bruno";
		Sex sex = Sex.M;
		List<MemberSkillDto> memberSkillDtos = List.of(
			new MemberSkillDto("combat", "********"),
			new MemberSkillDto("running", "******")
		);
		String mainSkill = "combat_invalid";
		String status = "AVAILABLE";
		
		RequestMethod method = RequestMethod.POST;
		String path = "/member";
		Object body = new MemberDto(id, email, name, sex, memberSkillDtos, mainSkill, status);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "mainSkill doesn't reference any skill from skills array";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void createMember_addExistingMember_shouldReturnBadRequest() throws Exception {
		Long id = null;
		String email = "helsinki@ag04.com";
		String name = "Helsinki";
		Sex sex = Sex.M;
		List<MemberSkillDto> memberSkillDtos = List.of(
			new MemberSkillDto("combat", "********"),
			new MemberSkillDto("running", "******")
		);
		String mainSkill = "combat";
		String status = "AVAILABLE";
		
		RequestMethod method = RequestMethod.POST;
		String path = "/member";
		Object body = new MemberDto(id, email, name, sex, memberSkillDtos, mainSkill, status);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member already exists";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void createMember_addMemberWithDuplicateSkills_shouldReturnBadRequest() throws Exception {
		Long id = null;
		String email = "bruno@ag04.com";
		String name = "Bruno";
		Sex sex = Sex.M;
		List<MemberSkillDto> memberSkillDtos = List.of(
			new MemberSkillDto("combat", "********"),
			new MemberSkillDto("combat", "******")
		);
		String mainSkill = "combat";
		String status = "AVAILABLE";

		RequestMethod method = RequestMethod.POST;
		String path = "/member";
		Object body = new MemberDto(id, email, name, sex, memberSkillDtos, mainSkill, status);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate member skill names";
		Assert.assertTrue(response.contains(problemTitleExpected));		
	}

	@Test
	public void updateMember_updateMemberWithValidData_shouldReturnNoContentStatusWithContentLocationHeader() throws Exception {
		String mainSkill = "lock-breaking";
		List<MemberSkillDto> memberSkillDtos = List.of(
				new MemberSkillDto("combat", "***"),
				new MemberSkillDto("money-laundering", "*"),
				new MemberSkillDto("lock-breaking", "****")
		);
		
		RequestMethod method = RequestMethod.PUT;
		String path = "/member/1/skills";
		Object body = new MemberSkillArrayDto(memberSkillDtos, mainSkill);
		HttpStatus expectedResponseStatus = HttpStatus.NO_CONTENT;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Content-Location");
		Assert.assertEquals(path, locationActual);
	}

	@Test
	public void updateMember_updateNonExistingMember_shouldReturnNotFound() throws Exception {
		String mainSkill = "lock-breaking";
		List<MemberSkillDto> memberSkillDtos = List.of(
				new MemberSkillDto("combat", "***"),
				new MemberSkillDto("money-laundering", "*"),
				new MemberSkillDto("lock-breaking", "****")
		);

		RequestMethod method = RequestMethod.PUT;
		String path = "/member/999/skills";
		Object body = new MemberSkillArrayDto(memberSkillDtos, mainSkill);
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void updateMember_updateMemberWithInvalidMainSkill_shouldReturnBadRequest() throws Exception {
		String mainSkill = "invalid";
		List<MemberSkillDto> memberSkillDtos = List.of(
				new MemberSkillDto("combat", "***"),
				new MemberSkillDto("money-laundering", "*"),
				new MemberSkillDto("lock-breaking", "****")
		);
		
		RequestMethod method = RequestMethod.PUT;
		String path = "/member/1/skills";
		Object body = new MemberSkillArrayDto(memberSkillDtos, mainSkill);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "mainSkill doesn't reference any skill from skills array";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void updateMember_updateMemberDuplicateSkillNames_shouldReturnBadRequest() throws Exception {
		String mainSkill = "combat";
		List<MemberSkillDto> memberSkillDtos = List.of(
				new MemberSkillDto("combat", "***"),
				new MemberSkillDto("combat", "*"),
				new MemberSkillDto("lock-breaking", "****")
		);
		
		RequestMethod method = RequestMethod.PUT;
		String path = "/member/1/skills";
		Object body = new MemberSkillArrayDto(memberSkillDtos, mainSkill);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate member skill names";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void deleteMemberSkill_deleteExistingSkillForExistingMember_shouldReturnNoContent() throws Exception {
		RequestMethod method = RequestMethod.DELETE;
		String path = "/member/1/skills/combat";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NO_CONTENT;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus);
	}
	
	@Test
	public void deleteMemberSkill_deleteExistingSkillForNonExistingMember_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.DELETE;
		String path = "/member/99/skills/combat";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void deleteMemberSkill_deleteNonExistingSkillOfExistingMember_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.DELETE;
		String path = "/member/1/skills/invalid";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus);

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member doesn't have the skill";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

}