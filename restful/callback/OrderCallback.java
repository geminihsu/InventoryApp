package spirit.fitness.scanner.restful.callback;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Modelbean;

/**
 * Created by User on 5/1/2017.
 */

public interface OrderCallback {
    
	@Headers("Content-Type: application/json")
	@GET("api/CustOrders")
	Call<List<CustOrderbean>> getAllOrders();
	
	@GET("api/CustOrders/salesOrder/{salesorder}")
	Call<List<CustOrderbean>> getItemsBySalesOrder(@Path("salesorder") String salesOrder);
	//Post one item
	/*@POST("/api/FGInventory")
	Call<Itembean> createItem(@Body Itembean itembean);*/
	
	//PUT more than one item
	@PUT("api/CustOrders")
	Call<List<CustOrderbean>> updateItem(@Body List<CustOrderbean> itembean);

}
