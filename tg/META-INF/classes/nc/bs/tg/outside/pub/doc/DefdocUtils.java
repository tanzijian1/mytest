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
	 * ������֯�����ȡ��֯pk����֯�汾pk
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
	 * ����ebs�������ݴ�������name��ȡ��֯pk
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
	 * ���ݴ������ƻ�ȡ�Զ��嵵���б�pk
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
	 * ���ݴ�������ȡ�Զ��嵵���б�pk
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
	 * ��ȡ�ϼ���֧��Ŀ���ƺͱ���
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
	 * ��ȡ�ϼ�Ԥ���Ŀ���ƺͱ���
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
	 * ��ȡ��֧��Ŀ����
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
		sql.append(" bd1.pk_inoutbusiclass pk_inoutbusiclass, ");// ��֧��Ŀpk
		sql.append(" bd1.code inoutbusiclass_code, ");// ��֧��Ŀ����
		sql.append(" bd1.name inoutbusiclass_name, ");// ��֧��Ŀ����
		sql.append(" bd1.enablestate enablestate, ");// ����״̬
		sql.append(" bd1.memo memo, ");// ��ע
		sql.append(" bd1.ts ts, ");// ����޸�ʱ��
		sql.append(" bd2.pk_parent pk_parent, ");// �ϼ���֧��Ŀ����
		sql.append(" bd2.code parent_code, ");// �ϼ���֧��Ŀ����
		sql.append(" bd2.name parent_name, ");// �ϼ���֧��Ŀ����
		sql.append(" (case when bd1.def1 = 'Y' then '��' else '��' end) def1, ");// �Ƿ���Ϣ��Ŀ
		sql.append(" (case when bd1.def2 = 'Y' then '��' else '��' end) def2, ");// �Ƿ��ͬ���
		sql.append(" (case when bd1.def3 = 'Y' then '��' else '��' end) def3, ");// �Ƿ񲻲��뵼��
		sql.append(" (case when bd1.def4 = 'Y' then '��' else '��' end) def4 ");// �Ƿ�������д
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
	 * ��ȡ�ϼ��������ƺͱ���
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
	 * ��ȡ�ϼ��������ƺͱ���
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
	 * ��ȡ�ϼ��������ƺͱ���
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
	 * ��ȡ�����б����ƺͱ���
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
	 * ��ȡԤ���Ŀ�Զ��嵵��
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public List<Map> getBudgetSubject(String starttime, String endtime)
			throws DAOException {
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select tb_budgetsub.pk_org as pk_org, ");// ������֯
		sqlb.append(" tb_budgetsub.pk_obj as pk_obj, ");// ��Ŀ����
		sqlb.append(" tb_budgetsub.objcode as objcode, ");// ��Ŀ����
		sqlb.append(" tb_budgetsub.objname as objname, ");// ��Ŀ����
		sqlb.append(" tb_budgetsub2.objname as parent_name, ");// �ϼ���Ŀ����
		sqlb.append(" tb_budgetsub.pk_parent as pk_parent, ");// �ϼ���Ŀ
		sqlb.append(" tb_budgetsub.def6 as pk_inoutbusiclass, ");// ��֧��Ŀ����
		sqlb.append(" bd_inoutbusiclass.code as inoutbusiclass_code, ");// ��֧��Ŀ����
		sqlb.append(" bd_inoutbusiclass.name as inoutbusiclass_name, ");// ��֧��Ŀ����
		sqlb.append(" tb_budgetsub.enablestate as enablestate, ");// ״̬[1:δ����,2:������,3:ͣ��]
		sqlb.append(" tb_budgetsub.ts as ts, ");// ����޸�ʱ��
		sqlb.append(" defdoc1.name as def1, ");// ���÷�����
		sqlb.append(" defdoc2.name as def2, ");// ��ͬ�������
		sqlb.append(" defdoc3.name as def3, ");// �Ǻ�ͬ�������
		sqlb.append(" tb_budgetsub.issystem as issystem, ");// �Ƿ�Ԥ��
		sqlb.append(" tb_budgetsub.createddate as createddate, ");// ��������
		sqlb.append(" v.nlevel ");// ��������
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
	 * ��ȡ�Զ��嵵����ͼ
	 * 
	 * @param ts
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public List<Map> getBdDefdoc(String code, String starttime, String endtime)
			throws DAOException {
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");// ������֯
		sqlb.append(" bd_defdoc.pk_defdoc pk_defdoc, ");// �Զ��嵵������
		sqlb.append(" bd_defdoc.code code, ");// �Զ��嵵������
		sqlb.append(" bd_defdoc.enablestate enablestate, ");// ״̬[1:δ����,2:������,3:ͣ��]
		sqlb.append(" bd_defdoc.memo memo, ");// ��ע
		sqlb.append(" bd_defdoc.ts ts, ");// ����޸�ʱ��
		sqlb.append(" bd_defdoc1.pk_defdoc pk_parnet, ");// �ϼ���������
		sqlb.append(" bd_defdoc1.code parnet_code, ");// �ϼ���������
		sqlb.append(" bd_defdoc1.name parnet_name, ");// �ϼ���������
		sqlb.append(" bd_defdoclist.pk_defdoclist, ");// �Զ��嵵���б�����
		sqlb.append(" bd_defdoclist.code defdoclist_code, ");// �Զ��嵵���б����
		sqlb.append(" bd_defdoclist.name defdoclist_name ");// �Զ��嵵���б�����
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
	 * ��ȡ�����˻���Ϣ
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public List<Map> getBankaccbas(HashMap<String, Object> value)
			throws DAOException {
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select bd_bankaccbas.accname accname,");// ����
		sqlb.append("bd_bankaccbas.accnum accnum,");// �˺�
		sqlb.append("bd_bankaccbas.pk_bankaccbas pk_bankaccbas,");// �����˻�����
		sqlb.append("bd_bankaccbas.code bankaccount_code,");// �����˻�����
		sqlb.append("bd_bankaccbas.name bankaccount_name,");// �����˻�����
		sqlb.append("bd_bankaccbas.pk_org pk_org,");// ��֯
		sqlb.append("bd_bankaccbas.ts ts,");// ʱ���
		sqlb.append("org_orgs.code org_code,");// ��֯����
		sqlb.append("org_orgs.name org_name,");// ��֯����
		sqlb.append("bd_bankaccbas.pk_bankdoc pk_bankdoc,");// ������������
		sqlb.append("bd_bankdoc.code bankdoc_code,");// �������б���
		sqlb.append("bd_bankdoc.name bankdoc_name,");// ������������
		sqlb.append("bd_bankaccbas.pk_banktype pk_banktype,");// �����������
		sqlb.append("bd_banktype.code banktype_code,");// �������б���
		sqlb.append("bd_banktype.name banktype_name,");
		;// ������������
		sqlb.append("bd_bankaccbas.financeorg financeorg,");// ������λ����
		sqlb.append("finorg.code financeorg_code,");// ������λ����
		sqlb.append("finorg.name financeorg_name,");// ������λ����
		sqlb.append("bd_bankaccbas.controlorg controlorg,");// ���������֯����
		sqlb.append("conorg.code controlorg_code,");// ���������֯����
		sqlb.append("conorg.name controlorg_name ");// ���������֯����
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
	 * ��ȡ���������˻���Ϣ
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		JSONArray jsonArray=JSONArray.parseArray(JSONObject.toJSONString(list));
		// TODO �Զ����ɵķ������
		return jsonArray;
	}

	
	
	
	/**
	 * ����vo��
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public int updateVO(SuperVO vo) throws DAOException {
		return new BaseDAO().updateVO(vo);
	}

	/**
	 * ��ѯ��Ŀ����pk
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
	 * ��ѯEPS
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
	 * ��ȡ��˾���ƺͱ���
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
	 * ��ѯ�������
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
	 * ��ѯ��������
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
	 * ��ѯ������λ
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
	 * ���ݿ�־û�
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
