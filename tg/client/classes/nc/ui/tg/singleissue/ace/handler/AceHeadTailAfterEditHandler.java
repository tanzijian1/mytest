package nc.ui.tg.singleissue.ace.handler;

import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.tg.singleissue.util.SingleIssueUtil;
import nc.util.SdfnUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/**
 * 回写还款计划页签
 * @author wenjie
 *
 */
public class AceHeadTailAfterEditHandler implements IAppEventHandler<CardHeadTailAfterEditEvent>{
	private nc.ui.tg.singleissue.ace.view.SingleissueBillForm editor;
	
	public nc.ui.tg.singleissue.ace.view.SingleissueBillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.tg.singleissue.ace.view.SingleissueBillForm editor) {
		this.editor = editor;
	}

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		if("def1".equals(e.getKey())){//关联批文字段
			try {
				String[] tabcodes = {"pk_repayplan","pk_bondresale","pk_cyclebuying","pk_absrepay"};
				for (String tabcode : tabcodes) {
					BillModel billModel = e.getBillCardPanel().getBillModel(tabcode);
					if(billModel != null){
						billModel.clearBodyData();
					}
				}
				String busiType = SingleIssueUtil.getBusiType((String)e.getValue());
				if(SdfnUtil.getABSList().contains(busiType)){
					e.getBillCardPanel().getHeadItem("def13").setEnabled(true);
					e.getBillCardPanel().getHeadItem("def14").setEnabled(true);
				}else{
					e.getBillCardPanel().getHeadItem("def13").setEnabled(false);
					e.getBillCardPanel().getHeadItem("def14").setEnabled(false);
					e.getBillCardPanel().getHeadItem("def13").setValue(null);
					e.getBillCardPanel().getHeadItem("def14").setValue(null);
					SingleIssueUtil.writeRepayPlan(e);
				}
			} catch (BusinessException e1) {
				ExceptionUtils.wrappBusinessException(e1.getMessage());
			}
			
		}else if("def10".equals(e.getKey())){//起息日字段
			SingleIssueUtil.writeRepayPlan(e);
			e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
		}else if("def12".equals(e.getKey())){//到期日
			SingleIssueUtil.writeRepayPlan(e);
			e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
		}else if("def16".equals(e.getKey())){//利率
			SingleIssueUtil.writeRepayPlan(e);
			e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
		}else if("def19".equals(e.getKey())){//循环购买日
			if(e.getValue()!=null){
				e.getBillCardPanel().getHeadItem("def20").setEnabled(true);
			}else{
				e.getBillCardPanel().getHeadItem("def20").setEnabled(false);
			}
		}else if("def6".equals(e.getKey()) || "def7".equals(e.getKey())){
			UFDouble yx = (UFDouble)e.getBillCardPanel().getHeadItem("def6").getValueObject();
			UFDouble cj = (UFDouble)e.getBillCardPanel().getHeadItem("def7").getValueObject();
			yx = yx==null?UFDouble.ZERO_DBL:yx;
			cj = cj==null?UFDouble.ZERO_DBL:cj;
			UFDouble money = yx.add(cj,2);
			e.getBillCardPanel().getHeadItem("def5").setValue(money);
			wirteData(e);
			BillModel billModel = e.getBillCardPanel().getBillModel("pk_bondresale");
			if(billModel != null){
				//转售金额
				UFDouble tumoney = (UFDouble)billModel.getValueAt(0, "def7");
				tumoney = tumoney==null?UFDouble.ZERO_DBL:tumoney;
				//回售金额
				UFDouble remoney = (UFDouble)billModel.getValueAt(0, "def2");
				remoney = remoney==null?UFDouble.ZERO_DBL:remoney;
				billModel.setValueAt(money.sub(remoney).add(tumoney), 0, "def3");
			}
			SingleIssueUtil.writeRepayPlan(e);
			e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
		}else if("def5".equals(e.getKey())){
			wirteData(e);
			UFDouble money = (UFDouble) e.getValue();
			BillModel billModel = e.getBillCardPanel().getBillModel("pk_bondresale");
			if(billModel != null){
				//转售金额
				UFDouble tumoney = (UFDouble)billModel.getValueAt(0, "def7");
				tumoney = tumoney==null?UFDouble.ZERO_DBL:tumoney;
				//回售金额
				UFDouble remoney = (UFDouble)billModel.getValueAt(0, "def2");
				remoney = remoney==null?UFDouble.ZERO_DBL:remoney;
				billModel.setValueAt(money.sub(remoney).add(tumoney), 0, "def3");
			}
			SingleIssueUtil.writeRepayPlan(e);
			e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
		}else if("def41".equals(e.getKey())){
			wirteData(e);
		}else if("def43".equals(e.getKey())){
			wirteData(e);
		}
	}
	
	private void wirteData(CardHeadTailAfterEditEvent e){
		BillModel billModel = e.getBillCardPanel().getBillModel("pk_bondresale");
		if(billModel != null){
			UFDouble rerate  = (UFDouble) billModel.getValueAt(0, "def4");//回售利率
			UFDouble value = (UFDouble) e.getBillCardPanel().getHeadItem("def41").getValueObject();//债券回售财务顾问费
			UFDouble total = (UFDouble) e.getBillCardPanel().getHeadItem("def5").getValueObject();//发行总规模
			UFDouble time = (UFDouble) e.getBillCardPanel().getHeadItem("def43").getValueObject();//可续期限
			if(rerate!=null && value!=null && total!=null && time!=null){
				e.getBillCardPanel().getHeadItem("def44").setValue(
						rerate.add(value.div(time.multiply(total)).multiply(new UFDouble(100),2)));
			}
		}
	}
}
