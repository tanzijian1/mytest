package nc.bs.tg.addticket.ace.rule;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.image.IGuoXinImage;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.addticket.AddTicket;
import nc.vo.tg.addticket.AggAddTicket;

public class AddTicketUnsaveDeleteBarcodeRule implements IRule<AggAddTicket>{

	@Override
	public void process(AggAddTicket[] vos) {
		IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		// TODO �Զ����ɵķ������
		String remark="";//�˵����� 
		String optype="2";//�������ͣ�1/���ϴ���(ֻ��״̬)��2/ɾ�����ݣ�Ĭ��1��
		try {
		for(AggAddTicket vo:vos){
			AddTicket hvo=vo.getParentVO();
			if(!("Y".equals(hvo.getAttributeValue("def47")))){//def47��ʶ�Ƿ�bpm�ջ�
				
				//TODO20201016--���Ӱ������Ӱ��ɾ���ӿ�
				IGuoXinImage image=NCLocator.getInstance().lookup(IGuoXinImage.class);
				if(StringUtils.isNotBlank((String) vo.getParent().getAttributeValue("def21"))){
					 String	username = (String)bs.executeQuery("select user_name  from sm_user where cuserid='"+InvocationInfoProxy.getInstance().getUserId()+"'", new ColumnProcessor());
					
					image.delrefund((String) vo.getParent().getAttributeValue("def21"), "1", InvocationInfoProxy.getInstance().getUserCode(), username, null, remark, null, optype);			
					}
				
				//--end
				vo.getParentVO().setAttributeValue("def21", null);
				vo.getParentVO().setAttributeValue("def20", null);
				vo.getParentVO().setAttributeValue("def19", null);
				hvo.setDef21(null);//���Ӱ�����
				hvo.setDef20(null);//���bpm��ַ
				hvo.setDef19(null);//���bpmid
			}
			hvo.setDef47(null);//���bpm�ջر�ʶ
		}
			} catch (BusinessException e) {
			// TODO �Զ����ɵ� catch ��
				ExceptionUtils.wrappException(e);
		}
	}

}
