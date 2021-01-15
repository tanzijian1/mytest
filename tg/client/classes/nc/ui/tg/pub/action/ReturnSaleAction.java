package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IUnsavebillAndDeleteBill;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;

public class ReturnSaleAction extends nc.ui.pubapp.uif2app.actions.pflow.DeleteScriptAction{
	public  ReturnSaleAction(){
	 setBtnName("退回");
	 setCode("returnSaleAction");
 }
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		Object obj=getModel().getSelectedData();
		IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
		 if(obj==null)throw new BusinessException("未选中数据");
		 IUnsavebillAndDeleteBill service =NCLocator.getInstance().lookup(IUnsavebillAndDeleteBill.class);
		int i= MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), "退回", "是否确认退回销售系统");
		if(UIDialog.ID_YES==i){
			AggregatedValueObject vo=  (AggregatedValueObject)obj;
			int approvestatus=(Integer)vo.getParentVO().getAttributeValue("approvestatus");
			if(!(approvestatus!=-1||approvestatus!=3)){
				throw new BusinessException("单据状态必须为自由态或提交态");
			}
			String billtype=(String) ((String)vo.getParentVO().getAttributeValue("billtype")==null ? vo.getParentVO().getAttributeValue("transtype"):vo.getParentVO().getAttributeValue("billtype"));
			String cuserid=(String)query.executeQuery("select cuserid  from sm_user where user_name='SALE' ", new ColumnProcessor());
    	    	if(cuserid==null)throw new BusinessException("SALE用户为空");
    	    	if(cuserid.equals(vo.getParentVO().getAttributeValue("creator"))){
//    	            super.doSuperAction(e);
					String num=getgl_num(vo.getParentVO().getPrimaryKey());
					String isgl = num==null ? "0":"1";
					String isshr= (int)vo.getParentVO().getAttributeValue("approvestatus")==1 ? "1":"0";
					HashMap<String, Object> mapdata = new HashMap<String, Object>();
					mapdata.put("vouchid", vo.getParentVO().getAttributeValue("def1"));// 销售系统单据ID
					mapdata.put("generateCredentials", isgl);// 是否生成凭证
					mapdata.put("ncDocumentNo", vo.getParentVO().getAttributeValue("billno"));// NC票据号
					mapdata.put("ncDocumentnumber", num);// 凭证号
					mapdata.put("shr", isshr);// 审核是否成功
					mapdata.put("isDj", "0");// 是否为转备案款单据（1为是）
					service.SaleBackDelete_RequiresNew(vo, billtype, mapdata);
//					if(getDataManager()!=null){
//						getDataManager().initModelByQueryScheme(getDataManager().getQueryScheme());
//					}
					getModel().initModel(null);
//					getModel().directlyDelete(vo);
	    	    	ShowStatusBarMsgUtil.showStatusBarMsg("退回成功", getModel().getContext());
                        
    	    	}
//    	    	getModel().update();
//    	    	getModel().update(vo);
//    	    	nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction refreshaction = new nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction();
//    			refreshaction.setModel(getModel());
//    			ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
//    			refreshaction.doAction(e1);
//    	    	HashMap eParam = new HashMap();
//    			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
//    					PfUtilBaseTools.PARAM_NOTE_CHECKED);
//    	    	pfaction.processAction(IPFActionName.DEL_DELETE,billtype , null, vo, null, eParam);
	            
		}
	}
	IUAPQueryBS query =NCLocator.getInstance().lookup(IUAPQueryBS.class);
	/**
	 * 得到凭证号
	 * @param pk
	 * @return
	 * @throws BusinessException 
	 */
	public String getgl_num(String pk) throws BusinessException{
		String num=(String)query.executeQuery("select g.num from fip_relation f inner join  gl_voucher  g on g.pk_voucher =f.des_relationid  where f.src_relationid='"+pk+"'", new ColumnProcessor());
		return num;
	}
}
