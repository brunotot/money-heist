package ag04.hackathon2020.moneyheist.util;

import java.util.List;

import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;

public class MemberSkillDtoUtility {

	public static List<MemberSkillDto> getRandomMemberSkillDtos() {
		return RandomUtility.getRandomListElements(MemberSkillDtoUtility.valuesToList(), false);
	}
	
	public static List<MemberSkillDto> valuesToList() {
		return List.of(
			new MemberSkillDto("DRIVING", "*****", 0),
			new MemberSkillDto("COMBAT", "*****", 0),
			new MemberSkillDto("MONEY-LAUNDERING", "*****", 0),
			new MemberSkillDto("LOCK-BREAKING", "*****", 0),
			new MemberSkillDto("HIDING", "*****", 0)
		);
	}
	
}
