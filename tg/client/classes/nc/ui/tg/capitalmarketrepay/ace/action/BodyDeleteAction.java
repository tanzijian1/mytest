package nc.ui.tg.capitalmarketrepay.ace.action;

import nc.vo.pub.lang.UFDouble;

public class BodyDeleteAction extends nc.ui.pubapp.uif2app.actions.BodyDelLineAction{

	@Override
	public void doAction() {
		// TODO 自动生成的方法存根
		super.doAction();
		String tabCode = this.getCardPanel().getBodyPanel().getTableCode();
		if("pk_marketrepaley_b".equals(tabCode)){
			UFDouble interest = UFDouble.ZERO_DBL;//利息
			UFDouble money = UFDouble.ZERO_DBL;//本金
			int rowCount = this.getCardPanel().getBillModel(tabCode).getRowCount();
			for (int i = 0; i < rowCount; i++) {
				UFDouble def4 = (UFDouble)this.getCardPanel().getBillModel(tabCode).getValueAt(i, "def4");
				def4 = def4==null?UFDouble.ZERO_DBL:def4;
				money = money.add(def4);
				UFDouble def5 = (UFDouble)this.getCardPanel().getBillModel(tabCode).getValueAt(i, "def5");
				def5 = def5==null?UFDouble.ZERO_DBL:def5;
				interest = interest.add(def5);
			}
			this.getCardPanel().getHeadItem("def23").setValue(money);
			this.getCardPanel().getHeadItem("def24").setValue(interest);
		}
	}
}
