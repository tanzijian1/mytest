package nc.bs.tg.outside.ebs.utils.marginworkorder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.SaleBillCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.vo.tgfn.marginworkorder.MarginBVO;
import nc.vo.tgfn.marginworkorder.MarginHVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/*
 * gwj
 * 保证金工单接口
 * 
 */
public class MarginwWorkorderUtils extends SaleBillUtils{

	static MarginwWorkorderUtils utils;

	public static MarginwWorkorderUtils getUtils() {
		if (utils == null) {
			utils = new MarginwWorkorderUtils();
		}
		return utils;
	}
	
	public String onSyncBill(HashMap<String, Object> value, String dectype,String srctype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		JSONArray jsonArray = (JSONArray) value.get("data");
		String result = "";
		for(int i=0;i<jsonArray.size();i++) {
			String json = jsonArray.getJSONObject(i).toJSONString();
			
			Map<String, Object> val =  (Map<String, Object>) JSON.parse(json);
			JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i).get("headInfo");
//			JSONObject jsonObject = (JSONObject) value.get("headInfo");// 获取表头信息
			String saleid = jsonObject.getString("def1");// 销售系统业务单据ID
//			String saleno = jsonObject.getString("def2");// 销售系统业务单据单据号
			String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + saleid;
			String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + saleid;
			// TODO Saleid 按实际存入信息位置进行变更
			AggMarginHVO aggVO = (AggMarginHVO) getBillVO(
					AggMarginHVO.class, "isnull(dr,0)=0 and def1 = '" + saleid
					+ "'");
			if (aggVO != null) {
				throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getBillno() + "】,请勿重复上传!");
			}
			BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
			try {
				AggMarginHVO billvo = onTranBill(val);
				HashMap eParam = new HashMap();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
						PfUtilBaseTools.PARAM_NOTE_CHECKED);
				Object obj = getPfBusiAction().processAction("SAVEBASE", "FN22", null, billvo,
						null, eParam);
				AggMarginHVO[] billvos = (AggMarginHVO[]) obj;
				HashMap<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("billid", billvos[0].getPrimaryKey());
				dataMap.put("billno", (String) billvos[0].getParentVO()
						.getAttributeValue("billno"));
				result += "第"+(i+1)+"行数据："+JSON.toJSONString(dataMap);
			} catch (Exception e) {
				throw new BusinessException("【" + billkey + "】," + e.getMessage(),
						e);
			} finally {
				BPMBillUtils.removeBillQueue(billqueue);
			}
			
        }
	
		return result;
	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param hMap
	 * @return
	 */
	private AggMarginHVO onTranBill(Map<String, Object> value)
			throws BusinessException {

			AggMarginHVO aggvo = new AggMarginHVO();
			
			JSONObject headVO = (JSONObject) value.get("headInfo");//获取表头信息
			if(headVO.isEmpty()){
				throw new BusinessException("数据异常，表头数据不能为空！");
			}
			JSONArray bodysarr = (JSONArray) value.get("itemInfo");//获取表体信息
			if(bodysarr.isEmpty()){
				throw new BusinessException("数据异常，表体数据不能为空！");
			}
			validHeadData(headVO);
			validBodyData(bodysarr);
			
			MarginHVO parent = new MarginHVO();
			//表头
			aggvo.setParentVO(parent);
			parent.setBilldate(new UFDate());
			parent.setApprovestatus(-1);
			parent.setPk_group("000112100000000005FD");
			parent.setPk_org(getPk_orgByCode(coverStr(headVO.get("pk_org"))));
			parent.setBilltype("FN22");
			parent.setDef25(headVO.getString("def25"));//经办部门
			parent.setDef26(headVO.getString("def26"));//经办人
//			parent.setPk_deptid_v(getOrg_dept_vByCode(coverStr(headVO.getString("pk_deptid_v"))));
//			parent.setPk_psndoc(getPsndocPkByCode(coverStr(headVO.getString("pk_psndoc"))));
//			parent.setPk_currtype(getcurrtypePkByCode(coverStr(headVO.getString("pk_currtype"))));
			String def22=getbd_cust_supplier(coverStr(headVO.getString("def22")));
			if(def22==null||def22.length()<1)throw new BusinessException("def22 (供应商)为空");
			parent.setDef22(def22);//供应商
//			parent.setPk_balatype(getbalatypePkByCode(coverStr(headVO.get("pk_balatype"))));
//			parent.setPayaccount(coverStr(headVO.getString("payaccount")));
			parent.setProject(getprojectByCode(coverStr(headVO.getString("project"))));
			parent.setStatus(VOStatus.NEW);
			parent.setBillstatus(-1);
			parent.setCreator(InvocationInfoProxy.getInstance().getUserId());
			parent.setBillmaker(InvocationInfoProxy.getInstance().getUserId());
			parent.setDef1(coverStr(headVO.getString("def1")));//EBS主键
//			parent.setDef2(coverStr(headVO.getString("def2")));//EBS单据号
			parent.setDef5(coverStr(headVO.getString("def5")));//合同编码
			parent.setDef6(coverStr(headVO.getString("def6")));//合同名称
//			parent.setDef4(coverStr(headVO.getString("def7")));//项目明细
			String cust=getbd_cust_supplier(headVO.getString("def23"));//内部客商
			if(cust==null) throw new BusinessException("def23内部客商为空");
			parent.setDef23(cust);
			parent.setDef27(getbd_cust_supplier(coverStr(headVO.getString("def27"))));
			MarginBVO[] itemVOs = new MarginBVO[bodysarr.size()];
			aggvo.setChildren(MarginBVO.class, itemVOs);
			for(int j=0;j<bodysarr.size();j++){
				itemVOs[j] = new MarginBVO();
				JSONObject body = bodysarr.getJSONObject(j);   
				itemVOs[j].setScomment(coverStr(body.getString("Scomment")));
				itemVOs[j].setDef1(coverStr(body.getString("def1")));
//				itemVOs[j].setDef2(coverStr(body.getString("def2")));
//				itemVOs[j].setDef3(coverStr(body.getString("def3")));
//				itemVOs[j].setDef4(coverStr(body.getString("def4")));
				itemVOs[j].setDef5(coverStr(body.getString("def5")));
				itemVOs[j].setDef6(coverStr(body.getString("def6")));
				itemVOs[j].setDef7(coverStr(body.getString("def7")));
				itemVOs[j].setDef8(coverStr(body.getString("def8")));
				itemVOs[j].setDef9(coverStr(body.getString("def9")));
				itemVOs[j].setDef10(coverStr(body.getString("def10")));
				itemVOs[j].setDef11(coverStr(body.getString("def11")));
				itemVOs[j].setDef12(coverStr(body.getString("def12")));
				itemVOs[j].setDef13(coverStr(body.getString("def13")));
				itemVOs[j].setDef14(coverStr(body.getString("def14")));
				itemVOs[j].setDef15(coverStr(body.getString("def15")));
				itemVOs[j].setDef16(coverStr(body.getString("def16")));
				itemVOs[j].setDef17(coverStr(body.getString("def17")));
				itemVOs[j].setDef18(coverStr(body.getString("def18")));
				itemVOs[j].setDef19(coverStr(body.getString("def19")));
				itemVOs[j].setDef20(coverStr(body.getString("def20")));
			}
			return aggvo;
		}
		
	
	
	/**
	 * 表头数据有效性校验
	 * @param json
	 * @throws BusinessException 
	 */
	private void validHeadData(JSONObject json) throws BusinessException{
		StringBuffer msg = new StringBuffer();
		String pk_org = getRefAttributePk("pk_org" , json.getString("pk_org"));
		String pk_deptid_v = json.getString("def25");//经办部门
		String pk_psndoc = json.getString("def26");//经办人
		String pk_currtype = getRefAttributePk("pk_currtype" ,json.getString("pk_currtype"));
		String supplier = json.getString("def22");//供应商
		String pk_balatype = getRefAttributePk("pk_balatype" ,json.getString("pk_balatype"));
		String payaccount = json.getString("payaccount");
		String project = json.getString("project");
		String def1 = json.getString("def1");
		String def2 = json.getString("def2");
		String def5 = json.getString("def5");
		String def6 = json.getString("def6");
		String def7 = json.getString("def7");
		
		if (pk_org == null || "".equals(pk_org)) 
			msg.append("数据异常，财务组织为空或不存在于NC系统中！");
		if(pk_deptid_v == null || "".equals(pk_deptid_v))
			msg.append("经办部门不能为空！");
		if(pk_psndoc == null || "".equals(pk_psndoc))
			msg.append("经办人不能为空！");
		if(supplier == null || "".equals(supplier))
			msg.append("供应商不能为空！");
//		if(pk_balatype == null || "".equals(pk_balatype))
//			msg.append("数据异常，结算方式为空或不存在于NC系统中！");
//		if(payaccount == null || "".equals(payaccount))
//			msg.append("付款银行账号不能为空！");
//		if(project == null || "".equals(project))
//			msg.append("项目不能为空！");
		if(def1 == null || "".equals(def1))
			msg.append("EBS主键不能为空！");
//		if(def2 == null || "".equals(def2))
//			msg.append("EBS单据号不能为空！");
		if(def5 == null || "".equals(def5))
			msg.append("合同编码不能为空！");
		if(def6 == null || "".equals(def6))
			msg.append("合同名称不能为空！");
//		if(def7 == null || "".equals(def7))
//			msg.append("项目明细不能为空！");
		if(msg.length() > 0)
			throw new BusinessException(msg.toString());
	}
	
	/**
	 * 表体数据有效性校验
	 * @param json
	 * @throws BusinessException 
	 */
	private void validBodyData(JSONArray jsonArray) throws BusinessException{
		StringBuffer msg = new StringBuffer();
		for(int i = 0; i<jsonArray.size(); i++){
			JSONObject json = jsonArray.getJSONObject(i);
			String scomment = json.getString("scomment");
//			String def1 = json.getString("def1");
			String def2 = json.getString("def2");
			String def3 = json.getString("def3");
			String def4 = json.getString("def4");
			String def5 = json.getString("def5");
			String def6 = json.getString("def6");
			
//			if (scomment == null || "".equals(scomment)) {
//				msg.append("第【" + (i + 1) + "】行,摘要为空或不存在NC系统中!");
//			}
//			if (def1 == null || "".equals(def1)) {
//				msg.append("第【" + (i + 1) + "】行,计税方式不能为空!");
//			}
//			if (def2 == null || "".equals(def2)) {
//				msg.append("第【" + (i + 1) + "】行,票据金额不能为空!");
//			}
//			if (def3 == null || "".equals(def3)) {
//				msg.append("第【" + (i + 1) + "】行,税率不能为空!");
//			}
//			if (def4 == null || "".equals(def4)) {
//				msg.append("第【" + (i + 1) + "】行,税额不能为空!");
//			}
			if (def5 == null || "".equals(def5)) {
				msg.append("第【" + (i + 1) + "】行,保证金不能为空!");
			}
			if (def6 == null || "".equals(def6)) {
				msg.append("第【" + (i + 1) + "】行,前期股份公司支付额不能为空!");
			}
			if(msg.length() > 0)
				throw new BusinessException(msg.toString());
		}
	}
	/**
	 * 编码得到内部客商主键
	 * @param code
	 * @return
	 * @throws DAOException 
	 */
	public String getbd_cust_supplier(String code) throws DAOException{
		String str=(String)getBaseDAO().executeQuery("select   pk_cust_sup  from bd_cust_supplier where code='"+code+"'", new ColumnProcessor());
		return str;
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
	 * 根据【经办部门】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getOrg_dept_vByCode(String code){
		String sql = "select pk_vid from org_dept_v where nvl(dr,0)=0 and code='"
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
	 * 根据【项目编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getprojectByCode(String code) {
		String sql = "select pk_project from bd_project where nvl(dr,0)=0 and (project_code ='"
				+ code + "' or def6='"+code+"')";
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

}
