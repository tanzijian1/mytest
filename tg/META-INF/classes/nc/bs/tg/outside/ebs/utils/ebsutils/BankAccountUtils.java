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
	 * �����˻���ѯ��
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
					Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
					
					postdata.put("pk_bankaccbas", map.get("pk_bankaccbas"));// �����˻�����
					postdata.put("bankaccount_name", map.get("bankaccount_name"));// �����˻�����
					postdata.put("bankaccount_code", map.get("bankaccount_code"));// �����˻�����
					
					postdata.put("accnum", map.get("accnum"));// �˺�
					postdata.put("accname", map.get("accname"));// ����
					
					postdata.put("pk_org", map.get("pk_org"));// ��֯
					postdata.put("org_code", map.get("org_code"));// ��֯����
					postdata.put("org_name", map.get("org_name"));//��֯����
					
					postdata.put("pk_bankdoc", map.get("pk_bankdoc"));// ������������
					postdata.put("bankdoc_code", map.get("bankdoc_code"));// �������б���
					postdata.put("bankdoc_name", map.get("bankdoc_name"));//������������
					
					postdata.put("pk_banktype", map.get("pk_banktype"));// �����������
					postdata.put("banktype_code", map.get("banktype_code"));// �������б���
					postdata.put("banktype_name", map.get("banktype_name"));//������������
					
					
					postdata.put("financeorg", map.get("financeorg"));// ������λ����
					postdata.put("financeorg_code", map.get("financeorg_code"));// ������λ����
					postdata.put("financeorg_name", map.get("financeorg_name"));//������λ����
					
					postdata.put("controlorg", map.get("controlorg"));// ���������֯����
					postdata.put("controlorg_code", map.get("controlorg_code"));// ���������֯����
					postdata.put("controlorg_name", map.get("controlorg_name"));//���������֯����
					
					postdata.put("ts", map.get("ts"));//ʱ���
					
					
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
