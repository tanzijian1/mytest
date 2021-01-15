	package nc.bs.tg.outside.sale.utils.salessystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.InoutbusiclassUtils;
import nc.bs.tg.outside.sale.servlet.SaleBillServlet;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import net.sf.json.JSONArray;

import com.alibaba.fastjson.JSON;

public class SettlementMethodUtils extends SaleBillUtils {
	static SettlementMethodUtils utils;

	public static SettlementMethodUtils getUtils() {
		if (utils == null) {
			utils = new SettlementMethodUtils();
		}
		return utils;
	}

	/**
	 * ���㷽ʽ��ѯ��
	 * 
	 * @param value
	 * @param dectype
	 * @param srcsystem
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		Map<String, String> refMap = new HashMap<String, String>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String starttime =null;
		String endtime = null;
		if(!"".equals(value.get("starttime"))&&value.get("starttime")!=null){
			starttime = (String) value.get("starttime");
		}else{
			starttime = "1900-01-01 00:00:00";
		}
		
		if(!"".equals(value.get("endtime"))&&value.get("endtime")!=null){
			endtime = (String) value.get("endtime");
		}else{
			endtime = "2500-01-01 00:00:00";
		}
		try {

			Collection<nc.vo.bd.balatype.BalaTypeVO> btVO = getBaseDAO()
					.retrieveByClause(nc.vo.bd.balatype.BalaTypeVO.class,
							"isnull(dr,0)=0 and ts>='"+starttime+"' and ts<='"+endtime+"'");
			if (btVO != null && btVO.size() > 0) {
				for (nc.vo.bd.balatype.BalaTypeVO balaTypeVO : btVO) {
					Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
					postdata.put("enablestate", balaTypeVO.getEnablestate());// ����״̬
					postdata.put("pk_balatype", balaTypeVO.getPk_balatype());// ���㷽ʽ����
					postdata.put("name", balaTypeVO.getName());// ���㷽ʽ����
					postdata.put("code", balaTypeVO.getCode());// ���㷽ʽ����
					String ts = null;
					if(balaTypeVO.getTs()!=null){
						ts=balaTypeVO.getTs().toString();
					}
					postdata.put("ts",ts );// ���㷽ʽʱ���
					list.add(postdata);
				}
			}
			JSONArray jsonarr = JSONArray.fromObject(list);
			String json = jsonarr.toString();

			refMap.put("msg", "," + "�������!");
			refMap.put("data", json);
			// null, eParam);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return JSON.toJSONString(refMap);

	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
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
