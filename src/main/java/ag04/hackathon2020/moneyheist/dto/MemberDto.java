package ag04.hackathon2020.moneyheist.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonProperty;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.MemberStatus;
import ag04.hackathon2020.moneyheist.entity.Role;
import ag04.hackathon2020.moneyheist.entity.Sex;
import ag04.hackathon2020.moneyheist.entity.Skill;

public class MemberDto {

	private String email;
	
	private String name;
	
	private Sex sex;
	
	@JsonProperty("skills")
	private List<MemberSkillDto> memberSkillDtos;
	
	private String mainSkill;
	
	private String status;
	
	private String role;
	
	private Integer active;
	
	private String password;

	public MemberDto() {
		super();
	}

	public MemberDto(String email, String name, Sex sex, List<MemberSkillDto> memberSkillDtos, String mainSkill,
			String status, String role, Integer active, String password) {
		super();
		this.email = email;
		this.name = name;
		this.sex = sex;
		this.memberSkillDtos = memberSkillDtos;
		this.mainSkill = mainSkill;
		this.status = status;
		this.role = role;
		this.active = active;
		this.password = password;
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

	public String getRole() {
		return role;
	}

	public Integer getActive() {
		return active;
	}

	public String getPassword() {
		return password;
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

	public void setRole(String role) {
		this.role = role;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static Member toEntity(MemberDto dto) {
		Member entity = new Member();
		entity.setEmail(dto.getEmail());
		entity.setName(dto.getName());
		entity.setSex(dto.getSex());
		entity.setStatus(dto.getStatus() == null ? null : MemberStatus.valueOf(dto.getStatus().toUpperCase()));
		entity.setMainSkill(dto.getMainSkill() == null ? null : new Skill(null, dto.getMainSkill().toUpperCase()));
		entity.setMemberSkills(dto.getMemberSkillDtos() == null ? null : dto.getMemberSkillDtos().stream().map(msd -> MemberSkillDto.toEntity(msd)).collect(Collectors.toList()));
		entity.setActive(dto.getActive());
		entity.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
		String role = dto.getRole();
		Long roleId = role.equals("ORGANISER") ? 1L : 2L;
		entity.setRole(new Role(roleId, role));
		return entity;
	}
	
	public static MemberDto toDto(Member entity) {
		MemberDto dto = new MemberDto();
		dto.setName(entity.getName());
		dto.setSex(entity.getSex());
		dto.setEmail(entity.getEmail());
		dto.setMainSkill(entity.getMainSkill() == null ? null : entity.getMainSkill().getName().toUpperCase());
		dto.setStatus(entity.getStatus() == null ? null : entity.getStatus().toString().toUpperCase());
		dto.setMemberSkillDtos(entity.getMemberSkills() == null ? null : entity.getMemberSkills().stream().map(ms -> MemberSkillDto.toDto(ms)).collect(Collectors.toList()));
		dto.setActive(entity.getActive());
		dto.setRole(entity.getRole().getValue());
		dto.setPassword(entity.getPassword());
		return dto;
	}
	
}
