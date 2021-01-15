package nc.bs.tg.outside.img.utils.contractcostcashout;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.img.utils.IMGBillUtils;
import nc.itf.tg.outside.IMGBillCont;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CcCashoutUtils extends IMGBillUtils {
	static CcCashoutUtils utils;

	public static CcCashoutUtils getUtils() {
		if (utils == null) {
			utils = new CcCashoutUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getIMGUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		// 解析json参数,获取表头数据
		JSONObject jsonInfo = (JSONObject) value.get("Info");
		// 单据号，获取唯一字段值
		String imgNo = jsonInfo.getString("djbh");// 影像系统业务单据号

		// 影像系统业务单据单据号做队列控制和日志输出
		String billkey = IMGBillCont.getBillNameMap().get(billtype) + ":"
				+ imgNo;
		/*String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;*/

		if(jsonInfo.getString("zyx25") == null || "".equals(jsonInfo.getString("zyx25"))){
			throw new BusinessException("发票验伪状态不能为空");
		}
		if(jsonInfo.getString("djbh") == null || "".equals(jsonInfo.getString("djbh"))){
			throw new BusinessException("单据编号不能为空");
		}
		
		// 检查影像系统业务单据是否存在**************
		BXVO aggVO = (BXVO) getBillVO(
				BXVO.class, "isnull(dr,0)=0 and djbh = '" + imgNo
						+ "'");
		
		if (aggVO == null) {
			throw new BusinessException("【" + billkey + "】,NC不存在对应的业务单据【"
					+ imgNo + "】,请检查确认单据是否存在");
		}

		BPMBillUtils.addBillQueue(billkey);// 增加队列处理***************
		try {
			JSONObject jsonObj = (JSONObject) value.get("Info");
			if(jsonObj == null || jsonObj.size() == 0){
				throw new BusinessException("单据信息不存在，请检查确认信息");
			}
			BXHeaderVO vo = (BXHeaderVO) aggVO.getParentVO();
			vo.setZyx14(jsonObj.getString("zyx14")); // 指定付款函会否符合
			vo.setZyx15(jsonObj.getString("zyx15")); // 指定付款函符合原因
			vo.setZyx18(jsonObj.getString("zyx18")); // 发票OCR识别金额
			vo.setZyx25(jsonObj.getString("zyx25")); // 发票验伪状态
			vo.setZyx25(jsonObj.getString("zyx30")); // 发票验伪失败原因
			int flag = getBaseDAO().updateVO(vo);
			
			if(flag == 0){
				throw new BusinessException("保存失败，请检查单据信息"); 
			}
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("billid", vo.getPrimaryKey());
			dataMap.put("billno", vo.getDjbh());
			//**************************
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billkey);
		}
	}
}
