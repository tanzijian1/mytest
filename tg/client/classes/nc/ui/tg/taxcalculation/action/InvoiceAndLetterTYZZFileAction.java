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
 * ����-˰�Ѽ��� Ӱ�񸽼��ϴ���ť
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
		setBtnName("Ӱ�񸽼��ϴ�");
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
	 * У�鵥���Ƿ��Ѿ�����Ӱ����첢�Ҵ洢��Ӱ�����
	 * 
	 * @param billVO
	 * @throws BusinessException
	 */
	private void validateBarcode(AggregatedValueObject aggvo) throws BusinessException {
		// Ӱ�����
		String barcode = (String) aggvo.getParentVO().getAttributeValue(getImageNoField(aggvo));
		if (StringUtils.isEmpty(barcode)) {
			throw new BusinessException("�ϴ�Ӱ�񸽼�ʧ�ܣ�����û��Ӱ��������Ӱ��ϵͳ�޷��ϴ�����");
		}
	}
	
	/**
	 * ��ȡӰ������ֶ�
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
			throw new BusinessException("��ȡ�������͡�"+pk_tradetype+"��Ӱ������ֶ��쳣��" + e.getMessage(), e);
		}
		if(StringUtils.isEmpty(imageNoField)){
			throw new BusinessException("��ȡ�������͡�"+pk_tradetype+"��Ӱ������ֶ�Ϊ�գ����齻�������Ƿ�������Ӱ������ֶΡ�NCRELINST��");
		}
		return imageNoField;
	}
	
	/**
	 * �ж��Ƿ�����Ҫ�ϴ�Ӱ�񸽼��Ľ������͵���
	 * @param aggvo
	 * @return
	 * @throws BusinessException 
	 */
	private void validateUploadImageFileBill(AggregatedValueObject aggvo) throws BusinessException{
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue("transtype");
		// ����-˰�Ѽ���
		if("ZZGD-Cxx-LL02".equals(pk_tradetype)){
			return ;
		}
		throw new BusinessException("�ϴ�Ӱ�񸽼�ʧ�ܣ����ݽ������͡�"+pk_tradetype+"������Ӱ�񸽼��ϴ�ҵ��Χ");
	}
	
}
