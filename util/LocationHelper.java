package spirit.fitness.scanner.util;

import spirit.fitness.scanner.common.Constrant;

/**
 * Created by geminihsu on 01/12/2017.
 */

public class LocationHelper {

	// find the location mapping zone number
	public static int MapZoneCode(String location) {
		int code = -1;
		int locNum = Integer.valueOf(location);

		if (locNum == Constrant.ZONE_CODE_4_ONE)
			code = Constrant.ZONE_CODE_4;
		else if (locNum == Constrant.ZONE_CODE_5_ONE)
			code = Constrant.ZONE_CODE_5;
		else if (locNum == Constrant.ZONE_CODE_6_ONE)
			code = Constrant.ZONE_CODE_6;
		else if (locNum == Constrant.ZONE_CODE_7_ONE)
			code = Constrant.ZONE_CODE_7;
		else if (locNum >= Constrant.ZONE_CODE_1_MIN && locNum <= Constrant.ZONE_CODE_1_MAX)
			code = Constrant.ZONE_CODE_1;
		else if (locNum >= Constrant.ZONE_CODE_2_MIN && locNum <= Constrant.ZONE_CODE_2_MAX
				&& locNum != Constrant.ZONE_CODE_3_A && locNum != Constrant.ZONE_CODE_3_B
				&& locNum != Constrant.ZONE_CODE_3_C && locNum != Constrant.ZONE_CODE_3_D
				&& locNum != Constrant.ZONE_CODE_4) {
			if (locNum % 10 == 1 || locNum % 10 == 2)
				code = Constrant.ZONE_CODE_2;
		} else if (locNum == Constrant.ZONE_CODE_3_A || locNum == Constrant.ZONE_CODE_3_B
				|| locNum == Constrant.ZONE_CODE_3_C || locNum == Constrant.ZONE_CODE_3_D) {
			code = Constrant.ZONE_CODE_3;
		}

		return code;
	}

	public static String convertLocation(String location) {
		String result = location;
		int locLen = result.length();

		while (locLen < 3) {
			result = "0" + result;
			locLen++;
		}

		return result;
	}

	// find the Display name mapping zone number
	public static String DisplayZoneCode(int number) {
		if (number == 1)
			return "Zone 1";
		else if (number == 2)
			return "Zone 2";

		else if (number == 3)
			return "Return";

		else if (number == 4)
			return "Show Room";

		else if (number == 5)
			return "Rework";

		else if (number == 6)
			return "QC";
		else if (number == 7)
			return "Scrapped";

		return "";
	}

}
