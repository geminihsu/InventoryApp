package spirit.fitness.scanner.restful.callback;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import spirit.fitness.scanner.model.Containerbean;
import spirit.fitness.scanner.model.Historybean;


public interface ContainerCallback {
	@Headers("Content-Type: application/json")
	
	@GET("api/Containers")
	Call<List<Containerbean>> getAllItems();
	
	@GET("api/Containers/date/{date}")
	Call<List<Containerbean>> getItemsByDate(@Path("date") String date);
	
	@GET("api/Containers/{containerNo}")
	Call<List<Containerbean>> getItemsBycontainerNo(@Path("containerNo") String containerNo);

	//Post ArrayList items
	@POST("api/Containers")
	Call<List<Containerbean>> createItem(@Body List<Containerbean> itembean);

	
	//PUT more than one item
    @PUT("api/Containers")
	Call<List<Containerbean>> updateItem(@Body List<Containerbean> itembean);

	//Delete no work
	@DELETE("api/Containers/{seq}")
	Call<Containerbean> deleteItem(@Path("seq") Integer seq);
}