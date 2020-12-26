package ag04.hackathon2020.moneyheist.validation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.MemberSkill;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.exception.ApiException;

@Component
public class MemberValidator {

	public void validateIfSkillsUniqueByName(Member member) {
		List<MemberSkill> skills = member.getMemberSkills();
		if (skills != null && !skills.isEmpty()) {
			int totalCount = skills.size();
			for (int i = 0; i < totalCount; i++) {
				for (int j = 0; j < totalCount; j++) {
					Skill si = skills.get(i).getSkill();
					Skill sj = skills.get(j).getSkill();
					if (i != j && si.getName().equals(sj.getName())) {
						throw new ApiException(HttpStatus.BAD_REQUEST, "Duplicate member skill names", "Duplicate member skill names were provided. Please, make sure to only provide distinct skill names", null);
					}
				}
			}
		}
	}
	
	public void validateIfProperSkillLevels(Member member) {
		List<MemberSkill> skills = member.getMemberSkills();
		if (skills != null && !skills.isEmpty()) {
			for (MemberSkill ms : skills) {
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
	
	public void validateIfNotExists(Member member) {
		if (member != null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Member already exists", "Member with id: " + member.getId() + ", email '" + member.getEmail() + "' and name: " + member.getName() + " already exist. Please, make sure to provide a member with unique email", null);
		}
	}
	
	public void validateIfExists(Member member) {
		if (member == null) {
			throw new ApiException(HttpStatus.NOT_FOUND, "Member not found", "Given member not found. Please, make sure to provide a member with an existing id", null);
		}
	}
	
	public void validateIfMainSkillProperlyReferenced(Member member) {
		List<Skill> skills = member.getSkills();
		Skill mainSkill = member.getMainSkill();
		String mainSkillString = null;
		if (mainSkill != null) {
			mainSkillString = mainSkill.getName();
		}
		if (mainSkill != null && !mainSkillString.isEmpty() && Skill.find(mainSkillString, skills) == null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "mainSkill doesn't reference any skill from skills array", "Invalid mainSkill was provided. Please, make sure to only provide mainSkill which references member's skills from skills array",  null);
		}
	}
	
	public void validateIfSkillExistsOnMember(Member member, String skillName) {
		List<Skill> skills = member.getSkills();
		final String skillNameUppercase = skillName.toUpperCase(); 
		Skill foundSkill = skills.stream().filter(s -> s.getName().equals(skillNameUppercase)).findFirst().orElse(null);
		if (foundSkill == null) {
			throw new ApiException(HttpStatus.NOT_FOUND, "Member doesn't have the skill", "Member with id: " + member.getId() + " has no skill named: " + skillName,  null);
		}
	}
	
}
