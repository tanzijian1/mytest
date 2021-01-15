package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.BillListView;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class LinkCmpPaybillAction extends NCAction{

	public LinkCmpPaybillAction() {
		setBtnName("联查付款结算单");
		setCode("linkCmpPaybillAction");
	}
	
	private BillManageModel model = null;
	
	private BillForm editor;
	
	private BillListView listView;
	
	IUAPQueryBS queryBS = null;
	@Override
	public void doAction(ActionEvent e) throws Exception {
		IBill obj = (IBill) getModel().getSelectedData();
		if (obj == null){
			throw new BusinessException("未选中数据");
		}
		AggregatedValueObject aggvo = (AggregatedValueObject) obj;
		String pk = null;
		if(obj instanceof AggFinancexpenseVO){
			pk = (String) aggvo.getParentVO().getAttributeValue("pk_finexpense");
		}else if(obj instanceof AggAddTicket){
			pk = (String) aggvo.getParentVO().getAttributeValue("pk_ticket");
		}else if(obj instanceof AggRePayReceiptBankCreditVO){
			pk = (String) aggvo.getParentVO().getAttributeValue("pk_repayrcpt");
		}
		
		List<String> pks = (List<String>) getQueryBS().executeQuery(
				"select pk_paybill from cmp_paybill where nvl(dr,0)=0 and pk_upbill = '"
						+ pk + "'", new ColumnListProcessor());
		if (pks == null || pks.size()<=0) {
			throw new BusinessException("该张单据联查不到对应的付款结算单");
		}
//		String billtype = null;
//		if(obj instanceof AggRePayReceiptBankCreditVO){
//			billtype = "36FF";
//		}else if(obj instanceof AggFinancexpenseVO){
//			billtype = (String) obj.getParent().getAttributeValue("transtype");
//		}else if(obj instanceof AggAddTicket){
//			billtype = "RZ30";
//		}
		DefaultLinkData userdata = new DefaultLinkData();
		userdata.setBillIDs(pks.toArray(new String[0]));
		FuncletInitData initdata = new FuncletInitData();
		initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
		initdata.setInitData(userdata);
//		BilltypeVO billType = PfDataCache.getBillType("F5");
		FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()	
				.getFuncRegisterVO("36070PBM");
		if (registerVO==null) {
			throw new BusinessException("当前用户没有付款结算管理节点权限，请联系系统管理员");
		}
		FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
				.getEntranceUI(), registerVO, initdata, null, true, true);
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}
	
	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}
	
	public BillListView getListView() {
		return listView;
	}

	public void setListView(BillListView listView) {
		this.listView = listView;
	}
	
	public IUAPQueryBS getQueryBS() {
		if (queryBS == null) {
			queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}

		return queryBS;
	}
}
