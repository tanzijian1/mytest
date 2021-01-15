package nc.ui.tg.fischeme.action;

import java.awt.Container;
import java.awt.event.ActionEvent;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FIScemeHVO;

public class FischemeSaveAction extends
		nc.ui.pubapp.uif2app.actions.pflow.SaveScriptAction {
	private IModelDataManager dataManager;

	public IModelDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IModelDataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO �Զ����ɵķ������
		AggFIScemeHVO aggvo = (AggFIScemeHVO) getModel().getSelectedData();
		if (aggvo != null) {
			FIScemeHVO hvo = (FIScemeHVO) aggvo.getParent();
//			int flag = 2;

			if (hvo.getApprovestatus() == 1) {
//				flag = MessageDialog.showOkCancelDlg((Container) e.getSource(),
//						"ȷ�ϰ�ť", "�Ƿ񸲸ǰ汾");
			}
//			if (flag == 2) {
				hvo.setAttributeValue("vdef10", "Y");
				((nc.ui.pubapp.uif2app.view.ShowUpableBillForm) editor)
						.getBillCardPanel().getHeadItem("vdef10").setValue("Y");
//			} else {
//				hvo.setAttributeValue("vdef10", "N");
//				((nc.ui.pubapp.uif2app.view.ShowUpableBillForm) editor)
//						.getBillCardPanel().getHeadItem("vdef10").setValue("N");
//			}
		}

		super.doAction(e);
		// if(flag==1){
		// IFishchemeChange
		// ip=NCLocator.getInstance().lookup(IFishchemeChange.class);
		//
		// AggFIScemeHVO
		// aggvoversion=(AggFIScemeHVO)getModel().getSelectedData();
		// ip.insertVertion(new AggFIScemeHVO[]{aggvoversion});
//		getDataManager().refresh();
		// }
	}
}
