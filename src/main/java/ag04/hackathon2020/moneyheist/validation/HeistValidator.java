package ag04.hackathon2020.moneyheist.validation;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistSkill;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.exception.ApiException;

@Component
public class HeistValidator {
	
	public void validateIfProperDates(Heist heist) {
		Long start = heist.getStartTime().getTime();
		Long current = new Date().getTime();
		Long end = heist.getEndTime().getTime();
		if (start < current || end < current || start > end) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid dates", "Invalid startTime and/or endTime parameters given. Please, make sure to give startTime latter than now and endTime that is after startTime", null);
		}
	}
	
	public void validateIfSkillsUniqueByNameAndLevel(Heist heist) {
		List<HeistSkill> skills = heist.getHeistSkills();
		if (skills != null && !skills.isEmpty()) {
			int totalCount = skills.size();
			for (int i = 0; i < totalCount; i++) {
				for (int j = 0; j < totalCount; j++) {
					HeistSkill hsi = skills.get(i);
					HeistSkill hsj = skills.get(j);
					Skill si = hsi.getSkill();
					Skill sj = hsj.getSkill();
					if (i != j && hsi.getLevel().equals(hsj.getLevel()) && si.getName().equals(sj.getName())) {
						throw new ApiException(HttpStatus.BAD_REQUEST, "Duplicate heist skills", "Duplicate heist skills were provided. Please, make sure to only provide skills that don't have the same level and the same name", null);
					}
				}
			}
		}
	}

	public void validateIfMembersEligibleForConfirmation(Heist heist, List<Member> membersForConfirmation, List<Member> eligibleMembers) {
		for (Member confirmationMember : membersForConfirmation) {
			if (eligibleMembers.stream().noneMatch(em -> em.getName().equals(confirmationMember.getName()))) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "Member not eligible", "At least one given member is not eligible for heist confirmation", null);
			}
		}
	}
	
	public void validateIfProperHeistStatus(Heist heist, List<HeistStatus> properHeistStatuses) {
		if (properHeistStatuses == null || properHeistStatuses.isEmpty()) {
			throw new RuntimeException("Empty properHeistStatuses parameter provided");
		}
		HeistStatus givenHeistStatus = heist.getHeistStatus();
		if (!properHeistStatuses.contains(givenHeistStatus)) {
			String errorTitle = "Heist status is not " + properHeistStatuses.get(0);
			for (int i = 1; i < properHeistStatuses.size(); i++) {
				errorTitle += " or " + properHeistStatuses.get(i);
			}
			String errorDescription = "Heist with id: " + heist.getId() + " has status: " + givenHeistStatus;
			throw new ApiException(HttpStatus.METHOD_NOT_ALLOWED, errorTitle, errorDescription, null);
		}
	}

	public void validateIfExists(Heist heist) {
		if (heist == null) {
			throw new ApiException(HttpStatus.NOT_FOUND, "Heist not found", "Given heist not found. Please, make sure to provide a heist with an existing id", null);
		}	
	}
	
	public void validateIfNotExists(Heist heist) {
		if (heist != null) {
			String heistName = heist.getName();
			throw new ApiException(HttpStatus.BAD_REQUEST, "Heist already exists", "Heist with name '" + heistName + "' already exist. Please, make sure to provide a heist with unique name", null);
		}
	}

	public void validateIfMembersNotEmpty(List<Member> members) {
		if (members.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid members array", "Empty arrays are not allowed", null);
		}
	}
	
}
