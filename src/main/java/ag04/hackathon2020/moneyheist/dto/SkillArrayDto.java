package ag04.hackathon2020.moneyheist.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SkillArrayDto {

	@JsonProperty("skills")
	private List<MemberSkillDto> memberSkillDtos;
	
	private String mainSkill;

	public SkillArrayDto() {
		super();
	}

	public SkillArrayDto(List<MemberSkillDto> memberSkillDtos, String mainSkill) {
		super();
		this.memberSkillDtos = memberSkillDtos;
		this.mainSkill = mainSkill;
	}

	public List<MemberSkillDto> getMemberSkillDtos() {
		return memberSkillDtos;
	}

	public String getMainSkill() {
		return mainSkill;
	}

	public void setMemberSkillDtos(List<MemberSkillDto> memberSkillDtos) {
		this.memberSkillDtos = memberSkillDtos;
	}

	public void setMainSkill(String mainSkill) {
		this.mainSkill = mainSkill;
	}
	
}
