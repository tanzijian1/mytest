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
	 setBtnName("�˻�");
	 setCode("returnSaleAction");
 }
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO �Զ����ɵķ������
		Object obj=getModel().getSelectedData();
		IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
		 if(obj==null)throw new BusinessException("δѡ������");
		 IUnsavebillAndDeleteBill service =NCLocator.getInstance().lookup(IUnsavebillAndDeleteBill.class);
		int i= MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), "�˻�", "�Ƿ�ȷ���˻�����ϵͳ");
		if(UIDialog.ID_YES==i){
			AggregatedValueObject vo=  (AggregatedValueObject)obj;
			int approvestatus=(Integer)vo.getParentVO().getAttributeValue("approvestatus");
			if(!(approvestatus!=-1||approvestatus!=3)){
				throw new BusinessException("����״̬����Ϊ����̬���ύ̬");
			}
			String billtype=(String) ((String)vo.getParentVO().getAttributeValue("billtype")==null ? vo.getParentVO().getAttributeValue("transtype"):vo.getParentVO().getAttributeValue("billtype"));
			String cuserid=(String)query.executeQuery("select cuserid  from sm_user where user_name='SALE' ", new ColumnProcessor());
    	    	if(cuserid==null)throw new BusinessException("SALE�û�Ϊ��");
    	    	if(cuserid.equals(vo.getParentVO().getAttributeValue("creator"))){
//    	            super.doSuperAction(e);
					String num=getgl_num(vo.getParentVO().getPrimaryKey());
					String isgl = num==null ? "0":"1";
					String isshr= (int)vo.getParentVO().getAttributeValue("approvestatus")==1 ? "1":"0";
					HashMap<String, Object> mapdata = new HashMap<String, Object>();
					mapdata.put("vouchid", vo.getParentVO().getAttributeValue("def1"));// ����ϵͳ����ID
					mapdata.put("generateCredentials", isgl);// �Ƿ�����ƾ֤
					mapdata.put("ncDocumentNo", vo.getParentVO().getAttributeValue("billno"));// NCƱ�ݺ�
					mapdata.put("ncDocumentnumber", num);// ƾ֤��
					mapdata.put("shr", isshr);// ����Ƿ�ɹ�
					mapdata.put("isDj", "0");// �Ƿ�Ϊת������ݣ�1Ϊ�ǣ�
					service.SaleBackDelete_RequiresNew(vo, billtype, mapdata);
//					if(getDataManager()!=null){
//						getDataManager().initModelByQueryScheme(getDataManager().getQueryScheme());
//					}
					getModel().initModel(null);
//					getModel().directlyDelete(vo);
	    	    	ShowStatusBarMsgUtil.showStatusBarMsg("�˻سɹ�", getModel().getContext());
                        
    	    	}
//    	    	getModel().update();
//    	    	getModel().update(vo);
//    	    	nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction refreshaction = new nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction();
//    			refreshaction.setModel(getModel());
//    			ActionEvent e1 = new ActionEvent(refreshaction, 1001, "ˢ��");
//    			refreshaction.doAction(e1);
//    	    	HashMap eParam = new HashMap();
//    			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
//    					PfUtilBaseTools.PARAM_NOTE_CHECKED);
//    	    	pfaction.processAction(IPFActionName.DEL_DELETE,billtype , null, vo, null, eParam);
	            
		}
	}
	IUAPQueryBS query =NCLocator.getInstance().lookup(IUAPQueryBS.class);
	/**
	 * �õ�ƾ֤��
	 * @param pk
	 * @return
	 * @throws BusinessException 
	 */
	public String getgl_num(String pk) throws BusinessException{
		String num=(String)query.executeQuery("select g.num from fip_relation f inner join  gl_voucher  g on g.pk_voucher =f.des_relationid  where f.src_relationid='"+pk+"'", new ColumnProcessor());
		return num;
	}
}
