package nc.bs.tg.outside.payablebill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.service.TimeService;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.paybill.PayBillCreateForSrmrf;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.calulate.CalultateUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class PayableBillUtils extends BillUtils implements
		ITGSyncService {
	String pk_tradetype = null;
	boolean isPushApprove = false;
	String defaultoperator = null;

	public String getDefaultoperator() {
		return DefaultOperator;
	}

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(getDefaultoperator()));
		InvocationInfoProxy.getInstance().setUserCode(getDefaultoperator());
		// 外系统信息
		JSONObject data = (JSONObject) info.get("data");// 外系统来源表头数据
		JSONObject jsonhead = (JSONObject) data.get("headInfo");// 外系统来源表头数据
		// SRM请款特殊业务,先预付款则直接走付款单 -start-
		String isadvancepay = jsonhead.getString("isadvancepay");// 是否预付款
		if ("Y".equals(isadvancepay)) {
			return new PayBillCreateForSrmrf().onSyncInfo(info, methodname);
		}
		// SRM请款特殊业务,先预付款则直接走付款单 -end-

		String srcid = jsonhead.getString("srcid");// 来源ID
		String srcno = jsonhead.getString("srcbillno");// 来源单据号

		Map<String, String> resultInfo = new HashMap<String, String>();
		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
					AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【"
						+ billkey
						+ "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "】,请勿重复上传!");
			}
			AggPayableBillVO billvo = onTranBill(info);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			WorkflownoteVO worknoteVO = getWorkflowMachine().checkWorkFlow(
					"SAVE", billvo.getHeadVO().getPk_tradetype(), billvo,
					new HashMap<String, Object>());
			Object obj = getPfBusiAction().processAction("SAVE",
					billvo.getHeadVO().getPk_tradetype(), worknoteVO, billvo,
					null, null);
			AggPayableBillVO[] billvos = (AggPayableBillVO[]) obj;
			if (isPushApprove()) {
				approveSilently(billvo.getHeadVO().getPk_tradetype(),
						billvos[0].getPrimaryKey(), "Y", "审核通过", billvos[0]
								.getHeadVO().getCreator(), false);
			}

			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayableBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggPayableBillVO onTranBill(HashMap<String, Object> info)
			throws BusinessException {
		JSONObject data = (JSONObject) info.get("data");// 外系统来源表头数据
		JSONObject head = (JSONObject) data.get("headInfo");// 外系统来源表头数据
		JSONArray bodys = (JSONArray) data.get("bodyInfos");// 外系统来源表体数据
		AggPayableBillVO billVO = onDefaultValue(head, bodys);
		// 主表信息转换
		UFDouble money = UFDouble.ZERO_DBL;// 金额
		UFDouble local_money = UFDouble.ZERO_DBL;// 组织金额
		UFDouble group_money = UFDouble.ZERO_DBL;// 集团金额
		UFDouble global_money = UFDouble.ZERO_DBL;// 全局金额
		// 明细信息转换
		List<PayableBillItemVO> blists = new ArrayList<PayableBillItemVO>();
		if (bodys != null && bodys.size() > 0) {
			for (int row = 0; row < bodys.size(); row++) {
				try {
					JSONObject body = bodys.getJSONObject(row);
					PayableBillItemVO itmevo = (PayableBillItemVO) billVO
							.getChildrenVO()[row];
					setItemVO((PayableBillVO) billVO.getParentVO(), itmevo,
							body);
					CalultateUtils.getUtils()
							.calculate(billVO, IBillFieldGet.MONEY_CR, row,
									Direction.CREDIT.VALUE);
					CalultateUtils.getUtils().calculate(billVO,
							IBillFieldGet.LOCAL_TAX_CR, row,
							Direction.CREDIT.VALUE);
					itmevo.setOccupationmny(itmevo.getMoney_cr());// 预占用原币余额

					money = money.add(itmevo.getMoney_cr());// 金额
					local_money = local_money.add(itmevo.getLocal_money_cr());// 组织金额
					group_money = group_money.add(itmevo.getGroupcrebit());// 集团金额
					global_money = global_money.add(itmevo.getGlobalcrebit());// 全局金额
					blists.add(itmevo);
				} catch (Exception e) {
					throw new BusinessException("行[" + (row + 1) + "],"
							+ e.getMessage(), e);
				}
			}
		}

		billVO.getParentVO().setAttributeValue(PayableBillVO.MONEY, money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.LOCAL_MONEY,
				local_money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.GROUPLOCAL,
				group_money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.GLOBALLOCAL,
				global_money);

		return billVO;
	}

	protected AggPayableBillVO onDefaultValue(JSONObject head,
			JSONArray bodylist) throws BusinessException {
		AggPayableBillVO aggvo = new AggPayableBillVO();
		PayableBillVO hvo = new PayableBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// 当前时间
		hvo.setAttributeValue(PayableBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// 集团
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// 期初标志
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// 是否红冲过
		hvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate(billdate));// 单据日期
		hvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate(billdate));// 起算日期
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// 单据会计年度
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// 单据会计期间
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);

		setHeaderVO(hvo, head);

		hvo.setAttributeValue(PayableBillVO.BILLMAKER,
				getUserIDByCode(getDefaultoperator()));// 制单人
		hvo.setAttributeValue(PayableBillVO.CREATOR, hvo.getBillmaker());// 创建人
		hvo.setAttributeValue(PayableBillVO.CREATIONTIME, currTime);// 创建时间
		hvo.setAttributeValue(PayableBillVO.PK_BILLTYPE, IBillFieldGet.F1);// 单据类型编码
		hvo.setAttributeValue(PayableBillVO.BILLCLASS, IBillFieldGet.YF);// 单据大类
		hvo.setAttributeValue(PayableBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据所属系统
		hvo.setAttributeValue(PayableBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据来源系统
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, IBillFieldGet.D1);// 应收类型code
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// 单据状态
		// 交易类型
		BilltypeVO billTypeVo = PfDataCache.getBillType(StringUtils
				.isBlank(head.getString("tradetype")) ? getTradetype() : head
				.getString("tradetype"));

		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// 应收类型
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE,
				billTypeVo.getPk_billtypecode());// 应收类型

		aggvo.setParentVO(hvo);
		PayableBillItemVO[] itemVOs = new PayableBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayableBillItemVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// 行号
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
		getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	}

	/**
	 * 主体信息
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected abstract void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException;

	/**
	 * 明细信息
	 * 
	 * @param parentVO
	 * @param itmevo
	 * @param row
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected abstract void setItemVO(PayableBillVO parentVO,
			PayableBillItemVO itmevo, JSONObject body) throws BusinessException;

	/**
	 * 相应接口下指向的交易类型
	 */
	protected abstract String getTradetype();

	/**
	 * 是否推审核
	 * 
	 * @return
	 */
	public boolean isPushApprove() {
		return isPushApprove;
	}

}
