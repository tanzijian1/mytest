package nc.bs.tg.outside.ebs.utils.ebsutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import net.sf.json.JSONArray;

/**
 * Ԥ���Ŀ����
 * @author acer
 *
 */
public class BudgetSubjectUtils extends EBSBillUtils{

	static BudgetSubjectUtils utils;

	public static BudgetSubjectUtils getUtils() {
		if (utils == null) {
			utils = new BudgetSubjectUtils();
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
		InvocationInfoProxy.getInstance().setGroupId(AppContext.getInstance()
				.getPkGroup());
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
//		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		
		Map<String,String> refMap = new HashMap<String,String>();
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		
		try {
//			NCObject[] docVO = (NCObject[]) getHeadVO(
//					DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
//					+ "'");
			
			List<Map> docVO = DefdocUtils.getUtils().getBudgetSubject((String) value.get("starttime"),(String) value.get("endtime"));
			if (docVO != null&&docVO.size()>0) {
				for (Map map : docVO) {
					Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
					postdata.put("pk_org",map.get("pk_org"));//������֯
					postdata.put("pk_obj",map.get("pk_obj"));//��Ŀ����
					postdata.put("objcode",map.get("objcode"));//��Ŀ����
					postdata.put("objname",map.get("objname"));//��Ŀ����
					if(StringUtils.isNotBlank((String) map.get("pk_parent"))&&!"~".equals(map.get("pk_parent"))){
						postdata.put("pk_parent",map.get("pk_parent"));//�ϼ���Ŀ����
						postdata.put("parent_code", map.get("parent_code"));//�ϼ���Ŀ����
						postdata.put("parent_name", map.get("parent_name"));//�ϼ���Ŀ����
					}else{
						postdata.put("pk_parent",null);//�ϼ���Ŀ����
						postdata.put("parent_code", null);//�ϼ���Ŀ����
						postdata.put("parent_name", null);//�ϼ���Ŀ����
					}
					if(StringUtils.isNotBlank((String) map.get("pk_inoutbusiclass"))&&!"~".equals(map.get("pk_inoutbusiclass"))){
						postdata.put("pk_inoutbusiclass", map.get("pk_inoutbusiclass"));//��֧��Ŀ����
						postdata.put("inoutbusiclass_code", map.get("inoutbusiclass_code"));//��֧��Ŀ����
						postdata.put("inoutbusiclass_name", map.get("inoutbusiclass_name"));//��֧��Ŀ����
					}else{
						postdata.put("pk_inoutbusiclass", map.get("pk_inoutbusiclass"));//��֧��Ŀ����
						postdata.put("inoutbusiclass_code", null);//��֧��Ŀ����
						postdata.put("inoutbusiclass_name", null);//��֧��Ŀ����
					}
					postdata.put("enablestate", map.get("enablestate"));//����״̬[1:δ����,2:������,3:ͣ��]
					if(map.get("issystem")!=null){
						postdata.put("issystem", map.get("issystem").equals("Y")?"��":"��");//�Ƿ�Ԥ��
					}else{
						postdata.put("issystem", "��");//�Ƿ�Ԥ��
					}
					postdata.put("def1", map.get("def1"));
					postdata.put("def2", map.get("def2"));
					postdata.put("def3", map.get("def3"));
					list.add(postdata);
				}
			}
//				nc.vo.bd.inoutbusiclass.InoutBusiClassVO[] defdocVO = docVO.toArray(new nc.vo.bd.inoutbusiclass.InoutBusiClassVO[0]);
//				defdocVO[0].setStatus(VOStatus.UPDATED);
//				headVO.setName((String) value.get("name"));
//				headVO.setCode((String) value.get("code"));
//				defdocService.updateSupplierVO(defdocVO[0],true);
//				throw new BusinessException("��"
//						+ billkey
//						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
//						+ docVO[0].getAttributeValue(
//								DefdocVO.CODE) + "��,�����ظ��ϴ�!");
			JSONArray jsonarr = JSONArray.fromObject(list);
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
}
