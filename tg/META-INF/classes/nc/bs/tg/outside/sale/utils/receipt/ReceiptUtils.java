package nc.bs.tg.outside.sale.utils.receipt;

import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * �տ
 * @author ln
 *
 */
public class ReceiptUtils extends SaleBillUtils {
	static ReceiptUtils utils;

	public static ReceiptUtils getUtils() {
		if (utils == null) {
			utils = new ReceiptUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String dectype)
			throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());

		JSONObject jsonObject = JSONObject.parseObject(value.get("data")
				.toString());// ��ȡ��ͷ��Ϣ
		JSONObject HeadVO = JSONObject.parseObject(jsonObject.get(
				"ar_gatherbill").toString());// ��ȡ��ͷ��Ϣ

		String srcid = HeadVO.getString("def1");// ��ϵͳ����
		String srcno = HeadVO.getString("def2");// ��ϵͳ���ݺ�
		// Ŀ��ҵ�񵥾�����������
		String billqueue = SaleBillCont.getBillNameMap().get(dectype) + ":"
				+ srcid;
		// Ŀ��ҵ�񵥾���������
		String billkey = SaleBillCont.getBillNameMap().get(dectype) + ":"
				+ srcno;
		// ��������ϵͳҵ�񵥾�ID��ѯ��Ӧ�����ۺ�VO
		AggGatheringBillVO aggVO = (AggGatheringBillVO) getBillVO(
				AggGatheringBillVO.class, "isnull(dr,0)=0 and def1 = '" + srcid
						+ "'");
		HashMap<String, String> dataMap = new HashMap<String, String>();
		
		// ��Ϊ���׳�����
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String) aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}
		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		
		try {
			// ������������VO����
			AggGatheringBillVO billvo = onTranBill(jsonObject, dectype);
			HashMap eParam = new HashMap();
			AggregatedValueObject afterbillvo;
			// �������Ƿ��Ѽ��
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			WorkflownoteVO worknote=null;
			worknote=iWorkflowMachine.checkWorkFlow(IPFActionName.SAVE, (String)billvo.getParentVO().getAttributeValue("pk_tradetype"), billvo, null);
			afterbillvo = ((AggregatedValueObject[]) getPfBusiAction().processAction(
					"SAVE", "D2", worknote, billvo, null, eParam))[0];
//			worknote=iWorkflowMachine.checkWorkFlow(IPFActionName.APPROVE+((GatheringBillVO)billvo.getParentVO()).getCreator(), (String)billvo.getParentVO().getAttributeValue("pk_tradetype"), billvo, null);
//			getPfBusiAction().processAction(
//					"APPROVE", "D2", worknote, billvo, null, eParam);
			dataMap = new HashMap<String, String>();
			dataMap.put("billid", billvo.getPrimaryKey());
			dataMap.put("billno", (String) afterbillvo.getParentVO()
					.getAttributeValue("billno"));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

		return JSON.toJSONString(dataMap);
	}

	public AggGatheringBillVO onTranBill(JSONObject jsonObject, String dectype)
			throws BusinessException {
		JSONObject headVO = JSONObject.parseObject(jsonObject.get(
				"ar_gatherbill").toString());// ��ȡ��ͷ��Ϣ
		AggGatheringBillVO billVO = new AggGatheringBillVO();
		validData(headVO);
		GatheringBillVO parent = new GatheringBillVO();
		// ��ͷ
		billVO.setParentVO(parent);
		parent.setPk_group("000112100000000005FD");
		parent.setPk_org(getPk_orgByCode(coverStr(headVO.get("pk_org"))));
		parent.setPk_tradetypeid(getBillTypePkByCode(
				coverStr(headVO.get("pk_tradetypeid")), "000112100000000005FD"));
		parent.setPk_tradetype(coverStr(headVO.getString("pk_tradetypeid")));
		parent.setPk_billtype("F2");
		parent.setBilldate(coverDate(headVO.getString("billdate")));
		// parent.setPk_psndoc(getPsndocPkByCode(coverStr(headVO
		// .getString("pk_psndoc"))));
		parent.setPk_currtype(getcurrtypePkByCode(coverStr(headVO
				.getString("pk_currtype"))));
		parent.setIsreded(coverBoolean(headVO.getString("isreded")));
		parent.setDef1(coverStr(headVO.getString("def1")));// ����ϵͳ����ID
		parent.setDef2(coverStr(headVO.getString("def2")));// ����ϵͳ����
		parent.setDef3(coverStr(headVO.getString("def3")));// Ӱ�����
		parent.setDef4(coverStr(headVO.getString("def4")));// Ӱ��״̬
		parent.setDef5(coverStr(headVO.getString("def5")));// ��ƱԱ
		parent.setLocal_money(coverDouble(headVO.getString("local_money")));
		parent.setSyscode(0);
		parent.setSrc_syscode(0);
		parent.setStatus(VOStatus.NEW);
		parent.setBillstatus(-1);
		parent.setCreator(InvocationInfoProxy.getInstance().getUserId());
		parent.setBillmaker(InvocationInfoProxy.getInstance().getUserId());
		parent.setCustomer(coverStr(headVO.getString("customer")));
		parent.setDef6(coverStr(headVO.getString("def6")));// ժҪ
		parent.setDef8(coverStr(getCustomerPKByname(headVO.getString("def8"))));// ����
		parent.setDef35(coverStr(getCustpk(headVO.getString("def35"))));// �ͻ�����
		parent.setDef31(coverStr(headVO.getString("def31")));// 
		parent.setDef32(coverStr(headVO.getString("def32")));// 
		parent.setDef33(coverStr(headVO.getString("def33")));// 
		parent.setDef34(coverStr(headVO.getString("def34")));// 
		parent.setDef45(coverStr(headVO.getString("def45")));
		parent.setDef39(coverStr(headVO.getString("def39")));//ת�����տ�Ψһ��ʶ
		/* start add by bobo ���def40���տ����ڣ�����ͷ41����Ϣ����������ͷdef42����ͬ���룩����ͷdef43����������ƣ�
		�ӿ��ֶ� 2020��6��28��11��45�� TODO*/
		parent.setDef40(coverStr(headVO.getString("def40")));// �տ�����
		parent.setDef41(coverStr(headVO.getString("def41")));// ��Ϣ����
		parent.setDef42(
				getContractRefDocPkByCode(
						coverStr(headVO.getString("def42"))));// ��ͬ����--����--��������ͬ����
		parent.setDef43(
				getCustSupplierPkByName(
						coverStr(headVO.getString("def43"))));// ���������--����--���������������
		/* end add by bobo ���def40���տ����ڣ�����ͷ41����Ϣ����������ͷdef42����ͬ���룩����ͷdef43����������ƣ�
		�ӿ��ֶ� 2020��6��28��11��45��*/
		if(headVO.getString("accessorynum")!=null&&!"".equals(headVO.getString("accessorynum")))
		parent.setAccessorynum(Integer.valueOf(headVO.getString("accessorynum")));//��������
		
		//TODO
		List<String> list=Arrays.asList(new String[]{"D2","F2-Cxx-001","F2-Cxx-002","F2-Cxx-003","F2-Cxx-021","F2-Cxx-022","F2-Cxx-023","F2-Cxx-026"});
		if(list.contains(headVO.get("pk_tradetypeid")))
		parent.setDef36(getUserByPsondoc(headVO.getString("def30")));// ����ϵͳ�����
		// ����
		JSONArray bodysarr = JSONObject.parseArray(jsonObject.get(
				"ar_gatheritem").toString());

		GatheringBillItemVO[] itemVOs = new GatheringBillItemVO[bodysarr.size()];
		billVO.setChildren(GatheringBillItemVO.class, itemVOs);
		for (int i = 0; i < bodysarr.size(); i++) {
			itemVOs[i] = new GatheringBillItemVO();
			JSONObject body = JSONObject
					.parseObject(bodysarr.get(i).toString());
			itemVOs[i].setScomment(coverStr(body.getString("scomment")));
			String pk_project = getPk_projectByCode(coverStr(body
					.getString("def1")));
			itemVOs[i].setDef1(pk_project);//���ز���Ŀ
			itemVOs[i].setDef2(getPk_projectByCode(coverStr(body.getString("def2"))));//���ز���Ŀ��ϸ
			String pk_defdoc = getPk_defdocByCode(coverStr(body.getString("def3")));
			itemVOs[i].setDef3(pk_defdoc);//��������
			String pk_defdoc1 = getPk_defdocByCode(coverStr(body.getString("def4")));
			itemVOs[i].setDef4(pk_defdoc1);//��������
			itemVOs[i].setDef5(getPk_defdocByName(coverStr(body.getString("def5"))));//ҵ̬
			itemVOs[i].setDef6(getPk_defdocByName(coverStr(body.getString("def6"))));//��˰��ʽ
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
			itemVOs[i].setDef23(coverStr(body.getString("def23")));
			itemVOs[i].setDef53(coverStr(body.getString("def53")));
			itemVOs[i].setDef54(coverStr(body.getString("def54")));
			itemVOs[i].setDef56(coverStr(body.getString("def56")));
			itemVOs[i].setRate(coverDouble(body.getString("rate")));
			itemVOs[i].setLocal_money_cr(coverDouble(body
					.getString("local_money_cr")));
			itemVOs[i].setPk_currtype(getcurrtypePkByCode(coverStr(headVO
					.getString("pk_currtype"))));// 1002Z0100000000001K1
			itemVOs[i].setObjtype(0);// 0
			itemVOs[i].setCustomer("0001121000000000XFSC");
			itemVOs[i].setMoney_cr(coverDouble(body.getString("money_cr")));
			itemVOs[i].setPk_balatype(getbalatypePkByCode(coverStr(body
					.getString("pk_balatype"))));
			//���ݿ������ƴ�����֧��Ŀ
			String pk_subcode = getPk_subcode(pk_defdoc1);
			itemVOs[i].setPk_subjcode(pk_subcode);
			
			String recaccount = getAccountIDByCode(coverStr(body
					.getString("recaccount")));
			itemVOs[i].setRecaccount(recaccount);

		}
		return billVO;
	}
	/**
	 * У���Ƿ�ش�
	 * @param headVO
	 * @throws BusinessException
	 */
public void validData(JSONObject headVO) throws BusinessException{
	if(headVO.getString("def1")==null||headVO.getString("def1").length()<1){
		throw new BusinessException("����ϵͳid����Ϊ��");
	}else if("F2-Cxx-001".equals(headVO.getString("pk_tradetypeid"))){
		if(headVO.getString("def39")==null||headVO.getString("def39").length()<1){//��ת�����տ�Ψһ��ʶ��
//			throw new BusinessException("ת�����տ�Ψһ��ʶ����Ϊ��");
		}
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
	 * <p>Title: getRefDocPkByCode<��p>
	 * <p>Description: ���ݱ����ȡ��ͬ�Զ��嵵�������ݵ�����ֵ<��p>
	 * @param code ����
	 * @param refDocPk �Զ��嵵����������
	 * @author BOBO
	 * @date 2020��6��28��14��32��
	 * @return
	 */
	public String getContractRefDocPkByCode(String code){
		String sql = " select pk_defdoc from bd_defdoc  where 11=11  and (enablestate = 2) and nvl(dr,0) = 0  and code = '" + code + "' and pk_defdoclist = '10011210000000016WHI'";
		String contractRefDocPk = null;
		try {
			contractRefDocPk = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (contractRefDocPk != null) {
				return contractRefDocPk;
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
	/**
	 * ��ȡ����pk
	 * @param code
	 * @return
	 */
	public String getCustomerPK(String code){
		String result = null;
		String sql = "SELECT pk_cust_sup FROM bd_cust_supplier WHERE CODE = '"+code+"' AND custenablestate = '2' AND NVL(dr,0)=0";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ���ƻ�ȡ����pk�����ڲ����ⲿ��
	 * @param code
	 * @return
	 */
	public String getCustomerPKByname(String name){
		String result = null;
		//�ڲ�����
		String sql = "select   pk_cust_sup  from bd_cust_supplier  where "
        + " pk_custclass=(select pk_custclass from bd_custclass where code='01')  and name='"+name+"'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if(result==null||result.length()<1){
				//�ⲿ����
				String wsql = "select   pk_cust_sup  from bd_cust_supplier  where "
				        + " pk_custclass=(select pk_custclass from bd_custclass where code='02')  and name='"+name+"'";
				result = (String) getBaseDAO().executeQuery(wsql,
						new ColumnProcessor());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ��ȡ����pk
	 * @param code
	 * @return
	 */
	public String getCustpk(String name){
		String result = null;
		String sql = "SELECT pk_customer FROM bd_customer WHERE name = '"+name+"' AND enablestate  = '2' AND NVL(dr,0)=0";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * <p>Title: getCustSupplierPkByName<��p>
	 * <p>Description: �������ƻ�ȡ��������<��p>
	 * @param custSupplierName ��������
	 * @author BOBO
	 * @date 2020��6��28��14��58��
	 * @return
	 */
	public String getCustSupplierPkByName(String custSupplierName){
		String custSupplierPk = null;
		String sql = "SELECT\n" +
				"	pk_cust_sup \n" +
				"FROM\n" +
				"	bd_cust_supplier \n" +
				"WHERE\n" +
				"	1 = 1 \n" +
				"	AND custenablestate <> 3 \n" +
				"	AND supenablestate <> 3\n" +
				"	AND name = '" + custSupplierName + "'";
		try {
			custSupplierPk = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custSupplierPk;
	}
}
