package nc.ui.tg.fischeme.action;
import java.awt.event.ActionEvent;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.fischeme.FISchemeMapping;
import nc.vo.tg.projectdata.ProjectDataVVO;

public class DisAbleAction  extends NCAction{
	private IModelDataManager dataManager;
	private BillManageModel model;
	private ShowUpableBillForm editor;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2837980465984766930L;
		public DisAbleAction(){
			setBtnName("停用");
			setCode("DisAbleAction");
		}
	
		@Override
		public boolean isEnabled() {
			// TODO 自动生成的方法存根
			AggFIScemeHVO aggvo=(AggFIScemeHVO)getModel().getSelectedData();
		if(aggvo!=null){
				FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
				UFBoolean disable=hvo.getDisable();
				if(!disable.booleanValue()){
					return true;
				}
			}
			return false;
		}
		
	@Override
	protected boolean isActionEnable() {
		// TODO 自动生成的方法存根
		AggFIScemeHVO aggvo = (AggFIScemeHVO) getModel().getSelectedData();
		if (aggvo != null) {
			FIScemeHVO hvo = (FIScemeHVO) aggvo.getParent();
			UFBoolean disable = hvo.getDisable();
			if(!disable.booleanValue()){
				return true;
			}
		}
		return false;
	}

	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		/*
		 * int i=MessageDialog.showOkCancelDlg((Container)e.getSource(),
		 * "停用融资方案", "确认是否继续停用该融资方案"); if(i==1){
		 * 
		 * if(aggvo==null){ throw new BusinessException("请选中数据"); }
		 */
		AggFIScemeHVO aggvo = (AggFIScemeHVO) getModel().getSelectedData();
		
		FIScemeHVO hvo = (FIScemeHVO) aggvo.getParent();
		hvo.setDisable(UFBoolean.TRUE);
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		iVOPersistence.updateObject(aggvo.getParent(), new FISchemeMapping());
		updateProjectData(aggvo, UFBoolean.TRUE);
		this.getModel().directlyUpdate(aggvo);//直接把VO数据更新界面
		this.isEnabled();
		this.isActionEnable();
		//this.getDataManager().refresh();
		
	}
	protected  void updateProjectData(AggFIScemeHVO aggvo ,UFBoolean disable ) throws BusinessException{
			FIScemeHVO hvo = (FIScemeHVO) aggvo.getParent();
			FISchemeBVO[] bvos=(FISchemeBVO[])aggvo.getAllChildrenVO();
			String pk_project=hvo.getPk_project();
		String sql ="select * from tgrz_projectdata_v "
				+ " where pk_projectdata='"+pk_project+"' "; 
			IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			
			List<ProjectDataVVO> result= (List<ProjectDataVVO>) bs.executeQuery(sql, new BeanListProcessor(ProjectDataVVO.class));
			for(ProjectDataVVO vo:result){
				vo.setDisable(disable);
				 	
			}
			IVOPersistence update=NCLocator.getInstance().lookup(IVOPersistence.class);
			update.updateVOList(result);
		
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
	
	
}
