package nc.ui.tg.singleissue.action;

import java.awt.event.ActionEvent;

import nc.ui.tg.singleissue.util.SingleIssueUtil;
import nc.util.SdfnUtil;
import nc.vo.pub.BusinessException;
/**
 * ��������а�ť
 * @author wenjie
 *
 */
public class SingleissueBodyInsertAction extends nc.ui.pubapp.uif2app.actions.BodyInsertLineAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 787030340632882224L;
	
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO �Զ����ɵķ������
		String tableCode = getCardPanel().getBodyPanel().getTableCode();
		String value = (String)getCardPanel().getHeadItem("def1").getValueObject();
		String busiType = SingleIssueUtil.getBusiType(value);
		if(busiType==null &&("pk_bondresale".equals(tableCode) || "pk_repayplan".equals(tableCode)
				|| "pk_cyclebuying".equals(tableCode) || "pk_absrepay".equals(tableCode))) 
			throw new BusinessException("���������ֶ�Ϊ�գ���ǰ���岻�ɱ༭");
		
		if(SdfnUtil.getABSList().contains(busiType) && "pk_bondresale".equals(tableCode)){
			throw new BusinessException("�������ĵ�ҵ������ΪABS����ծȯ���ۡ����ɱ༭");
		}
		
		if(!SdfnUtil.getABSList().contains(busiType) && "pk_repayplan".equals(tableCode)){
			throw new BusinessException("�������ĵ�ҵ������Ϊծȯ��������ƻ������ɱ༭");
		}
		if("pk_cyclebuying".equals(tableCode) && "��Ӧ��ABS".equals(busiType)){
			throw new BusinessException("�������ĵ�ҵ������Ϊ��Ӧ��ABS����ѭ������ƻ������ɱ༭");
		}
		if("pk_absrepay".equals(tableCode) && "����β��ABS".equals(busiType)){
			throw new BusinessException("�������ĵ�ҵ������Ϊ����β��ABS������Ӧ��ABS����ƻ������ɱ༭");
		}
		if("pk_bondresale".equals(tableCode)){
			int rowCount = getCardPanel().getBillModel("pk_bondresale").getRowCount();
			if(rowCount==0){
				super.doAction(e);
			}
		}else{
			super.doAction(e);
		}
		if("pk_repayplan".equals(tableCode)){
			//��д�������
			int rowCount = getCardPanel().getBillModel(tableCode).getRowCount();
			String no = null;
			for (int i = 0; i < rowCount; i++) {
				no = SingleIssueUtil.getRepaymentNo(no);
				getCardPanel().getBillModel(tableCode).setValueAt(no, i, "def1");
			}
		}
	}
	
}
