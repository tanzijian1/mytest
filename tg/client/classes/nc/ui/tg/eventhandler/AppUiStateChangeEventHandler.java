/**
 * <p>Title: AppUiStateChangeEventHandler.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��23�� ����2:06:27

 * @version 1.0
 */   

package nc.ui.tg.eventhandler;   

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.AppUiStateChangeEvent;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.uif2.UIState;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;

/**
 * ����ʱ�䣺2019��9��23�� ����2:06:27  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * �ļ����ƣ�AppUiStateChangeEventHandler.java  
 * ��˵����  
 */

/**
 * <p>Title: AppUiStateChangeEventHandler<��p>

 * <p>Description: <��p>

 * <p>Company: hanzhi<��p> 

 * @author bobo

 * @date 2019��9��23�� ����2:06:27
 */

public class AppUiStateChangeEventHandler implements IAppEventHandler<AppUiStateChangeEvent>{
	
	private ShowUpableBillForm billForm;
	
	/* (non-Javadoc)
	 * <p>Title: handleAppEvent<��p>
	 * <p>Description: <��p>
	 * @param e
	 * @see nc.ui.pubapp.uif2app.event.IAppEventHandler#handleAppEvent(nc.ui.uif2.AppEvent)
	 */
	@Override
	public void handleAppEvent(AppUiStateChangeEvent e) {
		UIState uiState = e.getNewState();
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		// *** ����͸���״̬ʱ��֯�ɱ༭���ģ�����״̬�²������޸���֯
		if(uiState == UIState.ADD || uiState == UIState.COPY_ADD){
			billCardPanel.getHeadItem("pk_org").setEdit(true);
		}else{
			billCardPanel.getHeadItem("pk_org").setEdit(false);
		}
		// *** ����͸���״̬ʱ��֯�ɱ༭���ģ�����״̬�²������޸���֯
	}

	/**
	 * @return billForm
	 */
	public ShowUpableBillForm getBillForm() {
		return billForm;
	}

	/**
	 * @param billForm Ҫ���õ� billForm
	 */
	public void setBillForm(ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}
	
}
  