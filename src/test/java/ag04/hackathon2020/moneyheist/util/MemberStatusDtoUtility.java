package ag04.hackathon2020.moneyheist.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ag04.hackathon2020.moneyheist.entity.MemberStatus;

public class MemberStatusDtoUtility {
	
	public static List<String> valuesToList() {
		List<MemberStatus> statuses = Arrays.asList(MemberStatus.values());
		return statuses.stream().map(s -> s.toString()).collect(Collectors.toList());
	}
	
}
