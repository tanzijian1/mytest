package nc.bs.tg.pub.wfbackwordcheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.pf.IWFBackwardCheck;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.core.parser.XPDLParserException;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import nc.vo.wfengine.pub.WFTask;
import nc.vo.yer.AggAddBillHVO;

/**
 * @modify zzl 2020年9月25日11:10:22 增加付款类交易类型
 * 
 */
public class WFBackwardCheckImpl implements IWFBackwardCheck {
	public static final String FIRST = "一审"; // 一审
	public static final String SECOND = "二审";// 二审
	public static final String THIRD = "三审";// 二审

	@Override
	public void checkBackward(String billtype, WorkflowProcess process,
			Activity activity, AggregatedValueObject billVO)
			throws BusinessException {
		try {
			List<String> bxlist = new ArrayList<String>();
			bxlist.add("264X-Cxx-007");
			bxlist.add("264X-Cxx-009");
			// start zzl 2020年9月25日11:10:22 增加付款类交易类型
			bxlist.add("F1-Cxx-LL02"); // 邻里-历史合同或历史非合同请款
			bxlist.add("F1-Cxx-LL03");// 邻里-合同或非合同请款应付单
			bxlist.add("F3-Cxx-LL07"); // 邻里-历史合同\非合同 付款单-预付款
			bxlist.add("F3-Cxx-LL06"); // 邻里-合同\非合同付款单-预付款
			bxlist.add("F1-Cxx-LL01"); // 邻里-SRM供应商请款应付单
			bxlist.add("F3-Cxx-LL01"); // 邻里-SRM供应商请款付款单-预付款
			bxlist.add("F3-Cxx-LL09"); // 邻里-SRM供应商请款付款单-非预付

			// end
			if (billtype.contains("264X")) {
				if (!bxlist.contains(billtype)) {
					return;
				}
			}
			if (billtype.contains("F1") || billtype.contains("F3")) {
				if (bxlist.contains(billtype)) {
					return;
				}
			}
			if (activity == null) {
				return;
			}

			List<Activity> actlist = process.getActivities();
			Boolean isbpmfolw = false;
			for (Activity act : actlist) {
				if (act.getMultiLangName().getText().contains("BPM")) {
					isbpmfolw = true;
					break;
				}
			}
			if (!isbpmfolw)
				return;

			String srcName = getSrcWFName(billVO, billtype);
			String desname = activity.getMultiLangName().getText();
			boolean isbackward = true;
			// 非BPM用户不能直接驳回制单人
			isbackward = (WorkflowTypeEnum
					.isMainFlow(process.getWorkflowType()))
					&& (process.findStartActivity().getId().equals(activity
							.getId())) && !srcName.contains(FIRST);
			if (isbackward) {
				throw new BusinessException("非一级审批人不能直接驳回制单人");
			}

			// 非二审人员不能直接驳回一审人员
			isbackward = (WorkflowTypeEnum
					.isMainFlow(process.getWorkflowType()))
					&& !srcName.contains(SECOND) && desname.contains(FIRST);

			if (isbackward) {
				throw new BusinessException("非二级审批人不能直接驳回一级审批人");
			}
			if (billtype.contains("264X")) {
				if (srcName.contains(THIRD) && desname.contains(SECOND)) {
					String primaryKey = billVO.getParentVO().getPrimaryKey();
					AggAddBillHVO aggvo = (AggAddBillHVO) isExistAddbill(primaryKey);
					if ("264X-Cxx-009".equals(billtype) && aggvo != null) {
						throw new BusinessException("当前单据已生成押票单，请先删除对应的押票单！");
					}
				}
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	/**
	 * 读取来源流程实例名
	 * 
	 * @param billVO
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	public String getSrcWFName(AggregatedValueObject billVO, String billtype)
			throws Exception {
		String sql = "select parentbilltype  from bd_billtype t where t.pk_billtypecode  = '"
				+ billtype + "' and isnull(dr,0)=0 and istransaction = 'Y' ";
		String pk_billtype = (String) new BaseDAO().executeQuery(sql,
				new ColumnProcessor());
		pk_billtype = pk_billtype == null ? billtype : pk_billtype;
		WorkflownoteVO srcworknoteVO = NCLocator
				.getInstance()
				.lookup(IWorkflowMachine.class)
				.checkWorkFlow(IPFActionName.APPROVE, pk_billtype, billVO, null);
		if (srcworknoteVO == null) {
			return null;
		}
		WFTask task = srcworknoteVO.getTaskInfo().getTask();
		WorkflowProcess process = PfDataCache.getWorkflowProcess(task
				.getWfProcessDefPK());
		Activity srcactivity = process.findActivityByID(task.getActivityID());
		String srcname = srcactivity.getMultiLangName().getText();
		return srcname;
	}

	/**
	 * 根据报销单PK查询押票单
	 * 
	 * @param pk_jkbx
	 * @return
	 * @throws BusinessException
	 */
	private AggregatedValueObject isExistAddbill(String pk_jkbx)
			throws BusinessException {
		IMDPersistenceQueryService service = NCLocator.getInstance().lookup(
				IMDPersistenceQueryService.class);
		Collection coll = service.queryBillOfVOByCond(AggAddBillHVO.class,
				"isnull(dr,0)=0 and def15 = '" + pk_jkbx + "'", false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
}
