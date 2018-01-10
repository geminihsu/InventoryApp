package spirit.fitness.scanner.common;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import spirit.fitness.scanner.model.Locationbean;
import spirit.fitness.scanner.model.Modelbean;

public class Constrant {
	public static String webUrl = "http://172.16.2.96:5000";
	
	public static HashMap<String,Modelbean> models;
	public static HashMap<String,Locationbean> locations;
	 
	//Zone mapping Table key : zone number, value : location number
    public static int ZONE_CODE_LEN_LIMIT = 3;
    public static int ZONE_CODE_1 = 1;
    public static int ZONE_CODE_2 = 2;
    public static int ZONE_CODE_3 = 3;
    public static int ZONE_CODE_4 = 4;
    //Zone 1 range
    public static int ZONE_CODE_1_MIN = 0;
    public static int ZONE_CODE_1_MAX = 69;
    //Zone 2 range
    public static int ZONE_CODE_2_MIN = 701;
    public static int ZONE_CODE_2_MAX = 992;

    //Zone 3 range
    public static int ZONE_CODE_3_A = 881;
    public static int ZONE_CODE_3_B = 891;
    public static int ZONE_CODE_3_C = 901;
    public static int ZONE_CODE_3_D = 911;
    //Zone 4 range
    public static int ZONE_CODE_4_ONE = 888;
    
    //Shipping
    public static int ZONE_CODE_SHIPPING = 999;
    
    public static Color BACKGROUN_COLOR = new java.awt.Color(235, 240, 255);
    public static Color TABLE_COLOR = new java.awt.Color(255, 255, 255);
    public static Color DISPALY_ITEMS_TABLE_COLOR = new java.awt.Color(255, 255, 240);
}
