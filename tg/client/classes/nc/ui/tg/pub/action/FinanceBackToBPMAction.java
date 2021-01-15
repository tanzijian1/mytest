package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.tg.pub.action.panel.TgReturnBpmMsgPanel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.core.parser.XPDLParserException;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.pub.WFTask;

/**
 * 融资驳回通知bpm按钮类
 * @author acer
 *
 */
public class FinanceBackToBPMAction extends NCAction{

	private static final long serialVersionUID = 1L;

	private BillForm editor;

	private BillListView listView;

	private BillManageModel model;
	
	public static final String FIRST = "一审"; // 一审
	public static final String SECOND = "二审";// 二审
	public static final String THIRD = "三审";// 二审
	IPushBPMBillFileService fileService = null;
	IMDPersistenceQueryService mdQryService = null;
	
	/**
	 * 元数据持久化查询接口
	 * 
	 * @return
	 */
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}
	
	
	
	@Override
	protected boolean isActionEnable(){
		IBill bill = (IBill) getModel().getSelectedData();
		AbstractBill aggvo = (AbstractBill) bill;
		WorkflownoteVO worknoteVO = null;
		IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
				IWorkflowMachine.class);
		String billtype = null;
		if(bill!=null){
			try {
				if("36H2030101".equals(getModel().getContext().getNodeCode())||"36H2030101000".equals(getModel().getContext().getNodeCode())||"36150BCRR".equals(getModel().getContext().getNodeCode())){
					
					if("36150BCRR".equals(getModel().getContext().getNodeCode())){
						billtype = "36FF";
					}else{
						billtype = "RZ06";
					}
					if(aggvo!=null&&billtype!=null){
						if("RZ06".equals(billtype)){
							Integer approvestatus=(Integer)aggvo.getParentVO().getAttributeValue("approvestatus");
						    if(approvestatus==-1){
						    	return false;
						    	}
						}else if("36FF".equals(billtype)){
							Integer approvestatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
						    if(approvestatus==-1){
						    	return false;
						    	}
						}
					worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.APPROVE,
							billtype, aggvo, null);
					}
					if(worknoteVO==null){
						return false;
					}
					WFTask task =worknoteVO.getTaskInfo().getTask();
					WorkflowProcess process = PfDataCache.getWorkflowProcess(task
							.getWfProcessDefPK());
					Activity activity = process.findActivityByID(task.getActivityID());
					String text = activity.getMultiLangName().getText();
//					if (queryActInsByPrceInsPK(aggvo.getPrimaryKey(),SECOND)
//							&&!"Y".equals(aggvo.getParentVO().getAttributeValue("def34"))) {
//						return true;
//					} 
					if (text!=null&&text.contains(SECOND)
							&&!"Y".equals(aggvo.getParentVO().getAttributeValue("def34"))) {
						return true;
					} 
					else {
						return false;
					}
				}else if("36H2030115".equals(getModel().getContext().getNodeCode())){//补票单需要判断是否已经通知bpm驳回的标志
					billtype = "RZ30";
					worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.APPROVE,
							billtype, aggvo, null);
					if(worknoteVO==null){
						return false;
					}
					WFTask task =worknoteVO.getTaskInfo().getTask();
					WorkflowProcess process = PfDataCache.getWorkflowProcess(task
							.getWfProcessDefPK());
					Activity activity = process.findActivityByID(task.getActivityID());
					String text = activity.getMultiLangName().getText();
//					if (queryActInsByPrceInsPK(aggvo.getPrimaryKey(),SECOND)
//							&&!"Y".equals(aggvo.getParentVO().getAttributeValue("def34"))) {
//						return true;
//					} 
					if (text!=null&&text.contains(SECOND)
							&&!"Y".equals(aggvo.getParentVO().getAttributeValue("def34"))) {
						return true;
					} 
					else {
						return false;
					}
				}else if("36H2030404".equals(getModel().getContext().getNodeCode())){//资本市场还款
					billtype="SD08";
					MarketRepalayVO pvo = (MarketRepalayVO)aggvo.getParentVO();
					Integer approvestatus = pvo.getApprovestatus();
					if(approvestatus == -1){
						return false;
					}
					
					worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.APPROVE,billtype, aggvo, null);
					if(worknoteVO==null){
						return false;
					}
					
					WFTask task =worknoteVO.getTaskInfo().getTask();
					WorkflowProcess process = PfDataCache.getWorkflowProcess(task
							.getWfProcessDefPK());
					Activity activity = process.findActivityByID(task.getActivityID());
					String text = activity.getMultiLangName().getText();
					if (text!=null&&text.contains(SECOND)&&!"Y".equals(pvo.getDef34())) {
						return true;
					}else{
						return false;
					}
				}
			} catch (XPDLParserException e) {
				e.printStackTrace();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		if(getModel().getSelectedData()==null){
			throw new BusinessException("请选中一条数据记录");
		}
//		String usercode = InvocationInfoProxy.getInstance().getUserCode();
//		boolean flag = checkUserCode(usercode);
//		if(!flag){
//			throw new BusinessException("当前用户：“"+usercode+"”，不是集团会计用户，不能驳回BPM");
//		}
		
		IBill bill = (IBill) getModel().getSelectedData();
		AggregatedValueObject aggVO = (AggregatedValueObject) bill;
//		AggregatedValueObject[] aggvos = null;
		TgReturnBpmMsgPanel panel=new TgReturnBpmMsgPanel();
		panel.showModal();
		if(panel.getFlag()==1){
		String resonmsg=panel.getMsg();
		if(resonmsg==null||resonmsg.length()<1)throw new BusinessException("退回意见不能为空");
		aggVO.getParentVO().setAttributeValue("def47", resonmsg);
		if("36H2030101".equals(getModel().getContext().getNodeCode())){//财顾费请款
//			billtype = "RZ06";
			AggFinancexpenseVO aggvo = (AggFinancexpenseVO) aggVO;
				//通知bpm
				aggVO = getPushBPMBillFileService().pushToFinBpmFile(aggvo,ISaleBPMBillCont.BILL_17,null);
		}else if("36H2030101000".equals(getModel().getContext().getNodeCode())){//融资费用
//			billtype = "RZ06";
			AggFinancexpenseVO aggvo = (AggFinancexpenseVO) aggVO;
				//通知bpm
				aggVO = getPushBPMBillFileService().pushToFinBpmFile(aggvo,ISaleBPMBillCont.BILL_18,null);
		}else if("36150BCRR".equals(getModel().getContext().getNodeCode())){//还本还息
//			billtype = "36FF";
			AggRePayReceiptBankCreditVO aggvo = (AggRePayReceiptBankCreditVO) aggVO;
				if (aggvo.getParentVO().getRepayamount() == null
						&& (aggvo.getParentVO().getInterest() != null||aggvo.getParentVO().getAttributeValue("preinterestmoney")!=null)) {//还息
					//通知bpm
					aggVO = getPushBPMBillFileService().pushToFinBpmFile(aggvo,ISaleBPMBillCont.BILL_16,null);
				}else{
					//通知bpm
					aggVO = getPushBPMBillFileService().pushToFinBpmFile(aggvo,ISaleBPMBillCont.BILL_15,null);
				}
		}else if("36H2030115".equals(getModel().getContext().getNodeCode())){//补票单
//			billtype = "RZ30";
			AggAddTicket aggvo = (AggAddTicket) aggVO;
				//通知bpm
				aggVO = getPushBPMBillFileService().pushToFinBpmFile(aggvo,ISaleBPMBillCont.BILL_19,null);
		}else if("36H2030404".equals(getModel().getContext().getNodeCode())){
			AggMarketRepalayVO aggvo = (AggMarketRepalayVO)aggVO;
			aggVO = getPushBPMBillFileService().pushToFinBpmFile(aggvo,ISaleBPMBillCont.BILL_24,null);
		}
		aggVO.getParentVO().setAttributeValue("def47", null);
		getModel().directlyUpdate(aggVO);
		MessageDialog.showWarningDlg(getEditor(), "提示", "驳回通知bpm成功");
		}
	}
	
	IPFBusiAction pfBusiAction = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}
	
	public FinanceBackToBPMAction() {
		super();
		setCode("financeBackToBPMAction");
		setBtnName("驳回通知BPM");
	}
	
	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public BillManageModel getModel() {
		return model;
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public BillListView getListView() {
		return listView;
	}

	public void setListView(BillListView listView) {
		this.listView = listView;
	}
	
	/**
	 * 读取业务单据聚合VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, true,false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
	
	/**
	 * 检查当前用户是否是集团会计用户,是则true,否则false
	 * @param usercode
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkUserCode(String usercode) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct r.cuserid  ");
		sql.append(" from sm_responsibility s ");
		sql.append(" inner join sm_perm_func p  on s.pk_responsibility = p.ruleid ");
		sql.append(" inner join sm_user_role r on r.pk_role = p.subjectid ");
		sql.append(" inner join sm_user e  on e.cuserid = r.cuserid ");
		sql.append(" inner join sm_role o  on o.pk_role = r.pk_role ");
		sql.append(" where isnull(s.dr,0) = 0  ");
		sql.append(" and isnull(p.dr,0) = 0  ");
		sql.append(" and isnull(r.dr,0) = 0  ");
		sql.append(" and isnull(e.dr,0) = 0  ");
		sql.append(" and isnull(o.dr,0) = 0  ");
		sql.append(" and s.code = 'YCYT03'  ");
		sql.append(" and e.user_code = '"+usercode+"'  ");
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<Map<String, Object>> list = (List<Map<String, Object>>) bs
				.executeQuery(sql.toString(), new MapListProcessor());
		return list.size()>0?true:false;
	}
	
	
	/**
	 * 后台推送bpm接口
	 * @return
	 */
	public IPushBPMBillFileService getPushBPMBillFileService() {
		if (fileService == null) {
			fileService = NCLocator.getInstance().lookup(
					IPushBPMBillFileService.class);
		}
		return fileService;
	}
	
	private boolean queryActInsByPrceInsPK(String billid, String type)
			throws DbException, XPDLParserException, BusinessException {
		int[] states = new int[] { 0, 1 };
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String stateIn = "";
		for (int s : states) {
			stateIn = stateIn + "," + s;
		}
		if (stateIn.length() > 0)
			stateIn = stateIn.substring(1);
		SQLParameter para = new SQLParameter();
		String sqlActivityQuery = "select a.activitydefid, a.actstatus, a.createtime, a.modifytime, b.processdefid from pub_wf_actinstance a left join pub_wf_instance b on a.pk_wf_instance = b.pk_wf_instance where a.pk_wf_instance =(select pk_wf_instance from pub_wf_instance where billid='"
				+ billid + "' )  ";

		para.addParam(billid);
		if (!StringUtil.isEmptyWithTrim(stateIn)) {
			sqlActivityQuery += " and a.actstatus in (" + stateIn + ")";
		}
		sqlActivityQuery += " order by a.ts desc";
		List<Object[]> alActInstance = null;
		try {
			alActInstance = (List<Object[]>) bs.executeQuery(sqlActivityQuery,
					new ArrayListProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Activity> result = new ArrayList<Activity>();
		try {
			for (int i = 0; i < alActInstance.size(); i++) {
				WorkflowProcess wp = PfDataCache.getWorkflowProcess(
						alActInstance.get(i)[4].toString(),
						alActInstance.get(i)[0].toString());
				Activity act = wp.findActivityByID(alActInstance.get(i)[0]
						.toString());
				result.add(act);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<String> querySet = new HashSet<String>();
		if (result != null) {
			for (Activity ty : result) {
				querySet.add(ty.getMultiLangName().toString());
			}
			if(querySet!=null){
				if(SECOND.equals(type)){//如果是二审,则判断传的名称是否含有三审,如有三审则返回false
					if (querySet.toString().contains(type)&&!querySet.toString().contains(THIRD)){
						return true;
					}else{
						return false;
					}
				}
				if(FIRST.equals(type)){//如果是一审,则判断传的名称是否含有二审,如有二审则返回false
					if (querySet.toString().contains(type)&&!querySet.toString().contains(SECOND)){
						return true;
					}else{
						return false;
					}
				}
				if(THIRD.equals(type)){//如果是三审true
					if (querySet.toString().contains(type)){
						return true;
					}
				}
			}
		}
		return false;
	}
}
