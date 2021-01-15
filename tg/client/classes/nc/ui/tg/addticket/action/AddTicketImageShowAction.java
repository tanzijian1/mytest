package nc.ui.tg.addticket.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.imag.scan.action.BaseImageShowAction;
import nc.itf.tg.image.IGuoXinImage;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class AddTicketImageShowAction extends NCAction {

	private static final long serialVersionUID = 1L;
	BillManageModel model;

	public AddTicketImageShowAction() {
		setCode("AddTicketImageImageShow");
		setBtnName(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"arapcommonv6-0173"));
	}

	public void doAction(ActionEvent e) throws Exception {

		// TODO ModifiedBy ln 2019年11月14日10:31:49 注释原代码逻辑
		// showDocument();
		AggregatedValueObject aggvo = (AggregatedValueObject) getModel().getSelectedData();
		Object barcode = aggvo.getParentVO().getAttributeValue("def21");
		if (barcode == null || "".equals(barcode)) {
			throw new BusinessException("外部系统的单据，影像编码不能空！");
		}
		IGuoXinImage iGuoXinImage = NCLocator.getInstance().lookup(
				IGuoXinImage.class);
		String url = iGuoXinImage.createImagePath(String.valueOf(barcode));
		Runtime.getRuntime()
				.exec("rundll32 url.dll,FileProtocolHandler " + url);
	}

	protected boolean isActionEnable() {
		if ((model.getUiState() == UIState.ADD)
				|| (model.getUiState() == UIState.COPY_ADD)
				|| (model.getUiState() == UIState.EDIT)) {
			return false;
		}
		if (model.getSelectedData() == null)
			return false;
		return true;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}
}
