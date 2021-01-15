package nc.impl.tg;

import nc.impl.pub.ace.AceAbnormalTaxTransferPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.itf.tg.IAbnormalTaxTransferMaintain;
import nc.vo.pub.BusinessException;

public class AbnormalTaxTransferMaintainImpl extends AceAbnormalTaxTransferPubServiceImpl
		implements IAbnormalTaxTransferMaintain {

	@Override
	public void delete(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggAbTaxTransferHVO[] insert(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggAbTaxTransferHVO[] update(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggAbTaxTransferHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggAbTaxTransferHVO[] save(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAbTaxTransferHVO[] unsave(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAbTaxTransferHVO[] approve(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAbTaxTransferHVO[] unapprove(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
