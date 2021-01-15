package nc.bs.tg.outside.inv.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class QryInvInfoUtils extends InvBillUtils {
	static QryInvInfoUtils utils;

	public static QryInvInfoUtils getUtils() {
		if (utils == null) {
			utils = new QryInvInfoUtils();
		}
		return utils;
	}

	public String getValue(HashMap<String, Object> value)
			throws BusinessException {

		List<String> imgcode = null;
		List<String> invcode = null;
		List<String> invno = null;
		String invdate = null;
		String voucherdate = null;
		JSONObject data = (JSONObject) value.get("data");
		if (data != null) {
			imgcode = (List<String>) data.get("imgcode");
			invcode = (List<String>) data.get("invcode");
			invno = (List<String>) data.get("invno");
			invdate = (String) data.get("invdate");
			voucherdate = (String) data.get("voucherdate");

		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				" select  distinct hzvat_invoice_h.def8 imgcode, hzvat_invoice_h.fpdm invcode, hzvat_invoice_h.fph invno, hzvat_invoice_h.kprq invdate,er_bxzb.djlxbm er_type,v.num invvouchernum,v.prepareddate  voucherdate ")
				.append(",case when ap_payablebill.pk_payablebill  is not null then  decode(nvl(ap_payablebill.def50,'N'),'~','N',nvl(ap_payablebill.def50,'N'))  else decode(nvl(er_bxzb.zyx48 ,'N'),'~','N',nvl(er_bxzb.zyx48,'N'))   end ticketholder ")
				.append(",case when ap_payablebill.pk_payablebill  is not null then  ap_payablebill.def5  "
				+ "  else er_bxzb.zyx2 end contcode ")
				.append(",case when ap_payablebill.pk_payablebill  is not null then  ap_payablebill.billno  else er_bxzb.djbh   end billno ")
				.append(",(case when ap_payablebill.pk_payablebill  is not null then  nvl(ap_payablebill.pk_tradetypeid,'~')   else  nvl(er_bxzb.pk_tradetypeid,'~')   end ) tradetype ,")
				.append(" org_orgs.name orgname , ")
				.append(" case  when ap_payablebill.pk_payablebill is not null then ap_payablebill.def47 else   (select sm_user.user_name from sm_user  where sm_user.cuserid = er_bxzb.operator) end billmaker, ")
				.append("  er_bxzb.pk_jkbx er_id")
				.append(" from hzvat_invoice_h  ")
				.append(" left join er_bxzb on er_bxzb.dr = 0 and er_bxzb.ZYX16 = hzvat_invoice_h.def8 ")
				.append(" left join fip_relation on fip_relation.busimessage1 = hzvat_invoice_h.def8 and fip_relation.dr = 0 ")
				.append(" left join gl_voucher v  on v.pk_voucher =fip_relation.des_relationid and v.dr = 0  ")
				.append(" left join ap_payablebill on ap_payablebill.dr = 0 and ap_payablebill.def3 =  hzvat_invoice_h.def8  ")
				.append(" left join org_orgs on (er_bxzb.pk_org=org_orgs.pk_org or ap_payablebill.pk_org=org_orgs.pk_org) and nvl(org_orgs.dr,0)=0 ")
				.append(" where hzvat_invoice_h.dr = 0 and hzvat_invoice_h.def8<>'~'  ");
		if (imgcode != null && imgcode.size() > 0) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(" hzvat_invoice_h.def8 ",
							imgcode.toArray(new String[0])));
		}

		if (invcode != null && invcode.size() > 0) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(" hzvat_invoice_h.fpdm ",
							invcode.toArray(new String[0])));
		}

		if (invno != null && invno.size() > 0) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("  hzvat_invoice_h.fph ",
							invno.toArray(new String[0])));
		}

		if (invdate != null && !"".equals(invdate)) {
			String[] splitStr = invdate.split(",");
			sql.append(" and ( hzvat_invoice_h.kprq >='"
					+ new UFDate(splitStr[0]).asBegin().toString()
					+ "' and hzvat_invoice_h.kprq<= '"
					+ new UFDate(splitStr[splitStr.length - 1]).asEnd()
							.toString() + "' )");
		}

		if (voucherdate != null && !"".equals(voucherdate)) {
			String[] splitStr = voucherdate.split(",");
			sql.append(" and ( v.creationtime >='"
					+ new UFDate(splitStr[0]).asBegin().toString()
					+ "' and v.creationtime<= '"
					+ new UFDate(splitStr[splitStr.length - 1]).asEnd()
							.toString() + "' )");
		}

		List<HashMap<String, Object>> orgValue = (List<HashMap<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String,Object[]> listmap=getVoucher();
		Map<String,String>  typemap=getBilltype();
		List<String> list=Arrays.asList(new String[]{"264X-Cxx-001","264X-Cxx-004","264X-Cxx-009"});
		if (orgValue != null && orgValue.size() > 0) {
		for(HashMap<String, Object> map:orgValue){
			
			if(map.get("er_type")!=null){
				//报销单凭证与非合同类处理
				Object[] objs=listmap.get(map.get("er_id"));
				if(objs!=null){
					map.put("invvouchernum", objs[0]);
					map.put("voucherdate", (String)objs[1]);
				}
				if(!(list.contains(map.get("er_type")))){
					map.put("contcode", "非合同类");
				}
			}
			map.put("tradetype", typemap.get(map.get("tradetype")));
				map.remove("er_id");
				map.remove("er_type");
			}
			
		
			return JSON.toJSONString(orgValue);
		}

		return null;
	}
	/**
	 * 交易类型
	 * @return
	 * @throws DAOException
	 */
	public Map<String,String> getBilltype() throws DAOException{
		String sql ="select bd_billtype.billtypename,pk_billtypeid from bd_billtype ";
		List<Map<String,String>> maplist=(List<Map<String,String>>)getBaseDAO().executeQuery(sql, new MapListProcessor());
		Map<String,String> mapobj=new HashMap<>();
		for(Map<String,String> map:maplist){
				mapobj.put(map.get("pk_billtypeid"), map.get("billtypename"));
		}
		return mapobj;
	}
	/**
	 * 得到凭证
	 * @return
	 * @throws DAOException
	 */
	public Map<String,Object[]> getVoucher() throws DAOException{
		String sql= " select  v.num invvouchernum,v.prepareddate voucherdate,src_relationid srcid from gl_voucher v inner join "
				+"  ( select fip_relation.des_relationid,substr(fip_relation.src_relationid,0,20) src_relationid from  fip_relation where fip_relation.dr=0 and src_system='erm' and substr(fip_relation.src_relationid,0,20) in  ( "
					+"	  select er_bxzb.pk_jkbx from hzvat_invoice_h inner join er_bxzb on  er_bxzb.dr = 0 and er_bxzb.ZYX16 = hzvat_invoice_h.def8 "
					+	"  )) on v.pk_voucher=des_relationid where nvl(v.dr,0)=0";
		List<Map<String,Object>> maplist=(List<Map<String,Object>>)getBaseDAO().executeQuery(sql, new MapListProcessor());
	    Map<String,Object[]> mapobj=new HashMap<>();
	    for(Map<String,Object> map:maplist){
	    	if(map.get("srcid")!=null){
	    	if(mapobj.get((String)map.get("srcid"))==null){
	    		mapobj.put((String)(map.get("srcid")), new Object[2]);
	    	}
	    	mapobj.get((String)map.get("srcid"))[0]=map.get("invvouchernum");
	    	mapobj.get((String)map.get("srcid"))[1]=map.get("voucherdate");
	    	}
	    }
	    
	   return mapobj; 
	}
}
