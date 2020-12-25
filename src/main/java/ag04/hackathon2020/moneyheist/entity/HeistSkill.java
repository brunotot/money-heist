package ag04.hackathon2020.moneyheist.entity;

public class HeistSkill {

	private Skill skill;
	
	private String level;
	
	private int members;

	public HeistSkill() {
		super();
	}

	public HeistSkill(Skill skill, String level, int members) {
		super();
		this.skill = skill;
		this.level = level;
		this.members = members;
	}

	public Skill getSkill() {
		return skill;
	}

	public String getLevel() {
		return level;
	}

	public int getMembers() {
		return members;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setMembers(int members) {
		this.members = members;
	}

}
