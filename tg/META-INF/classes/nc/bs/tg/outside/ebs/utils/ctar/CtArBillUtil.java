package nc.bs.tg.outside.ebs.utils.ctar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.fct.ar.IArMaintain;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.bd.cust.CustSupplierVO;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ar.entity.AggCtArVO;
import nc.vo.fct.ar.entity.CtArBVO;
import nc.vo.fct.ar.entity.CtArChangeVO;
import nc.vo.fct.ar.entity.CtArExecVO;
import nc.vo.fct.ar.entity.CtArMemoraVO;
import nc.vo.fct.ar.entity.CtArPlanVO;
import nc.vo.fct.ar.entity.CtArTermVO;
import nc.vo.fct.ar.entity.CtArVO;
import nc.vo.fct.entity.CtArArsubspiltVO;
import nc.vo.fct.entity.CtArExecDetailVO;
import nc.vo.fct.entity.CtArSubagremntVO;
import nc.vo.fct.entity.FctArmsgVO;
import nc.vo.fct.entity.FctarmsgsubVO;
import nc.vo.fct.entity.FctprogressVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.ctar.CtArExecJsonVO;
import nc.vo.tg.ctar.CtArJsonBVO;
import nc.vo.tg.ctar.CtArJsonVO;
import nc.vo.tg.ctar.CtArPlanJsonVO;
import nc.vo.tg.ctar.CtArSubagremntJsonVO;
import nc.vo.tg.ctar.CtArSubspiltJsonVO;
import nc.vo.tg.ctar.FctArmsgJsonVO;
import nc.vo.tg.ctar.FctarmsgsubJsonVO;
import nc.vo.tg.ctar.FctprogressJsonVO;

import com.alibaba.fastjson.JSONObject;

/**
 * �����ͬ���������
 * 
 * @since 2019-09-04
 * @author lhh
 * 
 */
public class CtArBillUtil extends EBSBillUtils {
	static CtArBillUtil utils;

	public static CtArBillUtil getUtils() {
		if (utils == null) {
			utils = new CtArBillUtil();
		}
		return utils;
	}

	/**
	 * �����ͬ����
	 * 
	 * @param value
	 * @param dectype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		IArMaintain service = NCLocator.getInstance().lookup(IArMaintain.class);
		JSONObject jsonData = (JSONObject) value.get("data");
		// ��ͷ��Ϣ
		CtArJsonVO headvo = JSONObject.parseObject(
				jsonData.getString("ctarheadvo"), CtArJsonVO.class);
		String ebsID = headvo.getVdef18();// EBS����
		String ebsNo = headvo.getVdef18();// EBS���ݺ�
		// Ŀ�굥����+�����ͬ���������
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + ebsNo;
		// Ŀ�굥����+���󵥾�pk����־���
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + ebsID;

//		EBSBillUtils.addBillQueue(billkey);// ���Ӷ��д���

		AggCtArVO aggVO = (AggCtArVO) getBillVO(AggCtArVO.class,
				"isnull(dr,0)=0 and vdef18 = '" + ebsID + "'");
		if (value.get("change") == null && aggVO != null) {
			throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ aggVO.getParentVO().getAttributeValue(CtArVO.VBILLCODE)
					+ "��,����䶯�����߱������!");
		}
		AggCtArVO[] aggvos = null;
		AggCtArVO billVO = null;
		Map valueMap = new HashMap<>();
		HashMap<String, Object> eParam = new HashMap<String, Object>();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		try {
			// ͨ�����ͬ
			if ("31".equals(srctype)) {
				if (value.get("change") != null
						&& !"".equals(value.get("change"))) {
					// ����߼�
					AggCtArVO[] oldaggvos = service
							.queryCtArVoByIds(new String[] { (String) value
									.get("change") });
					if (oldaggvos == null) {
						throw new BusinessException("���ʧ�ܣ�ncϵͳ�в鲻���õ��ݣ�");
					}
					AggCtArVO aggvo = new AggCtArVO();
					aggvo = onTranBill(value, srctype);
					
					//��ԭʼVO��ͷ�Ĳ���������䵽��VO
					String primaryKey = oldaggvos[0].getPrimaryKey();
					aggvo.getParentVO().setPrimaryKey(primaryKey);
					aggvo.getParentVO().setTs(oldaggvos[0].getParentVO().getTs()); // ͬ����ͷts
					aggvo.getParentVO().setStatus(VOStatus.UPDATED);
					aggvo.getParentVO().setBillmaker(oldaggvos[0].getParentVO().getBillmaker());
					aggvo.getParentVO().setActualinvalidate(oldaggvos[0].getParentVO().getActualinvalidate());
					aggvo.getParentVO().setApprover(oldaggvos[0].getParentVO().getApprover());
					aggvo.getParentVO().setCreator(oldaggvos[0].getParentVO().getCreator());
					aggvo.getParentVO().setCreationtime(oldaggvos[0].getParentVO().getCreationtime());
					aggvo.getParentVO().setDmakedate(oldaggvos[0].getParentVO().getDmakedate());
					aggvo.getParentVO().setTaudittime(oldaggvos[0].getParentVO().getTaudittime());
					
					syncBvoPkByEbsPk(CtArBVO.class, primaryKey, aggvo, CtArBVO.PK_FCT_AR_B, CtArBVO.getDefaultTableName());
					syncBvoPkByEbsPk(CtArPlanVO.class, primaryKey, aggvo, CtArPlanVO.PK_FCT_AR_PLAN, "fct_ar_plan");//�տ�ƻ�
					syncBvoPkByEbsPk(CtArSubagremntVO.class, primaryKey, aggvo, CtArSubagremntVO.PK_FCT_AR, CtArSubagremntVO.getDefaultTableName());
					syncBvoPkByEbsPk(CtArChangeVO.class, primaryKey, aggvo, CtArChangeVO.PK_FCT_AR_CHANGE, CtArChangeVO.getDefaultTableName());
					syncBvoPkByEbsPk(CtArExecDetailVO.class, primaryKey, aggvo, CtArExecDetailVO.PK_EXEC, CtArExecDetailVO.getDefaultTableName());
					syncBvoPkByEbsPk(CtArArsubspiltVO.class, primaryKey, aggvo, CtArArsubspiltVO.PK_ARSUBSPILT, CtArArsubspiltVO.getDefaultTableName());
//					setNewMsg(oldaggvos[0], aggvo);
					Object obj = getPfBusiAction().processAction(
							"MODIFY", "FCT2-Cxx-01", null,
							aggvo, null, eParam);
					aggvos = (AggCtArVO[]) obj;
				} else { 
					aggvos = service.save(new AggCtArVO[] { onTranBill(value,
							srctype) });
					if (aggvos.length > 0) {
						billVO = (AggCtArVO) getMDQryService().queryBillOfVOByPK(
								AggCtArVO.class, aggvos[0].getPrimaryKey(), false);

						eParam.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER",
								new AggCtArVO[] { billVO });

						Object obj = getPfBusiAction().processAction(
								"APPROVE" + getSaleUserID(), "FCT2", null, billVO,
								null, eParam);
						getPfBusiAction().processAction("VALIDATE" + getSaleUserID(),
								"FCT2", null, ((AggCtArVO[]) obj)[0], null, eParam);
					}
				}

			} else {
				// �ز��������ͬ
				aggvos = service.save(new AggCtArVO[] { onTranDCBill(value,
						srctype) });
				if (aggvos.length > 0) {
					billVO = (AggCtArVO) getMDQryService().queryBillOfVOByPK(
							AggCtArVO.class, aggvos[0].getPrimaryKey(), false);

					eParam.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER",
							new AggCtArVO[] { billVO });

					Object obj = getPfBusiAction().processAction(
							"APPROVE" + getSaleUserID(), "FCT2", null, billVO,
							null, eParam);
					getPfBusiAction().processAction("VALIDATE" + getSaleUserID(),
							"FCT2", null, ((AggCtArVO[]) obj)[0], null, eParam);
				}
			}

			

			valueMap.put("billno", aggvos[0].getParentVO().getVbillcode());
			valueMap.put("billid", aggvos[0].getParentVO().getPrimaryKey());
			return JSONObject.toJSON(valueMap).toString();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		} finally {
//			EBSBillUtils.removeBillQueue(billkey);
		}

	}

	private AggCtArVO onTranDCBill(HashMap<String, Object> value, String srctype)
			throws BusinessException {
		JSONObject jsonData = (JSONObject) value.get("data");
		CtArJsonVO headvo = JSONObject.parseObject(
				jsonData.getString("ctarheadvo"), CtArJsonVO.class);
		List<FctArmsgJsonVO> armsgvos = JSONObject.parseArray(
				jsonData.getString("armsgvos"), FctArmsgJsonVO.class);// �տ���Ϣ
		List<FctarmsgsubJsonVO> armsgsubvos = JSONObject.parseArray(
				jsonData.getString("armsgsubvos"), FctarmsgsubJsonVO.class);// �տ����Ϣ
		List<FctprogressJsonVO> progressvos = JSONObject.parseArray(
				jsonData.getString("progressvos"), FctprogressJsonVO.class);// ��������
		OrgVO orgvo = getOrgVO(headvo.getPk_org());
		// У���ͷ����
		vaildHeadData(headvo);

		// ��ͷ��Ϣ
		Object[] defpks = super.getDeptpksByCode(headvo.getDepid(),
				orgvo.getPk_org());
		AggCtArVO aggvo = new AggCtArVO();
		CtArVO ctarvo = buildheadvo(orgvo, defpks, headvo, srctype);

		// �տ���Ϣ
		List<FctArmsgVO> armsgList = new ArrayList<>();
		if (armsgvos != null && armsgvos.size() > 0) {
			for (FctArmsgJsonVO armsgjsonvo : armsgvos) {
				FctArmsgVO armsg = new FctArmsgVO();
				armsg.setPaymethod(armsgjsonvo.getPaymethod());// ���ʽ
				armsg.setStantotalprice(new UFDouble(armsgjsonvo
						.getStantotalprice()));// ��׼�ܼ�
				armsg.setPrice(new UFDouble(armsgjsonvo.getPrice()));// ����
				armsg.setPainland(new UFDouble(armsgjsonvo.getPainland()));// �Ѹ����
				armsg.setDebt(new UFDouble(armsgjsonvo.getDebt()));// Ƿ��
				armsg.setBalancedue(new UFDouble(armsgjsonvo.getBalancedue()));// װ��Ƿ��
				armsg.setMertrulitydebt(new UFDouble(armsgjsonvo
						.getMertrulitydebt()));// ����Ƿ��
				armsg.setUnmertrulitydebt(new UFDouble(armsgjsonvo
						.getUnmertrulitydebt()));// δ����Ƿ��
				armsg.setMgpayment(new UFDouble(armsgjsonvo.getMgpayment()));// ���ҿ�
				armsg.setMgpaymentdebt(new UFDouble(armsgjsonvo
						.getMgpaymentdebt()));// ����Ƿ��
				armsg.setMutiplerept(new UFDouble(armsgjsonvo.getMutiplerept()));// ���տ�
				armsg.setMgbank(armsgjsonvo.getMgbank());// ��������
				armsg.setMgstatus(armsgjsonvo.getMgstatus());// ����״̬
				armsg.setMgcompletime(armsgjsonvo.getMgcompletime() == null ? null
						: new UFDateTime(armsgjsonvo.getMgcompletime()));// �������ʱ��
				armsg.setVbdef1(armsgjsonvo.getVbdef1());// �Զ�����1
				armsg.setVbdef2(armsgjsonvo.getVbdef2());// �Զ�����2
				armsg.setVbdef3(armsgjsonvo.getVbdef3());// �Զ�����3
				armsg.setVbdef4(armsgjsonvo.getVbdef4());// �Զ�����4
				armsg.setVbdef5(armsgjsonvo.getVbdef5());// �Զ�����5
				armsg.setVbdef6(armsgjsonvo.getVbdef6());// �Զ�����6
				armsg.setVbdef7(armsgjsonvo.getVbdef7());// �Զ�����7
				armsg.setVbdef8(armsgjsonvo.getVbdef8());// �Զ�����8
				armsg.setVbdef9(armsgjsonvo.getVbdef9());// �Զ�����9
				armsg.setVbdef10(armsgjsonvo.getVbdef10());// �Զ�����10
				armsgList.add(armsg);
			}
		}

		// �տ����Ϣ
		List<FctarmsgsubVO> armsubList = new ArrayList<>();
		UFDouble mny = UFDouble.ZERO_DBL;
		if (armsgsubvos != null && armsgsubvos.size() > 0) {
			for (FctarmsgsubJsonVO armsgsub : armsgsubvos) {
				FctarmsgsubVO armsgvo = new FctarmsgsubVO();
				armsgvo.setArloanmny(armsgsub.getArloanmny());// ʵ�մ������
				armsgvo.setPripaymethod(armsgsub.getPripaymethod());// һ�����ʽ
				armsgvo.setSenpaymethod(armsgsub.getSenpaymethod());// �������ʽ
				armsgvo.setVbdef1(armsgsub.getVbdef1());// �Զ�����1
				armsgvo.setVbdef2(armsgsub.getVbdef2());// �Զ�����2
				armsgvo.setVbdef3(armsgsub.getVbdef3());// �Զ�����3
				armsgvo.setVbdef4(armsgsub.getVbdef4());// �Զ�����4
				armsgvo.setVbdef5(armsgsub.getVbdef5());// �Զ�����5
				armsgvo.setVbdef6(armsgsub.getVbdef6());// �Զ�����6
				armsgvo.setVbdef7(armsgsub.getVbdef7());// �Զ�����7
				armsgvo.setVbdef8(armsgsub.getVbdef8());// �Զ�����8
				armsgvo.setVbdef9(armsgsub.getVbdef9());// �Զ�����9
				armsgvo.setVbdef10(armsgsub.getVbdef10());// �Զ�����10
				armsgvo.setPk_psndoc(armsgsub.getPk_psndoc());// ������
				armsgvo.setPurchqualifi(armsgsub.getPurchqualifi());// �����ʸ�
				armsgvo.setBuildamount(armsgsub.getBuildamount());// ¥���ܶ�
				armsgvo.setArnoloanmny(armsgsub.getArnoloanmny());// ʵ�շǴ����ܶ�
				armsgvo.setCreditdate(armsgsub.getCreditdate() == null ? null
						: new UFDate(armsgsub.getCreditdate()));// �ſ�����
				armsgvo.setPrcreditdate(armsgsub.getPrcreditdate() == null ? null
						: new UFDate(armsgsub.getPrcreditdate()));// ����ſ�����
				mny = mny.add(new UFDouble(armsgsub.getBuildamount()));
				armsubList.add(armsgvo);

			}

		}
		// ������ͬ������Ĭ��һ��
		CtArBVO[] ctarbvos = new CtArBVO[1];
		CtArPlanVO[] ctarplavvos = new CtArPlanVO[1];
		CtArBVO ctarbvo = new CtArBVO();
		ctarbvo.setCrowno("10");
		ctarbvo.setPk_group(orgvo.getPk_group());
		ctarbvo.setPk_org(orgvo.getPk_org());
		ctarbvo.setPk_org_v(orgvo.getPk_vid());
		;
		ctarbvo.setPk_financeorg(orgvo.getPk_org());
		ctarbvo.setPk_financeorg_v(orgvo.getPk_vid());
		ctarbvo.setVchangerate("1.00/1.00");
		ctarbvo.setVqtunitrate("1.00/1.00");
		ctarbvo.setNtaxmny(mny);
		ctarbvo.setNorigtaxmny(mny);
		ctarbvos[0] = ctarbvo;
		// �տ�ƻ�
		CtArPlanVO ctarplanvo = new CtArPlanVO();
		ctarplanvo.setAccountdate(0);
		ctarplanvo.setPlanrate(new UFDouble("100"));// Ĭ��100
		ctarplanvo.setEnddate(new UFDate());// �տ���־
		ctarplanvo.setPk_org(orgvo.getPk_corp());
		ctarplanvo.setPk_org_v(orgvo.getPk_vid());
		ctarplanvo.setPlanmoney(mny);// ��ͬ���
		ctarplavvos[0] = ctarplanvo;
		// ��������
		List<FctprogressVO> fctList = new ArrayList<>();
		if (progressvos != null && progressvos.size() > 0) {
			for (FctprogressJsonVO progressvo : progressvos) {
				FctprogressVO fctprogressVO = new FctprogressVO();
				fctprogressVO.setApartment(progressvo.getApartment());// ����
				fctprogressVO.setDecoratestan(progressvo.getDecoratestan());// װ�ޱ�׼
				fctprogressVO.setNote(progressvo.getNote());// �ۿ�˵��
				fctprogressVO.setRoompart(progressvo.getRoompart());// �������䳵λ
				fctprogressVO.setProprocess(progressvo.getProprocess());// ��Ȩ����
				fctprogressVO.setSubtotalprice(new UFDouble(progressvo
						.getSubtotalprice()));// ����Э���ܼ�
				fctprogressVO.setPaymny(new UFDouble(progressvo.getPaymny()));// �Ѹ�װ�޿�
				fctprogressVO
						.setAgredate(progressvo.getAgredate() == null ? null
								: new UFDateTime(progressvo.getAgredate()));// Լ����ǩ����
				fctprogressVO
						.setAdvancedate(progressvo.getAdvancedate() == null ? null
								: new UFDateTime(progressvo.getAdvancedate()));// ȡ��Ԥ��֤����
				fctprogressVO.setAdvabcecode(progressvo.getAdvabcecode());// Ԥ��֤���
				fctprogressVO
						.setPartrightime(progressvo.getPartrightime() == null ? null
								: new UFDate(progressvo.getPartrightime()));// ��λȨȷ������
				fctprogressVO
						.setNetagredate(progressvo.getNetagredate() == null ? null
								: new UFDate(progressvo.getNetagredate()));// ��ǩ����
				fctprogressVO.setFiledate(progressvo.getFiledate());// ���������ͼ�����
				fctprogressVO.setVbdef1(progressvo.getVbdef1());// �Զ�����1
				fctprogressVO.setVbdef2(progressvo.getVbdef2());// �Զ�����2
				fctprogressVO.setVbdef3(progressvo.getVbdef3());// �Զ�����3
				fctprogressVO.setVbdef4(progressvo.getVbdef4());// �Զ�����4
				fctprogressVO.setVbdef5(progressvo.getVbdef5());// �Զ�����5
				fctprogressVO.setVbdef6(progressvo.getVbdef6());// �Զ�����6
				fctprogressVO.setVbdef7(progressvo.getVbdef7());// �Զ�����7
				fctprogressVO.setVbdef8(progressvo.getVbdef8());// �Զ�����8
				fctprogressVO.setVbdef9(progressvo.getVbdef9());// �Զ�����9
				fctprogressVO.setVbdef10(progressvo.getVbdef10());// �Զ�����10
				fctList.add(fctprogressVO);
			}
		}
		aggvo.setParent(ctarvo);
		aggvo.setCtArBVO(ctarbvos);
		aggvo.setCtArPlanVO(ctarplavvos);
		aggvo.setFctarmsgsubVO(armsubList.toArray(new FctarmsgsubVO[0]));
		aggvo.setFctArmsgVO(armsgList.toArray(new FctArmsgVO[0]));
		aggvo.setFctprogressVO(fctList.toArray(new FctprogressVO[0]));
		return aggvo;

	}

	/**
	 * ͨ�����ͬ ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @since 2019-09-09
	 * @author lhh
	 * @param hMap
	 * @return
	 */
	private AggCtArVO onTranBill(HashMap<String, Object> value, String srctype)
			throws BusinessException {
		AggCtArVO ctaraggvo = new AggCtArVO();
		JSONObject jsonData = (JSONObject) value.get("data");
		// ��ͷ��Ϣ
		CtArJsonVO headvo = JSONObject.parseObject(
				jsonData.getString("ctarheadvo"), CtArJsonVO.class);
		// ��ͬ����
		List<CtArJsonBVO> ctArBvos1 = JSONObject.parseArray(
				jsonData.getString("ctarbvos"), CtArJsonBVO.class);

		// ִ�����
		List<CtArExecJsonVO> ctArExecBvos1 = JSONObject.parseArray(
				jsonData.getString("ctarexecdetail"), CtArExecJsonVO.class);
		// �����Ŀ���
		List<CtArSubspiltJsonVO> ctArSplitBvos1 = JSONObject.parseArray(
				jsonData.getString("ctararsubspilt"), CtArSubspiltJsonVO.class);

		// ����Э��
		List<CtArSubagremntJsonVO> subagremnts1 = JSONObject.parseArray(
				jsonData.getString("subagremnts"), CtArSubagremntJsonVO.class);
		// �տ�ƻ�
		List<CtArPlanJsonVO> ctsrplans1 = JSONObject.parseArray(
				jsonData.getString("ctsrplans"), CtArPlanJsonVO.class);
		OrgVO orgvo = getOrgVO(headvo.getPk_org());

		// У�����ݷǿ�
		vaildHeadData(headvo);
		vaildCtArPlanBodyData(ctsrplans1);
		vaildCtArSubspiltBodyData(ctArSplitBvos1);

		// ��ͷ��Ϣ
		Object[] defpks = super.getDeptpksByCode(headvo.getDepid_v(),
				orgvo.getPk_org());

		CtArVO ctarvo = buildheadvo(orgvo, defpks, headvo, srctype);

		// ��ͬ������Ϣ���տ�ƻ�
		CtArBVO[] ctarbvos = null;
		CtArPlanVO[] ctarplavvos = null;
		ctarbvos = new CtArBVO[1];

		if (ctArBvos1 != null && ctArBvos1.size() > 0) {
			// for(int i=0;i<ctArBvos1.size();i++){
			// //��ͬ������Ϣ
			// CtArBVO ctarbvo= new CtArBVO();
			// ctarbvo.setCrowno(i+1+"0");
			// ctarbvo.setPk_group(orgvo.getPk_group());
			// ctarbvo.setPk_org(orgvo.getPk_org());
			// ctarbvo.setPk_org_v(orgvo.getPk_vid());;
			// ctarbvo.setPk_financeorg(orgvo.getPk_org());
			// ctarbvo.setPk_financeorg_v(orgvo.getPk_vid());
			// ctarbvo.setFtaxtypeflag(ctArBvos1.get(i).getFtaxtypeflag());
			// ctarbvo.setNnosubtaxrate(new
			// UFDouble(ctArBvos1.get(i).getNnosubtaxrate()));
			// ctarbvo.setNtaxrate(new
			// UFDouble(ctArBvos1.get(i).getNtaxrate()));
			// ctarbvo.setVbdef1(ctArBvos1.get(i).getVbdef1());//��Ŀ����
			// ctarbvo.setVbdef2(ctArBvos1.get(i).getVbdef2());//��ֱ���
			// ctarbvo.setVmemo(ctArBvos1.get(i).getVmemo());//˵��
			// ctarbvo.setProject(ctArBvos1.get(i).getProject());//��Ŀ
			// ctarbvo.setInoutcome(ctArBvos1.get(i).getInoutcome());//��֧��Ŀ
			// ctarbvo.setVbdef3(ctArBvos1.get(i).getVbdef3());//�Զ�����3
			// ctarbvo.setVbdef4(ctArBvos1.get(i).getVbdef4());//�Զ�����4
			// ctarbvo.setVbdef5(ctArBvos1.get(i).getVbdef5());//�Զ�����5
			// ctarbvo.setVbdef6(ctArBvos1.get(i).getVbdef6());//�Զ�����6
			// ctarbvo.setVbdef7(ctArBvos1.get(i).getVbdef7());//�Զ�����7
			// ctarbvo.setVbdef8(ctArBvos1.get(i).getVbdef8());//�Զ�����8
			// ctarbvo.setVbdef9(ctArBvos1.get(i).getVbdef9());//�Զ�����9
			// ctarbvo.setVbdef10(ctArBvos1.get(i).getVbdef10());//�Զ�����10
			// ctarbvo.setVbdef11(ctArBvos1.get(i).getVbdef11());//�Զ�����11
			// ctarbvo.setVbdef12(ctArBvos1.get(i).getVbdef12());//�Զ�����12
			// ctarbvo.setVbdef13(ctArBvos1.get(i).getVbdef13());//�Զ�����13
			// ctarbvo.setVbdef14(ctArBvos1.get(i).getVbdef14());//�Զ�����14
			// ctarbvo.setVbdef15(ctArBvos1.get(i).getVbdef15());//�Զ�����15
			// ctarbvo.setVbdef16(ctArBvos1.get(i).getVbdef16());//�Զ�����16
			// ctarbvo.setVbdef17(ctArBvos1.get(i).getVbdef17());//�Զ�����17
			// ctarbvo.setVbdef18(ctArBvos1.get(i).getVbdef18());//�Զ�����18
			// ctarbvo.setVbdef19(ctArBvos1.get(i).getVbdef19());//�Զ�����19
			// ctarbvo.setVbdef20(ctArBvos1.get(i).getVbdef20());//�Զ�����20
			// ctarbvo.setVchangerate("1.00/1.00");
			// ctarbvo.setVqtunitrate("1.00/1.00");
			// ctarbvo.setNtaxmny(new UFDouble(headvo.getVdef11()));
			// ctarbvo.setNorigtaxmny(new UFDouble(headvo.getVdef11()));
			// ctarbvos[i]=ctarbvo;
			// }
		}
		// ִ�����
		CtArExecDetailVO[] execvos = null;
		if (ctArExecBvos1 != null && ctArExecBvos1.size() > 0) {
			execvos = new CtArExecDetailVO[ctArExecBvos1.size()];
			for (int i = 0; i < ctArExecBvos1.size(); i++) {
				CtArExecDetailVO execvo = new CtArExecDetailVO();
				execvo.setVbillcode(ctArExecBvos1.get(i).getVbillcode());
				execvo.setVbilldate(new UFDate(ctArExecBvos1.get(i)
						.getVbilldate()));
				execvo.setNorigpshamount(new UFDouble(ctArExecBvos1.get(i)
						.getNorigpshamount()));
				execvo.setCtrunarmny(new UFDouble(ctArExecBvos1.get(i)
						.getCtrunarmny()));
				execvo.setNorigcopamount(new UFDouble(ctArExecBvos1.get(i)
						.getNorigcopamount()));
				execvo.setVbdef1(ctArExecBvos1.get(i).getVbdef1());
				execvo.setVbdef2(ctArExecBvos1.get(i).getVbdef2());//���б���vdef2��Ϊ��EBS����
				execvo.setVbdef3(ctArExecBvos1.get(i).getVbdef3());
				execvo.setVbdef4(ctArExecBvos1.get(i).getVbdef4());
				execvo.setVbdef5(ctArExecBvos1.get(i).getVbdef5());
				execvo.setVbdef6(ctArExecBvos1.get(i).getVbdef6());
				execvo.setVbdef7(ctArExecBvos1.get(i).getVbdef7());
				execvo.setVbdef8(ctArExecBvos1.get(i).getVbdef8());
				execvo.setVbdef9(ctArExecBvos1.get(i).getVbdef9());
				execvo.setVbdef10(ctArExecBvos1.get(i).getVbdef10());
				execvos[i] = execvo;
			}
		}
		// �����Ŀ���
		CtArArsubspiltVO[] subspiltvos = null;
		if (ctArSplitBvos1 != null && ctArSplitBvos1.size() > 0) {
			subspiltvos = new CtArArsubspiltVO[ctArSplitBvos1.size()];
			for (int i = 0; i < ctArSplitBvos1.size(); i++) {
				CtArArsubspiltVO subspiltvo = new CtArArsubspiltVO();
				// subspiltvo.setSubcode(ctArSplitBvos1.get(i).getSubcode());
				subspiltvo.setSpitrate(ctArSplitBvos1.get(i).getSpitrate());
				subspiltvo.setNote(ctArSplitBvos1.get(i).getNote());
				subspiltvo.setVbdef1(ctArSplitBvos1.get(i).getVbdef1());
				subspiltvo.setVbdef2(ctArSplitBvos1.get(i).getSubcode());
				subspiltvo.setVbdef3(ctArSplitBvos1.get(i).getVbdef3());
				subspiltvo.setVbdef4(ctArSplitBvos1.get(i).getVbdef4());
				subspiltvo.setVbdef5(ctArSplitBvos1.get(i).getVbdef5());
				subspiltvo.setVbdef6(ctArSplitBvos1.get(i).getVbdef6());
				subspiltvo.setVbdef7(ctArSplitBvos1.get(i).getVbdef7());
				subspiltvo.setVbdef8(ctArSplitBvos1.get(i).getVbdef8());
				subspiltvo.setVbdef9(ctArSplitBvos1.get(i).getVbdef9());
				subspiltvo.setVbdef10(ctArSplitBvos1.get(i).getVbdef10());
				subspiltvos[i] = subspiltvo;
			}
		}
		// �տ�ƻ�
		UFDouble total = new UFDouble();
		if (ctsrplans1 != null && ctsrplans1.size() > 0) {
			ctarplavvos = new CtArPlanVO[ctsrplans1.size()];
			for (int i = 0; i < ctsrplans1.size(); i++) {
				UFDouble planmoney = new UFDouble(ctsrplans1.get(i)
						.getPlanmoney());
				CtArPlanVO ctarplanvo = new CtArPlanVO();
				ctarplanvo.setAccountdate(0);
				UFDouble rate = new UFDouble(ctsrplans1.get(i).getPlanrate());
				ctarplanvo.setPlanrate(rate.setScale(2,
						UFDouble.ROUND_HALF_DOWN));// ��ֱ���
				ctarplanvo.setEnddate(new UFDate());// �տ���־
				ctarplanvo.setPk_org(orgvo.getPk_corp());
				ctarplanvo.setPk_org_v(orgvo.getPk_vid());
				ctarplanvo.setPlanmoney(planmoney);
				ctarplanvo.setVbdef1(ctsrplans1.get(i).getVbdef1());//�տ�����
				ctarplanvo.setVbdef2(ctsrplans1.get(i).getVbdef2());//EBS����
				ctarplanvo.setVbdef3(ctsrplans1.get(i).getVbdef3());
				ctarplanvo.setVbdef4(ctsrplans1.get(i).getVbdef4());
				ctarplanvo.setVbdef5(ctsrplans1.get(i).getVbdef5());
				ctarplanvo.setVbdef6(ctsrplans1.get(i).getVbdef6());
				ctarplanvo.setVbdef7(ctsrplans1.get(i).getVbdef7());
				ctarplanvo.setVbdef8(ctsrplans1.get(i).getVbdef8());
				ctarplanvo.setVbdef9(ctsrplans1.get(i).getVbdef9());
				ctarplanvo.setVbdef10(ctsrplans1.get(i).getVbdef10());
				ctarplavvos[i] = ctarplanvo;

				total = total.add(planmoney);
			}
			ctarvo.setNtotalorigmny(total);// ԭ�Ҽ�˰�ϼ�
		}

		CtArBVO ctarbvo = new CtArBVO();

		ctarbvo.setCrowno("10");
		ctarbvo.setFtaxtypeflag(1);// ��˰���
		ctarbvo.setNorigtaxmny(new UFDouble());
		ctarbvo.setPk_org(orgvo.getPk_org());
		ctarbvo.setPk_org_v(orgvo.getPk_vid());
		ctarbvo.setPk_financeorg(orgvo.getPk_org());
		ctarbvo.setPk_financeorg_v(orgvo.getPk_vid());
		ctarbvo.setNorigtaxmny(total);
		ctarbvo.setNtaxmny(total);
		ctarbvos[0] = ctarbvo;

		// ����Э��
		nc.vo.fct.entity.CtArSubagremntVO[] subagremnts = null;
		if (subagremnts1 != null && subagremnts1.size() > 0) {
			subagremnts = new nc.vo.fct.entity.CtArSubagremntVO[subagremnts1
					.size()];
			for (int i = 0; i < subagremnts1.size(); i++) {
				CtArSubagremntVO subagremnt = new CtArSubagremntVO();
				subagremnt.setCode(subagremnts1.get(i).getCode());// Э�����
				subagremnt.setName(subagremnts1.get(i).getName());// Э������
				subagremnt.setSubagremntdate(subagremnts1.get(i)
						.getSubagremntdate() == null ? null : new UFDate(
						subagremnts1.get(i).getSubagremntdate()));// Э��ʱ��
				subagremnt.setMny(new UFDouble(subagremnts1.get(i).getMny()));// Э����
				subagremnt.setVbdef1(subagremnts1.get(i).getVbdef1());// �Զ���1
				subagremnt.setVbdef2(subagremnts1.get(i).getVbdef2());// �Զ���2
				subagremnt.setVbdef3(subagremnts1.get(i).getVbdef3());// �Զ���3
				subagremnt.setVbdef4(subagremnts1.get(i).getVbdef4());// �Զ���4
				subagremnt.setVbdef5(subagremnts1.get(i).getVbdef5());// �Զ���5
				subagremnt.setVbdef6(subagremnts1.get(i).getVbdef6());// �Զ���6
				subagremnt.setVbdef7(subagremnts1.get(i).getVbdef7());// �Զ���7
				subagremnt.setVbdef8(subagremnts1.get(i).getVbdef8());// �Զ���8
				subagremnt.setVbdef9(subagremnts1.get(i).getVbdef9());// �Զ���9
				subagremnt.setVbdef10(subagremnts1.get(i).getVbdef10());// �Զ���10
				subagremnts[i] = subagremnt;
			}
		}
		CtArChangeVO changevo = new CtArChangeVO();
		changevo.setPk_org(orgvo.getPk_org());
		changevo.setPk_org_v(orgvo.getPk_vid());
		changevo.setPk_group(orgvo.getPk_group());
		changevo.setVmemo("ԭʼ�汾");
		changevo.setVchangecode(new UFDouble(1));

		ctaraggvo.setParentVO(ctarvo);
		ctaraggvo.setCtArBVO(ctarbvos);
		// ctaraggvo.setCtArBVO(new CtArBVO[]{});
		ctaraggvo.setCtArPlanVO(ctarplavvos);// �տ�ƻ�
		ctaraggvo.setCtArSubagremntVO(subagremnts);// ����Э��
		ctaraggvo.setCtArChangeVO(new CtArChangeVO[] { changevo });
		ctaraggvo.setCtArExecDetailVO(execvos);// ִ�����
		ctaraggvo.setCtArMemoraVO(new CtArMemoraVO[] {});
		ctaraggvo.setCtArTermVO(new CtArTermVO[] {});
		ctaraggvo.setCtArExecVO(new CtArExecVO[] {});
		ctaraggvo.setCtArArsubspiltVO(subspiltvos);// �����Ŀ���
		return ctaraggvo;
	}

	/**
	 * ������֯�����ѯ��֯VO
	 * 
	 * @param orgCode
	 * @return
	 * @throws BusinessException
	 */
	private OrgVO getOrgVO(String orgCode) throws BusinessException {
		OrgVO orgvo = (OrgVO) super.getBaseDAO().executeQuery(
				"select * from org_orgs where code ='" + orgCode + "'",
				new BeanProcessor(OrgVO.class));
		return orgvo;
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
	private void vaildHeadData(CtArJsonVO headvo) throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (StringUtil.isEmpty(headvo.getCbilltypecode())) {
			str.append("�����쳣����ͷ�������Ͳ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVtrantypecode())) {
			str.append("�����쳣����ͷ��ͬ���Ͳ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getDbilldate())) {
			str.append("�����쳣����ͷ�������ڲ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getCorigcurrencyid())) {
			str.append("�����쳣����ͷ���ֲ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getCtname())) {
			str.append("�����쳣����ͷ��ͬ���Ʋ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef22())) {
			str.append("�����쳣����ͷ�����˲���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef23())) {
			str.append("�����쳣����ͷ���Ų���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVbillcode())) {
			str.append("�����쳣����ͷ��ͬ���벻��Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef1())) {
			str.append("�����쳣����ͷ��鲻��Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef2())) {
			str.append("�����쳣����ͷ�տ˾����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef3())) {
			str.append("�����쳣����ͷ�׷�����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef4())) {
			str.append("�����쳣����ͷ�ҷ�����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef9())) {
			str.append("�����쳣����ͷ��ͬ״̬����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef10())) {
			str.append("�����쳣����ͷǩԼ����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef12())) {
			str.append("�����쳣����ͷ˰�ʲ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef13())) {
			str.append("�����쳣����ͷ���й�˾����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef14())) {
			str.append("�����쳣����ͷ��ͬ�����˲���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef16())) {
			str.append("�����쳣����ͷ����ͨ�����ڲ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef18())) {
			str.append("�����쳣����ͷESB����id����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef19())) {
			str.append("�����쳣����ͷESB���ݺŲ���Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getPk_org())) {
			str.append("�����쳣����ͷ������֯����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getPk_customer())) {
			str.append("�����쳣����ͷ�ͻ�����Ϊ�գ�");
		}
		if (StringUtil.isEmpty(headvo.getVdef21())) {
			str.append("�����쳣����ͷEBS�������Ͳ���Ϊ�գ�");
		}
		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 
	 * @param ctArBvos1
	 *            �տ�ƻ�
	 * @throws BusinessException
	 */
	private void vaildCtArPlanBodyData(List<CtArPlanJsonVO> ctsrplans1)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < ctsrplans1.size(); i++) {
			if (StringUtil.isEmpty(String.valueOf(ctsrplans1.get(i)
					.getPlanmoney()))) {
				str.append("�����쳣���տ�ƻ�ҳǩ����" + i + 1 + "�У��տ����Ϊ�գ�");
			}
			if (StringUtil.isEmpty(String.valueOf(ctsrplans1.get(i)
					.getPlanrate()))) {
				str.append("�����쳣���տ�ƻ�ҳǩ����" + i + 1 + "�У��տ���ʲ���Ϊ�գ�");
			}
			if (StringUtil.isEmpty(String.valueOf(ctsrplans1.get(i)
					.getEnddate()))) {
				str.append("�����쳣���տ�ƻ�ҳǩ����" + i + 1 + "�У��տ����ڲ���Ϊ�գ�");
			}
		}
		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	private void vaildCtArSubspiltBodyData(
			List<CtArSubspiltJsonVO> ctArSplitBvos1) throws BusinessException {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < ctArSplitBvos1.size(); i++) {
			if (StringUtil.isEmpty(String.valueOf(ctArSplitBvos1.get(i)
					.getSubcode()))) {
				str.append("�����쳣�������Ŀ���ҳǩ����" + i + 1 + "�У���Ŀ���Ʋ���Ϊ�գ�");
			}
			if (StringUtil.isEmpty(String.valueOf(ctArSplitBvos1.get(i)
					.getSpitrate()))) {
				str.append("�����쳣�������Ŀ���ҳǩ����" + i + 1 + "�У���ֱ��ʲ���Ϊ�գ�");
			}
			if (StringUtil.isEmpty(String.valueOf(ctArSplitBvos1.get(i)
					.getVbdef1()))) {
				str.append("�����쳣�������Ŀ���ҳǩ����" + i + 1 + "�У�����Ϊ�գ�");
			}
		}
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
	private CtArVO buildheadvo(OrgVO orgvo, Object[] defpks, CtArJsonVO headvo,
			String srctype) throws BusinessException {
		CtArVO ctarvo = new CtArVO();
		ctarvo.setBankaccount(headvo.getBankaccount());
		ctarvo.setPk_org(orgvo.getPk_org());
		ctarvo.setPk_org_v(orgvo.getPk_vid());// ��֯�汾
		ctarvo.setCbilltypecode(headvo.getCbilltypecode());
		ctarvo.setVtrantypecode(headvo.getVtrantypecode());
		ctarvo.setDbilldate(new UFDate(headvo.getDbilldate()));
		ctarvo.setCcurrencyid("1002Z0100000000001K1");
		// ctarvo.setCcurrencyid(headvo.getCcurrencyid());
		ctarvo.setCorigcurrencyid("1002Z0100000000001K1");// ����
		ctarvo.setCrececountryid("0001Z010000000079UJJ");// �ջ����ҵ���
		ctarvo.setCsendcountryid("0001Z010000000079UJJ");// �������ҵ���
		ctarvo.setCtaxcountryid("0001Z010000000079UJJ");// ��˰���ҵ���
		ctarvo.setVbillcode(headvo.getVbillcode());// ��ͬ����
		ctarvo.setCtrantypeid(super.getBillTypePkByCode(
				headvo.getVtrantypecode(), orgvo.getPk_group()));// ��������
		ctarvo.setPk_customer(super.getcustomer(headvo.getPk_customer()));// �ͻ�
		ctarvo.setCtname(headvo.getCtname());
		ctarvo.setFbuysellflag(1);// ��������
		ctarvo.setPersonnelid(super.getPsndocPkByCode(headvo.getPersonnelid()));// ������
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
		ctarvo.setVdef1(headvo.getVdef1());// ���
		OrgVO org = getOrgVO(headvo.getVdef2());
		OrgVO org1 = getOrgVO(headvo.getVdef3());
		String vdef2 = null;
		String vdef3 = null;
		if (org != null) {
			vdef2 = org.getPk_org();
		}
		if (org != null) {
			vdef3 = org1.getPk_org();
		}
		ctarvo.setVdef2(vdef2);// �տ˾
		ctarvo.setVdef3(vdef3);// �׷�
		ctarvo.setVdef4(getCustVO(headvo.getVdef4()));// �ҷ�
		ctarvo.setVdef5(getCustVO(headvo.getVdef5()));// ����
		ctarvo.setVdef6(getCustVO(headvo.getVdef6()));// ����
		ctarvo.setVdef7(getCustVO(headvo.getVdef7()));// �緽
		ctarvo.setVdef8(getCustVO(headvo.getVdef8()));// ����
		ctarvo.setVdef9(headvo.getVdef9());// ��ͬ״̬
		ctarvo.setVdef10(headvo.getVdef10());// ǩԼ���
		ctarvo.setVdef11(headvo.getVdef11());// ��̬���
		ctarvo.setVdef12(headvo.getVdef12());// ˰��
		ctarvo.setVdef13(headvo.getVdef13());// �Ƿ��˰��
		ctarvo.setVdef14(headvo.getVdef14());// ��ͬ����Ա
		ctarvo.setVdef15(headvo.getVdef15());// �ۼƲ���Э����
		ctarvo.setVdef16(headvo.getVdef16());// ����ͨ������
		ctarvo.setVdef17(headvo.getVdef17());// �����
		ctarvo.setVdef18(headvo.getVdef18());// esb����id
		ctarvo.setVdef19(headvo.getVdef19());// esb���ݺ�
		ctarvo.setVdef20(headvo.getVdef20());// ժҪ
		ctarvo.setVdef21(headvo.getVdef21());// �Զ�����21
		ctarvo.setVdef22(headvo.getVdef22());// �Զ�����22(������)
		ctarvo.setVdef23(headvo.getVdef23());// �Զ�����23(���첿��)
		ctarvo.setVdef24(headvo.getVdef24());// �Զ�����24����ͬ���ͣ�
		ctarvo.setVdef25(headvo.getVdef25());// �Զ�����25
		ctarvo.setVdef26(headvo.getVdef26());// �Զ�����26
		ctarvo.setVdef27(headvo.getVdef27());// �Զ�����27
		ctarvo.setVdef28(headvo.getVdef28());// �Զ�����28
		ctarvo.setVdef29(headvo.getVdef29());// �Զ�����29
		ctarvo.setVdef30(headvo.getVdef30());// �Զ�����30
		ctarvo.setVdef31(headvo.getVdef31());// ��Ʒ����
		ctarvo.setVdef32(headvo.getVdef32());// ��Ŀ����
		ctarvo.setVdef33(headvo.getVdef33());// ����Ŀ����
		ctarvo.setVdef34(headvo.getVdef34());// ¥������
		ctarvo.setVdef35(headvo.getVdef35());// ��Ԫ��
		ctarvo.setVdef36(headvo.getVdef36());// �Ϲ�����
		ctarvo.setVdef37(headvo.getVdef37());// �ѷ����֪ͨ������
		ctarvo.setVdef38(headvo.getVdef38());// �ѷ�װ���깤֪ͨ������
		ctarvo.setVdef39(headvo.getVdef39());// �ѷ�����¥֪ͨ������
		ctarvo.setVdef40(headvo.getVdef40());// Լ����¥����
		ctarvo.setVdef41(headvo.getVdef41());// ʵ����¥����
		ctarvo.setVdef42(headvo.getVdef42());// �ͱ�������
		ctarvo.setVdef43(headvo.getVdef43());// ��ͬ������
		ctarvo.setVdef44(headvo.getVdef44());// ��ͬ��������
		ctarvo.setVdef45(headvo.getVdef45());// ��ͬ�Ǽ����
		ctarvo.setVdef46(headvo.getVdef46());// ��ͬ�Ǽǰ�������
		ctarvo.setVdef47(headvo.getVdef47());// ������ʱ��
		ctarvo.setVdef48(headvo.getVdef48());// ������
		ctarvo.setVdef49(headvo.getVdef49());// �����̱�ע
		ctarvo.setVdef50(headvo.getVdef50());// �������
		ctarvo.setVdef51(headvo.getVdef51());// �������
		ctarvo.setVdef52(headvo.getVdef52());// ʵ�⽨�����
		ctarvo.setVdef53(headvo.getVdef53());// ʵ���������
		return ctarvo;

	}

	/**
	 * �����±���������Ϣ
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
	private void syncBvoPkByEbsPk(Class<? extends ISuperVO> clazz,
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
				tmpDesBVO.setAttributeValue("pk_group", InvocationInfoProxy.getInstance().getGroupId());

			}
			if (list != null && list.size() > 0) {
				setVODelStatus(clazz, parentPKValue, desAggVO, bvoPKfiled, table, syncDesPkBVOs, list);
			}

		}
		if(syncDesPkBVOs == null && !info.isEmpty()){
			setVODelStatus(clazz, parentPKValue, desAggVO, bvoPKfiled, table, syncDesPkBVOs, list);
		}
	}
	/**
	 * �������ɾ����־
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
			String table,ISuperVO[] syncDesPkBVOs,List<String> list) throws BusinessException{
		String condition = " pk_fct_ar='" + parentPKValue
				+ "' and dr = 0   ";
		// ɾ��ԭ��������
		String sqlwhere = "";
		for (String value : list) {
			sqlwhere += "'" + value + "',";
		}
		sqlwhere = sqlwhere.substring(0, sqlwhere.length() - 1);
		Collection<ISuperVO> coll = getBaseDAO().retrieveByClause(
				clazz, condition);
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
			desAggVO.setChildren(clazz,
					bodyList.toArray(new SuperVO[0]));
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
			String bvoPKfiled, String table, String parentPkValue) throws BusinessException {
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
}
