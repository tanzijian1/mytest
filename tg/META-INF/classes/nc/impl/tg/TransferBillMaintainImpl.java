package nc.impl.tg;

import nc.impl.pub.ace.AceTransferBillPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.itf.tg.ITransferBillMaintain;
import nc.vo.pub.BusinessException;

public class TransferBillMaintainImpl extends AceTransferBillPubServiceImpl
		implements ITransferBillMaintain {

	@Override
	public void delete(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggTransferBillHVO[] insert(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggTransferBillHVO[] update(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggTransferBillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggTransferBillHVO[] save(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTransferBillHVO[] unsave(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTransferBillHVO[] approve(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTransferBillHVO[] unapprove(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
