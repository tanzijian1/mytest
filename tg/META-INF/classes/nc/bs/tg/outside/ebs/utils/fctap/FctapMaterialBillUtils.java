package nc.bs.tg.outside.ebs.utils.fctap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.gateway60.accountbook.GlOrgUtils;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FctapMaterialBillUtils extends EBSBillUtils {
	static FctapMaterialBillUtils utils;

	public static FctapMaterialBillUtils getUtils() {
		if (utils == null) {
			utils = new FctapMaterialBillUtils();
		}
		return utils;
	}

	/**
	 * �����ͬ-���Ϻ�ͬ����
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		// ��ͷ����
		JSONObject headJSON = (JSONObject) value.get("headInfo");

		String vbillcode = headJSON.getString("vbillcode");// ebsЭ����
		String pk = headJSON.getString("def57");// Э��id

		// Ŀ�굥����+����Э����������
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":"
				+ vbillcode;
		// Ŀ�굥����+���󵥾�pk����־���
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + pk;

		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		AggCtApVO aggVO = null;
		// ���ر���
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			// ���NC�Ƿ�����Ӧ�ĵ��ݴ���
			aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0  and def57 = '" + pk + "'");// and blatest
																// ='Y'

			String hpk = null;
			if (aggVO != null) {
				hpk = aggVO.getParentVO().getPrimaryKey();
			}
			// ����ת�����߶���
			FctapMaterialConvertor fctapConvertor = new FctapMaterialConvertor();
			// ���ñ�ͷkey������ӳ��
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("fct_ap", "�����ͬ");
			fctapConvertor.setHVOKeyName(hVOKeyName);

			// ���ñ���key������ӳ��
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("ExecutionBVO", "ִ�����");
			fctapConvertor.setBVOKeyName(bVOKeyName);

			// ���ñ�ͷ�����ֶ�
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hKeyName = new HashMap<String, String>();
			hKeyName.put("pk_org", "������֯");
			hKeyName.put("vbillcode", "Э����"); // NC��ͬ����
			hKeyName.put("ctname", "Э������");// NC��ͬ����
			// hKeyName.put("vdef21", "��Э����");
			hKeyName.put("vdef22", "��Ӧ�̺�̨id");
			hKeyName.put("cvendorid", "��Ӧ�̱���");
			// hKeyName.put("vdef23", "��Ӧ������");
			hKeyName.put("def68", "Э��˰��");
			hKeyName.put("def71", "�ɹ�Ա");
			hKeyName.put("def72", "���ȿ����");
			hKeyName.put("def73", "��������");
			hKeyName.put("def74", "�ʱ������");
			hValidatedKeyName.put("fct_ap", hKeyName);
			fctapConvertor.sethValidatedKeyName(hValidatedKeyName);

			// ���ñ�������ֶ�
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			// **********ִ�����ҳǩ�ֶμ���
			Map<String, String> bExeContractrBVOKeyName = new HashMap<String, String>();
			bExeContractrBVOKeyName.put("def11", "��id");
			bExeContractrBVOKeyName.put("billno", "����");
			bValidatedKeyName.put("ExecutionBVO", bExeContractrBVOKeyName);
			// **********ִ�����ҳǩ�ֶμ���
			fctapConvertor.setbValidatedKeyName(bValidatedKeyName);

			// ���ò����ֶ�
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("fct_ap-pk_org"); // ������֯
			refKeys.add("fct_ap-personnelid"); // ������
			refKeys.add("fct_ap-cvendorid"); // ��Ӧ������ ���չ�Ӧ��

			fctapConvertor.setRefKeys(refKeys);

			// Ĭ�ϼ���-ʱ������
			fctapConvertor.setDefaultGroup("000112100000000005FD");

			AggCtApVO billvo = (AggCtApVO) fctapConvertor.castToBill(value,
					AggCtApVO.class, aggVO);

			// ���ú�ͬ����Ĭ����Ϣ
			// ��ͷĬ����Ϣ����
			billvo.getParentVO().setCtrantypeid(
					fctapConvertor.getRefAttributePk("ctrantypeid",
							"FCT1-Cxx-002")); // ��������
			billvo.getParentVO().setCbilltypecode("FCT1"); // ��������
			billvo.getParentVO().setValdate(new UFDate()); // �ƻ���Ч����
			billvo.getParentVO().setInvallidate(new UFDate());// �ƻ���ֹ����
			billvo.getParentVO().setNexchangerate(new UFDouble(100));// �۱�����
			billvo.getParentVO().setNtotalorigmny(new UFDouble(0));// ԭ�Ҽ�˰�ϼ�
			billvo.getParentVO().setSubscribedate(new UFDate());// ����ǩ������
			// billvo.getParentVO().setCvendorid(billvo.getParentVO().getSecond());//
			// ��Ӧ��

			// ********************��ͷ������Ϣ����***************
			billvo.getParentVO().setPk_org(
					fctapConvertor.getRefAttributePk("fct_ap-pk_org", billvo
							.getParentVO().getPk_org())); // ������֯
			billvo.getParentVO().setPk_org_v(
					GlOrgUtils.getPkorgVersionByOrgID(billvo.getParentVO()
							.getPk_org()));
			billvo.getParentVO().setCvendorid(
					fctapConvertor.getRefAttributePk("fct_ap-cvendorid", billvo
							.getParentVO().getCvendorid(), billvo.getParentVO()
							.getPk_org(), billvo.getParentVO().getPk_org())); // ��Ӧ������
																				// ���չ�Ӧ��
			// ********************��ͷ������Ϣ����***************

			// ����Ĭ����Ϣ����
			JSONObject jsonObject = (JSONObject) value.get("itemInfo");
			JSONArray executionBVOs = (JSONArray) jsonObject
					.get("ExeccontractrBVO");// ִ�����ҳǩ
			ExecutionBVO[] exeBVOs = new ExecutionBVO[executionBVOs.size()];
			for (int i = 0; i < executionBVOs.size(); i++) {
				JSONObject json = (JSONObject) executionBVOs.get(i);

				boolean check = selectno(json.getString("billno"));
				ExecutionBVO eVO = new ExecutionBVO();
				eVO.setPk_fct_ap(hpk);
				eVO.setBillno(json.getString("billno"));
				eVO.setDef11(json.getString("def11"));
				if (check) {
					eVO.setStatus(VOStatus.UPDATED);
				} else {
					eVO.setStatus(VOStatus.NEW);
				}

				exeBVOs[i] = eVO;
			}
			billvo.setChildrenVO(exeBVOs);

			CtApBVO ctApBVO = new CtApBVO();// �ɱ����ҳǩ����ͬ������
			int ctApBVORowNo = 10;
			ctApBVO.setCrowno(String.valueOf(ctApBVORowNo));
			ctApBVO.setPk_fct_ap(hpk);
			ctApBVO.setPk_org(billvo.getParentVO().getPk_org());
			ctApBVO.setPk_org_v(billvo.getParentVO().getPk_org());
			ctApBVO.setPk_group(billvo.getParentVO().getPk_group());
			ctApBVO.setFtaxtypeflag(1); // ��˰���
			ctApBVO.setNtaxmny(UFDouble.ONE_DBL);
			ctApBVO.setNorigtaxmny(new UFDouble(0)); // ԭ�Ҽ�˰�ϼ�
			billvo.setChildrenVO(new CtApBVO[] { ctApBVO });

			CtApPlanVO ctApPlanVO = new CtApPlanVO(); // ����ƻ�
			// ctApPlanVO.setPlanmoney(new UFDouble(0)); // �ƻ����
			if (aggVO == null) {
				ctApPlanVO.setPlanrate(new UFDouble(100));// �ƻ�����
			} else {
				ctApPlanVO.setPlanrate(UFDouble.ZERO_DBL);// �ƻ�����
			}
			ctApPlanVO.setPk_fct_ap(hpk);
			ctApPlanVO.setPk_org(billvo.getParentVO().getPk_org());
			ctApPlanVO.setPk_group(billvo.getParentVO().getPk_group());
			billvo.setChildrenVO(new CtApPlanVO[] { ctApPlanVO });
			// billvo.getParentVO().setAttributeValue("def40", "asdfasd");

			HashMap<String, Object> eParam = new HashMap<String, Object>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			AggCtApVO billVO = null;

			if (aggVO == null) {
				// �����ڣ���������
				billvo.getParentVO().setCreator(getSaleUserID());
				billvo.getParentVO().setBillmaker(getSaleUserID());
				billvo.getParentVO().setCreationtime(new UFDateTime());
				billvo.getParentVO().setDmakedate(new UFDate());
				billvo.getParentVO().setFstatusflag(0);
				billvo.getParentVO().setVersion(UFDouble.ONE_DBL);

				billVO = ((AggCtApVO[]) getPfBusiAction().processAction(
						"SAVEBASE", "FCT1", null, billvo, null, eParam))[0];
				// ���ر�ͷ���ݺź͵���PK����
				dataMap.put("billid", billVO.getParentVO().getPrimaryKey());
				dataMap.put("vbillcode", billVO.getParentVO().getVbillcode());
			} else {
				hpk = aggVO.getParentVO().getPrimaryKey();

				String vbcode = "";
				String ctname = "";
				String vdef21 = "";
				String vdef22 = "";
				String cvendorid = "";
				String vdef23 = "";
				String def65 = null;
				String def66 = null;
				String def67 = null;
				String def68 = null;
				String def69 = null;
				String def70 = null;
				String def71 = null;
				String def72 = null;
				String def73 = null;
				String def74 = null;
				String def75 = null;

				vbcode = billvo.getParentVO().getVbillcode();
				ctname = billvo.getParentVO().getCtname();
				if (billvo.getParentVO().getVdef21() != null) {
					vdef21 = billvo.getParentVO().getVdef21();
				}
				vdef22 = billvo.getParentVO().getVdef22();
				cvendorid = billvo.getParentVO().getCvendorid();
				if (billvo.getParentVO().getVdef23() != null) {
					vdef23 = billvo.getParentVO().getVdef23();
				}
				if (billvo.getParentVO().getDef65() != null) {
					def65 = billvo.getParentVO().getDef65();
				}
				if (billvo.getParentVO().getDef66() != null) {
					def66 = billvo.getParentVO().getDef66();
				}
				if (billvo.getParentVO().getDef67() != null) {
					def67 = billvo.getParentVO().getDef67();
				}
				if (billvo.getParentVO().getDef68() != null) {
					def68 = billvo.getParentVO().getDef68();
				}
				if (billvo.getParentVO().getDef69() != null) {
					def69 = billvo.getParentVO().getDef69();
				}
				if (billvo.getParentVO().getDef70() != null) {
					def70 = billvo.getParentVO().getDef70();
				}
				if (billvo.getParentVO().getDef71() != null) {
					def71 = billvo.getParentVO().getDef71();
				}
				if (billvo.getParentVO().getDef72() != null) {
					def72 = billvo.getParentVO().getDef72();
				}
				if (billvo.getParentVO().getDef73() != null) {
					def73 = billvo.getParentVO().getDef73();
				}
				if (billvo.getParentVO().getDef74() != null) {
					def74 = billvo.getParentVO().getDef74();
				}
				if (billvo.getParentVO().getDef75() != null) {
					def75 = billvo.getParentVO().getDef75();
				}

				String sql = "update fct_ap set vbillcode = '" + vbcode
						+ "',ctname = '" + ctname + "',vdef21 = '" + vdef21
						+ "',vdef22 = '" + vdef22 + "',cvendorid = '"
						+ cvendorid + "',vdef23 = '" + vdef23 + "',def65 = '"
						+ def65 + "',def66 = '" + def66 + "'" + ",def67 = '"
						+ def67 + "',def68 = '" + def68 + "',def69 = '" + def69
						+ "'" + ",def70 = '" + def70 + "' ,def71 = '" + def71
						+ "'" + ",def72 = '" + def72 + "',def73 = '" + def73
						+ "'" + ",def74 = '" + def74 + "' ,def75 = '" + def75
						+ "'" + " where pk_fct_ap = '" + hpk
						+ "' and nvl(dr,0)=0";
				BaseDAO dao = getBaseDAO();
				dao.executeUpdate(sql);
				String dsql = "delete from fct_execution_b where pk_fct_ap = '"
						+ hpk + "' and nvl(dr,0)=0";
				dao.executeUpdate(dsql);
				for (Object bvos : executionBVOs) {
					ExecutionBVO bvo = new ExecutionBVO();
					JSONObject json = (JSONObject) bvos;
					bvo.setDef11(json.getString("def11"));
					bvo.setBillno(json.getString("billno"));
					bvo.setPk_fct_ap(hpk);
					dao.insertVO(bvo);
				}
				// ���ر�ͷ���ݺź͵���PK����
				dataMap.put("billid", hpk);
				dataMap.put("vbillcode", billvo.getParentVO().getVbillcode());
			}
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(dataMap);
	}

	/**
	 * �Ա����Ψһ�Խ���У��
	 * 
	 * @param billno
	 * @return
	 */
	private Boolean selectno(String billno) {
		boolean check = false;

		int result = 0;

		String sql = "select count(1) from fct_execution_b where nvl(dr,0)=0 billno = "
				+ billno;

		try {
			result = (int) getBaseDAO()
					.executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (result > 0) {
			check = true;
		}

		return check;
	}
}
