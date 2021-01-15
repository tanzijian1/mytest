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
 * @modify zzl 2020��9��25��11:10:22 ���Ӹ����ཻ������
 * 
 */
public class WFBackwardCheckImpl implements IWFBackwardCheck {
	public static final String FIRST = "һ��"; // һ��
	public static final String SECOND = "����";// ����
	public static final String THIRD = "����";// ����

	@Override
	public void checkBackward(String billtype, WorkflowProcess process,
			Activity activity, AggregatedValueObject billVO)
			throws BusinessException {
		try {
			List<String> bxlist = new ArrayList<String>();
			bxlist.add("264X-Cxx-007");
			bxlist.add("264X-Cxx-009");
			// start zzl 2020��9��25��11:10:22 ���Ӹ����ཻ������
			bxlist.add("F1-Cxx-LL02"); // ����-��ʷ��ͬ����ʷ�Ǻ�ͬ���
			bxlist.add("F1-Cxx-LL03");// ����-��ͬ��Ǻ�ͬ���Ӧ����
			bxlist.add("F3-Cxx-LL07"); // ����-��ʷ��ͬ\�Ǻ�ͬ ���-Ԥ����
			bxlist.add("F3-Cxx-LL06"); // ����-��ͬ\�Ǻ�ͬ���-Ԥ����
			bxlist.add("F1-Cxx-LL01"); // ����-SRM��Ӧ�����Ӧ����
			bxlist.add("F3-Cxx-LL01"); // ����-SRM��Ӧ�����-Ԥ����
			bxlist.add("F3-Cxx-LL09"); // ����-SRM��Ӧ�����-��Ԥ��

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
			// ��BPM�û�����ֱ�Ӳ����Ƶ���
			isbackward = (WorkflowTypeEnum
					.isMainFlow(process.getWorkflowType()))
					&& (process.findStartActivity().getId().equals(activity
							.getId())) && !srcName.contains(FIRST);
			if (isbackward) {
				throw new BusinessException("��һ�������˲���ֱ�Ӳ����Ƶ���");
			}

			// �Ƕ�����Ա����ֱ�Ӳ���һ����Ա
			isbackward = (WorkflowTypeEnum
					.isMainFlow(process.getWorkflowType()))
					&& !srcName.contains(SECOND) && desname.contains(FIRST);

			if (isbackward) {
				throw new BusinessException("�Ƕ��������˲���ֱ�Ӳ���һ��������");
			}
			if (billtype.contains("264X")) {
				if (srcName.contains(THIRD) && desname.contains(SECOND)) {
					String primaryKey = billVO.getParentVO().getPrimaryKey();
					AggAddBillHVO aggvo = (AggAddBillHVO) isExistAddbill(primaryKey);
					if ("264X-Cxx-009".equals(billtype) && aggvo != null) {
						throw new BusinessException("��ǰ����������ѺƱ��������ɾ����Ӧ��ѺƱ����");
					}
				}
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	/**
	 * ��ȡ��Դ����ʵ����
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
	 * ���ݱ�����PK��ѯѺƱ��
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
