package nc.ui.tg.singleissue.action;

import java.awt.event.ActionEvent;

import nc.ui.tg.singleissue.util.SingleIssueUtil;
import nc.util.SdfnUtil;
import nc.vo.pub.BusinessException;

public class SingleissueBodyDelLineAction extends nc.ui.pubapp.uif2app.actions.BodyDelLineAction{

	@Override
	public void doAction(ActionEvent e) throws Exception {
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
		super.doAction(e);
	}
	
}
