package nc.ui.tg.distribution.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.image.IGuoXinImage;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.vo.tgfn.distribution.Distribution;

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
		AggDistribution aggDistribution = (AggDistribution) model.getSelectedData();
		Distribution distribution = aggDistribution.getParentVO();
		String barcode = distribution.getDef3();
		String saleID = distribution.getDef1();
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
