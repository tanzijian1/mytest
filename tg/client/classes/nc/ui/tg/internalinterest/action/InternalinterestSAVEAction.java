package nc.ui.tg.internalinterest.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import com.ibm.db2.jcc.am.pf;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IInterestShareMaintain;
import nc.itf.tg.IInternalInterestMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Interbus;
import nc.vo.tgfn.internalinterest.Internalinterest;

public class InternalinterestSAVEAction  extends nc.ui.pubapp.uif2app.actions.pflow.SaveScriptAction{
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		super.doAction(e);
		Object obj=getModel().getSelectedData();
		if(obj!=null){
			AggInternalinterest aggVO=(AggInternalinterest)obj;
			String pk=aggVO.getPrimaryKey();
			insertbody(aggVO);
			IMDPersistenceQueryService service=NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
			NCObject nobj=service.queryBillOfNCObjectByPK(AggInternalinterest.class, pk);
			getModel().initModel(nobj.getContainmentObject());
		}
	}
	/**
	 * 是否对内放贷公司插表体为新单
	 * @param bills
	 * @throws BusinessException 
	 */
	private void insertbody(AggInternalinterest bill) throws BusinessException{
		//加在规则那报并发
		Internalinterest internalinterest = (Internalinterest) bill.getParentVO();
		IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
		IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
				IWorkflowMachine.class);
		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		String pk=internalinterest.getPk_internal();
		IUAPQueryBS query=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String hpk=internalinterest.getPk_internal();
		String billno=internalinterest.getBillno();
		String transtypesql="select   pk_billtypeid  from bd_billtype where   pk_billtypecode ='FN06-Cxx-02'";
		String transtypepk=(String)query.executeQuery(transtypesql, new ColumnProcessor());
		if("Y".equals(internalinterest.getDef4()) && ("FN06-Cxx-01".equals(internalinterest.getTranstype()))){
		//是对内放贷公司
		String pk_org = internalinterest.getPk_org();
		//表头组织转客商
		String pkcust=(String)query.executeQuery("select pk_cust_sup from bd_cust_supplier where code=(select  code from org_orgs where pk_org='"+pk_org+"')", new ColumnProcessor());
		Interbus[] interbuss = (Interbus[]) bill.getChildrenVO();//获取单据的表体信息
		internalinterest.setDef4("N");//对内放贷公司设为否
		for(int i=0;i<interbuss.length;i++){
			Interbus interbus = interbuss[i];
			String def1 = interbus.getDef1();//表体 客商
			if(def1==null||def1.isEmpty()) throw new BusinessException("表体客商为空不能生成协同单据");
		    String pkorg=(String)query.executeQuery("select pk_org from org_orgs where code=( select   code  from   bd_cust_supplier where pk_cust_sup='"+def1+"')", new ColumnProcessor());
			String def5 = interbus.getDef5();//表体 金额合计
			interbus.setPk_internal(null);
			interbus.setDef1(pkcust);
			internalinterest.setDef10(pk);
			internalinterest.setDef4(null);
			internalinterest.setDef5(null);
			internalinterest.setPk_org(pkorg);
			internalinterest.setPk_org_v(getPk_vid(pkorg));
			internalinterest.setPk_internal(null);
			internalinterest.setBillno(null);
			internalinterest.setDef3(null);
			internalinterest.setDef11(def5);//表头 表体金额合计
			internalinterest.setUuid_contact(billno);
			internalinterest.setTranstype("FN06-Cxx-02");
			internalinterest.setTranstypepk(transtypepk);
//			interbus.setDef2(def5);//表体 利息金额=原表体的金额合计
//			interbus.setDef3(null);//表体 待摊分闲置资金利息金额
//			interbus.setDef4(null);//表体 待摊分承销费发行费金额
//			interbus.setDef7(def5);//表体 应付利息=原表体的金额合计
//			interbus.setDef8(null);//表体 差额
			interbus.setPk_business_b(null);//表体 主键设空
			AggInternalinterest aggInternalinterest = new AggInternalinterest();
			Interbus[] bodys = {interbus};
			aggInternalinterest.setParent(internalinterest);//设置表头信息
			aggInternalinterest.setChildren(Interbus.class, bodys);//设置表体信息
			AggInternalinterest[] aggs = {aggInternalinterest};
			
			IInternalInterestMaintain operator = NCLocator.getInstance()
					.lookup(IInternalInterestMaintain.class);
			 AggInternalinterest[] billvos=	operator.insert(aggs, aggs);
			 WorkflownoteVO worknoteVO=iWorkflowMachine.checkWorkFlow("SAVE", "FN06", billvos[0], null);
				pfaction.processAction("SAVE", "FN06", worknoteVO, billvos[0], null, eParam);
				
				
	}
		}
	}
		/**
		 * 
		 * 根据"财务组织"主键获取"组织版本"主键
		 * 
		 */
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
