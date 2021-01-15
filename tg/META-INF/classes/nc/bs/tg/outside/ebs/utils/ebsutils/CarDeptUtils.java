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
 * �������ŵ���
 * @author acer
 *
 */
public class CarDeptUtils extends EBSBillUtils{

	static CarDeptUtils utils;

	public static CarDeptUtils getUtils() {
		if (utils == null) {
			utils = new CarDeptUtils();
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
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		
		try {
//			NCObject[] docVO = (NCObject[]) getHeadVO(
//					DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
//					+ "'");
//			Collection<DefdocVO> docVO = getBaseDAO()
//					.retrieveByClause(DefdocVO.class,
//							"isnull(dr,0)=0 and pk_defdoclist = '"+DefdocUtils.getUtils().getDefdoclistByCode("ys008")+"'");
			List<Map> docVOList = DefdocUtils.getUtils().getBdDefdoc("ys008",(String) value.get("starttime"),(String) value.get("endtime"));
//			if (docVO != null&&docVO.size()>0) {
//				for (DefdocVO defdocVO : docVO) {
//					Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
//					postdata.put("pk_defdoc",defdocVO.getPk_defdoc());//�Զ��嵵������
//					postdata.put("code",defdocVO.getCode());//�Զ��嵵������
//					postdata.put("name", defdocVO.getName());//�Զ��嵵������
//					postdata.put("enablestate", defdocVO.getEnablestate());//����״̬
//					postdata.put("memo", defdocVO.getMemo());//��ע
//					if(StringUtils.isNotBlank(defdocVO.getPid())&&!"~".equals(defdocVO.getPid())){
//						postdata.put("pk_parent", defdocVO.getPid());//�ϼ���������
//						postdata.put("parent_code", DefdocUtils.getUtils().getDefdoc(defdocVO.getPid()).get("code"));//�ϼ���������
//						postdata.put("parent_name", DefdocUtils.getUtils().getDefdoc(defdocVO.getPid()).get("name"));//�ϼ���������
//					}else{
//						postdata.put("pk_parent", defdocVO.getPid());//�ϼ���������
//						postdata.put("parent_code", null);//�ϼ���������
//						postdata.put("parent_name", null);//�ϼ���������
//					}
//					if(StringUtils.isNotBlank(defdocVO.getPk_defdoclist())&&!"~".equals(defdocVO.getPk_defdoclist())){
//						postdata.put("pk_defdoclist", defdocVO.getPk_defdoclist());//�����б�����
//						postdata.put("defdoclist_code", DefdocUtils.getUtils().getDefdocList(defdocVO.getPk_defdoclist()).get("code"));//�����б����
//						postdata.put("defdoclist_name", DefdocUtils.getUtils().getDefdocList(defdocVO.getPk_defdoclist()).get("name"));//�����б�����
//					}else{
//						postdata.put("pk_defdoclist", defdocVO.getPk_defdoclist());//�����б�����
//						postdata.put("defdoclist_code", null);//�����б����
//						postdata.put("defdoclist_name", null);//�����б�����
//					}
//					list.add(postdata);
//				}
//			}
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
			JSONArray jsonarr = JSONArray.fromObject(docVOList);
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
