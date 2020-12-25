package ag04.hackathon2020.moneyheist.dto;

import java.util.List;
import java.util.stream.Collectors;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistSkill;
import ag04.hackathon2020.moneyheist.entity.Member;

public class EligibleMembersDto {

	private List<HeistSkillDto> skills;
	
	private List<MemberDto> members;

	public EligibleMembersDto() {
		super();
	}

	public EligibleMembersDto(List<HeistSkillDto> skills, List<MemberDto> members) {
		super();
		this.skills = skills;
		this.members = members;
	}

	public List<HeistSkillDto> getSkills() {
		return skills;
	}

	public List<MemberDto> getMembers() {
		return members;
	}

	public void setSkills(List<HeistSkillDto> skills) {
		this.skills = skills;
	}

	public void setMembers(List<MemberDto> members) {
		this.members = members;
	}
	
	public static EligibleMembersDto toDto(Heist heist, List<Member> eligibleMembers) {
		EligibleMembersDto dto = new EligibleMembersDto();
		List<HeistSkill> heistSkills = heist.getHeistSkills();
		List<HeistSkillDto> heistSkillDtos = heistSkills.stream().map(hs -> HeistSkillDto.toDto(hs)).collect(Collectors.toList());
		List<MemberDto> memberDtos = eligibleMembers.stream().map(m -> MemberDto.toDto(m)).collect(Collectors.toList());
		dto.setSkills(heistSkillDtos);
		dto.setMembers(memberDtos);
		return dto;
	}
	
}
