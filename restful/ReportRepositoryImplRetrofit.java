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
import spirit.fitness.scanner.model.Reportbean;
import spirit.fitness.scanner.restful.callback.InventoryCallback;
import spirit.fitness.scanner.restful.callback.ModelCallback;
import spirit.fitness.scanner.restful.callback.ReportCallback;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;
import spirit.fitness.scanner.restful.listener.ReportCallBackFunction;



public class ReportRepositoryImplRetrofit {

	// retrieve return code number
	private ReportCallBackFunction reportCallBackFunction;

	public void setinventoryServiceCallBackFunction(ReportCallBackFunction _reportCallBackFunction) {
		reportCallBackFunction = _reportCallBackFunction;

	}

	public List<Reportbean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ReportCallback service = retrofit.create(ReportCallback.class);
		
		Response<List<Reportbean>> request = service.getAllReport().execute();
		int code = request.code();
		List<Reportbean> result = retriveCode(code,request);
		
		return result;
	}

	
	
	
	public List<Reportbean> getItemsByModel(Integer modelNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ReportCallback service = retrofit.create(ReportCallback.class);
		
		Response<List<Reportbean>> request = service.getItemsByModelNo(modelNo).execute();
		int code = request.code();
		List<Reportbean> result = retriveCode(code,request);
		
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

	private List<Reportbean> retriveCode(int code, Response<List<Reportbean>> request) {
		List<Reportbean> resultData = null;

		if (reportCallBackFunction != null) {
			reportCallBackFunction.resultCode(code);
			if (code == HttpRequestCode.HTTP_REQUEST_OK) {
				resultData = request.body();
				reportCallBackFunction.getReportItems(resultData);
			}
			
		}
		return resultData;
	}
}
