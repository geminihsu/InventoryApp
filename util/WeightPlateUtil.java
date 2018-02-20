package spirit.fitness.scanner.util;

public class WeightPlateUtil {

	private static final String WEIGHT_PLATE_170Cartons = "900170";
	private static final String WEIGHT_PLATE_20_80 = "990080";
	private static final String WEIGHT_PLATE_200Cartons = "990200";
	private static final String WEIGHT_PLATE_90_150 = "990150";
	
	private static String MS7MODEL = "900MS7";
	private static String LP7MODEL = "900LP7";
	private static String FT7MODEL = "900FT7";
	private static String DR7MODEL = "900DR7";

	public static boolean isWeightPlate(String item) {
		if (item.equals(WEIGHT_PLATE_170Cartons) || item.equals(WEIGHT_PLATE_20_80)
				|| item.equals(WEIGHT_PLATE_200Cartons) || item.equals(WEIGHT_PLATE_90_150))
			return true;

		return false;
	}
	
	public static String modelAppendWithPart(String modelNo) 
	{
		String result = "";
		if(modelNo.equals(MS7MODEL))
			result = " (900MS7-2, 900MS7-3, 90200 x3)";
		else if(modelNo.equals(LP7MODEL))
			result = " (990200 x1)";
		else if(modelNo.equals(FT7MODEL))
			result = " (900FT7-2, 990170 x1)";
		
		return result;	
	}
	
	public static boolean isModelParts(String modelNo) 
	{
		if(modelNo.equals(MS7MODEL) || modelNo.equals(LP7MODEL) || modelNo.equals(FT7MODEL)|| modelNo.equals(DR7MODEL))
			return true;
		return false;
	}
}
