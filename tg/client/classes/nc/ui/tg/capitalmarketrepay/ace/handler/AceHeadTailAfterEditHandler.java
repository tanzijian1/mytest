package nc.ui.tg.capitalmarketrepay.ace.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import nc.bs.framework.common.NCLocator;
import nc.md.model.MetaDataException;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.MarketRepaleyBVO;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.BondTransSaleVO;
import nc.vo.tg.singleissue.RepaymentPlanVO;
import nc.vo.tg.singleissue.SingleIssueVO;
/**
 * ��д����ƻ�ҳǩ
 * @author wenjie
 *
 */
public class AceHeadTailAfterEditHandler implements IAppEventHandler<CardHeadTailAfterEditEvent>{
	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		// TODO �Զ����ɵķ������
		if("def5".equals(e.getKey())){//���ڷ������������Ŀ���ƺͻ���ƻ�,�ۼ��ѻ������ۼ��ѻ���Ϣ
			String pk_singleissue = (String)e.getValue();
			if(pk_singleissue != null){
				try {
					AggSingleIssueVO aggvo = (AggSingleIssueVO)getBillVO(AggSingleIssueVO.class,"nvl(dr,0)=0 and pk_singleissue='"+pk_singleissue+"'");
					if(aggvo != null){
						SingleIssueVO svo = aggvo.getParentVO();
						e.getBillCardPanel().setHeadItem("def6", svo.getDef4());
						e.getBillCardPanel().setHeadItem("def11", svo.getDef5());
						
						RepaymentPlanVO[] rvos = (RepaymentPlanVO[])aggvo.getChildren(RepaymentPlanVO.class);
						e.getBillCardPanel().getBillModel("pk_marketrepaley_b").clearBodyData();
						UFDouble sum = UFDouble.ZERO_DBL;
						UFDouble sumInterest = UFDouble.ZERO_DBL;
						if(rvos != null){
							Arrays.sort(rvos,new Comparator<RepaymentPlanVO>() {
								@Override
								public int compare(RepaymentPlanVO o1,
										RepaymentPlanVO o2) {
									UFDouble v1 = o1.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(o1.getDef3());
									UFDouble v2 = o2.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(o2.getDef3());
									if(o1.getDef2()!=null && o2.getDef2()!=null){
										if(o1.getDef2().compareTo(o2.getDef2())==0){
											return v1.compareTo(v2);
										}else{
											return o1.getDef2().compareTo(o2.getDef2());
										}
									}else{
										return 0;
									}
								}
							});
							for (RepaymentPlanVO rvo : rvos) {
								e.getBillCardPanel().getBillModel("pk_marketrepaley_b").addLine();
								int row = e.getBillCardPanel().getBillModel("pk_marketrepaley_b").getRowCount();
								e.getBillCardPanel().getBillModel("pk_marketrepaley_b").setValueAt(rvo.getPk_repayplan(),row-1,"def1");
								e.getBillCardPanel().getBillModel("pk_marketrepaley_b").setValueAt(rvo.getDef2()==null?UFDouble.ZERO_DBL:rvo.getDef2(),row-1,"def2");
								e.getBillCardPanel().getBillModel("pk_marketrepaley_b").setValueAt(rvo.getDef3()==null?UFDouble.ZERO_DBL:rvo.getDef3(),row-1,"def3");
								e.getBillCardPanel().getBillModel("pk_marketrepaley_b").setValueAt(rvo.getDef3()==null?UFDouble.ZERO_DBL:rvo.getDef3(),row-1,"def4");
								e.getBillCardPanel().getBillModel("pk_marketrepaley_b").setValueAt(rvo.getDef4()==null?UFDouble.ZERO_DBL:rvo.getDef4(),row-1,"def5");
								UFDouble money = rvo.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(rvo.getDef3());
								UFDouble interest = rvo.getDef4()==null?UFDouble.ZERO_DBL:new UFDouble(rvo.getDef4());
								sum = sum.add(money, 2);
								sumInterest = sumInterest.add(interest,2);
							}
							e.getBillCardPanel().getHeadItem("def23").setValue(sum);
							e.getBillCardPanel().getHeadItem("def24").setValue(sumInterest);
						}
						
						BondTransSaleVO[] tvos = (BondTransSaleVO[]) aggvo.getChildren(BondTransSaleVO.class);//��ִͬ�����
						UFDouble hamount = UFDouble.ZERO_DBL;//�ۼ��ѻ�����
						UFDouble hinterest = UFDouble.ZERO_DBL;//�ۼ��ѻ���Ϣ
						if(tvos != null){
							for (BondTransSaleVO vo : tvos) {
								hamount = hamount.add(vo.getDef6()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef6()),2);//�ѻ�����
								hinterest = hinterest.add(vo.getDef8()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef8()),2);//�ѻ���Ϣ
							}
						}
						e.getBillCardPanel().getHeadItem("def13").setValue(hamount);
						e.getBillCardPanel().getHeadItem("def14").setValue(hinterest);
					}
					
				} catch (Exception e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
					ExceptionUtils.wrappException(e1);
				}
			}else{
				e.getBillCardPanel().getBillModel("pk_marketrepaley_b").clearBodyData();
				e.getBillCardPanel().setHeadItem("def6", null);
				e.getBillCardPanel().setHeadItem("def11", null);
			}
		}
		if("def7".equals(e.getKey())){
			e.getBillCardPanel().getHeadItem("def8").setValue(null);//�޸��տλ������տλ�˻�
		}
		if("def9".equals(e.getKey())){
			e.getBillCardPanel().getHeadItem("def10").setValue(null);//�޸ĸ��λ��������λ�˻�
		}
		if("def15".equals(e.getKey())){
			try {
				if(null == e.getValue()){
					e.getBillCardPanel().getHeadItem("def16").setNull(false);
				}else{
					String pk_defdoc = (String) e.getValue();
					String name = getIsOrNot(pk_defdoc);
					if("��".equals(name)){
						e.getBillCardPanel().getHeadItem("def16").setNull(true);
					}else{
						e.getBillCardPanel().getHeadItem("def16").setNull(false);
					}
				}
			} catch (Exception e2) {
				ExceptionUtils.wrappException(e2);
			}
		}
	}
	private IMDPersistenceQueryService mdQryService = null;
	private IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}
	private String getIsOrNot(String pk_defdoc) throws BusinessException{
		DefdocVO vo = (DefdocVO)HYPubBO_Client.queryByPrimaryKey(DefdocVO.class, pk_defdoc);
		if(vo == null){
			return null;
		}else{
			return vo.getName();
		}
	}
	private AggregatedValueObject getBillVO(Class c,String whereCondStr) throws MetaDataException{
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
}
