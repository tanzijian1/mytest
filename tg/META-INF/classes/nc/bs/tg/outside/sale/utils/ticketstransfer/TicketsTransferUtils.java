package nc.bs.tg.outside.sale.utils.ticketstransfer;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.vo.tgfn.transferbill.TransferBillBVO;
import nc.vo.tgfn.transferbill.TransferBillHVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * 筹转定转账单(FN15)
 * @author ln
 *
 */
public class TicketsTransferUtils extends SaleBillUtils {
	static TicketsTransferUtils utils;

	public static TicketsTransferUtils getUtils() {
		if (utils == null) {
			utils = new TicketsTransferUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		JSONObject headJson = (JSONObject) value.get("headInfo");
		String saleid = headJson.getString("def1");// 销售系统业务单据ID
		String saleno = headJson.getString("def2");// 销售系统业务单据单据号

		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;

		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		// Saleid 按实际存入信息位置进行变更
		AggTransferBillHVO aggVO = (AggTransferBillHVO) getBillVO(
				AggTransferBillHVO.class, "isnull(dr,0)=0 and def1 = '"
						+ saleid + "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String) aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}
		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {

			AggregatedValueObject billvo = onTranBill(value);
			HashMap<String, String> eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN15", null, billvo,
					null, eParam);
			AggTransferBillHVO[] billvos = (AggTransferBillHVO[]) obj;
			obj = getPfBusiAction().processAction("SAVE", "FN15", null, billvos[0],
					null, eParam);//提交
			billvos = (AggTransferBillHVO[])obj;
			getPfBusiAction().processAction("APPROVE", "FN15", null, billvos[0],
					null, eParam);//审批
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}
	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param hMap
	 * @return
	 */
	private AggTransferBillHVO onTranBill(HashMap<String, Object> value)
			throws BusinessException {

		String errorMsg = null;
		if (value == null || value.size() <= 0) {
			errorMsg = "数据为空，请检查请求参数";
			throw new BusinessException(errorMsg);
		}
		// 获取表头和表体的数据
		JSONObject headObj = (JSONObject) value.get("headInfo");
		JSONArray bodyArray = (JSONArray) value.get("itemInfo");

		if (headObj == null || headObj.size() <= 0) {
			errorMsg = "数据异常，必须有表头数据";
			throw new BusinessException(errorMsg);
		}

		// 检验表头表体数据
		validateHeadData(headObj);
		validateBodyData(bodyArray);

		AggTransferBillHVO aggvo = new AggTransferBillHVO();

		TransferBillHVO hvo = new TransferBillHVO();
		// 默认集团-时代集团-code:0001
		hvo.setPk_group("000112100000000005FD");
		
		hvo.setPk_org(getRefAttributePk("pk_org", headObj.getString("pk_org")));
		hvo.setBilldate(new UFDate(headObj.getString("billdate")));
		// 默认交易类型-code:FN15
		hvo.setTranstype("FN15");
		// 默认单据类型-code:FN15
		hvo.setBilltype("FN15");
		hvo.setBillmaker(getSaleUserID());
		// 默认单据状态-1：自由
		hvo.setBillstatus(-1);
		// 默认审批状态-1：自由
		hvo.setApprovestatus(-1);
		// 默认生效状态0：未生效
		hvo.setEffectstatus(0);
		hvo.setDef1(headObj.getString("def1"));
		hvo.setDef2(headObj.getString("def2"));
		hvo.setDef3(headObj.getString("def3"));
		hvo.setDef4(headObj.getString("def4"));
		hvo.setDef5(headObj.getString("def5"));//
		hvo.setDef6(headObj.getString("def6"));//
		hvo.setDef7(headObj.getString("def7"));//
		hvo.setDef8(headObj.getString("def8"));//
		hvo.setDef9(headObj.getString("def9"));//
		hvo.setDef10(getUserByPsondoc(headObj.getString("def10")));//销售系统审批人
		hvo.setDef15(headObj.getString("def15"));//附件张数
		aggvo.setParentVO(hvo);

		TransferBillBVO[] bvos = new TransferBillBVO[bodyArray.size()];
		for (int i = 0; i < bvos.length; i++) {
			TransferBillBVO bvo = new TransferBillBVO();
			JSONObject bJSONObject = bodyArray.getJSONObject(i);
			bvo.setScomment(bJSONObject.getString("scomment"));//摘要
			bvo.setDef1(getPk_projectByCode(bJSONObject.getString("def1")));//房地产项目
			bvo.setDef2(getPk_projectByCode(bJSONObject.getString("def2")));//房地产项目明细
			bvo.setDef3(getPk_defdocByCode(bJSONObject.getString("def3")));//款项类型
			bvo.setDef4(getPk_defdocByCode(bJSONObject.getString("def4")));//款项名称
			bvo.setDef5(getPk_defdocByName(bJSONObject.getString("def5")));//业态
			bvo.setDef6(getPk_defdocByName(bJSONObject.getString("def6")));//计税方式
			bvo.setDef7(bJSONObject.getString("def7"));//税率
			bvo.setDef8(bJSONObject.getString("def8"));//税额
			bvo.setDef9(bJSONObject.getString("def9"));//不含税金额
			bvo.setDef10(bJSONObject.getString("def10"));//含税总金额
			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}

	private void validateHeadData(JSONObject data) throws BusinessException {
		
		String pkOrg = data.getString("pk_org");
		String billDate = data.getString("billdate");
		// 销售系统单据ID
		String def1 = data.getString("def1");
		// 销售系统单号
//		String def2 = data.getString("def2");
		// 影像编码
		String def3 = data.getString("def3");
		// 影像状态
		String def4 = data.getString("def4");
		
		if (pkOrg == null || "".equals(pkOrg)) {
			throw new BusinessException("财务组织不能为空");
		}
		
		if (billDate == null || "".equals(billDate)) {
			throw new BusinessException("单据日期不能为空");
		}
		if (def1 == null || "".equals(def1)) {
			throw new BusinessException("销售系统单据ID不能为空");
		}
//		if (def2 == null || "".equals(def2)) {
//			throw new BusinessException("销售系统单号不能为空");
//		}
//		if (def3 == null || "".equals(def3)) {
//			throw new BusinessException("影像编码不能为空");
//		}
//		if (def4 == null || "".equals(def4)) {
//			throw new BusinessException("影像状态不能为空");
//		}

	}

	private void validateBodyData(JSONArray jsonArray) throws BusinessException {
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject data = jsonArray.getJSONObject(i);
			// NC,SALE:摘要
//			String scomment = data.getString("scomment");
			// NC:房地产项目-SALE:所属项目
			String def1 = data.getString("def1");
			// NC:房地产项目明细-SALE:购买房间
			String def2 = data.getString("def2");
			// NC,SALE:业态
			String def3 = data.getString("def3");
			// NC,SALE:计税方式
			String def4 = data.getString("def4");
			// NC,SALE:可转金额
			String def5 = data.getString("def5");
			// NC,SALE:税率
			String def6 = data.getString("def6");
			// NC,SALE:税额
			String def7 = data.getString("def7");
			// NC:不含税金额-SALE:折人民币金额（不含税）
			String def8 = data.getString("def8");

			if (def1 == null || "".equals(def1)) {
				throw new BusinessException("【" + (i + 1) + "】所属项目不能为空");
			}
			if (def2 == null || "".equals(def2)) {
				throw new BusinessException("【" + (i + 1) + "】购买房间不能为空");
			}
			if (def3 == null || "".equals(def3)) {
				throw new BusinessException("【" + (i + 1) + "】业态不能为空");
			}
			if (def4 == null || "".equals(def4)) {
				throw new BusinessException("【" + (i + 1) + "】计税方式不能为空");
			}
			if (def5 == null || "".equals(def5)) {
				throw new BusinessException("【" + (i + 1) + "】可转金额不能为空");
			}
			if (def6 == null || "".equals(def6)) {
				throw new BusinessException("【" + (i + 1) + "】税率不能为空");
			}
			if (def7 == null || "".equals(def7)) {
				throw new BusinessException("【" + (i + 1) + "】税额不能为空");
			}
			if (def8 == null || "".equals(def8)) {
				throw new BusinessException("【" + (i + 1) + "】折人民币金额（不含税）不能为空");
			}
//			if (scomment == null || "".equals(scomment)) {
//				throw new BusinessException("【" + (i + 1) + "】摘要不能为空");
//			}
		}
	}
}
