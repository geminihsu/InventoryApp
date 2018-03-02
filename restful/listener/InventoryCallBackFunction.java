package spirit.fitness.scanner.restful.listener;

import java.util.List;

import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.Itembean;


public interface InventoryCallBackFunction {
	public void resultCode(int code);
	public void getInventoryItems(List<Itembean> items);
	public void checkInventoryItems(List<Itembean> items);
	public void checkInventoryZone2Items(int resultCode,List<Itembean> items);
}