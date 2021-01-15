package nc.ui.tg.fischeme.action;
import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.IActionCode;
import nc.itf.uap.IVOPersistence;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.fischeme.FISchemeMapping;

public class EnAbleAction extends NCAction {
	private IModelDataManager dataManager;
	private BillManageModel model;
	private ShowUpableBillForm editor;
	private DisAbleAction disAbleAction;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2837980465984766930L;
	public EnAbleAction(){
		setBtnName("启用");
		setCode("EnAbleAction");
	}
	
	@Override
	public boolean isEnabled() {
		// TODO 自动生成的方法存根
		AggFIScemeHVO aggvo=(AggFIScemeHVO)getModel().getSelectedData();
	if(aggvo!=null){
			FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
			UFBoolean disable=hvo.getDisable();
			if(disable.booleanValue()){
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
			UFBoolean disable=hvo.getDisable();
			if(disable.booleanValue()){
				return true;
			}
		}
		return false;
	}
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
	/*	int i=MessageDialog.showOkCancelDlg((Container)e.getSource(), "停用融资方案", "确认是否继续停用该融资方案");
		if(i==1){
		
		if(aggvo==null){
			throw new BusinessException("请选中数据");
		}*/
		AggFIScemeHVO aggvo=(AggFIScemeHVO)getModel().getSelectedData();

		FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
		hvo.setDisable(UFBoolean.FALSE);
		IVOPersistence iVOPersistence=( IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
		iVOPersistence.updateObject(aggvo.getParent(), new FISchemeMapping());
		//this.editor.getBillCardPanel().getHeadItem("disable").setValue(UFBoolean.FALSE);
		
		getDisAbleAction().updateProjectData(aggvo, UFBoolean.FALSE);
		this.getModel().directlyUpdate(aggvo);
		this.isEnabled();
		this.isActionEnable();
		//this.getDataManager().refresh();
	}
	
	public BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
		this.model = model;
		this.model.addAppEventListener(this);	
	}
	public ShowUpableBillForm getEditor() {
		return editor;
	}
	public void setEditor(ShowUpableBillForm editor) {
		this.editor = editor;
	}
	
	
	public IModelDataManager getDataManager() {
		return this.dataManager;
	}

	public void setDataManager(IModelDataManager dataManager) {
		this.dataManager = dataManager;
	}
	public 	DisAbleAction getDisAbleAction(){
		if(disAbleAction==null)
			disAbleAction =new DisAbleAction();
		return disAbleAction;

	}
}
