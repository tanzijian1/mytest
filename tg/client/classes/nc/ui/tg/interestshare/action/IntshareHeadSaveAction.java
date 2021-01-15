package nc.ui.tg.interestshare.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IInterestShareMaintain;
import nc.itf.tg.IInternalInterestMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.tgfn.interestshare.IntshareBody;
import nc.vo.tgfn.interestshare.IntshareHead;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Interbus;

public class IntshareHeadSaveAction extends nc.ui.pubapp.uif2app.actions.pflow.SaveScriptAction{
  @Override
public void doAction(ActionEvent e) throws Exception {
	// TODO 自动生成的方法存根
	super.doAction(e);
	Object obj=getModel().getSelectedData();
	IUAPQueryBS query=NCLocator.getInstance().lookup(IUAPQueryBS.class);
	IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
	IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
			IWorkflowMachine.class);
	HashMap eParam = new HashMap();
	eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
			PfUtilBaseTools.PARAM_NOTE_CHECKED);
	if(obj!=null){
		AggIntshareHead aggvo=(AggIntshareHead)obj;
		String pk=aggvo.getPrimaryKey();
		IntshareHead hvo=aggvo.getParentVO();
		String pk_org=hvo.getPk_org();
		String pkcust=(String)query.executeQuery("select pk_cust_sup from bd_cust_supplier where code=(select  code from org_orgs where pk_org='"+pk_org+"')", new ColumnProcessor());
		String hpk=hvo.getPk_intsharehead();
		String billno=hvo.getBillno();
		IInterestShareMaintain operator = NCLocator.getInstance()
				.lookup(IInterestShareMaintain.class);
		if("Y".equals(hvo.getDef8())){//是否对内放贷公司
			IntshareBody[] bodyvos = (IntshareBody[]) aggvo.getChildrenVO();//获取单据的表体信息
            for(IntshareBody bvo:bodyvos){
            	String transtypesql="select   pk_billtypeid  from bd_billtype where   pk_billtypecode ='FN24-Cxx-02'";
        		String transtypepk=(String)query.executeQuery(transtypesql, new ColumnProcessor());
            	AggIntshareHead newbillvo=new AggIntshareHead();
                String def1=bvo.getDef1();//对方组织
                if(def1==null||def1.isEmpty()) throw new BusinessException("表体客商为空不能生成协同单据");
    		    String pkorg=(String)query.executeQuery("select pk_org from org_orgs where code=( select   code  from   bd_cust_supplier where pk_cust_sup='"+def1+"')", new ColumnProcessor());
            	hvo.setBillno(null);
            	hvo.setTranstype("FN24-Cxx-02");
            	hvo.setTranstypepk(transtypepk);
            	hvo.setDef4(bvo.getDef4());//累计放款金额
            	hvo.setPk_intsharehead(null);
            	hvo.setPk_org_v(getPk_vid(pkorg));
            	hvo.setDef8(null);
            	hvo.setDef7(null);
            	hvo.setDef10(billno);
            	hvo.setPk_org(pkorg);
            	bvo.setDef1(pkcust);
            	bvo.setPk_intsharebody(null);
            	newbillvo.setParentVO(hvo);
            	newbillvo.setChildrenVO(new IntshareBody[]{bvo});
            	AggIntshareHead[] billvos=operator.insert(new AggIntshareHead[]{newbillvo}, null);
    			 WorkflownoteVO worknoteVO=iWorkflowMachine.checkWorkFlow("SAVE", "FN24", billvos[0], null);
    				pfaction.processAction("SAVE", "FN24", worknoteVO, billvos[0], null, eParam);
    				
            }
		}
		IMDPersistenceQueryService service=NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		NCObject nobj=service.queryBillOfNCObjectByPK(AggIntshareHead.class, pk);
		getModel().initModel(nobj.getContainmentObject());
		
	}
}
  public static String getPk_vid (String def1){
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String pk_vid = null;
		try {
			pk_vid = (String) bs.executeQuery("select pk_vid from org_orgs_v where pk_org = '"+def1+"'",new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return pk_vid;
	}
}
