package nc.ui.tg.changebill.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IUnsavebillAndDeleteBill;
import nc.itf.uap.pf.IPFBusiAction;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.tg.showpanel.ReturnMsgPanel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.changebill.AggChangeBillHVO;

public class BusinessBackAction extends NCAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessBackAction() {
		setBtnName("�˻���ҵϵͳ");
		setCode("BusinessBackAction");
	}

	ReturnMsgPanel msgPanel = null;

	private BillManageModel model;

	IPFBusiAction pfBusiAction = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object obj = getModel().getSelectedData();
		if (obj == null)
			throw new BusinessException("��ѡ������");

		boolean result = true;

		if ("F2-Cxx-SY001".equals(((AggChangeBillHVO) obj).getParentVO()
				.getAttributeValue("pk_tradetype"))) {
			try {
				NCLocator.getInstance().lookup(IUnsavebillAndDeleteBill.class)
						.BackAndDeleteGatherBill_RequiresNew(obj, "FPD");
			} catch (Exception e1) {
				result = false;
				MessageDialog.showHintDlg(getModel().getContext()
						.getEntranceUI(), "��ʾ", "�˻�ʧ�ܣ�" + e1.getMessage());
			}

			if (result) {
				MessageDialog.showHintDlg(getModel().getContext()
						.getEntranceUI(), "��ʾ", "�˻سɹ�");
			}
		} else {
			throw new BusinessException("������ҵϵͳ�ĵ���");
		}

	}

}
