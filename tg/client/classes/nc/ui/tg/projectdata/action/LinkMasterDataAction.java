package nc.ui.tg.projectdata.action;

import java.awt.event.ActionEvent;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.sm.funcreg.FuncRegisterVO;

public class LinkMasterDataAction extends NCAction {

	/**
	 * 联查投拓系统
	 */
	public LinkMasterDataAction() {
		super();
		setCode("linkMasterDataAction");
		setBtnName("联查主数据套表信息");
	}

	private BillForm editor;

	private BillManageModel model;

	private BillListView listView;

	private IUAPQueryBS queryBS;

	@Override
	public boolean isEnabled() {
		AggregatedValueObject aggvo = (AggregatedValueObject) getModel()
				.getSelectedData();
		if (aggvo == null || aggvo.getParentVO() == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected boolean isActionEnable() {
		AggregatedValueObject aggvo = (AggregatedValueObject) getModel()
				.getSelectedData();
		if (aggvo == null || aggvo.getParentVO() == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		IBill data = (IBill) getModel().getSelectedData();
		if (null == data) {
			throw new BusinessException("请至少选择一条数据");
		}
		AggregatedValueObject aggvo = (AggregatedValueObject) data;
		String name = (String) aggvo.getParentVO().getAttributeValue("name");

		List<String> pks = (List<String>) getQueryBS()
				.executeQuery(
						"select sdfn_masterdata.pk_masterdata from sdfn_masterdata where sdfn_masterdata.def3 ='"
								+ name + "'  and dr = 0 ",
						new ColumnListProcessor());

		if (pks == null || pks.size() <= 0) {
			throw new BusinessException("选择的付款单单据联查不到对应的主数据套表单");
		}

		DefaultLinkData userdata = new DefaultLinkData();
		userdata.setBillIDs(pks.toArray(new String[0]));
		FuncletInitData initdata = new FuncletInitData();
		initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
		initdata.setInitData(userdata);
		// BilltypeVO billType = PfDataCache.getBillType("F3");
		FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
				.getFuncRegisterVO("36H2020110");
		if (registerVO == null) {
			throw new BusinessException("当前用户没有项目资料节点权限，请联系系统管理员");
		}
		FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
				.getEntranceUI(), registerVO, initdata, null, true, true);
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public BillManageModel getModel() {
		model.addAppEventListener(this);
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
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
