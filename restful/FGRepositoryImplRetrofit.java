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

public class FGRepositoryImplRetrofit {

	/*
	 * public Itembean updateItem(Itembean item) throws Exception { Retrofit
	 * retrofit = new
	 * Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(
	 * GsonConverterFactory.create()) .build(); SpiritfitResource service =
	 * retrofit.create(SpiritfitResource.class);
	 * 
	 * return service.updateItem(item.seq, item).execute().body(); }
	 */

	public List<Itembean> updateItem(List<Itembean> item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		InventoryCallback service = retrofit.create(InventoryCallback.class);

		return service.updateItem(item).execute().body();
	}

	/*
	 * public Itembean createItem(Itembean item) throws Exception { Retrofit
	 * retrofit = new
	 * Retrofit.Builder().baseUrl(Constrant.webUrl).addConverterFactory(
	 * GsonConverterFactory.create()) .build(); SpiritfitResource service =
	 * retrofit.create(SpiritfitResource.class); return
	 * service.createItem(item).execute().body();
	 * 
	 * }
	 */

	public List<Itembean> createItem(List<Itembean> items) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		InventoryCallback service = retrofit.create(InventoryCallback.class);
		return service.createItem(items).execute().body();

	}

	public List<Itembean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		InventoryCallback service = retrofit.create(InventoryCallback.class);
		Call<List<Itembean>> items = service.getAllItems();
		return items.execute().body();
	}

	public List<Itembean> getItemsByLocation(Integer location) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		InventoryCallback service = retrofit.create(InventoryCallback.class);
		Call<List<Itembean>> items = service.getItemsByLocation(location);
		// System.out.println(items.execute().toString());
		return items.execute().body();
	}

	public List<Itembean> getItemsByModel(Integer modelNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		InventoryCallback service = retrofit.create(InventoryCallback.class);
		Call<List<Itembean>> items = service.getItemsByModelNo(modelNo);
		return items.execute().body();
	}

	public static void main(String[] args) throws Exception {
		FGRepositoryImplRetrofit fgRepository = new FGRepositoryImplRetrofit();

		List<Itembean> items = new ArrayList<>();

		Itembean item1 = new Itembean();

		item1.seq = 289;
		item1.SN = "1580121711003849";
		item1.date = "2018-01-08 14:56:58.000";
		item1.Location = "721";
		item1.ModelNo = "158012";
		items.add(item1);

		Itembean item2 = new Itembean();

		item2.seq = 288;
		item2.SN = "1580121711003848";
		item2.date = "2018-01-08 14:56:58.000";
		item2.Location = "721";
		item2.ModelNo = "158012";
		items.add(item2);

		/*
		 * Itembean item = new Itembean();
		 * 
		 * 
		 * item.seq = 1; item.SN = "158012130800080"+String.valueOf(2); item.date =
		 * "2017-12-13 16:14:02.343"; item.Location = "111"; item.ModelNo = "158012";
		 * items.add(item);
		 */
		// Itembean fg = fgRepository.getAllItems().get(0);
		// Itembean fg = fgRepository.getItemsByModel(Integer.valueOf("158012")).get(0);

		// Itembean fg = fgRepository.getItemsByLocation(Integer.valueOf("025")).get(0);
		// String fg = fgRepository.createItem(items).get(0).SN;
		// Itembean fg = fgRepository.updateItem(item);

		 fgRepository.deleteItem(items);
		 //System.out.println(fg);

		// bookRepository.deleteBook(book.getId());
	}

	public List<Itembean> deleteItem(List<Itembean> items) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		InventoryCallback service = retrofit.create(InventoryCallback.class);
		Call<List<Itembean>> data = service.deleteItem(items);
		return data.execute().body();
	}

	public Itembean findItemBySeq(Integer seq) {
		return null;
	}

}
