package nc.bs.tg.outside.ebs.utils.ebsutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import net.sf.json.JSONArray;

/**
 * ��֧��Ŀ����
 * @author acer
 *
 */
public class InoutbusiclassUtils extends EBSBillUtils{

	static InoutbusiclassUtils utils;

	public static InoutbusiclassUtils getUtils() {
		if (utils == null) {
			utils = new InoutbusiclassUtils();
		}
		return utils;
	}
	
	/**
	 * �Զ��嵵����ȡ������
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
		
		Map<String,String> refMap = new HashMap<String,String>();
//		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		
		try {
//			NCObject[] docVO = (NCObject[]) getHeadVO(
//					DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
//					+ "'");
			List<Map> docVO = DefdocUtils.getUtils().getBdInoutbusi((String) value.get("starttime"),(String) value.get("endtime"));//��ȡ��֧��Ŀ��ͼ
//			Collection<nc.vo.bd.inoutbusiclass.InoutBusiClassVO> docVO = getBaseDAO()
//					.retrieveByClause(nc.vo.bd.inoutbusiclass.InoutBusiClassVO.class,
//							"isnull(dr,0)=0 ");
//			if (docVO != null&&docVO.size()>0) {
//				for (nc.vo.bd.inoutbusiclass.InoutBusiClassVO inoutBusiClassVO : docVO) {
//					Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
//					postdata.put("bd_inoutbusiclass",inoutBusiClassVO.getPk_inoutbusiclass());//��֧��Ŀpk
//					postdata.put("inoutbusiclass_code",inoutBusiClassVO.getCode());//��֧��Ŀ����
//					postdata.put("inoutbusiclass_name", inoutBusiClassVO.getName());//��֧��Ŀ����
//					postdata.put("enablestate", inoutBusiClassVO.getEnablestate());//����״̬
//					postdata.put("memo", inoutBusiClassVO.getMemo());//��ע
//					if(StringUtils.isNotBlank(inoutBusiClassVO.getPk_parent())&&!"~".equals(inoutBusiClassVO.getPk_parent())){
//						postdata.put("pk_parent", inoutBusiClassVO.getPk_parent());//�ϼ���֧��Ŀ����
//						postdata.put("parent_code", DefdocUtils.getUtils().getInoutbusiClassParent(inoutBusiClassVO.getPk_parent()).get("code"));//�ϼ���֧��Ŀ����
//						postdata.put("parent_name", DefdocUtils.getUtils().getInoutbusiClassParent(inoutBusiClassVO.getPk_parent()).get("name"));//�ϼ���֧��Ŀ����
//					}else{
//						postdata.put("pk_parent", inoutBusiClassVO.getPk_parent());//�ϼ���֧��Ŀ����
//						postdata.put("parent_code", null);//�ϼ���֧��Ŀ����
//						postdata.put("parent_name", null);//�ϼ���֧��Ŀ����
//					}
//					postdata.put("def1", "Y".equals(inoutBusiClassVO.getDef1())?"��":"��");//�Ƿ���Ϣ��Ŀ
//					postdata.put("def2", "Y".equals(inoutBusiClassVO.getDef2())?"��":"��");//�Ƿ��ͬ���
//					postdata.put("def3", "Y".equals(inoutBusiClassVO.getDef3())?"��":"��");//�Ƿ񲻲��뵼��
//					postdata.put("def4", "Y".equals(inoutBusiClassVO.getDef4())?"��":"��");//�Ƿ�������д
//					list.add(postdata);
//				}
//			}
			JSONArray jsonarr = JSONArray.fromObject(docVO);
			String json = jsonarr.toString();
			
			refMap.put("msg", "�������!");
			refMap.put("data", json);
//					null, eParam);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(),
					e);
		} 
		return JSON.toJSONString(refMap) ;

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
