package spirit.fitness.scanner.restful.listener;

import java.util.List;

import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.Itembean;


public interface CustOrderCallBackFunction {
	public void resultCode(int code);
	public void updateSalesOrder(List<CustOrderbean> items);
}