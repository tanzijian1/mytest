package nc.ui.tg.financingexpense.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.ISRMSupplierAccount;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class SupplierAccountAction extends NCAction   {
	private BillManageModel model = null;
	private nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor;

	public SupplierAccountAction() {
		super.setCode("SupplierAccount");
		super.setBtnName("供应商账号维护");
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		
	String userId =	model.getContext().getPk_loginUser();
	String  nodeCode= model.getContext().getNodeCode();
 	
 String pk_supplier=null;
 
 if(nodeCode.startsWith("36H")){
	// AggFinancexpenseVO  vo=	(AggFinancexpenseVO) model.getSelectedData();
	 //pk_supplier=	 (String) vo.getParentVO().getAttributeValue("pk_payee");
	 pk_supplier =(String) editor.getBillCardPanel().getHeadItem("pk_payee").getValueObject();

 }else{
//	 AggRePayReceiptBankCreditVO vo= (AggRePayReceiptBankCreditVO)model.getSelectedData();
//	 pk_supplier=	 (String) vo.getParentVO().getAttributeValue("reunit");
	 pk_supplier =(String) editor.getBillCardPanel().getHeadItem("reunit").getValueObject();
 }
 
 if(pk_supplier==null){

		ExceptionUtils.wrappBusinessException("供应商信息(收款单位)为空");
 }
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
	String	userCode = (String) bs.executeQuery("SELECT sm.user_code FROM sm_user sm WHERE sm.cuserid = '"+userId+"';", new ColumnProcessor());
	String	supplierCode = (String) bs.executeQuery("select code  from bd_cust_supplier  where bd_cust_supplier.pk_cust_sup= '"+pk_supplier+"';", new ColumnProcessor());	
	ISRMSupplierAccount supplier = NCLocator.getInstance().lookup(ISRMSupplierAccount.class);
		String Url =supplier.HyperLinkHandler(userCode,supplierCode);
		URI uri =new URI(Url);
		Desktop desktop = Desktop.getDesktop();
	    desktop.browse(uri);
	}

	
	
	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}
	
	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor) {
		this.editor = editor;
	}



}
