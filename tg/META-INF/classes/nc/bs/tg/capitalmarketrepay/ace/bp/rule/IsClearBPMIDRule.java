package nc.bs.tg.capitalmarketrepay.ace.bp.rule;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.image.IGuoXinImage;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;


public class IsClearBPMIDRule implements IRule<AggMarketRepalayVO>{

	@Override
	public void process(AggMarketRepalayVO[] vos) {
		IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		IGuoXinImage image=NCLocator.getInstance().lookup(IGuoXinImage.class);
		MarketRepalayVO parentVO = vos[0].getParentVO();
		String def47 = parentVO.getDef47();
		try {
			if(!"Y".equals(def47)){//Ϊnull����N�����bpmid
				if(parentVO.getDef21()!=null && !"".equals(parentVO.getDef21())){
					String	username = (String)bs.executeQuery("select user_name  from sm_user where cuserid='"
							+InvocationInfoProxy.getInstance().getUserId()+"'", new ColumnProcessor());
					image.delrefund(parentVO.getDef21(), "1", InvocationInfoProxy.getInstance().getUserCode(),
							username, null, "", null, "2");
				}
				parentVO.setDef20(null);//���bpmid
				parentVO.setDef41(null);//���OPENURL
				parentVO.setDef21(null);//���Ӱ�����
			}
			parentVO.setDef47(null);
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}
}
