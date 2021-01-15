package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.vo.pub.BusinessException;

public interface IAbnormalTaxTransferMaintain {

	public void delete(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException;

	public AggAbTaxTransferHVO[] insert(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException;

	public AggAbTaxTransferHVO[] update(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException;

	public AggAbTaxTransferHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggAbTaxTransferHVO[] save(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException;

	public AggAbTaxTransferHVO[] unsave(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException;

	public AggAbTaxTransferHVO[] approve(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException;

	public AggAbTaxTransferHVO[] unapprove(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException;
}
