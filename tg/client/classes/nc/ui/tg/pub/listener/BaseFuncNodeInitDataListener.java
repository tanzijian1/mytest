package nc.ui.tg.pub.listener;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.tg.pub.model.BatchModelDataManager;

public class BaseFuncNodeInitDataListener extends
		nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener {
	BatchModelDataManager dataManager;

	public void initData(FuncletInitData data) {
		if (data == null) {
			getDataManager().initModel();
		} else {
			super.initData(data);
		}

	}

	public BatchModelDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(
			BatchModelDataManager dataManager) {
		this.dataManager = dataManager;
	}

}
