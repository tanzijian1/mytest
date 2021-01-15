package nc.ui.tg.capitalmarketrepay.ace.action;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;

import nc.bs.framework.common.NCLocator;
import nc.itf.pubapp.pub.smart.IBillQueryService;
import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.uif2app.actions.RefreshSingleAction;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepaleyBVO;

public class CardRefreshAction extends RefreshSingleAction{

	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object obj = this.model.getSelectedData();
		if (obj != null) {
			AbstractBill oldVO = (AbstractBill) obj;
			String pk = oldVO.getParentVO().getPrimaryKey();
			IBillQueryService billQuery =
					NCLocator.getInstance().lookup(IBillQueryService.class);
			AggMarketRepalayVO newVO = (AggMarketRepalayVO) billQuery.querySingleBillByPk(oldVO.getClass(), pk);
			if (newVO == null) {
				// 数据已经被删除
				throw new BusinessException(NCLangRes.getInstance().getStrByID(
						"uif2", "RefreshSingleAction-000000")/*数据已经被删除，请返回列表界面！*/);
			}
			MarketRepaleyBVO[] vos = (MarketRepaleyBVO[]) newVO.getChildren(MarketRepaleyBVO.class);
			Arrays.sort(vos, new Comparator<MarketRepaleyBVO>() {
				@Override
				public int compare(MarketRepaleyBVO o1,
						MarketRepaleyBVO o2) {
					UFDouble v1 = o1.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(o1.getDef3());
					UFDouble v2 = o2.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(o2.getDef3());
					if(o1.getDef2().compareTo(o2.getDef2())==0){
						return v1.compareTo(v2);
					}
					return o1.getDef2().compareTo(o2.getDef2());
				}
			});
			this.model.directlyUpdate(newVO);
		}
		this.showQueryInfo();
	}
	
}