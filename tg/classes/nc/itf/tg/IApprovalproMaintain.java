package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.pub.BusinessException;

public interface IApprovalproMaintain {

	public void delete(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException;

	public AggApprovalProVO[] insert(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException;

	public AggApprovalProVO[] update(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException;

	public AggApprovalProVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggApprovalProVO[] save(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException;

	public AggApprovalProVO[] unsave(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException;

	public AggApprovalProVO[] approve(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException;

	public AggApprovalProVO[] unapprove(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException;
}
