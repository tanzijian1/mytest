package nc.ui.tg.tgrz_mortgageagreement.action;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.vo.uif2.LoginContext;

public class CopyActionProcessor implements
		ICopyActionProcessor<AggMortgageAgreementVO> {

	@Override
	public void processVOAfterCopy(AggMortgageAgreementVO paramT,
			LoginContext paramLoginContext) {
		paramT.getParentVO().setPrimaryKey(null);
		paramT.getParentVO().setModifier(null);
    	paramT.getParentVO().setModifiedtime(null);
    	paramT.getParentVO().setCreator(null);
    	paramT.getParentVO().setCreationtime(null);
    	//add by tjl 2020-06-24
    	//时代新增需求,将复制功能下的单据编号和bpmid清掉
    	paramT.getParentVO().setBillno(null);
    	paramT.getParentVO().setDef19(null);
    	paramT.getParentVO().setDef20(null);
    	// 设置单据状态、单据业务日期默认值
    	paramT.getParentVO().setApprovestatus((Integer) BillStatusEnum.FREE.value());
    	paramT.getParentVO().setDbilldate(AppContext.getInstance().getBusiDate());
    	paramT.getParentVO().setTranstypepk("~");
    	paramT.getParentVO().setBilltype("RZ04");
    	String sql="select d.pk_psndoc ,d.pk_dept,d.pk_org from sm_user c,bd_psnjob d where c.pk_psndoc = d.pk_psndoc and c.cuserid='"+ AppContext.getInstance().getPkUser()+"' and  d.ismainjob='Y' and d.dr=0";
    	IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
    	try {
    		Object[] as=(Object[]) bs.executeQuery(sql, new ArrayProcessor());
    		if(as!=null&&as.length>0){
    			paramT.getParentVO().setProposer((String) as[0]);
    			paramT.getParentVO().setApplicationdept((String) as[1]);
    			paramT.getParentVO().setApplicationorg((String) as[2]);
    		}
    		paramT.getParentVO().setApplicationdate(new UFDate());
    		
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
