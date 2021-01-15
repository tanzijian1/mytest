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
 * 车辆部门档案
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
	 * 自定义档案获取数据类
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
//					Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
//					postdata.put("pk_defdoc",defdocVO.getPk_defdoc());//自定义档案编码
//					postdata.put("code",defdocVO.getCode());//自定义档案编码
//					postdata.put("name", defdocVO.getName());//自定义档案名称
//					postdata.put("enablestate", defdocVO.getEnablestate());//启用状态
//					postdata.put("memo", defdocVO.getMemo());//备注
//					if(StringUtils.isNotBlank(defdocVO.getPid())&&!"~".equals(defdocVO.getPid())){
//						postdata.put("pk_parent", defdocVO.getPid());//上级档案主键
//						postdata.put("parent_code", DefdocUtils.getUtils().getDefdoc(defdocVO.getPid()).get("code"));//上级档案编码
//						postdata.put("parent_name", DefdocUtils.getUtils().getDefdoc(defdocVO.getPid()).get("name"));//上级档案名称
//					}else{
//						postdata.put("pk_parent", defdocVO.getPid());//上级档案主键
//						postdata.put("parent_code", null);//上级档案编码
//						postdata.put("parent_name", null);//上级档案名称
//					}
//					if(StringUtils.isNotBlank(defdocVO.getPk_defdoclist())&&!"~".equals(defdocVO.getPk_defdoclist())){
//						postdata.put("pk_defdoclist", defdocVO.getPk_defdoclist());//档案列表主键
//						postdata.put("defdoclist_code", DefdocUtils.getUtils().getDefdocList(defdocVO.getPk_defdoclist()).get("code"));//档案列表编码
//						postdata.put("defdoclist_name", DefdocUtils.getUtils().getDefdocList(defdocVO.getPk_defdoclist()).get("name"));//档案列表名称
//					}else{
//						postdata.put("pk_defdoclist", defdocVO.getPk_defdoclist());//档案列表主键
//						postdata.put("defdoclist_code", null);//档案列表编码
//						postdata.put("defdoclist_name", null);//档案列表名称
//					}
//					list.add(postdata);
//				}
//			}
//				nc.vo.bd.inoutbusiclass.InoutBusiClassVO[] defdocVO = docVO.toArray(new nc.vo.bd.inoutbusiclass.InoutBusiClassVO[0]);
//				defdocVO[0].setStatus(VOStatus.UPDATED);
//				headVO.setName((String) value.get("name"));
//				headVO.setCode((String) value.get("code"));
//				defdocService.updateSupplierVO(defdocVO[0],true);
//				throw new BusinessException("【"
//						+ billkey
//						+ "】,NC已存在对应的业务单据【"
//						+ docVO[0].getAttributeValue(
//								DefdocVO.CODE) + "】,请勿重复上传!");
			JSONArray jsonarr = JSONArray.fromObject(docVOList);
			String json = jsonarr.toString();
			
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
