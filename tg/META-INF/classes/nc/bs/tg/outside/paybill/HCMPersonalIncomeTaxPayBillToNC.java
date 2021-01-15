package nc.bs.tg.outside.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.service.TimeService;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import vo.tg.outside.PayBillBodyVO;
import vo.tg.outside.PayBillHeadVO;
import vo.tg.outside.PersonalIncomeTaxBodyVO;
import vo.tg.outside.PersonalIncomeTaxHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HCMPersonalIncomeTaxPayBillToNC extends PayBillUtils implements
		ITGSyncService {
	public static final String HCMSalaryOperatorName = "RLZY";// HCMĬ�ϲ���Ա
	private IArapBillPubQueryService arapBillPubQueryService = null;
	String rlzyUserid = null;// HCMϵͳ�����û�
	static HCMPersonalIncomeTaxPayBillToNC utils;

	public static HCMPersonalIncomeTaxPayBillToNC getUtils() {
		if (utils == null) {
			utils = new HCMPersonalIncomeTaxPayBillToNC();
		}
		return utils;
	}

	/**
	 * ���
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(HCMSalaryOperatorName));
		InvocationInfoProxy.getInstance().setUserCode(HCMSalaryOperatorName);

		// �������Ϣ
		JSONObject jsonData = (JSONObject) info.get("data");// ������
		String jsonhead = jsonData.getString("headInfo");// ��ϵͳ��Դ��ͷ����
		String jsonbody = jsonData.getString("bodyInfo");// ��ϵͳ��Դ��������
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + jsonData);
		}

		// ת��json
		PersonalIncomeTaxHeadVO headVO = JSONObject.parseObject(jsonhead,
				PersonalIncomeTaxHeadVO.class);
		List<PersonalIncomeTaxBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				PersonalIncomeTaxBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ����飡json:" + jsonData);
		}

		String srcid = headVO.getSrcid();// ��ϵͳҵ�񵥾�ID
		String srcno = headVO.getSrcbillno();// ��ϵͳҵ�񵥾ݵ��ݺ�
		Map<String, String> resultInfo = new HashMap<String, String>();

		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue("billno")
						+ "��,�����ظ��ϴ�!");
			}
			// AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
			// AggPayBillVO.class, aggVO);
			AggPayBillVO billvo = onTranBill(headVO, bodyVOs);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = (AggPayBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F3", null, billvo, null, eParam);
			// obj = ipfBusiAction.processAction("APPROVE", billtype,
			// finalWorkflowVO == null ? null : finalWorkflowVO, objs[0],
			// userObj == null ? null : userObj, map != null ? map
			// : new HashMap());
			// IPFBusiAction ipfBusiAction = NCLocator.getInstance().lookup(
			// IPFBusiAction.class);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggPayBillVO onTranBill(PersonalIncomeTaxHeadVO headVO,
			List<PersonalIncomeTaxBodyVO> bodyVOs) throws BusinessException {
		AggPayBillVO aggvo = new AggPayBillVO();
		PayBillVO save_headVO = new PayBillVO();
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if (pk_org != null) {
			save_headVO.setAttributeValue("pk_org", pk_org);// ������֯
		} else {
			throw new BusinessException("HCMͬ��NC����ϵͳ���ʸ����" + headVO.getSrcid()
					+ " �Ĳ�����֯��NC����ϵͳ�в����ڣ�������֯��" + headVO.getPk_org());
		}
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// ��ϵͳ����
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setAttributeValue("pk_balatype", getBalatypePkByCode(headVO.getPk_balatype()));// ���㷽ʽ
		save_headVO.setAttributeValue("billdate", headVO.getBilldate());// �ۿ�����
		save_headVO.setAttributeValue("def67", headVO.getDef67().substring(0,7));// ����������

		String pk_vid = getvidByorg((String) save_headVO
				.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("pk_fiorg",
				(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("pk_fiorg_v", pk_vid);
		save_headVO.setAttributeValue("sett_org",
				(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("sett_org_v", pk_vid);
		save_headVO.setAttributeValue("creationtime",
				save_headVO.getAttributeValue("billdate"));
		save_headVO.setAttributeValue("objtype", 1);
		save_headVO.setAttributeValue("billclass", "fk");
		save_headVO.setAttributeValue("approvestatus", -1);
		save_headVO.setAttributeValue("pk_tradetype", "F3-Cxx-032");
		save_headVO.setAttributeValue("pk_billtype", "F3");// �������ͱ���
		save_headVO.setAttributeValue("billdate",
				new UFDate().toString());// ��������
		save_headVO.setAttributeValue("busidate", new UFDate().toString());// 
		save_headVO.setAttributeValue("syscode", 1);// ��������ϵͳ��Ĭ��Ϊ1��1=Ӧ��ϵͳ
		save_headVO.setAttributeValue("src_syscode", 1);// ������Դϵͳ
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		save_headVO.setAttributeValue("billstatus", 2);// ����״̬,Ĭ��Ϊ2������
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// �������ţ�Ĭ��Ϊʱ������
		save_headVO.setAttributeValue("billmaker",getUserIDByCode(HCMSalaryOperatorName));// �Ƶ���
		save_headVO.setAttributeValue("objtype", 1); // ��������
														// 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
		save_headVO.setAttributeValue("creator", getUserIDByCode(HCMSalaryOperatorName));// ������
		save_headVO.setAttributeValue("isinit", UFBoolean.FALSE);// �ڳ���־
		save_headVO.setAttributeValue("isreded", UFBoolean.FALSE);// �Ƿ����
		save_headVO.setStatus(VOStatus.NEW);

		List<PayBillItemVO> bodylist = new ArrayList<>();
		for (PersonalIncomeTaxBodyVO personalIncomeTaxBodyVO : bodyVOs) {
			PayBillItemVO save_bodyVO = new PayBillItemVO();
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// Ӧ�ղ�����֯
			//������㷽ʽΪ�Զ�����ʱ���籣������A11031
			save_bodyVO.setAttributeValue("supplier", getCustomerPK(personalIncomeTaxBodyVO.getSsname()));

			//String accnum = getCustomerBankNum(payBillBodyVO.getSsname(),payBillBodyVO.getRecaccount());
			String num = getBankNUm(getCustomerPK(personalIncomeTaxBodyVO.getSsname()),personalIncomeTaxBodyVO.getRecaccount());
			if(num != null){
				save_bodyVO.setAttributeValue("recaccount", num);
			}else {
				throw new BusinessException("�籣���Ŀ��������˻����籣������������飡���ݺ�srcid:" + headVO.getSrcid());
			}
			save_bodyVO.setAttributeValue("money_de",
					personalIncomeTaxBodyVO.getMoney_de());// ʵ���ϼ� ԭ��
			save_bodyVO.setAttributeValue("local_money_bal",
					personalIncomeTaxBodyVO.getMoney_de());// ����=ԭ�ң�����һ����
			save_bodyVO.setAttributeValue("local_money_de",
					personalIncomeTaxBodyVO.getMoney_de());// ���
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// �����־
			save_bodyVO
			.setAttributeValue("pk_balatype", save_headVO.getPk_balatype());// ���㷽ʽ
			save_bodyVO
					.setAttributeValue("billdate", save_headVO.getBilldate());// ��������
			save_bodyVO
					.setAttributeValue("pk_group", save_headVO.getPk_group());// ��������
			save_bodyVO.setAttributeValue("pk_billtype",
					save_headVO.getPk_billtype());// �������ͱ���
			save_bodyVO.setAttributeValue("billclass",
					save_headVO.getBillclass());// ���ݴ���
			save_bodyVO.setAttributeValue("pk_tradetype",
					save_headVO.getPk_tradetype());// Ӧ������code
			save_bodyVO.setAttributeValue("pk_tradetypeid",
					save_headVO.getPk_tradetypeid());// Ӧ������
			save_bodyVO
					.setAttributeValue("busidate", save_headVO.getBilldate());// ��������
			save_bodyVO.setAttributeValue("objtype", save_headVO.getObjtype());// ��������
																				// 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
			save_bodyVO.setAttributeValue("direction", 1);// ����
			save_bodyVO.setAttributeValue("pk_currtype",
					save_headVO.getPk_currtype());// ����
			save_bodyVO.setAttributeValue("rate", 1);// ��֯���һ���
			save_bodyVO.setAttributeValue("pk_deptid",
					save_headVO.getPk_deptid());// ����
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new PayBillItemVO[0]));
		getArapBillPubQueryService().getDefaultVO(aggvo, true);
		return aggvo;
	}

	protected AggPayBillVO onDefaultValue(JSONObject head, JSONArray bodylist)
			throws BusinessException {
		AggPayBillVO aggvo = new AggPayBillVO();
		PayBillVO hvo = new PayBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// ��ǰʱ��
		hvo.setAttributeValue(PayBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// ����
		hvo.setAttributeValue(PayBillVO.PK_ORG, DocInfoQryUtils.getUtils()
				.getOrgInfo(head.getString("org")).get("pk_org"));// Ӧ��������֯->NCҵ��Ԫ����
		hvo.setAttributeValue(PayBillVO.BILLMAKER,
				getUserIDByCode(HCMSalaryOperatorName));// �Ƶ���
		hvo.setAttributeValue(PayBillVO.CREATOR, hvo.getBillmaker());// ������
		hvo.setAttributeValue(PayBillVO.CREATIONTIME, currTime);// ����ʱ��
		hvo.setAttributeValue(PayBillVO.PK_BILLTYPE, IBillFieldGet.F3);// �������ͱ���
		hvo.setAttributeValue(PayBillVO.BILLCLASS, IBillFieldGet.FK);// ���ݴ���
		hvo.setAttributeValue(PayBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ��������ϵͳ
		hvo.setAttributeValue(PayBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ������Դϵͳ
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE, IBillFieldGet.D1);// Ӧ������code
		hvo.setAttributeValue(PayBillVO.BILLSTATUS, ARAPBillStatus.SAVE.VALUE);// ����״̬
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE, "");// ��������

		// BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetype());
		// hvo.setAttributeValue(PayBillVO.PK_TRADETYPEID,
		// billTypeVo.getPk_billtypeid());// Ӧ������
		// hvo.setAttributeValue(PayBillVO.PK_TRADETYPE,
		// billTypeVo.getPk_billtypecode());// Ӧ������
		hvo.setAttributeValue(PayBillVO.ISINIT, UFBoolean.FALSE);// �ڳ���־
		hvo.setAttributeValue(PayBillVO.ISREDED, UFBoolean.FALSE);// �Ƿ����

		hvo.setAttributeValue(PayBillVO.BILLDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(PayBillVO.BUSIDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// ���ݻ�����
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// ���ݻ���ڼ�
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);
		setHeaderVO(hvo, head);

		aggvo.setParentVO(hvo);
		PayableBillItemVO[] itemVOs = new PayableBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayableBillItemVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// �к�
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
		getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	}

	public IArapBillPubQueryService getArapBillPubQueryService() {
		if (arapBillPubQueryService == null) {
			arapBillPubQueryService = NCLocator.getInstance().lookup(
					IArapBillPubQueryService.class);
		}
		return arapBillPubQueryService;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected void setHeaderVO(PayBillVO hvo, JSONObject head)
			throws BusinessException {
	};

	/**
	 * �Զ��嵵��
	 * 
	 * @param key
	 * @param pk_project
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDefdocInfo(String key, String listcode)
			throws BusinessException {
		// listcode = "zdy046";
		return DocInfoQryUtils.getUtils().getDefdocInfo(key, listcode);
	}

	/**
	 * ҵ��Ա��Ϣ
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getPsnInfo(String code) throws BusinessException {
		return DocInfoQryUtils.getUtils().getPsnInfo(code);
	}

	/**
	 * ���ݡ��û����롿��ȡ����
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getUserIDByCode(String code) throws BusinessException {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	// /**
	// * ���ݡ��û����롿��ȡ����
	// *
	// * @param code
	// * @return
	// */
	// public String getUserPkByCode(String code) {
	// String sql =
	// "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
	// + code + "'";
	// String result = null;
	// try {
	// result = (String) getBaseDAO().executeQuery(sql,
	// new ColumnProcessor());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	/**
	 * ���ݱ���������ҿ���
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getcustomer(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_customer from bd_customer where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}

	/**
	 * ���ݡ���˾���롿��ȡ������֯
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	/**
	 * ���ݡ���˾���롿��ȡ����
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_orgByCode(String code) {
		String sql = "select pk_org from org_orgs where (code='" + code
				+ "' or name = '" + code + "') and dr=0 and enablestate=2 ";
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
	 * ���㷽ʽ��ѯ
	 * 
	 * @param balatype
	 * @return
	 */
	protected String getBalatypePkByCode(String balatype) {
		String result = null;
		String sql = "select  pk_balatype from bd_balatype where dr = 0 and code ='"
				+ balatype + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ͨ��ҵ��Ա�������ҵ��Ա���� 
	 * 
	 * @throws BusinessException
	 */
	public String getPk_psndocByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select c.pk_psndoc from bd_psndoc c where c.code = '"
				+ code + "' and nvl(c.dr,0) = 0 and c.enablestate = 2  ");
		String value = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return value;
	}

	/**
	 * ���ݸ��������˻������ȡ��Ӧ���� 2020-07-03-̸�ӽ�
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getPersonalAccountIDByCode(String recaccount,
			String pk_receiver) throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT bd_psnbankacc.pk_bankaccsub AS pk_bankaccsub  ");
		query.append("  FROM bd_bankaccbas, bd_bankaccsub, bd_psnbankacc  ");
		query.append(" WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
		query.append("   AND bd_bankaccsub.pk_bankaccsub = bd_psnbankacc.pk_bankaccsub  ");
		query.append("   AND bd_bankaccsub.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas  ");
		query.append("   and bd_psnbankacc.pk_bankaccsub != '~'  ");
		query.append("   AND bd_bankaccsub.Accnum = '" + recaccount + "'  ");
		query.append("   AND exists (select 1  ");
		query.append("          from bd_bankaccbas bas  ");
		query.append("         where bas.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas  ");
		query.append("           and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y'))  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and (pk_psndoc = '" + pk_receiver + "' and  ");
		query.append("       pk_psnbankacc IN  ");
		query.append("       (SELECT min(pk_psnbankacc)  ");
		query.append("           FROM bd_psnbankacc  ");
		query.append("          GROUP BY pk_bankaccsub, pk_psndoc))  ");
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * ���ݸ��˾pk�������˺Ż�ȡ�˺�
	 */
	public String getBankNUm(String pkcust, String accnum){
		String result = null;
		
		String sql = "  select b.pk_bankaccsub "+
					" from bd_bankaccbas a , bd_bankaccsub b , bd_custbank c "+
						" where a.pk_bankaccbas = c.pk_bankaccbas and c.pk_bankaccsub != '~' and a.pk_bankaccbas = b.pk_bankaccbas "+
						" AND b.pk_bankaccbas = c.pk_bankaccbas and a.accnum = '"+accnum+"' and c.pk_cust = '"+pkcust+"' and c.accclass = '3'";
		
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
