package ag04.hackathon2020.moneyheist.validation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistSkill;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.exception.ApiException;
import ag04.hackathon2020.moneyheist.mapper.HeistMapper;

@Component
@Scope("singleton")
public class HeistValidator {

	private HeistMapper heistMapper;
	
	public HeistValidator(HeistMapper heistMapper) {
		this.heistMapper = heistMapper;
	}
	
	public void validateName(Heist heist) {
		String heistName = heist.getName();
		Heist existingHeist = heistMapper.findByName(heistName);
		if (existingHeist != null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Heist already exists", "Heist with name '" + heist.getName() + "' already exist. Please, make sure to provide a heist with unique name", null);
		}
	}
	
	public void validateDates(Heist heist) {
		Date startTime = heist.getStartTime();
		Date endTime = heist.getEndTime();
		Date currentTime = new Date();
		Long start = startTime.getTime();
		Long current = currentTime.getTime();
		Long end = endTime.getTime();
		if (start < current || end < current || start > end) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid dates", "Invalid startTime and/or endTime parameters given. Please, make sure to give startTime later than now and endTime that is after startTime", null);
		}
	}
	
	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	public void validateDuplicateSkills(Heist heist) {
		List<HeistSkill> skills = heist.getHeistSkills();
		int totalCount = skills.size();
		if (skills != null && !skills.isEmpty()) {
			int distinctCount = (int) skills.stream()
		        .filter(distinctByKey(hs -> Arrays.asList(hs.getSkill().getName(), hs.getLevel())))
		        .count();
			if (distinctCount != totalCount) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "Duplicate heist skills", "Duplicate heist skills were provided. Please, make sure to only provide skills that don't have the same level and the same name", null);
			}
		}
	}

	public void validateEligibleMembersForConfirmation(Heist heist, List<Member> membersForConfirmation, List<Member> eligibleMembers) {
		for (Member confirmationMember : membersForConfirmation) {
			if (eligibleMembers.stream().noneMatch(em -> em.getName().equals(confirmationMember.getName()))) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "Member not eligible", "At least one given member is not eligible for heist confirmation", null);
			}
		}
	}
	
	public void validateHeistStatus(Heist heist) {
		if (!heist.getHeistStatus().equals(HeistStatus.PLANNING)) {
			throw new ApiException(HttpStatus.METHOD_NOT_ALLOWED, "Heist status is not PLANNING", "Heist with id: " + heist.getId() + " status is: " + heist.getHeistStatus(), null);
		}
	}
	
}
