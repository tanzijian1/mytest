package nc.bs.tg.outside.ebs.utils.ebsutils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.vo.pub.BusinessException;
import net.sf.json.JSONArray;

/**
 * 个人银行账号查询
 * @author acer
 *
 */
public class PsnBankAccountUtils extends EBSBillUtils{

	static PsnBankAccountUtils utils;

	public static PsnBankAccountUtils getUtils() {
		if (utils == null) {
			utils = new PsnBankAccountUtils();
		}
		return utils;
	}
	
	/**
	 * 个人银行账户
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
//		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		
		Map<String,Object> refMap = new HashMap<String,Object>();
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		String psncode =(String) value.get("psncode");
		if("all".equals(psncode)){
			psncode=null;
		}
		try {
			com.alibaba.fastjson.JSONArray json = DefdocUtils.getUtils().getPsnBankacc(psncode);
			refMap.put("msg", "操作完成!");
			refMap.put("data", json);
//					null, eParam);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(),
					e);
		} 
		return JSON.toJSONString(refMap) ;
		

	}
}
