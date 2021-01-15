package nc.ui.tg.pub.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.OutsideUtils;
import nc.ui.uif2.NCAction;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.outside.bpm.NcToBpmVO;

import org.apache.commons.lang.StringUtils;

public class OpenBPMViewAction extends NCAction {
	nc.ui.pubapp.uif2app.model.BillManageModel model = null;

	public OpenBPMViewAction() {
		super();
		setCode("openBPMView");
		setBtnName("�鿴BPM����");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		if(getModel().getSelectedData()==null){
			throw new BusinessException("��ѡ��һ�����ݼ�¼");
		}
		IBill bill = (IBill) getModel().getSelectedData();
		String address = OutsideUtils.getOutsideInfo("BPM");//�Զ��嵵��ά����ַ
		if(bill!=null&&StringUtils.isNotBlank(address)){
			String bpmid = null;
			String urlRePay = null;
			if(bill instanceof AggRePayReceiptBankCreditVO|| bill instanceof nc.vo.cdm.applybankcredit.AggApplyBankCreditVO){
				bpmid = (String) bill.getParent().getAttributeValue("vdef19");
			}else if(bill instanceof AggFinancexpenseVO || bill instanceof AggAddTicket || bill instanceof nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO){
				bpmid = (String) bill.getParent().getAttributeValue("def19");
			}
			if(StringUtils.isBlank(bpmid))
				throw new BusinessException("BPM����Ϊ��!");
			urlRePay = address
					+ "/YZSoft/Forms/Read.aspx?tid="+bpmid+"";
			if (!StringUtils.isBlank(bpmid)){
				IPushBPMBillFileService informservice = 
				NCLocator.getInstance().lookup(IPushBPMBillFileService.class);
				NcToBpmVO bpmVO = new NcToBpmVO();
				bpmVO.setApprovaltype("query");
				bpmVO.setTaskid(bpmid);
				informservice.pushBPMBillBackOrDelete(bpmVO);
				Desktop.getDesktop().browse(new URI(urlRePay));
			}
		}

	}

	public nc.ui.pubapp.uif2app.model.BillManageModel getModel() {
		return model;
	}

	public void setModel(nc.ui.pubapp.uif2app.model.BillManageModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}

	protected boolean isActionEnable() {
		if (getModel().getSelectedOperaDatas() == null
				|| (getModel().getSelectedOperaDatas() != null && getModel()
						.getSelectedOperaDatas().length > 1)) {
			return false;
		}

		return true;
	}

}
