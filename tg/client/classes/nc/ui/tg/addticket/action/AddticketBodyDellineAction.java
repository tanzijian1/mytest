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
	// TODO �Զ����ɵķ������
	super.doAction();
	try{
	BillModel model=getCardPanel().getBillModel();
	IUAPQueryBS query = NCLocator.getInstance().lookup(
			IUAPQueryBS.class);
	//������巢Ʊ���
	UFDouble sumfund = UFDouble.ZERO_DBL;
	UFDouble def37sumfund = UFDouble.ZERO_DBL;
	int  count= model.getRowCount();
	for (int i = 0; i < count; i++) {
		//��Ʊ���ͱ���(01,02,03��ֵ˰��Ʊ����)
		String def7 =(String) model.getValueAt(i, "def7");
		String code=(String)query.executeQuery("select  code from bd_defdoc  where (enablestate = 2) and pk_defdoclist = (select pk_defdoclist from bd_defdoclist where bd_defdoclist.code='pjlx' ) and (pk_defdoc='"+def7+"' or name='"+def7+"' or code='"+def7+"')", new ColumnProcessor());
		if(code==null||code.length()<1)continue;
		Object obj = model.getValueAt(i, "def5");
		if(!("01".equals(code)||"02".equals(code)||"03".equals(code))){
			if (obj != null) {
				UFDouble uf = (UFDouble)obj;
				def37sumfund = def37sumfund.add(uf);
			  }
		}else{//��Ʊ���ͱ���(01,02,03��ֵ˰��Ʊ����)
		if (obj != null) {
			UFDouble uf = (UFDouble)obj;
			sumfund = sumfund.add(uf);
		  }
		}
	}
	//������巢Ʊ˰��
		UFDouble sum = UFDouble.ZERO_DBL;
		UFDouble def38sum = UFDouble.ZERO_DBL;
		for (int i = 0; i < count; i++) {
			//��Ʊ���ͱ���(01,02,03��ֵ˰��Ʊ����)
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
