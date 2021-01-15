package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.bpm.push.FinancexpenseUtils;
import nc.itf.tg.bpm.IPushSaleBillToBpm;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.uap.pf.IPFBusiAction;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

/**
 * ��������bpm��ť��(����û������)
 * @author acer
 *
 */
public class FinancePushBPMAction extends NCAction{

	private static final long serialVersionUID = 1L;

	private BillForm editor;

	private BillListView listView;

	private BillManageModel model;
	
	public static final String FIRST = "һ��"; // һ��
	public static final String SECOND = "����";// ����
	public static final String THIRD = "����";// ����
	IPushBPMBillFileService fileService = null;
	
	@Override
	protected boolean isActionEnable() {
		if (editor.getBillCardPanel() != null && editor.getBillCardPanel().isShowing()) {
			return true;
		}
		if (getModel().getSelectedData() != null) {
			return true;
		}
		
		return false;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		if(getModel().getSelectedData()==null){
			throw new BusinessException("��ѡ��һ�����ݼ�¼");
		}
		IPushSaleBillToBpm service = NCLocator.getInstance().lookup(IPushSaleBillToBpm.class);
		IBill bill = (IBill) getModel().getSelectedData();
		Object object = null;
//		if("36H2030101".equals(getModel().getContext().getNodeCode())){//�ƹ˷����
//			AggFinancexpenseVO aggvo = (AggFinancexpenseVO) doCgfAction(service,bill);
//			object = getPfBusiAction().processAction(IPFActionName.SAVE, "F3",
//					null, aggvo, null, null);
//		}else if("36H2030101000".equals(getModel().getContext().getNodeCode())){//���ʷ���
//			AggFinancexpenseVO aggvo = (AggFinancexpenseVO) doFinAction(service,bill);
//			object = getPfBusiAction().processAction(IPFActionName.SAVE, "F3",
//					null, aggvo, null, null);
//		}else if("36150BCRR".equals(getModel().getContext().getNodeCode())){//������Ϣ
//			AggRePayReceiptBankCreditVO aggvo = (AggRePayReceiptBankCreditVO) doRepayAction(service,bill);
//			object = getPfBusiAction().processAction(IPFActionName.SAVE, "F3",
//					null, aggvo, null, null);
//		}
		
		getModel().directlyUpdate(object);
		MessageDialog.showWarningDlg(getEditor(), "��ʾ", "�ɹ��ύbpm");
	}
	
//	/**
//	 * ����/��Ϣ
//	 * @param service
//	 * @param bill
//	 * @return 
//	 * @throws Exception 
//	 */
//	private IBill doRepayAction(IPushSaleBillToBpm service, IBill bill) throws Exception {
//		AggRePayReceiptBankCreditVO aggVO = (AggRePayReceiptBankCreditVO) bill;
//		if (aggVO.getParentVO().getRepayamount() == null
//				&& (aggVO.getParentVO().getInterest() != null||aggVO.getParentVO().getAttributeValue("preinterestmoney")!=null)) {// ��Ϣ
//			return bill = RePayReceiptBankCreditChangeUtils.getUtils().onPushBillToBPM(ISaleBPMBillCont.BILL_16,
//					(AbstractBill) bill);
//		}else{
//			return bill = RePayReceiptBankCreditChangeUtils.getUtils().onPushBillToBPM(ISaleBPMBillCont.BILL_15,
//					(AbstractBill) bill);
//		}
//		
//	}
//
//	/**
//	 * ���ʷ���
//	 * @param service
//	 * @param bill
//	 * @return 
//	 * @throws Exception 
//	 */
//	private IBill doFinAction(IPushSaleBillToBpm service, IBill bill) throws Exception {
//		return bill = FinancexpenseChangeUtils.getUtils().onPushBillToBPM(ISaleBPMBillCont.BILL_18,
//				(AbstractBill) bill);
//	}

//	/**
//	 * �ƹ˷����
//	 * @param service
//	 * @param bill
//	 * @return 
//	 * @throws Exception 
//	 */
//	private IBill doCgfAction(IPushSaleBillToBpm service, IBill bill) throws Exception {
//		return bill = FinancexpenseChangeUtils.getUtils().onPushBillToBPM(ISaleBPMBillCont.BILL_17,
//				(AbstractBill) bill);
//	}
	
	IPFBusiAction pfBusiAction = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	
	public FinancePushBPMAction() {
		super();
		setCode("financePushBPMAction");
		setBtnName("����BPM");
	}
	
	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public BillManageModel getModel() {
		return model;
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public BillListView getListView() {
		return listView;
	}

	public void setListView(BillListView listView) {
		this.listView = listView;
	}
}
