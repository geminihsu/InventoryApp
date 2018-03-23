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
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.restful.callback.HistoryCallback;
import spirit.fitness.scanner.restful.callback.InventoryCallback;
import spirit.fitness.scanner.restful.listener.HistoryCallBackFunction;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;



public class HistoryRepositoryImplRetrofit {

	
	// retrieve return code number
	private HistoryCallBackFunction historyServiceCallBackFunction;

	public void setHistoryServiceCallBackFunction(HistoryCallBackFunction _historyServiceCallBackFunction) {
		historyServiceCallBackFunction = _historyServiceCallBackFunction;

	}
	/*public Shippingbean updateItem(Shippingbean item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		SpiritfitResource service = retrofit.create(SpiritfitResource.class);

		return service.updateItem(item.seq, item).execute().body();
	}*/
	
	
	/*public Shippingbean createItem(Shippingbean item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		SpiritfitResource service = retrofit.create(SpiritfitResource.class);
		return service.createItem(item).execute().body();

	}*/
	
	public List<Historybean> createItem(List<Historybean> items) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		HistoryCallback service = retrofit.create(HistoryCallback.class);
		Response<List<Historybean>> request = service.createItem(items).execute();
		int code = request.code();
		
		List<Historybean> result = retriveCode(code,request);
		
		return result;

	}
	
	
	public List<Historybean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		HistoryCallback service = retrofit.create(HistoryCallback.class);
		Call<List<Historybean>> items = service.getAllItems();
		return items.execute().body();
	}
	
	public List<Historybean> getItemsByDate(String date) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		HistoryCallback service = retrofit.create(HistoryCallback.class);
		Call<List<Historybean>> items = service.getItemsByDate(date);
		//System.out.println(items.execute().toString());
		return items.execute().body();
	}
	
	public List<Historybean> getItemsByModel(Integer modelNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		HistoryCallback service = retrofit.create(HistoryCallback.class);
		Response<List<Historybean>> request = service.getItemsByModelNo(modelNo).execute();
		int code = request.code();
		
		List<Historybean> result = retriveCode(code,request);
		
		return result;
	}
	
	public List<Historybean> getItemsBySalesOrder(String salesOrder) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		HistoryCallback service = retrofit.create(HistoryCallback.class);

		Response<List<Historybean>> request = service.getItemsBySalesOrder(salesOrder).execute();
		int code = request.code();
		
		List<Historybean> resultData = null;
		
		if(code == HttpRequestCode.HTTP_REQUEST_OK)
			resultData = request.body();
		
		if (historyServiceCallBackFunction != null) 
			historyServiceCallBackFunction.checkHistoryItemsBySalesOrder(resultData);
		
		return resultData;
	}

	/*public static void main(String[] args) throws Exception {
		HistoryRepositoryImplRetrofit fgRepository = new HistoryRepositoryImplRetrofit();
		
	    List<Shippingbean> items = new ArrayList<>();
		
		
		for(int i = 1; i < 9; i++) 
		{
			Shippingbean item1 = new Shippingbean();
			
			
			item1.seq = i;
			item1.SN = "158012130800080"+String.valueOf(i);
			item1.date = "2017-12-13 16:14:02.343";
			item1.Location = "060";
			item1.ModelNo = "158012";
			items.add(item1);
		}
		
		/*Shippingbean item = new Shippingbean();
		
		
		item.seq = 1;
		item.SN = "158012130800080"+String.valueOf(2);
		item.date = "2017-12-13 16:14:02.343";
		item.Location = "111";
		item.ModelNo = "158012";
		items.add(item);*/
	//	Historybean fg = fgRepository.getAllItems().get(0);
		//Shippingbean fg = fgRepository.getItemsByModel(Integer.valueOf("158012")).get(0);
		
		//Shippingbean fg = fgRepository.getItemsByLocation(Integer.valueOf("025")).get(0);
		//String fg = fgRepository.createItem(items).get(0).SN;
		//Shippingbean fg = fgRepository.updateItem(item);
		
		//fgRepository.deleteItem(7);
	//	System.out.println(fg);
		
		
		// bookRepository.deleteBook(book.getId());
	//}

	public void deleteItem(Integer seq) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		HistoryCallback service = retrofit.create(HistoryCallback.class);
		service.deleteItem(seq);
	}

	

	private List<Historybean> retriveCode(int code, Response<List<Historybean>> request) {
		List<Historybean> resultData = null;

		if (historyServiceCallBackFunction != null) {
			historyServiceCallBackFunction.resultCode(code);
			if (code == HttpRequestCode.HTTP_REQUEST_OK) {
				resultData = request.body();
				historyServiceCallBackFunction.getHistoryItems(resultData);
			}
		}
		return resultData;
	}
}
