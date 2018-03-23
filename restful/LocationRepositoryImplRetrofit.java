package spirit.fitness.scanner.restful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.common.HttpRequestCode;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Locationbean;
import spirit.fitness.scanner.model.Modelbean;
import spirit.fitness.scanner.restful.callback.InventoryCallback;
import spirit.fitness.scanner.restful.callback.LocationCallback;
import spirit.fitness.scanner.restful.callback.ModelCallback;
import spirit.fitness.scanner.restful.listener.LocationCallBackFunction;
import spirit.fitness.scanner.restful.listener.ModelsCallBackFunction;



public class LocationRepositoryImplRetrofit {

	// retrieve return code number
	private LocationCallBackFunction locationCallBackFunction;

	public void setinventoryServiceCallBackFunction(LocationCallBackFunction _locationCallBackFunction) {
		locationCallBackFunction = _locationCallBackFunction;

	}
	
	public List<Locationbean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		LocationCallback service = retrofit.create(LocationCallback.class);
		
		Response<List<Locationbean>> request = service.getAllLocations().execute();
		int code = request.code();
		List<Locationbean> result = retriveCode(code,request);
		
		return result;
	}
	
	
	
	public List<Locationbean> getItemsByModel(Integer code) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		LocationCallback service = retrofit.create(LocationCallback.class);
		
		Response<List<Locationbean>> request = service.getLocationsByCode(code).execute();
		int codeNo = request.code();
		List<Locationbean> result = retriveCode(codeNo,request);
		
		return result;
	}

	public static void main(String[] args) throws Exception {
		LocationRepositoryImplRetrofit locationRepository = new LocationRepositoryImplRetrofit();
		
	    
		List<Locationbean> fg = locationRepository.getAllItems();
		//Itembean fg = fgRepository.getItemsByModel(Integer.valueOf("158012")).get(0);
		
		//Itembean fg = fgRepository.getItemsByLocation(Integer.valueOf("025")).get(0);
		//String fg = fgRepository.createItem(items).get(0).SN;
		//Itembean fg = fgRepository.updateItem(item);
		
		//fgRepository.deleteItem(7);
		System.out.println(fg.get(100).ZoneCode);
		
		
		// bookRepository.deleteBook(book.getId());
	}

	private List<Locationbean> retriveCode(int code, Response<List<Locationbean>> request) {
		List<Locationbean> resultData = null;

		if (locationCallBackFunction != null) {
			locationCallBackFunction.resultCode(code);
			if (code == HttpRequestCode.HTTP_REQUEST_OK) {
				resultData = request.body();
				locationCallBackFunction.getLocationItems(resultData);
			}
			
		}
		return resultData;
	}
	
}
