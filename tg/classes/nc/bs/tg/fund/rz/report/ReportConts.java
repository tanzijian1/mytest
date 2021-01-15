package nc.bs.tg.fund.rz.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.tgfp.report.ReportOrgVO;

/**
 * 报表
 * @author acer
 *
 */
public class ReportConts {
	
	static ReportConts utils;

	public static ReportConts getUtils() {
		if (utils == null) {
			utils = new ReportConts();
		}
		return utils;
	}

	public static final String CASHPLEDGE = "36HA0203"; // 押金保证金台帐
	
	IUAPQueryBS bs = null;
	
	public IUAPQueryBS getQueryService() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(
					IUAPQueryBS.class);
		}
		return bs;
	}
	
	/**
	 * 读取业务单元归属的下级业务单元
	 * 
	 * @param secondaryOrgList
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<String> getSubordinateOrgMap()
			 {
		StringBuffer sql = new StringBuffer();
		sql.append("select  v.pk_financeorg ")
				.append("from ( " + getFinanceOrgSQL() + ")  v")
				.append(" start with v.def3 = (select pk_defdoc from bd_defdoc where name = '是' and code = '01') ")
				.append(" connect by prior v.pk_fatherorg = v.pk_financeorg");
//		SQLParameter parameter = new SQLParameter();
//		parameter.addParam(pk_fatherorg);
		List<String> list = null;
		try {
			list = (List<String>) getQueryService().executeQuery(
					sql.toString(),  new ColumnListProcessor());
		} catch (BusinessException e) {
			nc.bs.logging.Logger.debug("=============报表查询业务单元失败，"+e.getMessage());
		}

		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFinanceOrgSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT org_financeorg.pk_financeorg ")
				.append(", org_orgs.code")
				.append(", org_orgs.def3 ")
				.append(", org_orgs.pk_fatherorg")
				.append(" FROM org_financeorg ")
				.append(" inner join org_orgs  on org_financeorg.pk_financeorg = org_orgs.pk_org ")
				.append("  WHERE org_financeorg.enablestate = 2 and  org_orgs.enablestate = 2  order by org_orgs.code  ");
		return sql.toString();
	}
	
	/**
	 * 获取集团pk
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getorg(String code) throws BusinessException {
		String sqlorg = " select pk_org from org_orgs where code = '" + code
				+ "' ";// 默认设置集团值
		String orgpk = (String) getQueryService().executeQuery(sqlorg,
				new ColumnProcessor());// 集团
		return orgpk;
	}
	
	/**
	 * 查询次级业务单元信息
	 * @param secondaryOrgList
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "unchecked" })
	public Map<String, List<String>> getSecondaryOrgList(
			List<String> secondaryOrgList) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select a.pk_financeorg from   (" + getFinanceOrgSQL() + ") a")
				.append("  where a.pk_fatherorg = ?");
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (secondaryOrgList != null && secondaryOrgList.size() > 0) {
			for (String pk_financeorg : secondaryOrgList) {
				SQLParameter parameter = new SQLParameter();
				parameter.addParam(pk_financeorg);
				List<String> list = (List<String>) getQueryService().executeQuery(
						sql.toString(), parameter, new ColumnListProcessor());
				map.put(pk_financeorg, list == null ? new ArrayList<String>()
						: list);
			}
		}
		return map;
	}
	
	/**
	 * 查询出区域公司
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<String> getRegionOrgList() throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select pk_org from org_orgs where dr = 0 and enablestate ='2'")
				.append(" and def3 = (select pk_defdoc from bd_defdoc where name = '是' and code = '01')");
		List<String> list = (List<String>) getQueryService().executeQuery(
				sql.toString(), new ColumnListProcessor());
		return list == null || list.size() == 0 ? new ArrayList<String>()
				: list;
	}
	
	/**
	 * 取业务单元及对应父级的关联
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getFinanecMap() throws BusinessException {
		String sql = "select org_orgs.pk_org,org_orgs.pk_fatherorg from org_orgs "
				+ " inner join org_financeorg  on org_orgs.pk_org = org_financeorg.pk_financeorg "
				+ " where org_orgs.dr = 0 and org_orgs.enablestate = 2 and org_financeorg.dr =0 ";
		List<Map<String, String>> value = (List<Map<String, String>>) getQueryService()
				.executeQuery(sql, new MapListProcessor());
		Map<String, String> financeMap = new HashMap<String, String>();
		if (value != null && value.size() > 0) {
			for (Map<String, String> map : value) {
				financeMap.put((String) map.get("pk_org"),
						map.get("pk_fatherorg"));
			}
		}
		return financeMap;
	}
	
	/**
	 * 获得时代公司层次集合VO,物业公司存在第五层次
	 * @return
	 * @throws BusinessException
	 */
	public List<ReportOrgVO> getOrgVOs() throws BusinessException{
		ReportConts reportUtils = ReportConts.getUtils();
		String pk_group = reportUtils.getorg("JT0001");// 集团主键
		Map<String, String> fatherMap = reportUtils.getFinanecMap();//获取业务单元及对应的父级单元
		List<String> regionList = reportUtils.getRegionOrgList();// 获得区域公司
		Map<String, List<String>> orgMap = reportUtils.getSecondaryOrgList(regionList);// 项目公司信息
		ReportOrgVO orgVO = new ReportOrgVO();
		orgVO.setPk_clique(pk_group);
		List<ReportOrgVO> volist = new ArrayList<ReportOrgVO>();
		for (String regionOrg : regionList) {
			String pk_plate = fatherMap.get(regionOrg);
			if(pk_plate != null){
				orgVO = (ReportOrgVO) orgVO.clone();
				orgVO.setPk_plate(pk_plate.equals(pk_group) ? regionOrg
						: pk_plate);// 板块公司
				orgVO.setPk_region(regionOrg.equals(orgVO.getPk_plate()) ? "~"
						: regionOrg);// 区域公司
				List<String> orgList = orgMap.get(regionOrg);
				if (orgList != null && orgList.size() > 0) {
					for (String org : orgList) {
						ReportOrgVO repOrgVO = (ReportOrgVO) orgVO.clone();
						repOrgVO.setPk_org(org);// 项目公司
						volist.add(repOrgVO);
					}
				} else {
					ReportOrgVO repOrgVO = (ReportOrgVO) orgVO.clone();
					repOrgVO.setPk_org(regionOrg);// 项目公司
					volist.add(repOrgVO);
				}
			}
		}
		return volist;
	}
}
