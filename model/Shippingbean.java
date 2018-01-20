package spirit.fitness.scanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 5/1/2017.
 */

public class Shippingbean {



    @SerializedName("seq")
    public Integer seq;
    @SerializedName("SN")
    public String SN;
    @SerializedName("date")
    public String date;
    @SerializedName("Location")
    public String Location;
    @SerializedName("ModelNo")
    public String ModelNo;
    @SerializedName("SalesOrder")
    public String SalesOrder;
    @SerializedName("TrackingNo")
    public String TrackingNo;

    @Override
    public String toString() {
        return "Feed{" +
                "Item SN ='" + SN + '\'' +
                ", Item Location=" + Location +
                '}';
    }
}
