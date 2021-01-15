package nc.bs.tg.outside.ctap;

import java.util.HashMap;
import java.util.List;

import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApAccrualPlanBVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApDeductionRecordBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.CtApServiceInfoBVO;
import nc.vo.fct.ap.entity.CtApVO;
import nc.vo.fct.ap.entity.FctMoretax;
import nc.vo.fct.ap.entity.LeaseconBVO;
import nc.vo.fct.ap.entity.SupplyAgreementBVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.LLCtApAccrualPlanJsonVO;
import nc.vo.tg.outside.LLCtApDeductionRecordBVO;
import nc.vo.tg.outside.LLCtApJsonBVO;
import nc.vo.tg.outside.LLCtApJsonVO;
import nc.vo.tg.outside.LLCtApLeaseconJsonVO;
import nc.vo.tg.outside.LLCtApMoretaxJsonVO;
import nc.vo.tg.outside.LLCtApPlanJsonVO;
import nc.vo.tg.outside.LLCtApServiceInfoJsonVO;
import nc.vo.tg.outside.LLCtApSupplyAgreementJsonVO;

import com.alibaba.fastjson.JSONObject;

public class COMEBSCtApBillToNC extends CtApBillUtil {
	protected AggCtApVO onTranBill(HashMap<String, Object> info, String hpk)
			throws BusinessException {
		AggCtApVO ctapaggvo = new AggCtApVO();
		JSONObject jsonData = (JSONObject) info.get("data");
		// 表头信息
		LLCtApJsonVO headvo = JSONObject.parseObject(
				jsonData.getString("headInfo"), LLCtApJsonVO.class);
		// 合同基本
		List<LLCtApJsonBVO> ctApBvos = JSONObject.parseArray(
				jsonData.getString("ctapbvos"), LLCtApJsonBVO.class);

		// 收款计划
		List<LLCtApPlanJsonVO> ctApPlanBvos = JSONObject.parseArray(
				jsonData.getString("ctapplans"), LLCtApPlanJsonVO.class);
		// 补充协议
		List<LLCtApSupplyAgreementJsonVO> ctApSupplyAgreementBvos = JSONObject
				.parseArray(jsonData.getString("ctapsupplyagreements"),
						LLCtApSupplyAgreementJsonVO.class);
		// 多税率
		List<LLCtApMoretaxJsonVO> ctApMoretaxBvos = JSONObject.parseArray(
				jsonData.getString("ctapmoretaxs"), LLCtApMoretaxJsonVO.class);
		// 租赁合同/租赁协议
		List<LLCtApLeaseconJsonVO> ctApLeaseconBvos = JSONObject
				.parseArray(jsonData.getString("ctapleasecons"),
						LLCtApLeaseconJsonVO.class);
		// 服务信息
		List<LLCtApServiceInfoJsonVO> ctApServiceInfoBVOs = JSONObject
				.parseArray(jsonData.getString("ctapserviceinfos"),
						LLCtApServiceInfoJsonVO.class);
		// 财务计提计划
		List<LLCtApAccrualPlanJsonVO> ctApAccrualPlanBVOs = JSONObject
				.parseArray(jsonData.getString("ctapaccrualplans"),
						LLCtApAccrualPlanJsonVO.class);
		// 扣款记录
		List<LLCtApDeductionRecordBVO> ctApDeductionRecordBVOs = JSONObject
				.parseArray(jsonData.getString("ctapdeductionrecords"),
						LLCtApDeductionRecordBVO.class);

		OrgVO orgvo = getOrgVO(headvo.getOuflag());
		if (orgvo == null || "".equals(orgvo)) {
			throw new BusinessException("财务组织Ouflag:" + headvo.getOuflag()
					+ "在NC找不到关联的数据!");
		}

		String srcid = headvo.getSrcid();// 外系统业务单据ID
		String srcbillno = headvo.getSrcbillno();
		// Object[] defpks = super.getDeptpksByCode(headvo.getDepid(),
		// orgvo.getPk_org());

		CtApVO ctapvo = buildHeadVo(orgvo, null, headvo);

		// 合同基本信息
		CtApBVO[] ctarbvos = null;
		if (ctApBvos != null && ctApBvos.size() > 0) {
			ctarbvos = new CtApBVO[ctApBvos.size()];
			int ctApBVORowNo = 10;
			for (int i = 0; i < ctApBvos.size(); i++) {
				// 合同基本信息
				CtApBVO ctApBVO = new CtApBVO();
				ctApBVO.setCrowno(String.valueOf(ctApBVORowNo));
				ctApBVO.setPk_fct_ap(hpk);
				ctApBVO.setPk_org(orgvo.getPk_org());
				ctApBVO.setPk_org_v(orgvo.getPk_vid());
				ctApBVO.setPk_group(orgvo.getPk_group());
				ctApBVO.setFtaxtypeflag(1); // 扣税类别
				// 本币价税合计
				ctApBVORowNo += 10;

				// **********成本拆分（合同基本）参照设值*****************//
				ctApBVO.setPk_ebs(ctApBvos.get(i).getPk_ebs());// ebs行主键
				ctApBVO.setVbdef11(getPk_objByCode(ctApBvos.get(i)
						.getFbudgetsubjectname(), orgvo.getPk_org())); // 科目名称（预算科目）
				ctApBVO.setVbdef13(ctApBvos.get(i).getSplitratio());// 拆分比例
				ctApBVO.setVbdef14(ctApBvos.get(i).getAmount());// 金额（元）
				ctApBVO.setVbdef19(ctApBvos.get(i).getIspresupposed());// 预提
				ctApBVO.setVbdef20(ctApBvos.get(i).getRemark());// 说明
				ctApBVO.setVbdef30(ctApBvos.get(i).getBudgetyears());// 预算年度
				ctApBVO.setProject(ctApBvos.get(i).getNcprojectname());// 项目
				// ctApBVO.setMaterialcategory(ctApBvos.get(i)
				// .getMaterialcategory());// 物料类别
				// ctApBVO.setMaterialcode(ctApBvos.get(i).getMaterialcode());//
				// 物料编码
				// ctApBVO.setRemarks(ctApBvos.get(i).getRemarks());// 物料说明
				// ctApBVO.setNastnum(new
				// UFDouble(ctApBvos.get(i).getNastnum()));// 数量
				// ctApBVO.setNtaxrate(new
				// UFDouble(ctApBvos.get(i).getNtaxrate()));// 税率（%）
				// ctApBVO.setNorigtaxprice(new UFDouble(ctApBvos.get(i)
				// .getNorigtaxprice()));// 单价（含税）
				// ctApBVO.setNorigprice(new UFDouble(ctApBvos.get(i)
				// .getNorigprice()));// 单价（不含税）
				// ctApBVO.setNotaxmny(new
				// UFDouble(ctApBvos.get(i).getNotaxmny()));// 不含税总价
				ctApBVO.setNtaxmny(new UFDouble(ctApBvos.get(i).getAmount()));// 合同金额
				ctApBVO.setNorigtaxmny(new UFDouble(ctApBvos.get(i).getAmount()));
				ctApBVO.setTs(new UFDateTime());
				ctarbvos[i] = ctApBVO;
			}
		}
		// 付款计划
		CtApPlanVO[] ctapplanvos = null;
		if (ctApPlanBvos != null && ctApPlanBvos.size() > 0) {
			ctapplanvos = new CtApPlanVO[ctApPlanBvos.size()];
			for (int i = 0; i < ctApPlanBvos.size(); i++) {
				CtApPlanVO ctApPlanVO = new CtApPlanVO();
				ctApPlanVO.setPk_ebs(ctApPlanBvos.get(i).getPk_ebs());// ebs主键
				// 款项类型CONTRACT 签约金额请款 ,MARGIN 保证金押金质保金共管资金请款
				String proceedtype = getdefdocBycode(ctApPlanBvos.get(i)
						.getProceedtype(), "SDLL012");
				if (proceedtype != null && !"".equals(proceedtype)) {
					ctApPlanVO.setDef5(proceedtype);// 款项类型
				} else {
					throw new BusinessException("付款计划表体第" + i
							+ "行款项类型proceedtype: "
							+ ctApPlanBvos.get(i).getProceedtype()
							+ "在NC找不到关联数据!");
				}

				String paymentdate = ctApPlanBvos.get(i).getPaymentdate();
				if (null != paymentdate && !"".equals(paymentdate)) {
					ctApPlanVO.setD_payplan(new UFDate(paymentdate));// 计划付款日期
				}
				ctApPlanVO.setPlanrate(new UFDouble(ctApPlanBvos.get(i)
						.getPaymentratio()));// 计划付款比例
				ctApPlanVO.setPlanmoney(new UFDouble(ctApPlanBvos.get(i)
						.getPaymentamount()));// 计划付款金额
				ctApPlanVO.setPay_condition(ctApPlanBvos.get(i)
						.getPaymentcondition());// 付款条件
				ctApPlanVO.setDef2(getdefdocBycode(ctApPlanBvos.get(i)
						.getCharactertype(), "zdy020"));// 款项性质
				ctApPlanVO.setMsumapply(new UFDouble(ctApPlanBvos.get(i)
						.getLineamountam()));// 累计请款金额
				ctApPlanVO.setMsumpayed(new UFDouble(ctApPlanBvos.get(i)
						.getPaidamount()));// 累计已付款金额
				ctApPlanVO.setMcompensate(new UFDouble(ctApPlanBvos.get(i)
						.getOffsetamount()));// 抵冲金额
				ctApPlanVO.setCompensateno(ctApPlanBvos.get(i)
						.getFapplynumber());// 抵冲单号
				ctApPlanVO.setDeal(ctApPlanBvos.get(i).getFollowup());// 后续处理
				String expecteddate = ctApPlanBvos.get(i).getExpecteddate();
				if (null != expecteddate && !"".equals(expecteddate)) {
					ctApPlanVO.setD_return(new UFDate(expecteddate));// 预计退回/解付日期
				}
				ctApPlanVO.setConreturn(ctApPlanBvos.get(i)
						.getReturnconditions());// 退回/解付条件
				String refunddate = ctApPlanBvos.get(i).getRefunddate();
				if (null != refunddate && !"".equals(refunddate)) {
					ctApPlanVO.setD_refund(new UFDate(refunddate));// 收到退款日期
				}
				ctApPlanVO.setMback(new UFDouble(ctApPlanBvos.get(i)
						.getRefundamount()));// 退款金额
				ctApPlanVO.setDef1(ctApPlanBvos.get(i).getRefundamountsum());// 退款总额
				ctApPlanVO.setTs(new UFDateTime());
				ctApPlanVO.setPk_fct_ap(hpk);
				ctapplanvos[i] = ctApPlanVO;
			}
		}
		// 补充协议
		SupplyAgreementBVO[] supplyagreementvos = null;
		if (ctApSupplyAgreementBvos != null
				&& ctApSupplyAgreementBvos.size() > 0) {
			supplyagreementvos = new SupplyAgreementBVO[ctApSupplyAgreementBvos
					.size()];
			for (int i = 0; i < ctApSupplyAgreementBvos.size(); i++) {
				SupplyAgreementBVO supplyAgreementBVO = new SupplyAgreementBVO();
				supplyAgreementBVO.setPk_fct_ap(hpk);
				supplyAgreementBVO.setTs(new UFDateTime());
				supplyAgreementBVO.setBillno(ctApSupplyAgreementBvos.get(i)
						.getSupnumber());
				supplyAgreementBVO.setPk_ebs(ctApSupplyAgreementBvos.get(i)
						.getPk_ebs());
				supplyAgreementBVO.setName(ctApSupplyAgreementBvos.get(i)
						.getSupname());
				supplyAgreementBVO.setMmonery(new UFDouble(
						ctApSupplyAgreementBvos.get(i).getSupamount()));
				String supdate = ctApSupplyAgreementBvos.get(i).getSupdate();
				if (null != supdate && !"".equals(supdate)) {
					supplyAgreementBVO.setD_date(new UFDate(supdate));
				}
				supplyAgreementBVO.setMsupply(new UFDouble(
						ctApSupplyAgreementBvos.get(i).getMsupply()));
				supplyagreementvos[i] = supplyAgreementBVO;
			}
		}
		// 多税率 ctapmoretaxbvos
		FctMoretax[] moretaxs = null;
		if (ctApMoretaxBvos != null && ctApMoretaxBvos.size() > 0) {
			moretaxs = new FctMoretax[ctApMoretaxBvos.size()];
			for (int i = 0; i < ctApMoretaxBvos.size(); i++) {
				FctMoretax fctMoretax = new FctMoretax();
				fctMoretax.setPk_fct_ap(hpk);
				fctMoretax.setTs(new UFDateTime());
				fctMoretax.setMaininvoicetype(getdefdocBycode(ctApMoretaxBvos
						.get(i).getFinvoicetype(), "pjlx"));
				fctMoretax.setPk_ebs(ctApMoretaxBvos.get(i).getPk_ebs());
				fctMoretax.setTaxrate(new UFDouble(ctApMoretaxBvos.get(i)
						.getFtaxrateid()));
				fctMoretax.setIntaxmny(new UFDouble(ctApMoretaxBvos.get(i)
						.getFamount()));
				fctMoretax.setTaxmny(new UFDouble(ctApMoretaxBvos.get(i)
						.getFtaxvalue()));
				fctMoretax.setNotaxmny(new UFDouble(ctApMoretaxBvos.get(i)
						.getNotaxmny()));
				moretaxs[i] = fctMoretax;
			}
		}
		// 租赁合同/租赁协议ctApLeaseconBvos
		LeaseconBVO[] leasecons = null;
		if (ctApLeaseconBvos != null && ctApLeaseconBvos.size() > 0) {
			leasecons = new LeaseconBVO[ctApLeaseconBvos.size()];
			for (int i = 0; i < ctApLeaseconBvos.size(); i++) {
				LeaseconBVO leaseconBVO = new LeaseconBVO();
				leaseconBVO.setPk_fct_ap(hpk);
				leaseconBVO.setTs(new UFDateTime());
				leaseconBVO.setLease(ctApLeaseconBvos.get(i).getLeasename());
				leaseconBVO.setPk_ebs(ctApLeaseconBvos.get(i).getPk_ebs());
				leaseconBVO.setArea(ctApLeaseconBvos.get(i).getLeasearea());
				String begindate = ctApLeaseconBvos.get(i).getBegindate();
				if (null != begindate && !"".equals(begindate)) {
					leaseconBVO.setD_start(new UFDate(begindate));
				}
				String enddate = ctApLeaseconBvos.get(i).getEnddate();
				if (null != enddate && !"".equals(enddate)) {
					leaseconBVO.setD_end(new UFDate(enddate));
				}

				String isfixed = ctApLeaseconBvos.get(i).getIsfixed();
				leaseconBVO.setB_fixedfunds(isfixed == "Y" ? UFBoolean.TRUE
						: UFBoolean.FALSE);
				leaseconBVO.setClauses(ctApLeaseconBvos.get(i).getRemark());
				leasecons[i] = leaseconBVO;
			}
		}
		// 服务信息
		CtApServiceInfoBVO[] serviceinfos = null;
		if (ctApServiceInfoBVOs != null && ctApServiceInfoBVOs.size() > 0) {
			serviceinfos = new CtApServiceInfoBVO[ctApServiceInfoBVOs.size()];
			for (int i = 0; i < ctApServiceInfoBVOs.size(); i++) {
				CtApServiceInfoBVO ctApServiceInfoBVO = new CtApServiceInfoBVO();
				ctApServiceInfoBVO.setPk_fct_ap(hpk);
				ctApServiceInfoBVO.setTs(new UFDateTime());
				String startdate = ctApServiceInfoBVOs.get(i).getStartdate();
				if (null != startdate && !"".equals(startdate)) {
					ctApServiceInfoBVO.setStartdate(new UFDateTime(startdate));
				}
				String enddate = ctApServiceInfoBVOs.get(i).getEnddate();
				if (null != enddate && !"".equals(enddate)) {
					ctApServiceInfoBVO.setEnddate(new UFDateTime(enddate));
				}
				ctApServiceInfoBVO.setPersonnum(ctApServiceInfoBVOs.get(i)
						.getPersonnum());
				ctApServiceInfoBVO.setPersonprice(ctApServiceInfoBVOs.get(i)
						.getPersonprice());
				ctApServiceInfoBVO.setPrice(new UFDouble(ctApServiceInfoBVOs
						.get(i).getPrice()));
				serviceinfos[i] = ctApServiceInfoBVO;
			}
		}
		// 财务计提计划
		CtApAccrualPlanBVO[] accrualplans = null;
		if (ctApAccrualPlanBVOs != null && ctApAccrualPlanBVOs.size() > 0) {
			accrualplans = new CtApAccrualPlanBVO[ctApAccrualPlanBVOs.size()];
			for (int i = 0; i < ctApAccrualPlanBVOs.size(); i++) {
				CtApAccrualPlanBVO ctApAccrualPlanBVO = new CtApAccrualPlanBVO();
				ctApAccrualPlanBVO.setMonth(ctApAccrualPlanBVOs.get(i)
						.getMonth());
				ctApAccrualPlanBVO.setAccurpayamount(new UFDouble(
						ctApAccrualPlanBVOs.get(i).getAccurpayamount()));
				ctApAccrualPlanBVO.setIsaccurflag(ctApAccrualPlanBVOs.get(i)
						.getIsaccurflag());
				ctApAccrualPlanBVO.setPk_fct_ap(hpk);
				accrualplans[i] = ctApAccrualPlanBVO;
			}
		}
		// 扣款记录
		CtApDeductionRecordBVO[] deductionrecords = null;
		if (ctApDeductionRecordBVOs != null
				&& ctApDeductionRecordBVOs.size() > 0) {
			deductionrecords = new CtApDeductionRecordBVO[ctApDeductionRecordBVOs
					.size()];
			for (int i = 0; i < ctApAccrualPlanBVOs.size(); i++) {
				CtApDeductionRecordBVO ctApDeductionRecordBVO = new CtApDeductionRecordBVO();
				String deductiondate = ctApDeductionRecordBVOs.get(i)
						.getDeductiondate();
				if (null != deductiondate && !"".equals(deductiondate)) {
					ctApDeductionRecordBVO.setDeductiondate(new UFDateTime(
							deductiondate));
				}
				ctApDeductionRecordBVO.setDeductionrate(ctApDeductionRecordBVOs
						.get(i).getDeductionrate());
				ctApDeductionRecordBVO.setDeductionamount(new UFDouble(
						ctApDeductionRecordBVOs.get(i).getDeductionamount()));
				ctApDeductionRecordBVO.setAccumulateddeduction(new UFDouble(
						ctApDeductionRecordBVOs.get(i)
								.getAccumulateddeduction()));
				ctApDeductionRecordBVO
						.setDeductionremark(ctApDeductionRecordBVOs.get(i)
								.getDeductionremark());
				deductionrecords[i] = ctApDeductionRecordBVO;
			}
		}

		ctapaggvo.setParentVO(ctapvo);
		ctapaggvo.setCtApBVO(ctarbvos);
		ctapaggvo.setCtApPlanVO(ctapplanvos);
		ctapaggvo.setSupplyAgreementBVO(supplyagreementvos);
		ctapaggvo.setFctMoretax(moretaxs);
		ctapaggvo.setLeaseconBVO(leasecons);
		ctapaggvo.setCtApServiceInfoBVO(serviceinfos);
		ctapaggvo.setCtApAccrualPlanBVO(accrualplans);
		ctapaggvo.setCtApDeductionRecordBVO(deductionrecords);
		// 更新的合同
		if (hpk != null) {
			AggCtApVO oldaggvos = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0 and blatest ='Y' and vbillcode = '"
							+ srcbillno + "'");
			if (oldaggvos == null) {
				throw new BusinessException("变更失败！nc系统中查不到该单据！");
			}
			// 将原始VO表头的部分数据填充到新VO
			String primaryKey = oldaggvos.getPrimaryKey();
			ctapaggvo.getParentVO().setPrimaryKey(primaryKey);
			ctapaggvo.getParentVO().setTs(oldaggvos.getParentVO().getTs()); // 同步表头ts
			ctapaggvo.getParentVO().setStatus(VOStatus.UPDATED);
			ctapaggvo.getParentVO().setBillmaker(
					oldaggvos.getParentVO().getBillmaker());
			ctapaggvo.getParentVO().setActualinvalidate(
					oldaggvos.getParentVO().getActualinvalidate());
			ctapaggvo.getParentVO().setApprover(
					oldaggvos.getParentVO().getApprover());
			ctapaggvo.getParentVO().setCreator(
					oldaggvos.getParentVO().getCreator());
			ctapaggvo.getParentVO().setCreationtime(
					oldaggvos.getParentVO().getCreationtime());
			ctapaggvo.getParentVO().setDmakedate(
					oldaggvos.getParentVO().getDmakedate());
			ctapaggvo.getParentVO().setTaudittime(
					oldaggvos.getParentVO().getTaudittime());

			syncBvoPkByEbsPk(CtApBVO.class, primaryKey, ctapaggvo,
					CtApBVO.PK_FCT_AP_B, CtApBVO.getDefaultTableName());
			syncBvoPkByEbsPk(CtApPlanVO.class, primaryKey, ctapaggvo,
					CtApPlanVO.PK_FCT_AP_PLAN, CtApPlanVO.getDefaultTableName());
			syncBvoPkByEbsPk(SupplyAgreementBVO.class, primaryKey, ctapaggvo,
					SupplyAgreementBVO.PK_SUPPLY_AGREEMENT_B,
					SupplyAgreementBVO.getDefaultTableName());
			syncBvoPkByEbsPk(FctMoretax.class, primaryKey, ctapaggvo,
					FctMoretax.PK_MORETAXRATE, FctMoretax.getDefaultTableName());
			syncBvoPkByEbsPk(LeaseconBVO.class, primaryKey, ctapaggvo,
					LeaseconBVO.PK_LEASECON_B,
					LeaseconBVO.getDefaultTableName());
			syncBvoPkByEbsPk(CtApServiceInfoBVO.class, primaryKey, ctapaggvo,
					CtApServiceInfoBVO.PK_FCT_SERVICEINFO_B,
					CtApServiceInfoBVO.getDefaultTableName());
			syncBvoPkByEbsPk(CtApAccrualPlanBVO.class, primaryKey, ctapaggvo,
					CtApAccrualPlanBVO.PK_FCT_ACCRUALPLAN_B,
					CtApAccrualPlanBVO.getDefaultTableName());
			syncBvoPkByEbsPk(CtApDeductionRecordBVO.class, primaryKey,
					ctapaggvo, CtApDeductionRecordBVO.PK_FCT_DEDUCTIONRECORD_B,
					CtApDeductionRecordBVO.getDefaultTableName());
		}

		return ctapaggvo;
	}
}
