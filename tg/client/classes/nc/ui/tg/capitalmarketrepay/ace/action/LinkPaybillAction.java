package nc.ui.tg.capitalmarketrepay.ace.action;

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
import nc.vo.pub.BusinessException;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;

public class LinkPaybillAction extends NCAction{
	public LinkPaybillAction(){
		setBtnName("���鸶����㵥");
		setCode("linkPaybillAction");
	}
	private BillManageModel model = null;
	
	private BillForm editor;
	
	private BillListView listView;
	
	IUAPQueryBS queryBS = null;
	public IUAPQueryBS getQueryBS() {
		if (queryBS == null) {
			queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}

		return queryBS;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO �Զ����ɵķ������
		IBill obj = (IBill) getModel().getSelectedData();
		if (obj == null){
			throw new BusinessException("δѡ������");
		}
		AggMarketRepalayVO aggvo = (AggMarketRepalayVO) obj;
		MarketRepalayVO parentVO = aggvo.getParentVO();
		List<String> pks = (List<String>) getQueryBS().executeQuery(
				"select pk_paybill from cmp_paybill where nvl(dr,0)=0 and pk_upbill = '"
						+ parentVO.getPk_marketreplay() + "'", new ColumnListProcessor());
		if (pks == null || pks.size()<=0) {
			throw new BusinessException("���ŵ������鲻����Ӧ�ĸ�����㵥");
		}
		DefaultLinkData userdata = new DefaultLinkData();
		userdata.setBillIDs(pks.toArray(new String[0]));
		FuncletInitData initdata = new FuncletInitData();
		initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
		initdata.setInitData(userdata);
		FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()	
				.getFuncRegisterVO("36070PBM");
		if (registerVO==null) {
			throw new BusinessException("��ǰ�û�û�и���������ڵ�Ȩ�ޣ�����ϵϵͳ����Ա");
		}
		FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
				.getEntranceUI(), registerVO, initdata, null, true, true);
	}
	public BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
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

}
