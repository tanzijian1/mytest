package nc.bs.tg.outside.salebpm.push;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.bpm.push.QueryDocInfoUtils;
import nc.bs.tg.outside.salebpm.utils.SaleBPMBillUtils;
import nc.bs.tg.outside.salebpm.utils.SalePushBPMBillUtils;
import nc.bs.uap.lock.PKLock;
import nc.itf.arap.prv.IBXBillPrivate;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pnt.vo.FileManageVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.erm.accruedexpense.AccruedDetailVO;
import nc.vo.erm.accruedexpense.AccruedVerifyVO;
import nc.vo.erm.expenseaccount.Moretaxpage;
import nc.vo.erm.expenseaccount.PayPlanDetailVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * �����б������ύ�Ƶ���BPM����
 * @author ln
 *
 */
public class BxbillPushtoBpmUtils extends SaleBPMBillUtils{
	static BxbillPushtoBpmUtils utils;
	public static BxbillPushtoBpmUtils getUtils() {
		if (utils == null) {
			utils = new BxbillPushtoBpmUtils();
		}
		return utils;
	}
	public JKBXVO onPushBillToBPM(JKBXVO jkbxvo)
			throws BusinessException {
		BXVO aggVO = (BXVO) jkbxvo;
		if(aggVO != null){
			JKBXHeaderVO parent = aggVO.getParentVO();
			String usercode = getUser_code(parent.getJkbxr());
			String postcode = getPostcode(parent.getZyx1());
			String deptid = null;
			if (postcode != null) {
				deptid = getDeptID(postcode);
			}
			
			Map<String, Object> formData = getFormData(aggVO);
			Map<String, String> infoMap;
			try {
				infoMap = SalePushBPMBillUtils
						.getUtils()
						.pushBillToBpm(usercode, formData,
								aggVO.getParentVO().getZyx10()+"@@@", deptid,
								parent.getPrimaryKey(),
								(String) parent.getAttributeValue("zyx30"));
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			String pushFlag = "N";//����BPM��־
			if("true".equals(infoMap.get("success")))
				pushFlag = "Y";
			if(StringUtils.isBlank((String) parent.getAttributeValue("zyx30"))){
				parent.setZyx30(infoMap.get("taskID"));
			}
			aggVO.getParentVO().setAttributeValue("zyx29", pushFlag);
		}
		return aggVO;
	}
	/**
	 * ��Ϣת��
	 * 
	 * @param billCode
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFormData(JKBXVO aggVO) 
			throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// ������
		formData = getBxbillsInfo(aggVO);
		return formData;
	}
	/**
	 * ��ô�����Ϣ
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getBxbillsInfo(JKBXVO jkbxvo)
			throws BusinessException {
		String billtype = jkbxvo.getParentVO().getDjlxbm();
		String lock = jkbxvo.getParentVO().getPrimaryKey() + "&ERM";
		// add by huangdq 2019-09-11 ����BPM����������,�����ظ���������
		boolean ispush = PKLock.getInstance().isLocked(lock, null, null);
		if (ispush) {
			throw new LfwRuntimeException("�����ύ��BPM,��ȴ�BPM��Ϣ����!");
		}
		try {
			JKBXHeaderVO parent = jkbxvo.getParentVO();
			String postcode = getPostcode(parent.getZyx1());
			Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
			//����֯��ѯҵ��Ԫ�Զ�����11����bpm add by tjl 2020-04-24
			String billtypebx = parent.getDjlxbm();
			String pk_org = parent.getPk_org();
			String easy = null;
			if("264X-Cxx-007".equals(billtypebx)||"264X-Cxx-008".equals(billtypebx)||"264X-Cxx-009".equals(billtypebx)){
				if(StringUtils.isNotBlank(pk_org)){
					Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_org);
					if(orgsInfoMap!=null){
						easy = orgsInfoMap.get("def11");
					}
				}
			}
			if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}
			//end
			purchase.put("platename", getPlatename(parent.getZyx6()));// �������
			purchase.put("bzbm_name", getBzbm(parent.getBzbm()));// ��������
			purchase.put("pk_org_v_name",
					getPk_org_v_name(parent.getPk_org_v()));// ���˹�˾����
			purchase.put("pk_org_v_code",
					getPk_org_v_code(parent.getPk_org_v()));// ���˹�˾����
			purchase.put("fydwbm_v_name",
					getFydwbm_v_name(parent.getFydwbm_v()));// Ԥ�������λ����
			purchase.put("fydwbm_v_code",
					getFydwbm_v_code(parent.getFydwbm_v()));// Ԥ�������λ����
			purchase.put("fydeptid_v_name",
					getFydeptid_v_name(parent.getFydeptid_v()));// Ԥ�������������
			purchase.put("fydeptid_v_code",
					getFydeptid_v_code(parent.getFydeptid_v()));// Ԥ��������ű���
			purchase.put("dwbm_v_name", getDwbm_v_name(parent.getDwbm_v()));// �����˵�λ����
			purchase.put("dwbm_v_code", getDwbm_v_code(parent.getDwbm_v()));// �����˵�λ����
			purchase.put("deptid_v_name",
					getDeptid_v_name(parent.getDeptid_v()));// �����˲�������
			purchase.put("deptid_v_code",
					getDeptid_v_code(parent.getDeptid_v()));// �����˲��ű���
			purchase.put("jkbxr_name", getJkbxr_name(parent.getJkbxr()));// ����������
			purchase.put("jkbxr_code", getUser_code(parent.getJkbxr()));// �����˱���
			purchase.put("post_name", getPostname(parent.getZyx1()));// ��λ����
			purchase.put("post_code", postcode);// ��λ����
			purchase.put("processCategory_name", parent.getZyx10());// �����������
			purchase.put("processCategory_code",
					getProcessCategory_code(parent.getZyx10()));// ����������
			purchase.put("bbhl", parent.getBbhl() == null ? 0 : parent
					.getBbhl().getDouble());
			purchase.put("bbje", parent.getBbje() == null ? 0 : parent
					.getBbje().getDouble());
			purchase.put("bzbm", parent.getBzbm());// ����
			purchase.put("cjkbbje", parent.getCjkbbje() == null ? 0 : parent
					.getCjkbbje().getDouble());
			purchase.put("cjkybje", parent.getCjkybje() == null ? 0 : parent
					.getCjkybje().getDouble());
			purchase.put("creationtime", parent.getCreationtime() == null ? ""
					: parent.getCreationtime().toString());
			purchase.put("creator", parent.getCreator());
			purchase.put("deptid", parent.getDeptid());
			purchase.put("deptid_v", parent.getDeptid_v());
			purchase.put("djbh", parent.getDjbh());
			purchase.put("djdl", parent.getDjdl());
			purchase.put("djlxbm", parent.getDjlxbm());
			purchase.put("djrq", parent.getDjrq().toString());
			purchase.put("djzt", parent.getDjzt());
			purchase.put("dwbm", parent.getDwbm());
			purchase.put("dwbm_v", parent.getDwbm_v());
			purchase.put("flexible_flag", parent.getFlexible_flag()
					.booleanValue() == true ? "Y" : "N");
			purchase.put("fydeptid", parent.getFydeptid());
			purchase.put("fydeptid_v", parent.getFydeptid_v());
			purchase.put("fydwbm", parent.getFydwbm());
			purchase.put("fydwbm_v", parent.getFydwbm_v());
			purchase.put("globalbbhl", parent.getGlobalbbhl() == null ? 0
					: parent.getGlobalbbhl().getDouble());
			purchase.put("globalbbje", parent.getGlobalbbje() == null ? 0
					: parent.getGlobalbbje().getDouble());
			purchase.put("globalcjkbbje", parent.getGlobalcjkbbje() == null ? 0
					: parent.getGlobalcjkbbje().getDouble());
			purchase.put("globalhkbbje", parent.getGlobalhkbbje() == null ? 0
					: parent.getGlobalhkbbje().getDouble());
			purchase.put("globalzfbbje", parent.getGlobalzfbbje() == null ? 0
					: parent.getGlobalzfbbje().getDouble());
			purchase.put("groupbbhl", parent.getGroupbbhl() == null ? 0
					: parent.getGroupbbhl().getDouble());
			purchase.put("groupbbje", parent.getGroupbbje() == null ? 0
					: parent.getGroupbbje().getDouble());
			purchase.put("groupcjkbbje", parent.getGroupcjkbbje() == null ? 0
					: parent.getGroupcjkbbje().getDouble());
			purchase.put("grouphkbbje", parent.getGrouphkbbje() == null ? 0
					: parent.getGrouphkbbje().getDouble());
			purchase.put("groupzfbbje", parent.getGroupzfbbje() == null ? 0
					: parent.getGroupzfbbje().getDouble());
			purchase.put("hkbbje", parent.getHkbbje() == null ? 0 : parent
					.getHkbbje().getDouble());
			purchase.put("hkybje", parent.getHkybje() == null ? 0 : parent
					.getHkybje().getDouble());
			purchase.put("ischeck",
					parent.getIscheck()==null? UFBoolean.FALSE.toString() : parent.getIscheck().toString());
			purchase.put("iscostshare",
					parent.getIscostshare()==null? UFBoolean.FALSE.toString() : parent.getIscostshare().toString());
			purchase.put("isexpamt",
					parent.getIsexpamt()==null ? UFBoolean.FALSE.toString() : parent.getIsexpamt().toString());
			purchase.put("isexpedited",
					parent.getIsexpedited()==null ? UFBoolean.FALSE.toString() : parent.getIsexpedited().toString());
			purchase.put("isinitgroup",
					parent.getIsinitgroup()==null ? UFBoolean.FALSE.toString() : parent.getIsinitgroup().toString());
			purchase.put("ismashare",
					parent.getIsmashare()==null ? UFBoolean.FALSE.toString() : parent.getIsmashare().toString());
			purchase.put("isneedimag",
					parent.getIsneedimag()==null ? UFBoolean.FALSE.toString() : parent.getIsneedimag().toString());
			purchase.put("jkbxr", parent.getJkbxr());
			purchase.put("jobid", parent.getJobid());
			purchase.put("kjnd", parent.getKjnd());
			purchase.put("kjqj", parent.getKjqj());
			purchase.put("modifiedtime", parent.getModifiedtime() == null ? ""
					: parent.getModifiedtime().toString());
			purchase.put("modifier", parent.getModifier());
			purchase.put("operator", getOperator_name(parent.getOperator()));// ������
			purchase.put("operator_code",
					getOperator_code(parent.getOperator()));// �����˱���
			purchase.put("payflag", parent.getPayflag());
			purchase.put("paytarget", parent.getPaytarget() == 0 ? "Ա��"
					: parent.getPaytarget() == 1 ? "��Ӧ��" : "�ͻ�");// �տ����
			purchase.put("pk_billtype", parent.getPk_billtype());
			purchase.put("pk_fiorg", parent.getPk_fiorg());
			purchase.put("pk_group", parent.getPk_group());
			purchase.put("pk_jkbx", parent.getPk_jkbx());
			purchase.put("pk_org", parent.getPk_org());
			purchase.put("pk_org_v", parent.getPk_org_v());// ���˹�˾
			purchase.put("pk_payorg", parent.getPk_payorg());
			purchase.put("pk_payorg_v", parent.getPk_payorg_v());
			purchase.put("pk_tradetypeid", parent.getPk_tradetypeid());
			
			String receiver = getReceiver_name(parent.getReceiver());
			String skyhzh = getSkyhzh_accnum(parent.getSkyhzh());
			purchase.put("receiver", receiver);// �տ���
			purchase.put("skyhzh", skyhzh);// �տ��˻�
			purchase.put("spzt", parent.getSpzt());
			purchase.put("sxbz", parent.getSxbz());
			purchase.put("total", parent.getTotal() == null ? 0 : parent
					.getTotal().getDouble());
			purchase.put("ybje", parent.getYbje() == null ? 0 : parent
					.getYbje().getDouble());
			purchase.put("zfbbje", parent.getZfbbje() == null ? 0 : parent
					.getZfbbje().getDouble());
			purchase.put("zfybje", parent.getZfybje() == null ? 0 : parent
					.getZfybje().getDouble());
			
			//TODO modifiedBy ln 2020��5��15��16:14:59  ===start{===
			String conSql = "select NAME from bd_supplier where pk_supplier ='"+parent.getZyx42()+"'";//��ͬ�տ
			String con_name = getIUAPQueryBS(conSql);//�տ
			String hbbm_name = getHbbm_name(parent.getHbbm());
			String custaccount_accnum = getCustaccount_accnum(parent.getCustaccount());//�տ�˻�
			String actualPayee = "";//ʵ���տ(�����տ��Ϣ)
			String receivingBankAccount = "";//�տ������˻�(�����տ��Ϣ)
			if("264X-Cxx-009".equals(billtypebx)){
				if(parent.getPaytarget() == 1){//��Ӧ��
					receivingBankAccount = custaccount_accnum;
					if(!parent.getHbbm().equals(parent.getZyx42())){//ָ���տ���ͬ�տ��ͬ
						//�տ�˻��ÿգ�ʵ���տ=��ͬ�տ
						custaccount_accnum = "";
						actualPayee = hbbm_name;
					}else{
						actualPayee = con_name;
					}
				}else if(parent.getPaytarget() == 0){//Ա��
					custaccount_accnum = "";
					actualPayee = receiver;
					receivingBankAccount = skyhzh;
				}
			}
			purchase.put("hbbm_name", con_name);// �տ
			purchase.put("custaccount_accnum",custaccount_accnum);// �տ�˻�
			//===end}===
			
			purchase.put("tax_rate", getTax_rate(parent.getZyx21()));// ˰��
//			purchase.put("zy", parent.getZy());
			purchase.put("zy", parent.getDef67());
			purchase.put("zyx1", parent.getZyx1());
			purchase.put("zyx2", parent.getZyx2());
			purchase.put("zyx3", parent.getZyx3());
			purchase.put("zyx4", parent.getZyx4());
			purchase.put("zyx5", parent.getZyx5());
			purchase.put("zyx6", parent.getZyx6());
			purchase.put("zyx7", parent.getZyx7());
			purchase.put("zyx8", parent.getZyx8());
			if (billtype.equals("264X-Cxx-001") || billtype.equals("264X-Cxx-004") || billtype.equals("264X-Cxx-009")) {
				purchase.put("contract_name",
						getContract_name(parent.getZyx9()));// ��ͬ����
			} else {
				purchase.put("zyx9", parent.getZyx9());
			}
			purchase.put("zyx10", parent.getZyx10());
			purchase.put("zyx11", parent.getZyx11());
			purchase.put("zyx12", parent.getZyx12());
			purchase.put("zyx13", parent.getZyx13());
			// �Ǻ�ͬ����ñ�������ҵ��Ŀ���� ���� add by KongYL 2018-09-18
			if (billtype.equals("264X-Cxx-002")) {
				purchase.put("zyx14", getOperator_code(parent.getZyx14()));
			} else {
				purchase.put("zyx14", parent.getZyx14());
			}
			// purchase.put("zyx14", parent.getZyx14());
			purchase.put("zyx15", parent.getZyx15());
			purchase.put("zyx16", parent.getZyx16());
			purchase.put("zyx18", parent.getZyx17());
			purchase.put("zyx18", parent.getZyx18());
			purchase.put("zyx19", parent.getZyx19());
			purchase.put("zyx20", parent.getZyx20());
			purchase.put("zyx21", parent.getZyx21());
			purchase.put("zyx22", parent.getZyx22());
			purchase.put("zyx23", parent.getZyx23());
			purchase.put("zyx24", parent.getZyx24());
			purchase.put("zyx25", parent.getZyx25());
			purchase.put("zyx26", parent.getZyx26());
			purchase.put("zyx27", parent.getZyx27());
			purchase.put("zyx28", parent.getZyx28());
			purchase.put("zyx29", parent.getZyx29());
			purchase.put("zyx30", parent.getZyx30());
			String Attachments = "";
			List<FileManageVO> fileVOs = fileManageVOs(parent.getPk_jkbx());
			if (fileVOs != null && fileVOs.size() > 0) {
				for (int i = 0; i < fileVOs.size(); i++) {
					Attachments += fileVOs.get(i).getDocument_name() + "&"
							+ fileVOs.get(i).getFile_id() + ";";
				}
				purchase.put("Attachments",
						Attachments.substring(0, Attachments.lastIndexOf(";")));
			} else {
				purchase.put("Attachments", null);
			}
			
			//TODO addBy ln 2020��2��14��10:12:41 ===start{===
			purchase.put("BarCode", parent.getZyx16());//Ӱ�����
			String jsfsName = getJsfs("name", parent.getJsfs());
			String jsfsCode = getJsfs("code",parent.getJsfs());
			purchase.put("PaymentMethod", jsfsName);//֧����ʽ
			purchase.put("SettlementMethod", jsfsCode);//���㷽ʽ
			purchase.put("StandardPaymentLetterOrNot", parent.getZyx55() == null ? "N":parent.getZyx55());//�Ƿ�Ǳ�׼ָ�����
			purchase.put("ReceiptPriority", "InstantlyPay");//�����̶�
			purchase.put("SupplementOrNot", "N");//�Ƿ񲹸���
			purchase.put("Completed", "N");//�����Ѳ�ȫ
			
			purchase.put("MarginLevel", parent.getZyx53());//��֤�����
			purchase.put("ABSPaidInProportion", parent.getDef65());//ABSʵ������
			purchase.put("ABSPaidInAmount", parent.getZyx54());//ABSʵ�����
			if(billtype.equals("264X-Cxx-008") || billtype.equals("264X-Cxx-009")){
				String sql = null;
				String reqStyle = null;
				sql = "select name from bd_defdoc where pk_defdoc = '"+parent.getZyx59()+"'";
				reqStyle = getIUAPQueryBS(sql);
				String paymentBeforeInvoice = "N";
				if(!StringUtils.isBlank(reqStyle)){
					switch (reqStyle) {
					case "�״��ȸ����Ʊ":
						paymentBeforeInvoice = "Y";
						break;
					case "����ȸ����Ʊ":
						paymentBeforeInvoice = "Y";
						break;
					default:
						break;
					}
				}
				purchase.put("PaymentBeforeInvoice", paymentBeforeInvoice);//�Ƿ��ȸ���󲹷�Ʊ
				if(billtype.equals("264X-Cxx-009")){
					purchase.put("BusinessType", "�������");//ҵ������
					sql = "SELECT name FROM bd_defdoc WHERE pk_defdoc = '"
							+ parent.getZyx31() + "'";
					reqStyle = getIUAPQueryBS(sql);
					purchase.put("PaymentType", reqStyle);//������� 
//					purchase.put("PaymentMethod", "");//֧����ʽ
					String SpecifyOtherPayeeOrNot = "Y";
					if(parent.getZyx42().equals(parent.getHbbm())){
						SpecifyOtherPayeeOrNot = "N";
					}
					purchase.put("SpecifyOtherPayeeOrNot", SpecifyOtherPayeeOrNot);//�Ƿ�ָ�������տ
					purchase.put("AmountOfDeposit", parent.getZyx41());//��֤��/Ѻ��/�����ʽ���
					purchase.put("AccumulatedInvoiceAmount", parent.getZyx51());//�ۼƷ�Ʊ���
					purchase.put("IndentureTrustee", "");//��ͬ������
					purchase.put("ContractManagementDepartment", "");//��ͬ������
					purchase.put("HasOtherPayeeInformation", "Y");//�Ƿ���������տ��Ϣ�ӱ�
					purchase.put("HasArrivalBuildingDetails", "N");//�Ƿ��е�¥��ϸ�ӱ�
					purchase.put("HasInvoiceInformationDetails", "");//�Ƿ��з�Ʊ��Ϣ�ӱ�
					
					String payPlanSql = "SELECT code FROM bd_defdoc WHERE pk_defdoc = '"
							+ parent.getZyx31() + "'";
					String payPlanStyle = getIUAPQueryBS(payPlanSql);
					String str = "N";
					String string = "Y";
					if("001".equals(payPlanStyle)){
						str = "Y";
						string = "N";
					}
					purchase.put("HasPaymentPlan_DynamicAmount", str);//�Ƿ��и���ƻ�-��̬���
					purchase.put("HasPaymentPlan_Margin", string);//�Ƿ��и���ƻ�-��֤��/Ѻ��/�����ʽ��ӱ�
				}
			}
			//===end}===

			// ������Ϣ����
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
			purchaseDetaillist = getPurchaseDetaillist(jkbxvo);
			// Ԥ����Ϣ����
			List<Map<String, Object>> accruedVerifyList = new ArrayList<>();// Ԥ����Ϣ����list
			if (jkbxvo.getAccruedVerifyVO() != null && jkbxvo.getAccruedVerifyVO().length != 0) {
				accruedVerifyList = getAccruedVerifyList(jkbxvo);
			}

			Map<String, Object> formData = new HashMap<String, Object>();// ������
			formData.put("er_bxzb", purchase);
			formData.put("er_busitem", purchaseDetaillist);
			formData.put("er_accrued_verify", accruedVerifyList);
			
			//TODO addBy ln 2020��2��14��10:12:41 ===start{===
			if(billtype.equals("264X-Cxx-009")){
				String sql = "";
				String result = "";
				// �����տ��Ϣ
				Map<String, Object> ortherPayeeMsglist = new HashMap<String, Object>();
				sql = "select name from bd_supplier where pk_supplier = '"+parent.hbbm+"';";
				result = getIUAPQueryBS(sql);
				ortherPayeeMsglist.put("ActualPayee", actualPayee);//ʵ���տ
				ortherPayeeMsglist.put("ActualAmountPaid", parent.getTotal());//ʵ�����
				ortherPayeeMsglist.put("ReceivingBankAccount", receivingBankAccount);//�տ������˻�
				ortherPayeeMsglist.put("JointBankNoOfBillReceiver", parent.getZyx52());//��Ʊ�����к�
				ortherPayeeMsglist.put("ReceivingOpeningBank", parent.getZyx50());//�տ���п�����
				sql = "SELECT CODE FROM bd_supplier WHERE  pk_supplier = '"
						+ parent.getHbbm() + "'";
				result = getIUAPQueryBS(sql);
				ortherPayeeMsglist.put("PayeeCompanyCode", result);//ʵ���տ��˾���
				formData.put("C_OtherPayeeInformation_Details", ortherPayeeMsglist);
				
//				formData.put("C_ArrivalBuilding_Details", "");//��¥��Ϣ
				//��Ʊ��Ϣ
				List<Map<String, Object>> invoiceMsgList = new ArrayList<>();
				invoiceMsgList = getInvoiceMsglist(jkbxvo);
				formData.put("C_InvoiceInformation_Details", invoiceMsgList);
				
				List<Map<String, Object>> payPlanMsgList = getPayPlanMsglist(jkbxvo);
				formData.put("C_PaymentPlan_DynamicAmount_Details", payPlanMsgList);//����ƻ�-��̬���
				formData.put("C_PaymentPlan_Margin_Details", payPlanMsgList);//����ƻ�-��֤��/Ѻ��/�����ʽ�    
			}
			return formData;
		}catch (Exception e){
			throw new BusinessException(e.getMessage());
		}
	}
	/**
	 * ��ȡ������ϸ����
	 * 
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getPurchaseDetaillist(JKBXVO jkbxvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		int i = 1;
		for (BXBusItemVO vos : jkbxvo.getChildrenVO()) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������

			purchaseDetail.put("item_jkbxrname", getJkbxr_name(vos.getJkbxr()));// ���屨��������
			purchaseDetail.put("item_jkbxrcode", getUser_code(vos.getJkbxr()));// ���屨���˱���
			purchaseDetail.put("objname", getObjname(vos.getDefitem12()));// Ԥ���Ŀ����
			purchaseDetail.put("objcode", getObjcode(vos.getDefitem12()));// Ԥ���Ŀ����
			purchaseDetail.put("costtype_name",
					getCosttype_name(vos.getDefitem20()));// ������������
			purchaseDetail
					.put("project_name", getPproject_name(vos.getJobid()));// ��Ŀ����
			purchaseDetail.put("yetai_name", getYetai_name(vos.getDefitem22()));// ҵ̬ά����
			purchaseDetail.put("platedept_name",
					getPlatedept_name(vos.getDefitem26()));// ���Ʋ�������
			purchaseDetail.put("floordept_name",
					getFloordept_name(vos.getDefitem24()));// ����¥������
			purchaseDetail.put("skyhzh_accnum",
					getSkyhzh_accnum(vos.getSkyhzh()));// ���������˺�
			purchaseDetail.put("customer_name",
					getCustomer_name(vos.getCustomer()));// �ͻ�����
			purchaseDetail.put("custaccount_accnum",
					getCustaccount_accnum(vos.getCustaccount()));// ���������˻�
			purchaseDetail.put("szxmid_name", getSzxmid_name(vos.getSzxmid()));// ��֧��Ŀ����
			purchaseDetail.put("invoice_name",
					getInvoice_name(vos.getDefitem15()));// ��Ʊ��������
			purchaseDetail.put("amount", vos.getAmount() == null ? 0 : vos
					.getAmount().getDouble());
			purchaseDetail.put("bbje", vos.getBbje() == null ? 0 : vos
					.getBbje().getDouble());
			purchaseDetail.put("bbye", vos.getBbye() == null ? 0 : vos
					.getBbye().getDouble());
			purchaseDetail.put("cjkbbje", vos.getCjkbbje() == null ? 0 : vos
					.getCjkbbje().getDouble());
			purchaseDetail.put("cjkybje", vos.getCjkybje() == null ? 0 : vos
					.getCjkybje().getDouble());
			purchaseDetail.put("custaccount", vos.getCustaccount());
			purchaseDetail.put("customer", vos.getCustomer());
			purchaseDetail.put("defitem1", vos.getDefitem1());
			purchaseDetail.put("defitem2", vos.getDefitem2());
			purchaseDetail.put("defitem3", vos.getDefitem3());
			purchaseDetail.put("defitem4", vos.getDefitem4());
			purchaseDetail.put("defitem5", vos.getDefitem5());
			purchaseDetail.put("defitem6", vos.getDefitem6());
			purchaseDetail.put("defitem7", vos.getDefitem7());
			purchaseDetail.put("defitem8", vos.getDefitem8());
			purchaseDetail.put("defitem9", vos.getDefitem9());
			purchaseDetail.put("defitem11", vos.getDefitem10());
			purchaseDetail.put("defitem11", vos.getDefitem11());
			purchaseDetail.put("defitem12", vos.getDefitem12());
			purchaseDetail.put("defitem13", vos.getDefitem13());
			purchaseDetail.put("defitem14", vos.getDefitem14());
			purchaseDetail.put("defitem15", vos.getDefitem15());
			purchaseDetail.put("defitem16", vos.getDefitem16());
			purchaseDetail.put("defitem17", vos.getDefitem17());
			purchaseDetail.put("defitem18", vos.getDefitem18());
			purchaseDetail.put("defitem19", vos.getDefitem19());//��Ʊ����
			purchaseDetail.put("defitem20", vos.getDefitem20());
			purchaseDetail.put("defitem21", vos.getDefitem21());
			purchaseDetail.put("defitem22", vos.getDefitem22());
			purchaseDetail.put("defitem23", vos.getDefitem23());
			purchaseDetail.put("defitem24", vos.getDefitem24());
			purchaseDetail.put("defitem25", vos.getDefitem25());
			purchaseDetail.put("defitem26", vos.getDefitem26());
			purchaseDetail.put("defitem27", vos.getDefitem27());
			purchaseDetail.put("defitem28", vos.getDefitem28());
			purchaseDetail.put("defitem29", vos.getDefitem29());
			purchaseDetail.put("defitem30", vos.getDefitem30());
			purchaseDetail.put("defitem31", vos.getDefitem31());
			purchaseDetail.put("defitem32", vos.getDefitem32());
			purchaseDetail.put("defitem33", vos.getDefitem33());
			purchaseDetail.put("defitem34", vos.getDefitem34());
			purchaseDetail.put("defitem35", vos.getDefitem35());
			purchaseDetail.put("defitem36", vos.getDefitem36());
			purchaseDetail.put("defitem37", vos.getDefitem37());
			purchaseDetail.put("defitem38", vos.getDefitem38());
			purchaseDetail.put("defitem39", vos.getDefitem39());
			purchaseDetail.put("defitem40", vos.getDefitem40());
			purchaseDetail.put("defitem41", vos.getDefitem41());
			purchaseDetail.put("defitem42", vos.getDefitem42());
			purchaseDetail.put("defitem43", vos.getDefitem43());
			purchaseDetail.put("defitem44", vos.getDefitem44());
			purchaseDetail.put("defitem45", vos.getDefitem45());
			purchaseDetail.put("defitem46", vos.getDefitem46());
			purchaseDetail.put("defitem47", vos.getDefitem47());
			purchaseDetail.put("defitem48", vos.getDefitem48());
			purchaseDetail.put("defitem49", vos.getDefitem49());
			purchaseDetail.put("defitem50", vos.getDefitem50());
			purchaseDetail.put("deptid", vos.getDeptid());
			purchaseDetail.put("dwbm", vos.getDwbm());
			purchaseDetail.put("fctno", vos.getFctno());
			purchaseDetail.put("freecust", vos.getFreecust());
			purchaseDetail.put("globalbbje", vos.getGlobalbbje() == null ? 0
					: vos.getGlobalbbje().getDouble());
			purchaseDetail.put("globalbbye", vos.getGlobalbbye() == null ? 0
					: vos.getGlobalbbye().getDouble());
			purchaseDetail.put("globalcjkbbje",
					vos.getGlobalcjkbbje() == null ? 0 : vos.getGlobalcjkbbje()
							.getDouble());
			purchaseDetail.put("globalhkbbje",
					vos.getGlobalhkbbje() == null ? 0 : vos.getGlobalhkbbje()
							.getDouble());
			purchaseDetail.put("globalzfbbje",
					vos.getGlobalzfbbje() == null ? 0 : vos.getGlobalzfbbje()
							.getDouble());
			purchaseDetail.put("groupbbje", vos.getGroupbbje() == null ? 0
					: vos.getGroupbbje().getDouble());
			purchaseDetail.put("groupbbye", vos.getGroupbbye() == null ? 0
					: vos.getGroupbbye().getDouble());
			purchaseDetail.put("groupcjkbbje",
					vos.getGroupcjkbbje() == null ? 0 : vos.getGroupcjkbbje()
							.getDouble());
			purchaseDetail.put("grouphkbbje", vos.getGrouphkbbje() == null ? 0
					: vos.getGrouphkbbje().getDouble());
			purchaseDetail.put("groupzfbbje", vos.getGroupzfbbje() == null ? 0
					: vos.getGroupzfbbje().getDouble());
			purchaseDetail.put("hbbm", getHbbm_name(vos.getHbbm()));// ��Ӧ������
			purchaseDetail.put("hkbbje", vos.getHkbbje() == null ? 0 : vos
					.getHkbbje().getDouble());
			purchaseDetail.put("hkybje", vos.getHkybje() == null ? 0 : vos
					.getHkybje().getDouble());
			purchaseDetail.put("jkbxr", vos.getJkbxr());
			purchaseDetail.put("jobid", vos.getJobid());
			purchaseDetail.put("paytarget", vos.getPaytarget() == 0 ? "Ա��"
					: vos.getPaytarget() == 1 ? "��Ӧ��" : "�ͻ�");// �տ����
			purchaseDetail.put("pk_brand", vos.getPk_brand());
			purchaseDetail.put("pk_busitem", vos.getPk_busitem());
			purchaseDetail.put("pk_checkele", vos.getPk_checkele());
			purchaseDetail.put("pk_crmdetail", vos.getPk_crmdetail());
			purchaseDetail.put("pk_item", vos.getPk_item());
			purchaseDetail.put("pk_jkbx", vos.getPk_jkbx());
			purchaseDetail.put("pk_mtapp_detail", vos.getPk_mtapp_detail());
			purchaseDetail.put("pk_pcorg", vos.getPk_pcorg());
			purchaseDetail.put("pk_pcorg_v", vos.getPk_pcorg_v());
			purchaseDetail.put("pk_proline", vos.getPk_proline());
			purchaseDetail.put("pk_reimtype", vos.getPk_reimtype());
			purchaseDetail.put("pk_resacostcenter", vos.getPk_resacostcenter());
			purchaseDetail.put("projecttask", vos.getProjecttask());
			purchaseDetail.put("receiver", getReceiver_name(vos.getReceiver()));// �տ���
			purchaseDetail.put("rowno", vos.getRowno());
			purchaseDetail.put("skyhzh", vos.getSkyhzh());
			purchaseDetail.put("srcbilltype", vos.getSrcbilltype());
			purchaseDetail.put("srctype", vos.getSrctype());
			purchaseDetail.put("szxmid", vos.getSzxmid());
			purchaseDetail.put("tablecode", vos.getTablecode());
			purchaseDetail.put("ybje", vos.getYbje() == null ? 0 : vos
					.getYbje().getDouble());
			purchaseDetail.put("ybye", vos.getYbye() == null ? 0 : vos
					.getYbye().getDouble());
			purchaseDetail.put("yjye", vos.getYjye() == null ? 0 : vos
					.getYjye().getDouble());
			purchaseDetail.put("zfbbje", vos.getZfbbje() == null ? 0 : vos
					.getZfbbje().getDouble());
			purchaseDetail.put("zfybje", vos.getZfybje() == null ? 0 : vos
					.getZfybje().getDouble());
            
			purchaseDetail.put("RelationRowGuid",  i);
			i++;
			String sql= "";
			String result = "";
			purchaseDetail.put("InvoiceAmount", vos.getDefitem23());//��Ʊ���
			purchaseDetail.put("TaxAmount", vos.getDefitem14());//˰��
			if("264X-Cxx-009".equals(jkbxvo.getParentVO().getDjlxbm())){
				purchaseDetail.put("SubjectNumber", vos.getDefitem12());//��Ŀ����
				purchaseDetail.put("SubjectFullName", vos.getDefitem48());//��Ŀȫ��
				sql = "select name from bd_defdoc where pk_defdoc = '"+vos.getDefitem22()+"';";
				result = getIUAPQueryBS(sql);
				purchaseDetail.put("Format", result);//ҵ̬
				purchaseDetail.put("UserOfMoney", "");//�ÿ���
				purchaseDetail.put("ExpenseBearingDepartment", "");//���óе�����
				sql = "select project_name from bd_project where pk_project = '"+vos.getJobid()+"';";
				result = getIUAPQueryBS(sql);
				purchaseDetail.put("Project", result);//��Ŀ
				purchaseDetail.put("Proportion", vos.getDefitem49());//����
				
			}
			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}

	/**
	 * ��ȡԤ���������
	 * 
	 * @param jkbxvo
	 * @return
	 * @author KongYL
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getAccruedVerifyList(JKBXVO jkbxvo)
			throws BusinessException {
		List<Map<String, Object>> accruedVerifyList = new ArrayList<>();
		int i = 1;
		for (AccruedVerifyVO accVerVo : jkbxvo.getAccruedVerifyVO()) {
			Map<String, Object> accruedDetail = new HashMap<String, Object>();
			// ��ȡԤ�ᵥ��������
			String pk_accrued_detail = accVerVo.getPk_accrued_detail();
			// -----------------
			// ��������Ԥ����ϸ er_accrued_verify
			accruedDetail.put("verify_accrued_billno",
					accVerVo.getAccrued_billno());
			accruedDetail.put("verify_bxd_billno", accVerVo.getBxd_billno());
			accruedDetail
					.put("verify_effectstatus", accVerVo.getEffectstatus());
			accruedDetail.put("verify_global_verify_amount", accVerVo
					.getGlobal_verify_amount() == null ? 0 : accVerVo
					.getGlobal_verify_amount().getDouble());
			accruedDetail.put("verify_group_verify_amount", accVerVo
					.getGroup_verify_amount() == null ? 0 : accVerVo
					.getGroup_verify_amount().getDouble());
			accruedDetail.put("verify_org_verify_amount", accVerVo
					.getOrg_verify_amount() == null ? 0 : accVerVo
					.getOrg_verify_amount().getDouble());
			accruedDetail.put("verify_pk_accrued_detail",
					accVerVo.getPk_accrued_detail());
			accruedDetail.put("verify_pk_accrued_verify",
					accVerVo.getPk_accrued_verify());
			accruedDetail.put("verify_pk_bxd", accVerVo.getPk_bxd());
			accruedDetail
					.put("verify_pk_iobsclass", accVerVo.getPk_iobsclass());
			accruedDetail.put("verify_amount",
					accVerVo.getVerify_amount() == null ? 0 : accVerVo
							.getVerify_amount().getDouble());
			accruedDetail.put("verify_date", accVerVo.getVerify_date()
					.toString());
			accruedDetail.put("verify_man_pk", accVerVo.getVerify_man());
			accruedDetail.put("verify_man",
					getSmUserName(accVerVo.getVerify_man()));
			// ---------------------------
			// Ԥ�ᵥ��ͷpk_org���˹�˾
			String sqlGetOrgName = "select name from org_orgs o where pk_org in (select pk_org from er_accrued where pk_accrued_bill = '"
					+ accVerVo.getPk_accrued_bill() + "')";
			String orgName = getIUAPQueryBS(sqlGetOrgName);
			accruedDetail.put("accountOrgName", orgName);
			// Ԥ����ϸ er_accrued_detail
			IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			AccruedDetailVO accDetailVO = (AccruedDetailVO) bs.retrieveByPK(
					AccruedDetailVO.class, pk_accrued_detail);
			accruedDetail.put("amount", accDetailVO.getAmount() == null ? 0
					: accDetailVO.getAmount().toDouble());
			accruedDetail.put("assume_dept", accDetailVO.getAssume_dept());
			// Ԥ�������������
			accruedDetail.put("assume_dept_name",
					getOrg_dept_name(accDetailVO.getAssume_dept()));
			accruedDetail.put("assume_org", accDetailVO.getAssume_org());
			// Ԥ�������λ����
			accruedDetail.put("assume_org_name",
					getOrg_orgs_name(accDetailVO.getAssume_org()));
			accruedDetail.put("defitem1", accDetailVO.getDefitem1());
			accruedDetail.put("defitem10", accDetailVO.getDefitem10());
			accruedDetail.put("defitem11", accDetailVO.getDefitem11());
			// Ԥ���Ŀ
			accruedDetail.put("defitem12",
					getObjname(accDetailVO.getDefitem12()));
			accruedDetail.put("defitem13", accDetailVO.getDefitem13());
			accruedDetail.put("defitem14", accDetailVO.getDefitem14());
			accruedDetail.put("defitem15", accDetailVO.getDefitem15());
			accruedDetail.put("defitem16", accDetailVO.getDefitem16());
			accruedDetail.put("defitem17", accDetailVO.getDefitem17());
			accruedDetail.put("defitem18", accDetailVO.getDefitem18());
			accruedDetail.put("defitem19", accDetailVO.getDefitem19());
			accruedDetail.put("defitem2", accDetailVO.getDefitem2());
			accruedDetail.put("defitem20", accDetailVO.getDefitem20());
			accruedDetail.put("defitem21", accDetailVO.getDefitem21());
			// ҵ̬
			accruedDetail.put("defitem22",
					getYetai_name(accDetailVO.getDefitem22()));
			accruedDetail.put("defitem23", accDetailVO.getDefitem23());
			accruedDetail.put("defitem24", accDetailVO.getDefitem24());
			accruedDetail.put("defitem25", accDetailVO.getDefitem25());
			// ���Ʋ���
			accruedDetail.put("defitem26",
					getPlatedept_name(accDetailVO.getDefitem26()));
			accruedDetail.put("defitem27", accDetailVO.getDefitem27());
			accruedDetail.put("defitem28", accDetailVO.getDefitem28());
			accruedDetail.put("defitem29", accDetailVO.getDefitem29());
			accruedDetail.put("defitem3", accDetailVO.getDefitem3());
			accruedDetail.put("defitem30", accDetailVO.getDefitem30());
			accruedDetail.put("defitem4", accDetailVO.getDefitem4());
			accruedDetail.put("defitem5", accDetailVO.getDefitem5());
			accruedDetail.put("defitem6", accDetailVO.getDefitem6());
			accruedDetail.put("defitem7", accDetailVO.getDefitem7());
			accruedDetail.put("defitem8", accDetailVO.getDefitem8());
			accruedDetail.put("defitem9", accDetailVO.getDefitem9());
			accruedDetail.put("global_amount",
					accDetailVO.getGlobal_amount() == null ? 0 : accDetailVO
							.getGlobal_amount().toDouble());
			accruedDetail.put("global_currinfo", accDetailVO
					.getGlobal_currinfo() == null ? 0 : accDetailVO
					.getGlobal_currinfo().toDouble());
			accruedDetail.put("global_rest_amount", accDetailVO
					.getGlobal_rest_amount() == null ? 0 : accDetailVO
					.getGlobal_rest_amount().toDouble());
			accruedDetail.put("global_verify_amount", accDetailVO
					.getGlobal_verify_amount() == null ? 0 : accDetailVO
					.getGlobal_verify_amount().toDouble());
			accruedDetail.put("group_amount",
					accDetailVO.getGroup_amount() == null ? 0 : accDetailVO
							.getGroup_amount().toDouble());
			accruedDetail.put("group_currinfo",
					accDetailVO.getGroup_currinfo() == null ? 0 : accDetailVO
							.getGroup_currinfo().toDouble());
			accruedDetail.put("group_rest_amount", accDetailVO
					.getGroup_rest_amount() == null ? 0 : accDetailVO
					.getGroup_rest_amount().toDouble());
			accruedDetail.put("group_verify_amount", accDetailVO
					.getGroup_verify_amount() == null ? 0 : accDetailVO
					.getGroup_verify_amount().toDouble());
			accruedDetail.put("org_amount",
					accDetailVO.getOrg_amount() == null ? 0 : accDetailVO
							.getOrg_amount().toDouble());
			accruedDetail.put("org_currinfo",
					accDetailVO.getOrg_currinfo() == null ? 0 : accDetailVO
							.getOrg_currinfo().toDouble());
			accruedDetail.put("org_rest_amount", accDetailVO
					.getOrg_rest_amount() == null ? 0 : accDetailVO
					.getOrg_rest_amount().toDouble());
			accruedDetail.put("org_verify_amount", accDetailVO
					.getOrg_verify_amount() == null ? 0 : accDetailVO
					.getOrg_verify_amount().toDouble());
			accruedDetail.put("pk_accrued_bill",
					accDetailVO.getPk_accrued_bill());
			accruedDetail.put("pk_accrued_detail",
					accDetailVO.getPk_accrued_detail());
			accruedDetail.put("pk_brand", accDetailVO.getPk_brand());
			accruedDetail.put("pk_checkele", accDetailVO.getPk_checkele());
			accruedDetail.put("pk_customer", accDetailVO.getPk_customer());
			accruedDetail.put("pk_iobsclass", accDetailVO.getPk_iobsclass());
			// ��֧��Ŀ_����
			accruedDetail.put("pk_iobsclass_name",
					getSzxmid_name(accDetailVO.getPk_iobsclass()));
			accruedDetail.put("pk_pcorg", accDetailVO.getPk_pcorg());
			accruedDetail.put("pk_project", accDetailVO.getPk_project());
			// ��Ŀ_����
			accruedDetail.put("pk_project_name",
					getPproject_name(accDetailVO.getPk_project()));
			accruedDetail.put("pk_proline", accDetailVO.getPk_proline());
			accruedDetail.put("pk_resacostcenter",
					accDetailVO.getPk_resacostcenter());
			accruedDetail.put("pk_supplier", accDetailVO.getPk_supplier());
			// ��Ӧ�̵���_����
			accruedDetail.put("pk_supplier_name",
					getHbbm_name(accDetailVO.getPk_supplier()));
			accruedDetail.put("pk_wbs", accDetailVO.getPk_wbs());
			accruedDetail.put("predict_rest_amount", accDetailVO
					.getPredict_rest_amount() == null ? 0 : accDetailVO
					.getPredict_rest_amount().toDouble());
			accruedDetail.put("red_amount",
					accDetailVO.getRed_amount() == null ? 0 : accDetailVO
							.getRed_amount().toDouble());
			accruedDetail.put("rest_amount",
					accDetailVO.getRest_amount() == null ? 0 : accDetailVO
							.getRest_amount().toDouble());
			accruedDetail.put("rowno", accDetailVO.getRowno());
			accruedDetail.put("src_accruedpk", accDetailVO.getSrc_accruedpk());
			accruedDetail.put("src_detailpk", accDetailVO.getSrc_detailpk());
			accruedDetail.put("srctype", accDetailVO.getSrctype());
			accruedDetail.put("verify_amount",
					accDetailVO.getVerify_amount() == null ? 0 : accDetailVO
							.getVerify_amount().toDouble());
			// ���д���
			accruedDetail.put("RelationRowGuid", i);
			i++;
			accruedVerifyList.add(accruedDetail);
		}
		return accruedVerifyList;
	}
	/**
	 * ��ȡ��Ʊ��Ϣ�������
	 * 2020��2��17��15:28:54 ln
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getInvoiceMsglist(JKBXVO jkbxvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		String sql = "";
		String result = "";
		if(jkbxvo.getMoreDetailVO() != null && jkbxvo.getMoreDetailVO().length >0){
			for (Moretaxpage vo : jkbxvo.getMoreDetailVO()) {
				Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������
				purchaseDetail.put("InvoiceAmount", vo.getIntaxmny());//��Ʊ���
				sql = "select name from bd_defdoc where pk_defdoc = '"+vo.getMaininvoicetype()+"';";
				result = getIUAPQueryBS(sql);
				purchaseDetail.put("InvoiceType", result);//��Ʊ����
				purchaseDetail.put("TaxRate", vo.getTaxrate());//˰��
				purchaseDetail.put("TaxAmount", vo.getTaxmny());//˰��
				purchaseDetaillist.add(purchaseDetail);
			}
		}
		return purchaseDetaillist;
	}
	/**
	 * ��ȡ��Ʊ��Ϣ�������
	 * 2020��2��17��15:28:54 ln
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getPayPlanMsglist(JKBXVO jkbxvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		String sql = "";
		String result = "";
		if(jkbxvo.getPayPlanDetailVO() != null && jkbxvo.getPayPlanDetailVO().length >0){
			for (PayPlanDetailVO vo : jkbxvo.getPayPlanDetailVO()) {
				Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������
				purchaseDetail.put("PlannedPaymentDate", vo.getPayingdate()==null?null:vo.getPayingdate().toString());//�ƻ���������
				purchaseDetail.put("PlannedPaymentAmount", vo.getPaymoney());//�ƻ�������
				purchaseDetail.put("TermOfPayment", vo.getPaycondit());//��������
				sql = "select name from bd_defdoc where pk_defdoc = '"+vo.getFundtype()+"';";
				result = getIUAPQueryBS(sql);
				purchaseDetail.put("MoneyNature", result);//��������
				purchaseDetail.put("OperationMode", "");//������ʽ
				purchaseDetail.put("AccumulatedRequestedPayment", vo.getTotalapplymoney());//�ۼ��������
				purchaseDetail.put("CumulativePaid", vo.getTotalpaymoney());//�ۼ��Ѹ�����
				purchaseDetail.put("ThisPaymentRequestAmount", vo.getZyx1());//���������
				purchaseDetail.put("CurrentOffsetAmount", "");//���γ�ֽ��
				purchaseDetaillist.add(purchaseDetail);
			}
		}
		return purchaseDetaillist;
	}
	private List<FileManageVO> fileManageVOs(String pk_jkbx) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select b.* from bd_filemanage b, sm_pub_filesystem s "
				+ "where s.pk = b.pk_filemanage and s.isfolder = 'n' and  s.filepath like '"
				+ pk_jkbx + "%'";
		List<FileManageVO> listVos = new ArrayList<FileManageVO>();
		try {
			listVos = (List<FileManageVO>) bs.executeQuery(sql,
					new BeanListProcessor(FileManageVO.class));
			if (listVos != null && listVos.size() > 0) {
				return listVos;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
	}
	private String getIUAPQueryBS(String sql) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String result = "";
		try {
			result = (String) bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * �������
	 * 
	 * @param zyx6
	 * @return
	 */
	private String getPlatename(String zyx6) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ zyx6 + "'";
		return getIUAPQueryBS(sql);
	}
	
	/**
	 * ��������
	 * 
	 * @param bzbm
	 * @return
	 */
	private String getBzbm(String bzbm) {
		String sql = "select b.name from bd_currtype b where b.pk_currtype = '"
				+ bzbm + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ���˹�˾����
	 * 
	 * @param pk_org_v
	 * @return
	 */
	private String getPk_org_v_name(String pk_org_v) {
		String sql = "select o.name, o.code from org_financeorg_v o where o.pk_vid='"
				+ pk_org_v + "'";
		return getIUAPQueryBS(sql);
	}
	/**
	 * ���˹�˾����
	 * 
	 * @param pk_org_v
	 * @return
	 */
	private String getPk_org_v_code(String pk_org_v) {
		String sql = "select o.code, o.code from org_financeorg_v o where o.pk_vid='"
				+ pk_org_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ�������λ����
	 * 
	 * @param fydwbm_v
	 * @return
	 */
	private String getFydwbm_v_name(String fydwbm_v) {
		String sql = "select o.name from org_orgs_v o where o.pk_vid = '"
				+ fydwbm_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ�������λ����
	 * 
	 * @param fydwbm_v
	 * @return
	 */
	private String getFydwbm_v_code(String fydwbm_v) {
		String sql = "select o.code from org_orgs_v o where o.pk_vid = '"
				+ fydwbm_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ�������������
	 * 
	 * @param fydeptid_v
	 * @return
	 */
	private String getFydeptid_v_name(String fydeptid_v) {
		String sql = "select o.name from org_dept_v o where o.pk_vid = '"
				+ fydeptid_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ��������ű���
	 * 
	 * @param fydeptid_v
	 * @return
	 */
	private String getFydeptid_v_code(String fydeptid_v) {
		String sql = "select o.code from org_dept_v o where o.pk_vid = '"
				+ fydeptid_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˵�λ����
	 * 
	 * @param dwbm_v
	 * @return
	 */
	private String getDwbm_v_name(String dwbm_v) {
		String sql = "select o.name from org_adminorg_v o where o.pk_vid='"
				+ dwbm_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˵�λ����
	 * 
	 * @param dwbm_v
	 * @return
	 */
	private String getDwbm_v_code(String dwbm_v) {
		String sql = "select o.code from org_adminorg_v o where o.pk_vid='"
				+ dwbm_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˲�������
	 * 
	 * @param deptid_v
	 * @return
	 */
	private String getDeptid_v_name(String deptid_v) {
		String sql = "select o.name from org_dept_v o where o.pk_vid='"
				+ deptid_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��ȡHRϵͳdeptid
	 * 
	 * @param deptid
	 * @return
	 */
	private String getDeptID(String postcode) {
		String sql = "select c.departmentid from positioitem c where c.seqnum='"
				+ postcode + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˲��ű���
	 * 
	 * @param deptid_v
	 * @return
	 */
	private String getDeptid_v_code(String deptid_v) {
		String sql = "select o.code from org_dept_v o where o.pk_vid='"
				+ deptid_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����������
	 * 
	 * @param jkbxr
	 * @return
	 */
	private String getJkbxr_name(String jkbxr) {
		String sql = "select b.name from bd_psndoc b where b.pk_psndoc='"
				+ jkbxr + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �������û�����
	 */
	private String getUser_code(String pk_psndoc) {
		String sql = "select s.user_code from sm_user s where s.pk_psndoc='"
				+ pk_psndoc + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��λ����
	 * 
	 * @param zyx1
	 * @return
	 */
	private String getPostname(String zyx1) {
		String sql = "select o.postname from om_post o where o.pk_post = '"
				+ zyx1 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��λ����
	 * 
	 * @param zyx1
	 * @return
	 */
	private String getPostcode(String zyx1) {
		String sql = "select o.postcode from om_post o where o.pk_post = '"
				+ zyx1 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��ͬ����
	 * 
	 * @param zyx6
	 * @return
	 */
	private String getContract_name(String zyx9) {
		String sql = "select b.name from yer_contractfile b where b.pk_defdoc = '"
				+ zyx9 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �û�����
	 * 
	 * @param bzbm
	 * @return
	 */
	private String getSmUserName(String cuserid) {
		String sql = "select b.user_name from sm_user b where b.cuserid = '"
				+ cuserid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����������
	 * 
	 * @param zyx10
	 * @return
	 */
	private String getProcessCategory_code(String zyx10) {
		String sql = "select b.code from bd_defdoc b where b.pk_defdoc = '"
				+ zyx10 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ӧ������
	 * 
	 * @param hbbm
	 * @return
	 */
	private String getHbbm_name(String hbbm) {
		String sql = "	select b.name from bd_supplier b where b.pk_supplier = '"
				+ hbbm + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����������
	 * 
	 * @param operator
	 * @return
	 */
	private String getOperator_name(String operator) {
		String sql = "select d.name	from sm_user c, bd_psndoc d"
				+ " where c.pk_psndoc = d.pk_psndoc  and c.cuserid = '"
				+ operator + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �������û�����
	 */
	private String getOperator_code(String operator) {
		String sql = "select s.user_code from sm_user s where s.cuserid='"
				+ operator + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �տ�������
	 * 
	 * @param receiver
	 * @return
	 */
	private String getReceiver_name(String receiver) {
		String sql = "select b.name from bd_psndoc b where b.pk_psndoc='"
				+ receiver + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ˰��
	 * 
	 * @param zyx21
	 * @return
	 */
	private String getTax_rate(String zyx21) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ zyx21 + "'";
		return getIUAPQueryBS(sql);
	}

	// ===========================================================================================
	/**
	 * Ԥ���Ŀ����
	 * 
	 * @param defitem12
	 * @return
	 */
	private String getObjname(Object object) {
		String sql = "select b.objname from tb_budgetsub b where b.pk_obj = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ���Ŀ����
	 * 
	 * @param defitem12
	 * @return
	 */
	private String getObjcode(Object object) {
		String sql = "select b.objcode from tb_budgetsub b where b.pk_obj = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ������������
	 * 
	 * @param defitem20
	 * @return
	 */
	private String getCosttype_name(Object object) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ŀ����
	 * 
	 * @param jobid
	 * @return
	 */
	private String getPproject_name(String jobid) {
		String sql = "select b.project_name from bd_project b where b.pk_project='"
				+ jobid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ҵ̬ά����
	 * 
	 * @param defitem22
	 * @return
	 */
	private String getYetai_name(Object object) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ���Ʋ�������
	 * 
	 * @param defitem26
	 * @return
	 */
	private String getPlatedept_name(Object object) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����¥������
	 * 
	 * @param defitem24
	 * @return
	 */
	private String getFloordept_name(Object defitem24) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ defitem24 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ���������˺�
	 * 
	 * @param skyhzh
	 * @return
	 */
	private String getSkyhzh_accnum(String skyhzh) {
		String sql = "select b.accnum from bd_bankaccsub b where b.pk_bankaccsub='"
				+ skyhzh + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �ͻ�����
	 * 
	 * @param customer
	 * @return
	 */
	private String getCustomer_name(String customer) {
		String sql = "select b.name from bd_customer b where b.pk_customer='"
				+ customer + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˻������˻�
	 * 
	 * @param custaccount
	 * @return
	 */
	private String getCustaccount_accnum(String custaccount) {
		String sql = "select b.accnum from bd_bankaccsub b where b.pk_bankaccsub = '"
				+ custaccount + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��֧��Ŀ����
	 * 
	 * @param szxmid
	 * @return
	 */
	private String getSzxmid_name(String szxmid) {
		String sql = "select b.name from bd_inoutbusiclass b where b.pk_inoutbusiclass='"
				+ szxmid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ʊ��������
	 * 
	 * @param defitem15
	 * @return
	 */
	private String getInvoice_name(Object defitem15) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ defitem15 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * org_orgs��֯����
	 * 
	 * @param defitem15
	 * @return
	 */
	private String getOrg_orgs_name(Object defitem15) {
		String sql = "select name from org_orgs o where o.pk_org = '"
				+ defitem15 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * org_dept��������
	 * 
	 * @param defitem15
	 * @return
	 */
	private String getOrg_dept_name(Object defitem15) {
		String sql = "select name from org_dept o where o.pk_dept = '"
				+ defitem15 + "'";
		return getIUAPQueryBS(sql);
	}
	private String getJsfs(String key,String pk_balatype) {
		String sql = "SELECT "+key+" FROM bd_balatype WHERE pk_balatype = '"+pk_balatype+"' AND enablestate = 2 AND NVL(dr,0)=0";
		return getIUAPQueryBS(sql);
	}
	/**
	 * ����TaskId
	 * 
	 * @param
	 * @return
	 */
	public void updateTaskID(String zyx30, String pk_jkbx) {
		BaseDAO dao = new BaseDAO();
		String sql = "update er_bxzb e  set e.zyx29 = 'Y',e.zyx30 = '" + zyx30
				+ "' where e.pk_jkbx = '" + pk_jkbx + "'";
		try {
			dao.executeUpdate(sql);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡҵ�񵥾ݾۺ�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	private AggregatedValueObject getBillVO(String pk_jkbx)
			throws BusinessException {
		Collection coll = NCLocator.getInstance().lookup(IBXBillPrivate.class).queryVOsByPrimaryKeys(new String[]{pk_jkbx}, "bx");
		if (coll == null || coll.size() == 0) {
			throw new BusinessException("NCϵͳδ�ܹ�����Ϣ!");
		}
		return (AggregatedValueObject) coll.toArray()[0];
	}
}
