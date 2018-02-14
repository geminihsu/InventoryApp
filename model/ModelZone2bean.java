package spirit.fitness.scanner.model;

import com.google.gson.annotations.SerializedName;

public class ModelZone2bean {
	
	@SerializedName("FG")
	public String FG;
	
	@SerializedName("Model")
	public String Model;
	
	@SerializedName("Z2MinQty")
	public Integer Z2MinQty;
	
	@SerializedName("Zone1Code")
	public String Zone1Code;

	@SerializedName("Zone2Code")
	public String Zone2Code;
	
	@SerializedName("Z2CurtQty")
	public Integer Z2CurtQty;
	
	@SerializedName("Z2MaxQty")
	public Integer Z2MaxQty;
	
	@SerializedName("PalletNum")
	public Integer PalletNum;
}
