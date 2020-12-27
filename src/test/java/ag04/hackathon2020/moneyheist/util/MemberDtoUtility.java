package ag04.hackathon2020.moneyheist.util;

import java.util.List;
import java.util.stream.Collectors;

import ag04.hackathon2020.moneyheist.dto.MemberDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;
import ag04.hackathon2020.moneyheist.entity.Sex;

public class MemberDtoUtility {

	public static MemberDto getRandomMemberDto() {
		String name = RandomUtility.getRandomString(10);
		String password = RandomUtility.getRandomString(10);
		String role = RandomUtility.getRandomListElement(RoleDtoUtility.valuesToList(), false);
		String email = name + "@gmail.com";
		Sex sex = RandomUtility.getRandomListElement(SexDtoUtility.valuesToList(), false);
		List<MemberSkillDto> memberSkillDtos = MemberSkillDtoUtility.getRandomMemberSkillDtos();
		String mainSkill = RandomUtility.getRandomListElement(memberSkillDtos.stream().map(msd -> msd.getName()).collect(Collectors.toList()), true);
		String status = RandomUtility.getRandomListElement(MemberStatusDtoUtility.valuesToList(), false);
		Integer active = 1;
		return new MemberDto(email, name, sex, memberSkillDtos, mainSkill, status, role, active, password);
	}
	
}
