package ag04.hackathon2020.moneyheist;

import java.util.List;
import java.util.stream.Collectors;

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

import ag04.hackathon2020.moneyheist.dto.MemberSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;
import ag04.hackathon2020.moneyheist.util.MemberSkillDtoUtility;
import ag04.hackathon2020.moneyheist.util.RandomUtility;
import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SBSS_02_UpdateMemberTests {

	@Autowired
	private RequestHelper requestHelper;
	
	@Test
	public void updateMemberSkills_updateMemberWithValidData_shouldReturnNoContentStatusWithContentLocationHeader() throws Exception {
		List<MemberSkillDto> memberSkillDtos = MemberSkillDtoUtility.getRandomMemberSkillDtos();
		String mainSkill = RandomUtility.getRandomListElement(memberSkillDtos.stream().map(msd -> msd.getName()).collect(Collectors.toList()), true);
		RequestMethod method = RequestMethod.PUT;
		String path = "/member/1/skills";
		Object body = new MemberSkillArrayDto(memberSkillDtos, mainSkill);
		HttpStatus expectedResponseStatus = HttpStatus.NO_CONTENT;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Content-Location");
		Assert.assertEquals(path, locationActual);
	}

	@Test
	public void updateMemberSkills_updateNonExistingMember_shouldReturnNotFound() throws Exception {
		List<MemberSkillDto> memberSkillDtos = MemberSkillDtoUtility.getRandomMemberSkillDtos();
		String mainSkill = RandomUtility.getRandomListElement(memberSkillDtos.stream().map(msd -> msd.getName()).collect(Collectors.toList()), true);
		RequestMethod method = RequestMethod.PUT;
		String path = "/member/999/skills";
		Object body = new MemberSkillArrayDto(memberSkillDtos, mainSkill);
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member not found";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void updateMemberSkills_updateMemberWithInvalidMainSkill_shouldReturnBadRequest() throws Exception {
		List<MemberSkillDto> memberSkillDtos = MemberSkillDtoUtility.getRandomMemberSkillDtos();
		String mainSkill = "INVALID";
		RequestMethod method = RequestMethod.PUT;
		String path = "/member/1/skills";
		Object body = new MemberSkillArrayDto(memberSkillDtos, mainSkill);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "mainSkill doesn't reference any skill from skills array";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void updateMemberSkills_updateMemberDuplicateSkillNames_shouldReturnBadRequest() throws Exception {
		List<MemberSkillDto> memberSkillDtos = MemberSkillDtoUtility.getRandomMemberSkillDtos();
		memberSkillDtos.add(new MemberSkillDto("COMBAT", "***", 0));
		memberSkillDtos.add(new MemberSkillDto("COMBAT", "***", 0));
		String mainSkill = RandomUtility.getRandomListElement(memberSkillDtos.stream().map(msd -> msd.getName()).collect(Collectors.toList()), true);
		RequestMethod method = RequestMethod.PUT;
		String path = "/member/1/skills";
		Object body = new MemberSkillArrayDto(memberSkillDtos, mainSkill);
		HttpStatus expectedResponseStatus = HttpStatus.BAD_REQUEST;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate member skill names";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

}
