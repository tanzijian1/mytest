package nc.bs.tg.outside.sale.utils.marginworkorder;

import java.util.HashMap;

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
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.vo.tgfn.marginworkorder.MarginBVO;
import nc.vo.tgfn.marginworkorder.MarginHVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MarginwWorkorderUtils extends SaleBillUtils{

	static MarginwWorkorderUtils utils;

	public static MarginwWorkorderUtils getUtils() {
		if (utils == null) {
			utils = new MarginwWorkorderUtils();
		}
		return utils;
	}
	
	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		JSONObject jsonObject = (JSONObject) value.get("headInfo");// ��ȡ��ͷ��Ϣ
		String saleid = jsonObject.getString("def1");// ����ϵͳҵ�񵥾�ID
		String saleno = jsonObject.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		// TODO Saleid ��ʵ�ʴ�����Ϣλ�ý��б��
		AggMarginHVO aggVO = (AggMarginHVO) getBillVO(
				AggMarginHVO.class, "isnull(dr,0)=0 and def1 = '" + saleid
						+ "'");
		if (aggVO != null) {
			throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ aggVO.getParentVO().getBillno() + "��,�����ظ��ϴ�!");
		}
		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			AggMarginHVO billvo = onTranBill(value);
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
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}
	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param hMap
	 * @return
	 */
	private AggMarginHVO onTranBill(HashMap<String, Object> value)
			throws BusinessException {
		AggMarginHVO aggvo = new AggMarginHVO();
		
		JSONObject headVO = (JSONObject) value.get("headInfo");//��ȡ��ͷ��Ϣ
		if(headVO.isEmpty()){
			throw new BusinessException("�����쳣����ͷ���ݲ���Ϊ�գ�");
		}
		JSONArray bodysarr = (JSONArray) value.get("itemInfo");//��ȡ������Ϣ
		if(bodysarr.isEmpty()){
			throw new BusinessException("�����쳣���������ݲ���Ϊ�գ�");
		}
		validHeadData(headVO);
		validBodyData(bodysarr);
		
		MarginHVO parent = new MarginHVO();
		//��ͷ
		aggvo.setParentVO(parent);
		parent.setPk_group("000112100000000005FD");
		parent.setPk_org(getPk_orgByCode(coverStr(headVO.get("pk_org"))));
		parent.setBilltype("FN22");
		parent.setPk_deptid_v(coverStr(headVO.getString("pk_deptid_v")));
		parent.setPk_psndoc(coverStr(headVO.getString("pk_psndoc")));
		parent.setPk_currtype(getcurrtypePkByCode(coverStr(headVO.getString("pk_currtype"))));
		parent.setSupplier(coverStr(headVO.getString("supplier")));
		parent.setPk_balatype(getbalatypePkByCode(coverStr(headVO.get("pk_balatype"))));
		parent.setPayaccount(coverStr(headVO.getString("payaccount")));
		parent.setProject(coverStr(headVO.getString("project")));
		parent.setStatus(VOStatus.NEW);
		parent.setBillstatus(-1);
		parent.setCreator(InvocationInfoProxy.getInstance().getUserId());
		parent.setBillmaker(InvocationInfoProxy.getInstance().getUserId());
		parent.setDef1(coverStr(headVO.getString("def1")));//EBS����
		parent.setDef2(coverStr(headVO.getString("def2")));//EBS���ݺ�
		parent.setDef3(coverStr(headVO.getString("def5")));//��ͬ����
		parent.setDef4(coverStr(headVO.getString("def6")));//��ͬ����
		parent.setDef4(coverStr(headVO.getString("def7")));//��Ŀ��ϸ
		
		
		MarginBVO[] itemVOs = new MarginBVO[bodysarr.size()];
		aggvo.setChildren(MarginBVO.class, itemVOs);
		for(int i=0;i<bodysarr.size();i++){
			itemVOs[i] = new MarginBVO();
			JSONObject body = bodysarr.getJSONObject(i);   
			itemVOs[i].setScomment(coverStr(body.getString("Scomment")));
			itemVOs[i].setDef1(coverStr(body.getString("def1")));
			itemVOs[i].setDef2(coverStr(body.getString("def2")));
			itemVOs[i].setDef3(coverStr(body.getString("def3")));
			itemVOs[i].setDef4(coverStr(body.getString("def4")));
			itemVOs[i].setDef5(coverStr(body.getString("def5")));
			itemVOs[i].setDef6(coverStr(body.getString("def6")));
			itemVOs[i].setDef7(coverStr(body.getString("def7")));
			itemVOs[i].setDef8(coverStr(body.getString("def8")));
			itemVOs[i].setDef9(coverStr(body.getString("def9")));
			itemVOs[i].setDef10(coverStr(body.getString("def10")));
			itemVOs[i].setDef11(coverStr(body.getString("def11")));
			itemVOs[i].setDef12(coverStr(body.getString("def12")));
			itemVOs[i].setDef13(coverStr(body.getString("def13")));
			itemVOs[i].setDef14(coverStr(body.getString("def14")));
			itemVOs[i].setDef15(coverStr(body.getString("def15")));
			itemVOs[i].setDef16(coverStr(body.getString("def16")));
			itemVOs[i].setDef17(coverStr(body.getString("def17")));
			itemVOs[i].setDef18(coverStr(body.getString("def18")));
			itemVOs[i].setDef19(coverStr(body.getString("def19")));
			itemVOs[i].setDef20(coverStr(body.getString("def20")));
		 }
		return aggvo;
	}
	
	/**
	 * ��ͷ������Ч��У��
	 * @param json
	 * @throws BusinessException 
	 */
	private void validHeadData(JSONObject json) throws BusinessException{
		StringBuffer msg = new StringBuffer();
		String pk_org = getRefAttributePk("pk_org" , json.getString("pk_org"));
		String pk_deptid_v = json.getString("pk_deptid_v");
		String pk_psndoc = json.getString("pk_psndoc");
		String pk_currtype = getRefAttributePk("pk_currtype" ,json.getString("pk_currtype"));
		String supplier = json.getString("supplier");
		String pk_balatype = getRefAttributePk("pk_balatype" ,json.getString("pk_balatype"));
		String payaccount = json.getString("payaccount");
		String project = json.getString("project");
		String def1 = json.getString("def1");
		String def2 = json.getString("def2");
		String def5 = json.getString("def5");
		String def6 = json.getString("def6");
		String def7 = json.getString("def7");
		
		if (pk_org == null || "".equals(pk_org)) 
			msg.append("�����쳣��������֯Ϊ�ջ򲻴�����NCϵͳ�У�");
		if(pk_deptid_v == null || "".equals(pk_deptid_v))
			msg.append("���첿�Ų���Ϊ�գ�");
		if(pk_psndoc == null || "".equals(pk_psndoc))
			msg.append("�����˲���Ϊ�գ�");
		if(supplier == null || "".equals(supplier))
			msg.append("��Ӧ�̲���Ϊ�գ�");
		if(pk_balatype == null || "".equals(pk_balatype))
			msg.append("�����쳣�����㷽ʽΪ�ջ򲻴�����NCϵͳ�У�");
		if(payaccount == null || "".equals(payaccount))
			msg.append("���������˺Ų���Ϊ�գ�");
		if(project == null || "".equals(project))
			msg.append("��Ŀ����Ϊ�գ�");
		if(def1 == null || "".equals(def1))
			msg.append("EBS��������Ϊ�գ�");
		if(def2 == null || "".equals(def2))
			msg.append("EBS���ݺŲ���Ϊ�գ�");
		if(def5 == null || "".equals(def5))
			msg.append("��ͬ���벻��Ϊ�գ�");
		if(def6 == null || "".equals(def6))
			msg.append("��ͬ���Ʋ���Ϊ�գ�");
		if(def7 == null || "".equals(def7))
			msg.append("��Ŀ��ϸ����Ϊ�գ�");
		if(msg.length() > 0)
			throw new BusinessException(msg.toString());
	}
	
	/**
	 * ����������Ч��У��
	 * @param json
	 * @throws BusinessException 
	 */
	private void validBodyData(JSONArray jsonArray) throws BusinessException{
		StringBuffer msg = new StringBuffer();
		for(int i = 0; i<jsonArray.size(); i++){
			JSONObject json = jsonArray.getJSONObject(i);
			String scomment = getRefAttributePk("scomment" , json.getString("scomment"));
//			String def1 = json.getString("def1");
			String def2 = json.getString("def2");
			String def3 = json.getString("def3");
			String def4 = json.getString("def4");
			String def5 = json.getString("def5");
			String def6 = json.getString("def6");
			
			/*if (scomment == null || "".equals(scomment)) {
				msg.append("�ڡ�" + (i + 1) + "����,ժҪΪ�ջ򲻴���NCϵͳ��!");
			}*/
//			if (def1 == null || "".equals(def1)) {
//				msg.append("�ڡ�" + (i + 1) + "����,��˰��ʽ����Ϊ��!");
//			}
			if (def2 == null || "".equals(def2)) {
				msg.append("�ڡ�" + (i + 1) + "����,Ʊ�ݽ���Ϊ��!");
			}
			if (def3 == null || "".equals(def3)) {
				msg.append("�ڡ�" + (i + 1) + "����,˰�ʲ���Ϊ��!");
			}
			if (def4 == null || "".equals(def4)) {
				msg.append("�ڡ�" + (i + 1) + "����,˰���Ϊ��!");
			}
			if (def5 == null || "".equals(def5)) {
				msg.append("�ڡ�" + (i + 1) + "����,��֤����Ϊ��!");
			}
			if (def6 == null || "".equals(def6)) {
				msg.append("�ڡ�" + (i + 1) + "����,ǰ�ڹɷݹ�˾֧�����Ϊ��!");
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
	 * ���ݡ���˾���롿��ȡ����
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
	 * ���ݡ��������ͱ��롿��ȡ����
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
	 * ���ݡ���Ա���롿��ȡ����
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
	 * ���ݡ����ֱ��롿��ȡ����
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
	 * ���ݡ�������롿��ȡ����
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
	 * ���������˻������ȡ��Ӧ����
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
