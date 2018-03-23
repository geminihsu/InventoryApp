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
import spirit.fitness.scanner.model.Modelbean;
import spirit.fitness.scanner.restful.callback.InventoryCallback;
import spirit.fitness.scanner.restful.callback.ModelCallback;
import spirit.fitness.scanner.restful.listener.ModelsCallBackFunction;




public class ModelRepositoryImplRetrofit {

	// retrieve return code number
	private ModelsCallBackFunction modelsCallBackFunction;

	public void setinventoryServiceCallBackFunction(ModelsCallBackFunction _modelsCallBackFunction) {
		modelsCallBackFunction = _modelsCallBackFunction;

	}
	
	public List<Modelbean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ModelCallback service = retrofit.create(ModelCallback.class);
		
		Response<List<Modelbean>> request = service.getAllModels().execute();
		int code = request.code();
		List<Modelbean> result = retriveCode(code,request);
		
		return result;
	}
	
	
	
	public List<Modelbean> getItemsByModel(String modelNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ModelCallback service = retrofit.create(ModelCallback.class);
		
		Response<List<Modelbean>> request = service.getItemsByModelNo(modelNo).execute();
		int code = request.code();
		List<Modelbean> result = retriveCode(code,request);
		
		return result;
	}

	public static void main(String[] args) throws Exception {
		ModelRepositoryImplRetrofit modelRepository = new ModelRepositoryImplRetrofit();
		
	    
		Modelbean fg = modelRepository.getAllItems().get(0);
		//Itembean fg = fgRepository.getItemsByModel(Integer.valueOf("158012")).get(0);
		
		//Itembean fg = fgRepository.getItemsByLocation(Integer.valueOf("025")).get(0);
		//String fg = fgRepository.createItem(items).get(0).SN;
		//Itembean fg = fgRepository.updateItem(item);
		
		//fgRepository.deleteItem(7);
		System.out.println(fg.ModelNo);
		
		
		// bookRepository.deleteBook(book.getId());
	}

	private List<Modelbean> retriveCode(int code, Response<List<Modelbean>> request) {
		List<Modelbean> resultData = null;

		if (modelsCallBackFunction != null) {
			modelsCallBackFunction.resultCode(code);
			if (code == HttpRequestCode.HTTP_REQUEST_OK) {
				resultData = request.body();
				modelsCallBackFunction.getModelsItems(resultData);
			}
			
		}
		return resultData;
	}
}
