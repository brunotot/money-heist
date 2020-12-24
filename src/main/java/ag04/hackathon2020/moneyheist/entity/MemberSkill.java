package ag04.hackathon2020.moneyheist.entity;

public class MemberSkill {
	
	private Skill skill;
	
	private String level;

	public MemberSkill() {
		super();
	}

	public MemberSkill(Skill skill, String level) {
		super();
		this.skill = skill;
		this.level = level;
	}

	public Skill getSkill() {
		return skill;
	}

	public String getLevel() {
		return level;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
}
