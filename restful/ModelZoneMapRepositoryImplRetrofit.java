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
import spirit.fitness.scanner.model.ModelDailyReportbean;
import spirit.fitness.scanner.model.ModelZone2bean;
import spirit.fitness.scanner.model.Modelbean;
import spirit.fitness.scanner.restful.callback.InventoryCallback;
import spirit.fitness.scanner.restful.callback.ModelCallback;
import spirit.fitness.scanner.restful.callback.ModelZoneMapCallback;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;
import spirit.fitness.scanner.restful.listener.ModelZone2CallBackFunction;




public class ModelZoneMapRepositoryImplRetrofit {

	// retrieve return code number
	private ModelZone2CallBackFunction reportCallBackFunction;

	public void setinventoryServiceCallBackFunction(ModelZone2CallBackFunction _modelZoneMapCallback) {
		reportCallBackFunction = _modelZoneMapCallback;

	}

	public List<ModelZone2bean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ModelZoneMapCallback service = retrofit.create(ModelZoneMapCallback.class);
		
		Response<List<ModelZone2bean>> request = service.getAllModelMapZone2().execute();
		int code = request.code();
		List<ModelZone2bean> result = retriveCode(code,request);
		
		return result;
	}

	
	public List<ModelZone2bean> getAllItemsQty() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ModelZoneMapCallback service = retrofit.create(ModelZoneMapCallback.class);
		
		Response<List<ModelZone2bean>> request = service.getAllModelQtyReport().execute();
		int code = request.code();
		List<ModelZone2bean> result = retriveCode(code,request);
		
		return result;
	}

	//Retrieve Daily Report
	public List<ModelDailyReportbean> getAllItems(String date) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ModelZoneMapCallback service = retrofit.create(ModelZoneMapCallback.class);
		
		Response<List<ModelDailyReportbean>> request = service.getAllModelDailyReport(date).execute();
		int code = request.code();
		List<ModelDailyReportbean> result = retriveResponseCode(code,request);
		
		return result;
	}
	
	
	public List<ModelZone2bean> getItemsByModel(Integer modelNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ModelZoneMapCallback service = retrofit.create(ModelZoneMapCallback.class);
		
		Response<List<ModelZone2bean>> request = service.getItemsByModelNo(modelNo).execute();
		int code = request.code();
		List<ModelZone2bean> result = retriveCode(code,request);
		
		return result;
	}

	/*public static void main(String[] args) throws Exception {
		ReportRepositoryImplRetrofit modelRepository = new ReportRepositoryImplRetrofit();
		
	    
		Reportbean fg = modelRepository.getAllItems().get(0);
		//Itembean fg = fgRepository.getItemsByModel(Integer.valueOf("158012")).get(0);
		
		//Itembean fg = fgRepository.getItemsByLocation(Integer.valueOf("025")).get(0);
		//String fg = fgRepository.createItem(items).get(0).SN;
		//Itembean fg = fgRepository.updateItem(item);
		
		//fgRepository.deleteItem(7);
		System.out.println(fg.Model);
		
		
		// bookRepository.deleteBook(book.getId());
	}*/

	private List<ModelZone2bean> retriveCode(int code, Response<List<ModelZone2bean>> request) {
		List<ModelZone2bean> resultData = null;

		if (reportCallBackFunction != null) {
			reportCallBackFunction.resultCode(code);
			if (code == HttpRequestCode.HTTP_REQUEST_OK) {
				resultData = request.body();
				reportCallBackFunction.getReportItems(resultData);
			}
			
		}
		return resultData;
	}
	
	private List<ModelDailyReportbean> retriveResponseCode(int code, Response<List<ModelDailyReportbean>> request) {
		List<ModelDailyReportbean> resultData = null;

		if (reportCallBackFunction != null) {
			reportCallBackFunction.resultCode(code);
			if (code == HttpRequestCode.HTTP_REQUEST_OK) {
				resultData = request.body();
				reportCallBackFunction.getModelDailyReportItems(resultData);
			}
			
		}
		return resultData;
	}
}
