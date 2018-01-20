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
import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Modelbean;
import spirit.fitness.scanner.restful.callback.InventoryCallback;
import spirit.fitness.scanner.restful.callback.ModelCallback;
import spirit.fitness.scanner.restful.callback.OrderCallback;
import spirit.fitness.scanner.restful.listener.CustOrderCallBackFunction;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;



public class OrdersRepositoryImplRetrofit {

	// retrieve return code number
	private CustOrderCallBackFunction custOrderCallBackFunction;

	public void setCustOrderServiceCallBackFunction(CustOrderCallBackFunction _custOrderCallBackFunction) {
		custOrderCallBackFunction = _custOrderCallBackFunction;

	}

	public List<CustOrderbean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		OrderCallback service = retrofit.create(OrderCallback.class);
		Call<List<CustOrderbean>> items = service.getAllOrders();
		return items.execute().body();
	}
	
	
	
	public List<CustOrderbean> getItemsBySalesOrderNo(String salesOrderNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		OrderCallback service = retrofit.create(OrderCallback.class);
		
		Response<List<CustOrderbean>> request = service.getItemsBySalesOrder(salesOrderNo).execute();
		int code = request.code();
		
		List<CustOrderbean> result = retriveCode(code,request);
		return result;
	}

	public List<CustOrderbean> updateItem(List<CustOrderbean> item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		OrderCallback service = retrofit.create(OrderCallback.class);

		Response<List<CustOrderbean>> request = service.updateItem(item).execute();
		int code = request.code();
		
		List<CustOrderbean> result = retriveCode(code,request);
		return result;
	}
	/*public static void main(String[] args) throws Exception {
		OrdersRepositoryImplRetrofit orderRepository = new OrdersRepositoryImplRetrofit();
		
	    
		List<Orderbean> fg = orderRepository.getItemsBySalesOrderNo("P72673");
		//Itembean fg = fgRepository.getItemsByModel(Integer.valueOf("158012")).get(0);
		
		//Itembean fg = fgRepository.getItemsByLocation(Integer.valueOf("025")).get(0);
		//String fg = fgRepository.createItem(items).get(0).SN;
		//Itembean fg = fgRepository.updateItem(item);
		
		//fgRepository.deleteItem(7);
		System.out.println(fg.get(0).MODELNO);
		
		
		
		// bookRepository.deleteBook(book.getId());
	}*/

	private List<CustOrderbean> retriveCode(int code, Response<List<CustOrderbean>> request) {
		List<CustOrderbean> resultData = null;

		if (custOrderCallBackFunction != null) {
			custOrderCallBackFunction.resultCode(code);
			if (code == HttpRequestCode.HTTP_REQUEST_OK) 
			{
				resultData = request.body();
				custOrderCallBackFunction.updateSalesOrder(resultData);
			}
		}
		return resultData;
	}
}
