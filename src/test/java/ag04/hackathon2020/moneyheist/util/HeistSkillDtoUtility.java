package ag04.hackathon2020.moneyheist.util;

import java.util.List;

import ag04.hackathon2020.moneyheist.dto.HeistSkillDto;

public class HeistSkillDtoUtility {

	public static List<HeistSkillDto> getRandomHeistSkillDtos() {
		return RandomUtility.getRandomListElements(HeistSkillDtoUtility.valuesToList(), false);
	}
	
	public static List<HeistSkillDto> valuesToList() {
		return List.of(
				new HeistSkillDto("DRIVING", "****", 1),
				new HeistSkillDto("COMBAT", "****", 2),
				new HeistSkillDto("MONEY-LAUNDERING", "****", 1),
				new HeistSkillDto("LOCK-BREAKING", "****", 3),
				new HeistSkillDto("HIDING", "****", 1)
		);
	}
	
}
