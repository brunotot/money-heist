package ag04.hackathon2020.moneyheist.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtility {

	private static Random r = new Random();
	
	public static Integer getRandomInt(Integer range) {
		return r.nextInt(range) + 1;
	}
	
	public static <T> List<T> getRandomListElements(List<T> list, boolean includeNull) {
		List<T> copy = new ArrayList<>(list);
		if (includeNull) {
			copy.add(null);
		}
		List<T> newList = new ArrayList<>();
		Integer range = RandomUtility.getRandomInt(copy.size());
		while (range > 0) {
			T elem = RandomUtility.getRandomListElement(copy, false);
			newList.add(elem);
			copy.remove(elem);
			range--;
		}
		return newList;
	}
	
	public static <T> T getRandomListElement(List<T> list, boolean includeNull) {
		List<T> copy = new ArrayList<>(list);
		if (includeNull) {
			copy.add(null);
		}
	    return copy.get(r.nextInt(copy.size()));
	}
	
	public static String getRandomString(Integer length) {
		return RandomStringUtils.random(length, true, false);
	}
	
}
