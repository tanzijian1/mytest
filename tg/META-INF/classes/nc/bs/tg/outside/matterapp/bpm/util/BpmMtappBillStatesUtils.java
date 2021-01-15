package nc.bs.tg.outside.matterapp.bpm.util;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BPMBillUtil;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.IPushBPMLLBillService;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IPfExchangeService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.erm.matterapp.MatterAppVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import uap.distribution.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BpmMtappBillStatesUtils extends BPMBillUtil implements
		ITGSyncService {

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		JSONObject jsonhead = (JSONObject) info.get("headinfo");// 外系统来源表头数据
		valid(jsonhead);
		Map<String, String> resultInfo = new HashMap<String, String>();// 返回信息
		String operator = jsonhead.getString("operator");
		if (operator == null || "".equals(operator)) {
			InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
			InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		} else {
			Map<String, String> userInfo = getUserInfo(operator);
			if (userInfo == null) {
				throw new BusinessException("操作员【" + operator
						+ "】未能在NC用户档案关联到,请联系系统管理员！");
			}
		}
		String bpmid = jsonhead.getString("bpmid");// BPMID
		String action = jsonhead.getString("billstate");/*
														 * UNSAVE 集团财务退回；
														 * UNAPPROVE 地区财务退回；
														 * GROUPAPPROVE 集团财务审批；
														 * REGAPPROVE 地区财务审批；
														 * REFUSE 拒绝；
														 */
		String returnMsg = jsonhead.getString("returnMsg");// 退回原因
		if (returnMsg.contains("退回发起人")) {/*
										 * 处理BPM反调机制； 例如共享退回发起人NC单据已经回到自由态
										 * ，但BPM还会回调NC取消审批接口
										 * ，故直接返回成功;该退回原因是由NC发给BPM，BPM直接返回
										 * 的，涉及类有hz
										 * .itf.fssc.impl.billhandler.around
										 * .after.MatterHandlerAfterHandler
										 * 和nc.ui.cmp.settlement.actions.
										 * ChargebackBillAction
										 */
			resultInfo.put("BPMID", bpmid);
			resultInfo.put("msg", "【" + action + "】单据操作完成");
			return JSON.toJSONString(resultInfo);
		}

		if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
				|| "REFUSE".equals(action)) {
			if (StringUtil.isBlank(returnMsg)) {
				throw new BusinessException("操作异常，退回操作时退回信息不能为空!");
			}
		}
		AggMatterAppVO aggVO = (AggMatterAppVO) getBillVO(AggMatterAppVO.class,
				"nvl(dr,0)=0 and defitem1 ='" + bpmid + "'");
		try {
			if (aggVO == null) {
				throw new BusinessException("NC系统bpm主键未有对应单据");
			}
			MatterAppVO hvo = aggVO.getParentVO();
			String billtype = hvo.getPk_tradetype();
			String billid = hvo.getPrimaryKey();
			String defitem14 = hvo.getDefitem14();// 是否预付款
			String concode = hvo.getDefitem7();// 合同编号

			if ("GROUPAPPROVE".equals(action) || "REGAPPROVE".equals(action)) {
				if (hvo.getApprstatus() != 1) {
					approveSilently("261X", billid, "Y", "批准", "", false);
				}
				// BPM第一次调用审批接口时推生应付单，第二次调用时需同时审批应付单
				AggPayableBillVO payaggvo = (AggPayableBillVO) getBillVO(
						AggPayableBillVO.class,
						"nvl(dr,0)=0 and pk_tradetype in('F1-Cxx-LL02','F1-Cxx-LL03') and bpmid ='"
								+ bpmid + "'");
				AggPayBillVO paybillaggvo = null;
				if (payaggvo == null) {// 应付单为null时，才去查询付款单
					paybillaggvo = (AggPayBillVO) getBillVO(AggPayBillVO.class,
							"nvl(dr,0)=0 and bpmid ='" + bpmid + "'");
				}
				if (payaggvo != null || paybillaggvo != null) {
					if (payaggvo != null) {// 当应付单不为空时，进行应付单的审批操作
						approveSilently("F1", payaggvo.getParentVO()
								.getPrimaryKey(), "Y", "批准", "", false);
					}
					if (paybillaggvo != null) {// 当付款单不为空时，进行付款单的审批操作
						approveSilently("F3", paybillaggvo.getParentVO()
								.getPrimaryKey(), "Y", "批准", "", false);
					}
				} else {
					/*
					 * 1、费用申请单是否预付款为Y，NC和共享生成付款单； 2、费用申请单是否预付款为N，NC和共享生成应付单；
					 * 3、费用申请单是否周期性合同为Y、是否预付款为N，共享和NC生成应付单（请款单金额），
					 * 并且生成一张红字应付单，冲减对应合同的周期性累计计提金额。（请款金额<累计计提金额,
					 * 红字应付单金额为负的请款金额；请款金额>累计计提金额，红字应付单金额为负数累计计提金额）。
					 */
					if ("Y".equals(defitem14)) {
						// 生成付款单
						try {
							createPayBill(aggVO, billtype, bpmid);
						} catch (Exception e) {
							throw new BusinessException("生成付款单异常:"
									+ e.getMessage());
						}
					} else {
						Object obj;
						try {
							obj = createPayableBill(payaggvo, aggVO, billtype,
									bpmid, concode, false);
						} catch (Exception e) {
							throw new BusinessException("生成应付单异常:"
									+ e.getMessage());
						}
					}
				}

			} else if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
					|| "REFUSE".equals(action)) {// BPM集团财务退回操作：不清除taskid

				// 终止费用申请单的流程
				try {
					IPushBPMLLBillService service = NCLocator.getInstance()
							.lookup(IPushBPMLLBillService.class);
					service.dealChargebackMattApp(bpmid, returnMsg);
				} catch (Exception e) {
					throw new BusinessException("终止流程发生异常:" + e.getMessage(), e);
				}

				aggVO = (AggMatterAppVO) getBillVO(AggMatterAppVO.class,
						"nvl(dr,0)=0 and defitem1 ='" + bpmid + "'");
				aggVO.getParentVO().setDefitem2("N");
				if ("REFUSE".equals(action)) {
					aggVO.getParentVO().setDefitem1("~");
				}
				// 更新VO数据(将BPM流程修改)
				updateBillWithAttrs(aggVO, new String[] { MatterAppVO.DEFITEM1,
						MatterAppVO.DEFITEM2 });
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		resultInfo.put("BPMID", bpmid);
		resultInfo.put("msg", "【" + action + "】单据操作完成");
		return JSON.toJSONString(resultInfo);
	}

	private void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("操作状态不能为空");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPM业务单据主键不能为空");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("目标单据不能为空");
		// if ("APPROVE".equals(jobj.getString("billstate"))) {
		// if (jobj.getString("data") == null || jobj.getString("") == "")
		// throw new BusinessException("传入信息DATA不能为空");
		// }
	}

	/**
	 * 生成对应的应付单信息
	 * 
	 * @param payaggvo
	 *            应付单聚合VO
	 * @param aggVO
	 *            费用申请单聚合VO
	 * @param billtype
	 *            费用申请单交易类型
	 * @param bpmid
	 *            BPMID
	 * @param isCycleProvision
	 *            是否周期性计提
	 * @throws BusinessException
	 */
	private Object createPayableBill(AggPayableBillVO payaggvo,
			AggMatterAppVO aggVO, String billtype, String bpmid,
			String concode, Boolean isCycleProvision) throws Exception {
		String pk_tradetype = null;
		if ("261X-Cxx-LL01".equals(billtype)) {
			pk_tradetype = "F1-Cxx-LL02";
		} else if ("261X-Cxx-LL02".equals(billtype)) {
			pk_tradetype = "F1-Cxx-LL03";
		}
		IPfExchangeService service = NCLocator.getInstance().lookup(
				IPfExchangeService.class);
		payaggvo = (AggPayableBillVO) service.runChangeData(billtype, pk_tradetype,
				aggVO, null);
		payaggvo.getParent().setAttributeValue(PayableBillVO.PK_TRADETYPE,
				pk_tradetype);
		Map<String, String> map = DocInfoQryUtils.getUtils().getBillType(
				String.valueOf(payaggvo.getParent().getAttributeValue(
						PayableBillVO.PK_TRADETYPE)));
		String billtypid = map.get("pk_billtypeid");
		PayableBillVO hvo = (PayableBillVO) payaggvo.getParentVO();
		UFDate billdate = hvo.getBilldate();
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID, billtypid);
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// 期初标志
		if (billdate != null) {
			hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
					String.valueOf(billdate.getYear()));// 单据会计年度
			hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
					billdate.getStrMonth());// 单据会计期间
		}
		hvo.setAttributeValue(PayableBillVO.BILLCLASS, IBillFieldGet.YF);// 单据大类
		hvo.setAttributeValue(PayableBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据所属系统
		hvo.setAttributeValue(PayableBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据来源系统
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);
		hvo.setAttributeValue(PayableBillVO.BPMID, bpmid);// BPMID
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// 单据状态
		if (isCycleProvision) {// 红字应付单
			UFDouble orig_amount = aggVO.getParentVO().getOrig_amount();
			UFDouble def64 = UFDouble.ZERO_DBL;// 累计周期计提金额
			UFDouble def50 = UFDouble.ZERO_DBL;// 累计计提红冲金额
			String sql = "SELECT fct_ap.def64,fct_ap.def50 FROM fct_ap "
					+ "where fct_ap.vbillcode = '" + concode
					+ "' and fct_ap.blatest = 'Y' and fct_ap.dr = 0";
			@SuppressWarnings("unchecked")
			Map<String, String> conMap = (Map<String, String>) getQueryBS()
					.executeQuery(sql, new MapProcessor());
			if (conMap != null && conMap.size() > 0) {
				def64 = new UFDouble(conMap.get("def64") == null ? ""
						: conMap.get("def64"));
				def50 = new UFDouble(conMap.get("def50") == null ? ""
						: conMap.get("def50"));
			}
			UFDouble rest_money = def64.sub(def50);
			UFDouble red_money = UFDouble.ZERO_DBL;// 红冲金额
			if (orig_amount.compareTo(rest_money) > 0) {
				hvo.setAttributeValue(PayableBillVO.LOCAL_MONEY,
						rest_money.multiply(-1));
				red_money = rest_money;
			} else {
				hvo.setAttributeValue(PayableBillVO.LOCAL_MONEY,
						orig_amount.multiply(-1));
				rest_money = orig_amount;
			}
			// 更新付款合同的累计计提红冲金额
			red_money = red_money.add(def50);
		}

		String recaccount = null;// 收款银行账号
		String def18 = aggVO.getParentVO().getDefitem18();
		if (StringUtil.isBlank(def18)) {
			recaccount = aggVO.getParentVO().getDefitem21();
		} else {
			recaccount = def18;
		}
		hvo.setAttributeValue(PayableBillVO.RECACCOUNT, recaccount);// 收款银行账号
		hvo.setAttributeValue(PayableBillVO.PAYACCOUNT, recaccount);// 付款银行账户

		String def15 = aggVO.getParentVO().getDefitem15();// 费用申请单-收款对象
		String objtypeName = DocInfoQryUtils.getUtils().getDefNameInfo(def15);// 收款对象名称
		Integer objtype = new Integer(3);
		if (!StringUtil.isBlank(objtypeName)) {
			switch (objtypeName) {
			case "业务员":
				objtype = 3;
				break;
			case "部门":
				objtype = 2;
				break;
			case "供应商":
				objtype = 1;
				break;
			default:
				break;
			}
		}
		hvo.setAttributeValue(PayableBillVO.OBJTYPE, objtype);// 收款对象
		PayableBillItemVO[] itemVOs = (PayableBillItemVO[]) payaggvo
				.getChildrenVO();
		String def16 = aggVO.getParentVO().getDefitem16();// 费用申请单-用款人
		String supplier = aggVO.getParentVO().getPk_supplier();// 费用申请单-供应商
		String pk_bankaccbas = null;
		if (objtype == 3) {// 业务员
			String sql = "SELECT pk_bankaccsub FROM bd_bankaccsub where pk_bankaccbas = (SELECT "
					+ "pk_bankaccbas FROM bd_bankaccbas bas where bas.accnum = '"
					+ def18
					+ "' and enablestate = 2 and nvl(dr,0)=0) and nvl(dr,0)=0";
			pk_bankaccbas = (String) getQueryBS().executeQuery(sql,
					new ColumnProcessor());
		}
		for (int i$ = 0, j = itemVOs.length; i$ < j; i$++) {
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// 行号
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
			itemVOs[i$].setAttributeValue(PayableBillItemVO.OBJTYPE, objtype);
			itemVOs[i$].setAttributeValue(PayableBillItemVO.PK_PSNDOC, def16);
			itemVOs[i$].setAttributeValue(PayableBillItemVO.SUPPLIER, supplier);
			if (objtype == 3) {
				itemVOs[i$].setAttributeValue(IBillFieldGet.RECACCOUNT,
						pk_bankaccbas);
			}
		}
		IArapBillPubQueryService servie = NCLocator.getInstance().lookup(
				IArapBillPubQueryService.class);
		servie.getDefaultVO(payaggvo, true);
		HashMap hmPfExParams = new HashMap();
		hmPfExParams.put("nolockandconsist", true);
		Object obj = getPfBusiAction().processAction("SAVE", "F1", null,
				payaggvo, null, hmPfExParams);
		return obj;
	}

	/**
	 * 生成对应的付款单信息
	 * 
	 * @param aggVO
	 *            费用申请单聚合VO
	 * @param billtype
	 *            费用申请单交易类型
	 * @param bpmid
	 *            BPMID
	 * @throws BusinessException
	 */
	private void createPayBill(AggMatterAppVO aggVO, String billtype,
			String bpmid) throws Exception {
		AggPayBillVO aggvo = new AggPayBillVO();
		String busicode = null;
		String pk_tradetype = null;
		String mbilltype = aggVO.getParentVO().getPk_tradetype();// 费用申请单交易类型
		if ("261X-Cxx-LL01".equals(mbilltype)) {// 邻里-历史合同或非合同请款
			pk_tradetype = "F3-Cxx-LL07";
			busicode = "Cxx-SDLL05";
		} else if ("261X-Cxx-LL02".equals(mbilltype)) {// 邻里-合同或非合同请款
			pk_tradetype = "F3-Cxx-LL06";
			busicode = "Cxx-SDLL04";
		}
		IPfExchangeService service = NCLocator.getInstance().lookup(
				IPfExchangeService.class);
		aggvo = (AggPayBillVO) service.runChangeData(billtype, pk_tradetype, aggVO,
				null);
		PayBillVO hvo = (PayBillVO) aggvo.getParentVO();

		String def15 = aggVO.getParentVO().getDefitem15();// 费用申请单-收款对象
		String objtypeName = DocInfoQryUtils.getUtils().getDefNameInfo(def15);// 收款对象名称
		Integer objtype = new Integer(3);
		if (!StringUtil.isBlank(objtypeName)) {
			switch (objtypeName) {
			case "业务员":
				objtype = 3;
				break;
			case "部门":
				objtype = 2;
				break;
			case "供应商":
				objtype = 1;
				break;
			default:
				break;
			}
		}
		hvo.setObjtype(objtype);
		hvo.setPk_billtype("F3");
		hvo.setPk_tradetype(pk_tradetype);
		// 设置默认信息
		hvo.setAttributeValue("syscode", "1");
		hvo.setAttributeValue("src_syscode", "1");
		String pk_busitype = DocInfoQryUtils.getUtils().getBusitype(busicode);
		hvo.setAttributeValue("pk_busitype", pk_busitype);
		hvo.setAttributeValue("billstatus", -1);
		hvo.setAttributeValue("approvestatus", -1);
		hvo.setBpmid(bpmid);
		Map<String, String> map = DocInfoQryUtils.getUtils().getBillType(
				hvo.getPk_tradetype());
		String pk_tradetypeid = map.get("pk_billtypeid");
		hvo.setPk_tradetypeid(pk_tradetypeid);

		String pk_bankaccbas = null;
		String def18 = aggVO.getParentVO().getDefitem18();
		if (objtype == 3) {// 业务员
			String sql = "SELECT pk_bankaccbas FROM bd_bankaccbas bas where bas.accnum = '"
					+ def18 + "' and enablestate = 2 and nvl(dr,0)=0";
			pk_bankaccbas = (String) getQueryBS().executeQuery(sql,
					new ColumnProcessor());
		}

		// 给表体信息设置默认值
		PayBillItemVO[] bvos = (PayBillItemVO[]) aggvo.getChildrenVO();
		for (PayBillItemVO bvo : bvos) {
			bvo.setObjtype(objtype);
			bvo.setSupplier(hvo.getSupplier());
			bvo.setMoney_de(bvo.getLocal_money_de());
			if (objtype == 3) {
				bvo.setRecaccount(pk_bankaccbas);
			}
		}

		IArapBillPubQueryService servie = NCLocator.getInstance().lookup(
				IArapBillPubQueryService.class);
		servie.getDefaultVO(aggvo, true);
		getPfBusiAction().processAction("SAVE", "F3", null, aggvo, null, null);
		// approveSilently("F3", aggvo.getPrimaryKey(), "Y", "批准", "", false);
	}

}
