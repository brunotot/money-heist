package ag04.hackathon2020.moneyheist.dto;

import ag04.hackathon2020.moneyheist.entity.HeistSkill;
import ag04.hackathon2020.moneyheist.entity.Skill;

public class HeistSkillDto {

	private String name;
	
	private String level;
	
	private int members;

	public HeistSkillDto() {
		super();
	}

	public HeistSkillDto(String name, String level, int members) {
		super();
		this.name = name;
		this.level = level;
		this.members = members;
	}

	public String getName() {
		return name;
	}

	public String getLevel() {
		return level;
	}

	public int getMembers() {
		return members;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setMembers(int members) {
		this.members = members;
	}

	public static HeistSkill toEntity(HeistSkillDto dto) {
		HeistSkill heistSkill = new HeistSkill();
		heistSkill.setLevel(dto.getLevel());
		heistSkill.setSkill(new Skill(null, dto.getName().toUpperCase()));
		heistSkill.setMembers(dto.getMembers());
		return heistSkill;
	}
	
	public static HeistSkillDto toDto(HeistSkill entity) {
		HeistSkillDto dto = new HeistSkillDto();
		dto.setLevel(entity.getLevel());
		dto.setName(entity.getSkill().getName().toUpperCase());
		dto.setMembers(entity.getMembers());
		return dto;
	}
}
