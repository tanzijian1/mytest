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
		// TODO 自动生成的方法存根
		String remark="";//退单描述 
		String optype="2";//操作类型（1/作废处理(只改状态)，2/删除数据，默认1）
		try {
		for(AggAddTicket vo:vos){
			AddTicket hvo=vo.getParentVO();
			if(!("Y".equals(hvo.getAttributeValue("def47")))){//def47标识是否bpm收回
				
				//TODO20201016--清空影像编码调影像删除接口
				IGuoXinImage image=NCLocator.getInstance().lookup(IGuoXinImage.class);
				if(StringUtils.isNotBlank((String) vo.getParent().getAttributeValue("def21"))){
					 String	username = (String)bs.executeQuery("select user_name  from sm_user where cuserid='"+InvocationInfoProxy.getInstance().getUserId()+"'", new ColumnProcessor());
					
					image.delrefund((String) vo.getParent().getAttributeValue("def21"), "1", InvocationInfoProxy.getInstance().getUserCode(), username, null, remark, null, optype);			
					}
				
				//--end
				vo.getParentVO().setAttributeValue("def21", null);
				vo.getParentVO().setAttributeValue("def20", null);
				vo.getParentVO().setAttributeValue("def19", null);
				hvo.setDef21(null);//清除影像编码
				hvo.setDef20(null);//清除bpm地址
				hvo.setDef19(null);//清除bpmid
			}
			hvo.setDef47(null);//清除bpm收回标识
		}
			} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
				ExceptionUtils.wrappException(e);
		}
	}

}
