package nc.impl.tg;

import nc.impl.pub.ace.AceTartingBillPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.itf.tg.ITartingBillMaintain;
import nc.vo.pub.BusinessException;

public class TartingBillMaintainImpl extends AceTartingBillPubServiceImpl
		implements ITartingBillMaintain {

	@Override
	public void delete(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggTartingBillVO[] insert(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggTartingBillVO[] update(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggTartingBillVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggTartingBillVO[] save(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTartingBillVO[] unsave(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTartingBillVO[] approve(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTartingBillVO[] unapprove(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
