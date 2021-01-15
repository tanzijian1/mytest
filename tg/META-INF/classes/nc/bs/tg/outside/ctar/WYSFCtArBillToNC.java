package nc.bs.tg.outside.ctar;

import java.util.HashMap;
import java.util.List;

import nc.vo.fct.ar.entity.AggCtArVO;
import nc.vo.fct.ar.entity.ArAddedServicesBVO;
import nc.vo.fct.ar.entity.ArOrganizationBVO;
import nc.vo.fct.ar.entity.ArPerformanceBVO;
import nc.vo.fct.ar.entity.CtArBVO;
import nc.vo.fct.ar.entity.CtArPlanVO;
import nc.vo.fct.ar.entity.CtArVO;
import nc.vo.fct.entity.CtArExecDetailVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.LLArAddedServicesJsonBVO;
import nc.vo.tg.outside.LLArPerformanceJsonBVO;
import nc.vo.tg.outside.LLCtArExecJsonVO;
import nc.vo.tg.outside.LLCtArJsonBVO;
import nc.vo.tg.outside.LLCtArJsonVO;
import nc.vo.tg.outside.LLCtArPlanJsonVO;
import nc.vo.tg.outside.LLOrganizationJsonVO;

import com.alibaba.fastjson.JSONObject;

public class WYSFCtArBillToNC extends CtArBillUtil {
	protected AggCtArVO onTranBill(HashMap<String, Object> info, String hpk)
			throws BusinessException {
		AggCtArVO ctaraggvo = new AggCtArVO();
		JSONObject jsonData = (JSONObject) info.get("data");
		// ��ͷ��Ϣ
		LLCtArJsonVO headvo = JSONObject.parseObject(
				jsonData.getString("headInfo"), LLCtArJsonVO.class);
		// ��ͬ����
		List<LLCtArJsonBVO> ctArBvos = JSONObject.parseArray(
				jsonData.getString("ctarbvos"), LLCtArJsonBVO.class);

		// ִ�����
		List<LLCtArExecJsonVO> ctArExecBvos = JSONObject.parseArray(
				jsonData.getString("ctarexecdetail"), LLCtArExecJsonVO.class);
		// �տ�ƻ�
		List<LLCtArPlanJsonVO> ctArPlanBvos = JSONObject.parseArray(
				jsonData.getString("ctarplans"), LLCtArPlanJsonVO.class);
		// �������
		List<LLOrganizationJsonVO> organizationBVOs = JSONObject.parseArray(
				jsonData.getString("organization"), LLOrganizationJsonVO.class);
		// ��Ч����ھ�
		List<LLArPerformanceJsonBVO> performanceBVOs = JSONObject
				.parseArray(jsonData.getString("performance"),
						LLArPerformanceJsonBVO.class);
		// ��ֵ����
		List<LLArAddedServicesJsonBVO> addedservicesBVOs = JSONObject
				.parseArray(jsonData.getString("addedservices"),
						LLArAddedServicesJsonBVO.class);

		OrgVO orgvo = getOrgVO(headvo.getPk_org());

		String srcid = headvo.getSrcid();// ��ϵͳҵ�񵥾�ID

		Object[] defpks = super.getDeptpksByCode(headvo.getDepid(),
				orgvo.getPk_org());

		CtArVO ctarvo = buildHeadVo(orgvo, defpks, headvo);

		// ��ͬ������Ϣ
		CtArBVO[] ctarbvos = null;
		ctarbvos = new CtArBVO[ctArBvos.size()];

		if (ctArBvos != null && ctArBvos.size() > 0) {
			for (int i = 0; i < ctArBvos.size(); i++) {
				// ��ͬ������Ϣ
				CtArBVO ctarbvo = new CtArBVO();
				ctarbvo.setCrowno(i + 1 + "0");
				ctarbvo.setPk_group(orgvo.getPk_group());
				ctarbvo.setPk_org(orgvo.getPk_org());
				ctarbvo.setPk_org_v(orgvo.getPk_vid());
				;
				ctarbvo.setPk_financeorg(orgvo.getPk_org());
				ctarbvo.setPk_financeorg_v(orgvo.getPk_vid());
				ctarbvo.setFtaxtypeflag(1);
				ctarbvo.setNnosubtaxrate(new UFDouble(ctArBvos.get(i)
						.getNnosubtaxrate()));
				ctarbvo.setNtaxrate(new UFDouble(ctArBvos.get(i).getNtaxrate()));
				ctarbvo.setVbdef1(ctArBvos.get(i).getVbdef1());// ��Ŀ����
				ctarbvo.setVbdef2(ctArBvos.get(i).getVbdef2());// ��ֱ���
				ctarbvo.setVmemo(ctArBvos.get(i).getVmemo());// ˵��
				ctarbvo.setProject(ctArBvos.get(i).getProject());// ��Ŀ
				ctarbvo.setInoutcome(ctArBvos.get(i).getInoutcome());// ��֧��Ŀ
				ctarbvo.setVbdef3(ctArBvos.get(i).getVbdef3());// �Զ�����3
				ctarbvo.setVbdef4(ctArBvos.get(i).getVbdef4());// �Զ�����4
				ctarbvo.setVbdef5(ctArBvos.get(i).getVbdef5());// �Զ�����5
				ctarbvo.setVbdef6(ctArBvos.get(i).getVbdef6());// �Զ�����6
				ctarbvo.setVbdef7(ctArBvos.get(i).getVbdef7());// �Զ�����7
				ctarbvo.setVbdef8(ctArBvos.get(i).getVbdef8());// �Զ�����8
				ctarbvo.setVbdef9(ctArBvos.get(i).getVbdef9());// �Զ�����9
				ctarbvo.setVbdef10(ctArBvos.get(i).getVbdef10());// �Զ�����10
				ctarbvo.setVbdef11(ctArBvos.get(i).getVbdef11());// �Զ�����11
				ctarbvo.setVbdef12(ctArBvos.get(i).getVbdef12());// �Զ�����12
				ctarbvo.setVbdef13(ctArBvos.get(i).getVbdef13());// �Զ�����13
				ctarbvo.setVbdef14(ctArBvos.get(i).getVbdef14());// �Զ�����14
				ctarbvo.setVbdef15(ctArBvos.get(i).getVbdef15());// �Զ�����15
				ctarbvo.setVbdef16(ctArBvos.get(i).getVbdef16());// �Զ�����16
				ctarbvo.setVbdef17(ctArBvos.get(i).getVbdef17());// �Զ�����17
				ctarbvo.setVbdef18(ctArBvos.get(i).getVbdef18());// �Զ�����18
				ctarbvo.setVbdef19(ctArBvos.get(i).getVbdef19());// �Զ�����19
				ctarbvo.setVbdef20(ctArBvos.get(i).getVbdef20());// �Զ�����20
				ctarbvo.setVchangerate("1.00/1.00");
				ctarbvo.setVqtunitrate("1.00/1.00");
				ctarbvo.setNtaxmny(new UFDouble(ctArBvos.get(i)
						.getNorigtaxmny()));
				ctarbvo.setNorigtaxmny(new UFDouble(ctArBvos.get(i)
						.getNorigtaxmny()));
				// 2020-09-28-̸�ӽ�-����ֶ�-start
				ctarbvo.setVbdef29(getdefdocBycode(ctArBvos.get(i)
						.getCollectionitemstype(), "SDLL008"));// �շ���Ŀ����

				ctarbvo.setVbdef30(getItemnameByPk(ctArBvos.get(i)
						.getCollectionitemsname(), "SDLL008"));// �շ���Ŀ����

				ctarbvo.setNtax(new UFDouble(ctArBvos.get(i).getNorigtax()));
				// 2020-09-28-̸�ӽ�-����ֶ�-end
				ctarbvos[i] = ctarbvo;
			}
		}
		// ִ�����
		CtArExecDetailVO[] execvos = null;
		if (ctArExecBvos != null && ctArExecBvos.size() > 0) {
			execvos = new CtArExecDetailVO[ctArExecBvos.size()];
			for (int i = 0; i < ctArExecBvos.size(); i++) {
				CtArExecDetailVO execvo = new CtArExecDetailVO();
				execvo.setVbillcode(ctArExecBvos.get(i).getVbillcode());
				execvo.setVbilldate(new UFDate(ctArExecBvos.get(i)
						.getVbilldate()));
				execvo.setNorigpshamount(new UFDouble(ctArExecBvos.get(i)
						.getNorigpshamount()));
				execvo.setCtrunarmny(new UFDouble(ctArExecBvos.get(i)
						.getCtrunarmny()));
				execvo.setNorigcopamount(new UFDouble(ctArExecBvos.get(i)
						.getNorigcopamount()));
				execvo.setVbdef1(ctArExecBvos.get(i).getVbdef1());
				execvo.setVbdef2(ctArExecBvos.get(i).getVbdef2());// ���б���vdef2��Ϊ��EBS����
				execvo.setVbdef3(ctArExecBvos.get(i).getVbdef3());
				execvo.setVbdef4(ctArExecBvos.get(i).getVbdef4());
				execvo.setVbdef5(ctArExecBvos.get(i).getVbdef5());
				execvo.setVbdef6(ctArExecBvos.get(i).getVbdef6());
				execvo.setVbdef7(ctArExecBvos.get(i).getVbdef7());
				execvo.setVbdef8(ctArExecBvos.get(i).getVbdef8());
				execvo.setVbdef9(ctArExecBvos.get(i).getVbdef9());
				execvo.setVbdef10(ctArExecBvos.get(i).getVbdef10());
				execvo.setTs(new UFDateTime());
				execvo.setDr(0);
				// 2020-09-28-̸�ӽ�-����ֶ�-start

				execvo.setAdvancemoney(ctArExecBvos.get(i).getAdvancemoney());// Ԥ�ս��
				execvo.setInvoiceamountincludingtax(ctArExecBvos.get(i)
						.getInvoiceamountincludingtax());// �ۼ��ѿ���Ʊ��˰���
				execvo.setInvoiceamountexcludingtax(ctArExecBvos.get(i)
						.getInvoiceamountexcludingtax());// �ۼ��ѿ���Ʊ����˰���

				// 2020-09-28-̸�ӽ�-����ֶ�-end
				execvos[i] = execvo;
			}
		}
		// �տ�ƻ�
		CtArPlanVO[] ctarplavvos = null;
		if (ctArPlanBvos != null && ctArPlanBvos.size() > 0) {
			ctarplavvos = new CtArPlanVO[ctArPlanBvos.size()];
			for (int i = 0; i < ctArPlanBvos.size(); i++) {
				CtArPlanVO ctarplanvo = new CtArPlanVO();
				ctarplanvo.setAccountdate(0);
				ctarplanvo.setEnddate(new UFDate(ctArPlanBvos.get(i)
						.getEnddate()));// �տ�����
				ctarplanvo.setPk_org(orgvo.getPk_corp());
				ctarplanvo.setPk_org_v(orgvo.getPk_vid());
				ctarplanvo.setPlanmoney(new UFDouble(ctArPlanBvos.get(i)
						.getPlanmoney()));// ��ͬ���
				// 2020-09-28-̸�ӽ�-����ֶ�-start

				ctarplanvo.setPerformancedate(ctArPlanBvos.get(i)
						.getPerformancedate());// ��������
				ctarplanvo.setMoney(ctArPlanBvos.get(i).getMoney());// ��˰���
				ctarplanvo.setNotaxmny(ctArPlanBvos.get(i).getNotaxmny());// ����˰���
				ctarplanvo.setApprovetime(ctArPlanBvos.get(i).getApprovetime());// Ӧ�յ�����ʱ��
				ctarplanvo.setAmountreceivable(ctArPlanBvos.get(i)
						.getAmountreceivable());// ��תӦ�ս��
				ctarplanvo.setPayer(ctArPlanBvos.get(i).getPayer());// ������
				ctarplanvo.setProjectprogress(ctArPlanBvos.get(i)
						.getProjectprogress());// ���̽���
				ctarplanvo.setIsdeposit(ctArPlanBvos.get(i).getIsdeposit());// �Ƿ��ʱ���
				ctarplanvo.setDepositterm(ctArPlanBvos.get(i).getDepositterm());// �ʱ�������
				ctarplanvo.setChargingname(ctArPlanBvos.get(i)
						.getChargingname());// �շѱ�׼����
				// 2020-09-28-̸�ӽ�-����ֶ�-end
				ctarplavvos[i] = ctarplanvo;
			}
		}
		// ������� organizationBVOs
		ArOrganizationBVO[] organizationvos = null;
		if (organizationBVOs != null && organizationBVOs.size() > 0) {
			organizationvos = new ArOrganizationBVO[organizationBVOs.size()];
			for (int i = 0; i < organizationBVOs.size(); i++) {
				ArOrganizationBVO arOrganizationBVO = new ArOrganizationBVO();
				arOrganizationBVO.setPostname(organizationBVOs.get(i)
						.getPostname());// ��λ����
				arOrganizationBVO
						.setPeople(organizationBVOs.get(i).getPeople());// ����
				arOrganizationBVO.setUnivalence(new UFDouble(organizationBVOs
						.get(i).getUnivalence()));// ����
				arOrganizationBVO.setBillingperiod(organizationBVOs.get(i)
						.getBillingperiod());// �Ʒ�����
				arOrganizationBVO.setBillingamount(new UFDouble(
						organizationBVOs.get(i).getBillingamount()));// �Ʒѽ��
				organizationvos[i] = arOrganizationBVO;
			}
		}
		// ��Ч����ھ� performanceBVOs
		ArPerformanceBVO[] performancevos = null;
		if (performanceBVOs != null && performanceBVOs.size() > 0) {
			performancevos = new ArPerformanceBVO[performanceBVOs.size()];
			for (int i = 0; i < performanceBVOs.size(); i++) {
				ArPerformanceBVO arPerformanceBVO = new ArPerformanceBVO();
				arPerformanceBVO.setPerformancedate(performanceBVOs.get(i)
						.getPerformancedate());// ����xx��xx��
				arPerformanceBVO.setMoney(new UFDouble(performanceBVOs.get(i)
						.getMoney()));// ��˰���
				arPerformanceBVO.setNotaxmny(new UFDouble(performanceBVOs
						.get(i).getNotaxmny()));// ����˰���
				arPerformanceBVO.setApprovestatus(Integer
						.valueOf(performanceBVOs.get(i).getApprovestatus()));// �������Ӧ�յ�״̬

				performancevos[i] = arPerformanceBVO;
			}
		}

		// ��ֵ����
		ArAddedServicesBVO[] addedservicesvos = null;
		if (addedservicesBVOs != null && addedservicesBVOs.size() > 0) {
			addedservicesvos = new ArAddedServicesBVO[addedservicesBVOs.size()];
			for (int i = 0; i < addedservicesBVOs.size(); i++) {
				ArAddedServicesBVO arAddedServicesBVO = new ArAddedServicesBVO();
				arAddedServicesBVO.setServicecontent(addedservicesBVOs.get(i)
						.getServicecontent());// ��չ��ֵ��������
				arAddedServicesBVO.setChargingname(addedservicesBVOs.get(i)
						.getChargingname());// �շѱ�׼����
				arAddedServicesBVO.setServiceperiod(addedservicesBVOs.get(i)
						.getServiceperiod());// ��������
				arAddedServicesBVO.setMoney(new UFDouble(addedservicesBVOs.get(
						i).getMoney()));// ��˰���
				arAddedServicesBVO.setPaytype(addedservicesBVOs.get(i)
						.getPaytype());// ���ʽ

				addedservicesvos[i] = arAddedServicesBVO;
			}
		}

		ctaraggvo.setParentVO(ctarvo);
		ctaraggvo.setCtArBVO(ctarbvos);
		ctaraggvo.setCtArPlanVO(ctarplavvos);
		ctaraggvo.setCtArExecDetailVO(execvos);// ִ�����
		ctaraggvo.setArOrganizationBVO(organizationvos);
		ctaraggvo.setArPerformanceBVO(performancevos);
		ctaraggvo.setArAddedServicesBVO(addedservicesvos);

		// ���µĺ�ͬ
		if (hpk != null) {
			// ����߼�
			AggCtArVO oldaggvos = (AggCtArVO) getBillVO(AggCtArVO.class,
					"isnull(dr,0)=0 and blatest ='Y' and vdef18 = '" + srcid
							+ "'");
			if (oldaggvos == null) {
				throw new BusinessException("���ʧ�ܣ�ncϵͳ�в鲻���õ��ݣ�");
			}

			// ��ԭʼVO��ͷ�Ĳ���������䵽��VO
			String primaryKey = oldaggvos.getPrimaryKey();
			ctaraggvo.getParentVO().setPrimaryKey(primaryKey);
			ctaraggvo.getParentVO().setTs(oldaggvos.getParentVO().getTs()); // ͬ����ͷts
			ctaraggvo.getParentVO().setStatus(VOStatus.UPDATED);
			ctaraggvo.getParentVO().setBillmaker(
					oldaggvos.getParentVO().getBillmaker());
			ctaraggvo.getParentVO().setActualinvalidate(
					oldaggvos.getParentVO().getActualinvalidate());
			ctaraggvo.getParentVO().setApprover(
					oldaggvos.getParentVO().getApprover());
			ctaraggvo.getParentVO().setCreator(
					oldaggvos.getParentVO().getCreator());
			ctaraggvo.getParentVO().setCreationtime(
					oldaggvos.getParentVO().getCreationtime());
			ctaraggvo.getParentVO().setDmakedate(
					oldaggvos.getParentVO().getDmakedate());
			ctaraggvo.getParentVO().setTaudittime(
					oldaggvos.getParentVO().getTaudittime());

			syncBvoPkByEbsPk(CtArBVO.class, primaryKey, ctaraggvo,
					CtArBVO.PK_FCT_AR_B, CtArBVO.getDefaultTableName());

			syncBvoPkByEbsPk(CtArExecDetailVO.class, primaryKey, ctaraggvo,
					CtArExecDetailVO.PK_EXEC,
					CtArExecDetailVO.getDefaultTableName());

			syncBvoPkByEbsPk(CtArPlanVO.class, primaryKey, ctaraggvo,
					CtArPlanVO.PK_FCT_AR_PLAN, "fct_ar_plan");

			syncBvoPkByEbsPk(ArOrganizationBVO.class, primaryKey, ctaraggvo,
					ArOrganizationBVO.PK_ORGANIZATION,
					ArOrganizationBVO.getDefaultTableName());

			syncBvoPkByEbsPk(ArPerformanceBVO.class, primaryKey, ctaraggvo,
					ArPerformanceBVO.PK_PERFORMANCE,
					ArPerformanceBVO.getDefaultTableName());

			syncBvoPkByEbsPk(ArAddedServicesBVO.class, primaryKey, ctaraggvo,
					ArAddedServicesBVO.PK_ADDEDSERVICES,
					ArAddedServicesBVO.getDefaultTableName());

		}

		return ctaraggvo;
	}
}
