package nc.ui.tg.fischeme.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.tmpub.version.VersionDeleteBP;
import nc.bs.tmpub.version.VersionInsertBP;
import nc.funcnode.ui.AbstractFunclet;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletLinkEvent;
import nc.funcnode.ui.FuncletLinkListener;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.funcnode.ui.IFunclet;
import nc.funcnode.ui.IFuncletWindow;
import nc.itf.tg.outside.IFishchemeChange;
import nc.itf.tmpub.version.IVersionAggVO;
import nc.itf.uap.IVOPersistence;
import nc.md.persist.framework.IMDPersistenceService;
import nc.sfbase.client.ClientToolKit;
import nc.ui.pub.LinkEvent;
import nc.ui.pub.LinkListener;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.sm.power.FuncRegisterCacheAccessor;
import nc.ui.tmpub.version.listener.VersionRefreshDataListener;
import nc.ui.tmpub.version.model.VersionCardModel;
import nc.ui.tmpub.version.model.VersionFixDigitVO;
import nc.ui.tmpub.version.model.VersionLinkAddData;
import nc.ui.tmpub.version.model.VersionTreeModel;
import nc.ui.tmpub.version.view.VersionCardNodeKey;
import nc.ui.uif2.NCAction;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;
import nc.vo.tmpub.util.DialogTool;
import nc.vo.uif2.LoginContext;

public class VersionDeleteAction extends NCAction {
	/**
	 *
	 */
	private LoginContext context;
	public LoginContext getContext() {
		return context;
	}
	public void setContext(LoginContext context) {
		this.context = context;
	}
	private nc.ui.tmpub.version.view.VersionTreePanel treePanel;
	private nc.ui.tmpub.version.view.VersionCardEditor propEditor;
	
	public nc.ui.tmpub.version.view.VersionCardEditor getPropEditor() {
		return propEditor;
	}
	public void setPropEditor(nc.ui.tmpub.version.view.VersionCardEditor propEditor) {
		this.propEditor = propEditor;
	}
	public nc.ui.tmpub.version.view.VersionTreePanel getTreePanel() {
		return treePanel;
	}
	public void setTreePanel(nc.ui.tmpub.version.view.VersionTreePanel treePanel) {
		this.treePanel = treePanel;
	}
	private nc.ui.tmpub.version.model.VersionModelManager modelDataManager;
	public nc.ui.tmpub.version.model.VersionModelManager getModelDataManager() {
		return modelDataManager;
	}
	public void setModelDataManager(
			nc.ui.tmpub.version.model.VersionModelManager modelDataManager) {
		this.modelDataManager = modelDataManager;
	}
	public VersionDeleteAction(){
		setBtnName("删除");
		setCode("deleteVersion");
	}
	private static final long serialVersionUID = 4131619602418159534L;
	private VersionCardModel cardModel;
	private VersionTreeModel appModel;
	public VersionTreeModel getAppModel() {
		return appModel;
	}
	public void setAppModel(VersionTreeModel appModel) {
		this.appModel = appModel;
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
	int flag=	MessageDialog.showOkCancelDlg((Container)e.getSource(), "确认", "是否确认删除");
	if(flag==1){
	if(this.appModel.getSelectedData() instanceof AggFIScemeHVO){
		Object[] obj=this.appModel.getAllDatas();
		int i=0;
		Object selectobj=this.appModel.getSelectedData();
		Object[] refreshobj=new Object[obj.length];
		AggFIScemeHVO aggvo=(nc.vo.tg.fischeme.AggFIScemeHVO)this.appModel.getSelectedData();
		for(Object o:obj){
			if(!(o==selectobj)){
				refreshobj[i]=o;
				i++;
			}
//			AggFIScemeHVO aggvo_temp=(AggFIScemeHVO)o;
//			if(!(aggvo_temp.getParent().getAttributeValue("pk_fiscemeversion").equals(aggvo.getParent().getAttributeValue("pk_fiscemeversion")))){
//				refreshobj[i]=o;
//				i++;
//			}
		}
		IFishchemeChange toversiontool=	(IFishchemeChange)NCLocator.getInstance().lookup(IFishchemeChange.class.getName());
		AggFISchemeversionHVO aggvo_version=toversiontool.deleteVersionvo(aggvo);
		IFuncletWindow c = (IFuncletWindow)(context.getEntranceUI().getRootPane().getParent());
        c.closeWindow();
//		treePanel.initUI();
//		propEditor.initUI();
		
//for(Object o:refreshobj){
//	if(o!=null){
//		get(o);
//		return;
//	}
//}

	IMDPersistenceService iMDPersistenceService=(IMDPersistenceService)NCLocator.getInstance().lookup(IMDPersistenceService.class.getName());
//	iMDPersistenceService.deleteBill(aggvo_version);
//	IVOPersistence iVOPersistence=( IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
//	iVOPersistence.deleteVO(aggvo.getParentVO());
//	if(aggvo.getChildrenVO()!=null){
//		for(CircularlyAccessibleValueObject bvo:aggvo.getChildrenVO()){
//			iVOPersistence.deleteVO((FISchemeBVO) bvo);
//		}
//	}
	}
	}
}
	public VersionCardModel getCardModel() {
		return cardModel;
	}
	public void setCardModel(VersionCardModel cardModel) {
		this.cardModel = cardModel;
	}

	private static class LinkAdapter implements FuncletLinkListener{
		private LinkListener listener = null;
		
	public LinkAdapter(LinkListener listener) {
			super();
			this.listener = listener;
	}
	@Override
	public void dealLinkEvent(FuncletLinkEvent event) {
		IFunclet source = (IFunclet)event.getSource();
		if(source instanceof ToftPanel){
			ToftPanel tp = (ToftPanel)source;
			LinkEvent e = new LinkEvent(tp,event.getOperateID(),event.getSource());
			listener.dealLinkEvent(e);
		}
		
	}
		
	}
	public final static String FUNCODE = "36010VER";
	public void get(Object obj){
		
		FuncRegisterVO frVO = FuncRegisterCacheAccessor.getInstance().getFuncRegisterVOByFunCode(FUNCODE);;
        if(frVO != null){
            VersionRefreshDataListener versionDataListener = new VersionRefreshDataListener();
            VersionLinkAddData data = new VersionLinkAddData();
            data.setUserObject(obj);
            data.setVersionQueryPk(null);
            data.setCondition(null);
            data.setSrcDestItemCollection(null);
//            if(fixDigit != null){
//            	List<VersionFixDigitVO> digitList = fixDigit.getFixDigit();
//            	if(digitList != null && !digitList.isEmpty()){
//            		data.setDigitList(digitList);
//            	}
//            }
            if(obj == null){
            	MessageDialog.showErrorDlg(ClientToolKit.getApplet(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3601tmpub_0","03601tmpub-0024")/*@res "错误"*/ , nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3601tmpub_0","03601tmpub-0097")/*@res "请选择一条记录"*/);
            	return;
            }
            VersionCardNodeKey.setNodeKey("");
           // FuncNodeStarter.openLinkedDialog(frVO, ILinkType.LINK_TYPE_ADD, data, this.getEditor(), true, false, versionDataListener);
            FuncletWindowLauncher.openFuncNodeFrame((AbstractFunclet) getAppModel().getContext().getEntranceUI(),frVO, new FuncletInitData(ILinkType.LINK_TYPE_QUERY,data),new LinkAdapter(versionDataListener),true,DialogTool.getWindowOfScreenCenter());
        } else {
            MessageDialog.showErrorDlg(ClientToolKit.getApplet(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3601tmpub_0","03601tmpub-0024")/*@res "错误"*/ , nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3601tmpub_0","03601tmpub-0188")/*@res "没有打开此节点的权限 . 节点号="*/ + "36010VER");
        }
	}
}
