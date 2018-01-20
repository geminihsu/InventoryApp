package spirit.fitness.scanner.restful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.model.Shippingbean;
import spirit.fitness.scanner.restful.callback.InventoryCallback;
import spirit.fitness.scanner.restful.callback.ShippingCallback;



public class ShippingRepositoryImplRetrofit {

	

	/*public Shippingbean updateItem(Shippingbean item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		SpiritfitResource service = retrofit.create(SpiritfitResource.class);

		return service.updateItem(item.seq, item).execute().body();
	}*/
	
	public List<Shippingbean> updateItem(List<Shippingbean> item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);

		return service.updateItem(item).execute().body();
	}

	/*public Shippingbean createItem(Shippingbean item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		SpiritfitResource service = retrofit.create(SpiritfitResource.class);
		return service.createItem(item).execute().body();

	}*/
	
	public List<Shippingbean> createItem(List<Shippingbean> items) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		return service.createItem(items).execute().body();

	}
	
	
	public List<Shippingbean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		Call<List<Shippingbean>> items = service.getAllItems();
		return items.execute().body();
	}
	
	public List<Shippingbean> getItemsByDate(String date) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		Call<List<Shippingbean>> items = service.getItemsByDate(date);
		//System.out.println(items.execute().toString());
		return items.execute().body();
	}
	
	public List<Shippingbean> getItemsByModel(Integer modelNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		Call<List<Shippingbean>> items = service.getItemsByModelNo(modelNo);
		return items.execute().body();
	}
	
	public List<Shippingbean> getItemsBySalesOrder(String salesOrder) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		Call<List<Shippingbean>> items = service.getItemsBySalesOrder(salesOrder);
		return items.execute().body();
	}

	/*public static void main(String[] args) throws Exception {
		FGRepositoryImplRetrofit fgRepository = new FGRepositoryImplRetrofit();
		
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
		//Shippingbean fg = fgRepository.getAllItems().get(0);
		//Shippingbean fg = fgRepository.getItemsByModel(Integer.valueOf("158012")).get(0);
		
		//Shippingbean fg = fgRepository.getItemsByLocation(Integer.valueOf("025")).get(0);
		//String fg = fgRepository.createItem(items).get(0).SN;
		//Shippingbean fg = fgRepository.updateItem(item);
		
		//fgRepository.deleteItem(7);
		//System.out.println(fg);
		
		
		// bookRepository.deleteBook(book.getId());
	//}

	public void deleteItem(Integer seq) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		service.deleteItem(seq);
	}

	public Shippingbean findItemBySeq(Integer seq) {
		return null;
	}

}
