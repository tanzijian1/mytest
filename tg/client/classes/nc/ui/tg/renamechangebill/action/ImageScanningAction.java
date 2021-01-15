package nc.ui.tg.renamechangebill.action;

import java.awt.event.ActionEvent;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.image.IGuoXinImage;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.tgfn.renamechangebill.RenameChangeBillHVO;

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
		AggRenameChangeBillHVO aggRenameChangeBillHVO = (AggRenameChangeBillHVO) model.getSelectedData();
		RenameChangeBillHVO renameChangeBillHVO = aggRenameChangeBillHVO.getParentVO();
		String barcode = renameChangeBillHVO.getDef3();
		String saleID = renameChangeBillHVO.getDef1();
		if(saleID == null || "".equals(saleID)){
			return;
		}
		if(barcode == null || "".equals(barcode)){
			throw new BusinessException("�ⲿϵͳ�ĵ��ݣ�Ӱ����벻�ܿգ�");
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
