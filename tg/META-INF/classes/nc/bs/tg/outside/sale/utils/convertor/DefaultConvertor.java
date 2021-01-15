/**
 * <p>Title: DefaultConvertor.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��2�� ����4:02:39

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
 * ����ʱ�䣺2019��9��2�� ����4:02:39  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.8
 * �ļ����ƣ�DefaultConvertor.java  
 * ��˵����  
 */

/**
 * <p>Title: DefaultConvertor<��p>

 * <p>Description: <��p>

 * <p>Company: hanzhi<��p> 

 * @author bobo

 * @date 2019��9��2�� ����4:02:39
 */

public abstract class DefaultConvertor{

	private String headKey;
	private String bodyKey;
	private String defaultGroup;
	
	public AggregatedValueObject castToBill(HashMap<String, Object> value,
			Class<?> aggVOClass) throws BusinessException {


		String errorMsg = null;
		if (value == null || value.size() <= 0) {
			errorMsg = "����Ϊ�գ������������";
			throw new BusinessException(errorMsg);
		}
		// ��ȡ��ͷ�ͱ��������
		JSONObject headObj = (JSONObject) value.get(headKey);
		JSONObject bodyObj = (JSONObject) value.get(bodyKey);

		if (headObj == null || headObj.size() <= 0) {
			errorMsg = "�����쳣�������б�ͷ����";
			throw new BusinessException(errorMsg);
		}
		// �õ��ۺ�VO�뵥��Ԫ����
		AbstractBill aggVO = null;
		try {
			aggVO = (AbstractBill) aggVOClass.newInstance();
			// ����Ԫ����
			IBillMeta billMeta = aggVO.getMetaData();

			IVOMeta parentMeta = billMeta.getParent();
			// �õ���ͷ��class
			Class<?> parentClass = billMeta.getVOClass(parentMeta);

			IVOMeta childrenMeta[] = billMeta.getChildren();
			Map<String, Class<?>> bvoNameClassMap = new HashMap<String, Class<?>>();
			// ���ݱ���Ԫ���ݵõ�ÿһ�������name:classӳ����Ϣ
			for (int i = 0; i < childrenMeta.length; i++) {
				String bvoEntityName = childrenMeta[i].getEntityName();
				String bvoName = bvoEntityName.substring(bvoEntityName
						.indexOf(".") + 1);

				Class<?> bvoClass = billMeta.getVOClass(childrenMeta[i]);
				bvoNameClassMap.put(bvoName, bvoClass);
			}

			// ����ͷVO����
			CircularlyAccessibleValueObject hvo = (CircularlyAccessibleValueObject) parentClass
					.newInstance();
			hvo = (CircularlyAccessibleValueObject) castToVO(headObj,
					parentClass);

			// ���ö�Ӧ��Ŀ����Ĭ�ϼ�����Ϣ-ʱ������-code=0001
			if (headObj.getString("pk_group") == null
					|| "".equals(headObj.getString("pk_group")))
				hvo.setAttributeValue("pk_group", defaultGroup);
			aggVO.setParentVO(hvo);

			if (bodyObj != null && bodyObj.size() > 0) {
				// ����ı���keys
				Set<String> bvoKeyNames = bodyObj.keySet();
				Iterator<String> bvoKeyNameIterator = bvoKeyNames.iterator();

				while (bvoKeyNameIterator.hasNext()) {
					// ����ı���key
					String bvoKeyName = bvoKeyNameIterator.next();

					// �жϱ���Ԫ�����Ƿ�����и�key�ı���
					if (bvoNameClassMap.containsKey(bvoKeyName)) {
						// ��������
						JSONArray bvoArray = new JSONArray();
						bvoArray = bodyObj.getJSONArray(bvoKeyName);

						// �������
						clearJSONArray(bvoArray);
						// ������VO����
						if (bvoArray != null && bvoArray.size() > 0) {
							// ����ʵ�ʵ�BVO[]�����С
							SuperVO[] bvos = (SuperVO[]) Array.newInstance(
									SuperVO.class, bvoArray.size());

							// ��JSONObject[] ת��Ϊbean��������
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

		// �����ֶ�������
		Set<String> keysSet = jsonObj.keySet();
		Object[] keys = keysSet.toArray();

		// ���������ֶ�������ֵӳ��
		Map<String, Object> mapValue = new HashMap<String, Object>();

		for (int i = 0; i < keys.length; i++) {
			String key = keys[i].toString();
			// vo��ͨ�ֶ�����
			Field field = null;
			// voԪ��������
			IAttributeMeta metaField = null;

			metaField = voMeta.getAttribute(key);
			// �Ҳ������ֶ����ԣ��׳��쳣
			if (metaField == null) {
				throw new BusinessException("������û�С�" + key + "�����ݣ������������");
			} else {
				// ���Բ�����Ϣ
				String refDoc = null;
				refDoc = metaField.getReferenceDoc();
				// ���ֶ������ǲ������ͣ���ת��Ϊ���յ�pkֵ
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

					// ��Ԫ���������ֶΣ������ֶ���ֵ��ӳ��
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
				// �õ��ֶε�����
				Class<?> typeClass = field.getType();

				// �ж��ֶ� �����Ƿ������Զ�������
				if (typeClass == UFDate.class) {
					// ����ֵ���������mapValue
					mapValue.put(key, new UFDate(jsonObj.getString(key)));
					// ��jsonObject���Ƴ������ֶ��������ݣ�
					// ����JSON.toJavaObject(jsonObj, VOClass)�޷�ת��
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

		// ������������ֵ��VO
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
  