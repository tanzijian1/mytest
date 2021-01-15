package nc.ui.tg.pub.filter;

import java.util.ArrayList;
import java.util.List;

import nc.bs.tg.fund.rz.report.ReportConts;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.query2.refedit.IRefFilter;
import nc.ui.tg.report.ReportQueryAction;
import nc.vo.bd.ref.IFilterStrategy;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.tmpub.util.ArrayUtil;
import nc.vo.uap.rbac.FuncSubInfo;

/**
 * 查询模板主组织查询条件范围 保持于单据模板新增时一致
 * 
 * 在初始化查询模板的IQueryConditionDLGInitializer实现类中加入
 * condDLGDelegator.setRefFilter("pk_org", new
 * TMFuncPermissionOrgFilter(model));
 * 
 * @author xwq
 * 
 */
@SuppressWarnings("restriction")
public class MainOrgWithPermissionOrgFilter implements IRefFilter {

	private FuncSubInfo funcSubInfo;

	public MainOrgWithPermissionOrgFilter(FuncSubInfo info) {
		super();
		funcSubInfo = info;
	}

	@Override
	public void doFilter(UIRefPane refPane) {

		// add by tjl 2020-06-12
		// 判断是否对应的报表功能节点
		// if (ReportQueryAction.CASHPLEDGE.equals(funcSubInfo.getFuncode())
		// || ReportQueryAction.HYDROPOWERDEDUCTIONINV.equals(funcSubInfo
		// .getFuncode())
		// || ReportQueryAction.SALECONTRACT.equals(funcSubInfo
		// .getFuncode())
		// || ReportQueryAction.SALEPAYCONTRACT.equals(funcSubInfo
		// .getFuncode())
		// || ReportQueryAction.YUSUANRICHANG.equals(funcSubInfo
		// .getFuncode())
		// || ReportQueryAction.YINZIAOGUANLI.equals(funcSubInfo
		// .getFuncode())) {
		// 查询地区公司及上级所有公司
		List<String> pk_orgs = ReportConts.getUtils().getSubordinateOrgMap();
		if(ReportQueryAction.YUSUANRICHANG.equals(funcSubInfo.getFuncode())){
			pk_orgs.add("000112100000000024I0");
		}
		if (pk_orgs != null && pk_orgs.size() > 0) {
			StringBuffer sql = new StringBuffer();
			sql.append(" and exists (select 1 from org_financeorg v where "
					+ SQLUtil.buildSqlForIn(" v.pk_financeorg ",
							pk_orgs.toArray(new String[0]))
					+ " and org_financeorg_temp.pk_financeorg = v.pk_financeorg )");
			refPane.getRefModel().addWherePart(sql.toString());
		}

		if (refPane.getRefModel().getRefNodeName().contains("财务核算账簿")) {
			refPane.getRefModel().setFilterPks(
					this.getNeedShowAccountingBookPks(),
					IFilterStrategy.INSECTION);
		} else {
			refPane.getRefModel().setFilterPks(this.getNeedShowOrgPks(),
					IFilterStrategy.INSECTION);

		}
	}

	/**
	 * 权限读取受到功能注册内的类型影响,财务核算参数变更成取凭证制单的权限强制读取权限
	 * 
	 * @return
	 */
	protected String[] getNeedShowAccountingBookPks() {
		List<String> needShowOrgPks = new ArrayList<String>();
		String[] permissionPkorgs = funcSubInfo
				.getFuncPermissionPkorgsWithoutTypeFilter();
		if (!ArrayUtil.isNull(permissionPkorgs)) {
			for (String pkOrg : permissionPkorgs) {
				if (pkOrg != null && !"".equals(pkOrg)) {
					needShowOrgPks.add(pkOrg);
				}
			}

		}
		return needShowOrgPks.toArray(new String[0]);
	}

	protected String[] getNeedShowOrgPks() {
		List<String> needShowOrgPks = new ArrayList<String>();
		String[] permissionPkorgs = funcSubInfo.getFuncPermissionPkorgsWithoutTypeFilter();
		if (!ArrayUtil.isNull(permissionPkorgs)) {
			for (String pkOrg : permissionPkorgs) {
				if (pkOrg != null && !"".equals(pkOrg)) {
					needShowOrgPks.add(pkOrg);
				}
			}

		}
		return needShowOrgPks.toArray(new String[0]);
	}
}