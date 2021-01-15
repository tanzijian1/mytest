package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.vo.pub.BusinessException;

public interface IMasterdataMaintain {

	public void delete(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException;

	public AggMasterDataVO[] insert(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException;

	public AggMasterDataVO[] update(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException;

	public AggMasterDataVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggMasterDataVO[] save(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException;

	public AggMasterDataVO[] unsave(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException;

	public AggMasterDataVO[] approve(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException;

	public AggMasterDataVO[] unapprove(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException;
}
