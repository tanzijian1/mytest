package nc.ui.tg.report;

import java.awt.Container;

import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.itf.tg.rz.report.TGReprotContst;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.pub.smart.model.descriptor.FilterDescriptor;
import nc.pub.smart.model.descriptor.FilterItem;
import nc.pub.smart.model.preferences.Parameter;
import nc.ui.iufo.freereport.extend.DefaultQueryAction;
import nc.ui.iufo.freereport.extend.ReportNormalQueryPanel;
import nc.ui.pubapp.uif2app.query2.DefaultQueryConditionDLG;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.querytemplate.filter.IFilter;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.tg.pub.filter.BilltypeFilter;
import nc.ui.tg.pub.filter.CreditReferToFilter;
import nc.ui.tg.pub.filter.ExpensesubFilter;
import nc.ui.tg.pub.filter.MainOrgWithPermissionOrgFilter;
import nc.ui.tg.pub.filter.QueryTempMainOrgFilterBaseDoc;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.querytemplate.QueryTemplateConvertor;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.uap.rbac.FuncSubInfo;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;
import com.ufida.report.anareport.base.BaseNormalQueryObject;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.model.AbsAnaReportModel;
import com.ufida.report.free.plugin.param.ReportVariableHelper;
import com.ufida.zior.context.ComContextKey;

public class ReportQueryAction extends DefaultQueryAction {

	public static final String CASHPLEDGE = "36HA0203"; // Ѻ��֤��̨��
	public static final String HYDROPOWERDEDUCTIONINV = "36HA0206"; // ˮ���Զ����۷�Ʊ��ϸ���ɱ�����
	public static final String YUSUANRICHANG = "36HA0404"; // Ԥ���ճ�ִ����ϸ������˾
															// 36HA0402 ����9605
	public static final String YINZIAOGUANLI = "36HA0405"; // Ӫ����������ú�Ʊ��Ȩ��������ϸ
															// 36HA0208����9605
	public static final String CREDIT = "36HA0204"; // �Ŵ����ʷ��÷�Ʊ��ϸ 36HA0204����9605
													// 36HA0204
	public static final String SALECONTRACT = "36HA0401";// Ӫ����ͬ��Ʊ̨��
	public static final String SALEPAYCONTRACT = "36HA0403";// Ӫ����ͬ��Ʊ����̨��
	public static final String FINANCESUBDETAIL = "36HA0402";// ���ÿ�Ŀ��ϸ��
																// 36HA0207Ϊ���Ի����ڵ����
	public static final String CHENGBENTAIZHANG = "36HA0201";// �ɱ�̨��
	public static final String GOUFANGWEIKUAN = "36H2040221";// ����β���ʲ�ѭ������
																// 36HA0201Ϊ���Ի����ڵ����
	
	public static final String CONTRAT_PAY_DETAIL = "36HA0202";// ��ͬ������ϸ���ɱ�����
	
	public static final String CHENGBENTAIZHANG2 = "36HA0209";// �ɱ�̨��(����ҵ̬)
	
	public static final String FENBAO = "36HA0207" ;// �ְܷ��ɱ�̨��

	/**
	 * �Ӳ�ѯ�Ի����л�ȡ�û�������Ϣ
	 * 
	 * @param condition
	 * @param queryDlg
	 * @param normalPanel
	 * @param repParams
	 * @param context
	 * @return
	 */
	protected IQueryCondition setQueryResult(IQueryCondition condition,
			QueryConditionDLG queryDlg, AbsAnaReportModel reportModel,
			IContext context) {
		if (condition == null || !(condition instanceof BaseQueryCondition))
			return condition;
		BaseQueryCondition result = (BaseQueryCondition) condition;
		// update by tanghw at 2011-3-28 ��Ϊ���ñ������
		Parameter[] params = ReportVariableHelper.getParams(reportModel
				.getFormatModel());
		if (params != null && params.length > 0) {
			params = ((BaseNormalQueryObject) ((ReportNormalQueryPanel) getNormalPanel())
					.getNormalQueryObject()).getReportParameter();
			// ������ֵд�������
			ReportVariableHelper.addParamsValueToPool(
					ReportVariableHelper.getPoolInstance(context), params);
		}

		// Descriptor[] descriptors = getDescriptors(queryDlg);
		// result.setDescriptors(descriptors);
		String whereSql = queryDlg.getQueryScheme().getWhereSQLOnly();
		if (whereSql != null && whereSql.trim().length() > 0) {
			FilterItem item = new FilterItem();
			item.setExpression(whereSql);
			FilterDescriptor desc = new FilterDescriptor();
			desc.addFilter(item);
			if (condition instanceof BaseQueryCondition)
				((BaseQueryCondition) condition)
						.setDescriptors(new Descriptor[] { desc });
		}

		// 2012-11-1 sunhld ����conditionVOs��context�й��������ʹ��
		ConditionVO[] condVOs = queryDlg.getQryCondEditor()
				.getGeneralCondtionVOs();
		context.setAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS,
				condVOs);

		ConditionVO[] qryCondVOs = (ConditionVO[]) queryDlg.getQueryScheme()
				.get(IQueryScheme.KEY_LOGICAL_CONDITION);
		if (qryCondVOs != null && qryCondVOs.length > 0) {
			com.ufida.report.free.plugin.param.ReportVarsPool varPool = (com.ufida.report.free.plugin.param.ReportVarsPool) context
					.getAttribute("variables_pool");
			for (ConditionVO qryCondVO : qryCondVOs) {
				if (qryCondVO.getValue() == null
						|| "".equals(qryCondVO.getValue())) {
					continue;
				}

				if ("date".equals(qryCondVO.getFieldCode())) {
					String[] splitValues = qryCondVO.getValue().split("@@");
					varPool.add("qry_" + qryCondVO.getFieldCode(), new UFDate(
							splitValues[0]).getYear()
							+ "-01-01 �� "
							+ new UFDate(splitValues[0]).toStdString());
				}
				if (qryCondVO.getValue().contains("@@")) {
					String[] splitValues = qryCondVO.getValue().split("@@");
					varPool.add("qry_" + qryCondVO.getFieldCode() + "_begin",
							splitValues[0]);
					varPool.add("qry_" + qryCondVO.getFieldCode() + "_end",
							splitValues[1]);
				} else {
					varPool.add("qry_" + qryCondVO.getFieldCode(),
							qryCondVO.getValue());

				}
			}
			context.setAttribute("variables_pool", varPool);

		}

		context.setAttribute(TGReprotContst.KEY_QueryScheme, qryCondVOs);

		return result;
	}

	/**
	 * ����ָ����ѯ�������в�ѯ ��IQueryAction�ӿڵķ�����ʵ�֣���
	 * 
	 * @param parent
	 * @param context
	 * @param reportModel
	 * @param queryScheme
	 * @return
	 */
	@Override
	public IQueryCondition doQueryByScheme(Container parent, IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		if (queryScheme == null)
			return new BaseQueryCondition(false);
		if (getQueryConditionDlg(parent, context, reportModel, null) != null
				&& getNormalPanel() != null) {
			getNormalPanel().setNormalQueryObject(
					queryScheme.get(IQueryScheme.KEY_NORMAL_CONDITION));
		}
		String whereSql = queryScheme.getWhereSQLOnly();
		IQueryCondition condition = createQueryCondition(context);
		if (whereSql != null && whereSql.trim().length() > 0) {
			FilterItem item = new FilterItem();
			item.setExpression(whereSql);
			FilterDescriptor desc = new FilterDescriptor();
			desc.addFilter(item);
			if (condition instanceof BaseQueryCondition)
				((BaseQueryCondition) condition)
						.setDescriptors(new Descriptor[] { desc });
		}
		Object obj = queryScheme.get(IQueryScheme.KEY_FILTERS);
		if (obj instanceof IFilter[]) {
			IFilter[] iFilters = (IFilter[]) obj;
			int len = iFilters.length;
			ConditionVO[] conditionVOs = new ConditionVO[len];
			for (int i = 0; i < len; i++) {
				conditionVOs[i] = QueryTemplateConvertor
						.convert2ConditionVO(iFilters[i]);
			}
			ConditionVO[] qryCondVOs = (ConditionVO[]) queryScheme
					.get(IQueryScheme.KEY_LOGICAL_CONDITION);
			if (qryCondVOs != null && qryCondVOs.length > 0) {
				com.ufida.report.free.plugin.param.ReportVarsPool varPool = (com.ufida.report.free.plugin.param.ReportVarsPool) context
						.getAttribute("variables_pool");
				for (ConditionVO qryCondVO : qryCondVOs) {
					if (qryCondVO.getValue() == null
							|| "".equals(qryCondVO.getValue())) {
						continue;
					}

					if ("date".equals(qryCondVO.getFieldCode())) {
						String[] splitValues = qryCondVO.getValue().split("@@");
						varPool.add(
								"qry_" + qryCondVO.getFieldCode(),
								new UFDate(splitValues[0]).getYear()
										+ "-01-01 �� "
										+ new UFDate(splitValues[0])
												.toStdString());
					}
					if (qryCondVO.getValue().contains("@@")) {
						String[] splitValues = qryCondVO.getValue().split("@@");
						varPool.add("qry_" + qryCondVO.getFieldCode()
								+ "_begin", splitValues[0]);
						varPool.add("qry_" + qryCondVO.getFieldCode() + "_end",
								splitValues[1]);
					} else {
						varPool.add("qry_" + qryCondVO.getFieldCode(),
								qryCondVO.getValue());

					}
				}
				context.setAttribute("variables_pool", varPool);

			}
			context.setAttribute(TGReprotContst.KEY_QueryScheme, qryCondVOs);
		}
		return condition;
	}

	protected FilterDescriptor getFilterFromQueryDlg(QueryConditionDLG queryDlg) {
		// ��ȡ��ѯģ������
		String whereSql = queryDlg.getWhereSQL();
		if (whereSql == null || whereSql.trim().length() == 0)
			return null;

		// where����ֱ�Ӵ����ɸѡ������
		FilterItem item = new FilterItem();
		item.setExpression(whereSql);
		item.setManualExpression(true);

		FilterDescriptor filter = new FilterDescriptor();
		filter.addFilter(item);

		return filter;
	}

	protected QueryConditionDLG createQueryDlg(Container parent,
			TemplateInfo ti, IContext context, IQueryCondition oldCondition) {
		DefaultQueryConditionDLG dlg = new DefaultQueryConditionDLG(parent, ti);
		dlg.getQryCondEditor().getQueryContext().setMultiTB(true);
		initQueryConditionDLG(dlg, context);
		return dlg;
	}

	/**
	 * ��ʼ������
	 * 
	 * @param dlg
	 * @param context
	 */
	private void initQueryConditionDLG(DefaultQueryConditionDLG dlg,
			IContext context) {
		// new QryCondDLGInitializer(new QueryConditionDLGDelegator(dlg),
		// (FuncSubInfo) context
		// .getAttribute(ComContextKey.FUNC_NODE_INFO));
		// add by tjl 2020-06-11
		String funCode=((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode();
		if (CASHPLEDGE.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// Ѻ��֤��̨��
			String orgName = "area";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));

		} else if (HYDROPOWERDEDUCTIONINV.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// ˮ���Զ����۷�Ʊ��ϸ���ɱ�����
			String orgName = "area";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		} else if (YUSUANRICHANG.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// Ԥ���ճ�ִ����ϸ��
			String orgName = "dimension";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
			String expensesub = "expensesub";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(expensesub,
					new ExpensesubFilter());

			// ���ý������Ͳ�������
			String billtype = "billtype";
			new QueryConditionDLGDelegator(dlg).setRefFilter(billtype,
					new BilltypeFilter());
		} else if (YINZIAOGUANLI.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// Ԥ���ճ�ִ����ϸ��
			String orgName = "region";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
			String expensesub = "courseTitle";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(expensesub,
					new ExpensesubFilter());
		} else if (CREDIT.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// �Ŵ����ʷ��÷�Ʊ��ϸ
			/*
			 * String orgName = "companyName"; // ��������˾�������� new
			 * QueryConditionDLGDelegator(dlg).setRefFilter(orgName, new
			 * MainOrgWithPermissionOrgFilter((FuncSubInfo) context
			 * .getAttribute(ComContextKey.FUNC_NODE_INFO)));
			 */
			String expensesub = "costtype";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					expensesub,
					new CreditReferToFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		} else if (SALECONTRACT.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {
			String orgName = "vdef14";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		} else if (SALEPAYCONTRACT.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {
			String orgName = "vdef14";
			// ��������˾��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		} else if (FINANCESUBDETAIL.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// ���ÿ�Ŀ��ϸ��
			String expensesub = "expenseaccount";
			// ���÷��ÿ�Ŀ��������
			new QueryConditionDLGDelegator(dlg).setRefFilter(expensesub,
					new ExpensesubFilter());
		} else if (CHENGBENTAIZHANG.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())||CONTRAT_PAY_DETAIL.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())||CHENGBENTAIZHANG2.equals(((FuncSubInfo) context
						.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())||FENBAO.equals(funCode)) {// ���ÿ�Ŀ��ϸ��
			String pk_org = "pk_org";
			String pk_project = "pk_project";
			QueryTempMainOrgFilterBaseDoc baseDocByOrgFileter = new QueryTempMainOrgFilterBaseDoc(
					new QueryConditionDLGDelegator(dlg), pk_org, pk_project);
			baseDocByOrgFileter.addEditorListener();
		}else if(GOUFANGWEIKUAN.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())){//����β���ʲ�ѭ������
			String orgName = "���й�˾";
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		}
		// end

	}

}
