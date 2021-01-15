package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.pub.BusinessException;

public interface IContractApportionmentMaintain {

	public void delete(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException;

	public AggContractAptmentVO[] insert(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException;

	public AggContractAptmentVO[] update(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException;

	public AggContractAptmentVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggContractAptmentVO[] save(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException;

	public AggContractAptmentVO[] unsave(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException;

	public AggContractAptmentVO[] approve(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException;

	public AggContractAptmentVO[] unapprove(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException;
}
