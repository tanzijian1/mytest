package nc.ui.tg.capitalmarketrepay.ace.handler;

import java.util.HashMap;
import java.util.Map;

import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.singleissue.RepaymentPlanVO;

public class AceBodyTailAfterEditHandler implements IAppEventHandler<CardBodyAfterEditEvent>{
	private nc.ui.pubapp.uif2app.model.BillManageModel model;
	
	public nc.ui.pubapp.uif2app.model.BillManageModel getModel() {
		return model;
	}

	public void setModel(nc.ui.pubapp.uif2app.model.BillManageModel model) {
		this.model = model;
	}

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		// TODO 自动生成的方法存根
		String tabCode = e.getTableCode();
		if("pk_marketrepaley_b".equals(tabCode)){//资本市场还款，还款计划
			if("def1".equals(e.getKey())){
				DefaultConstEnum cEnum = (DefaultConstEnum)e.getBillCardPanel().getBillModel(tabCode).getValueObjectAt(e.getRow(), e.getKey());
				String pk_repaypaln = cEnum==null?null:(String)cEnum.getValue();
				if(pk_repaypaln != null){
					boolean flag = true;
					int rowCount = e.getBillCardPanel().getBillModel(tabCode).getRowCount();
					for (int i = 0; i < rowCount; i++) {
						if(i != e.getRow()){
							DefaultConstEnum conEnum = (DefaultConstEnum)e.getBillCardPanel().getBillModel(tabCode).getValueObjectAt(i, e.getKey());
							String pk = conEnum==null?null:(String)conEnum.getValue();
							if(pk_repaypaln.equals(pk)){
								flag = false;
								e.getBillCardPanel().getBillModel(tabCode).setValueAt(null, e.getRow(), e.getKey());
								e.getBillCardPanel().getBillModel(tabCode).setValueAt(null, e.getRow(), "def2");
								e.getBillCardPanel().getBillModel(tabCode).setValueAt(null, e.getRow(), "def3");
								e.getBillCardPanel().getBillModel(tabCode).setValueAt(null, e.getRow(), "def4");
								e.getBillCardPanel().getBillModel(tabCode).setValueAt(null, e.getRow(), "def5");
								ShowStatusBarMsgUtil.showErrorMsgWithClear("提示","还款计划重复！", e.getContext());
							}
						}
					}
					if(flag){
						try {
							RepaymentPlanVO vo = (RepaymentPlanVO)HYPubBO_Client.queryByPrimaryKey(RepaymentPlanVO.class, pk_repaypaln);
							e.getBillCardPanel().getBillModel(tabCode).setValueAt(vo.getDef2(), e.getRow(), "def2");
							e.getBillCardPanel().getBillModel(tabCode).setValueAt(vo.getDef3()==null?UFDouble.ZERO_DBL:vo.getDef3(), e.getRow(), "def3");
							
							e.getBillCardPanel().getBillModel(tabCode).setValueAt(vo.getDef3()==null?UFDouble.ZERO_DBL:vo.getDef3(), e.getRow(), "def4");
							e.getBillCardPanel().getBillModel(tabCode).setValueAt(vo.getDef4()==null?UFDouble.ZERO_DBL:vo.getDef4(), e.getRow(), "def5");
							
							ShowStatusBarMsgUtil.showStatusBarMsg(null, e.getContext());
						} catch (UifException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
							ExceptionUtils.wrappException(e1);
						}
					}
					Map<String, UFDouble> total = this.getTotalMoney(e);
					e.getBillCardPanel().getHeadItem("def23").setValue(total.get("money"));
					e.getBillCardPanel().getHeadItem("def24").setValue(total.get("interest"));
				}else{
					e.getBillCardPanel().getBillModel(tabCode).setValueAt(null,e.getRow(),"def2");
					e.getBillCardPanel().getBillModel(tabCode).setValueAt(null,e.getRow(),"def3");
					e.getBillCardPanel().getBillModel(tabCode).setValueAt(null,e.getRow(),"def4");
					e.getBillCardPanel().getBillModel(tabCode).setValueAt(null,e.getRow(),"def5");
				}
			}
			if("def4".equals(e.getKey()) || "def5".equals(e.getKey())){
				Map<String, UFDouble> total = this.getTotalMoney(e);
				e.getBillCardPanel().getHeadItem("def23").setValue(total.get("money"));
				e.getBillCardPanel().getHeadItem("def24").setValue(total.get("interest"));
			}
		}
	}
	
	private Map<String,UFDouble> getTotalMoney(CardBodyAfterEditEvent e){
		UFDouble interest = UFDouble.ZERO_DBL;//利息
		UFDouble money = UFDouble.ZERO_DBL;//本金
		String tabCode = e.getTableCode();
		int rowCount = e.getBillCardPanel().getBillModel(tabCode).getRowCount();
		for (int i = 0; i < rowCount; i++) {
			UFDouble def4 = (UFDouble)e.getBillCardPanel().getBillModel(tabCode).getValueAt(i, "def4");
			def4 = def4==null?UFDouble.ZERO_DBL:def4;
			money = money.add(def4);
			UFDouble def5 = (UFDouble)e.getBillCardPanel().getBillModel(tabCode).getValueAt(i, "def5");
			def5 = def5==null?UFDouble.ZERO_DBL:def5;
			interest = interest.add(def5);
		}
		Map<String,UFDouble> map = new HashMap<>();
		map.put("interest", interest);
		map.put("money", money);
		return map;
	}

}
