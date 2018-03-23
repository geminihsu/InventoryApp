package spirit.fitness.scanner.restful.listener;

import java.util.List;

import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Locationbean;



public interface LocationCallBackFunction {
	public void resultCode(int code);
	public void getLocationItems(List<Locationbean> items);
}