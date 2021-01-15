package nc.impl.tg.apply;

import java.util.HashMap;
import java.util.List;

import nc.itf.tg.apply.IApplyBillSyncService;
import nc.vo.tg.apply.ApplyBillBodyVO;
import nc.vo.tg.apply.ApplyBillHeadVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Business_b;
import nc.vo.tgfn.paymentrequest.Payrequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ApplyBillSyncServiceImpl implements IApplyBillSyncService {


	@Override
	public String syncBillForEBS(HashMap<String, Object> value) {
		// TODO 自动生成的方法存根
		
		try {
			JSONObject jsonData=  (JSONObject) value.get("data");
			JSON jsonhead=  (JSON) jsonData.get("applyHeadVO");
			String jsonbody=  jsonData.getString("applyBodyVO");
			ApplyBillHeadVO headVO=JSONObject.toJavaObject(jsonhead, ApplyBillHeadVO.class);
			List<ApplyBillBodyVO> bodyVOs=JSONObject.parseArray(jsonbody, ApplyBillBodyVO.class);
//			headVO.set
			AggPayrequest aggvo=new AggPayrequest();
			Payrequest save_headVO=new Payrequest();
			Business_b save_bodyVO=new Business_b();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}

}
