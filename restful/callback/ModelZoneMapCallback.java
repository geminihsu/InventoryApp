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
import spirit.fitness.scanner.model.ModelDailyReportbean;
import spirit.fitness.scanner.model.ModelZone2bean;
import spirit.fitness.scanner.model.Modelbean;

/**
 * Created by User on 5/1/2017.
 */

public interface ModelZoneMapCallback {
    
	@Headers("Content-Type: application/json")

	@GET("api/ModelZoneMaps")
	Call<List<ModelZone2bean>> getAllModelMapZone2();
	
	@GET("api/ModelZoneMaps/Qty")
	Call<List<ModelZone2bean>> getAllModelQtyReport();
	
	@GET("api/ModelZoneMaps/DailyReport/date/{date}")
	Call<List<ModelDailyReportbean>> getAllModelDailyReport(@Path("date") String date);
	
	@GET("api/ModelZoneMaps/{modelNo}")
	Call<List<ModelZone2bean>> getItemsByModelNo(@Path("modelNo") Integer modelNo);
	//Post one item
	/*@POST("/api/FGInventory")
	Call<Itembean> createItem(@Body Itembean itembean);*/
	
	

}
