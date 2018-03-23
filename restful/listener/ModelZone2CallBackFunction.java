package spirit.fitness.scanner.restful.listener;

import java.util.List;

import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.ModelDailyReportbean;
import spirit.fitness.scanner.model.ModelZone2bean;


public interface ModelZone2CallBackFunction {
	public void resultCode(int code);
	public void getReportItems(List<ModelZone2bean> items);
	public void getModelDailyReportItems(List<ModelDailyReportbean> items);
}