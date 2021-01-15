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
		// 表头信息
		LLCtArJsonVO headvo = JSONObject.parseObject(
				jsonData.getString("headInfo"), LLCtArJsonVO.class);
		// 合同基本
		List<LLCtArJsonBVO> ctArBvos = JSONObject.parseArray(
				jsonData.getString("ctarbvos"), LLCtArJsonBVO.class);

		// 执行情况
		List<LLCtArExecJsonVO> ctArExecBvos = JSONObject.parseArray(
				jsonData.getString("ctarexecdetail"), LLCtArExecJsonVO.class);
		// 收款计划
		List<LLCtArPlanJsonVO> ctArPlanBvos = JSONObject.parseArray(
				jsonData.getString("ctarplans"), LLCtArPlanJsonVO.class);
		// 编制情况
		List<LLOrganizationJsonVO> organizationBVOs = JSONObject.parseArray(
				jsonData.getString("organization"), LLOrganizationJsonVO.class);
		// 绩效计提口径
		List<LLArPerformanceJsonBVO> performanceBVOs = JSONObject
				.parseArray(jsonData.getString("performance"),
						LLArPerformanceJsonBVO.class);
		// 增值服务
		List<LLArAddedServicesJsonBVO> addedservicesBVOs = JSONObject
				.parseArray(jsonData.getString("addedservices"),
						LLArAddedServicesJsonBVO.class);

		OrgVO orgvo = getOrgVO(headvo.getPk_org());

		String srcid = headvo.getSrcid();// 外系统业务单据ID

		Object[] defpks = super.getDeptpksByCode(headvo.getDepid(),
				orgvo.getPk_org());

		CtArVO ctarvo = buildHeadVo(orgvo, defpks, headvo);

		// 合同基本信息
		CtArBVO[] ctarbvos = null;
		ctarbvos = new CtArBVO[ctArBvos.size()];

		if (ctArBvos != null && ctArBvos.size() > 0) {
			for (int i = 0; i < ctArBvos.size(); i++) {
				// 合同基本信息
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
				ctarbvo.setVbdef1(ctArBvos.get(i).getVbdef1());// 科目名称
				ctarbvo.setVbdef2(ctArBvos.get(i).getVbdef2());// 拆分比例
				ctarbvo.setVmemo(ctArBvos.get(i).getVmemo());// 说明
				ctarbvo.setProject(ctArBvos.get(i).getProject());// 项目
				ctarbvo.setInoutcome(ctArBvos.get(i).getInoutcome());// 收支项目
				ctarbvo.setVbdef3(ctArBvos.get(i).getVbdef3());// 自定义项3
				ctarbvo.setVbdef4(ctArBvos.get(i).getVbdef4());// 自定义项4
				ctarbvo.setVbdef5(ctArBvos.get(i).getVbdef5());// 自定义项5
				ctarbvo.setVbdef6(ctArBvos.get(i).getVbdef6());// 自定义项6
				ctarbvo.setVbdef7(ctArBvos.get(i).getVbdef7());// 自定义项7
				ctarbvo.setVbdef8(ctArBvos.get(i).getVbdef8());// 自定义项8
				ctarbvo.setVbdef9(ctArBvos.get(i).getVbdef9());// 自定义项9
				ctarbvo.setVbdef10(ctArBvos.get(i).getVbdef10());// 自定义项10
				ctarbvo.setVbdef11(ctArBvos.get(i).getVbdef11());// 自定义项11
				ctarbvo.setVbdef12(ctArBvos.get(i).getVbdef12());// 自定义项12
				ctarbvo.setVbdef13(ctArBvos.get(i).getVbdef13());// 自定义项13
				ctarbvo.setVbdef14(ctArBvos.get(i).getVbdef14());// 自定义项14
				ctarbvo.setVbdef15(ctArBvos.get(i).getVbdef15());// 自定义项15
				ctarbvo.setVbdef16(ctArBvos.get(i).getVbdef16());// 自定义项16
				ctarbvo.setVbdef17(ctArBvos.get(i).getVbdef17());// 自定义项17
				ctarbvo.setVbdef18(ctArBvos.get(i).getVbdef18());// 自定义项18
				ctarbvo.setVbdef19(ctArBvos.get(i).getVbdef19());// 自定义项19
				ctarbvo.setVbdef20(ctArBvos.get(i).getVbdef20());// 自定义项20
				ctarbvo.setVchangerate("1.00/1.00");
				ctarbvo.setVqtunitrate("1.00/1.00");
				ctarbvo.setNtaxmny(new UFDouble(ctArBvos.get(i)
						.getNorigtaxmny()));
				ctarbvo.setNorigtaxmny(new UFDouble(ctArBvos.get(i)
						.getNorigtaxmny()));
				// 2020-09-28-谈子健-添加字段-start
				ctarbvo.setVbdef29(getdefdocBycode(ctArBvos.get(i)
						.getCollectionitemstype(), "SDLL008"));// 收费项目类型

				ctarbvo.setVbdef30(getItemnameByPk(ctArBvos.get(i)
						.getCollectionitemsname(), "SDLL008"));// 收费项目名称

				ctarbvo.setNtax(new UFDouble(ctArBvos.get(i).getNorigtax()));
				// 2020-09-28-谈子健-添加字段-end
				ctarbvos[i] = ctarbvo;
			}
		}
		// 执行情况
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
				execvo.setVbdef2(ctArExecBvos.get(i).getVbdef2());// 所有表体vdef2定为：EBS主键
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
				// 2020-09-28-谈子健-添加字段-start

				execvo.setAdvancemoney(ctArExecBvos.get(i).getAdvancemoney());// 预收金额
				execvo.setInvoiceamountincludingtax(ctArExecBvos.get(i)
						.getInvoiceamountincludingtax());// 累计已开发票含税金额
				execvo.setInvoiceamountexcludingtax(ctArExecBvos.get(i)
						.getInvoiceamountexcludingtax());// 累计已开发票不含税金额

				// 2020-09-28-谈子健-添加字段-end
				execvos[i] = execvo;
			}
		}
		// 收款计划
		CtArPlanVO[] ctarplavvos = null;
		if (ctArPlanBvos != null && ctArPlanBvos.size() > 0) {
			ctarplavvos = new CtArPlanVO[ctArPlanBvos.size()];
			for (int i = 0; i < ctArPlanBvos.size(); i++) {
				CtArPlanVO ctarplanvo = new CtArPlanVO();
				ctarplanvo.setAccountdate(0);
				ctarplanvo.setEnddate(new UFDate(ctArPlanBvos.get(i)
						.getEnddate()));// 收款日期
				ctarplanvo.setPk_org(orgvo.getPk_corp());
				ctarplanvo.setPk_org_v(orgvo.getPk_vid());
				ctarplanvo.setPlanmoney(new UFDouble(ctArPlanBvos.get(i)
						.getPlanmoney()));// 合同金额
				// 2020-09-28-谈子健-添加字段-start

				ctarplanvo.setPerformancedate(ctArPlanBvos.get(i)
						.getPerformancedate());// 所属年月
				ctarplanvo.setMoney(ctArPlanBvos.get(i).getMoney());// 含税金额
				ctarplanvo.setNotaxmny(ctArPlanBvos.get(i).getNotaxmny());// 不含税金额
				ctarplanvo.setApprovetime(ctArPlanBvos.get(i).getApprovetime());// 应收单审批时间
				ctarplanvo.setAmountreceivable(ctArPlanBvos.get(i)
						.getAmountreceivable());// 已转应收金额
				ctarplanvo.setPayer(ctArPlanBvos.get(i).getPayer());// 付款人
				ctarplanvo.setProjectprogress(ctArPlanBvos.get(i)
						.getProjectprogress());// 工程进度
				ctarplanvo.setIsdeposit(ctArPlanBvos.get(i).getIsdeposit());// 是否质保金
				ctarplanvo.setDepositterm(ctArPlanBvos.get(i).getDepositterm());// 质保金期限
				ctarplanvo.setChargingname(ctArPlanBvos.get(i)
						.getChargingname());// 收费标准名称
				// 2020-09-28-谈子健-添加字段-end
				ctarplavvos[i] = ctarplanvo;
			}
		}
		// 编制情况 organizationBVOs
		ArOrganizationBVO[] organizationvos = null;
		if (organizationBVOs != null && organizationBVOs.size() > 0) {
			organizationvos = new ArOrganizationBVO[organizationBVOs.size()];
			for (int i = 0; i < organizationBVOs.size(); i++) {
				ArOrganizationBVO arOrganizationBVO = new ArOrganizationBVO();
				arOrganizationBVO.setPostname(organizationBVOs.get(i)
						.getPostname());// 岗位名称
				arOrganizationBVO
						.setPeople(organizationBVOs.get(i).getPeople());// 人数
				arOrganizationBVO.setUnivalence(new UFDouble(organizationBVOs
						.get(i).getUnivalence()));// 单价
				arOrganizationBVO.setBillingperiod(organizationBVOs.get(i)
						.getBillingperiod());// 计费期限
				arOrganizationBVO.setBillingamount(new UFDouble(
						organizationBVOs.get(i).getBillingamount()));// 计费金额
				organizationvos[i] = arOrganizationBVO;
			}
		}
		// 绩效计提口径 performanceBVOs
		ArPerformanceBVO[] performancevos = null;
		if (performanceBVOs != null && performanceBVOs.size() > 0) {
			performancevos = new ArPerformanceBVO[performanceBVOs.size()];
			for (int i = 0; i < performanceBVOs.size(); i++) {
				ArPerformanceBVO arPerformanceBVO = new ArPerformanceBVO();
				arPerformanceBVO.setPerformancedate(performanceBVOs.get(i)
						.getPerformancedate());// 日期xx年xx月
				arPerformanceBVO.setMoney(new UFDouble(performanceBVOs.get(i)
						.getMoney()));// 含税金额
				arPerformanceBVO.setNotaxmny(new UFDouble(performanceBVOs
						.get(i).getNotaxmny()));// 不含税金额
				arPerformanceBVO.setApprovestatus(Integer
						.valueOf(performanceBVOs.get(i).getApprovestatus()));// 共享审核应收单状态

				performancevos[i] = arPerformanceBVO;
			}
		}

		// 增值服务
		ArAddedServicesBVO[] addedservicesvos = null;
		if (addedservicesBVOs != null && addedservicesBVOs.size() > 0) {
			addedservicesvos = new ArAddedServicesBVO[addedservicesBVOs.size()];
			for (int i = 0; i < addedservicesBVOs.size(); i++) {
				ArAddedServicesBVO arAddedServicesBVO = new ArAddedServicesBVO();
				arAddedServicesBVO.setServicecontent(addedservicesBVOs.get(i)
						.getServicecontent());// 开展增值服务内容
				arAddedServicesBVO.setChargingname(addedservicesBVOs.get(i)
						.getChargingname());// 收费标准名称
				arAddedServicesBVO.setServiceperiod(addedservicesBVOs.get(i)
						.getServiceperiod());// 服务期限
				arAddedServicesBVO.setMoney(new UFDouble(addedservicesBVOs.get(
						i).getMoney()));// 含税金额
				arAddedServicesBVO.setPaytype(addedservicesBVOs.get(i)
						.getPaytype());// 付款方式

				addedservicesvos[i] = arAddedServicesBVO;
			}
		}

		ctaraggvo.setParentVO(ctarvo);
		ctaraggvo.setCtArBVO(ctarbvos);
		ctaraggvo.setCtArPlanVO(ctarplavvos);
		ctaraggvo.setCtArExecDetailVO(execvos);// 执行情况
		ctaraggvo.setArOrganizationBVO(organizationvos);
		ctaraggvo.setArPerformanceBVO(performancevos);
		ctaraggvo.setArAddedServicesBVO(addedservicesvos);

		// 更新的合同
		if (hpk != null) {
			// 变更逻辑
			AggCtArVO oldaggvos = (AggCtArVO) getBillVO(AggCtArVO.class,
					"isnull(dr,0)=0 and blatest ='Y' and vdef18 = '" + srcid
							+ "'");
			if (oldaggvos == null) {
				throw new BusinessException("变更失败！nc系统中查不到该单据！");
			}

			// 将原始VO表头的部分数据填充到新VO
			String primaryKey = oldaggvos.getPrimaryKey();
			ctaraggvo.getParentVO().setPrimaryKey(primaryKey);
			ctaraggvo.getParentVO().setTs(oldaggvos.getParentVO().getTs()); // 同步表头ts
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
