package ag04.hackathon2020.moneyheist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMethod;

import ag04.hackathon2020.moneyheist.util.RequestHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SBSS_09_RicherApiTests {

	@Autowired
	private RequestHelper requestHelper;

	@Test
	public void getMember_getExistingMember_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/member/1";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getMember_getNonExistingMember_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/member/99";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getMemberSkills_getExistingMemberSkills_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/member/1/skills";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getMemberSkills_getNonExistingMemberSkills_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/member/99/skills";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getHeist_getExistingHeist_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getHeist_getNonExistingHeist_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getHeistMembers_getExistingAndValidHeistMembers_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/3/members";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getHeistMembers_getNonExistingHeistMembers_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99/members";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getHeistMembers_getExistingAndInvalidHeistMembers_shouldReturnMethodNotAllowed() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1/members";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.METHOD_NOT_ALLOWED;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}

	@Test
	public void getHeistSkills_getExistingHeistSkills_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1/skills";
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getHeistSkills_getNonExistingHeistSkills_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99/skills";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}

	@Test
	public void getHeistStatus_getExistingHeistSkills_shouldReturnOk() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/1/status";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.OK;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
	@Test
	public void getHeistStatus_getNonExistingHeistSkills_shouldReturnNotFound() throws Exception {
		RequestMethod method = RequestMethod.GET;
		String path = "/heist/99/status";	
		Object body = null;
		HttpStatus expectedResponseStatus = HttpStatus.NOT_FOUND;
		requestHelper.sendRequest(method, path, body, expectedResponseStatus, null);
	}
	
}
