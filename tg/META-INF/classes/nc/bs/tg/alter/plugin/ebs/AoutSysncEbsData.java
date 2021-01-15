package nc.bs.tg.alter.plugin.ebs;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.plugin.ebs.result.EBSAlterMessage;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.itf.tg.IProjectdataMaintain;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pub.BusinessException;

/**
 * NC调用EBS接口交互处理任务
 * 
 * @author ASUS
 * 
 */
public abstract class AoutSysncEbsData implements IBackgroundWorkPlugin {
	BaseDAO baseDAO = null;
	IMDPersistenceQueryService queryServcie = null;
	IProjectdataMaintain maintain = null;
	ISyncEBSServcie ebsService = null;

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		String title = bgwc.getAlertTypeName();
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new EBSAlterMessage());
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

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public IMDPersistenceQueryService getQueryServcie() {
		if (queryServcie == null) {
			queryServcie = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return queryServcie;
	}

	public IProjectdataMaintain getMaintain() {
		if (maintain == null) {
			maintain = NCLocator.getInstance().lookup(
					IProjectdataMaintain.class);
		}
		return maintain;
	}

	public ISyncEBSServcie getEBSServcie() {
		if (ebsService == null) {
			ebsService = NCLocator.getInstance().lookup(ISyncEBSServcie.class);
		}
		return ebsService;
	}
}
