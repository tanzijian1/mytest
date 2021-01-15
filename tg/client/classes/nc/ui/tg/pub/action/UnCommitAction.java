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
		//由于是收回前动作,所以这里的bill必定有值
		IBill bill = (IBill) getModel().getSelectedData();
		try {
			if("RZ06-Cxx-001".equals(bill.getParent().getAttributeValue("transtype"))){//财顾费
				getPushBPMBillFileService().pushRetrieveBpmFile(IBPMBillCont.PROCESSNAME_RZ06_01, (String) bill.getParent().getAttributeValue("def19"));
			}else if("RZ06-Cxx-002".equals(bill.getParent().getAttributeValue("transtype"))){//融资费
				getPushBPMBillFileService().pushRetrieveBpmFile(IBPMBillCont.PROCESSNAME_RZ06_02, (String) bill.getParent().getAttributeValue("def19"));
			}else if("RZ04".equals(bill.getParent().getAttributeValue("billtype"))){//按揭协议
				getPushBPMBillFileService().pushRetrieveBpmFile(IBPMBillCont.PROCESSNAME_RZ04 ,(String) bill.getParent().getAttributeValue("def19"));
			}else{//补票单
				getPushBPMBillFileService().pushRetrieveBpmFile(ISaleBPMBillCont.BILLNAME_19 ,(String) bill.getParent().getAttributeValue("def19"));
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
	}

	/**
	 * 后台推送bpm接口
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
