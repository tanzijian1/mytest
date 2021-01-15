package nc.bs.itf.tools;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.itf.tg.outside.BusinessBillCont;
import nc.vo.cmp.bill.RecBillDetailVO;
import nc.vo.cmp.bill.RecBillVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.erm.costshare.CShareDetailVO;
import nc.vo.gpm.guarantee.prop.AggGuaPropertyVO;
import nc.vo.gpm.guarantee.prop.GuaPropertyVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.Attribute;
import uap.json.JSONArray;
import uap.json.JSONObject;

/**
 * @Description: 外系统传输JSON数据转换成AggVO工具类
 * @version with NC V6.3
 */
public class ItfJsonTools {
	private ItfJsonTools() {
	}	

	@SuppressWarnings("unchecked")
	public static <T extends AggregatedValueObject> T jsonToAggVO (
			Object billdata,Class<? extends AggregatedValueObject> aggVOClass, Class<? extends CircularlyAccessibleValueObject> headVOClass, Class<? extends CircularlyAccessibleValueObject> bodyVOClass)throws Exception{		
		JSONObject data=new JSONObject();	
		if(billdata instanceof String){
			JSONObject jsonObj = new JSONObject((String)billdata);
			data = (JSONObject) jsonObj.get("data");
		}else if(billdata instanceof JSONObject){
			data=(JSONObject)billdata;
		}
		T aggVO = null;
		try {
			if(data == null||StringUtil.isEmptyWithTrim(data.toString())) {
				return null;
			}
			aggVO = (T) aggVOClass.newInstance();
			CircularlyAccessibleValueObject headVO;
			CircularlyAccessibleValueObject[] bodyVOs;
			CircularlyAccessibleValueObject bodyVO;
			JSONObject bodyJsonObj;
			JSONObject head = (JSONObject) data.get("headInfo");
			JSONArray bodyitems = (JSONArray) data.get("bodyInfos");			
			if (head == null || bodyitems == null) {
				return null;
			}
			headVO = headVOClass.newInstance();
			//表头设值
			if(headVO instanceof RecBillVO){
				setRecValue(headVOClass,headVO,head);
			}else{
				setValue(headVOClass,headVO,head);
			}			
			//表体VO
			bodyVOs =(CircularlyAccessibleValueObject[]) Array.newInstance(bodyVOClass,bodyitems.length());
			for (int j = 0; j < bodyitems.length(); j++) {
				bodyJsonObj = (JSONObject) bodyitems.get(j);
				if (bodyJsonObj == null) {
					continue;
				}
				bodyVO = bodyVOClass.newInstance();
				//表体设值
				if(bodyVO instanceof RecBillDetailVO){
					setRecValue(bodyVOClass, bodyVO, bodyJsonObj);
				}else{
					setValue(bodyVOClass, bodyVO, bodyJsonObj);	
				}

				bodyVOs[j] = bodyVO;
			}
			//组装AggVO
			aggVO.setParentVO(headVO);
			if(aggVO instanceof BXVO){
				((nc.vo.ep.bx.BXVO)aggVO).setcShareDetailVo((CShareDetailVO[])bodyVOs);
			}else{
				aggVO.setChildrenVO(bodyVOs);	
			}
		} catch (Exception e) {
			throw new Exception("json参数转换AggVO出错 ！"+ e.getMessage());
		}

		return aggVO;
	}
	/**
	 * VO设值
	 * @param voClass
	 * @param vo
	 * @param jaonObject
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void setValue(Class<? extends CircularlyAccessibleValueObject> voClass,CircularlyAccessibleValueObject vo, JSONObject jaonObject) {
		Field[] headFields = voClass.getFields();
		Iterator<String> it;
		String attributeName;
		Object value;
		it = (Iterator<String>) jaonObject.keys();
		while (it.hasNext()) {
			attributeName = it.next();
			if (StringUtil.isEmptyWithTrim(attributeName)) {
				continue;
			}
			value = jaonObject.get(attributeName);
			if (value == null || StringUtil.isEmptyWithTrim(value.toString())) {
				continue;
			}
			if ("bbje".equalsIgnoreCase(attributeName)|| "assume_amount".equalsIgnoreCase(attributeName)) {
				vo.setAttributeValue(attributeName,new UFDouble(value.toString()));
			}else if ("rowno".equalsIgnoreCase(attributeName)) {
				vo.setAttributeValue(attributeName,Integer.parseInt(value.toString()));
			}
			else {
				for (int j = 0; j < headFields.length; j++) {
					Field field = headFields[j];
					if (attributeName.equalsIgnoreCase(headFields[j].getName())) {
						Class type = field.getType();
						if (type == String.class) {
							vo.setAttributeValue(attributeName, value);
						} else if (type == Integer.class) {
							vo.setAttributeValue(attributeName,
									Integer.parseInt(value.toString()));
						} else if (type == UFDouble.class) {
							vo.setAttributeValue(attributeName, new UFDouble(
									value.toString()));
						} else if (type == UFBoolean.class) {
							vo.setAttributeValue(attributeName, new UFBoolean(
									value.toString()));
						} else if (type == UFDate.class) {
							vo.setAttributeValue(attributeName, new UFDate(
									value.toString()));
						} else if (type == UFDateTime.class) {							
							vo.setAttributeValue(attributeName, new UFDateTime(
									value.toString()));															
						} else {
							vo.setAttributeValue(attributeName, value);
						}
						break;
					}
				}
			}
		}
		//VO状态
		vo.setStatus(VOStatus.NEW);
	}
	/**
	 * 内转单VO设值
	 * @param voClass
	 * @param vo
	 * @param jaonObject
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings({ "unchecked"})
	protected static void setRecValue(Class<? extends CircularlyAccessibleValueObject> voClass,CircularlyAccessibleValueObject vo, JSONObject jaonObject) throws InstantiationException, IllegalAccessException {
		CircularlyAccessibleValueObject recvo;
		recvo=voClass.newInstance();				
		nc.vo.pubapp.pattern.model.meta.entity.vo.ModelVOMeta model =
				(nc.vo.pubapp.pattern.model.meta.entity.vo.ModelVOMeta)((SuperVO)recvo).getMetaData();
		IAttributeMeta[] iMeta = model.getAttributes();		
		Iterator<String> it;
		String attributeName;
		Object value;
		it = (Iterator<String>) jaonObject.keys();
		while (it.hasNext()) {
			attributeName = it.next();
			if (StringUtil.isEmptyWithTrim(attributeName)) {
				continue;
			}
			value = jaonObject.get(attributeName);
			if (value == null || StringUtil.isEmptyWithTrim(value.toString())) {
				continue;
			}
			for (int j = 0; j < iMeta.length; j++) {													
				Attribute field=(Attribute)iMeta[j];
				String  attriname=field.getName();
				nc.vo.pub.JavaType type=field.getJavaType();												
				if (attributeName.equalsIgnoreCase(attriname.trim())) {						
					if (type.name().equals("String")) {
						vo.setAttributeValue(attributeName, value);
					} else if (type.name().equals("Integer")) {
						vo.setAttributeValue(attributeName,
								Integer.parseInt(value.toString()));
					} else if (type.name().equals("UFDouble")) {
						vo.setAttributeValue(attributeName, new UFDouble(
								value.toString()));
					} else if (type.name().equals("UFBoolean")) {
						vo.setAttributeValue(attributeName, new UFBoolean(
								value.toString()));
					} else if (type.name().equals("UFDate")) {
						vo.setAttributeValue(attributeName, new UFDate(
								value.toString()));
					} else if (type.name().equals("UFDateTime")) {
						vo.setAttributeValue(attributeName, new UFDateTime(
								value.toString()));
					} else {
						vo.setAttributeValue(attributeName, value);
					}
					break;
				}
			}
		}
		//VO状态
		vo.setStatus(VOStatus.NEW);
	}

}
