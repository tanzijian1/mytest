package nc.ui.tg.capitalmarketrepay.ace.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tg.outside.bpm.NcToBpmVO;

import org.apache.commons.lang.StringUtils;

public class OpenBPMViewAction extends NCAction{

	/**
	 * 查看BPM流程按钮
	 */
	private static final long serialVersionUID = 2975177743632452602L;

	IMDPersistenceQueryService mdQryService = null;
	
	private BillForm editor;

	private BillListView listView;

	private BillManageModel model;
	
	public BillListView getListView() {
		return listView;
	}

	public void setListView(BillListView listView) {
		this.listView = listView;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	IPFBusiAction pfBusiAction = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}
	
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}
	
	public OpenBPMViewAction() {
		super();
		setCode("openBPMViewAction");
		setBtnName("查看BPM流程");
	}
	
	@Override
	protected boolean isActionEnable() {
		AggregatedValueObject aggvo = (AggregatedValueObject) getModel().getSelectedData();
		if (aggvo == null || aggvo.getParentVO() == null) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public boolean isEnabled() {
		AggregatedValueObject aggvo = (AggregatedValueObject) getModel().getSelectedData();
		if (aggvo == null || aggvo.getParentVO() == null) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		IBill bill = (IBill) getModel().getSelectedData();
		if (bill != null) {
			String taskid = (String) bill.getParent().getAttributeValue("def20");
			if (StringUtils.isBlank(taskid))
				throw new BusinessException("BPM主键为空!");
			String openUrl = (String) bill.getParent().getAttributeValue("def41");
			if (!StringUtils.isBlank(openUrl) && !StringUtils.isBlank(taskid)) {
				IPushBPMBillFileService informservice = 
						NCLocator.getInstance().lookup(IPushBPMBillFileService.class);
				NcToBpmVO bpmVO = new NcToBpmVO();
				bpmVO.setApprovaltype("query");
				bpmVO.setTaskid(taskid);
				informservice.pushBPMBillBackOrDelete(bpmVO);
				Desktop.getDesktop().browse(new URI(openUrl));
			}
		}
	}

}
