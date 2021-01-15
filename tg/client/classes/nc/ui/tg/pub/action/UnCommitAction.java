package nc.ui.tg.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.IBPMBillCont;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class UnCommitAction extends nc.ui.pubapp.uif2app.actions.pflow.UnCommitScriptAction{

	IPushBPMBillFileService fileService = null;
	@Override
	protected void beforeDoAction() {
		super.beforeDoAction();
		//�������ջ�ǰ����,���������bill�ض���ֵ
		IBill bill = (IBill) getModel().getSelectedData();
		try {
			if("RZ06-Cxx-001".equals(bill.getParent().getAttributeValue("transtype"))){//�ƹ˷�
				getPushBPMBillFileService().pushRetrieveBpmFile(IBPMBillCont.PROCESSNAME_RZ06_01, (String) bill.getParent().getAttributeValue("def19"));
			}else if("RZ06-Cxx-002".equals(bill.getParent().getAttributeValue("transtype"))){//���ʷ�
				getPushBPMBillFileService().pushRetrieveBpmFile(IBPMBillCont.PROCESSNAME_RZ06_02, (String) bill.getParent().getAttributeValue("def19"));
			}else if("RZ04".equals(bill.getParent().getAttributeValue("billtype"))){//����Э��
				getPushBPMBillFileService().pushRetrieveBpmFile(IBPMBillCont.PROCESSNAME_RZ04 ,(String) bill.getParent().getAttributeValue("def19"));
			}else{//��Ʊ��
				getPushBPMBillFileService().pushRetrieveBpmFile(ISaleBPMBillCont.BILLNAME_19 ,(String) bill.getParent().getAttributeValue("def19"));
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
	}

	/**
	 * ��̨����bpm�ӿ�
	 * @return
	 */
	public IPushBPMBillFileService getPushBPMBillFileService() {
		if (fileService == null) {
			fileService = NCLocator.getInstance().lookup(
					IPushBPMBillFileService.class);
		}
		return fileService;
	}
}
