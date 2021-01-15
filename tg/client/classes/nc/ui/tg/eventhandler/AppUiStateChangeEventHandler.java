/**
 * <p>Title: AppUiStateChangeEventHandler.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月23日 下午2:06:27

 * @version 1.0
 */   

package nc.ui.tg.eventhandler;   

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.AppUiStateChangeEvent;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.uif2.UIState;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;

/**
 * 创建时间：2019年9月23日 下午2:06:27  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：AppUiStateChangeEventHandler.java  
 * 类说明：  
 */

/**
 * <p>Title: AppUiStateChangeEventHandler<／p>

 * <p>Description: <／p>

 * <p>Company: hanzhi<／p> 

 * @author bobo

 * @date 2019年9月23日 下午2:06:27
 */

public class AppUiStateChangeEventHandler implements IAppEventHandler<AppUiStateChangeEvent>{
	
	private ShowUpableBillForm billForm;
	
	/* (non-Javadoc)
	 * <p>Title: handleAppEvent<／p>
	 * <p>Description: <／p>
	 * @param e
	 * @see nc.ui.pubapp.uif2app.event.IAppEventHandler#handleAppEvent(nc.ui.uif2.AppEvent)
	 */
	@Override
	public void handleAppEvent(AppUiStateChangeEvent e) {
		UIState uiState = e.getNewState();
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		// *** 保存和复制状态时组织可编辑更改，其他状态下不可以修改组织
		if(uiState == UIState.ADD || uiState == UIState.COPY_ADD){
			billCardPanel.getHeadItem("pk_org").setEdit(true);
		}else{
			billCardPanel.getHeadItem("pk_org").setEdit(false);
		}
		// *** 保存和复制状态时组织可编辑更改，其他状态下不可以修改组织
	}

	/**
	 * @return billForm
	 */
	public ShowUpableBillForm getBillForm() {
		return billForm;
	}

	/**
	 * @param billForm 要设置的 billForm
	 */
	public void setBillForm(ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}
	
}
  