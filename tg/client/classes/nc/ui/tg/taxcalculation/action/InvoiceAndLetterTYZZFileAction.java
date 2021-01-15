package nc.ui.tg.taxcalculation.action;

import java.awt.Container;
import java.awt.event.ActionEvent;

import org.apache.axis.utils.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.action.INCAction;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.tg.taxcalculation.ace.pub.yxfj.SFJTYxFileDLG;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.IEditor;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 邻里-税费计提 影像附件上传按钮
 * @author zhaozhiying
 *
 */
public class InvoiceAndLetterTYZZFileAction extends NCAction{
	
	private static final long serialVersionUID = 1L;

	private BillManageModel model;

	private Container container;

	private IEditor editor;

	public IEditor getEditor() {
		return editor;
	}

	public void setEditor(IEditor editor) {
		this.editor = editor;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}
	
	public InvoiceAndLetterTYZZFileAction() {
		super();
		setBtnName("影像附件上传");
		putValue(INCAction.CODE, "linkdeal");
	}

	@Override
	public void doAction(ActionEvent e) throws BusinessException {
		try {
			AggregatedValueObject aggVO = (AggregatedValueObject) getModel().getSelectedData();
			//LinkBillDealDlg dlg = new LinkBillDealDlg(container,aggVO);
			validateBarcode(aggVO);
			validateUploadImageFileBill(aggVO);
			SFJTYxFileDLG dlg = new SFJTYxFileDLG(container,aggVO);
			dlg.setReset(true);
			dlg.showModal();
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new BusinessException(e1.getMessage(),e1);
		}
	}
	
	/**
	 * 校验单据是否已经推送影像代办并且存储了影像编码
	 * 
	 * @param billVO
	 * @throws BusinessException
	 */
	private void validateBarcode(AggregatedValueObject aggvo) throws BusinessException {
		// 影像编码
		String barcode = (String) aggvo.getParentVO().getAttributeValue(getImageNoField(aggvo));
		if (StringUtils.isEmpty(barcode)) {
			throw new BusinessException("上传影像附件失败：单据没有影像编码关联影像系统无法上传附件");
		}
	}
	
	/**
	 * 获取影像编码字段
	 * @param aggvo
	 * @return
	 * @throws BusinessException 
	 */
	private String getImageNoField(AggregatedValueObject aggvo) throws BusinessException{
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue("transtype");
		IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String imageNoField = null;
		String sql = "select IMAGECODEFIELD from NCRELINST where PK_TRADETYPE = '"+pk_tradetype+"'";
		try {
			imageNoField = (String) service.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			throw new BusinessException("获取交易类型【"+pk_tradetype+"】影像编码字段异常：" + e.getMessage(), e);
		}
		if(StringUtils.isEmpty(imageNoField)){
			throw new BusinessException("获取交易类型【"+pk_tradetype+"】影像编码字段为空，请检查交易类型是否配置了影像编码字段【NCRELINST】");
		}
		return imageNoField;
	}
	
	/**
	 * 判断是否是需要上传影像附件的交易类型单据
	 * @param aggvo
	 * @return
	 * @throws BusinessException 
	 */
	private void validateUploadImageFileBill(AggregatedValueObject aggvo) throws BusinessException{
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue("transtype");
		// 邻里-税费计提
		if("ZZGD-Cxx-LL02".equals(pk_tradetype)){
			return ;
		}
		throw new BusinessException("上传影像附件失败：单据交易类型【"+pk_tradetype+"】不在影像附件上传业务范围");
	}
	
}
