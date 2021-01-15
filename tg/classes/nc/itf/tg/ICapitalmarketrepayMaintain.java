package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.pub.BusinessException;

public interface ICapitalmarketrepayMaintain {

	public void delete(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException;

	public AggMarketRepalayVO[] insert(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException;

	public AggMarketRepalayVO[] update(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException;

	public AggMarketRepalayVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggMarketRepalayVO[] save(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException;

	public AggMarketRepalayVO[] unsave(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException;

	public AggMarketRepalayVO[] approve(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException;

	public AggMarketRepalayVO[] unapprove(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException;
}
