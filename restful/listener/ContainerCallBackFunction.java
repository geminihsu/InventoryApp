package spirit.fitness.scanner.restful.listener;

import java.util.List;

import spirit.fitness.scanner.model.Containerbean;
import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.Itembean;


public interface ContainerCallBackFunction {
	public void resultCode(int code);
	public void addContainerInfo(List<Containerbean> items);
	public void getContainerItems(List<Containerbean> items);
}