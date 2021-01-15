package nc.bs.tg.outside.pub.doc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.tg.fund.ebs.query.PsnBankAccountQueryVO;
import nc.vo.tg.outside.EBSDefdocVO;

public class DefdocUtils {

	static DefdocUtils utils;

	BaseDAO baseDAO = null;

	public static DefdocUtils getUtils() {
		if (utils == null) {
			utils = new DefdocUtils();
		}
		return utils;
	}

	/**
	 * 根据组织编码获取组织pk和组织版本pk
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getOrg(String org_code) throws DAOException {
		String sql = "select distinct pk_org,pk_vid from org_orgs where code = '"
				+ org_code + "' and nvl(dr,0) = 0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 根据ebs或主数据传过来的name获取组织pk
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public String getPk_org(String org_code) throws DAOException {
		return (String) new BaseDAO().executeQuery(
				"select pk_org from org_orgs where code = '" + org_code
						+ "' and nvl(dr,0) = 0 ", new ColumnProcessor());
	}

	/**
	 * 根据传入名称获取自定义档案列表pk
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public String getDefdoclist(String dectype) throws DAOException {
		return (String) new BaseDAO().executeQuery(
				"select pk_defdoclist from bd_defdoclist where name = '"
						+ dectype + "' and nvl(dr,0) = 0 ",
				new ColumnProcessor());
	}

	/**
	 * 根据传入编码获取自定义档案列表pk
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public String getDefdoclistByCode(String code) throws DAOException {
		return (String) new BaseDAO().executeQuery(
				"select pk_defdoclist from bd_defdoclist where code = '" + code
						+ "' and nvl(dr,0) = 0 ", new ColumnProcessor());
	}

	public Map<String, Object> getDefdocByCode(String listCode, String code)
			throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql.append("select code,name,pk_defdoc from bd_defdoc where bd_defdoc.pk_defdoclist in ( ");
		sql.append("select bd_defdoclist.pk_defdoclist from bd_defdoclist where code = '"
				+ listCode + "') and code = '" + code + "' ");
		Map<String, Object> infoMap = (Map<String, Object>) getBaseDAO()
				.executeQuery(sql.toString(), new MapProcessor());
		return infoMap;
	}

	/**
	 * 获取上级收支项目名称和编码
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getInoutbusiClassParent(String pk_parent)
			throws DAOException {
		String sql = "select distinct name,code from bd_inoutbusiclass where pk_inoutbusiclass = '"
				+ pk_parent + "' and nvl(dr,0) = 0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 获取上级预算科目名称和编码
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getBudgetSubjectCode(String pk_parent)
			throws DAOException {
		String sql = "select distinct objname name,objcode code from tb_budgetsub where pk_obj = '"
				+ pk_parent + "' and nvl(dr,0) = 0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 获取收支项目档案
	 * 
	 * @param string
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public List<Map> getBdInoutbusi(String starttime, String endtime)
			throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(" bd1.pk_inoutbusiclass pk_inoutbusiclass, ");// 收支项目pk
		sql.append(" bd1.code inoutbusiclass_code, ");// 收支项目编码
		sql.append(" bd1.name inoutbusiclass_name, ");// 收支项目名称
		sql.append(" bd1.enablestate enablestate, ");// 启用状态
		sql.append(" bd1.memo memo, ");// 备注
		sql.append(" bd1.ts ts, ");// 最后修改时间
		sql.append(" bd2.pk_parent pk_parent, ");// 上级收支项目主键
		sql.append(" bd2.code parent_code, ");// 上级收支项目编码
		sql.append(" bd2.name parent_name, ");// 上级收支项目名称
		sql.append(" (case when bd1.def1 = 'Y' then '是' else '否' end) def1, ");// 是否利息项目
		sql.append(" (case when bd1.def2 = 'Y' then '是' else '否' end) def2, ");// 是否合同理财
		sql.append(" (case when bd1.def3 = 'Y' then '是' else '否' end) def3, ");// 是否不参与导出
		sql.append(" (case when bd1.def4 = 'Y' then '是' else '否' end) def4 ");// 是否无需填写
		sql.append(" from ");
		sql.append("  bd_inoutbusiclass bd1 ");
		sql.append("  left join ");
		sql.append("  bd_inoutbusiclass bd2 on bd1.pk_inoutbusiclass = bd2.pk_parent ");
		if (StringUtils.isNotBlank(starttime)
				&& StringUtils.isNotBlank(endtime)) {
			sql.append("  where nvl(bd1.dr,0) = 0 and nvl(bd2.dr,0) = 0 and bd1.ts>= '"
					+ starttime + "' and bd1.ts<= '" + endtime + "' ");
		} else if (StringUtils.isBlank(starttime)
				&& StringUtils.isNotBlank(endtime)) {
			sql.append("  where nvl(bd1.dr,0) = 0 and nvl(bd2.dr,0) = 0 and bd1.ts<= '"
					+ endtime + "' ");
		} else if (StringUtils.isNotBlank(starttime)
				&& StringUtils.isBlank(endtime)) {
			sql.append("  where nvl(bd1.dr,0) = 0 and nvl(bd2.dr,0) = 0 and bd1.ts>= '"
					+ starttime + "' ");
		} else {
			sql.append("  where nvl(bd1.dr,0) = 0 and nvl(bd2.dr,0) = 0 ");
		}
		List<Map> infoMap = (List<Map>) getBaseDAO().executeQuery(
				sql.toString(), new MapListProcessor());
		return infoMap;
	}

	/**
	 * 获取上级档案名称和编码
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getDefdoc(String pk_parent) throws DAOException {
		String sql = "select distinct name,code from bd_defdoc where pk_defdoc = '"
				+ pk_parent + "' and nvl(dr,0) = 0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 获取上级档案名称和编码
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getDefdocPranetPk(String ebs_parnet_id,
			String code) throws DAOException {
		// String sql = "select pk_defdoc from bd_defdoc where (code = '" +
		// parent_code + "'  or name = '" + parent_name+
		// "')  and isnull(dr,0)=0 ";
		String sql = "select pk_defdoc from bd_defdoc where def1 = '"
				+ ebs_parnet_id + "'  and pk_defdoclist = '" + code
				+ "' and isnull(dr,0)=0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 获取上级档案名称和编码
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getDefdocPranetPkByVO(EBSDefdocVO defdocheadVO,
			String pk_defdoclist) throws DAOException {
		String sql = "select pk_defdoc from bd_defdoc where def1 = '"
				+ defdocheadVO.getEbs_parent_id() + "' and pk_defdoclist = '"
				+ pk_defdoclist + "' and isnull(dr,0)=0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 获取档案列表名称和编码
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getDefdocList(String pk_defdoclist)
			throws DAOException {
		String sql = "select distinct name,code from bd_defdoclist where pk_defdoclist = '"
				+ pk_defdoclist + "' and nvl(dr,0) = 0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 获取预算科目自定义档案
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public List<Map> getBudgetSubject(String starttime, String endtime)
			throws DAOException {
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select tb_budgetsub.pk_org as pk_org, ");// 创建组织
		sqlb.append(" tb_budgetsub.pk_obj as pk_obj, ");// 科目主键
		sqlb.append(" tb_budgetsub.objcode as objcode, ");// 科目编码
		sqlb.append(" tb_budgetsub.objname as objname, ");// 科目名称
		sqlb.append(" tb_budgetsub2.objname as parent_name, ");// 上级科目名称
		sqlb.append(" tb_budgetsub.pk_parent as pk_parent, ");// 上级科目
		sqlb.append(" tb_budgetsub.def6 as pk_inoutbusiclass, ");// 收支项目主键
		sqlb.append(" bd_inoutbusiclass.code as inoutbusiclass_code, ");// 收支项目编码
		sqlb.append(" bd_inoutbusiclass.name as inoutbusiclass_name, ");// 收支项目名称
		sqlb.append(" tb_budgetsub.enablestate as enablestate, ");// 状态[1:未启用,2:已启用,3:停用]
		sqlb.append(" tb_budgetsub.ts as ts, ");// 最后修改时间
		sqlb.append(" defdoc1.name as def1, ");// 差旅费流程
		sqlb.append(" defdoc2.name as def2, ");// 合同情况流程
		sqlb.append(" defdoc3.name as def3, ");// 非合同情况流程
		sqlb.append(" tb_budgetsub.issystem as issystem, ");// 是否预置
		sqlb.append(" tb_budgetsub.createddate as createddate, ");// 创建日期
		sqlb.append(" v.nlevel ");// 创建日期
		sqlb.append(" from tb_budgetsub ");
		sqlb.append(" inner join (select level nlevel , pk_obj from tb_budgetsub where dr = 0 start with pk_obj in (select pk_obj from tb_budgetsub  where pk_parent = '~') ");
		sqlb.append(" connect by prior pk_obj  = pk_parent ) v on v.pk_obj =  tb_budgetsub.pk_obj ");
		sqlb.append(" left join bd_inoutbusiclass on tb_budgetsub.def6 = bd_inoutbusiclass.pk_inoutbusiclass ");
		sqlb.append(" left join bd_defdoc defdoc1 on tb_budgetsub.def1 = defdoc1.pk_defdoc ");
		sqlb.append(" left join bd_defdoc defdoc2 on tb_budgetsub.def2 = defdoc2.pk_defdoc ");
		sqlb.append(" left join bd_defdoc defdoc3 on tb_budgetsub.def3 = defdoc3.pk_defdoc ");
		sqlb.append(" left join tb_budgetsub tb_budgetsub2 on tb_budgetsub.pk_obj = tb_budgetsub2.pk_parent ");
		sqlb.append(" where nvl(tb_budgetsub.dr,0) = 0 and nvl(bd_inoutbusiclass.dr,0) =0 ");
		if (StringUtils.isNotBlank(starttime)
				&& StringUtils.isNotBlank(endtime)) {
			sqlb.append("  and nvl(defdoc1.dr,0) = 0 and tb_budgetsub.ts>= '"
					+ starttime + "' and tb_budgetsub.ts <= '" + endtime + "' ");
		} else if (StringUtils.isBlank(starttime)
				&& StringUtils.isNotBlank(endtime)) {
			sqlb.append("  and nvl(defdoc1.dr,0) = 0 and  tb_budgetsub.ts <= '"
					+ endtime + "' ");
		} else if (StringUtils.isNotBlank(starttime)
				&& StringUtils.isBlank(endtime)) {
			sqlb.append("  and nvl(defdoc1.dr,0) = 0 and tb_budgetsub.ts>= '"
					+ starttime + "'  ");
		} else {
			sqlb.append(" and nvl(defdoc1.dr,0) = 0 ");
		}
		sqlb.append(" order by bd_inoutbusiclass.code ");
		List<Map> infoMap = (List<Map>) getBaseDAO().executeQuery(
				sqlb.toString(), new MapListProcessor());
		return infoMap;
	}

	/**
	 * 获取自定义档案视图
	 * 
	 * @param ts
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public List<Map> getBdDefdoc(String code, String starttime, String endtime)
			throws DAOException {
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");// 创建组织
		sqlb.append(" bd_defdoc.pk_defdoc pk_defdoc, ");// 自定义档案主键
		sqlb.append(" bd_defdoc.code code, ");// 自定义档案编码
		sqlb.append(" bd_defdoc.enablestate enablestate, ");// 状态[1:未启用,2:已启用,3:停用]
		sqlb.append(" bd_defdoc.memo memo, ");// 备注
		sqlb.append(" bd_defdoc.ts ts, ");// 最后修改时间
		sqlb.append(" bd_defdoc1.pk_defdoc pk_parnet, ");// 上级档案主键
		sqlb.append(" bd_defdoc1.code parnet_code, ");// 上级档案编码
		sqlb.append(" bd_defdoc1.name parnet_name, ");// 上级档案名称
		sqlb.append(" bd_defdoclist.pk_defdoclist, ");// 自定义档案列表主键
		sqlb.append(" bd_defdoclist.code defdoclist_code, ");// 自定义档案列表编码
		sqlb.append(" bd_defdoclist.name defdoclist_name ");// 自定义档案列表名称
		sqlb.append(" from bd_defdoc ");
		sqlb.append(" left join bd_defdoclist on bd_defdoc.pk_defdoclist = bd_defdoclist.pk_defdoclist ");
		sqlb.append(" left join bd_defdoc bd_defdoc1 on bd_defdoc.pid = bd_defdoc1.pk_defdoc ");
		sqlb.append(" where nvl(bd_defdoc.dr,0) = 0 and nvl(bd_defdoclist.dr,0) = 0 and nvl(bd_defdoc1.dr,0) = 0 ");
		if (StringUtils.isNotBlank(starttime)
				&& StringUtils.isNotBlank(endtime)) {
			sqlb.append("  and bd_defdoclist.code='" + code
					+ "' and bd_defdoc.ts>= '" + starttime
					+ "' and bd_defdoc.ts<= '" + endtime + "' ");
		} else if (StringUtils.isBlank(starttime)
				&& StringUtils.isNotBlank(endtime)) {
			sqlb.append("  and bd_defdoclist.code='" + code
					+ "' and  bd_defdoc.ts<= '" + endtime + "' ");
		} else if (StringUtils.isNotBlank(starttime)
				&& StringUtils.isBlank(endtime)) {
			sqlb.append("  and bd_defdoclist.code='" + code
					+ "' and bd_defdoc.ts>= '" + starttime + "' ");
		} else {
			sqlb.append(" and bd_defdoclist.code='" + code + "' ");
		}
		List<Map> infoMap = (List<Map>) getBaseDAO().executeQuery(
				sqlb.toString(), new MapListProcessor());
		return infoMap;
	}

	/**
	 * 获取银行账户信息
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public List<Map> getBankaccbas(HashMap<String, Object> value)
			throws DAOException {
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select bd_bankaccbas.accname accname,");// 户名
		sqlb.append("bd_bankaccbas.accnum accnum,");// 账号
		sqlb.append("bd_bankaccbas.pk_bankaccbas pk_bankaccbas,");// 银行账户编码
		sqlb.append("bd_bankaccbas.code bankaccount_code,");// 银行账户编码
		sqlb.append("bd_bankaccbas.name bankaccount_name,");// 银行账户名称
		sqlb.append("bd_bankaccbas.pk_org pk_org,");// 组织
		sqlb.append("bd_bankaccbas.ts ts,");// 时间戳
		sqlb.append("org_orgs.code org_code,");// 组织编码
		sqlb.append("org_orgs.name org_name,");// 组织名称
		sqlb.append("bd_bankaccbas.pk_bankdoc pk_bankdoc,");// 开户银行主键
		sqlb.append("bd_bankdoc.code bankdoc_code,");// 开户银行编码
		sqlb.append("bd_bankdoc.name bankdoc_name,");// 开户银行名称
		sqlb.append("bd_bankaccbas.pk_banktype pk_banktype,");// 银行类别主键
		sqlb.append("bd_banktype.code banktype_code,");// 开户银行编码
		sqlb.append("bd_banktype.name banktype_name,");
		;// 开户银行名称
		sqlb.append("bd_bankaccbas.financeorg financeorg,");// 开户单位主键
		sqlb.append("finorg.code financeorg_code,");// 开户单位编码
		sqlb.append("finorg.name financeorg_name,");// 开户单位名称
		sqlb.append("bd_bankaccbas.controlorg controlorg,");// 核算归属组织主键
		sqlb.append("conorg.code controlorg_code,");// 核算归属组织编码
		sqlb.append("conorg.name controlorg_name ");// 核算归属组织名称
		sqlb.append("from bd_bankaccbas ");
		sqlb.append("left join org_orgs on bd_bankaccbas.pk_org = org_orgs.pk_org ");
		sqlb.append("left join bd_bankdoc on bd_bankaccbas.pk_bankdoc = bd_bankdoc.pk_bankdoc ");
		sqlb.append("left join bd_banktype on bd_bankaccbas.pk_banktype = bd_banktype.pk_banktype ");
		sqlb.append("left join org_financeorg finorg on bd_bankaccbas.financeorg = finorg.pk_financeorg ");
		sqlb.append("left join org_financeorg conorg on bd_bankaccbas.controlorg = conorg.pk_financeorg ");
		sqlb.append("where nvl(bd_bankaccbas.dr,0)=0 and nvl(org_orgs.dr,0)=0 and nvl(conorg.dr,0)=0 "
				+ "and nvl(bd_bankdoc.dr,0)=0 and nvl(bd_banktype.dr,0)=0 and nvl(finorg.dr,0)=0 ");

		if (!"".equals(value.get("starttime"))
				&& value.get("starttime") != null) {
			String starttime = (String) value.get("starttime");
			sqlb.append(" and bd_bankaccbas.ts>='" + starttime + "'");
		}

		if (!"".equals(value.get("endtime")) && value.get("endtime") != null) {
			String endtime = (String) value.get("endtime");
			sqlb.append(" and bd_bankaccbas.ts<='" + endtime + "'");
		}

		List<Map> infoMap = (List<Map>) getBaseDAO().executeQuery(
				sqlb.toString(), new MapListProcessor());
		return infoMap;
	}

	
	/**
	 * 获取个人银行账户信息
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public JSONArray getPsnBankacc(String accountcode)
			throws DAOException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
				.append("  select f.code as  PERSON_NUMBER,"
						+ "g.name as FVENDOR_OPEN_BANK_NAME,"
						+ "g.code  as FVENDOR_OPEN_BANK_CODE,"
						+ "d.accnum  as  fvendor_bank_code,"
						+ " d.accname as FBANK_ACCOUNT_NAME,"
						+ "c.ts      ,"
						+ "c.pk_psnbankacc as   nc_key_id  ,"
						+ " case  WHEN  e.enablestate='2' THEN 'Y'  when e.enablestate='3' THEN 'N' end as enable_flag "
						+ "  from bd_psnbankacc c, bd_bankaccsub d,bd_bankaccbas e,bd_psndoc f ,bd_bankdoc g "
						+ "   where c.pk_bankaccsub = d.pk_bankaccsub and c.pk_bankaccbas=e.pk_bankaccbas and c.pk_psndoc=f.pk_psndoc and e.pk_bankdoc=g.pk_bankdoc "
						+ " and c.dr = d.dr  and c.dr=e.dr and c.dr=g.dr and c.dr=e.dr and c.dr = 0");
		List<PsnBankAccountQueryVO> list = null;
		if (accountcode != null) {
			stringBuffer.append(" and f.code ='" + accountcode + "'");
		}
		try {
			list = (List<PsnBankAccountQueryVO>) new BaseDAO().executeQuery(
					stringBuffer.toString(), new BeanListProcessor(PsnBankAccountQueryVO.class));
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		JSONArray jsonArray=JSONArray.parseArray(JSONObject.toJSONString(list));
		// TODO 自动生成的方法存根
		return jsonArray;
	}

	
	
	
	/**
	 * 更新vo类
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public int updateVO(SuperVO vo) throws DAOException {
		return new BaseDAO().updateVO(vo);
	}

	/**
	 * 查询项目类型pk
	 * 
	 * @param string
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public String getProjectclass(String projectCode) throws DAOException {
		return (String) new BaseDAO().executeQuery(
				"select pk_projectclass from bd_projectclass where type_code = '"
						+ projectCode + "' and  nvl(dr,0) = 0 ",
				new ColumnProcessor());
	}

	/**
	 * 查询EPS
	 * 
	 * @param string
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public String getEPSPk(String epsCode, String epsName) throws DAOException {
		return (String) new BaseDAO().executeQuery(
				"select pk_eps from pm_eps where  (eps_code = '" + epsCode
						+ "' and nvl(dr,0) = 0) or (eps_name = '" + epsName
						+ "' and nvl(dr,0) = 0) ", new ColumnProcessor());
	}

	/**
	 * 获取公司名称和编码
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getOrgmsg(String code) throws BusinessException {
		String sql = "select pk_org,code,name from org_orgs  where code = '"
				+ code + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 查询银行类别
	 * 
	 * @param banktype
	 * @return
	 * @throws DAOException
	 */
	public String getBanktype(String banktype_code) throws DAOException {//
		return (String) new BaseDAO().executeQuery(
				"select pk_banktype from bd_banktype where (code ='"
						+ banktype_code + "' or name='" + banktype_code
						+ "') and nvl(dr,0) = 0 ", new ColumnProcessor());
	}

	/**
	 * 查询开户银行
	 * 
	 * @param pk_bankdoc
	 * @return
	 * @throws DAOException
	 */
	public String getPk_bankdoc(String pk_bankdoc) throws DAOException {
		return (String) new BaseDAO().executeQuery(
				"select pk_bankdoc from bd_bankdoc where name = '" + pk_bankdoc
						+ "' and nvl(dr,0) = 0 ", new ColumnProcessor());
	}

	/**
	 * 查询开户单位
	 * 
	 * @param financeorg
	 * @return
	 * @throws DAOException
	 */
	public String getFinanceorg(String financeorg) throws DAOException {
		return (String) new BaseDAO().executeQuery(
				"select pk_financeorg from org_financeorg where name = '"
						+ financeorg + "' and nvl(dr,0) = 0 ",
				new ColumnProcessor());
	}

	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
}
