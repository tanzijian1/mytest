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
 * 收支项目档案
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
//		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		
		try {
//			NCObject[] docVO = (NCObject[]) getHeadVO(
//					DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
//					+ "'");
			List<Map> docVO = DefdocUtils.getUtils().getBdInoutbusi((String) value.get("starttime"),(String) value.get("endtime"));//获取收支项目视图
//			Collection<nc.vo.bd.inoutbusiclass.InoutBusiClassVO> docVO = getBaseDAO()
//					.retrieveByClause(nc.vo.bd.inoutbusiclass.InoutBusiClassVO.class,
//							"isnull(dr,0)=0 ");
//			if (docVO != null&&docVO.size()>0) {
//				for (nc.vo.bd.inoutbusiclass.InoutBusiClassVO inoutBusiClassVO : docVO) {
//					Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
//					postdata.put("bd_inoutbusiclass",inoutBusiClassVO.getPk_inoutbusiclass());//收支项目pk
//					postdata.put("inoutbusiclass_code",inoutBusiClassVO.getCode());//收支项目编码
//					postdata.put("inoutbusiclass_name", inoutBusiClassVO.getName());//收支项目名称
//					postdata.put("enablestate", inoutBusiClassVO.getEnablestate());//启用状态
//					postdata.put("memo", inoutBusiClassVO.getMemo());//备注
//					if(StringUtils.isNotBlank(inoutBusiClassVO.getPk_parent())&&!"~".equals(inoutBusiClassVO.getPk_parent())){
//						postdata.put("pk_parent", inoutBusiClassVO.getPk_parent());//上级收支项目主键
//						postdata.put("parent_code", DefdocUtils.getUtils().getInoutbusiClassParent(inoutBusiClassVO.getPk_parent()).get("code"));//上级收支项目编码
//						postdata.put("parent_name", DefdocUtils.getUtils().getInoutbusiClassParent(inoutBusiClassVO.getPk_parent()).get("name"));//上级收支项目名称
//					}else{
//						postdata.put("pk_parent", inoutBusiClassVO.getPk_parent());//上级收支项目主键
//						postdata.put("parent_code", null);//上级收支项目编码
//						postdata.put("parent_name", null);//上级收支项目名称
//					}
//					postdata.put("def1", "Y".equals(inoutBusiClassVO.getDef1())?"是":"否");//是否利息项目
//					postdata.put("def2", "Y".equals(inoutBusiClassVO.getDef2())?"是":"否");//是否合同理财
//					postdata.put("def3", "Y".equals(inoutBusiClassVO.getDef3())?"是":"否");//是否不参与导出
//					postdata.put("def4", "Y".equals(inoutBusiClassVO.getDef4())?"是":"否");//是否无需填写
//					list.add(postdata);
//				}
//			}
			JSONArray jsonarr = JSONArray.fromObject(docVO);
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
