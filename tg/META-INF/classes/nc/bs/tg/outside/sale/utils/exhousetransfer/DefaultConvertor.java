/**
 * <p>Title: DefaultConvertor.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��2�� ����4:02:39

 * @version 1.0
 */

package nc.bs.tg.outside.sale.utils.exhousetransfer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
 * @since  JDK1.7
 * �ļ����ƣ�DefaultConvertor.java  
 * ��˵����  
 */

/**
 * <p>
 * Title: DefaultConvertor<��p>
 * 
 * <p>
 * Description: <��p>
 * 
 * <p>
 * Company: hanzhi<��p>
 * 
 * @author bobo
 * 
 * @date 2019��9��2�� ����4:02:39
 */

public abstract class DefaultConvertor {

	// ���������ͷ��
	private String headKey = "headInfo";
	// ������������
	private String bodyKey = "itemInfo";
	// Ĭ�ϼ���PKֵ
	private String defaultGroup;
	// ���������ֶ�������
	private List<String> refKeys = new ArrayList<String>();
	// ��ҪУ�����ݵı�ͷ�ֶΣ�keyΪӢ���ֶ�����valueΪ�ֶζ�Ӧ�쳣������ʾ
	private Map<String, String> hValidatedKeyName = new HashMap<String, String>();
	// ��ҪУ�����ݵı����ֶΣ�keyΪӢ���ֶ�����valueΪ�ֶζ�Ӧ�쳣������ʾ
	private Map<String, String> bValidatedKeyName = new HashMap<String, String>();
	// �����ֶ���
	private String groupKey = "pk_group";

	public AggregatedValueObject castToBill(HashMap<String, Object> value,
			Class<?> aggVOClass) throws BusinessException {

		String errorMsg = null;
		if (value == null || value.size() <= 0) {
			errorMsg = "����Ϊ�գ������������";
			throw new BusinessException(errorMsg);
		}

		// ��ȡ��ͷ������
		JSONObject headObj = (JSONObject) value.get(headKey);

		if (headObj == null || headObj.size() <= 0) {
			errorMsg = "�����쳣�������б�ͷ����";
			throw new BusinessException(errorMsg);
		}

		// У���ͷ�����ֶ�
		validateData(hValidatedKeyName, headObj);

		// �ۺ�VO
		AbstractBill aggVO = null;
		try {
			aggVO = (AbstractBill) aggVOClass.newInstance();
			// ����Ԫ����
			IBillMeta billMeta = aggVO.getMetaData();

			IVOMeta parentMeta = billMeta.getParent();
			// �õ���ͷ��class
			Class<?> parentClass = billMeta.getVOClass(parentMeta);

			// ����ͷVO����
			CircularlyAccessibleValueObject hvo = (CircularlyAccessibleValueObject) parentClass
					.newInstance();
			hvo = (CircularlyAccessibleValueObject) castToVO(headObj,
					parentClass);

			// ����Ĭ�ϼ�����Ϣ
			if (headObj.getString(groupKey) == null
					|| "".equals(headObj.getString(groupKey)))
				hvo.setAttributeValue(groupKey, defaultGroup);
			aggVO.setParentVO(hvo);

			IVOMeta childrenMeta[] = billMeta.getChildren();
			if (childrenMeta != null && childrenMeta.length != 0) {
				JSON bodyObj = (JSON) value.get(bodyKey);
				// У���������ֶ�
				validateData(bValidatedKeyName, bodyObj);
				// ������name:classӳ����Ϣ
				Map<String, Class<?>> bvoNameClassMap = new HashMap<String, Class<?>>();
				// ���ݱ���Ԫ���ݵõ�ÿһ�������name:classӳ����Ϣ
				for (int i = 0; i < childrenMeta.length; i++) {

					String bvoEntityName = childrenMeta[i].getEntityName();
					// ����vo����������ĸСд��
					String bvoName = bvoEntityName.substring(bvoEntityName
							.indexOf(".") + 1);
					// ����voClass
					Class<?> bvoClass = billMeta.getVOClass(childrenMeta[i]);
					bvoNameClassMap.put(bvoName, bvoClass);
				}
				// ���������ת������
				if (bodyObj instanceof JSONObject) {

					JSONObject bJSONObj = (JSONObject) bodyObj;
					if (bJSONObj != null && bJSONObj.size() > 0) {
						// ����ı���keys
						Set<String> bvoKeyNames = bJSONObj.keySet();
						Iterator<String> bvoKeyNameIterator = bvoKeyNames
								.iterator();

						while (bvoKeyNameIterator.hasNext()) {
							// ����ı���key
							String bvoKeyName = bvoKeyNameIterator.next();

							// �жϱ���Ԫ�����Ƿ�����и�key�ı���
							if (bvoNameClassMap.containsKey(bvoKeyName)) {
								// ��������
								JSONArray bvoArray = new JSONArray();
								bvoArray = bJSONObj.getJSONArray(bvoKeyName);

								// �������
								clearJSONArray(bvoArray);
								// ������VO����
								if (bvoArray != null && bvoArray.size() > 0) {
									// ����ʵ�ʵ�BVO[]�����С
									SuperVO[] bvos = (SuperVO[]) Array
											.newInstance(SuperVO.class,
													bvoArray.size());

									// ��JSONObject[] ת��Ϊbean��������
									for (int i = 0; i < bvos.length; i++) {
										bvos[i] = (SuperVO) castToVO(
												bvoArray.getJSONObject(i),
												bvoNameClassMap.get(bvoKeyName));
									}

									aggVO.setChildrenVO(bvos);

								}
							}
						}
					}
				}
				// ����������ת��
				else if (bodyObj instanceof JSONArray) {
					JSONArray bvoArray = (JSONArray) bodyObj;
					// ������������Ԫ��
					clearJSONArray(bvoArray);
					// ������VO����
					if (bvoArray != null && bvoArray.size() > 0) {
						// ����ʵ�ʵ�BVO[]�����С
						SuperVO[] bvos = (SuperVO[]) Array.newInstance(
								SuperVO.class, bvoArray.size());

						// ��JSONObject[] ת��Ϊbean��������
						for (int i = 0; i < bvos.length; i++) {
							bvos[i] = (SuperVO) castToVO(
									bvoArray.getJSONObject(i),
									billMeta.getVOClass(childrenMeta[0]));
						}

						aggVO.setChildrenVO(bvos);

					}
				}

				else {
				}
			}
		} catch (InstantiationException e) {
			errorMsg = "�������쳣������ϵϵͳ����Ա���м��";
			throw new BusinessException(errorMsg, e);

		} catch (IllegalAccessException e) {
			errorMsg = "�������쳣������ϵϵͳ����Ա���м��";
			throw new BusinessException(errorMsg, e);

		} catch (SecurityException e) {
			errorMsg = "�������쳣������ϵϵͳ����Ա���м��";
			throw new BusinessException(errorMsg, e);
		}

		return (AggregatedValueObject) aggVO;

	}

	/**
	 * <p>
	 * Title: castToVO<��p>
	 * <p>
	 * Description: ��JSONObject����ת��ΪVO����<��p>
	 * 
	 * @param jsonObj
	 *            JSONObject ���ݶ���
	 * @param VOClass
	 * @return VO����
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws BusinessException
	 */
	public SuperVO castToVO(JSONObject jsonObj, Class<?> VOClass)
			throws InstantiationException, SecurityException,
			IllegalAccessException, BusinessException {
		
		// �����ݷ���null
		if(jsonObj == null || jsonObj.size() == 0)
			return null;
		
		SuperVO vo = (SuperVO) VOClass.newInstance();
		IVOMeta voMeta = vo.getMetaData();

		Set<String> keysSet = jsonObj.keySet();
		// �������ݲ���������
		Object[] keys = keysSet.toArray();

		// ���������ֶ�������ֵӳ��
		Map<String, Object> spacialTypeValueMap = new HashMap<String, Object>();

		// ѭ��ת������ֵ������vo����
		for (int i = 0; i < keys.length; i++) {
			// ���������
			String key = keys[i].toString();
			// vo��ͨ�ֶ�����
			Field field = null;
			// voԪ��������
			IAttributeMeta metaField = null;
			metaField = voMeta.getAttribute(key);

			// �Ҳ������ֶ����ԣ��׳��쳣
			if (metaField == null) {
				throw new BusinessException("������û����֮��Ӧ�������ֶΡ�" + key
						+ "���������������");
			} else {

				// ���ֶ������ǲ������ͣ���ת��Ϊ���յ�pkֵ
				if (refKeys.contains(key)) {
					// �������Ա���ֵת��ΪPKֵ
					String pkValue = getRefAttributePk(key,
							jsonObj.getString(key));
					spacialTypeValueMap.put(key, pkValue);
					jsonObj.remove(key);
					continue;
				}
				try {
					field = VOClass.getField(key);
					// vo��û�и��ֶΣ�����Ԫ���������ֶΣ������ֶ���ֵ��ӳ��
				} catch (NoSuchFieldException e) {

					//��������
					JavaType javaType = metaField.getJavaType();
					//�������ֵ
					String jsonValue = jsonObj.getString(key);
					//Ŀ������ֵ
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

					spacialTypeValueMap.put(key, metaValue);
					jsonObj.remove(key);
					continue;
				}
				// �õ��ֶε�����
				Class<?> typeClass = field.getType();

				// �ж��ֶ� �����Ƿ������Զ�������
				if (typeClass == UFDate.class) {
					// ����ֵ���������mapValue
					spacialTypeValueMap.put(key,
							new UFDate(jsonObj.getString(key)));
					// ��jsonObject���Ƴ������ֶ��������ݣ�
					// ����JSON.toJavaObject(jsonObj, VOClass)�޷�ת��
					jsonObj.remove(key);
				} else if (typeClass == UFDateTime.class) {
					spacialTypeValueMap.put(key,
							new UFDateTime(jsonObj.getString(key)));
					jsonObj.remove(key);
				} else if (typeClass == UFDouble.class) {
					spacialTypeValueMap.put(key,
							new UFDouble(jsonObj.getString(key)));
					jsonObj.remove(key);
				} else if (typeClass == UFBoolean.class) {
					spacialTypeValueMap.put(key,
							new UFBoolean(jsonObj.getString(key)));
					jsonObj.remove(key);
				} else if (typeClass == UFLiteralDate.class) {
					spacialTypeValueMap.put(key,
							new UFLiteralDate(jsonObj.getString(key)));
					jsonObj.remove(key);
				} else if (typeClass == UFTime.class) {
					spacialTypeValueMap.put(key,
							new UFTime(jsonObj.getString(key)));
					jsonObj.remove(key);
				}
			}
		}
		
		vo = (SuperVO) JSON.toJavaObject(jsonObj, VOClass);

		// ������������ֵ��VO
		Iterator<String> valueKey = spacialTypeValueMap.keySet().iterator();
		while (valueKey.hasNext()) {
			String key = valueKey.next();
			vo.setAttributeValue(key, spacialTypeValueMap.get(key));
		}

		return vo;

	}

	/**
	 * <p>
	 * Title: clearJSONArray<��p>
	 * <p>
	 * Description: ���JSON����Ŀ�Ԫ��<��p>
	 * 
	 * @param array
	 *            JSON����
	 */
	public void clearJSONArray(JSONArray array) {

		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject tmpA = (JSONObject) array.get(i);
				if (tmpA == null || tmpA.size() == 0)
					array.remove(i);
			}
		}

	}

	/**
	 * <p>
	 * Title: validateData<��p>
	 * <p>
	 * Description: У������ֶ����ݣ�����ָ����hValidatedKeyName��
	 * bValidatedKeyName��ͷ�����ֶν��п�ֵУ�顣������������У���������д����<��p>
	 * 
	 * @param validatedKeyName
	 *            У���ֶ�key
	 * @param data
	 *            JSON���ݶ���
	 * @throws BusinessException
	 */
	public void validateData(Map<String, String> validatedKeyName, JSON data)
			throws BusinessException {
		if (validatedKeyName == null || validatedKeyName.size() == 0)
			return;

		if (data == null)
			throw new BusinessException("�����쳣��" + validatedKeyName.toString()
					+ "����Ϊ��");

		if (data instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) data;
			// ��Ҫ��У����ֶ�keys
			Set<String> keys = validatedKeyName.keySet();

			Iterator<String> itKeys = keys.iterator();

			while (itKeys.hasNext()) {
				// ��У���ֶ�key
				String key = itKeys.next();

				String value = jsonObj.getString(key);
				if (value == null || "".equals(value)) {
					// ��Ӧ�����ֶ���ʾ��
					String errorName = validatedKeyName.get(key);
					throw new BusinessException("�����쳣����" + key + "��"
							+ errorName + "����Ϊ��");
				}
			}
		} else if (data instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) data;

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				// ��Ҫ��У����ֶ�keys
				Set<String> keys = validatedKeyName.keySet();

				Iterator<String> itKeys = keys.iterator();

				while (itKeys.hasNext()) {
					// ��У���ֶ�key
					String key = itKeys.next();

					String value = jsonObj.getString(key);
					if (value == null || "".equals(value)) {
						// ��Ӧ�����ֶ���ʾ��
						String errorName = validatedKeyName.get(key);
						throw new BusinessException("�����쳣����(" + (i + 1) + ")��"
								+ "��" + key + "��" + errorName + "����Ϊ��");
					}
				}
			}
		} else {
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

	public List<String> getRefKeys() {
		return refKeys;
	}

	public void setRefKeys(List<String> refKeys) {
		this.refKeys = refKeys;
	}

	public Map<String, String> gethValidatedKeyName() {
		return hValidatedKeyName;
	}

	public void sethValidatedKeyName(Map<String, String> hValidatedKeyName) {
		this.hValidatedKeyName = hValidatedKeyName;
	}

	public Map<String, String> getbValidatedKeyName() {
		return bValidatedKeyName;
	}

	public void setbValidatedKeyName(Map<String, String> bValidatedKeyName) {
		this.bValidatedKeyName = bValidatedKeyName;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public abstract String getRefAttributePk(String attribute, String code);

}
