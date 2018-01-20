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
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Shippingbean;

/**
 * Created by User on 5/1/2017.
 */

public interface ShippingCallback {
    
	@Headers("Content-Type: application/json")
	@GET("api/Shippings")
	Call<List<Shippingbean>> getAllItems();

	@GET("api/Shippings/date/{date}")
	Call<List<Shippingbean>> getItemsByDate(@Path("date") String location);
	
	@GET("api/Shippings/model/{modelNo}")
	Call<List<Shippingbean>> getItemsByModelNo(@Path("modelNo") Integer modelNo);
	@GET("api/Shippings/salesOrder/{salesOrder}")
	Call<List<Shippingbean>> getItemsBySalesOrder(@Path("salesOrder") String modelNo);
	//Post one item
	/*@POST("/api/FGInventory")
	Call<Itembean> createItem(@Body Itembean itembean);*/
	
	//Post ArrayList items
	@POST("api/Shippings")
	Call<List<Shippingbean>> createItem(@Body List<Shippingbean> itembean);

	/*//PUT one item
	@PUT("/api/FGInventory/{seq}")
	Call<Itembean> updateItem(@Path("seq") Integer seq, @Body Itembean item);*/

	
	//PUT more than one item
	@PUT("/api/Shippings")
	Call<List<Shippingbean>> updateItem(@Body List<Shippingbean> itembean);

	//Delete no work
	@DELETE("/api/Shippings/{seq}")
	Call<Shippingbean> deleteItem(@Path("seq") Integer seq);

}
