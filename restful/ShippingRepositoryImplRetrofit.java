package spirit.fitness.scanner.restful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.restful.callback.InventoryCallback;
import spirit.fitness.scanner.restful.callback.ShippingCallback;



public class ShippingRepositoryImplRetrofit {

	

	/*public Itembean updateItem(Itembean item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		SpiritfitResource service = retrofit.create(SpiritfitResource.class);

		return service.updateItem(item.seq, item).execute().body();
	}*/
	
	public List<Itembean> updateItem(List<Itembean> item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);

		return service.updateItem(item).execute().body();
	}

	/*public Itembean createItem(Itembean item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		SpiritfitResource service = retrofit.create(SpiritfitResource.class);
		return service.createItem(item).execute().body();

	}*/
	
	public List<Itembean> createItem(List<Itembean> items) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		return service.createItem(items).execute().body();

	}
	
	
	public List<Itembean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		Call<List<Itembean>> items = service.getAllItems();
		return items.execute().body();
	}
	
	public List<Itembean> getItemsByDate(String date) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		Call<List<Itembean>> items = service.getItemsByDate(date);
		//System.out.println(items.execute().toString());
		return items.execute().body();
	}
	
	public List<Itembean> getItemsByModel(Integer modelNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(GsonConverterFactory.create())
				.build();
		ShippingCallback service = retrofit.create(ShippingCallback.class);
		Call<List<Itembean>> items = service.getItemsByModelNo(modelNo);
		return items.execute().body();
	}

	/*public static void main(String[] args) throws Exception {
		FGRepositoryImplRetrofit fgRepository = new FGRepositoryImplRetrofit();
		
	    List<Itembean> items = new ArrayList<>();
		
		
		for(int i = 1; i < 9; i++) 
		{
			Itembean item1 = new Itembean();
			
			
			item1.seq = i;
			item1.SN = "158012130800080"+String.valueOf(i);
			item1.date = "2017-12-13 16:14:02.343";
			item1.Location = "060";
			item1.ModelNo = "158012";
			items.add(item1);
		}
		
		/*Itembean item = new Itembean();
		
		
		item.seq = 1;
		item.SN = "158012130800080"+String.valueOf(2);
		item.date = "2017-12-13 16:14:02.343";
		item.Location = "111";
		item.ModelNo = "158012";
		items.add(item);*/
		//Itembean fg = fgRepository.getAllItems().get(0);
		//Itembean fg = fgRepository.getItemsByModel(Integer.valueOf("158012")).get(0);
		
		//Itembean fg = fgRepository.getItemsByLocation(Integer.valueOf("025")).get(0);
		//String fg = fgRepository.createItem(items).get(0).SN;
		//Itembean fg = fgRepository.updateItem(item);
		
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

	public Itembean findItemBySeq(Integer seq) {
		return null;
	}

}
