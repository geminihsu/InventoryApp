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
import spirit.fitness.scanner.model.Locationbean;
import spirit.fitness.scanner.model.Modelbean;

/**
 * Created by User on 5/1/2017.
 */

public interface LocationCallback {
    
	@Headers("Content-Type: application/json")
	@GET("FGService/api/Locations")
	Call<List<Locationbean>> getAllLocations();
	
	@GET("FGService/api/Locations/{Code}")
	Call<List<Locationbean>> getLocationsByCode(@Path("Code") Integer code);
	//Post one item
	/*@POST("/api/FGInventory")
	Call<Itembean> createItem(@Body Itembean itembean);*/
	
	

}
