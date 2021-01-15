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
 * ��ѯģ������֯��ѯ������Χ �����ڵ���ģ������ʱһ��
 * 
 * �ڳ�ʼ����ѯģ���IQueryConditionDLGInitializerʵ�����м���
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
		// �ж��Ƿ��Ӧ�ı����ܽڵ�
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
		// ��ѯ������˾���ϼ����й�˾
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

		if (refPane.getRefModel().getRefNodeName().contains("��������˲�")) {
			refPane.getRefModel().setFilterPks(
					this.getNeedShowAccountingBookPks(),
					IFilterStrategy.INSECTION);
		} else {
			refPane.getRefModel().setFilterPks(this.getNeedShowOrgPks(),
					IFilterStrategy.INSECTION);

		}
	}

	/**
	 * Ȩ�޶�ȡ�ܵ�����ע���ڵ�����Ӱ��,���������������ȡƾ֤�Ƶ���Ȩ��ǿ�ƶ�ȡȨ��
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