package nc.bs.tg.outside.ebs.utils.ebsutils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.tobankdoc.InsertBankdoc;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.EBSBankdocVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import net.sf.json.JSONArray;

import com.alibaba.fastjson.JSON;

public class BankArchivesUtils extends EBSBillUtils {

	static BankArchivesUtils utils;

	public static BankArchivesUtils getUtils() {
		if (utils == null) {
			utils = new BankArchivesUtils();
		}
		return utils;
	}

	/**
	 * 银行档案保存类
	 * 
	 * @param value
	 * @param dectype
	 * @param srcsystem
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srcsystem) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		JSONArray jsonarr = JSONArray.fromObject(value.get("data"));
		Map<String,String> refMap = new HashMap<String,String>();
		Collection<EBSBankdocVO> docvos = JSONArray.toCollection(jsonarr,
				EBSBankdocVO.class);

		for (EBSBankdocVO ebsBankdocVO : docvos) {
			
			InsertBankdoc ban = NCLocator.getInstance().lookup(
					InsertBankdoc.class);
			refMap = ban.executeTask(ebsBankdocVO, srcsystem, dectype);

			
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
