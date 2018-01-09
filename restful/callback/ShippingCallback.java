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

/**
 * Created by User on 5/1/2017.
 */

public interface ShippingCallback {
    
	@Headers("Content-Type: application/json")
	@GET("api/Shippings")
	Call<List<Itembean>> getAllItems();

	@GET("api/Shippings/date/{date}")
	Call<List<Itembean>> getItemsByDate(@Path("date") String location);
	
	@GET("api/Shippings/model/{modelNo}")
	Call<List<Itembean>> getItemsByModelNo(@Path("modelNo") Integer modelNo);
	//Post one item
	/*@POST("/api/FGInventory")
	Call<Itembean> createItem(@Body Itembean itembean);*/
	
	//Post ArrayList items
	@POST("api/Shippings")
	Call<List<Itembean>> createItem(@Body List<Itembean> itembean);

	/*//PUT one item
	@PUT("/api/FGInventory/{seq}")
	Call<Itembean> updateItem(@Path("seq") Integer seq, @Body Itembean item);*/

	
	//PUT more than one item
	@PUT("/api/Shippings")
	Call<List<Itembean>> updateItem(@Body List<Itembean> itembean);

	//Delete no work
	@DELETE("/api/Shippings/{seq}")
	Call<Itembean> deleteItem(@Path("seq") Integer seq);

}