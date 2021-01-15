package nc.bs.tg.outside.sale.utils.distribution;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.vo.tgfn.distribution.Carrybus;
import nc.vo.tgfn.distribution.Distribution;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 分摊工单接口
 * @author gwj
 *
 */
public class DisrtibutionUtils extends SaleBillUtils {
	static DisrtibutionUtils utils;

	public static DisrtibutionUtils getUtils() {
		if (utils == null) {
			utils = new DisrtibutionUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		JSONObject jsonObject = (JSONObject) value.get("headInfo");// 获取表头信息
		String saleid = jsonObject.getString("def1");// 销售系统业务单据ID
		String saleno = jsonObject.getString("def2");// 销售系统业务单据单据号
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		
		// TODO Saleid 按实际存入信息位置进行变更
		AggDistribution aggVO = (AggDistribution) getBillVO(
				AggDistribution.class, "isnull(dr,0)=0 and def1 = '" + saleid
						+ "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
		    dataMap.put("billno", (String) aggVO.getParentVO()
				.getAttributeValue("billno"));
		    return JSON.toJSONString(dataMap);
		}
		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			AggDistribution billvo = onTranBill(value);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN13", null, billvo,
					null, eParam);
			AggDistribution[] billvos = (AggDistribution[]) obj;
			obj = getPfBusiAction().processAction("SAVE", "FN13", null, billvos[0],
					null, eParam);
			billvos = (AggDistribution[]) obj;
			getPfBusiAction().processAction("APPROVE", "FN13", null, billvos[0],
					null, eParam);
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}
	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param hMap
	 * @return
	 */
	private AggDistribution onTranBill(HashMap<String, Object> value)
			throws BusinessException {
		AggDistribution aggvo = new AggDistribution();
		
		JSONObject headVO = (JSONObject) value.get("headInfo");//获取表头信息
		if(headVO.isEmpty()){
			throw new BusinessException("数据异常，表头数据不能为空！");
		}
		JSONArray bodysarr = (JSONArray) value.get("itemInfo");//获取表体信息
		if(bodysarr.isEmpty()){
			throw new BusinessException("数据异常，表体数据不能为空！");
		}
		String pk_tradetypeid = headVO.getString("pk_tradetypeid");
		validHeadData(headVO);
		validBodyData(bodysarr,pk_tradetypeid);
		
		Distribution parent = new Distribution();
		//表头
		aggvo.setParentVO(parent);
		parent.setPk_group("000112100000000005FD");
		parent.setPk_org(getPk_orgByCode(coverStr(headVO.get("pk_org"))));
		parent.setBilltype("FN13");
		parent.setDbilldate(coverDate(headVO.getString("billdate")));
		
		//mod by huangxj 2020年3月24日
		parent.setTranstype((String) headVO.get("pk_tradetypeid"));
		parent.setTranstypepk(getBillTypePkByCode(
				coverStr(headVO.get("pk_tradetypeid")), "000112100000000005FD"));
		if(parent.getTranstypepk()==null||"".equals(parent.getTranstypepk())){
			throw new BusinessException("该交易类型与NC未关联");
		}
		//默认审批状态
		parent.setApprovestatus(-1);
		//默认启用状态
		//parent.setEffectstatus(2);
		
		
		parent.setPk_busitype(coverStr(headVO.getString("pk_busitype")));
		if(headVO.getString("objtype") != null)
			parent.setObjtype(new Integer(headVO.getString("objtype")));
		parent.setCustomer(coverStr(headVO.getString("customer")));
		parent.setPk_currtype(getcurrtypePkByCode(coverStr(headVO
				.getString("pk_currtype"))));
		parent.setStatus(VOStatus.NEW);
		parent.setBillstatus(-1);
		parent.setCreator(InvocationInfoProxy.getInstance().getUserId());
		parent.setBillmaker(InvocationInfoProxy.getInstance().getUserId());
		parent.setDef1(coverStr(headVO.getString("def1")));//销售系统单据ID
		parent.setDef2(coverStr(headVO.getString("def2")));//销售系统单号
		parent.setDef3(coverStr(headVO.getString("def3")));//影像编码
		parent.setDef4(coverStr(headVO.getString("def4")));//影像状态
		parent.setDef5(coverStr(headVO.getString("def5")));//开票员
		parent.setDef6(coverStr(headVO.getString("def6")));//备注
		parent.setDef7(coverStr(headVO.getString("def7")));//待查房款NC单据号
		parent.setDef8(coverStr(headVO.getString("def8")));//待查房款NC凭证号
		parent.setDef9(coverStr(headVO.getString("isreded")));//是否红冲
		//TODO
		parent.setDef10(getUserByPsondoc(coverStr(headVO.getString("def10"))));//销售系统审批人
		parent.setDef15(coverStr(headVO.getString("def15")));//附件张数
	    parent.setDef12(coverStr(headVO.getString("def12")));
	    parent.setDef13(coverStr(headVO.getString("def13")));
	    parent.setDef31(coverStr(headVO.getString("def31")));
	    parent.setDef32(coverStr(headVO.getString("def32")));
	    parent.setDef33(coverStr(headVO.getString("def33")));
	    parent.setDef34(coverStr(headVO.getString("def34")));
	    parent.setDef35(coverStr(headVO.getString("def35")));
	    parent.setDef41(coverStr(headVO.getString("def41")));
	    parent.setDef42(coverStr(headVO.getString("def42")));
	    parent.setDef43(coverStr(headVO.getString("def43")));
	    parent.setDef44(coverStr(headVO.getString("def44")));
		Carrybus[] itemVOs = new Carrybus[bodysarr.size()];
		aggvo.setChildren(Carrybus.class, itemVOs);
		for(int i=0;i<bodysarr.size();i++){
			itemVOs[i] = new Carrybus();
			JSONObject body = bodysarr.getJSONObject(i);   
			itemVOs[i].setScomment(coverStr(body.getString("scomment")));
			itemVOs[i].setDef1(getproject(coverStr(body.getString("def1"))));
			itemVOs[i].setDef2(getproject(coverStr(body.getString("def2"))));
			itemVOs[i].setDef3(getdefdocBycode(coverStr(body.getString("def3")),"zdy020"));//款项类型
			itemVOs[i].setDef4(getdefdocBycode(coverStr(body.getString("def4")),"zdy021"));//款项名称
			itemVOs[i].setDef5(getdefdocByname(coverStr(body.getString("def5")),"zdy009"));//业态
			itemVOs[i].setDef6(getdefdocByname(coverStr(body.getString("def6")),"zdy019"));//计税方式
			itemVOs[i].setDef7(coverStr(body.getString("def7")));
			itemVOs[i].setDef8(coverStr(body.getString("def8")));
			itemVOs[i].setDef9(coverStr(body.getString("def9")));
			itemVOs[i].setDef10(coverStr(body.getString("def10")));
			itemVOs[i].setDef11(getRefAttributePk("balatypecode",body.getString("pk_balatype")));
			itemVOs[i].setDef12(coverStr(body.getString("def12")));
			itemVOs[i].setDef13(coverStr(body.getString("def13")));
			itemVOs[i].setDef14(coverStr(body.getString("def14")));
			itemVOs[i].setDef15(coverStr(body.getString("def15")));
			itemVOs[i].setDef16(coverStr(body.getString("def16")));
			itemVOs[i].setDef17(coverStr(body.getString("def17")));
			itemVOs[i].setDef18(coverStr(body.getString("def18")));
			itemVOs[i].setDef19(coverStr(body.getString("def19")));
			itemVOs[i].setDef20(coverStr(body.getString("def20")));
			itemVOs[i].setLocal_money_cr(coverDouble(body
					.getString("local_money_cr")));
			itemVOs[i].setRecaccount(getAccountIDByCode(
					coverStr(body.getString("recaccount"))));
		 }
		return aggvo;
	}
	
	/**
	 * 自定义档案pk（编码）
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String getdefdocBycode(String code,String codelist) throws DAOException{
		return (String)getBaseDAO().executeQuery("select pk_defdoc from bd_defdoc where code='"+code+"' and pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='"+codelist+"')", new ColumnProcessor());
	}
	/**
	 * 自定义档案pk（名称）
	 * @param name
	 * @param codelist
	 * @return
	 * @throws DAOException
	 */
	public String getdefdocByname(String name,String codelist) throws DAOException{
		return (String)getBaseDAO().executeQuery("select pk_defdoc from bd_defdoc where name='"+name+"' and pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='"+codelist+"')", new ColumnProcessor());
	}
	/**
	 * 项目pk
	 * @return
	 * @throws DAOException 
	 */
	public String getproject(String code) throws DAOException{
		return(String)getBaseDAO().executeQuery("select pk_project from bd_project where bd_project.def2='"+code+"'", new ColumnProcessor());
		
		 
	}
	/**
	 * 表头数据有效性校验
	 * @param json
	 * @throws BusinessException 
	 */
	private void validHeadData(JSONObject json) throws BusinessException{
		StringBuffer msg = new StringBuffer();
		String pk_org = getRefAttributePk("pk_org" , json.getString("pk_org"));
		String billdate = json.getString("billdate");
		String pk_tradetypeid = json.getString("pk_tradetypeid");
		String pk_busitype = json.getString("pk_busitype");
		String objtype = json.getString("objtype");
		String customer = json.getString("customer");
		String pk_currtype = getRefAttributePk("pk_currtype" ,json.getString("pk_currtype"));
		String billmaker = getRefAttributePk("billmaker" ,json.getString("billmaker"));
		String def1 = json.getString("def1");
		String def2 = json.getString("def2");
		String def3 = json.getString("def3");
		String def4 = json.getString("def4");
		
		if (pk_org == null || "".equals(pk_org)) 
			msg.append("数据异常，财务组织为空或不存在于NC系统中！");
		if(billdate == null || "".equals(billdate))
			msg.append("单据日期不能为空！");
		if(pk_tradetypeid == null || "".equals(pk_tradetypeid))
			msg.append("收款类型不能为空！");
//		if(billmaker == null || "".equals(billmaker))
//			msg.append("制单人不能为空！");
		if(def1 == null || "".equals(def1))
			msg.append("外系统主键不能为空！");
//		if(!"FN13-Cxx-02".equals(pk_tradetypeid)){
//			if(pk_busitype == null || "".equals(pk_busitype))
//				msg.append("业务流程不能为空！");
//			if(objtype == null || "".equals(objtype))
//				msg.append("往来对象不能为空！");
//			if(customer == null || "".equals(customer))
//				msg.append("客户不能为空！");
//			if(def2 == null || "".equals(def2))
//				msg.append("外系统单号不能为空！");
//			if(def3 == null || "".equals(def3))
//				msg.append("影像编码不能为空！");
//			if(def4 == null || "".equals(def4))
//				msg.append("影像状态不能为空！");
//		}
		if(msg.length() > 0)
			throw new BusinessException(msg.toString());
	}
	
	/**
	 * 表体数据有效性校验
	 * @param json
	 * @throws BusinessException 
	 */
	private void validBodyData(JSONArray jsonArray,String pk_tradetypeid) throws BusinessException{
		StringBuffer msg = new StringBuffer();
		for(int i = 0; i<jsonArray.size(); i++){
			JSONObject json = jsonArray.getJSONObject(i);
			String scomment = json.getString("scomment");
			String def1 = json.getString("def1");
			String def2 = json.getString("def2");
			String def3 = json.getString("def3");
			String def4 = json.getString("def4");
			String def5 = json.getString("def5");
			String def6 = json.getString("def6");
			String def7 = json.getString("def7");
			String def8 = json.getString("def8");
			String local_money_cr = json.getString("local_money_cr");
			String def9 = json.getString("def9");
			String def10 = json.getString("def10");
			String def11 = json.getString("def11");
			String def12 = json.getString("def12");
			String def13 = json.getString("def13");
			String pk_balatype = getRefAttributePk("pk_balatype",json.getString("pk_balatype"));
			String recaccount = json.getString("recaccount");
			
//			if (scomment == null || "".equals(scomment)) {
//				msg.append("第【" + (i + 1) + "】行,摘要为空或不存在NC系统中!");
//			}
//			if(!"FN13-Cxx-02".equals(pk_tradetypeid)){
//				if (def1 == null || "".equals(def1)) {
//					msg.append("第【" + (i + 1) + "】行,房地产项目不能为空!");
//				}
//				if (def2 == null || "".equals(def2)) {
//					msg.append("第【" + (i + 1) + "】行,房地产项目明细不能为空!");
//				}
//				if (def3 == null || "".equals(def3)) {
//					msg.append("第【" + (i + 1) + "】行,款项类型不能为空!");
//				}
//				if (def4 == null || "".equals(def4)) {
//					msg.append("第【" + (i + 1) + "】行,款项名称不能为空!");
//				}
//				if (def5 == null || "".equals(def5)) {
//					msg.append("第【" + (i + 1) + "】行,业态不能为空!");
//				}
//				if (def6 == null || "".equals(def6)) {
//					msg.append("第【" + (i + 1) + "】行,计税方式不能为空!");
//				}
//				if (def7 == null || "".equals(def7)) {
//					msg.append("第【" + (i + 1) + "】行,税率不能为空!");
//				}
//				if (def8 == null || "".equals(def8)) {
//					msg.append("第【" + (i + 1) + "】行,税额不能为空!");
//				}
//				if (local_money_cr == null || "".equals(local_money_cr)) {
//					msg.append("第【" + (i + 1) + "】行,含税总金额不能为空!");
//				}
//				if (def9 == null || "".equals(def9)) {
//					msg.append("第【" + (i + 1) + "】行,不含税金额不能为空!");
//				}
//				if (def10 == null || "".equals(def10)) {
//					msg.append("第【" + (i + 1) + "】行,分摊含税金额不能为空!");
//				}
//				if (def11 == null || "".equals(def11)) {
//					msg.append("第【" + (i + 1) + "】行,发票号不能为空!");
//				}
//				if (def12 == null || "".equals(def12)) {
//					msg.append("第【" + (i + 1) + "】行,分摊不含税金额不能为空!");
//				}
//				if (def13 == null || "".equals(def13)) {
//					msg.append("第【" + (i + 1) + "】行,分摊税率不能为空!");
//				}
//			}
//			if (pk_balatype == null || "".equals(pk_balatype)) {
//				msg.append("第【" + (i + 1) + "】行,结算方式为空或不存在NC系统中!!");
//			}
			if (recaccount == null || "".equals(recaccount)) {
				msg.append("第【" + (i + 1) + "】行,收款银行账户不能为空!");
			}
			if(msg.length() > 0)
				throw new BusinessException(msg.toString());
		}
	}
	
	private String coverStr(Object object) {
		if (object != null && ((String) object).length() != 0)
			return object.toString();
		return null;
	}

	private UFBoolean coverBoolean(Object object) {
		if (object != null && !object.equals(""))
			return new UFBoolean(object.toString());
		return null;
	}

	private UFDate coverDate(Object object) {
		if (object != null && !object.equals(""))
			return new UFDate(object.toString());
		return null;
	}

	private UFDouble coverDouble(Object object) {
		if (object != null && !object.equals(""))
			return new UFDouble(object.toString());
		return null;
	}
	
	

	/**
	 * 根据【公司编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_orgByCode(String code) {
		String sql = "select pk_org from org_orgs where code='" + code
				+ "' and dr=0 and enablestate=2 ";
		String pk_org = null;
		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据【交易类型编码】获取主键
	 * 
	 * @param code
	 * @return
	 */

	public String getBillTypePkByCode(String code, String pk_group) {
		String sql = "select pk_billtypeid from bd_billtype where nvl(dr,0)=0 and PK_BILLTYPECODE='"
				+ code + "' and pk_group='" + pk_group + "'";
		String pk_billtypeid = null;
		try {
			pk_billtypeid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_billtypeid != null) {
				return pk_billtypeid;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据【人员编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPsndocPkByCode(String code) {
		String sql = "select pk_psndoc from bd_psndoc where nvl(dr,0)=0 and code='"
				+ code + "'";
		String pk = null;
		try {
			pk = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
			if (pk != null && !pk.equals("")) {
				return pk;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 根据【币种编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getcurrtypePkByCode(String code) {
		String sql = "select  pk_currtype  from bd_currtype where nvl(dr,0)=0 and code ='"
				+ code + "'";
		String pk = null;
		try {
			pk = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
			if (pk != null && !pk.equals("")) {
				return pk;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 根据【结算编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getbalatypePkByCode(String code) {
		String sql = "select  pk_balatype  from bd_balatype where nvl(dr,0)=0 and code ='"
				+ code + "'";
		String pk = null;
		try {
			pk = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
			if (pk != null && !pk.equals("")) {
				return pk;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 根据银行账户编码读取对应主键
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 */
	public String getAccountIDByCode(String recaccount, String pk_receiver) {
		String result = null;
		String sql = "SELECT bd_custbank.pk_bankaccsub AS pk_bankaccsub "
				+ "   FROM bd_bankaccbas, bd_bankaccsub, bd_custbank "
				+ " WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
				+ " AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub "
				+ " AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ " AND bd_custbank.pk_bankaccsub != '~' "
				+ " AND bd_bankaccsub.Accnum = '"
				+ recaccount
				+ "' "
				+ " AND exists "
				+ " (select 1 from bd_bankaccbas bas  where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas  and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y')) "
				+ " and (enablestate = 2) "
				+ " and (pk_cust = '"
				+ pk_receiver
				+ "' and pk_custbank IN (SELECT min(pk_custbank) FROM bd_custbank GROUP BY pk_bankaccsub, pk_cust)) ";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 根据银行账户编码读取对应主键
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 */
	public String getAccountIDByCode(String recaccount) {
		String result = null;
		String sql = "SELECT sub.pk_bankaccsub FROM bd_bankaccsub sub\n"
				+ "LEFT JOIN bd_bankaccbas bas ON bas.pk_bankaccbas = sub.pk_bankaccbas\n"
				+ "WHERE sub.accnum = '"
				+ recaccount
				+ "' AND NVL(sub.dr,0)=0 AND NVL(bas.dr,0)=0 AND bas.enablestate = '2';";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
