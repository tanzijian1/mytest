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
 * 收入合同工具类入口
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
	 * 收入合同报错
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
		// 表头信息
		CtArJsonVO headvo = JSONObject.parseObject(
				jsonData.getString("ctarheadvo"), CtArJsonVO.class);
		String ebsID = headvo.getVdef18();// EBS主键
		String ebsNo = headvo.getVdef18();// EBS单据号
		// 目标单据名+请求合同编号作队列
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + ebsNo;
		// 目标单据名+请求单据pk作日志输出
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + ebsID;

//		EBSBillUtils.addBillQueue(billkey);// 增加队列处理

		AggCtArVO aggVO = (AggCtArVO) getBillVO(AggCtArVO.class,
				"isnull(dr,0)=0 and vdef18 = '" + ebsID + "'");
		if (value.get("change") == null && aggVO != null) {
			throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
					+ aggVO.getParentVO().getAttributeValue(CtArVO.VBILLCODE)
					+ "】,如需变动，请走变更流程!");
		}
		AggCtArVO[] aggvos = null;
		AggCtArVO billVO = null;
		Map valueMap = new HashMap<>();
		HashMap<String, Object> eParam = new HashMap<String, Object>();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		try {
			// 通用类合同
			if ("31".equals(srctype)) {
				if (value.get("change") != null
						&& !"".equals(value.get("change"))) {
					// 变更逻辑
					AggCtArVO[] oldaggvos = service
							.queryCtArVoByIds(new String[] { (String) value
									.get("change") });
					if (oldaggvos == null) {
						throw new BusinessException("变更失败！nc系统中查不到该单据！");
					}
					AggCtArVO aggvo = new AggCtArVO();
					aggvo = onTranBill(value, srctype);
					
					//将原始VO表头的部分数据填充到新VO
					String primaryKey = oldaggvos[0].getPrimaryKey();
					aggvo.getParentVO().setPrimaryKey(primaryKey);
					aggvo.getParentVO().setTs(oldaggvos[0].getParentVO().getTs()); // 同步表头ts
					aggvo.getParentVO().setStatus(VOStatus.UPDATED);
					aggvo.getParentVO().setBillmaker(oldaggvos[0].getParentVO().getBillmaker());
					aggvo.getParentVO().setActualinvalidate(oldaggvos[0].getParentVO().getActualinvalidate());
					aggvo.getParentVO().setApprover(oldaggvos[0].getParentVO().getApprover());
					aggvo.getParentVO().setCreator(oldaggvos[0].getParentVO().getCreator());
					aggvo.getParentVO().setCreationtime(oldaggvos[0].getParentVO().getCreationtime());
					aggvo.getParentVO().setDmakedate(oldaggvos[0].getParentVO().getDmakedate());
					aggvo.getParentVO().setTaudittime(oldaggvos[0].getParentVO().getTaudittime());
					
					syncBvoPkByEbsPk(CtArBVO.class, primaryKey, aggvo, CtArBVO.PK_FCT_AR_B, CtArBVO.getDefaultTableName());
					syncBvoPkByEbsPk(CtArPlanVO.class, primaryKey, aggvo, CtArPlanVO.PK_FCT_AR_PLAN, "fct_ar_plan");//收款计划
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
				// 地产类收入合同
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
				jsonData.getString("armsgvos"), FctArmsgJsonVO.class);// 收款信息
		List<FctarmsgsubJsonVO> armsgsubvos = JSONObject.parseArray(
				jsonData.getString("armsgsubvos"), FctarmsgsubJsonVO.class);// 收款补充信息
		List<FctprogressJsonVO> progressvos = JSONObject.parseArray(
				jsonData.getString("progressvos"), FctprogressJsonVO.class);// 交付进度
		OrgVO orgvo = getOrgVO(headvo.getPk_org());
		// 校验表头数据
		vaildHeadData(headvo);

		// 表头信息
		Object[] defpks = super.getDeptpksByCode(headvo.getDepid(),
				orgvo.getPk_org());
		AggCtArVO aggvo = new AggCtArVO();
		CtArVO ctarvo = buildheadvo(orgvo, defpks, headvo, srctype);

		// 收款信息
		List<FctArmsgVO> armsgList = new ArrayList<>();
		if (armsgvos != null && armsgvos.size() > 0) {
			for (FctArmsgJsonVO armsgjsonvo : armsgvos) {
				FctArmsgVO armsg = new FctArmsgVO();
				armsg.setPaymethod(armsgjsonvo.getPaymethod());// 付款方式
				armsg.setStantotalprice(new UFDouble(armsgjsonvo
						.getStantotalprice()));// 标准总价
				armsg.setPrice(new UFDouble(armsgjsonvo.getPrice()));// 单价
				armsg.setPainland(new UFDouble(armsgjsonvo.getPainland()));// 已付借款
				armsg.setDebt(new UFDouble(armsgjsonvo.getDebt()));// 欠款
				armsg.setBalancedue(new UFDouble(armsgjsonvo.getBalancedue()));// 装修欠款
				armsg.setMertrulitydebt(new UFDouble(armsgjsonvo
						.getMertrulitydebt()));// 到期欠款
				armsg.setUnmertrulitydebt(new UFDouble(armsgjsonvo
						.getUnmertrulitydebt()));// 未到期欠款
				armsg.setMgpayment(new UFDouble(armsgjsonvo.getMgpayment()));// 按揭款
				armsg.setMgpaymentdebt(new UFDouble(armsgjsonvo
						.getMgpaymentdebt()));// 按揭欠款
				armsg.setMutiplerept(new UFDouble(armsgjsonvo.getMutiplerept()));// 多收款
				armsg.setMgbank(armsgjsonvo.getMgbank());// 按揭银行
				armsg.setMgstatus(armsgjsonvo.getMgstatus());// 按揭状态
				armsg.setMgcompletime(armsgjsonvo.getMgcompletime() == null ? null
						: new UFDateTime(armsgjsonvo.getMgcompletime()));// 按揭完成时间
				armsg.setVbdef1(armsgjsonvo.getVbdef1());// 自定义项1
				armsg.setVbdef2(armsgjsonvo.getVbdef2());// 自定义项2
				armsg.setVbdef3(armsgjsonvo.getVbdef3());// 自定义项3
				armsg.setVbdef4(armsgjsonvo.getVbdef4());// 自定义项4
				armsg.setVbdef5(armsgjsonvo.getVbdef5());// 自定义项5
				armsg.setVbdef6(armsgjsonvo.getVbdef6());// 自定义项6
				armsg.setVbdef7(armsgjsonvo.getVbdef7());// 自定义项7
				armsg.setVbdef8(armsgjsonvo.getVbdef8());// 自定义项8
				armsg.setVbdef9(armsgjsonvo.getVbdef9());// 自定义项9
				armsg.setVbdef10(armsgjsonvo.getVbdef10());// 自定义项10
				armsgList.add(armsg);
			}
		}

		// 收款补充信息
		List<FctarmsgsubVO> armsubList = new ArrayList<>();
		UFDouble mny = UFDouble.ZERO_DBL;
		if (armsgsubvos != null && armsgsubvos.size() > 0) {
			for (FctarmsgsubJsonVO armsgsub : armsgsubvos) {
				FctarmsgsubVO armsgvo = new FctarmsgsubVO();
				armsgvo.setArloanmny(armsgsub.getArloanmny());// 实收贷款进额
				armsgvo.setPripaymethod(armsgsub.getPripaymethod());// 一级付款方式
				armsgvo.setSenpaymethod(armsgsub.getSenpaymethod());// 二级付款方式
				armsgvo.setVbdef1(armsgsub.getVbdef1());// 自定义项1
				armsgvo.setVbdef2(armsgsub.getVbdef2());// 自定义项2
				armsgvo.setVbdef3(armsgsub.getVbdef3());// 自定义项3
				armsgvo.setVbdef4(armsgsub.getVbdef4());// 自定义项4
				armsgvo.setVbdef5(armsgsub.getVbdef5());// 自定义项5
				armsgvo.setVbdef6(armsgsub.getVbdef6());// 自定义项6
				armsgvo.setVbdef7(armsgsub.getVbdef7());// 自定义项7
				armsgvo.setVbdef8(armsgsub.getVbdef8());// 自定义项8
				armsgvo.setVbdef9(armsgsub.getVbdef9());// 自定义项9
				armsgvo.setVbdef10(armsgsub.getVbdef10());// 自定义项10
				armsgvo.setPk_psndoc(armsgsub.getPk_psndoc());// 跟进人
				armsgvo.setPurchqualifi(armsgsub.getPurchqualifi());// 购房资格
				armsgvo.setBuildamount(armsgsub.getBuildamount());// 楼款总额
				armsgvo.setArnoloanmny(armsgsub.getArnoloanmny());// 实收非贷款总额
				armsgvo.setCreditdate(armsgsub.getCreditdate() == null ? null
						: new UFDate(armsgsub.getCreditdate()));// 放款日期
				armsgvo.setPrcreditdate(armsgsub.getPrcreditdate() == null ? null
						: new UFDate(armsgsub.getPrcreditdate()));// 达待放款日期
				mny = mny.add(new UFDouble(armsgsub.getBuildamount()));
				armsubList.add(armsgvo);

			}

		}
		// 构建合同基本，默认一行
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
		// 收款计划
		CtArPlanVO ctarplanvo = new CtArPlanVO();
		ctarplanvo.setAccountdate(0);
		ctarplanvo.setPlanrate(new UFDouble("100"));// 默认100
		ctarplanvo.setEnddate(new UFDate());// 收款日志
		ctarplanvo.setPk_org(orgvo.getPk_corp());
		ctarplanvo.setPk_org_v(orgvo.getPk_vid());
		ctarplanvo.setPlanmoney(mny);// 合同金额
		ctarplavvos[0] = ctarplanvo;
		// 交付进度
		List<FctprogressVO> fctList = new ArrayList<>();
		if (progressvos != null && progressvos.size() > 0) {
			for (FctprogressJsonVO progressvo : progressvos) {
				FctprogressVO fctprogressVO = new FctprogressVO();
				fctprogressVO.setApartment(progressvo.getApartment());// 户型
				fctprogressVO.setDecoratestan(progressvo.getDecoratestan());// 装修标准
				fctprogressVO.setNote(progressvo.getNote());// 折扣说明
				fctprogressVO.setRoompart(progressvo.getRoompart());// 关联房间车位
				fctprogressVO.setProprocess(progressvo.getProprocess());// 产权进程
				fctprogressVO.setSubtotalprice(new UFDouble(progressvo
						.getSubtotalprice()));// 补充协议总价
				fctprogressVO.setPaymny(new UFDouble(progressvo.getPaymny()));// 已付装修款
				fctprogressVO
						.setAgredate(progressvo.getAgredate() == null ? null
								: new UFDateTime(progressvo.getAgredate()));// 约定网签日期
				fctprogressVO
						.setAdvancedate(progressvo.getAdvancedate() == null ? null
								: new UFDateTime(progressvo.getAdvancedate()));// 取得预售证日期
				fctprogressVO.setAdvabcecode(progressvo.getAdvabcecode());// 预售证编号
				fctprogressVO
						.setPartrightime(progressvo.getPartrightime() == null ? null
								: new UFDate(progressvo.getPartrightime()));// 车位权确认日期
				fctprogressVO
						.setNetagredate(progressvo.getNetagredate() == null ? null
								: new UFDate(progressvo.getNetagredate()));// 网签日期
				fctprogressVO.setFiledate(progressvo.getFiledate());// 资料齐已送件日期
				fctprogressVO.setVbdef1(progressvo.getVbdef1());// 自定义项1
				fctprogressVO.setVbdef2(progressvo.getVbdef2());// 自定义项2
				fctprogressVO.setVbdef3(progressvo.getVbdef3());// 自定义项3
				fctprogressVO.setVbdef4(progressvo.getVbdef4());// 自定义项4
				fctprogressVO.setVbdef5(progressvo.getVbdef5());// 自定义项5
				fctprogressVO.setVbdef6(progressvo.getVbdef6());// 自定义项6
				fctprogressVO.setVbdef7(progressvo.getVbdef7());// 自定义项7
				fctprogressVO.setVbdef8(progressvo.getVbdef8());// 自定义项8
				fctprogressVO.setVbdef9(progressvo.getVbdef9());// 自定义项9
				fctprogressVO.setVbdef10(progressvo.getVbdef10());// 自定义项10
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
	 * 通用类合同 将来源信息转换成NC信息
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
		// 表头信息
		CtArJsonVO headvo = JSONObject.parseObject(
				jsonData.getString("ctarheadvo"), CtArJsonVO.class);
		// 合同基本
		List<CtArJsonBVO> ctArBvos1 = JSONObject.parseArray(
				jsonData.getString("ctarbvos"), CtArJsonBVO.class);

		// 执行情况
		List<CtArExecJsonVO> ctArExecBvos1 = JSONObject.parseArray(
				jsonData.getString("ctarexecdetail"), CtArExecJsonVO.class);
		// 收入科目拆分
		List<CtArSubspiltJsonVO> ctArSplitBvos1 = JSONObject.parseArray(
				jsonData.getString("ctararsubspilt"), CtArSubspiltJsonVO.class);

		// 补充协议
		List<CtArSubagremntJsonVO> subagremnts1 = JSONObject.parseArray(
				jsonData.getString("subagremnts"), CtArSubagremntJsonVO.class);
		// 收款计划
		List<CtArPlanJsonVO> ctsrplans1 = JSONObject.parseArray(
				jsonData.getString("ctsrplans"), CtArPlanJsonVO.class);
		OrgVO orgvo = getOrgVO(headvo.getPk_org());

		// 校验数据非空
		vaildHeadData(headvo);
		vaildCtArPlanBodyData(ctsrplans1);
		vaildCtArSubspiltBodyData(ctArSplitBvos1);

		// 表头信息
		Object[] defpks = super.getDeptpksByCode(headvo.getDepid_v(),
				orgvo.getPk_org());

		CtArVO ctarvo = buildheadvo(orgvo, defpks, headvo, srctype);

		// 合同基本信息，收款计划
		CtArBVO[] ctarbvos = null;
		CtArPlanVO[] ctarplavvos = null;
		ctarbvos = new CtArBVO[1];

		if (ctArBvos1 != null && ctArBvos1.size() > 0) {
			// for(int i=0;i<ctArBvos1.size();i++){
			// //合同基本信息
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
			// ctarbvo.setVbdef1(ctArBvos1.get(i).getVbdef1());//科目名称
			// ctarbvo.setVbdef2(ctArBvos1.get(i).getVbdef2());//拆分比例
			// ctarbvo.setVmemo(ctArBvos1.get(i).getVmemo());//说明
			// ctarbvo.setProject(ctArBvos1.get(i).getProject());//项目
			// ctarbvo.setInoutcome(ctArBvos1.get(i).getInoutcome());//收支项目
			// ctarbvo.setVbdef3(ctArBvos1.get(i).getVbdef3());//自定义项3
			// ctarbvo.setVbdef4(ctArBvos1.get(i).getVbdef4());//自定义项4
			// ctarbvo.setVbdef5(ctArBvos1.get(i).getVbdef5());//自定义项5
			// ctarbvo.setVbdef6(ctArBvos1.get(i).getVbdef6());//自定义项6
			// ctarbvo.setVbdef7(ctArBvos1.get(i).getVbdef7());//自定义项7
			// ctarbvo.setVbdef8(ctArBvos1.get(i).getVbdef8());//自定义项8
			// ctarbvo.setVbdef9(ctArBvos1.get(i).getVbdef9());//自定义项9
			// ctarbvo.setVbdef10(ctArBvos1.get(i).getVbdef10());//自定义项10
			// ctarbvo.setVbdef11(ctArBvos1.get(i).getVbdef11());//自定义项11
			// ctarbvo.setVbdef12(ctArBvos1.get(i).getVbdef12());//自定义项12
			// ctarbvo.setVbdef13(ctArBvos1.get(i).getVbdef13());//自定义项13
			// ctarbvo.setVbdef14(ctArBvos1.get(i).getVbdef14());//自定义项14
			// ctarbvo.setVbdef15(ctArBvos1.get(i).getVbdef15());//自定义项15
			// ctarbvo.setVbdef16(ctArBvos1.get(i).getVbdef16());//自定义项16
			// ctarbvo.setVbdef17(ctArBvos1.get(i).getVbdef17());//自定义项17
			// ctarbvo.setVbdef18(ctArBvos1.get(i).getVbdef18());//自定义项18
			// ctarbvo.setVbdef19(ctArBvos1.get(i).getVbdef19());//自定义项19
			// ctarbvo.setVbdef20(ctArBvos1.get(i).getVbdef20());//自定义项20
			// ctarbvo.setVchangerate("1.00/1.00");
			// ctarbvo.setVqtunitrate("1.00/1.00");
			// ctarbvo.setNtaxmny(new UFDouble(headvo.getVdef11()));
			// ctarbvo.setNorigtaxmny(new UFDouble(headvo.getVdef11()));
			// ctarbvos[i]=ctarbvo;
			// }
		}
		// 执行情况
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
				execvo.setVbdef2(ctArExecBvos1.get(i).getVbdef2());//所有表体vdef2定为：EBS主键
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
		// 收入科目拆分
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
		// 收款计划
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
						UFDouble.ROUND_HALF_DOWN));// 拆分比例
				ctarplanvo.setEnddate(new UFDate());// 收款日志
				ctarplanvo.setPk_org(orgvo.getPk_corp());
				ctarplanvo.setPk_org_v(orgvo.getPk_vid());
				ctarplanvo.setPlanmoney(planmoney);
				ctarplanvo.setVbdef1(ctsrplans1.get(i).getVbdef1());//收款条件
				ctarplanvo.setVbdef2(ctsrplans1.get(i).getVbdef2());//EBS主键
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
			ctarvo.setNtotalorigmny(total);// 原币价税合计
		}

		CtArBVO ctarbvo = new CtArBVO();

		ctarbvo.setCrowno("10");
		ctarbvo.setFtaxtypeflag(1);// 扣税类别
		ctarbvo.setNorigtaxmny(new UFDouble());
		ctarbvo.setPk_org(orgvo.getPk_org());
		ctarbvo.setPk_org_v(orgvo.getPk_vid());
		ctarbvo.setPk_financeorg(orgvo.getPk_org());
		ctarbvo.setPk_financeorg_v(orgvo.getPk_vid());
		ctarbvo.setNorigtaxmny(total);
		ctarbvo.setNtaxmny(total);
		ctarbvos[0] = ctarbvo;

		// 补充协议
		nc.vo.fct.entity.CtArSubagremntVO[] subagremnts = null;
		if (subagremnts1 != null && subagremnts1.size() > 0) {
			subagremnts = new nc.vo.fct.entity.CtArSubagremntVO[subagremnts1
					.size()];
			for (int i = 0; i < subagremnts1.size(); i++) {
				CtArSubagremntVO subagremnt = new CtArSubagremntVO();
				subagremnt.setCode(subagremnts1.get(i).getCode());// 协议编码
				subagremnt.setName(subagremnts1.get(i).getName());// 协议名称
				subagremnt.setSubagremntdate(subagremnts1.get(i)
						.getSubagremntdate() == null ? null : new UFDate(
						subagremnts1.get(i).getSubagremntdate()));// 协议时间
				subagremnt.setMny(new UFDouble(subagremnts1.get(i).getMny()));// 协议金额
				subagremnt.setVbdef1(subagremnts1.get(i).getVbdef1());// 自定项1
				subagremnt.setVbdef2(subagremnts1.get(i).getVbdef2());// 自定项2
				subagremnt.setVbdef3(subagremnts1.get(i).getVbdef3());// 自定项3
				subagremnt.setVbdef4(subagremnts1.get(i).getVbdef4());// 自定项4
				subagremnt.setVbdef5(subagremnts1.get(i).getVbdef5());// 自定项5
				subagremnt.setVbdef6(subagremnts1.get(i).getVbdef6());// 自定项6
				subagremnt.setVbdef7(subagremnts1.get(i).getVbdef7());// 自定项7
				subagremnt.setVbdef8(subagremnts1.get(i).getVbdef8());// 自定项8
				subagremnt.setVbdef9(subagremnts1.get(i).getVbdef9());// 自定项9
				subagremnt.setVbdef10(subagremnts1.get(i).getVbdef10());// 自定项10
				subagremnts[i] = subagremnt;
			}
		}
		CtArChangeVO changevo = new CtArChangeVO();
		changevo.setPk_org(orgvo.getPk_org());
		changevo.setPk_org_v(orgvo.getPk_vid());
		changevo.setPk_group(orgvo.getPk_group());
		changevo.setVmemo("原始版本");
		changevo.setVchangecode(new UFDouble(1));

		ctaraggvo.setParentVO(ctarvo);
		ctaraggvo.setCtArBVO(ctarbvos);
		// ctaraggvo.setCtArBVO(new CtArBVO[]{});
		ctaraggvo.setCtArPlanVO(ctarplavvos);// 收款计划
		ctaraggvo.setCtArSubagremntVO(subagremnts);// 补充协议
		ctaraggvo.setCtArChangeVO(new CtArChangeVO[] { changevo });
		ctaraggvo.setCtArExecDetailVO(execvos);// 执行情况
		ctaraggvo.setCtArMemoraVO(new CtArMemoraVO[] {});
		ctaraggvo.setCtArTermVO(new CtArTermVO[] {});
		ctaraggvo.setCtArExecVO(new CtArExecVO[] {});
		ctaraggvo.setCtArArsubspiltVO(subspiltvos);// 收入科目拆分
		return ctaraggvo;
	}

	/**
	 * 根据组织编码查询组织VO
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
	 * 查询
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
	 * 非空数据校验
	 * 
	 * @param headvo
	 *            表头VO
	 */
	private void vaildHeadData(CtArJsonVO headvo) throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (StringUtil.isEmpty(headvo.getCbilltypecode())) {
			str.append("数据异常，表头单据类型不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVtrantypecode())) {
			str.append("数据异常，表头合同类型不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getDbilldate())) {
			str.append("数据异常，表头创建日期不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getCorigcurrencyid())) {
			str.append("数据异常，表头币种不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getCtname())) {
			str.append("数据异常，表头合同名称不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef22())) {
			str.append("数据异常，表头经办人不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef23())) {
			str.append("数据异常，表头部门不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVbillcode())) {
			str.append("数据异常，表头合同编码不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef1())) {
			str.append("数据异常，表头板块不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef2())) {
			str.append("数据异常，表头收款公司不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef3())) {
			str.append("数据异常，表头甲方不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef4())) {
			str.append("数据异常，表头乙方不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef9())) {
			str.append("数据异常，表头合同状态不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef10())) {
			str.append("数据异常，表头签约金额不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef12())) {
			str.append("数据异常，表头税率不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef13())) {
			str.append("数据异常，表头城市公司不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef14())) {
			str.append("数据异常，表头合同管理人不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef16())) {
			str.append("数据异常，表头审批通过日期不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef18())) {
			str.append("数据异常，表头ESB单据id不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef19())) {
			str.append("数据异常，表头ESB单据号不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getPk_org())) {
			str.append("数据异常，表头财务组织不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getPk_customer())) {
			str.append("数据异常，表头客户不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVdef21())) {
			str.append("数据异常，表头EBS单据类型不能为空！");
		}
		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 
	 * @param ctArBvos1
	 *            收款计划
	 * @throws BusinessException
	 */
	private void vaildCtArPlanBodyData(List<CtArPlanJsonVO> ctsrplans1)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < ctsrplans1.size(); i++) {
			if (StringUtil.isEmpty(String.valueOf(ctsrplans1.get(i)
					.getPlanmoney()))) {
				str.append("数据异常，收款计划页签，第" + i + 1 + "行，收款金额不能为空！");
			}
			if (StringUtil.isEmpty(String.valueOf(ctsrplans1.get(i)
					.getPlanrate()))) {
				str.append("数据异常，收款计划页签，第" + i + 1 + "行，收款比率不能为空！");
			}
			if (StringUtil.isEmpty(String.valueOf(ctsrplans1.get(i)
					.getEnddate()))) {
				str.append("数据异常，收款计划页签，第" + i + 1 + "行，收款日期不能为空！");
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
				str.append("数据异常，收入科目拆分页签，第" + i + 1 + "行，科目名称不能为空！");
			}
			if (StringUtil.isEmpty(String.valueOf(ctArSplitBvos1.get(i)
					.getSpitrate()))) {
				str.append("数据异常，收入科目拆分页签，第" + i + 1 + "行，拆分比率不能为空！");
			}
			if (StringUtil.isEmpty(String.valueOf(ctArSplitBvos1.get(i)
					.getVbdef1()))) {
				str.append("数据异常，收入科目拆分页签，第" + i + 1 + "行，金额不能为空！");
			}
		}
		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 创建表头vo
	 * 
	 * @param srctype
	 *            收入合同类型
	 * @return
	 * @throws BusinessException
	 */
	private CtArVO buildheadvo(OrgVO orgvo, Object[] defpks, CtArJsonVO headvo,
			String srctype) throws BusinessException {
		CtArVO ctarvo = new CtArVO();
		ctarvo.setBankaccount(headvo.getBankaccount());
		ctarvo.setPk_org(orgvo.getPk_org());
		ctarvo.setPk_org_v(orgvo.getPk_vid());// 组织版本
		ctarvo.setCbilltypecode(headvo.getCbilltypecode());
		ctarvo.setVtrantypecode(headvo.getVtrantypecode());
		ctarvo.setDbilldate(new UFDate(headvo.getDbilldate()));
		ctarvo.setCcurrencyid("1002Z0100000000001K1");
		// ctarvo.setCcurrencyid(headvo.getCcurrencyid());
		ctarvo.setCorigcurrencyid("1002Z0100000000001K1");// 币种
		ctarvo.setCrececountryid("0001Z010000000079UJJ");// 收货国家地区
		ctarvo.setCsendcountryid("0001Z010000000079UJJ");// 发货国家地区
		ctarvo.setCtaxcountryid("0001Z010000000079UJJ");// 报税国家地区
		ctarvo.setVbillcode(headvo.getVbillcode());// 合同编码
		ctarvo.setCtrantypeid(super.getBillTypePkByCode(
				headvo.getVtrantypecode(), orgvo.getPk_group()));// 交易类型
		ctarvo.setPk_customer(super.getcustomer(headvo.getPk_customer()));// 客户
		ctarvo.setCtname(headvo.getCtname());
		ctarvo.setFbuysellflag(1);// 购销类型
		ctarvo.setPersonnelid(super.getPsndocPkByCode(headvo.getPersonnelid()));// 经办人
		if (defpks != null) {
			ctarvo.setDepid(defpks[0].toString());// 部门
			ctarvo.setDepid_v(defpks[1].toString());// 部门版本
		}
		ctarvo.setValdate(headvo.getValdate() == null ? new UFDate()
				: new UFDate(headvo.getValdate()));// 计划生效时间
		ctarvo.setInvallidate(headvo.getInvallidate() == null ? new UFDate()
				: new UFDate(headvo.getInvallidate()));// 计划终止时间
		ctarvo.setSubscribedate(headvo.getSubscribedate() == null ? new UFDate()
				: new UFDate(headvo.getSubscribedate()));// 合同签订日志
		ctarvo.setNexchangerate(new UFDouble(1.00));
		// 组织部部门，人员，信息
		ctarvo.setOrganizer(orgvo.getPk_org());// 承办组织
		ctarvo.setOrganizer_v(orgvo.getPk_vid());// 承办组织版本
		ctarvo.setPk_group(orgvo.getPk_group());
		ctarvo.setPk_org(orgvo.getPk_org());
		ctarvo.setPk_org_v(orgvo.getPk_vid());
		ctarvo.setSignorg(orgvo.getPk_org());
		ctarvo.setSignorg_v(orgvo.getPk_vid());
		ctarvo.setOverrate(UFDouble.ZERO_DBL);// 超合同收款比例
		ctarvo.setIprintcount(0);
		ctarvo.setFstatusflag(0);// 合同状态
		ctarvo.setVersion(new UFDouble(1.0));
		ctarvo.setVdef1(headvo.getVdef1());// 板块
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
		ctarvo.setVdef2(vdef2);// 收款公司
		ctarvo.setVdef3(vdef3);// 甲方
		ctarvo.setVdef4(getCustVO(headvo.getVdef4()));// 乙方
		ctarvo.setVdef5(getCustVO(headvo.getVdef5()));// 丙方
		ctarvo.setVdef6(getCustVO(headvo.getVdef6()));// 丁方
		ctarvo.setVdef7(getCustVO(headvo.getVdef7()));// 午方
		ctarvo.setVdef8(getCustVO(headvo.getVdef8()));// 己方
		ctarvo.setVdef9(headvo.getVdef9());// 合同状态
		ctarvo.setVdef10(headvo.getVdef10());// 签约金额
		ctarvo.setVdef11(headvo.getVdef11());// 动态金额
		ctarvo.setVdef12(headvo.getVdef12());// 税率
		ctarvo.setVdef13(headvo.getVdef13());// 是否多税率
		ctarvo.setVdef14(headvo.getVdef14());// 合同管理员
		ctarvo.setVdef15(headvo.getVdef15());// 累计补充协议金额
		ctarvo.setVdef16(headvo.getVdef16());// 审批通过日期
		ctarvo.setVdef17(headvo.getVdef17());// 填单日期
		ctarvo.setVdef18(headvo.getVdef18());// esb单据id
		ctarvo.setVdef19(headvo.getVdef19());// esb单据号
		ctarvo.setVdef20(headvo.getVdef20());// 摘要
		ctarvo.setVdef21(headvo.getVdef21());// 自定义项21
		ctarvo.setVdef22(headvo.getVdef22());// 自定义项22(经办人)
		ctarvo.setVdef23(headvo.getVdef23());// 自定义项23(经办部门)
		ctarvo.setVdef24(headvo.getVdef24());// 自定义项24（合同类型）
		ctarvo.setVdef25(headvo.getVdef25());// 自定义项25
		ctarvo.setVdef26(headvo.getVdef26());// 自定义项26
		ctarvo.setVdef27(headvo.getVdef27());// 自定义项27
		ctarvo.setVdef28(headvo.getVdef28());// 自定义项28
		ctarvo.setVdef29(headvo.getVdef29());// 自定义项29
		ctarvo.setVdef30(headvo.getVdef30());// 自定义项30
		ctarvo.setVdef31(headvo.getVdef31());// 产品类型
		ctarvo.setVdef32(headvo.getVdef32());// 项目名称
		ctarvo.setVdef33(headvo.getVdef33());// 大项目名称
		ctarvo.setVdef34(headvo.getVdef34());// 楼栋名称
		ctarvo.setVdef35(headvo.getVdef35());// 单元号
		ctarvo.setVdef36(headvo.getVdef36());// 认购书编号
		ctarvo.setVdef37(headvo.getVdef37());// 已发入伙通知书日期
		ctarvo.setVdef38(headvo.getVdef38());// 已发装修完工通知书日期
		ctarvo.setVdef39(headvo.getVdef39());// 已发催收楼通知书日期
		ctarvo.setVdef40(headvo.getVdef40());// 约定收楼日期
		ctarvo.setVdef41(headvo.getVdef41());// 实际收楼日期
		ctarvo.setVdef42(headvo.getVdef42());// 送备案日期
		ctarvo.setVdef43(headvo.getVdef43());// 合同备案号
		ctarvo.setVdef44(headvo.getVdef44());// 合同备案日期
		ctarvo.setVdef45(headvo.getVdef45());// 合同登记情况
		ctarvo.setVdef46(headvo.getVdef46());// 合同登记办理日期
		ctarvo.setVdef47(headvo.getVdef47());// 入伙完成时间
		ctarvo.setVdef48(headvo.getVdef48());// 入伙进程
		ctarvo.setVdef49(headvo.getVdef49());// 入伙进程备注
		ctarvo.setVdef50(headvo.getVdef50());// 建筑面积
		ctarvo.setVdef51(headvo.getVdef51());// 套内面积
		ctarvo.setVdef52(headvo.getVdef52());// 实测建筑面积
		ctarvo.setVdef53(headvo.getVdef53());// 实测套内面积
		return ctarvo;

	}

	/**
	 * 处理新表体数据信息
	 * @param clazz
	 *            voClass
	 * @param parentPKValue
	 *            NC付款合同表头主键值
	 * @param desAggVO
	 *            传参过来的aggVO(目标)
	 * @param bvoPKfiled
	 *            vo的主键字段名
	 * @param table
	 *            vo数据表名
	 * @throws BusinessException 
	 */
	private void syncBvoPkByEbsPk(Class<? extends ISuperVO> clazz,
			String parentPKValue, AggCtArVO desAggVO, String bvoPKfiled,
			String table) throws BusinessException {
		// 传参数的BVOs
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
	 * 处理表体删除标志
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
		// 删除原来的数据
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
	 * Title: getBvoPkByEbsPK<／p>
	 * <p>
	 * Description: <／p>
	 * 
	 * @param ebsPk
	 *            ebs对应页签数据行主键
	 * @param parentPKfield
	 *            NC付款合同表头主键字段
	 * @param bvoPKfiled
	 *            NC付款合同对应页签主键字段
	 * @param table
	 *            NC付款合同对应页签表名
	 * @param parentPkValue
	 *            NC付款合同表头主键值
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
