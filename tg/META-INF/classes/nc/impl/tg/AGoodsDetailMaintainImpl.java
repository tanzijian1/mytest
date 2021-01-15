package nc.impl.tg;

import nc.impl.pub.ace.AceAGoodsDetailPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.itf.tg.IAGoodsDetailMaintain;
import nc.vo.pub.BusinessException;

public class AGoodsDetailMaintainImpl extends AceAGoodsDetailPubServiceImpl
		implements IAGoodsDetailMaintain {

	@Override
	public void delete(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggAGoodsDetail[] insert(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggAGoodsDetail[] update(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggAGoodsDetail[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggAGoodsDetail[] save(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAGoodsDetail[] unsave(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAGoodsDetail[] approve(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAGoodsDetail[] unapprove(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
