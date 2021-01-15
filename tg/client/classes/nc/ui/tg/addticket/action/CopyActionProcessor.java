package nc.ui.tg.addticket.action;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.uif2.LoginContext;

public class CopyActionProcessor implements
		ICopyActionProcessor<AggAddTicket> {

	@Override
	public void processVOAfterCopy(AggAddTicket paramT,
			LoginContext paramLoginContext) {
		paramT.getParentVO().setPrimaryKey(null);
		paramT.getParentVO().setModifier(null);
    	paramT.getParentVO().setModifiedtime(null);
    	paramT.getParentVO().setCreator(null);
    	paramT.getParentVO().setCreationtime(null);
    	paramT.getParentVO().setBillno(null);
    	//**************
    	paramT.getParentVO().setBilldate(null);
    	paramT.getParentVO().setApprovedate(null);
    	paramT.getParentVO().setApprovenote(null);
    	paramT.getParentVO().setApprover(null);
    	paramT.getParentVO().setApprovestatus(-1);
    	//add by tjl 2020-06-24
    	//增加补票单复制将单据编码,bpmid和影像编码清掉
    	IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
    	paramT.getParentVO().setBillno(null);
    	paramT.getParentVO().setDef19(null);
    	paramT.getParentVO().setDef20(null);
    	paramT.getParentVO().setDef21(null);
    	// 设置单据状态、单据业务日期默认值
    	paramT.getParentVO().setApprovestatus((Integer) BillStatusEnum.FREE.value());
    	paramT.getParentVO().setBilldate(AppContext.getInstance().getBusiDate());
    	paramT.getParentVO().setMaketime(AppContext.getInstance().getServerTime());
    	String sql="select d.pk_psndoc ,d.pk_dept,d.pk_org from sm_user c,bd_psnjob d where c.pk_psndoc = d.pk_psndoc and c.cuserid='"+ AppContext.getInstance().getPkUser()+"' and  d.ismainjob='Y' and d.dr=0";
    	try {
    		Object[] as=(Object[]) bs.executeQuery(sql, new ArrayProcessor());
    		if(as!=null&&as.length>0){
    			paramT.getParentVO().setDef24((String) as[0]);
    			paramT.getParentVO().setDef25((String) as[1]);
    			paramT.getParentVO().setDef26((String) as[2]);
    		}
    		
    	} catch (BusinessException e1) {
    		// TODO 自动生成的 catch 块
    		e1.printStackTrace();
    		MessageDialog.showErrorDlg(null, "错误", e1.getMessage());
    	}
    	//end
    	
		//TODO 根据需要业务自己补充处理清空
		String[] codes =paramT.getTableCodes();
		if (codes != null && codes.length>0) {
			for (int i = 0; i < codes.length; i++) {
				String tableCode = codes[i];
				 CircularlyAccessibleValueObject[] childVOs = 	paramT.getTableVO(tableCode);
				 if(childVOs!=null){
				 for (CircularlyAccessibleValueObject childVO : childVOs) {
					 try {
						childVO.setPrimaryKey(null);
					} catch (BusinessException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				 }
			}
		}
	}
}
