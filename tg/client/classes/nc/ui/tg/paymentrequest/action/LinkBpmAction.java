package nc.ui.tg.paymentrequest.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.OutsideUtils;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.bpm.NcToBpmVO;
/**
 * �������뵥
 * @author ln
 *
 */
public class LinkBpmAction extends NCAction{
	private static final long serialVersionUID = -7110064408625514231L;

	public LinkBpmAction() {
		setBtnName("����BPM");
		setCode("linkBpmAction");
	}

	private BillManageModel model;

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO �Զ����ɵķ������
		Object obj = getModel().getSelectedData();
		if (obj == null)
			throw new BusinessException("δѡ������");
		AggregatedValueObject aggvo = (AggregatedValueObject) obj;

		String taskid = (String) aggvo.getParentVO()
				.getAttributeValue("bpmid");
		if (StringUtils.isBlank(taskid))
			throw new BusinessException("BPM����Ϊ��!");
		String address = OutsideUtils.getOutsideInfo("BPM");
		String url = address
				+ "/YZSoft/Forms/Read.aspx?tid="+taskid+"";
		if (!StringUtils.isBlank(address)) {
			IPushBPMBillFileService informservice = 
					NCLocator.getInstance().lookup(IPushBPMBillFileService.class);
			NcToBpmVO bpmVO = new NcToBpmVO();
			bpmVO.setApprovaltype("query");
			bpmVO.setTaskid(taskid);
			informservice.pushBPMBillBackOrDelete(bpmVO);
			Desktop.getDesktop().browse(new URI(url));
		}
	}
}
