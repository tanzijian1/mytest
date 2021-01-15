package nc.ui.tg.addticket.action;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.bill.BillModel;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class AddticketBodyDellineAction extends nc.ui.pubapp.uif2app.actions.BodyDelLineAction{
 @Override
public void doAction() {
	// TODO 自动生成的方法存根
	super.doAction();
	try{
	BillModel model=getCardPanel().getBillModel();
	IUAPQueryBS query = NCLocator.getInstance().lookup(
			IUAPQueryBS.class);
	//重算表体发票金额
	UFDouble sumfund = UFDouble.ZERO_DBL;
	UFDouble def37sumfund = UFDouble.ZERO_DBL;
	int  count= model.getRowCount();
	for (int i = 0; i < count; i++) {
		//发票类型编码(01,02,03增值税发票类型)
		String def7 =(String) model.getValueAt(i, "def7");
		String code=(String)query.executeQuery("select  code from bd_defdoc  where (enablestate = 2) and pk_defdoclist = (select pk_defdoclist from bd_defdoclist where bd_defdoclist.code='pjlx' ) and (pk_defdoc='"+def7+"' or name='"+def7+"' or code='"+def7+"')", new ColumnProcessor());
		if(code==null||code.length()<1)continue;
		Object obj = model.getValueAt(i, "def5");
		if(!("01".equals(code)||"02".equals(code)||"03".equals(code))){
			if (obj != null) {
				UFDouble uf = (UFDouble)obj;
				def37sumfund = def37sumfund.add(uf);
			  }
		}else{//发票类型编码(01,02,03增值税发票类型)
		if (obj != null) {
			UFDouble uf = (UFDouble)obj;
			sumfund = sumfund.add(uf);
		  }
		}
	}
	//重算表体发票税额
		UFDouble sum = UFDouble.ZERO_DBL;
		UFDouble def38sum = UFDouble.ZERO_DBL;
		for (int i = 0; i < count; i++) {
			//发票类型编码(01,02,03增值税发票类型)
			String def7 =(String) model.getValueAt(i, "def7");
			String code=(String)query.executeQuery("select  code from bd_defdoc  where (enablestate = 2) and pk_defdoclist = (select pk_defdoclist from bd_defdoclist where bd_defdoclist.code='pjlx' )  and (pk_defdoc='"+def7+"' or name='"+def7+"' or code='"+def7+"')", new ColumnProcessor());
			if(code==null||code.length()<1)continue;
			Object obj =model.getValueAt(i, "def6");
			if(!("01".equals(code)||"02".equals(code)||"03".equals(code))){
				if (obj != null) {
					UFDouble uf = (UFDouble)obj;
					def38sum = def38sum.add(uf);
				   } 
			}else{
			if (obj != null) {
				UFDouble uf = (UFDouble)obj;
				sum = sum.add(uf);
			   } 
			 }
			}
		getCardPanel().getHeadItem("def10").setValue(sumfund);
		getCardPanel().getHeadItem("def11").setValue(sum);
		getCardPanel().getHeadItem("def37").setValue(def37sumfund);
		getCardPanel().getHeadItem("def38").setValue(def38sum);
    }catch(Exception e){
    	ExceptionUtils.wrappException(e);
	}
}
}
