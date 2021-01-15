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
 * 预算科目档案
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
	 * 自定义档案获取数据类
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
					Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
					postdata.put("pk_org",map.get("pk_org"));//创建组织
					postdata.put("pk_obj",map.get("pk_obj"));//科目主键
					postdata.put("objcode",map.get("objcode"));//科目编码
					postdata.put("objname",map.get("objname"));//科目名称
					if(StringUtils.isNotBlank((String) map.get("pk_parent"))&&!"~".equals(map.get("pk_parent"))){
						postdata.put("pk_parent",map.get("pk_parent"));//上级科目主键
						postdata.put("parent_code", map.get("parent_code"));//上级科目编码
						postdata.put("parent_name", map.get("parent_name"));//上级科目名称
					}else{
						postdata.put("pk_parent",null);//上级科目主键
						postdata.put("parent_code", null);//上级科目编码
						postdata.put("parent_name", null);//上级科目名称
					}
					if(StringUtils.isNotBlank((String) map.get("pk_inoutbusiclass"))&&!"~".equals(map.get("pk_inoutbusiclass"))){
						postdata.put("pk_inoutbusiclass", map.get("pk_inoutbusiclass"));//收支项目主键
						postdata.put("inoutbusiclass_code", map.get("inoutbusiclass_code"));//收支项目编码
						postdata.put("inoutbusiclass_name", map.get("inoutbusiclass_name"));//收支项目名称
					}else{
						postdata.put("pk_inoutbusiclass", map.get("pk_inoutbusiclass"));//收支项目主键
						postdata.put("inoutbusiclass_code", null);//收支项目编码
						postdata.put("inoutbusiclass_name", null);//收支项目名称
					}
					postdata.put("enablestate", map.get("enablestate"));//启用状态[1:未启用,2:已启用,3:停用]
					if(map.get("issystem")!=null){
						postdata.put("issystem", map.get("issystem").equals("Y")?"是":"否");//是否预置
					}else{
						postdata.put("issystem", "否");//是否预置
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
//				throw new BusinessException("【"
//						+ billkey
//						+ "】,NC已存在对应的业务单据【"
//						+ docVO[0].getAttributeValue(
//								DefdocVO.CODE) + "】,请勿重复上传!");
			JSONArray jsonarr = JSONArray.fromObject(list);
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
