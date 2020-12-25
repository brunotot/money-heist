package ag04.hackathon2020.moneyheist.entity;

import java.util.List;
import java.util.stream.Collectors;

import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;
import ag04.hackathon2020.moneyheist.dto.SkillArrayDto;

public class Member {

	private Long id;
	
	private String email;
	
	private String name;
	
	private Sex sex;
	
	private List<MemberSkill> memberSkills;
	
	private Skill mainSkill;
	
	private Status status;

	public Member() {
		super();
	}

	public Member(Long id, String email, String name, Sex sex, List<MemberSkill> memberSkills, Skill mainSkill,
			Status status) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.sex = sex;
		this.memberSkills = memberSkills;
		this.mainSkill = mainSkill;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public Sex getSex() {
		return sex;
	}

	public List<MemberSkill> getMemberSkills() {
		return memberSkills;
	}

	public Skill getMainSkill() {
		return mainSkill;
	}

	public Status getStatus() {
		return status;
	}
	
	public List<Skill> getSkills() {
		return memberSkills.stream()
				.filter(ms -> ms != null)
				.map(ms -> ms.getSkill())
				.collect(Collectors.toList());

	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public void setMemberSkills(List<MemberSkill> memberSkills) {
		this.memberSkills = memberSkills;
	}
	
	public void setMemberSkills(SkillArrayDto skillArrayDto) {
		List<MemberSkillDto> memberSkillDtos = skillArrayDto.getMemberSkillDtos();
		List<MemberSkill> memberSkills = memberSkillDtos.stream().map(msd -> MemberSkillDto.toEntity(msd)).collect(Collectors.toList());
		List<Skill> skills = memberSkills.stream().map(ms -> ms.getSkill()).collect(Collectors.toList());
		this.setMemberSkills(memberSkills);
		
		String mainSkillString = skillArrayDto.getMainSkill();
		if (mainSkillString == null) {
			this.setMainSkill(null);
		} else {
			mainSkillString = mainSkillString.toUpperCase();
			Skill mainSkill = Skill.find(mainSkillString, skills);
			if (mainSkill == null) {
				this.setMainSkill(new Skill(null, mainSkillString));
			} else {
				this.setMainSkill(mainSkill);
			}
		}
		
	}

	public void setMainSkill(Skill mainSkill) {
		this.mainSkill = mainSkill;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
