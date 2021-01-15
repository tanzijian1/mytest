package nc.ui.tg.addticket.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class AceTicketBodyAfterEditHandler implements
		IAppEventHandler<CardBodyAfterEditEvent> {

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		try {
			// TODO 自动生成的方法存根
			 nc.ui.pub.bill.BillCardPanel panel=e.getBillCardPanel();
			BillModel model = e.getBillCardPanel().getBillModel();
			IUAPQueryBS query = NCLocator.getInstance().lookup(
					IUAPQueryBS.class);
			if ("def5".equals(e.getKey())) {//增值税发票金额
				UFDouble sum = UFDouble.ZERO_DBL;
				UFDouble def37sum = UFDouble.ZERO_DBL;
				int count = model.getRowCount();
				for (int i = 0; i < count; i++) {
					//发票类型编码(01,02,03增值税发票类型)
					String def7 =(String) model.getValueAt(i, "def7");
					String code=(String)query.executeQuery("select  code from bd_defdoc  where (enablestate = 2) and pk_defdoclist = (select pk_defdoclist from bd_defdoclist where bd_defdoclist.code='pjlx' ) and (pk_defdoc='"+def7+"' or name='"+def7+"' or code='"+def7+"')", new ColumnProcessor());
					if(code==null||code.length()<1)continue;
					Object obj = model.getValueAt(i, "def5");
					if(!("01".equals(code)||"02".equals(code)||"03".equals(code))){
						if (obj != null) {
							UFDouble uf = (UFDouble)obj;
							def37sum = def37sum.add(uf);
						  }
					}else{//发票类型编码(01,02,03增值税发票类型)
					if (obj != null) {
						UFDouble uf = (UFDouble)obj;
						sum = sum.add(uf);
					  }
					}
				}
				e.getBillCardPanel().getHeadItem("def10").setValue(sum);
				e.getBillCardPanel().getHeadItem("def37").setValue(def37sum);
			}

			if ("def6".equals(e.getKey())) {//增值税发票税额
				UFDouble sum = UFDouble.ZERO_DBL;
				UFDouble def38sum = UFDouble.ZERO_DBL;
				int count = model.getRowCount();
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
					}else{//发票类型编码(01,02,03增值税发票类型)
					if (obj != null) {
						UFDouble uf = (UFDouble)obj;
						sum = sum.add(uf);
					  }
					}
				}
				e.getBillCardPanel().getHeadItem("def11").setValue(sum);
				e.getBillCardPanel().getHeadItem("def38").setValue(def38sum);
			}
			if("def7".equals(e.getKey())){
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
					}else{//(01,02,03增值税发票类型)
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
				e.getBillCardPanel().getHeadItem("def10").setValue(sumfund);
				e.getBillCardPanel().getHeadItem("def11").setValue(sum);
				e.getBillCardPanel().getHeadItem("def37").setValue(def37sumfund);
				e.getBillCardPanel().getHeadItem("def38").setValue(def38sum);
			}
			
			if ("def2".equals(e.getKey())) {// 指定侍函账户全称
				UIRefPane refPane = (UIRefPane) e.getBillCardPanel()
						.getBodyItem("def3").getComponent();
				((nc.ui.bd.ref.model.CustBankaccDefaultRefModel) refPane
						.getRefModel()).setPk_cust((String) e.getValue());
			} 
			
			if("def1".equals(e.getKey())){//请款单号
				int currow=e.getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
				String def1=(String)e.getBillCardPanel().getBodyValueAt(currow, "def1");
				e.getBillCardPanel().setBodyValueAt(getRecUnit(def1), currow, "def2");
			}
		} catch (Exception e1) {
			Logger.error(e1.getMessage(), e1);
			ExceptionUtils.wrappBusinessException(e1.getMessage());
		}
	}
   
	/**
	 * 获取收款单位
	 * @return
	 */
	private String getRecUnit(String pk){
		IUAPQueryBS bs =NCLocator.getInstance().lookup(IUAPQueryBS.class);
		try {
			return (String)bs.executeQuery("select reunit from (select  tf.pk_finexpense pk_key,tf.pk_payee  reunit from tgrz_financexpense tf "
					+ " union select cr.pk_repayrcpt pk_key,cr.reunit reunit from cdm_repayrcpt  cr"
					+ " union select mt.pk_marketreplay pk_key,mt.def7 reunit from sdfn_marketrepalay mt)  where pk_key='"+pk+"'", new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
}
