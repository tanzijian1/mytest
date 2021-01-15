package nc.ui.tg.tartingbill.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.image.IGuoXinImage;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.pub.BusinessException;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.vo.tg.tartingbill.TartingBillVO;

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
		AggTartingBillVO aggTartingBillVO = (AggTartingBillVO) model.getSelectedData();
		TartingBillVO tartingBillVO = aggTartingBillVO.getParentVO();
		String barcode = tartingBillVO.getDef3();
		String saleID = tartingBillVO.getDef1();
		if(saleID == null || "".equals(saleID)){
			return;
		}
		if(barcode == null || "".equals(barcode)){
			throw new BusinessException("外部系统的单据，影像编码不能空！");
		}
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
