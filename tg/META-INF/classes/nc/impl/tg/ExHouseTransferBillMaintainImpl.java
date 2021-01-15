package nc.impl.tg;

import nc.impl.pub.ace.AceExHouseTransferBillPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.itf.tg.IExHouseTransferBillMaintain;
import nc.vo.pub.BusinessException;

public class ExHouseTransferBillMaintainImpl extends AceExHouseTransferBillPubServiceImpl
		implements IExHouseTransferBillMaintain {

	@Override
	public void delete(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggExhousetransferbillHVO[] insert(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggExhousetransferbillHVO[] update(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggExhousetransferbillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggExhousetransferbillHVO[] save(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggExhousetransferbillHVO[] unsave(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggExhousetransferbillHVO[] approve(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggExhousetransferbillHVO[] unapprove(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
