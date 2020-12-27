package ag04.hackathon2020.moneyheist.util;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RequestHelper {

	private MockMvc mvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public RequestHelper(MockMvc mvc) {
		this.mvc = mvc;
	}
	
	public MvcResult sendRequest(RequestMethod method, String path, Object body, HttpStatus expectedResponseStatus, HttpHeaders headers) throws Exception {
		ResultMatcher resultMatcher = MockMvcResultMatchers.status().is(expectedResponseStatus.value());
		String dto = mapper.writeValueAsString(body);
		MvcResult mvcResult = null;
		String contentType = MediaType.APPLICATION_JSON_VALUE;
		if (method.equals(RequestMethod.GET)) {
			mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.get(path).headers(headers).contentType(contentType).content(dto))
				.andExpect(resultMatcher).andReturn();
		} else if (method.equals(RequestMethod.DELETE)) {
			mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.delete(path).headers(headers).contentType(contentType).content(dto))
				.andExpect(resultMatcher).andReturn();
		} else if (method.equals(RequestMethod.POST)) {
			mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.post(path).headers(headers).contentType(contentType).content(dto))
				.andExpect(resultMatcher).andReturn();
		} else if (method.equals(RequestMethod.PUT)) {
			mvcResult = this.mvc.perform(
					MockMvcRequestBuilders.put(path).headers(headers).contentType(contentType).content(dto))
					.andExpect(resultMatcher).andReturn();
		} else if (method.equals(RequestMethod.PATCH)) {
			mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.patch(path).headers(headers).contentType(contentType).content(dto))
				.andExpect(resultMatcher).andReturn();
		}
		if (mvcResult == null) {
			throw new RuntimeException("mvcResult is null - check!");
		}
		return mvcResult;
	}
	
}
