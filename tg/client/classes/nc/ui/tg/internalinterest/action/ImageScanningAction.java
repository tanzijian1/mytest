package nc.ui.tg.internalinterest.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.image.IGuoXinImage;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Internalinterest;

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
		AggInternalinterest aggInternalinterest =   (AggInternalinterest) model.getSelectedData();
		Internalinterest invoiceBillVO = aggInternalinterest.getParentVO();
		String barcode = invoiceBillVO.getPk_internal()+invoiceBillVO.getBilltype();
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
