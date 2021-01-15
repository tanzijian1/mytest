package nc.ui.tg.fischeme.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.IFishchemeChange;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.vo.pub.BusinessException;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;
import nc.vo.tg.projectdata.ProjectDataVVO;
import nc.ui.pubapp.uif2app.model.BillManageModel ;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;

public class FischmChangeAction  extends FischemeSaveAction{
	private BillManageModel model;
	private ShowUpableBillForm editor;
	private IModelDataManager dataManager;


	/**
	 * 
	 */
	private static final long serialVersionUID = -2837980465984766930L;
	public FischmChangeAction(){
	setBtnName("变更");
	setCode("FischmChangeAction");
}
	public nc.ui.pubapp.uif2app.model.BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
		this.model = model;
	}
	public ShowUpableBillForm getEditor() {
		return editor;
	}
	public void setEditor(ShowUpableBillForm editor) {
		this.editor = editor;
	}
	
	public IModelDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IModelDataManager dataManager) {
		this.dataManager = dataManager;
	}
	@Override
	public boolean isEnabled() {
		// TODO 自动生成的方法存根
		AggFIScemeHVO aggvo=(AggFIScemeHVO)getModel().getSelectedData();
		if(aggvo!=null){
			FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
			int status=hvo.getApprovestatus();
			if(status==1){
				return true;
			}
		}
		return false;
	}
	@Override
	protected boolean isActionEnable() {
		// TODO 自动生成的方法存根
		AggFIScemeHVO aggvo=(AggFIScemeHVO)getModel().getSelectedData();
		if(aggvo!=null){
			FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
			int status=hvo.getApprovestatus();
			
		}
		return false ;
	}
	public void doAction(ActionEvent e) throws Exception {

		// TODO 自动生成的方法存根
		int i=MessageDialog.showOkCancelDlg((Container)e.getSource(), "变更确认", "确认是否继续变更");
		if(i==1){
		AggFIScemeHVO aggvo=(AggFIScemeHVO)getModel().getSelectedData();
		if(aggvo==null){
			throw new BusinessException("请选中数据");
		}
		
		getModel().setUiState(UIState.EDIT);
		FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
         //this.editor.getBillCardPanel().getHeadItem("approvestatus").setEdit(false);
 		hvo.setAttributeValue("vdef10", "Y");
		((nc.ui.pubapp.uif2app.view.ShowUpableBillForm) editor)
				.getBillCardPanel().getHeadItem("vdef10").setValue("Y");
		
	}
}
}
