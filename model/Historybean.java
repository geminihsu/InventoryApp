package spirit.fitness.scanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 5/1/2017.
 */

public class Historybean {



    @SerializedName("seq")
    public Integer seq;
    @SerializedName("SN")
    public String SN;
    @SerializedName("Date")
    public String date;
    @SerializedName("Location")
    public String location;
    @SerializedName("ModelNo")
    public String modelNo;
    @SerializedName("SalesOrder")
    public String salesOrder;
    @SerializedName("TrackingNo")
    public String trackingNo;
    @SerializedName("BillTo")
    public String billTo;
    @SerializedName("ShipVia")
    public String shipVia;
    @SerializedName("ShipState")
    public String shipState;
    @SerializedName("ShipCity")
    public String shipCity;
  
    @Override
    public String toString() {
        return "Feed{" +
                "Item SN ='" + SN + '\'' +
                ", Item SalesOrder=" + salesOrder +
                '}';
    }
}
