package nc.bs.tg.outside.ctap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.calulate.CalultateUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.outside.LLCtApJsonBVO;
import nc.vo.tg.outside.LLCtApJsonVO;
import nc.vo.tg.outside.LLCtApPlanJsonVO;

import com.alibaba.fastjson.JSONObject;

/**
 * �տ��ͬ���������
 * 
 * @since 2020-07-24
 * @author ̸�ӽ�
 * 
 */
public class CtApBillUtil extends BillUtils implements ITGSyncService {
	static CtApBillUtil utils;
	String userid = null;// �����û�

	public static CtApBillUtil getUtils() {
		if (utils == null) {
			utils = new CtApBillUtil();
		}
		return utils;
	}

	/**
	 * �տ��ͬ
	 * 
	 * @param value
	 * @param dectype
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		AggCtApVO billvos[] = null;
		Map valueMap = new HashMap<>();
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(DefaultOperator));
		// �����û���
		InvocationInfoProxy.getInstance().setUserCode(DefaultOperator);

		// �������Ϣ
		JSONObject jsonData = (JSONObject) info.get("data");// ������
		String jsonhead = jsonData.getString("headInfo");// ��ϵͳ��Դ��ͷ����
		if (jsonData == null || jsonhead == null) {
			throw new BusinessException("�����ݻ��ͷ��ϢΪ�գ����飡json:" + jsonData);
		}
		// ��ͷ��Ϣ
		LLCtApJsonVO headVO = JSONObject.parseObject(jsonhead,
				LLCtApJsonVO.class);
		// ö�٣�00 �ݸ塢10 �����С�20 �����С�30 �����ϡ�40 ����С�50 �����
		String wflowstate = headVO.getWflowstate();
		String srcid = headVO.getSrcid();// ��ϵͳҵ�񵥾�ID
		String srcbillno = headVO.getSrcbillno();
		// ���NC�Ƿ�����Ӧ�ĵ��ݴ���
		AggCtApVO aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
				"isnull(dr,0)=0 and blatest ='Y'  and vbillcode = '"
						+ srcbillno + "'");

		String hpk = null;
		if (aggVO != null) {
			hpk = aggVO.getParentVO().getPrimaryKey();
		}
		// �ж��Ƿ������ϵĺ�ͬ-2020-09-04-̸�ӽ�
		if ("30".equals(wflowstate)) {
			if (hpk != null && !"".equals(hpk)) {
				cancellationEbsContract(aggVO);
				valueMap.put("billno", aggVO.getParentVO().getVbillcode());
				valueMap.put("billid", aggVO.getParentVO().getPrimaryKey());
			} else {
				throw new BusinessException("���ݺ�:" + srcid
						+ "��صĺ�ͬ��NC������,���ܽ������Ϻ�ͬ�Ĳ���!");
			}

		} else {
			// ��ͬ����(��Ŀ���)
			List<LLCtApJsonBVO> ctApBvos = JSONObject.parseArray(
					jsonData.getString("ctapbvos"), LLCtApJsonBVO.class);

			if (jsonData == null || jsonhead == null) {
				throw new BusinessException("�����ݻ��ͷ��ϢΪ�գ����飡json:" + jsonData);
			}
			// ����ƻ�
			List<LLCtApPlanJsonVO> ctApPlanBvos = JSONObject.parseArray(
					jsonData.getString("ctapplans"), LLCtApPlanJsonVO.class);
			// У���ͷ����
			vaildHeadData(headVO);
			// У���ͬ��������
			vaildctApBvosData(ctApBvos);
			// У�鸶��ƻ�����
			vaildCtApPlanBodyData(ctApPlanBvos);

			String billqueue = methodname + ":" + srcid;
			BillUtils.addBillQueue(billqueue);
			try {

				AggCtApVO billvo = onTranBill(info, hpk);

				HashMap<String, Object> eParam = new HashMap<String, Object>();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
						PfUtilBaseTools.PARAM_NOTE_CHECKED);

				if (billvo != null) {
					if (hpk != null && !"".equals(hpk)) {
						Object obj = getPfBusiAction().processAction("MODIFY",
								"FCT1", null, billvo, null, eParam);
						billvos = (AggCtApVO[]) obj;
					} else {
						billvos = ((AggCtApVO[]) getPfBusiAction()
								.processAction("SAVEBASE", "FCT1", null,
										billvo, null, eParam));

						billvo = (AggCtApVO) getMDQryService()
								.queryBillOfVOByPK(AggCtApVO.class,
										billvos[0].getPrimaryKey(), false);

						eParam.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER",
								new AggCtApVO[] { billvo });

						Object obj = getPfBusiAction().processAction(
								"APPROVE" + getSaleUserID(), "FCT1", null,
								billvo, null, eParam);

						getPfBusiAction().processAction(
								"VALIDATE" + getSaleUserID(), "FCT1", null,
								((AggCtApVO[]) obj)[0], null, eParam);
					}

				}

			} catch (Exception e) {
				throw new BusinessException(e.getMessage(), e);
			} finally {
				BillUtils.removeBillQueue(billqueue);
			}
			valueMap.put("billno", billvos[0].getParentVO().getVbillcode());
			valueMap.put("billid", billvos[0].getParentVO().getPrimaryKey());
		}
		return JSONObject.toJSON(valueMap).toString();

	}

	/**
	 * ��ѯ
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getCustVO(String code) throws BusinessException {
		String s = (String) super.getBaseDAO().executeQuery(
				"select pk_cust_sup from bd_cust_supplier where code ='" + code
						+ "'", new ColumnProcessor());
		return s;
	}

	/**
	 * �ǿ�����У��
	 * 
	 * @param headvo
	 *            ��ͷVO
	 */
	protected void vaildHeadData(LLCtApJsonVO headvo) throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (StringUtil.isEmpty(headvo.getSrcid())) {
			str.append("�����쳣����ͷSrcid��ϵͳ��������Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getSrcbillno())) {
			str.append("�����쳣����ͷSrcbillno��ϵͳ��Ų���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getOuflag())) {
			str.append("�����쳣����ͷOuflag������֯����˹�˾����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getPlanbudgetname())) {
			str.append("�����쳣����ͷPlanbudgetnameԤ�����岻��Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getFcreatemode())) {
			str.append("�����쳣����ͷFcreatemode��ͬ���Բ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getFcontracttypenew())) {
			str.append("�����쳣����ͷFcontracttypenew��ͬ���Ͳ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getFcontractdetail())) {
			str.append("�����쳣����ͷFcontractdetail��ͬϸ�಻��Ϊ�գ�");
		}
		// if (StringUtil.isEmpty(headvo.getFnumber())) {
		// str.append("�����쳣����ͷFnumber��ͬ���벻��Ϊ�գ�");
		// }
		if (StringUtil.isEmpty(headvo.getFname())) {
			str.append("�����쳣����ͷFname��ͬ���Ʋ���Ϊ�գ�");
		}

		if (StringUtil.isEmpty(headvo.getOrgvendorname())) {
			str.append("�����쳣����ͷOrgvendorname�׷�����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVendorname())) {
			str.append("�����쳣����ͷVendorname�ҷ�����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getCreateddate())) {
			str.append("�����쳣����ͷCreateddate�������ڲ���Ϊ�գ�");
		}
		// if (StringUtil.isEmpty(headvo.getContractoperatorname())) {
		// str.append("�����쳣����ͷContractoperatorname��ͬ�����˲���Ϊ�գ�");
		// }
		// if (StringUtil.isEmpty(headvo.getContractmanagerdepartment())) {
		// str.append("�����쳣����ͷcontractmanagerdepartment��ͬ�����˲��Ų���Ϊ�գ�");
		// }
		if (StringUtil.isEmpty(headvo.getAgent())) {
			str.append("�����쳣����ͷagent�����˲���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getOperatordepartment())) {
			str.append("�����쳣����ͷoperatordepartment�����˲��Ų���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getWflowstate())) {
			str.append("�����쳣����ͷWflowstateEBS��ͬ״̬����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getLldatasources())) {
			str.append("�����쳣����ͷLldatasources������Դ����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getLlpayperiod())) {
			str.append("�����쳣����ͷLlpayperiod�������ڲ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getLlsharecontractflag())) {
			str.append("�����쳣����ͷllsharecontractflag�Ƿ��̯��ͬ����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getLlperiodaccrualflag())) {
			str.append("�����쳣����ͷllperiodaccrualflag�Ƿ����ڼ������ͬ����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getLlsubcontractflag())) {
			str.append("�����쳣����ͷllsubcontractflag�Ƿ�Ϊ�ְ���ͬ����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getCvendorid())) {
			str.append("�����쳣����ͷcvendorid�տ����Ϊ�գ�");
		}
		// if (StringUtil.isEmpty(headvo.getIsreimbursement())) {
		// str.append("�����쳣����ͷisreimbursement�Ƿ��ѱ�������Ϊ�գ�");
		// }

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * ������ͷvo
	 * 
	 * @param srctype
	 *            �����ͬ����
	 * @return
	 * @throws BusinessException
	 */
	protected CtApVO buildHeadVo(OrgVO orgvo, Object[] defpks,
			LLCtApJsonVO headvo) throws BusinessException {
		CtApVO ctApVO = new CtApVO();
		if (defpks != null) {
			ctApVO.setDepid(defpks[0].toString());// ����
			ctApVO.setDepid_v(defpks[1].toString());// ���Ű汾
		}// ���죨�а죩����
		/**
		 * EBS���������ֶ�-2020-08-06-̸�ӽ�-Start
		 */
		ctApVO.setDef49(headvo.getSrcid());// EBS��ͬ����
		ctApVO.setVbillcode(headvo.getSrcbillno());// ��ͬ����

		if (orgvo != null) {
			String pk_org = orgvo.getPk_org();
			String pk_vid = orgvo.getPk_vid();
			ctApVO.setPk_org(pk_org);// ������֯(ebs���˹�˾)
			ctApVO.setPk_org_v(pk_vid);
			// ��ͬ������
			Object[] psndoc = getPsndocByCode(headvo.getContractoperatorname());
			if (psndoc == null || psndoc.length < 0) {
				// throw new BusinessException("��ͬ������Contractoperatorname����:"
				// + headvo.getContractoperatorname() + "��NC�Ҳ�����������Ա��Ϣ!");
			} else {
				ctApVO.setConadmin((String) psndoc[0]);// ��ͬ����������
				ctApVO.setDef110((String) psndoc[1]);// ��ͬ����������
			}

		}
		// ctApVO.setPlate(getPlateByCode(headvo.getPlate()));// ���
		ctApVO.setSubbudget(getSubbudgetByCode(headvo.getPlanbudgetname()));// Ԥ������
		ctApVO.setBilltype(headvo.getFcreatemode());// ��ͬ����(ebs��������)
		ctApVO.setContype(headvo.getFcontracttypenew());// ��ͬ���ͣ�EBS��ͬ���ͣ�
		ctApVO.setCondetails(headvo.getFcontractdetail());// ��ͬϸ��
		ctApVO.setCtname(headvo.getFname());// ��ͬ����
		ctApVO.setB_lease("Y".equals(headvo.getIsleasecontract()) ? UFBoolean.TRUE
				: UFBoolean.FALSE);// �Ƿ����޺�ͬ
		ctApVO.setB_payed("Y".equals(headvo.getIsbond()) ? UFBoolean.TRUE
				: UFBoolean.FALSE);// �Ƿ�����Ѹ�����
		ctApVO.setB_stryear("Y".equals(headvo.getIscrossyear()) ? UFBoolean.TRUE
				: UFBoolean.FALSE);// �Ƿ�Ԥ������ͬ
		ctApVO.setAccountorg(getAccountorgByCode(headvo.getOuflag()));// ���˹�˾
		ctApVO.setFirst(getSupplierByCode(headvo.getOrgvendorname()));// �׷�
		ctApVO.setSecond(getSupplierByCode(headvo.getVendorname()));// �ҷ�
		ctApVO.setThird(getSupplierByCode(headvo.getSecondparty1()));// ����
		ctApVO.setFourth(getSupplierByCode(headvo.getSecondparty2()));// ����
		ctApVO.setFifth(getSupplierByCode(headvo.getSecondparty3()));// �췽
		ctApVO.setSixth(getSupplierByCode(headvo.getSecondparty4()));// ����
		ctApVO.setVdef1(headvo.getLlfsecondparty5name());// ����
		ctApVO.setVdef2(headvo.getLlfsecondparty6name());// ����
		ctApVO.setD_creator(headvo.getCreateddate());// ��������
		// ctApVO.setPersonnelid(getPsndocByCode(headvo.getPersonnelid(),
		// orgvo.getPk_org(), defpks == null ? "" : defpks[0].toString()));//

		// ctApVO.setGrade(getGradeByCode(headvo.getGrade(),
		// orgvo.getPk_org()));// ���죨�а죩ְλ
		ctApVO.setCon_abstract(headvo.getFdescription());// ��ͬժҪ
		ctApVO.setVdef17(headvo.getSumboudamount());// Ѻ��/��֤����
		// ctApVO.setNtotalorigmny(new
		// UFDouble(headvo.getSumdynamicamount()));// ��̬��˰���
		ctApVO.setMsign(new UFDouble(headvo.getFcontractmoney()));// ǩԼ���
		ctApVO.setVdef9(headvo.getSumsupamount());// ����Э����
		ctApVO.setVdef10(headvo.getSumdynamicamount());// ��̬��˰���
		ctApVO.setEbscontractstatus(headvo.getWflowstate());// ebs��ͬ״̬
		ctApVO.setDef61(headvo.getIspresupposition());// �Ƿ��Ԥ��
		ctApVO.setDef38(headvo.getLldatasources());// ������Դ
		String llstartdate = headvo.getLlstartdate();
		if (llstartdate != null && !"".equals(llstartdate)) {
			ctApVO.setDef108(llstartdate);// �����ͬ��ʼ����
		}
		ctApVO.setValdate(new UFDate());// ��ͬ��ʼ����

		String llenddate = headvo.getLlenddate();
		if (llenddate != null && !"".equals(llenddate)) {
			ctApVO.setDef109(llenddate);// �����ͬ��������
		}

		ctApVO.setInvallidate(new UFDate());// ��ͬ��������

		ctApVO.setVdef13(headvo.getLlpayperiod());// ��������
		ctApVO.setVdef3(headvo.getLlbusinessperiod());// ҵ��������
		ctApVO.setDef32(headvo.getLlsharecontractflag());// �Ƿ��̯��ͬ
		ctApVO.setDef33(headvo.getLlperiodaccrualflag());// �Ƿ����ڼ������ͬ
		ctApVO.setDef34(headvo.getLlsubcontractflag());// �Ƿ�Ϊ�ְ���ͬ
		ctApVO.setDef35(headvo.getLlmaincontractno());// �ܰ���ͬ����
		ctApVO.setDef36(headvo.getLlmaincontractname());// �ܰ���ͬ����
		ctApVO.setDef37(headvo.getLlMaincontractamt());// �ܰ���ͬ���
		String cvendorCode = headvo.getCvendorid();// �տ
		String cvendorid = getSupplierByCode(cvendorCode);
		if (cvendorid == null || "".equals(cvendorid)) {
			throw new BusinessException("�տ" + cvendorCode + "��NC��Ӧ��û�ҵ���������Ϣ!");
		}
		ctApVO.setCvendorid(cvendorid);
		ctApVO.setDef47(headvo.getAdvancebillno());// Ԥ�ᵥ��
		ctApVO.setDef48(headvo.getAmountwithdrawn());// ���γ�Ԥ����
		ctApVO.setDef60(headvo.getIsdirectdebit()); // �Ƿ��Զ��ۿ�
		ctApVO.setDef61(headvo.getDeductiontype()); // ��������
		ctApVO.setDef62(headvo.getSigningaccount()); // ǩԼ�˻�
		ctApVO.setDef44(headvo.getIsreimbursement()); // �Ƿ��ѱ���
		ctApVO.setDef45(getPsndocPkByCode(headvo.getExpense_employee_number())); // �����˱��
		// ctApVO.setFstatusflag(1);// ��ͬ״̬Ĭ��Ϊ��Ч

		ctApVO.setDef85(getDeptpksByCode(headvo.getContractmanagerdepartment()));// ��ͬ�����˲���
		ctApVO.setDef86(getPsndocPkByCode(headvo.getAgent()));// ������
		ctApVO.setDef87(getDeptpksByCode(headvo.getOperatordepartment()));// �����˲���

		ctApVO.setDef81(getPkContractByName(headvo.getFcontracttypenew(),
				"SDLL015"));// ��ͬ����
		ctApVO.setDef82(getPkContractByName(headvo.getFcontractdetail(),
				"SDLL016"));// ��ͬϸ��

		/**
		 * EBS���������ֶ�-2020-08-06-̸�ӽ�-end
		 */
		// ********************��ͷ������Ϣ����***************
		// ��ͷĬ����Ϣ����
		ctApVO.setPk_group("000112100000000005FD");
		ctApVO.setRate(UFDouble.ONE_DBL);// ˰��
		ctApVO.setCtrantypeid(getCtrantypeidByCode("FCT1-Cxx-LL01")); // ��������
		ctApVO.setCbilltypecode("FCT1"); // ��������
		ctApVO.setNexchangerate(new UFDouble(100));// �۱�����
		ctApVO.setSubscribedate(new UFDate());// ����ǩ������
		return ctApVO;

	}

	/**
	 * �����±���������Ϣ
	 * 
	 * @param clazz
	 *            voClass
	 * @param parentPKValue
	 *            NC�����ͬ��ͷ����ֵ
	 * @param desAggVO
	 *            ���ι�����aggVO(Ŀ��)
	 * @param bvoPKfiled
	 *            vo�������ֶ���
	 * @param table
	 *            vo���ݱ���
	 * @throws BusinessException
	 */
	protected void syncBvoPkByEbsPk(Class<? extends ISuperVO> clazz,
			String parentPKValue, AggCtApVO desAggVO, String bvoPKfiled,
			String table) throws BusinessException {
		// ��������BVOs
		ISuperVO[] syncDesPkBVOs = desAggVO.getChildren(clazz);
		Map<String, String> info = getBvoPkByEbsMap("pk_fct_ap", bvoPKfiled,
				table, parentPKValue);
		List<String> list = new ArrayList<String>();
		if (info.size() > 0) {
			list.addAll(info.values());
		}
		if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
			for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
				tmpDesBVO.setStatus(VOStatus.NEW);
				tmpDesBVO.setAttributeValue("pk_fct_ap", parentPKValue);
				tmpDesBVO.setAttributeValue("pk_group", InvocationInfoProxy
						.getInstance().getGroupId());

			}
			if (list != null && list.size() > 0) {
				setVODelStatus(clazz, parentPKValue, desAggVO, bvoPKfiled,
						table, syncDesPkBVOs, list);
			}

		}
		if (syncDesPkBVOs == null && !info.isEmpty()) {
			setVODelStatus(clazz, parentPKValue, desAggVO, bvoPKfiled, table,
					syncDesPkBVOs, list);
		}
	}

	/**
	 * �������ɾ����־
	 * 
	 * @param clazz
	 * @param parentPKValue
	 * @param desAggVO
	 * @param bvoPKfiled
	 * @param table
	 * @param syncDesPkBVOs
	 * @param list
	 * @throws BusinessException
	 */
	private void setVODelStatus(Class<? extends ISuperVO> clazz,
			String parentPKValue, AggCtApVO desAggVO, String bvoPKfiled,
			String table, ISuperVO[] syncDesPkBVOs, List<String> list)
			throws BusinessException {
		String condition = " pk_fct_ap='" + parentPKValue + "' and dr = 0   ";
		// ɾ��ԭ��������
		String sqlwhere = "";
		for (String value : list) {
			sqlwhere += "'" + value + "',";
		}
		sqlwhere = sqlwhere.substring(0, sqlwhere.length() - 1);
		Collection<ISuperVO> coll = getBaseDAO().retrieveByClause(clazz,
				condition);
		if (coll != null && coll.size() > 0) {
			List<ISuperVO> bodyList = new ArrayList<ISuperVO>();
			if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
				bodyList.addAll(Arrays.asList(syncDesPkBVOs));
			}
			for (ISuperVO obj : coll) {
				SuperVO vo = (SuperVO) obj;
				vo.setStatus(VOStatus.DELETED);
				bodyList.add(vo);
			}
			desAggVO.setChildren(clazz, bodyList.toArray(new SuperVO[0]));
		}
	}

	/**
	 * <p>
	 * Title: getBvoPkByEbsPK<��p>
	 * <p>
	 * Description: <��p>
	 * 
	 * @param ebsPk
	 *            ebs��Ӧҳǩ����������
	 * @param parentPKfield
	 *            NC�����ͬ��ͷ�����ֶ�
	 * @param bvoPKfiled
	 *            NC�����ͬ��Ӧҳǩ�����ֶ�
	 * @param table
	 *            NC�����ͬ��Ӧҳǩ����
	 * @param parentPkValue
	 *            NC�����ͬ��ͷ����ֵ
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, String> getBvoPkByEbsMap(String parentPKfield,
			String bvoPKfiled, String table, String parentPkValue)
			throws BusinessException {
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = getBaseDAO();
		sql = "select ts," + bvoPKfiled + " pk from " + table + " where  "
				+ parentPKfield + " = ? and dr =0";
		Map<String, String> info = new HashMap<String, String>();
		parameter.addParam(parentPkValue);
		List<Map<String, String>> list = (List<Map<String, String>>) dao
				.executeQuery(sql, parameter, new MapListProcessor());
		if (list != null && list.size() > 0) {
			for (Map<String, String> map : list) {
				info.put(map.get("vbdef2"), map.get("pk"));
			}
		}
		return info;
	}

	protected AggCtApVO onTranBill(HashMap<String, Object> info, String hpk)
			throws BusinessException {
		return null;
	}

	/**
	 * ������֯�����ѯ��֯VO
	 * 
	 * @param orgCode
	 * @return
	 * @throws BusinessException
	 */
	protected OrgVO getOrgVO(String orgCode) throws BusinessException {
		OrgVO orgvo = (OrgVO) super.getBaseDAO().executeQuery(
				"select * from org_orgs where code ='" + orgCode + "'",
				new BeanProcessor(OrgVO.class));
		return orgvo;
	}

	/**
	 * ���ݡ����ű��롿��ȡ����,�汾��Ϣ
	 * 
	 * @param code
	 * @return
	 */
	protected String getDeptpksByCode(String code) throws BusinessException {
		String sql = "select pk_dept  from org_dept "
				+ "  where nvl(org_dept.dr,0)=0  and nvl(org_dept.dr,0)=0 and enablestate = '2' and org_dept.code='"
				+ code + "'";
		String pk_depts = null;
		try {
			pk_depts = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_depts != null) {
				return pk_depts;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
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
		String pk_billtypeid = null;
		String sql = "select pk_billtypeid from bd_billtype where nvl(dr,0)=0 and PK_BILLTYPECODE='"
				+ code + "' and pk_group='" + pk_group + "'";
		try {
			pk_billtypeid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (pk_billtypeid != null) {
			return pk_billtypeid;
		}
		return pk_billtypeid;

	}

	/**
	 * ���ݱ���������ҿ���
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	protected String getcustomer(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = null;
		try {
			obj = dao.executeQuery(
					"select pk_customer from bd_customer where (code='" + code
							+ "' or name='" + code + "') and nvl(dr,0)=0",
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return (String) obj;
	}

	/**
	 * ���ݡ���Ա���롿��ȡ����
	 * 
	 * @param code
	 * @return
	 */
	public String getPsndocPkByCode(String code) {
		StringBuffer query = new StringBuffer();
		query.append("select bd_psndoc.pk_psndoc  ");
		query.append("     from bd_psndoc  ");
		query.append("    where nvl(bd_psndoc.dr, 0) = 0  ");
		query.append("      and bd_psndoc.code = '" + code + "'  ");
		query.append("      and bd_psndoc.enablestate = 2  ");

		String pk = null;
		try {
			pk = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (pk != null && !pk.equals("")) {
			return pk;
		}
		return null;
	}

	/**
	 * ��ȡ��ҵ�շ�ϵͳ����ԱĬ���û�
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getSaleUserID() throws BusinessException {
		if (userid == null) {
			String sql = "select cuserid from sm_user  where user_code = '"
					+ DefaultOperator + "'";
			userid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
		return userid;
	}

	/**
	 * 
	 * @param ctApBvos
	 *            ����ƻ�
	 * @throws BusinessException
	 */
	private void vaildCtApPlanBodyData(List<LLCtApPlanJsonVO> ctApPlanBvos)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (ctApPlanBvos == null || ctApPlanBvos.size() <= 0) {
			str.append("�����쳣������ƻ�ҳǩ����Ϊ�գ�");
		} else {
			for (int i = 0; i < ctApPlanBvos.size(); i++) {
				if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
						.getPk_ebs()))) {
					str.append("�����쳣������ƻ�ҳǩ����" + i + 1 + "�У�pk_ebs����������Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
						.getProceedtype()))) {
					str.append("�����쳣������ƻ�ҳǩ����" + i + 1
							+ "�У�proceedtype������Ͳ���Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
						.getPaymentamount()))) {
					str.append("�����쳣������ƻ�ҳǩ����" + i + 1
							+ "�У�paymentamount�ƻ��������Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
						.getCharactertype()))) {
					str.append("�����쳣������ƻ�ҳǩ����" + i + 1
							+ "�У�charactertype�������ʲ���Ϊ�գ�");
				}

				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getReturnconditions()))) {
				// str.append("�����쳣������ƻ�ҳǩ����" + i + 1
				// + "�У�returnconditions�˻�/�⸶��������Ϊ�գ�");
				// }

				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getLineamountam()))) {
				// str.append("�����쳣������ƻ�ҳǩ����" + i + 1
				// + "�У�lineamountam�ۼ�������Ϊ�գ�");
				// }
				//
				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getPaidamount()))) {
				// str.append("�����쳣������ƻ�ҳǩ����" + i + 1
				// + "�У�paidamount�ۼ��Ѹ������Ϊ�գ�");
				// }

				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getFollowup()))) {
				// str.append("�����쳣������ƻ�ҳǩ����" + i + 1 + "�У�followup����������Ϊ�գ�");
				// }
				//
				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getExpecteddate()))) {
				// str.append("�����쳣������ƻ�ҳǩ����" + i + 1
				// + "�У�expecteddateԤ���˻�/�⸶���ڲ���Ϊ�գ�");
				// }
				//
				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getReturnconditions()))) {
				// str.append("�����쳣������ƻ�ҳǩ����" + i + 1
				// + "�У�returnconditions�˻�/�⸶��������Ϊ�գ�");
				// }
				//
				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getRefunddate()))) {
				// str.append("�����쳣������ƻ�ҳǩ����" + i + 1
				// + "�У�refunddate�յ��˿����ڲ���Ϊ�գ�");
				// }

			}
		}

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 
	 * @param ctApBvos
	 *            ��ͬ������Ϣҳǩ
	 * @throws BusinessException
	 */
	private void vaildctApBvosData(List<LLCtApJsonBVO> ctApBvos)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (ctApBvos == null || ctApBvos.size() <= 0) {
			str.append("�����쳣���տ��ͬ�������ҳǩ����Ϊ�գ�");
		} else {
			for (int i = 0; i < ctApBvos.size(); i++) {
				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getFbudgetsubjectname()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1
							+ "�У�Fbudgetsubjectname�п�Ŀ���Ʋ���Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getPk_ebs()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1 + "�У�pk_ebs����������Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getSplitratio()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1
							+ "�У�splitratio��ֱ�������Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getNotaxmny()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1 + "�У�amount����Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getIspresupposed()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1
							+ "�У�ispresupposed�Ƿ�Ԥ�᲻��Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getIspresupposed()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1
							+ "�У�ispresupposed�Ƿ�Ԥ�᲻��Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getBudgetyears()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1
							+ "�У�budgetyearsԤ����Ȳ���Ϊ�գ�");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getNcprojectname()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1
							+ "�У�ncprojectname��Ŀ����Ϊ�գ�");
				}
			}
		}

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * ��ѯ���
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getPlateByCode(String code) throws BusinessException {
		// ���
		StringBuffer query = new StringBuffer();
		query.append("select pk_defdoc  ");
		query.append("  from bd_defdoc  ");
		query.append(" where code = '" + code + "'  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and (pk_defdoclist =  ");
		query.append("       (select l.pk_defdoclist from bd_defdoclist l where l.code = 'bkxx'))  ");

		String plate = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return plate;

	}

	/**
	 * ��ѯԤ������
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getSubbudgetByCode(String code) throws BusinessException {

		StringBuffer query = new StringBuffer();
		query.append("select PK_PLANBUDGET from org_planbudget where code = '"
				+ code + "' and nvl(dr,0) = 0 and enablestate = 2;  ");

		String subbudget = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return subbudget;

	}

	/**
	 * ��ѯ���˹�˾
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getAccountorgByCode(String code) throws BusinessException {

		StringBuffer query = new StringBuffer();
		query.append("select pk_financeorg  ");
		query.append("  from org_financeorg  ");
		query.append(" where code = '" + code + "'  ");
		query.append("   and nvl(dr, 0) = 0  ");
		query.append("   and enablestate = 2;  ");

		String accountorg = (String) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());
		return accountorg;

	}

	/**
	 * ����code��ѯ��Ӧ�̻�����Ϣ
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getSupplierByCode(String code) throws BusinessException {
		// ����code��ѯ��Ӧ�̻�����Ϣ
		StringBuffer query = new StringBuffer();
		query.append("SELECT pk_supplier  ");
		query.append("  FROM bd_supplier  ");
		query.append(" WHERE CODE = '" + code + "'  ");
		query.append("   and enablestate = '2'  ");
		query.append("   and dr = 0  ");
		String supplier = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return supplier;
	}

	/**
	 * ����code��ѯ���죨�а죩ְλ
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getGradeByCode(String code, String pk_org)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_post  ");
		query.append("  from om_post  ");
		query.append(" where postcode = '" + code + "'  ");
		query.append("   and enablestate = 2  ");
		query.append("   and pk_org = '" + pk_org + "'  ");

		String grade = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return grade;
	}

	/**
	 * ����code��ѯ���죨�а죩��Ա
	 * 
	 * @param code
	 * @param pk_org
	 * @param defpk
	 * @return
	 * @throws BusinessException
	 */
	private Object[] getPsndocByCode(String code) throws BusinessException {
		Object[] psndoc = null;

		StringBuffer query = new StringBuffer();
		query.append("select bd_psndoc.name,bd_psndoc.pk_psndoc ");
		query.append("  from bd_psndoc  ");
		query.append(" where bd_psndoc.code = '" + code + "'  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and dr = 0  ");

		psndoc = (Object[]) getBaseDAO().executeQuery(query.toString(),
				new ArrayProcessor());

		return psndoc;
	}

	/**
	 * ����code��ѯ������������
	 * 
	 * @param code
	 * @param pk_org
	 * @param defpk
	 * @return
	 * @throws BusinessException
	 */
	private String getCtrantypeidByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_billtypeid  ");
		query.append("  from bd_billtype  ");
		query.append(" where nvl(dr, 0) = 0  ");
		query.append("   and PK_BILLTYPECODE = '" + code + "';  ");

		String ctrantypeid = (String) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());
		return ctrantypeid;
	}

	/**
	 * ���ݱ����ȡ��Ŀ�������� 2020-08-10-̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	public String getPk_objByCode(String objcode, String pk_org)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_obj  ");
		query.append("  from tb_budgetsub  ");
		query.append(" where objcode = '" + objcode + "'  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and ((pk_org = '" + pk_org
				+ "' or pk_group = '000112100000000005FD'))  ");
		String pk_obj = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_obj;
	}

	/**
	 * EBS��ͬ����-2020-09-04
	 * 
	 * @author ̸�ӽ�
	 * 
	 */
	public void cancellationEbsContract(AggCtApVO aggVO)
			throws BusinessException {
		// ��ȡ��ͬ���
		String billno = aggVO.getParentVO().getVbillcode();
		if (billno != null && !"".equals(billno)) {
			// ��ȡllperiodaccrualflag �Ƿ����ڼ������ͬ
			String llperiodaccrualflag = aggVO.getParentVO().getDef33();

			if (llperiodaccrualflag != null && !"".equals(llperiodaccrualflag)) {
				// ����������Ժ�ͬ���ϣ�NC���ɺ���Ӧ������ú�֮ͬǰ�����Լ�����ۼƽ��
				if ("Y".equals(llperiodaccrualflag)) {
					// ��ѯ��ͬ���������ڼ���Ӧ����
					Collection<AggregatedValueObject> billVOs = getpayableBillVO(
							AggPayableBillVO.class,
							"isnull(dr,0)=0 and def5 = '" + billno
									+ "' and pk_tradetype ='F1-Cxx-LL04'");
					for (AggregatedValueObject vo : billVOs) {
						AggPayableBillVO payableBillvo = (AggPayableBillVO) vo;
						AggPayableBillVO targetBill = changeBill(payableBillvo);
						HashMap eParam = new HashMap();
						eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
								PfUtilBaseTools.PARAM_NOTE_CHECKED);
						WorkflownoteVO worknoteVO = getWorkflowMachine()
								.checkWorkFlow(
										"SAVE",
										targetBill.getHeadVO()
												.getPk_tradetype(), targetBill,
										new HashMap<String, Object>());
						Object obj = getPfBusiAction().processAction("SAVE",
								targetBill.getHeadVO().getPk_tradetype(),
								worknoteVO, targetBill, null, null);
					}
				}
			}
			// ��ֹ��ͬ����
			// TERMINATE
			getPfBusiAction().processAction("TERMINATE", "FCT1", null, aggVO,
					null, null);
		}

	}

	public AggPayableBillVO changeBill(AggPayableBillVO payableBillvo)
			throws BusinessException {
		UFDouble money = UFDouble.ZERO_DBL;// ���
		UFDouble local_money = UFDouble.ZERO_DBL;// ��֯���
		UFDouble group_money = UFDouble.ZERO_DBL;// ���Ž��
		UFDouble global_money = UFDouble.ZERO_DBL;// ȫ�ֽ��
		AggPayableBillVO targetAggVo = payableBillvo.clone();
		PayableBillVO targetHvo = (PayableBillVO) targetAggVo.getParentVO();
		targetHvo.setPk_payablebill(null);
		targetHvo.setBillno(null);
		targetHvo.setAttributeValue(PayableBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// ����
		targetHvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// �ڳ���־
		targetHvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.TRUE);// �Ƿ����
		targetHvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate());// ��������
		targetHvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate());// ��������
		targetHvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate().getYear()));// ���ݻ�����
		targetHvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate().getStrMonth());// ���ݻ���ڼ�
		targetHvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);

		targetHvo.setAttributeValue(PayableBillVO.CREATIONTIME, new UFDate());// ����ʱ��

		targetHvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, "F1-Cxx-LL09");// Ӧ������code

		// ��ϸ��Ϣת��
		PayableBillItemVO[] childrenVOs = (PayableBillItemVO[]) targetAggVo
				.getChildrenVO();
		if (childrenVOs != null && childrenVOs.length > 0) {
			for (int i = 0; i < childrenVOs.length; i++) {
				try {
					PayableBillItemVO payableBillItemVO = childrenVOs[i];
					payableBillItemVO.setPk_payablebill(null);
					payableBillItemVO.setPk_payableitem(null);
					UFDouble money_cr = payableBillItemVO.getMoney_cr();
					if (money_cr != null) {
						money_cr = money_cr.multiply(-1);
						payableBillItemVO.setMoney_cr(money_cr);
					}
					CalultateUtils.getUtils().calculate(targetAggVo,
							IBillFieldGet.MONEY_CR, i, Direction.CREDIT.VALUE);

					UFDouble local_tax_cr = payableBillItemVO.getLocal_tax_cr();
					if (local_tax_cr != null) {
						local_tax_cr = local_tax_cr.multiply(-1);
						payableBillItemVO.setLocal_tax_cr(local_tax_cr);
					}
					CalultateUtils.getUtils().calculate(targetAggVo,
							IBillFieldGet.LOCAL_TAX_CR, i,
							Direction.CREDIT.VALUE);
					money = money.add(payableBillItemVO.getMoney_cr());// ���
					local_money = local_money.add(payableBillItemVO
							.getLocal_money_cr());// ��֯���
					group_money = group_money.add(payableBillItemVO
							.getGroupcrebit());// ���Ž��
					global_money = global_money.add(payableBillItemVO
							.getGlobalcrebit());// ȫ�ֽ��

				} catch (BusinessException e) {
					throw new BusinessException("��[" + (i + 1) + "],"
							+ e.getMessage(), e);
				}

			}

			targetHvo.setAttributeValue(PayableBillVO.MONEY, money);
			targetHvo.setAttributeValue(PayableBillVO.LOCAL_MONEY, local_money);
			targetHvo.setAttributeValue(PayableBillVO.GROUPLOCAL, group_money);
			targetHvo
					.setAttributeValue(PayableBillVO.GLOBALLOCAL, global_money);

		}
		return targetAggVo;

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
	public Collection<AggregatedValueObject> getpayableBillVO(Class c,
			String whereCondStr) throws BusinessException {
		Collection<AggregatedValueObject> aggVos = getMDQryService()
				.queryBillOfVOByCond(c, whereCondStr, false);
		if (aggVos.size() > 0) {
			return aggVos;
		} else {
			return null;
		}
	}

	/**
	 * ��ѯ��ͬ���ͺ�ϸ��
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getPkContractByName(String name, String listcode)
			throws BusinessException {

		StringBuffer query = new StringBuffer();
		query.append("select pk_defdoc  ");
		query.append(" from bd_defdoc  ");
		query.append(" where bd_defdoc.name = '" + name + "'");
		query.append(" and bd_defdoc.enablestate = 2  ");
		query.append(" and bd_defdoc.dr  =0  ");
		query.append(" and pk_defdoclist in  ");
		query.append(" (select pk_defdoclist from bd_defdoclist where code = '"
				+ listcode + "');");
		String pk_defdoc = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_defdoc;

	}

}
