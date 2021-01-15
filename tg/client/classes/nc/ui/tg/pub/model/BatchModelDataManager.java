package nc.ui.tg.pub.model;

import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pubapp.AppContext;

public class BatchModelDataManager extends
		nc.ui.pubapp.uif2app.model.BatchModelDataManager {
	boolean isShowSealDataFlag = false;

	@Override
	public void initModel() {
		try {
			String condition = "";
			if (!isShowSealDataFlag) {
				condition = " and enablestate = 2 ";
			}
			String pk_group = AppContext.getInstance().getPkGroup();
			initModelBySqlWhere("and isnull(dr,0)=0 and pk_group = '"
					+ pk_group + "' " + condition);

		} catch (Exception e) {
			throw new BusinessRuntimeException("", e);
		}
	}

	@Override
	public void setShowSealDataFlag(boolean showSealDataFlag) {
		isShowSealDataFlag = showSealDataFlag;
		initModel();
	}

	public void refresh() {
		initModelBySqlWhere(getSqlWhere());
	}

}
