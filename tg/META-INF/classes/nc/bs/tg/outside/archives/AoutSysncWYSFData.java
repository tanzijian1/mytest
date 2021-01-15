package nc.bs.tg.outside.archives;

import java.util.List;

import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.vo.pub.BusinessException;

public abstract class AoutSysncWYSFData implements IBackgroundWorkPlugin {

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		String title = bgwc.getAlertTypeName();
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new WYSFAlterMessage());
		List<Object[]> reflist;
		try {
			reflist = getWorkResult(bgwc);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return util.getUtil().executeTask(title, e.getMessage());
		}
		return util.executeTask(title, reflist);

	}

	protected abstract List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException;

}
