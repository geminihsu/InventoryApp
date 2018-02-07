package spirit.fitness.scanner.model;

import com.google.gson.annotations.SerializedName;

public class ModelDailyReportbean{
	
	@SerializedName("ModelNo")
	public String ModelNo;
	@SerializedName("ModelFG")
	public String ModelFG;
	@SerializedName("Previous")
	public Integer Previous;
	@SerializedName("Shipped")
	public Integer Shipped;
	@SerializedName("Received")
	public Integer Received;
	@SerializedName("Scrapped")
	public Integer Scrapped;
	@SerializedName("OnHand")
	public Integer OnHand;
	
	@SerializedName("ShowRoom")
	public Integer ShowRoom;
	@SerializedName("ReturnItem")
	public Integer ReturnItem;
	@SerializedName("Rework")
	public Integer Rework;
	@SerializedName("QC")
	public Integer QC;
	@SerializedName("Total")
	public Integer Total;
	
}
