package nc.bs.tg.outside.img.utils.contractcostcashout;

import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.img.utils.IMGBillUtils;
import nc.itf.tg.outside.IMGBillCont;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ImagCallbackutils extends IMGBillUtils {
	static ImagCallbackutils utils;

	public static ImagCallbackutils getUtils() {
		if (utils == null) {
			utils = new ImagCallbackutils();
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
		if (jsonInfo == null || jsonInfo.size() == 0) {
			throw new BusinessException("单据信息不存在，请检查确认信息");
		}
		// 单据号，获取唯一字段值
		String imgNo = jsonInfo.getString("barcode");// 影像系统业务单据号
		String status = jsonInfo.getString("imagestatus");// 影像系统状态
		// 影像系统业务单据单据号做队列控制和日志输出
		String billkey = IMGBillCont.getBillNameMap().get(billtype) + ":"
				+ imgNo;
		BPMBillUtils.addBillQueue(billkey);// 增加队列处理***************
		try {
			String sql = null;
			if("264X".equals(billtype)){
				sql = "select pk_jkbx  from er_bxzb where nvl(dr,0)=0 and zyx16='"+imgNo+"'";
			} else if("267X-Cxx-001".equals(billtype)){
				sql = "select pk_addbill from yer_fillbill where nvl(dr,0)=0 and imagcode='"+imgNo+"'";
			}
			List<String> pks = (List<String>) getBaseDAO().executeQuery(sql, new ColumnListProcessor());
			if (pks.isEmpty())
				throw new BusinessException("影像编码未关联NC数据");
			for (String str : pks) {
				if("264X".equals(billtype)){
					sql = "update er_bxzb set zyx17='" + status
							+ "' where pk_jkbx='" + str + "'";
				} else if("267X-Cxx-001".equals(billtype)){
					sql = "update yer_fillbill set imagstatus='" + status
							+ "' where pk_addbill='" + str + "'";
				}
				getBaseDAO().executeUpdate(sql);
			}
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billkey);
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("msg", "接口调用成功");
		return JSON.toJSONString(map);
	}
}
