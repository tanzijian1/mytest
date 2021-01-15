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
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import net.sf.json.JSONArray;

public class BankAccountUtils extends EBSBillUtils {

	static BankAccountUtils utils;

	public static BankAccountUtils getUtils() {
		if (utils == null) {
			utils = new BankAccountUtils();
		}
		return utils;
	}

	/**
	 * 银行账户查询类
	 * 
	 * @param value
	 * @param dectype
	 * @param srcsystem
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		Map<String, String> refMap = new HashMap<String, String>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			// NCObject[] docVO = (NCObject[]) getHeadVO(
			// DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
			// + "'");

			List<Map> docVO = DefdocUtils.getUtils().getBankaccbas(value);
			
			if (docVO != null && docVO.size() > 0) {
				for (Map map : docVO) {
					Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
					
					postdata.put("pk_bankaccbas", map.get("pk_bankaccbas"));// 银行账户主键
					postdata.put("bankaccount_name", map.get("bankaccount_name"));// 银行账户名称
					postdata.put("bankaccount_code", map.get("bankaccount_code"));// 银行账户编码
					
					postdata.put("accnum", map.get("accnum"));// 账号
					postdata.put("accname", map.get("accname"));// 户名
					
					postdata.put("pk_org", map.get("pk_org"));// 组织
					postdata.put("org_code", map.get("org_code"));// 组织编码
					postdata.put("org_name", map.get("org_name"));//组织名称
					
					postdata.put("pk_bankdoc", map.get("pk_bankdoc"));// 开户银行主键
					postdata.put("bankdoc_code", map.get("bankdoc_code"));// 开户银行编码
					postdata.put("bankdoc_name", map.get("bankdoc_name"));//开户银行名称
					
					postdata.put("pk_banktype", map.get("pk_banktype"));// 银行类别主键
					postdata.put("banktype_code", map.get("banktype_code"));// 开户银行编码
					postdata.put("banktype_name", map.get("banktype_name"));//开户银行名称
					
					
					postdata.put("financeorg", map.get("financeorg"));// 开户单位主键
					postdata.put("financeorg_code", map.get("financeorg_code"));// 开户单位编码
					postdata.put("financeorg_name", map.get("financeorg_name"));//开户单位名称
					
					postdata.put("controlorg", map.get("controlorg"));// 核算归属组织主键
					postdata.put("controlorg_code", map.get("controlorg_code"));// 核算归属组织编码
					postdata.put("controlorg_name", map.get("controlorg_name"));//核算归属组织名称
					
					postdata.put("ts", map.get("ts"));//时间戳
					
					
					list.add(postdata);
				}
			}

			JSONArray jsonarr = JSONArray.fromObject(list);
			String json = jsonarr.toString();

			refMap.put("msg", "," + "操作完成!");
			refMap.put("data", json);
			// null, eParam);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return JSON.toJSONString(refMap);

	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggPayrequest onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {
		AggPayrequest aggvo = new AggPayrequest();
		JSON headjson = (JSON) value.get("headInfo");
		JSON bodyjson = (JSON) value.get("itemInfo");

		return aggvo;
	}

}
