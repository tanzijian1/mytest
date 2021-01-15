package nc.ui.tg.financingexpense.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.ref.model.CustBankaccDefaultRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringUtils;

public class AceHeadBeforeEditHandler implements
IAppEventHandler<CardHeadTailBeforeEditEvent>{

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent event) {
		// TODO 自动生成的方法存根
		
		if("def7".equals(event.getKey())){
			String pk_cust= (String) event.getBillCardPanel().getHeadItem("pk_payee").getValueObject();
			nc.ui.pub.bill.BillItem item = (BillItem) event.getSource();
			UIRefPane refPane = (UIRefPane) item.getComponent();
			nc.ui.bd.ref.model.CustBankaccDefaultRefModel ref=(CustBankaccDefaultRefModel) refPane.getRefModel();
			ref.setPk_cust(pk_cust);
		}if("def1".equals(event.getKey())){
			nc.ui.pub.bill.BillItem item = (BillItem) event.getSource();
			UIRefPane refPane = (UIRefPane) item.getComponent();
			String pk_payer = null;
			String pk_tradetype= (String) event.getBillCardPanel().getHeadItem("transtype").getValueObject();
			if("RZ06-Cxx-001".equals(pk_tradetype)){
				//单期发行情况不为空
				if(StringUtils.isNotBlank((String) event.getBillCardPanel().getHeadItem("def12").getValueObject())){
					pk_payer = (String) event.getBillCardPanel().getHeadItem("pk_payer").getValueObject();
					refPane.getRefModel().setPk_org(pk_payer);
				}else{
					if(StringUtils.isBlank((String) event.getBillCardPanel().getHeadItem("def4").getValueObject())){
						pk_payer = (String) event.getBillCardPanel().getHeadItem("def61").getValueObject();
						nc.ui.bd.ref.AbstractRefModel model=((UIRefPane)event.getBillCardPanel().getHeadItem("def1").getComponent()).getRefModel();
						if(model instanceof nc.ui.cdm.repay.ref.RePayBankaccSubCodeNameRefModel){
							((nc.ui.cdm.repay.ref.RePayBankaccSubCodeNameRefModel)model).setPayunit(pk_payer);
						}
					}else{
						pk_payer = (String) event.getBillCardPanel().getHeadItem("pk_payer").getValueObject();
						refPane.getRefModel().setPk_org(pk_payer);
					}
				}
			}else{
				pk_payer = (String) event.getBillCardPanel().getHeadItem("pk_payer").getValueObject();
				refPane.getRefModel().setPk_org(pk_payer);
			}
		}else if("transtypepk".equals(event.getKey())){
			nc.ui.pub.bill.BillItem item = (BillItem) event.getSource();
			UIRefPane refPane = (UIRefPane) item.getComponent();
			String wheresql = "and parentbilltype ='RZ06'";
			refPane.getRefModel().addWherePart(wheresql);
		}
		else if("pk_payer".equals(event.getKey())){
			String wheresql = null;
			nc.ui.pub.bill.BillItem item = (BillItem) event.getSource();
			UIRefPane refPane = (UIRefPane) item.getComponent();
			if(event.getBillCardPanel().getHeadItem("def4").getValueObject() != null){
				String pk_tradetype= (String) event.getBillCardPanel().getHeadItem("transtype").getValueObject();
				if("RZ06-Cxx-001".equals(pk_tradetype)){
					String pk_contract= (String) event.getBillCardPanel().getHeadItem("def4").getValueObject();
					wheresql = "and pk_financeorg in(select pk_czgs from cdm_cwfyft where  pk_contract ='"+pk_contract+"' and cdm_cwfyft.dr='0' )";
				}
			}else if(event.getBillCardPanel().getHeadItem("def12").getValueObject() != null){
				String pk_tradetype= (String) event.getBillCardPanel().getHeadItem("transtype").getValueObject();
				if("RZ06-Cxx-001".equals(pk_tradetype)){
					String pk_singleissue = (String)event.getBillCardPanel().getHeadItem("def12").getValueObject();
					wheresql = "and pk_financeorg in(select def1 from sdfn_contractstate where pk_singleissue = '"+pk_singleissue+"' and dr=0)";
				}
			}else{
				String pk_tradetype= (String) event.getBillCardPanel().getHeadItem("transtype").getValueObject();
				if("RZ06-Cxx-001".equals(pk_tradetype)){
					wheresql = "and pk_financeorg in('')";
				}
			}
			refPane.getRefModel().addWherePart(wheresql);
		}
		
		if(event.getBillCardPanel().getHeadItem("transtypepk").getValueObject()!=null){
			if("RZ06-Cxx-001".equals(queryTranstypeCodeByPk(event.getBillCardPanel().getHeadItem("transtypepk").getValueObject().toString()))){
				
				if("def4".equals(event.getKey())){
					Object org	=event.getBillCardPanel().getHeadItem("pk_org").getValueObject();
					UIRefPane conPanel=(UIRefPane)event.getBillCardPanel().getHeadItem("def4").getComponent();
					//getBodyItem("pk_bankprotocol").getComponent();
					String wheresql="and pk_org ='"+org+"'";
					conPanel.getRefModel().addWherePart(wheresql);
				}else if("def12".equals(event.getKey())){
					String org	=(String)event.getBillCardPanel().getHeadItem("pk_org").getValueObject();
					UIRefPane conPanel=(UIRefPane)event.getBillCardPanel().getHeadItem("def12").getComponent();
					String wheresql="and pk_org ='"+org+"'";
					conPanel.getRefModel().addWherePart(wheresql);
				}
			}
		}
		event.setReturnValue(true);

	}
	
	private String queryTranstypeCodeByPk(String pk) {
	     String sql = "select bd_billtype.pk_billtypecode code from bd_billtype where bd_billtype.pk_billtypeid = '"+pk+"' and nvl(dr,0) = 0 ";
	     IUAPQueryBS bs = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class);
	     try {
			return (String)bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
	   }


}
