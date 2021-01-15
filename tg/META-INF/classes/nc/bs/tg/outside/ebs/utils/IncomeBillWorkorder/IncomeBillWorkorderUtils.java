package nc.bs.tg.outside.ebs.utils.IncomeBillWorkorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.service.TimeService;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.EBSCont;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.tg.invoicebill.InvoiceBillBVO;
import nc.vo.tg.invoicebill.InvoiceBillVO;

import nc.vo.tg.outside.incomebill.IncomeBillBodyVO;
import nc.vo.tg.outside.incomebill.IncomeBillHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class IncomeBillWorkorderUtils extends EBSBillUtils{
	Map<String, String> tradetypeMap = null;
	static IncomeBillWorkorderUtils utils;
	
	
	public static IncomeBillWorkorderUtils getUtils() {
		if (utils == null) {
			utils = new IncomeBillWorkorderUtils();
		}
		return utils;
	}
	

	/**
	 * 进项发票单
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */

	public String onSyncBill(HashMap<String, Object> value, String srctype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		// 外系统信息
		JSONObject jsonData = (JSONObject) value.get("data");// 表单数据
		JSONObject jsonhead = (JSONObject) jsonData.get("headInfo");// 外系统来源表头数据
		String srcid = jsonhead.getString("ebsid");// EBS业务单据ID
		String srcno = jsonhead.getString("ebsbillno");// EBS业务单据单据号
		String billqueue = EBSCont.getSrcBillNameMap().get(srctype) + ":"
				+ srcid;
		String billkey = EBSCont.getSrcBillNameMap().get(srctype) + ":" + srcno;
		AggInvoiceBillVO aggVO = (AggInvoiceBillVO) DataChangeUtils.getUtils().getInvoiceBillVO(
				AggInvoiceBillVO.class, "isnull(dr,0)=0 and def1 = '" + srcid
						+ "'");
		if (aggVO != null) {
			throw new BusinessException("【"
					+ billkey
					+ "】,NC已存在对应的业务单据【"
					+ aggVO.getParentVO().getAttributeValue(
							InvoiceBillVO.BILLNO) + "】,请勿重复上传!");
		}
		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			AggInvoiceBillVO billvo = onTranBill(jsonData, srctype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN05", null,
					billvo, null, eParam);
			AggInvoiceBillVO[] billvos = (AggInvoiceBillVO[]) obj;
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(InvoiceBillVO.BILLNO));
			return JSON.toJSONString(dataMap);

		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param hMap
	 * @return
	 */

	/**
	 * 信息转换
	 * 
	 * @param srcdata
	 * @param srctype
	 * @return
	 */
	public AggInvoiceBillVO onTranBill(JSONObject srcdata, String srctype)
			throws BusinessException {
		JSON jsonhead = (JSON) srcdata.get("headInfo");// 外系统来源表头数据
		String jsonbody = srcdata.getString("bodyInfos");// 外系统来源表体数据
		if (srcdata == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("表单数据为空，请检查！json:" + srcdata);
		}
		IncomeBillHeadVO headvo = JSONObject.toJavaObject(jsonhead,
				IncomeBillHeadVO.class);// 外部系统信息
		List<IncomeBillBodyVO> bodylist = JSON.parseArray(jsonbody,
				IncomeBillBodyVO.class);// 外部系统信息

		AggInvoiceBillVO billVO = onDefaultValue(headvo, bodylist, srctype);
		// 主表信息转换

		UFDouble money = UFDouble.ZERO_DBL;// 金额
		UFDouble local_money = UFDouble.ZERO_DBL;// 组织金额
		UFDouble group_money = UFDouble.ZERO_DBL;// 集团金额
		UFDouble global_money = UFDouble.ZERO_DBL;// 全局金额
		// 明细信息转换
		List<InvoiceBillBVO> blists = new ArrayList<InvoiceBillBVO>();
		if (bodylist != null && bodylist.size() > 0) {
			for (int row = 0; row < bodylist.size(); row++) {

				try {
					IncomeBillBodyVO bodyvo = bodylist.get(row);
					InvoiceBillBVO itmevo = (InvoiceBillBVO) billVO
							.getChildrenVO()[row];

					DataChangeUtils.getUtils().setItemVO((InvoiceBillVO) billVO.getParentVO(), itmevo,
							bodyvo);

//					calculate(billVO, IBillFieldGet.MONEY_CR, row);
					// itmevo.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR,
					// bodyvo.getLocal_tax_cr());// 税额
//					calculate(billVO, IBillFieldGet.LOCAL_TAX_CR, row);
	/*				money = money.add(itmevo.getMoney_cr());// 金额
					local_money = local_money.add(itmevo.getLocal_money_cr());// 组织金额
					group_money = group_money.add(itmevo.getGroupcrebit());// 集团金额
					global_money = global_money.add(itmevo.getGlobalcrebit());// 全局金额
					blists.add(itmevo);*/
				} catch (Exception e) {
					throw new BusinessException("行[" + (row + 1) + "],"
							+ e.getMessage(), e);
				}
			}
		}
//
//		billVO.getParentVO().setAttributeValue(PayableBillVO.MONEY, money);
//		billVO.getParentVO().setAttributeValue(PayableBillVO.LOCAL_MONEY,
//				local_money);
//		billVO.getParentVO().setAttributeValue(PayableBillVO.GROUPLOCAL,
//				group_money);
//		billVO.getParentVO().setAttributeValue(PayableBillVO.GLOBALLOCAL,
//				global_money);

		return billVO;
	}
	
	protected AggInvoiceBillVO onDefaultValue(IncomeBillHeadVO headvo,
			List<IncomeBillBodyVO> bodylist, String srctype)
			throws BusinessException {
		AggInvoiceBillVO aggvo = new AggInvoiceBillVO();
		InvoiceBillVO hvo = new InvoiceBillVO();
		String billdate = headvo.getBilldate();
		if (billdate == null || "".equals(billdate)) {
			throw new BusinessException("单据日期不可为空!");
		}

		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// 当前时间

		hvo.setAttributeValue(InvoiceBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// 集团
		hvo.setAttributeValue(InvoiceBillVO.PK_ORG,
				getPk_orgByCode(headvo.getOrg()));// 应付财务组织->NC业务单元编码
		hvo.setAttributeValue(InvoiceBillVO.CREATOR, getSaleUserID());// 制单人
		//hvo.setAttributeValue(PayableBillVO.CREATOR, hvo.getBillmaker());// 创建人
		hvo.setAttributeValue(InvoiceBillVO.CREATIONTIME, currTime);// 创建时间
		/*			hvo.setAttributeValue(InvoiceBillVO.TRANSTYPE, IBillFieldGet.F1);// 单据类型编码
		//hvo.setAttributeValue(InvoiceBillVO.BILLCLASS, IBillFieldGet.YF);// 单据大类
		hvo.setAttributeValue(InvoiceBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据所属系统
		hvo.setAttributeValue(InvoiceBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据来源系统
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, IBillFieldGet.D1);// 应收类型code
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// 单据状态
		// 交易类型
	BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetypeMap().get(
				srctype));
		if (billTypeVo == null) {
			throw new BusinessException("【"
					+ EBSCont.getSrcBillNameMap().get(srctype)
					+ "】相关的交易类型未设置,请联系系统管理员!");
		}

		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// 应收类型
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, getTradetypeMap()
				.get(srctype));// 应收类型
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// 期初标志
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// 是否红冲过
		hvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate(billdate));// 单据日期
		hvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate(billdate));// 起算日期
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// 单据会计年度
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// 单据会计期间
*/
		//DataChangeUtils.utils.setHeaderVO(hvo, headvo);
		DataChangeUtils.utils.setHeaderVO(hvo, headvo);

		aggvo.setParentVO(hvo);
		InvoiceBillBVO[] itemVOs = new InvoiceBillBVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new InvoiceBillBVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// 行号
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
	/*	IArapBillPubQueryService servie = NCLocator.getInstance().lookup(
				.class);
		servie.getDefaultVO(aggvo, true);*/

		return aggvo;
	}
	
	



	

}
