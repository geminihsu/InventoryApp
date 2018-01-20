package spirit.fitness.scanner.model;

import com.google.gson.annotations.SerializedName;

public class Reportbean {
	
	@SerializedName("FG")
	public String FG;
	@SerializedName("Model")
	public String Model;
	@SerializedName("OnHand")
	public Integer onHand;
	@SerializedName("total")
	public Integer total;
	@SerializedName("Shipped")
	public Integer shipped;
	@SerializedName("Unshippable")
	public Integer unshippable;
	
	@SerializedName("Zone1")
	public Integer zone1;
	@SerializedName("Zone2")
	public Integer zone2;
	@SerializedName("ReturnItem")
	public Integer returnItem;
	@SerializedName("ShowRoom")
	public Integer showRoom;
	@SerializedName("MinQuantity")
	public Integer minQuantity;
	
}
