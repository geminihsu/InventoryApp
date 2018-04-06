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
import spirit.fitness.scanner.model.Modelbean;

/**
 * Created by User on 5/1/2017.
 */

public interface ModelCallback {
    
	@Headers("Content-Type: application/json")
	@GET("FGService/api/ModelInfoes")
	Call<List<Modelbean>> getAllModels();
	
	@GET("FGService/api/ModelInfoes/{modelNo}")
	Call<List<Modelbean>> getItemsByModelNo(@Path("modelNo") String modelNo);
	//Post one item
	/*@POST("/api/FGInventory")
	Call<Itembean> createItem(@Body Itembean itembean);*/
	
	

}
