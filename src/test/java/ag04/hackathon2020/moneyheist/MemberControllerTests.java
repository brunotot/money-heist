package ag04.hackathon2020.moneyheist;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import ag04.hackathon2020.moneyheist.dto.MemberDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;
import ag04.hackathon2020.moneyheist.dto.SkillArrayDto;
import ag04.hackathon2020.moneyheist.entity.Sex;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTests {

	@Autowired
	private MockMvc mvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void createMember_addValidMemberObject_shouldReturnCreatedStatusWithLocationHeader() throws Exception {
		
		// MemberDto
		/**********************************************/
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
		/**********************************************/
		
		MemberDto memberDto = new MemberDto(id, email, name, sex, memberSkillDtos, mainSkill, status);
		String memberDtoString = mapper.writeValueAsString(memberDto);
		MvcResult mvcResult = this.mvc.perform(
				post("/member").contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isCreated()).andReturn();		
		String locationExpected = "/member/2";
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Location");
		Assert.assertEquals(locationExpected, locationActual);
	}
	
	@Test
	public void createMember_addMemberWithInvalidMainSkill_shouldReturnBadRequest() throws Exception {
		
		// MemberDto
		/**********************************************/
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
		/**********************************************/
		
		MemberDto memberDto = new MemberDto(id, email, name, sex, memberSkillDtos, mainSkill, status);
		String memberDtoString = mapper.writeValueAsString(memberDto);
		MvcResult mvcResult = this.mvc.perform(
				post("/member").contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "mainSkill doesn't reference any skill from skills array";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void createMember_addExistingMember_shouldReturnBadRequest() throws Exception {
		
		// MemberDto
		/**********************************************/
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
		/**********************************************/
		
		MemberDto memberDto = new MemberDto(id, email, name, sex, memberSkillDtos, mainSkill, status);
		String memberDtoString = mapper.writeValueAsString(memberDto);
		MvcResult mvcResult = this.mvc.perform(
				post("/member").contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member already exists";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	

	@Test
	public void createMember_addMemberWithDuplicateSkills_shouldReturnBadRequest() throws Exception {
		
		// MemberDto
		/**********************************************/
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
		/**********************************************/
		
		MemberDto memberDto = new MemberDto(id, email, name, sex, memberSkillDtos, mainSkill, status);
		String memberDtoString = mapper.writeValueAsString(memberDto);
		MvcResult mvcResult = this.mvc.perform(
				post("/member").contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate member skill names";
		Assert.assertTrue(response.contains(problemTitleExpected));
		
	}

	@Test
	public void updateMember_updateMemberWithValidData_shouldReturnNoContentStatusWithContentLocationHeader() throws Exception {
		
		// SkillArrayDto
		/**********************************************/
		String mainSkill = "lock-breaking";
		List<MemberSkillDto> memberSkillDtos = List.of(
				new MemberSkillDto("combat", "***"),
				new MemberSkillDto("money-laundering", "*"),
				new MemberSkillDto("lock-breaking", "****")
		);
		/**********************************************/
		
		SkillArrayDto dto = new SkillArrayDto(memberSkillDtos, mainSkill);
		String memberDtoString = mapper.writeValueAsString(dto);
		String locationExpected = "/member/1/skills";
		MvcResult mvcResult = this.mvc.perform(
				put(locationExpected).contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isNoContent()).andReturn();		
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Content-Location");
		Assert.assertEquals(locationExpected, locationActual);
		
	}

	@Test
	public void updateMember_updateNonExistingMember_shouldReturnNotFound() throws Exception {
		
		// SkillArrayDto
		/**********************************************/
		String mainSkill = "lock-breaking";
		List<MemberSkillDto> memberSkillDtos = List.of(
				new MemberSkillDto("combat", "***"),
				new MemberSkillDto("money-laundering", "*"),
				new MemberSkillDto("lock-breaking", "****")
		);
		/**********************************************/
		
		SkillArrayDto dto = new SkillArrayDto(memberSkillDtos, mainSkill);
		String memberDtoString = mapper.writeValueAsString(dto);
		String locationExpected = "/member/999/skills";
		this.mvc.perform(
				put(locationExpected).contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isNotFound()).andReturn();		
		
	}
	
	@Test
	public void updateMember_updateMemberWithInvalidMainSkill_shouldReturnBadRequest() throws Exception {
		
		// SkillArrayDto
		/**********************************************/
		String mainSkill = "invalid";
		List<MemberSkillDto> memberSkillDtos = List.of(
				new MemberSkillDto("combat", "***"),
				new MemberSkillDto("money-laundering", "*"),
				new MemberSkillDto("lock-breaking", "****")
		);
		/**********************************************/
		
		SkillArrayDto dto = new SkillArrayDto(memberSkillDtos, mainSkill);
		String memberDtoString = mapper.writeValueAsString(dto);
		String locationExpected = "/member/1/skills";
		MvcResult mvcResult = this.mvc.perform(
				put(locationExpected).contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();	

		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "mainSkill doesn't reference any skill from skills array";
		Assert.assertTrue(response.contains(problemTitleExpected));
		
	}
	
	@Test
	public void updateMember_updateMemberDuplicateSkillNames_shouldReturnBadRequest() throws Exception {
		
		// SkillArrayDto
		/**********************************************/
		String mainSkill = "combat";
		List<MemberSkillDto> memberSkillDtos = List.of(
				new MemberSkillDto("combat", "***"),
				new MemberSkillDto("combat", "*"),
				new MemberSkillDto("lock-breaking", "****")
		);
		/**********************************************/
		
		SkillArrayDto dto = new SkillArrayDto(memberSkillDtos, mainSkill);
		String memberDtoString = mapper.writeValueAsString(dto);
		String locationExpected = "/member/1/skills";
		MvcResult mvcResult = this.mvc.perform(
				put(locationExpected).contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate member skill names";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void deleteMemberSkill_deleteExistingSkillForExistingMember_shouldReturnNoContent() throws Exception {
		
		// skillNameToDelete
		/**********************************************/
		String skillNameToDelete = "combat";
		/**********************************************/
		
		String locationExpected = "/member/1/skills/" + skillNameToDelete;
		this.mvc.perform(
				delete(locationExpected).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNoContent()).andReturn();
	}
	
	@Test
	public void deleteMemberSkill_deleteExistingSkillForNonExistingMember_shouldReturnNotFound() throws Exception {
		
		// skillNameToDelete
		/**********************************************/
		String skillNameToDelete = "combat";
		Long nonExistingMemberId = 99L;
		/**********************************************/
		
		String locationExpected = "/member/" + nonExistingMemberId + "/skills/" + skillNameToDelete;
		MvcResult mvcResult = this.mvc.perform(
				delete(locationExpected).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member not found";
		Assert.assertTrue(response.contains(problemTitleExpected));

	}
	
	@Test
	public void deleteMemberSkill_deleteNonExistingSkillOfExistingMember_shouldReturnNotFound() throws Exception {
		
		// skillNameToDelete
		/**********************************************/
		String skillNameToDelete = "invalid";
		Long nonExistingMemberId = 1L;
		/**********************************************/
		
		String locationExpected = "/member/" + nonExistingMemberId + "/skills/" + skillNameToDelete;
		MvcResult mvcResult = this.mvc.perform(
				delete(locationExpected).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member doesn't have the skill";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	
}