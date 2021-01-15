package nc.bs.tg.outside.sale.utils.paybill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.bs.tg.outside.sale.utils.salessystem.BasicInformationUtil;
import nc.itf.tg.outside.SaleBillCont;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ��Ӧ�̸��
 * 
 * @author kyy
 * 
 */
public class PayBillUtils extends SaleBillUtils {
	static PayBillUtils utils;

	public static PayBillUtils getUtils() {
		if (utils == null) {
			utils = new PayBillUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);
		
		// ����json����,��ȡ��ͷ����
		JSONObject headJson = (JSONObject) value.get("headInfo");
		HashMap<String, String> dataMap = new HashMap<String, String>();
		
		// Saleid ��ʵ�ʴ�����Ϣλ�ý��б��
		String saleid = headJson.getString("def1");// ����ϵͳҵ�񵥾�ID
		String saleno = headJson.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�

		// ����ϵͳҵ�񵥾�ID������ϵͳҵ�񵥾ݵ��ݺ������п��ƺ���־���
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		// ������������˻�Ψһ�� add by huangxj
		Collection<SupplierVO> docVO = SaleBillUtils
				.getUtils()
				.getBaseDAO()
				.retrieveByClause(
						SupplierVO.class,
						"isnull(dr,0)=0 and def2 = '"
								+ headJson.getString("def43")
								+ "' and name = '"
								+ headJson.getString("supplier") + "'");

		// �������ϵͳҵ�񵥾�IDΨһ��
		AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
				"isnull(dr,0)=0 and def1 = '" + saleid + "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String)aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}

		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {

			// ת������ΪVO
			// *********************************
			PayBillConvertor convertor = new PayBillConvertor();
			// Ĭ�ϼ���PK
			convertor.setDefaultGroup("000112100000000005FD");
			// ���ñ���key������ӳ��
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("payitem", "��Ӧ�̸����");
			convertor.setBVOKeyName(bVOKeyName);

			// ���ñ�ͷkey������ӳ��
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("paybill", "��Ӧ�̸��");
			convertor.setHVOKeyName(hVOKeyName);

			// ��ͷ��ֵУ���ֶ�
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hPayBillHVOKeyName = new HashMap<String, String>();

			hPayBillHVOKeyName.put("def1", "����ϵͳ����ID");
			// hPayBillHVOKeyName.put("def2", "����ϵͳ����");
			// hPayBillHVOKeyName.put("def3", "Ӱ�����");
			// hPayBillHVOKeyName.put("def4", "Ӱ��״̬");
			hPayBillHVOKeyName.put("def41", "��ƱԱ");
			// hPayBillHVOKeyName.put("def6", "��ע");
			if (!"F3-Cxx-024".equals(headJson.getString("pk_tradetypeid"))) {
				hPayBillHVOKeyName.put("def43", "�������ƣ���Ӧ�̣��ͻ���");
				hPayBillHVOKeyName.put("def44", "�տ������˻�������");
			}
			hPayBillHVOKeyName.put("billdate", "��������");
			hPayBillHVOKeyName.put("pk_org", "������֯");
			// hPayBillHVOKeyName.put("objtype", "��������");
			hPayBillHVOKeyName.put("supplier", "����id����Ӧ�̣��ͻ���");
			hPayBillHVOKeyName.put("pk_currtype", "����");
			hValidatedKeyName.put("paybill", hPayBillHVOKeyName);
			// �����˻�
			// String pk_account = null;
			// if ("F3-Cxx-001".equals(headJson.getString("pk_tradetypeid"))) {
			String[] result = new String[2];

			if (docVO.size() <= 0) {
				// result = service.saveSupplierAccount_RequiresNew(headJson);
				result = BasicInformationUtil.getUtils().onBasicBill(headJson);
			}/*
			 * else { pk_account = convertor.getRefAttributePk(
			 * "payitem-recaccount", headJson.getString("def43")); }
			 */
			// }
			// �����ֵУ���ֶ�
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> bPayBillBVOKeyName = new HashMap<String, String>();
			
			//bPayBillBVOKeyName.put("scomment", "ժҪ");
			bPayBillBVOKeyName.put("def19", "������Ŀ");
			// bPayBillBVOKeyName.put("def20", "���򷿼�");
			// bPayBillBVOKeyName.put("def25", "ҵ̬");
			// bPayBillBVOKeyName.put("def26", "��˰��ʽ");
			// bPayBillBVOKeyName.put("recaccount", "�ͻ������˻�");
			// ����������Ϊת������Ҫ������������Ҫ��
			// if (!"F3-Cxx-001".equals(headJson.getString("pk_tradetypeid")) )
			// {
			// bPayBillBVOKeyName.put("def23", "��������");
			// bPayBillBVOKeyName.put("def24", "��������");
			// bPayBillBVOKeyName.put("def27", "˰��");
			// bPayBillBVOKeyName.put("def28", "˰��");
			// bPayBillBVOKeyName.put("def29", "����˰���");
			// bPayBillBVOKeyName.put("def30", "�ͻ������˺�");
			// bPayBillBVOKeyName.put("def31", "��Ӧ������");
			// }
			bPayBillBVOKeyName.put("rate", "����");
			// bPayBillBVOKeyName.put("local_money_de",
			// "�����˿���(NC-��֯���ҽ��(�跽))");
			bPayBillBVOKeyName.put("money_de", "�����˿���(NC-�˿�ԭ�ҽ��)");
			bPayBillBVOKeyName.put("pk_balatype", "���㷽ʽ");
			// bPayBillBVOKeyName.put("payaccount", "���������˻�");
			// bPayBillBVOKeyName.put("supplier", "��Ӧ������");
			bValidatedKeyName.put("payitem", bPayBillBVOKeyName);

			// ���������ֶ�
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("paybill-pk_org");
			refKeys.add("paybill-supplier");
			refKeys.add("paybill-pk_currtype");
			refKeys.add("paybill-pk_tradetypeid");
			refKeys.add("paybill-def59");
			refKeys.add("payitem-scomment");
			refKeys.add("payitem-pk_balatype");
			refKeys.add("payitem-payaccount");
			refKeys.add("payitem-def19");
			refKeys.add("payitem-def20");
			refKeys.add("payitem-def23");
			refKeys.add("payitem-def24");
			refKeys.add("payitem-def25");
			refKeys.add("payitem-def26");
			refKeys.add("payitem-def27");

			convertor.sethValidatedKeyName(hValidatedKeyName);
			convertor.setbValidatedKeyName(bValidatedKeyName);
			convertor.setRefKeys(refKeys);
           
			// *********************************
			AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
					AggPayBillVO.class, aggVO);
			//У�鵥��Ϊת���������Ƿ���Ψһ��ʶ
			
			if("F3-Cxx-001".equals(billvo.getParentVO().getAttributeValue("F3-Cxx-001"))){
				String def80=(String)billvo.getParentVO().getAttributeValue("def80");
				for(PayBillItemVO bvo:billvo.getBodyVOs()){
					if(bvo.getScomment()==null||bvo.getScomment().length()<1){
						throw new BusinessException("ת�������������ע���벻Ϊ��");
					}
				}
//				if(def80==null||def80.length()<1)throw new BusinessException("ת�����ո���Ψһ��ʶ����Ϊ��");
			}
			// ����������
			if (!"".equals(billvo.getParentVO().getAttributeValue("def59"))
					&& billvo.getParentVO().getAttributeValue("def59") != null) {
				billvo.getParentVO().setAttributeValue(
						"def59",
						getUserByPsondoc((String) billvo.getParentVO()
								.getAttributeValue("def59")));
			}
			// ������֯
			billvo.getParentVO().setAttributeValue(
					"pk_org",
					convertor.getRefAttributePk(
							"paybill-pk_org",
							(String) billvo.getParentVO().getAttributeValue(
									"pk_org")));

			String pk_vid = getvidByorg((String) billvo.getParentVO()
					.getAttributeValue("pk_org"));
			String pk_tradetype = (String) billvo.getParentVO()
					.getAttributeValue("pk_tradetypeid");

			// ����NCĬ����Ϣ
			billvo.getParentVO().setAttributeValue("billmaker",
					getUserByCode("SALE"));
			billvo.getParentVO().setAttributeValue("objtype", 1);
			// billvo.getParentVO().setAttributeValue("pk_tradetypeid",
			// getRefAttributePk("transtype", "F3"));
			// billvo.getParentVO().setAttributeValue("pk_tradetype", "F3");
			billvo.getParentVO().setAttributeValue("syscode", "1");
			billvo.getParentVO().setAttributeValue("src_syscode", "1");
			billvo.getParentVO().setAttributeValue("pk_billtype", "F3");
			billvo.getParentVO().setAttributeValue("pk_busitype",
					getRefAttributePk("pk_busitype", "AP02"));
			billvo.getParentVO().setAttributeValue("billstatus", -1);
			billvo.getParentVO().setAttributeValue("approvestatus", -1);
			billvo.getParentVO().setAttributeValue("creationtime",
					billvo.getParentVO().getAttributeValue("billdate"));
			billvo.getParentVO().setAttributeValue("creator",
					getUserByCode("SALE"));
			billvo.getParentVO().setAttributeValue("pk_org_v", pk_vid);
            billvo.getParentVO().setAttributeValue("def83", headJson.getString("def83"));
            billvo.getHeadVO().setDef83(headJson.getString("def83"));
			billvo.getParentVO().setAttributeValue("pk_fiorg",
					(String) billvo.getParentVO().getAttributeValue("pk_org"));
			billvo.getParentVO().setAttributeValue("pk_fiorg_v", pk_vid);
			billvo.getParentVO().setAttributeValue("sett_org",
					(String) billvo.getParentVO().getAttributeValue("pk_org"));
			billvo.getParentVO().setAttributeValue("sett_org_v", pk_vid);

			billvo.getParentVO().setAttributeValue("pk_group",
					"000112100000000005FD");
			billvo.getParentVO()
					.setAttributeValue("pk_tradetype", pk_tradetype);
			// ***********��ͷ������ֵ

			// ��Ӧ�̣��ͻ������̣�
			if (docVO.size() <= 0) {
				billvo.getParentVO().setAttributeValue("supplier", result[0]);
			} else {
				//TODO--20201116-Cת��������ȡ�ڲ�����
				if("F3-Cxx-001".equals(billvo.getParentVO().getAttributeValue("F3-Cxx-001"))){
					billvo.getParentVO().setAttributeValue(
							"supplier",
							getinner_supplier(
									(String) billvo.getParentVO()
											.getAttributeValue("supplier"),
									(String) billvo.getParentVO()
											.getAttributeValue("pk_org"),
									(String) billvo.getParentVO()
											.getAttributeValue("pk_org")));
				}else{
					billvo.getParentVO().setAttributeValue(
							"supplier",
							convertor.getRefAttributePk("paybill-supplier",
									(String) billvo.getParentVO()
											.getAttributeValue("supplier"),
									(String) billvo.getParentVO()
											.getAttributeValue("pk_org"),
									(String) billvo.getParentVO()
											.getAttributeValue("pk_org")));
				}
			}
			// ��������
			billvo.getParentVO().setAttributeValue(
					"pk_tradetypeid",
					convertor.getRefAttributePk(
							"paybill-pk_tradetypeid",
							(String) billvo.getParentVO().getAttributeValue(
									"pk_tradetypeid")));
			// ����
			billvo.getParentVO().setAttributeValue(
					"pk_currtype",
					convertor.getRefAttributePk(
							"paybill-pk_currtype",
							(String) billvo.getParentVO().getAttributeValue(
									"pk_currtype")));

			// ***********��ͷ������ֵ
            
			//TODO �Ƿ����ҵ��ģʽ
			String def11 = (String) getBaseDAO().executeQuery(
					"select def11  from org_orgs where pk_org='" + 
							billvo.getParentVO()
							.getAttributeValue("pk_org")+ "'", new ColumnProcessor());

			
				
			// ***********���������ֵ
			ISuperVO[] payBillItemVOs = billvo.getChildren(PayBillItemVO.class);
			for (ISuperVO tmppayBillItemVO : payBillItemVOs) {
				PayBillItemVO payBillItemVO = (PayBillItemVO) tmppayBillItemVO;
				payBillItemVO.setPk_currtype((String) billvo.getParentVO()
						.getAttributeValue("pk_currtype"));

				payBillItemVO.setPk_billtype("F3");
				payBillItemVO.setPk_org((String) billvo.getParentVO()
						.getAttributeValue("pk_org"));
				payBillItemVO.setPk_org_v(pk_vid);
				payBillItemVO.setPk_fiorg((String) billvo.getParentVO()
						.getAttributeValue("pk_org"));
				payBillItemVO.setPk_fiorg_v(pk_vid);
				payBillItemVO.setSett_org((String) billvo.getParentVO()
						.getAttributeValue("pk_org"));
				payBillItemVO.setSett_org_v(pk_vid);
				payBillItemVO.setMoney_bal((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setNotax_de((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setLocal_money_de((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setLocal_money_bal((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setLocal_notax_de((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setOccupationmny((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setObjtype(1);
				payBillItemVO.setRowno(0);
				payBillItemVO.setDirection(1);
				payBillItemVO.setPk_group("000112100000000005FD");
				payBillItemVO.setPk_payitem("ID_INDEX0");
				payBillItemVO.setPk_tradetype(pk_tradetype);
				payBillItemVO.setPk_tradetypeid((String) billvo.getParentVO()
						.getAttributeValue("pk_tradetypeid"));
				// ������Ŀ(NC���ز���Ŀ)
				if (!"".equals(payBillItemVO.getDef19())
						&& payBillItemVO.getDef19() != null) {
					if (!("��ҵ��ģʽ".equals(def11))) {
						String def19=convertor.getRefAttributePk(
								"payitem-def21", payBillItemVO.getDef19());
						if(!("F3-Cxx-001".equals(billvo.getHeadVO().getPk_tradetype()))){
							if(def19==null||def19.length()<1){
								throw new BusinessException("������Ŀ(NC���ز���Ŀ) ����" +payBillItemVO.getDef19() 
										+ "δ����NC�����й���");
							}
							payBillItemVO.setDef19(def19);
						}else{
							payBillItemVO.setDef19(def19);
						}
					}else{
						payBillItemVO.setDef19(null);
					}
				}
				// ���򷿼䣨NC���ز���Ŀ��ϸ��
				if (!"".equals(payBillItemVO.getDef20())
						&& payBillItemVO.getDef20() != null) {
					if (!("��ҵ��ģʽ".equals(def11))) {
						String def20=convertor.getRefAttributePk(
								"payitem-def22", payBillItemVO.getDef20());
						if(!("F3-Cxx-001".equals(billvo.getHeadVO().getPk_tradetype()))){
							if(def20==null||def20.length()<1){
								throw new BusinessException("���򷿼䣨NC���ز���Ŀ��ϸ������" +payBillItemVO.getDef20() 
										+ "δ����NC�����й���");
							}
							payBillItemVO.setDef20(def20);
						}else{
							payBillItemVO.setDef20(def20);
						}
					}else{
						payBillItemVO.setDef20(null);
					}
					
				}
				if (!"".equals(payBillItemVO.getDef23())
						&& payBillItemVO.getDef23() != null) {
					// ��������
					payBillItemVO.setDef23(convertor.getRefAttributePk(
							"payitem-def23", payBillItemVO.getDef23()));
				}
				
				if (!"".equals(payBillItemVO.getDef24())
						&& payBillItemVO.getDef24() != null) {
					
					//TODO20200817
					if(!("��ҵ��ģʽ".equals(def11))){
					if("F3-Cxx-019".equals(billvo.getParentVO().getAttributeValue("pk_tradetype"))){
						String ischeck=getTaxsepara(payBillItemVO.getDef24(),"def7");//�Ƿ��˰У��
						if(!("��".equals(ischeck))){
						String iftax=getTaxsepara(payBillItemVO.getDef24(),"def6");//�Ƿ��˰
						   if("Y".equals(iftax)){
							   if("0".equals(payBillItemVO.getDef27())||payBillItemVO.getDef27()==null){//˰��
								   throw new BusinessException("�������ƣ�"+getdefdocname(payBillItemVO.getDef24())+",Ϊ��˰��˰�ʲ���Ϊ���� ");
							   }
						   }else{
							   if(!("0".equals(payBillItemVO.getDef27()))){
								   throw new BusinessException("�������ƣ�"+getdefdocname(payBillItemVO.getDef24())+",Ϊ����˰��˰�ʲ��ܴ����� ");
							   }
						   }
						}
					  }
					}
					// ��������
					payBillItemVO.setDef24(convertor.getRefAttributePk(
							"payitem-def24", payBillItemVO.getDef24()));
					
				}
                 
				// ҵ̬
				if (payBillItemVO.getDef25() != null
						&& !"".equals(payBillItemVO.getDef25())) {

					payBillItemVO.setDef25(convertor.getRefAttributePk(
							"payitem-def25", payBillItemVO.getDef25()));
				}
				// ��˰��ʽ
				if (payBillItemVO.getDef26() != null
						&& !"".equals(payBillItemVO.getDef26())) {
					payBillItemVO.setDef26(convertor.getRefAttributePk(
							"payitem-def26", payBillItemVO.getDef26()));
				}
				// �տ������˻�������
				// payBillItemVO.setDef31(convertor.getRefAttributePk(
				// "payitem-def31", payBillItemVO.getDef31()));
				// �ͻ������˺�
				if (docVO.size() <= 0) {
					payBillItemVO.setRecaccount(result[1]);
				} else {
					
					payBillItemVO.setRecaccount(convertor.getRefAttributePk(
							"payitem-recaccount",
							(String) billvo.getParentVO().getAttributeValue(
									"def43"),
							(String) billvo.getParentVO().getAttributeValue(
									"supplier")));
				}
				//���������
				payBillItemVO.setDef51(getCustomerPK(payBillItemVO.getDef51()));
				//def50��ͬ����
				payBillItemVO.setDef50(getPk_defdocByNameORCode(payBillItemVO.getDef50(), "zdy012"));
				// ���幩Ӧ�̣��ͻ������̣�
				payBillItemVO.setSupplier((String) billvo.getParentVO()
						.getAttributeValue("supplier"));
				// ���幩Ӧ�̣��ͻ������̣�
				payBillItemVO.setCustomer((String) billvo.getParentVO()
						.getAttributeValue("supplier"));
				// ���������˻�
				payBillItemVO.setPayaccount(convertor.getRefAttributePk(
						"payitem-payaccount",
						payBillItemVO.getPayaccount(),
						(String) billvo.getParentVO().getAttributeValue(
								"pk_org")));
				// ���㷽ʽ
				payBillItemVO.setPk_balatype(convertor.getRefAttributePk(
						"payitem-pk_balatype", payBillItemVO.getPk_balatype(),
						payBillItemVO.getPk_balatype()));
			}
			// ***********���������ֵ

			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);

			WorkflownoteVO worknoteVO = null;
			HashMap<String, String> eParam = new HashMap<String, String>();
			// �־û�����VO
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.SAVE,
					"F3", billvo, null);
			Object obj = getPfBusiAction().processAction("SAVE", "F3",
					worknoteVO, billvo, null, eParam);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
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
	 * ��ȡorg_orgs��pk_orgֵ
	 * 
	 * @param pk_org����
	 * 
	 * @return ����ת�����pk_vidֵ
	 */
	public String getvidByorg(String pk_org) throws DAOException {
		String sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + pk_org
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_vid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_vid;
	}
	//F3-Cxx-019	�˿	false	true	
	/**
	 * �Ƿ��˰
	 * @param code
	 * @throws DAOException 
	 */
	public String getTaxsepara(String code,String select) throws DAOException{
		return (String)getBaseDAO().executeQuery("select "+select+" from bd_defdoc where bd_defdoc.pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='zdy021') and code='"+code+"'", new ColumnProcessor());
	}
	
	/**
	 * �õ��Զ���������
	 * @param code
	 * @return
	 * @throws DAOException 
	 */
	public String getdefdocname(String code) throws DAOException{
		return (String)getBaseDAO().executeQuery("select name from bd_defdoc where bd_defdoc.pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='zdy021') and code='"+code+"'", new ColumnProcessor());
	}
	/**
	 * ���Ȼ�ȡ�ڲ���Ӧ��
	 */
	public String getinner_supplier(String ...condons) throws DAOException{
		String sql = "SELECT pk_supplier,to_char(supprop) supprop FROM bd_supplier WHERE "
				+ " name = ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
				+ "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
				+ "pk_org = ? ))))";
		List<Map<String, String>> listmap=(List<Map<String, String>>)getBaseDAO().executeQuery(sql, new MapListProcessor());
		if(listmap!=null){
			if(listmap.size()==1){
				return listmap.get(0).get("pk_supplier");
			}else if(listmap.size()>1){
				for(Map<String, String> map:listmap){
					if("1".equals(map.get("supprop"))){
						return map.get("pk_supplier");
					}
				}
				return listmap.get(0).get("pk_supplier");
			}
		}
		return null;
	}
}
