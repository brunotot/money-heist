package ag04.hackathon2020.moneyheist.util;

import java.util.Arrays;
import java.util.List;

import ag04.hackathon2020.moneyheist.entity.Sex;

public class SexDtoUtility {

	public static List<Sex> valuesToList() {
		return Arrays.asList(Sex.values());
	}

}
