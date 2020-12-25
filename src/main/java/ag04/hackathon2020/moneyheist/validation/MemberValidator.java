package ag04.hackathon2020.moneyheist.validation;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.MemberSkill;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.exception.ApiException;
import ag04.hackathon2020.moneyheist.mapper.MemberMapper;

@Component
@Scope("singleton")
public class MemberValidator {

	private MemberMapper memberMapper;
	
	public MemberValidator(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	public void validateDuplicateSkillNames(Member member) {
		List<MemberSkill> skills = member.getMemberSkills();
		int totalCount = skills.size();
		if (skills != null && !skills.isEmpty()) {
			int distinctCount = (int) skills.stream().map(ms -> ms.getSkill().getName()).distinct().count();
			if (distinctCount != totalCount) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "Duplicate member skill names", "Duplicate member skill names were provided. Please, make sure to only provide distinct skill names", null);
			}
		}
	}
	
	public void validateSkillLevels(Member member) {
		List<MemberSkill> skills = member.getMemberSkills();
		if (skills != null && !skills.isEmpty()) {
			for (MemberSkill ms : skills) {
				ms.getSkill().setName(ms.getSkill().getName());
				String level = ms.getLevel();
				if (level != null && !"".equals(level)) {
					int asteriskCount = (int) level.chars().filter(ch -> ch == '*').count();
					if (level.length() > 10 || asteriskCount != level.length()) {
						throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid skill level provided", "Invalid skill level was provided. Please, make sure to only provide a string consisted of 0 to 10 asterisks (if left empty, 1 asterisk gets applied as a default value)", null);
					}
				}
			}
		}
	}
	
	public void validateMemberAlreadyExists(Member member) {
		String memberEmail = member.getEmail();
		Member existingMember = memberMapper.findByEmail(memberEmail);
		if (existingMember != null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Member already exists", "Member with email '" + member.getEmail() + "' already exist. Please, make sure to provide a member with unique email", null);
		}
	}
	
	public void validateMainSkillReference(Member member) {
		List<Skill> skills = member.getSkills();
		String mainSkill = member.getMainSkill() == null ? null : member.getMainSkill().getName();
		if (member.getMainSkill() != null) {
			member.getMainSkill().setName(mainSkill);
		}
		if (mainSkill != null && !mainSkill.isEmpty() && Skill.find(mainSkill, skills) == null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "mainSkill doesn't reference any skill from skills array", "Invalid mainSkill was provided. Please, make sure to only provide mainSkill which references member's skills from skills array",  null);
		}
	}
	
	public void validateSkillExists(Member member, String skillName) {
		List<Skill> skills = member.getSkills();
		if (skills.stream().filter(s -> s.getName().equalsIgnoreCase(skillName)).findAny().isEmpty()) {
			throw new ApiException(HttpStatus.NOT_FOUND, "Member doesn't have the skill", "Member with id: " + member.getId() + " has no skill named: " + skillName,  null);
		}
	}
	
}
