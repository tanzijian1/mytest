package nc.bs.tg.outside.sale.utils.exhousetransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.tgfn.exhousetransferbill.ExhousetransferbillBVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 换房转账单(FN19)
 * @author ln
 *
 */
public class ExHouseTransferBillUtils extends SaleBillUtils {
	static ExHouseTransferBillUtils utils;

	public static ExHouseTransferBillUtils getUtils() {
		if (utils == null) {
			utils = new ExHouseTransferBillUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		// 解析json参数,获取表头数据
		JSONObject headJson = (JSONObject) value.get("headInfo");
		// Saleid 按实际存入信息位置进行变更
		String saleid = headJson.getString("def1");// 销售系统业务单据ID
		String saleno = headJson.getString("def2");// 销售系统业务单据单据号

		// 销售系统业务单据ID和销售系统业务单据单据号做队列控制和日志输出
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		// 检查销售系统业务单据ID唯一性
		AggExhousetransferbillHVO aggVO = (AggExhousetransferbillHVO) getBillVO(
				AggExhousetransferbillHVO.class, "isnull(dr,0)=0 and def1 = '" + saleid
						+ "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String) aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}

		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			// 转换数据为VO
			//*********************************
			ExHouseTransferBillConvertor convertor = new ExHouseTransferBillConvertor();
			//默认集团PK
			convertor.setDefaultGroup("000112100000000005FD");
			// 配置表体key与名称映射
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("exhousetransferbillBVO", "换房转账单表体");
			convertor.setBVOKeyName(bVOKeyName);
			
			// 配置表头key与名称映射
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("exhousetransferbillHVO", "换房转账单表头");
			convertor.setHVOKeyName(hVOKeyName);
			
			//表头空值校验字段
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hExhousetransferbillHVOKeyName= new HashMap<String, String>();
			hExhousetransferbillHVOKeyName.put("def1", "销售系统单据ID");
			hExhousetransferbillHVOKeyName.put("def2", "销售系统单号");
			hExhousetransferbillHVOKeyName.put("def3", "影像编码");
			hExhousetransferbillHVOKeyName.put("def4", "影像状态");
			hExhousetransferbillHVOKeyName.put("billdate", "单据日期");
			hExhousetransferbillHVOKeyName.put("pk_org", "财务组织");
			hValidatedKeyName.put("exhousetransferbillHVO", hExhousetransferbillHVOKeyName);
			
			//表体空值校验字段
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> bExhousetransferbillBVOKeyName = new HashMap<String, String>();
//			bExhousetransferbillBVOKeyName.put("scomment", "摘要");
			bExhousetransferbillBVOKeyName.put("def1", "所属项目");
//			bExhousetransferbillBVOKeyName.put("def2", "购买房间");
			bExhousetransferbillBVOKeyName.put("def3", "款项类型");
			bExhousetransferbillBVOKeyName.put("def4", "款项名称");
			bExhousetransferbillBVOKeyName.put("def5", "业态");
			bExhousetransferbillBVOKeyName.put("def6", "计税方式");
			bExhousetransferbillBVOKeyName.put("def7", "税率");
			bExhousetransferbillBVOKeyName.put("def8", "税额");
			bExhousetransferbillBVOKeyName.put("def9", "折人民币金额（不含税）");
			bValidatedKeyName.put("exhousetransferbillBVO", bExhousetransferbillBVOKeyName);
			
			//参照类型字段
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("exhousetransferbillHVO-pk_org");
			refKeys.add("exhousetransferbillHVO-billmaker");
			
			convertor.sethValidatedKeyName(hValidatedKeyName);
			convertor.setbValidatedKeyName(bValidatedKeyName);
			convertor.setRefKeys(refKeys);
			
			//*********************************
			AggExhousetransferbillHVO billvo = (AggExhousetransferbillHVO) convertor.castToBill(value, AggExhousetransferbillHVO.class,aggVO);
			
			//设置NC默认信息
//			billvo.getParentVO().setTranstype("FN19");
			billvo.getParentVO().setBilltype("FN19");
			billvo.getParentVO().setBillstatus(-1);
			billvo.getParentVO().setApprovestatus(-1);
			billvo.getParentVO().setEffectstatus(0);
			billvo.getParentVO().setDef10(getUserByPsondoc(billvo.getParentVO().getDef10()));//销售审批
			billvo.getParentVO().setTs(new UFDateTime());
			billvo.getParentVO().setDef5(headJson.getString("def5"));//
			billvo.getParentVO().setDef6(headJson.getString("def6"));//
			billvo.getParentVO().setDef7(headJson.getString("def7"));//
			billvo.getParentVO().setDef8(headJson.getString("def8"));//
			billvo.getParentVO().setDef9(headJson.getString("def9"));//
			billvo.getParentVO().setDef15(headJson.getString("def15"));//附件张数
			
			// ***********表头参照设值
			billvo.getParentVO().setPk_org(convertor.getRefAttributePk(
					"exhousetransferbillHVO-pk_org",
					billvo.getParentVO().getPk_org())); // 财务组织
			billvo.getParentVO().setDef11(getBd_cust_supplier(billvo.getParentVO().getDef11()));//包销商
			billvo.getParentVO().setBillmaker(getSaleUserID());; // 制单人（用户）
			// ***********表头参照设值
			//表体参照转化
			SuperVO[] bvos = (SuperVO[]) billvo.getChildrenVO();
			for(int i = 0 ; i < bvos.length ; i++){
				String def1 = getPk_projectByCode(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def1")));
				billvo.getChildrenVO()[i].setAttributeValue("def1", def1);
				
				String def2 = getPk_projectByCode(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def2")));
				billvo.getChildrenVO()[i].setAttributeValue("def2", def2);
				
				String def3 = getPk_defdocByCode(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def3")));
				billvo.getChildrenVO()[i].setAttributeValue("def3", def3);
				
				String def4 = getPk_defdocByCode(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def4")));
				billvo.getChildrenVO()[i].setAttributeValue("def4", def4);
				
				String def5 = getPk_defdocByName(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def5")));
				billvo.getChildrenVO()[i].setAttributeValue("def5", def5);
				
				String def6 = getPk_defdocByName(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def6")));
				billvo.getChildrenVO()[i].setAttributeValue("def6", def6);
			}
			
			HashMap<String, String> eParam = new HashMap<String, String>();
			// 持久化工单VO
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN19", null, billvo,
					null, eParam);
			//**************************
			
			AggExhousetransferbillHVO[] billvos = (AggExhousetransferbillHVO[]) obj;
			billvos[0].getParentVO().setTranstypepk(getRefAttributePk("transtype", billvos[0].getParentVO().getTranstype()));
			obj = getPfBusiAction().processAction("SAVE", "FN19", null, billvos[0],
					null, eParam);
			billvos = (AggExhousetransferbillHVO[]) obj;
			getPfBusiAction().processAction("APPROVE", "FN19", null, billvos[0],
					null, eParam);
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
			//**************************
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}
	}

}
