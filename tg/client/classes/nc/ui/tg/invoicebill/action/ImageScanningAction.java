package nc.ui.tg.invoicebill.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.impl.tg.image.GuoXinImageImpl;
import nc.itf.tg.image.IGuoXinImage;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.tg.invoicebill.InvoiceBillVO;

public class ImageScanningAction extends NCAction{
	private static final long serialVersionUID = 944250971694099894L;
	
	private BillManageModel model = null;

	
	public ImageScanningAction(){
		setCode("ImageScanningAction");
		setBtnName("影像查看");
	}
	
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		AggInvoiceBillVO aggInvoiceBillVO =   (AggInvoiceBillVO) model.getSelectedData();
		InvoiceBillVO invoiceBillVO = aggInvoiceBillVO.getParentVO();
		String barcode = invoiceBillVO.getPk_invoicebill_h()+invoiceBillVO.getBilltype();
		IGuoXinImage iGuoXinImage =  NCLocator.getInstance().lookup(IGuoXinImage.class);
		String url = iGuoXinImage.createImagePath(barcode);
		Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+url);
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}

	
	
}
