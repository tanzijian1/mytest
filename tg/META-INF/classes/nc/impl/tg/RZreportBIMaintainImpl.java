package nc.impl.tg;

import nc.impl.pub.ace.AceRZreportBIPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.itf.tg.IRZreportBIMaintain;
import nc.vo.pub.BusinessException;

public class RZreportBIMaintainImpl extends AceRZreportBIPubServiceImpl
		implements IRZreportBIMaintain {

	@Override
	public void delete(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggRZreportBIVO[] insert(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggRZreportBIVO[] update(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggRZreportBIVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggRZreportBIVO[] save(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggRZreportBIVO[] unsave(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggRZreportBIVO[] approve(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggRZreportBIVO[] unapprove(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
