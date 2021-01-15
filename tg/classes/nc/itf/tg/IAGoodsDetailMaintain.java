package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.vo.pub.BusinessException;

public interface IAGoodsDetailMaintain {

	public void delete(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException;

	public AggAGoodsDetail[] insert(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException;

	public AggAGoodsDetail[] update(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException;

	public AggAGoodsDetail[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggAGoodsDetail[] save(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException;

	public AggAGoodsDetail[] unsave(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException;

	public AggAGoodsDetail[] approve(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException;

	public AggAGoodsDetail[] unapprove(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException;
}
