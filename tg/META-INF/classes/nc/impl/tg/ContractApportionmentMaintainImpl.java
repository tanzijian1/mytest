package nc.impl.tg;

import nc.impl.pub.ace.AceContractApportionmentPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.itf.tg.IContractApportionmentMaintain;
import nc.vo.pub.BusinessException;

public class ContractApportionmentMaintainImpl extends AceContractApportionmentPubServiceImpl
		implements IContractApportionmentMaintain {

	@Override
	public void delete(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggContractAptmentVO[] insert(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggContractAptmentVO[] update(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggContractAptmentVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggContractAptmentVO[] save(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggContractAptmentVO[] unsave(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggContractAptmentVO[] approve(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggContractAptmentVO[] unapprove(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
