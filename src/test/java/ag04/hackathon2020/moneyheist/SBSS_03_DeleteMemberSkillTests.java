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

import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SBSS_03_DeleteMemberSkillTests {

	@Autowired
	private RequestHelper requestHelper;

	@Test
	public void deleteMemberSkill_deleteExistingSkillForExistingMember_shouldReturnNoContent() throws Exception {
		RequestMethod method = RequestMethod.DELETE;
		String path = "/member/1/skills/combat";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NO_CONTENT;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void deleteMemberSkill_deleteExistingSkillForNonExistingMember_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.DELETE;
		String path = "/member/99/skills/combat";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
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
		MvcResult mvcResult = requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Member doesn't have the skill";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

}
