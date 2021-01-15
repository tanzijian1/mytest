package nc.bs.tg.outside.word.utils;

import java.util.HashMap;
import java.util.List;

import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 组织权限表
 * 
 * @author ASUS
 * 
 */
public class QueryOrgUtils extends WordBillUtils {
	static QueryOrgUtils utils;

	public static QueryOrgUtils getUtils() {
		if (utils == null) {
			utils = new QueryOrgUtils();
		}
		return utils;
	}

	public String getValue(HashMap<String, Object> value)
			throws BusinessException {
		
		List<String >orgids = null;
		JSONObject data = (JSONObject) value.get("data");
		if(data!=null){
			orgids= (List<String >) data.get("orgids");
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct org_orgs.pk_org  ")
				// 组织主键
				.append(",org_orgs.pk_fatherorg ")
				.append(",org_orgs.code")
				.append(",org_orgs.name ")
				.append(",case when org_orgs.code = 'JT0001' then  '集团'  when org_orgs.pk_org = v1.pk_fatherorg or org_orgs.pk_fatherorg =  (select org_financeorg.pk_financeorg  from org_financeorg  where code = 'JT0001') then  '总部'  when org_orgs.pk_org = v1.pk_org then '区域总部'  when v1.pk_org = org_orgs.pk_fatherorg then '分公司' else  '~'  end orglevel ")
				.append("   from org_orgs ")
				.append(" inner join org_financeorg on org_financeorg.pk_financeorg = org_orgs.pk_org and org_financeorg.dr = 0   and org_financeorg.enablestate = 2 ")
				.append(" left join (select pk_org, org_orgs.pk_fatherorg from org_orgs where dr = 0   and enablestate = '2' and def3 =  (select pk_defdoc from bd_defdoc where name = '是')) v1 ")
				.append(" on org_orgs.pk_fatherorg = v1.pk_org  or org_orgs.pk_org = v1.pk_org  or v1.pk_fatherorg = org_orgs.pk_org ")
				.append("  where org_orgs.dr = 0 and org_orgs.enablestate = '2'  and v1.pk_org is not null ");
      if(orgids!=null&&orgids.size()>0){
    	  sql.append(" and "+SQLUtil.buildSqlForIn("org_orgs.pk_org", orgids.toArray(new String[0])));
      }	
		List<HashMap<String, String>> orgValue = (List<HashMap<String, String>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		if (orgValue != null && orgValue.size() > 0) {
			return JSON.toJSONString(orgValue);
		}

		return null;
	}
}
