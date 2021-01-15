package nc.bs.tg.outside.ctar;

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
import nc.itf.tg.outside.ITGSyncService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.fct.ar.entity.AggCtArVO;
import nc.vo.fct.ar.entity.CtArVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.LLCtArJsonBVO;
import nc.vo.tg.outside.LLCtArJsonVO;
import nc.vo.tg.outside.LLCtArPlanJsonVO;

import com.alibaba.fastjson.JSONObject;

/**
 * �տ��ͬ���������
 * 
 * @since 2020-07-24
 * @author ̸�ӽ�
 * 
 */
public class CtArBillUtil extends BillUtils implements ITGSyncService {
	static CtArBillUtil utils;
	public static final String DefaultOperator = "LLWYSF";// Ĭ���Ƶ���
	String userid = null;// �����û�

	public static CtArBillUtil getUtils() {
		if (utils == null) {
			utils = new CtArBillUtil();
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
		AggCtArVO billvos[] = null;
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
		LLCtArJsonVO headVO = JSONObject.parseObject(jsonhead,
				LLCtArJsonVO.class);

		// ��ͬ����
		List<LLCtArJsonBVO> ctArBvos = JSONObject.parseArray(
				jsonData.getString("ctarbvos"), LLCtArJsonBVO.class);

		// �տ�ƻ�
		List<LLCtArPlanJsonVO> ctArPlanBvos = JSONObject.parseArray(
				jsonData.getString("ctarplans"), LLCtArPlanJsonVO.class);

		// У���ͷ����
		vaildHeadData(headVO);
		// У���տ�ƻ�����
		vaildCtArPlanBodyData(ctArPlanBvos);
		// У���ͬ��������
		vaildCtArBvosData(ctArBvos);

		String srcid = headVO.getSrcid();// ��ϵͳҵ�񵥾�ID

		String billqueue = methodname + ":" + srcid;
		BillUtils.addBillQueue(billqueue);
		try {
			AggCtArVO aggVO = (AggCtArVO) getBillVO(AggCtArVO.class,
					"isnull(dr,0)=0 and blatest ='Y' and vdef18 = '" + srcid
							+ "'");

			String hpk = null;
			if (aggVO != null) {
				if (aggVO != null) {
					hpk = aggVO.getParentVO().getPrimaryKey();
				}
			}

			AggCtArVO billvo = onTranBill(info, hpk);

			Map valueMap = new HashMap<>();
			HashMap<String, Object> eParam = new HashMap<String, Object>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			if (billvo != null) {
				if (hpk != null && !"".equals(hpk)) {
					Object obj = getPfBusiAction().processAction("MODIFY",
							"FCT2", null, billvo, null, eParam);
					billvos = (AggCtArVO[]) obj;

				} else {
					billvos = ((AggCtArVO[]) getPfBusiAction().processAction(
							"SAVEBASE", "FCT2", null, billvo, null, eParam));

					eParam.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER",
							new AggCtArVO[] { billvos[0] });

					AggCtArVO[] aggvo = (AggCtArVO[]) getPfBusiAction()
							.processAction("APPROVE" + getSaleUserID(), "FCT2",
									null, billvos[0], null, eParam);
					Object processAction = getPfBusiAction().processAction(
							"VALIDATE" + getSaleUserID(), "FCT2", null,
							aggvo[0], null, eParam);

				}

			}

			valueMap.put("billno", billvos[0].getParentVO().getVbillcode());
			valueMap.put("billid", billvos[0].getParentVO().getPrimaryKey());
			return JSONObject.toJSON(valueMap).toString();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}

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
	protected void vaildHeadData(LLCtArJsonVO headvo) throws BusinessException {
		StringBuffer str = new StringBuffer();
		// if (StringUtil.isEmpty(headvo.getCbilltypecode())) {
		// str.append("�����쳣����ͷ�������Ͳ���Ϊ�գ�");
		// }
		if (StringUtil.isEmpty(headvo.getVtrantypecode())) {
			str.append("�����쳣����ͷ��ͬ���Ͳ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getDbilldate())) {
			str.append("�����쳣����ͷ�������ڲ���Ϊ�գ�");
		}
		// if (StringUtil.isEmpty(headvo.getCorigcurrencyid())) {
		// str.append("�����쳣����ͷ���ֲ���Ϊ�գ�");
		// }
		if (StringUtil.isEmpty(headvo.getCtname())) {
			str.append("�����쳣����ͷ��ͬ���Ʋ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVbillcode())) {
			str.append("�����쳣����ͷ��ͬ���벻��Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getSrcid())) {
			str.append("�����쳣����ͷ��ϵͳ��������Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getSrcbillno())) {
			str.append("�����쳣����ͷ��ϵͳ��Ų���Ϊ�գ�");
		}
		// if (StringUtil.isEmpty(headvo.getVdef3())) {
		// str.append("�����쳣����ͷ�׷�����Ϊ�գ�");
		// }
		// if (StringUtil.isEmpty(headvo.getVdef4())) {
		// str.append("�����쳣����ͷ�ҷ�����Ϊ�գ�");
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
	protected CtArVO buildHeadVo(OrgVO orgvo, Object[] defpks,
			LLCtArJsonVO headvo) throws BusinessException {
		CtArVO ctarvo = new CtArVO();
		ctarvo.setBankaccount(headvo.getBankaccount());
		ctarvo.setPk_org(orgvo.getPk_org());// ������֯
		ctarvo.setPk_org_v(orgvo.getPk_vid());// ��֯�汾
		ctarvo.setCbilltypecode("FCT2"); // ҵ������
		ctarvo.setVtrantypecode(headvo.getVtrantypecode());
		ctarvo.setDbilldate(new UFDate(headvo.getDbilldate()));// ��������
		ctarvo.setCcurrencyid("1002Z0100000000001K1");
		ctarvo.setCorigcurrencyid("1002Z0100000000001K1");// ����
		ctarvo.setCrececountryid("0001Z010000000079UJJ");// �ջ����ҵ���
		ctarvo.setCsendcountryid("0001Z010000000079UJJ");// �������ҵ���
		ctarvo.setCtaxcountryid("0001Z010000000079UJJ");// ��˰���ҵ���
		ctarvo.setVbillcode(headvo.getVbillcode());// ��ͬ����
		ctarvo.setCtrantypeid(getBillTypePkByCode(headvo.getVtrantypecode(),
				orgvo.getPk_group()));// ��������
		ctarvo.setPk_customer(getcustomer(headvo.getPk_customer()));// �ͻ�
		ctarvo.setCtname(headvo.getCtname());// ��ͬ����
		ctarvo.setFbuysellflag(1);// ��������
		ctarvo.setPersonnelid(getPsndocPkByCode(headvo.getPersonnelid()));// ������
		if (defpks != null) {
			ctarvo.setDepid(defpks[0].toString());// ����
			ctarvo.setDepid_v(defpks[1].toString());// ���Ű汾
		}
		ctarvo.setValdate(headvo.getValdate() == null ? new UFDate()
				: new UFDate(headvo.getValdate()));// �ƻ���Чʱ��
		ctarvo.setInvallidate(headvo.getInvallidate() == null ? new UFDate()
				: new UFDate(headvo.getInvallidate()));// �ƻ���ֹʱ��
		ctarvo.setSubscribedate(headvo.getSubscribedate() == null ? new UFDate()
				: new UFDate(headvo.getSubscribedate()));// ��ͬǩ����־
		ctarvo.setNexchangerate(new UFDouble(1.00));
		// ��֯�����ţ���Ա����Ϣ
		ctarvo.setOrganizer(orgvo.getPk_org());// �а���֯
		ctarvo.setOrganizer_v(orgvo.getPk_vid());// �а���֯�汾
		ctarvo.setPk_group(orgvo.getPk_group());
		ctarvo.setPk_org(orgvo.getPk_org());
		ctarvo.setPk_org_v(orgvo.getPk_vid());
		ctarvo.setSignorg(orgvo.getPk_org());
		ctarvo.setSignorg_v(orgvo.getPk_vid());
		ctarvo.setOverrate(UFDouble.ZERO_DBL);// ����ͬ�տ����
		ctarvo.setIprintcount(0);
		ctarvo.setFstatusflag(0);// ��ͬ״̬
		ctarvo.setVersion(new UFDouble(1.0));
		ctarvo.setVdef18(headvo.getSrcid());// ��ҵ�շ�ϵͳ����ID
		ctarvo.setVdef19(headvo.getSrcbillno());// ��ҵ�շ�ϵͳ���ݺ�
		ctarvo.setActualvalidate(new UFDate(headvo.getActualvalidate()));// ��ͬ��ʼ����
		ctarvo.setActualinvalidate(new UFDate(headvo.getActualinvalidate()));// ��ͬ��������
		ctarvo.setCitycompany(headvo.getCitycompany()); // ���й�˾
		ctarvo.setEntryname(headvo.getEntryname()); // ������Ŀ����
		ctarvo.setNeighborhood(headvo.getNeighborhood()); // ������Ŀ���
		ctarvo.setRelatedthird(headvo.getRelatedthird()); // ������/������
		ctarvo.setContractdate(headvo.getContractdate()); // ��ͬǩ������
		ctarvo.setStartexecution(headvo.getStartexecution()); // ��ͬԼ���Ŀ�ʼִ�����
		ctarvo.setEndexecution(headvo.getEndexecution()); // ��ͬԼ���Ľ���ִ�����
		ctarvo.setTermcontract(headvo.getTermcontract()); // ��ͬ���ޣ���+�գ�
		ctarvo.setSettlementcycle(headvo.getSettlementcycle()); // ��������
		ctarvo.setTaxrate(headvo.getTaxrate()); // ˰��
		ctarvo.setNtotalorigmny(new UFDouble(headvo.getContractamount())); // ǩԼ��˰���
		ctarvo.setDynamicamount(headvo.getDynamicamount()); // ��̬���
		ctarvo.setContractualstatus(headvo.getContractualstatus()); // ��ͬ״̬
		ctarvo.setVdef42(getdefdocBycode(headvo.getBusinesstype(), "SDLL003")); // ҵ������
		ctarvo.setVdef43(getdefdocBycode(headvo.getBusinessbreakdown(),
				"SDLL004")); // ҵ��ϸ��

		// 2020-09-28-̸�ӽ�--�¼��ֶ�-start

		ctarvo.setCorporatename(headvo.getCorporatename());// �ҷ�ǩ����ͬ��˾����
		ctarvo.setFirst(headvo.getFirst());// �׷�
		ctarvo.setSecond(headvo.getSecond());// �ҷ�
		ctarvo.setThird(headvo.getThird());// ����
		ctarvo.setFourth(headvo.getFourth());// ����
		ctarvo.setFifth(headvo.getFifth());// �췽
		ctarvo.setSixth(headvo.getSixth());// ����
		ctarvo.setOperator(headvo.getOperator());// ������
		ctarvo.setDepartment(headvo.getDepartment());// ���첿��
		ctarvo.setProject(headvo.getProject());// ��Ӫ��Ŀ/�ְ���Ŀ
		ctarvo.setPeriod(headvo.getPeriod());// ����/������
		ctarvo.setTripartiteagreement(headvo.getTripartiteagreement());// ���/����Э���ͬ
		ctarvo.setQuantity(headvo.getQuantity());// ̨��
		ctarvo.setClassification(headvo.getClassification());// ʱ������/ʱ���ز�/������
		ctarvo.setPropertyname(headvo.getPropertyname());// �����ڹ�¥������
		ctarvo.setOtherentryname(headvo.getOtherentryname());// ������Ŀ����

		// 2020-09-28-̸�ӽ�--�¼��ֶ�-end
		return ctarvo;

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
			String parentPKValue, AggCtArVO desAggVO, String bvoPKfiled,
			String table) throws BusinessException {
		// ��������BVOs
		ISuperVO[] syncDesPkBVOs = desAggVO.getChildren(clazz);
		Map<String, String> info = getBvoPkByEbsMap("pk_fct_ar", bvoPKfiled,
				table, parentPKValue);
		List<String> list = new ArrayList<String>();
		if (info.size() > 0) {
			list.addAll(info.values());
		}
		if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
			for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
				tmpDesBVO.setStatus(VOStatus.NEW);
				tmpDesBVO.setAttributeValue("pk_fct_ar", parentPKValue);
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
			String parentPKValue, AggCtArVO desAggVO, String bvoPKfiled,
			String table, ISuperVO[] syncDesPkBVOs, List<String> list)
			throws BusinessException {
		String condition = " pk_fct_ar='" + parentPKValue + "' and dr = 0   ";
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

	protected AggCtArVO onTranBill(HashMap<String, Object> info, String hpk)
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
	protected Object[] getDeptpksByCode(String code, String pk_org)
			throws BusinessException {
		String sql = "select pk_dept,pk_vid  from org_dept "
				+ "  where nvl(org_dept.dr,0)=0  and nvl(org_dept.dr,0)=0 and enablestate = '2' and org_dept.code='"
				+ code + "'";
		Object[] pk_depts = null;
		try {
			pk_depts = (Object[]) getBaseDAO().executeQuery(sql,
					new ArrayProcessor());
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
		String sql = "select bd_psndoc.pk_psndoc from bd_psndoc "
				+ "  where nvl(bd_psndoc.dr,0)=0 and bd_psndoc.code ='" + code
				+ "'";
		String pk = null;
		try {
			pk = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
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
	 * @param ctArBvos
	 *            �տ�ƻ�
	 * @throws BusinessException
	 */
	private void vaildCtArPlanBodyData(List<LLCtArPlanJsonVO> ctArPlanBvos)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (ctArPlanBvos == null || ctArPlanBvos.size() <= 0) {
			str.append("�����쳣���տ�ƻ�ҳǩ����Ϊ�գ�");
		} else {
			for (int i = 0; i < ctArPlanBvos.size(); i++) {
				if (StringUtil.isEmpty(String.valueOf(ctArPlanBvos.get(i)
						.getPlanmoney()))) {
					str.append("�����쳣���տ�ƻ�ҳǩ����" + i + 1 + "�У��տ����Ϊ�գ�");
				}
				if (StringUtil.isEmpty(String.valueOf(ctArPlanBvos.get(i)
						.getName()))) {
					str.append("�����쳣���տ�ƻ�ҳǩ����" + i + 1 + "�У��տ��׼���Ʋ���Ϊ�գ�");
				}
				if (StringUtil.isEmpty(String.valueOf(ctArPlanBvos.get(i)
						.getEnddate()))) {
					str.append("�����쳣���տ�ƻ�ҳǩ����" + i + 1 + "�У��տ����ڲ���Ϊ�գ�");
				}

			}
		}

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 
	 * @param ctArBvos
	 *            ��ͬ������Ϣҳǩ
	 * @throws BusinessException
	 */
	private void vaildCtArBvosData(List<LLCtArJsonBVO> ctArBvos)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (ctArBvos == null || ctArBvos.size() <= 0) {
			str.append("�����쳣���տ��ͬ�������ҳǩ����Ϊ�գ�");
		} else {
			for (int i = 0; i < ctArBvos.size(); i++) {
				if (StringUtil.isEmpty(String.valueOf(ctArBvos.get(i)
						.getNorigtaxmny()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1 + "�У�ǩԼ��˰����Ϊ�գ�");
				}
				if (StringUtil.isEmpty(String.valueOf(ctArBvos.get(i)
						.getNorigmny()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1 + "�У�˰���Ϊ�գ�");
				}
				if (StringUtil.isEmpty(String.valueOf(ctArBvos.get(i)
						.getNtaxrate()))) {
					str.append("�����쳣���տ��ͬ�������ҳǩ����" + i + 1 + "�У�˰�ʲ���Ϊ�գ�");
				}

			}
		}

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

}
