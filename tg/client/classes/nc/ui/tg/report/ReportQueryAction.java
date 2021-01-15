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

	public static final String CASHPLEDGE = "36HA0203"; // 押金保证金台帐
	public static final String HYDROPOWERDEDUCTIONINV = "36HA0206"; // 水电自动划扣发票明细（成本报表）
	public static final String YUSUANRICHANG = "36HA0404"; // 预算日常执行明细表区域公司
															// 36HA0402 测试9605
	public static final String YINZIAOGUANLI = "36HA0405"; // 营销、管理费用后补票及权责发生制明细
															// 36HA0208测试9605
	public static final String CREDIT = "36HA0204"; // 信贷融资费用发票明细 36HA0204测试9605
													// 36HA0204
	public static final String SALECONTRACT = "36HA0401";// 营销合同发票台账
	public static final String SALEPAYCONTRACT = "36HA0403";// 营销合同发票付款台账
	public static final String FINANCESUBDETAIL = "36HA0402";// 费用科目明细表
																// 36HA0207为测试环境节点编码
	public static final String CHENGBENTAIZHANG = "36HA0201";// 成本台账
	public static final String GOUFANGWEIKUAN = "36H2040221";// 购房尾款资产循环购买
																// 36HA0201为测试环境节点编码
	
	public static final String CONTRAT_PAY_DETAIL = "36HA0202";// 合同付款明细（成本报表）
	
	public static final String CHENGBENTAIZHANG2 = "36HA0209";// 成本台账(不分业态)
	
	public static final String FENBAO = "36HA0207" ;// 总分包成本台账

	/**
	 * 从查询对话框中获取用户设置信息
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
		// update by tanghw at 2011-3-28 改为设置报表参数
		Parameter[] params = ReportVariableHelper.getParams(reportModel
				.getFormatModel());
		if (params != null && params.length > 0) {
			params = ((BaseNormalQueryObject) ((ReportNormalQueryPanel) getNormalPanel())
					.getNormalQueryObject()).getReportParameter();
			// 将参数值写入变量池
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

		// 2012-11-1 sunhld 设置conditionVOs到context中供报表变量使用
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
							+ "-01-01 至 "
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
	 * 根据指定查询方案进行查询 （IQueryAction接口的方法的实现）。
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
										+ "-01-01 至 "
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
		// 获取查询模板条件
		String whereSql = queryDlg.getWhereSQL();
		if (whereSql == null || whereSql.trim().length() == 0)
			return null;

		// where条件直接处理成筛选描述器
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
	 * 初始化条件
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
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// 押金保证金台帐
			String orgName = "area";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));

		} else if (HYDROPOWERDEDUCTIONINV.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// 水电自动划扣发票明细（成本报表）
			String orgName = "area";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		} else if (YUSUANRICHANG.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// 预算日常执行明细表
			String orgName = "dimension";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
			String expensesub = "expensesub";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(expensesub,
					new ExpensesubFilter());

			// 设置交易类型参照条件
			String billtype = "billtype";
			new QueryConditionDLGDelegator(dlg).setRefFilter(billtype,
					new BilltypeFilter());
		} else if (YINZIAOGUANLI.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// 预算日常执行明细表
			String orgName = "region";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
			String expensesub = "courseTitle";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(expensesub,
					new ExpensesubFilter());
		} else if (CREDIT.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// 信贷融资费用发票明细
			/*
			 * String orgName = "companyName"; // 设置区域公司参照条件 new
			 * QueryConditionDLGDelegator(dlg).setRefFilter(orgName, new
			 * MainOrgWithPermissionOrgFilter((FuncSubInfo) context
			 * .getAttribute(ComContextKey.FUNC_NODE_INFO)));
			 */
			String expensesub = "costtype";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					expensesub,
					new CreditReferToFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		} else if (SALECONTRACT.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {
			String orgName = "vdef14";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		} else if (SALEPAYCONTRACT.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {
			String orgName = "vdef14";
			// 设置区域公司参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		} else if (FINANCESUBDETAIL.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())) {// 费用科目明细表
			String expensesub = "expenseaccount";
			// 设置费用科目参照条件
			new QueryConditionDLGDelegator(dlg).setRefFilter(expensesub,
					new ExpensesubFilter());
		} else if (CHENGBENTAIZHANG.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())||CONTRAT_PAY_DETAIL.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())||CHENGBENTAIZHANG2.equals(((FuncSubInfo) context
						.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())||FENBAO.equals(funCode)) {// 费用科目明细表
			String pk_org = "pk_org";
			String pk_project = "pk_project";
			QueryTempMainOrgFilterBaseDoc baseDocByOrgFileter = new QueryTempMainOrgFilterBaseDoc(
					new QueryConditionDLGDelegator(dlg), pk_org, pk_project);
			baseDocByOrgFileter.addEditorListener();
		}else if(GOUFANGWEIKUAN.equals(((FuncSubInfo) context
				.getAttribute(ComContextKey.FUNC_NODE_INFO)).getFuncode())){//购房尾款资产循环购买
			String orgName = "城市公司";
			new QueryConditionDLGDelegator(dlg).setRefFilter(
					orgName,
					new MainOrgWithPermissionOrgFilter((FuncSubInfo) context
							.getAttribute(ComContextKey.FUNC_NODE_INFO)));
		}
		// end

	}

}
