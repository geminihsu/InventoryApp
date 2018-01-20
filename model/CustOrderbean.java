package spirit.fitness.scanner.model;

import com.google.gson.annotations.SerializedName;

public class CustOrderbean {
	
	@SerializedName("Seq")
	public Integer seq;
	@SerializedName("Sales_Order")
	public String salesOrder;
	@SerializedName("Date")
	public String shippingDate;
	@SerializedName("Bill_to")
	public String bill_to;
	@SerializedName("Ship_to_Address_Line_One")
	public String shipToAddress;
	@SerializedName("Ship_to_City")
	public String shipToCity;
	@SerializedName("Ship_to_State")
	public String shipToState;
	@SerializedName("Ship_to_Zipcode")
	public String shipToZipCode;
	@SerializedName("Ship_to_Country")
	public String shipToCountry;
	@SerializedName("Customer_PO")
	public String customerPO;
	@SerializedName("Ship_Via")
	public String shipVia;
	@SerializedName("Quantity")
	public String quantity;
	@SerializedName("Item_ID")
	public String ItemID;
	@SerializedName("Description")
	public String description;
	@SerializedName("Closed")
	public Boolean closed;
}
