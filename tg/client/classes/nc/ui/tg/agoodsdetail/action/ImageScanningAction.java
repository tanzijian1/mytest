package nc.ui.tg.agoodsdetail.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.image.IGuoXinImage;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.tgfn.agoodsdetail.AGoodsDetail;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;

public class ImageScanningAction extends NCAction{
	private static final long serialVersionUID = 944250971694099894L;
	
	private BillManageModel model = null;

	
	public ImageScanningAction(){
		setCode("ImageScanningAction");
		setBtnName("Ӱ��鿴");
	}
	
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO �Զ����ɵķ������
		AggAGoodsDetail aggAGoodsDetail = (AggAGoodsDetail) model.getSelectedData();
		AGoodsDetail aGoodsDetail = aggAGoodsDetail.getParentVO();
		String barcode = aGoodsDetail.getPk_agoodsdetail_h()+aGoodsDetail.getBilltype();
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
