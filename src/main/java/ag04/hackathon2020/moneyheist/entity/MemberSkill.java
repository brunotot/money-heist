package ag04.hackathon2020.moneyheist.entity;

public class MemberSkill {
	
	private Skill skill;
	
	private String level;

	private Integer experience;
	
	public MemberSkill() {
		super();
	}

	public MemberSkill(Skill skill, String level, Integer experience) {
		super();
		this.skill = skill;
		this.level = level;
		this.experience = experience;
	}

	public Skill getSkill() {
		return skill;
	}

	public String getLevel() {
		return level;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}
	
}
