/**
 * <p>Title: DefaultConvertor.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月2日 下午4:02:39

 * @version 1.0
 */   

package nc.bs.tg.outside.sale.utils.convertor;   

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.JavaType;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pub.lang.UFTime;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 创建时间：2019年9月2日 下午4:02:39  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.8
 * 文件名称：DefaultConvertor.java  
 * 类说明：  
 */

/**
 * <p>Title: DefaultConvertor<／p>

 * <p>Description: <／p>

 * <p>Company: hanzhi<／p> 

 * @author bobo

 * @date 2019年9月2日 下午4:02:39
 */

public abstract class DefaultConvertor{

	private String headKey;
	private String bodyKey;
	private String defaultGroup;
	
	public AggregatedValueObject castToBill(HashMap<String, Object> value,
			Class<?> aggVOClass) throws BusinessException {


		String errorMsg = null;
		if (value == null || value.size() <= 0) {
			errorMsg = "数据为空，请检查请求参数";
			throw new BusinessException(errorMsg);
		}
		// 获取表头和表体的数据
		JSONObject headObj = (JSONObject) value.get(headKey);
		JSONObject bodyObj = (JSONObject) value.get(bodyKey);

		if (headObj == null || headObj.size() <= 0) {
			errorMsg = "数据异常，必须有表头数据";
			throw new BusinessException(errorMsg);
		}
		// 得到聚合VO与单据元数据
		AbstractBill aggVO = null;
		try {
			aggVO = (AbstractBill) aggVOClass.newInstance();
			// 单据元数据
			IBillMeta billMeta = aggVO.getMetaData();

			IVOMeta parentMeta = billMeta.getParent();
			// 得到表头的class
			Class<?> parentClass = billMeta.getVOClass(parentMeta);

			IVOMeta childrenMeta[] = billMeta.getChildren();
			Map<String, Class<?>> bvoNameClassMap = new HashMap<String, Class<?>>();
			// 根据表体元数据得到每一个表体的name:class映射信息
			for (int i = 0; i < childrenMeta.length; i++) {
				String bvoEntityName = childrenMeta[i].getEntityName();
				String bvoName = bvoEntityName.substring(bvoEntityName
						.indexOf(".") + 1);

				Class<?> bvoClass = billMeta.getVOClass(childrenMeta[i]);
				bvoNameClassMap.put(bvoName, bvoClass);
			}

			// 填充表头VO数据
			CircularlyAccessibleValueObject hvo = (CircularlyAccessibleValueObject) parentClass
					.newInstance();
			hvo = (CircularlyAccessibleValueObject) castToVO(headObj,
					parentClass);

			// 设置对应项目集团默认集团信息-时代集团-code=0001
			if (headObj.getString("pk_group") == null
					|| "".equals(headObj.getString("pk_group")))
				hvo.setAttributeValue("pk_group", defaultGroup);
			aggVO.setParentVO(hvo);

			if (bodyObj != null && bodyObj.size() > 0) {
				// 传入的表体keys
				Set<String> bvoKeyNames = bodyObj.keySet();
				Iterator<String> bvoKeyNameIterator = bvoKeyNames.iterator();

				while (bvoKeyNameIterator.hasNext()) {
					// 传入的表体key
					String bvoKeyName = bvoKeyNameIterator.next();

					// 判断表体元数据是否包含有该key的表体
					if (bvoNameClassMap.containsKey(bvoKeyName)) {
						// 表体数据
						JSONArray bvoArray = new JSONArray();
						bvoArray = bodyObj.getJSONArray(bvoKeyName);

						// 清理表体
						clearJSONArray(bvoArray);
						// 填充表体VO数据
						if (bvoArray != null && bvoArray.size() > 0) {
							// 定义实际的BVO[]数组大小
							SuperVO[] bvos = (SuperVO[]) Array.newInstance(
									SuperVO.class, bvoArray.size());

							// 将JSONObject[] 转换为bean表体数组
							for (int i = 0; i < bvoArray.size(); i++) {
								bvos[i] = (SuperVO) castToVO(
										bvoArray.getJSONObject(i),
										bvoNameClassMap.get(bvoKeyName));
							}

							aggVO.setChildrenVO(bvos);

						}
					}
				}
			}
		} catch (InstantiationException e) {
			errorMsg = e.getMessage();
			throw new BusinessException(errorMsg, e);

		} catch (IllegalAccessException e) {
			errorMsg = e.getMessage();
			throw new BusinessException(errorMsg, e);

		} catch (SecurityException e) {
			errorMsg = e.getMessage();
			throw new BusinessException(errorMsg, e);
		}

		return (AggregatedValueObject) aggVO;
	
	}

	public SuperVO castToVO(JSONObject jsonObj, Class<?> VOClass)
			throws InstantiationException, SecurityException,
			IllegalAccessException, BusinessException {

		SuperVO vo = (SuperVO) VOClass.newInstance();
		IVOMeta voMeta = vo.getMetaData();

		// 数据字段名数组
		Set<String> keysSet = jsonObj.keySet();
		Object[] keys = keysSet.toArray();

		// 特殊类型字段与数据值映射
		Map<String, Object> mapValue = new HashMap<String, Object>();

		for (int i = 0; i < keys.length; i++) {
			String key = keys[i].toString();
			// vo普通字段属性
			Field field = null;
			// vo元数据属性
			IAttributeMeta metaField = null;

			metaField = voMeta.getAttribute(key);
			// 找不到该字段属性，抛出异常
			if (metaField == null) {
				throw new BusinessException("工单中没有【" + key + "】数据，请检查请求参数");
			} else {
				// 属性参照信息
				String refDoc = null;
				refDoc = metaField.getReferenceDoc();
				// 若字段属性是参照类型，将转换为参照的pk值
				if (refDoc != null && !"".equals(refDoc)) {
					String pkValue = getRefAttributePk(refDoc,
							jsonObj.getString(key));
					mapValue.put(key, pkValue);
					jsonObj.remove(key);
					continue;
				}
				try {
					field = VOClass.getField(key);
				} catch (NoSuchFieldException e) {

					// 是元数据类型字段，保存字段与值得映射
					JavaType javaType = metaField.getJavaType();
					String jsonValue = jsonObj.getString(key);
					Object metaValue = null;
					if (javaType == JavaType.String) {
						metaValue = jsonValue;
					} else if (javaType == JavaType.UFDate) {
						metaValue = new UFDate(jsonValue);
					} else if (javaType == JavaType.UFDateTime) {
						metaValue = new UFDateTime(jsonValue);
					} else if (javaType == JavaType.UFDouble) {
						metaValue = new UFDouble(jsonValue);
					} else if (javaType == JavaType.BigDecimal) {
						metaValue = new BigDecimal(jsonValue);
					} else if (javaType == JavaType.Integer) {
						metaValue = new Integer(jsonValue);
					} else if (javaType == JavaType.UFBoolean) {
						metaValue = new UFBoolean(jsonValue);
					} else if (javaType == JavaType.UFLiteralDate) {
						metaValue = new UFLiteralDate(jsonValue);
					} else if (javaType == JavaType.UFTime) {
						metaValue = new UFTime(jsonValue);
					}

					mapValue.put(key, metaValue);
					jsonObj.remove(key);
					continue;
				}
				// 得到字段的类型
				Class<?> typeClass = field.getType();

				// 判断字段 类型是否属于自定义类型
				if (typeClass == UFDate.class) {
					// 数据值保存给变量mapValue
					mapValue.put(key, new UFDate(jsonObj.getString(key)));
					// 从jsonObject中移除特殊字段类型数据，
					// 避免JSON.toJavaObject(jsonObj, VOClass)无法转换
					jsonObj.remove(key);
				} else if (typeClass == UFDateTime.class) {
					mapValue.put(key, new UFDateTime(jsonObj.getString(key)));
					jsonObj.remove(key);
				} else if (typeClass == UFDouble.class) {
					mapValue.put(key, new UFDouble(jsonObj.getString(key)));
					jsonObj.remove(key);
				} else if (typeClass == UFBoolean.class) {
					mapValue.put(key, new UFBoolean(jsonObj.getString(key)));
					jsonObj.remove(key);
				} else if (typeClass == UFLiteralDate.class) {
					mapValue.put(key, new UFLiteralDate(jsonObj.getString(key)));
					jsonObj.remove(key);
				} else if (typeClass == UFTime.class) {
					mapValue.put(key, new UFTime(jsonObj.getString(key)));
					jsonObj.remove(key);
				}
			}
		}
		vo = (SuperVO) JSON.toJavaObject(jsonObj, VOClass);

		// 设置特殊类型值给VO
		Iterator<String> valueKey = mapValue.keySet().iterator();
		while (valueKey.hasNext()) {
			String key = valueKey.next();
			vo.setAttributeValue(key, mapValue.get(key));
		}

		return vo;
	
	}

	public void clearJSONArray(JSONArray array) {

		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject tmpA = (JSONObject) array.get(i);
				if (tmpA == null || tmpA.size() <= 0)
					array.remove(i);
			}
		}
	
	}
	
	

	public String getHeadKey() {
		return headKey;
	}

	public void setHeadKey(String headKey) {
		this.headKey = headKey;
	}

	public String getBodyKey() {
		return bodyKey;
	}

	public void setBodyKey(String bodyKey) {
		this.bodyKey = bodyKey;
	}

	public String getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(String defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	public abstract String getRefAttributePk(String refDoc, String code);

}
  