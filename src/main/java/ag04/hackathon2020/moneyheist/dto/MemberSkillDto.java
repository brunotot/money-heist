package ag04.hackathon2020.moneyheist.dto;

import ag04.hackathon2020.moneyheist.entity.MemberSkill;
import ag04.hackathon2020.moneyheist.entity.Skill;

public class MemberSkillDto {
	
	private String name;
	
	private String level;

	private Integer experience;
	
	public MemberSkillDto() {
		super();
	}

	public MemberSkillDto(String name, String level, Integer experience) {
		super();
		this.name = name;
		this.level = level;
		this.experience = experience;
	}

	public String getName() {
		return name;
	}

	public String getLevel() {
		return level;
	}
	
	public Integer getExperience() {
		return experience;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}
	
	public static MemberSkillDto toDto(MemberSkill entity) {
		MemberSkillDto dto = new MemberSkillDto();
		dto.setLevel(entity.getLevel());
		dto.setName(entity.getSkill() == null ? null : entity.getSkill().getName().toUpperCase());
		dto.setExperience(entity.getExperience());
		return dto;
	}
	
	public static MemberSkill toEntity (MemberSkillDto dto) {
		MemberSkill memberSkill = new MemberSkill();
		memberSkill.setLevel(dto.getLevel());
		memberSkill.setSkill(dto.getName() == null ? null : new Skill(null, dto.getName().toUpperCase()));
		memberSkill.setExperience(dto.getExperience());
		return memberSkill;
	}
	
}
