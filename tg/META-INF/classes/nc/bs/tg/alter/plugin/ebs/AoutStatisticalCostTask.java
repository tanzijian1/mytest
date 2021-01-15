package nc.bs.tg.alter.plugin.ebs;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.plugin.ebs.result.EBSAlterMessage;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.vo.pub.BusinessException;

/**
 * 自动每天同步EBS成本合同统计已入成本定时任务2019-11-22
 * 
 * @author TZj
 * 
 */
public abstract class AoutStatisticalCostTask implements IBackgroundWorkPlugin {
	BaseDAO baseDAO = null;

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		String title = bgwc.getAlertTypeName();
		PreAlertObject pobj = new PreAlertObject();
		pobj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new EBSAlterMessage());

		try {
			getStatisticalResult(bgwc);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return util.getUtil().executeTask(title, e.getMessage());
		}
		return pobj;
	}

	protected abstract void getStatisticalResult(BgWorkingContext bgwc)
			throws BusinessException;

}
