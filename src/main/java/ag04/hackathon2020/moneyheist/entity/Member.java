package ag04.hackathon2020.moneyheist.entity;

import java.util.List;
import java.util.stream.Collectors;

import ag04.hackathon2020.moneyheist.dto.MemberSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;

public class Member {

	private Long id;
	
	private String email;
	
	private String name;
	
	private Sex sex;
	
	private List<MemberSkill> memberSkills;
	
	private Skill mainSkill;
	
	private MemberStatus status;

	private String password;
	
	private Integer active;
	
	private Role role;
	
	public Member() {
		super();
	}
	
	public Member(Long id, String email, String name, Sex sex, List<MemberSkill> memberSkills, Skill mainSkill,
			MemberStatus status, String password, Integer active, Role role) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.sex = sex;
		this.memberSkills = memberSkills;
		this.mainSkill = mainSkill;
		this.status = status;
		this.password = password;
		this.active = active;
		this.role = role;
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

	public MemberStatus getStatus() {
		return status;
	}

	public String getPassword() {
		return password;
	}

	public Integer getActive() {
		return active;
	}

	public Role getRole() {
		return role;
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

	public void setMainSkill(Skill mainSkill) {
		this.mainSkill = mainSkill;
	}

	public void setStatus(MemberStatus status) {
		this.status = status;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Skill> getSkills() {
		return memberSkills.stream()
				.filter(ms -> ms != null)
				.map(ms -> ms.getSkill())
				.collect(Collectors.toList());

	}
	
	public void setMemberSkills(MemberSkillArrayDto skillArrayDto) {
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

}
