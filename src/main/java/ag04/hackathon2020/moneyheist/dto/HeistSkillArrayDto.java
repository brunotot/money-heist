package ag04.hackathon2020.moneyheist.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HeistSkillArrayDto {

	@JsonProperty("skills")
	private List<HeistSkillDto> heistSkillDtos;
	
	public HeistSkillArrayDto() {
		super();
	}

	public HeistSkillArrayDto(List<HeistSkillDto> heistSkillDtos) {
		super();
		this.heistSkillDtos = heistSkillDtos;
	}

	public List<HeistSkillDto> getHeistSkillDtos() {
		return heistSkillDtos;
	}

	public void setHeistSkillDtos(List<HeistSkillDto> heistSkillDtos) {
		this.heistSkillDtos = heistSkillDtos;
	}
	
}
