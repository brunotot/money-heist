package ag04.hackathon2020.moneyheist.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.Sex;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.entity.MemberStatus;

public class MemberDto {

	private Long id;
	
	private String email;
	
	private String name;
	
	private Sex sex;
	
	@JsonProperty("skills")
	private List<MemberSkillDto> memberSkillDtos;
	
	private String mainSkill;
	
	private String status;

	public MemberDto() {
		super();
	}

	public MemberDto(Long id, String email, String name, Sex sex, List<MemberSkillDto> memberSkillDtos, String mainSkill,
			String status) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.sex = sex;
		this.memberSkillDtos = memberSkillDtos;
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

	public List<MemberSkillDto> getMemberSkillDtos() {
		return memberSkillDtos;
	}

	public String getMainSkill() {
		return mainSkill;
	}

	public String getStatus() {
		return status;
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

	public void setMemberSkillDtos(List<MemberSkillDto> memberSkillDtos) {
		this.memberSkillDtos = memberSkillDtos;
	}

	public void setMainSkill(String mainSkill) {
		this.mainSkill = mainSkill;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static Member toEntity(MemberDto dto) {
		Member entity = new Member();
		entity.setEmail(dto.getEmail());
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setSex(dto.getSex());
		entity.setStatus(dto.getStatus() == null ? null : MemberStatus.valueOf(dto.getStatus().toUpperCase()));
		entity.setMainSkill(dto.getMainSkill() == null ? null : new Skill(null, dto.getMainSkill().toUpperCase()));
		entity.setMemberSkills(dto.getMemberSkillDtos() == null ? null : dto.getMemberSkillDtos().stream().map(msd -> MemberSkillDto.toEntity(msd)).collect(Collectors.toList()));
		return entity;
	}
	
	public static MemberDto toDto(Member entity) {
		MemberDto dto = new MemberDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setSex(entity.getSex());
		dto.setEmail(entity.getEmail());
		dto.setMainSkill(entity.getMainSkill() == null ? null : entity.getMainSkill().getName().toUpperCase());
		dto.setStatus(entity.getStatus() == null ? null : entity.getStatus().toString().toUpperCase());
		dto.setMemberSkillDtos(entity.getMemberSkills() == null ? null : entity.getMemberSkills().stream().map(ms -> MemberSkillDto.toDto(ms)).collect(Collectors.toList()));
		return dto;
	}
	
}
