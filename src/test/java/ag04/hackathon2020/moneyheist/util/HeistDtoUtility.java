package ag04.hackathon2020.moneyheist.util;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import ag04.hackathon2020.moneyheist.dto.HeistDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillDto;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;

public class HeistDtoUtility {

	public static HeistDto getRandomHeistDto() {
		String name = RandomUtility.getRandomString(10);
		String location = RandomUtility.getRandomString(10);
		String iso8601Format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
		SimpleDateFormat formatter = new SimpleDateFormat(iso8601Format);
		String startTime = formatter.format(new Date(new Date().getTime() + (60 * 1000)));
		String endTime = formatter.format(new Date(new Date().getTime() + (120 * 1000)));
		HeistStatus heistStatus = HeistStatus.PLANNING;
		List<HeistSkillDto> heistSkillDtos = HeistSkillDtoUtility.getRandomHeistSkillDtos();
		return new HeistDto(name, location, startTime, endTime, heistSkillDtos, heistStatus, null);
	}
	
	public static HeistDto getPreparedHeistDto() {
		String name = "Test";
		String location = "Test";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		ZonedDateTime now = ZonedDateTime.now();
		long delay = 2L;
		String startTime = now.plusSeconds(delay).format(formatter);
		String endTime = now.plusSeconds(delay * 2).format(formatter);
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("COMBAT", "*", 1),
			new HeistSkillDto("HIDING", "*", 1)
		);
		HeistStatus heistStatus = HeistStatus.PLANNING;
		return new HeistDto(name, location, startTime, endTime, heistSkillDtos, heistStatus, null);
	}
	
}
