/**
 * <p>Title: DefaultConvertor.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��2�� ����4:02:39

 * @version 1.0
 */

package nc.bs.tg.outside.convert;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.vo.fct.ap.entity.AggCtApVO;
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
 * Description:JSON����ת��NC������ <��p>
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
	private String defaultGroup = null;
	// ���������ֶ�������
	private List<String> refKeys = new ArrayList<String>();
	// ��ͷkey������ӳ�䣬��������־�����ͬ�������ֶ�����
	private Map<String, String> HVOKeyName = new HashMap<String, String>();

	// ����key������ӳ�䣬��������־�����ͬ�������ֶ�����
	private Map<String, String> BVOKeyName = new HashMap<String, String>();

	// ��ҪУ�����ݵı�ͷ�ֶΣ�keyΪ��ͷ����valueΪ�ֶζ�Ӧ�쳣������ʾ
	private Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
	// ��ҪУ�����ݵı����ֶΣ�keyΪ��������valueΪ�ֶζ�Ӧ�쳣������ʾ
	private Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
	// �����ֶ���
	private String groupKey = "pk_group";

	public AggregatedValueObject castToBill(HashMap<String, Object> value,
			Class<?> aggVOClass, AbstractBill billVO) throws BusinessException {

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

		// �ۺ�VO
		AbstractBill aggVO = null;
		try {
			aggVO = (AbstractBill) aggVOClass.newInstance();

			// ����Ԫ����
			IBillMeta billMeta = aggVO.getMetaData();

			IVOMeta parentMeta = billMeta.getParent();
			// �õ���ͷ��class
			Class<?> parentClass = billMeta.getVOClass(parentMeta);

			// ��ͷԪ��������
			String hVOKey = getMetaEntityName(parentMeta.getEntityName());
			// У���ͷ�����ֶ�
			validateData(hValidatedKeyName.get(hVOKey), headObj, hVOKey);

			// ����ͷVO����
			CircularlyAccessibleValueObject hvo = (CircularlyAccessibleValueObject) parentClass
					.newInstance();
			if (billVO != null) {
				hvo = billVO.getParentVO();
			}

			hvo = (CircularlyAccessibleValueObject) castToVO(headObj,
					(SuperVO) hvo);

			// ����Ĭ�ϼ�����Ϣ
			if (headObj.getString(groupKey) == null
					|| "".equals(headObj.getString(groupKey)))
				hvo.setAttributeValue(groupKey, defaultGroup);
			aggVO.setParentVO(hvo);

			IVOMeta childrenMeta[] = billMeta.getChildren();

			// �б���
			if (childrenMeta != null && childrenMeta.length != 0) {
				JSON bodyObj = (JSON) value.get(bodyKey);

				// ���������ת������
				if (bodyObj instanceof JSONObject) {

					// ������name:classӳ����Ϣ
					Map<String, Class<?>> bvoNameClassMap = new HashMap<String, Class<?>>();
					// ���ݱ���Ԫ���ݵõ�ÿһ�������name:classӳ����Ϣ
					for (int i = 0; i < childrenMeta.length; i++) {

						// Ԫ����ʵ������
						String bvoName = getMetaEntityName(childrenMeta[i]
								.getEntityName());

						// ֻ��ȡ����ı���class
						if (BVOKeyName.containsKey(bvoName)) {
							// ����voClass
							Class<?> bvoClass = billMeta
									.getVOClass(childrenMeta[i]);
							bvoNameClassMap.put(bvoName, bvoClass);
						}
					}

					JSONObject bJSONObj = (JSONObject) bodyObj;
					if (bJSONObj != null && bJSONObj.size() > 0) {
						// ����ı���keys
						Set<String> bvoKeyNames = bJSONObj.keySet();
						Iterator<String> bvoKeyNamesIterator = bvoKeyNames
								.iterator();

						while (bvoKeyNamesIterator.hasNext()) {
							// ����ı���key
							String bvoKeyName = bvoKeyNamesIterator.next();

							// �жϱ���Ԫ�����Ƿ�����и�key�ı���
							if (bvoNameClassMap.containsKey(bvoKeyName)) {
								// ��������
								JSONArray bvoArray = new JSONArray();
								bvoArray = bJSONObj.getJSONArray(bvoKeyName);

								// У���������ֶ�
								validateData(bValidatedKeyName.get(bvoKeyName),
										bvoArray, bvoKeyName);

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
					String tempBEntityName = childrenMeta[0].getEntityName();
					String bVOKey = tempBEntityName.substring(tempBEntityName
							.indexOf(".") + 1);
					// У���������ֶ�
					validateData(bValidatedKeyName.get(bVOKey), bvoArray,
							bVOKey);

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
			errorMsg = "�������쳣������ϵϵͳ����Ա";
			throw new BusinessException(errorMsg, e);

		} catch (IllegalAccessException e) {
			errorMsg = "�������쳣������ϵϵͳ����Ա";
			throw new BusinessException(errorMsg, e);

		} catch (SecurityException e) {
			errorMsg = "�������쳣������ϵϵͳ����Ա";
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
		if (jsonObj == null || jsonObj.size() == 0)
			return null;

		SuperVO vo = (SuperVO) VOClass.newInstance();
		IVOMeta voMeta = vo.getMetaData();

		Set<String> keysSet = jsonObj.keySet();
		// �������ݲ���������
		Object[] keys = keysSet.toArray();

		// ���������ֶ�������ֵӳ��
		// Map<String, Object> spacialTypeValueMap = new HashMap<String,
		// Object>();

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
				continue;
			} else {
				// ���ֶ������ǲ������ͣ���ת��Ϊ���յ�pkֵ
				// *****�����ֶ������ڲ�ͬ�����д��ڣ��Ҳ�ͬ�����ֶ���Щ���ǲ��գ����ã�ʵ������-�ֶ�����
				String entityName = getMetaEntityName(voMeta.getEntityName());
				String outputtime = null;
				// ��ֵʱ�����жϽ����޸�2019-11-08-̸�ӽ�-start
				if ("OutPutBVO".equals(entityName)
						&& "outputtime".equals(key.toString())) {
					String yearMonth = jsonObj.getString(key).trim();
					outputtime = getLastDayOfMonth(yearMonth);

				}
				// ��ֵʱ�����жϽ����޸�2019-11-08-̸�ӽ�-end
				String voRefKey = entityName + "-" + key; // ʵ������-�ֶ�
				// *****�����ֶ������ڲ�ͬ�����д��ڣ��Ҳ�ͬ�����ֶ���Щ���ǲ��գ����ã�ʵ������-�ֶ�����
				if (refKeys.contains(voRefKey)) {

					// ����ֱ����ֵ����ͬ���ջ��в�ͬ�Ĳ�ѯ�������޷�ͨ����ֵ��
					// ���ⲿ�������õ����룬Ȼ��ʵ�ֲ�ͬ�Ĳ��ղ�ѯ�͵���
					try {
						field = VOClass.getDeclaredField(key);
					} catch (NoSuchFieldException e) {
						// vo��û�и��ֶΣ�����Ԫ���������ֶ�,Ԫ�����ֶ���ֵ
						setMetaFiledValue(vo, key, jsonObj.getString(key),
								metaField.getJavaType());
						continue;
					}
					// �������Ա���ֵת��ΪPKֵ
					/*
					 * String pkValue = getRefAttributePk(voRefKey,
					 * jsonObj.getString(key)); vo.setAttributeValue(key,
					 * pkValue);
					 */

					setFiledValue(vo, field, jsonObj.getString(key));
					/*
					 * field = VOClass.getDeclaredField(key); setFiledValue(vo,
					 * field, pkValue);
					 */
					continue;
				}
				// ***************�ǲ�����ֵ
				try {
					field = VOClass.getDeclaredField(key);
				} catch (NoSuchFieldException e) {
					// vo��û�и��ֶΣ�����Ԫ���������ֶ�,Ԫ�����ֶ���ֵ
					setMetaFiledValue(vo, key, jsonObj.getString(key),
							metaField.getJavaType());
					continue;
				}
				// ��ͨ�ֶ���ֵ
				if (outputtime != null) {
					setFiledValue(vo, field, outputtime);
				} else {
					setFiledValue(vo, field, jsonObj.getString(key));
				}

				// ***************�ǲ�����ֵ
			}
		}

		return vo;

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
	public SuperVO castToVO(JSONObject jsonObj, SuperVO vo)
			throws InstantiationException, SecurityException,
			IllegalAccessException, BusinessException {

		// �����ݷ���null
		if (jsonObj == null || jsonObj.size() == 0)
			return null;

		IVOMeta voMeta = vo.getMetaData();

		Set<String> keysSet = jsonObj.keySet();
		// �������ݲ���������
		Object[] keys = keysSet.toArray();

		// ���������ֶ�������ֵӳ��
		// Map<String, Object> spacialTypeValueMap = new HashMap<String,
		// Object>();

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
				continue;
				// throw new BusinessException("�����С�" + voMeta.getLabel()
				// + "��û�������ֶΡ�" + key + "���������������");
			} else {
				// ���ֶ������ǲ������ͣ���ת��Ϊ���յ�pkֵ
				// *****�����ֶ������ڲ�ͬ�����д��ڣ��Ҳ�ͬ�����ֶ���Щ���ǲ��գ����ã�ʵ������-�ֶ�����
				String entityName = getMetaEntityName(voMeta.getEntityName());
				String voRefKey = entityName + "-" + key; // ʵ������-�ֶ�
				// *****�����ֶ������ڲ�ͬ�����д��ڣ��Ҳ�ͬ�����ֶ���Щ���ǲ��գ����ã�ʵ������-�ֶ�����
				if (refKeys.contains(voRefKey)) {

					// ����ֱ����ֵ����ͬ���ջ��в�ͬ�Ĳ�ѯ�������޷�ͨ����ֵ��
					// ���ⲿ�������õ����룬Ȼ��ʵ�ֲ�ͬ�Ĳ��ղ�ѯ�͵���
					try {
						field = vo.getClass().getDeclaredField(key);
					} catch (NoSuchFieldException e) {
						// vo��û�и��ֶΣ�����Ԫ���������ֶ�,Ԫ�����ֶ���ֵ
						setMetaFiledValue(vo, key, jsonObj.getString(key),
								metaField.getJavaType());
						continue;
					}
					// �������Ա���ֵת��ΪPKֵ
					/*
					 * String pkValue = getRefAttributePk(voRefKey,
					 * jsonObj.getString(key)); vo.setAttributeValue(key,
					 * pkValue);
					 */
					setFiledValue(vo, field, jsonObj.getString(key));
					/*
					 * field = VOClass.getDeclaredField(key); setFiledValue(vo,
					 * field, pkValue);
					 */
					continue;
				}
				// ***************�ǲ�����ֵ
				try {
					field = vo.getClass().getDeclaredField(key);
				} catch (NoSuchFieldException e) {
					// vo��û�и��ֶΣ�����Ԫ���������ֶ�,Ԫ�����ֶ���ֵ
					setMetaFiledValue(vo, key, jsonObj.getString(key),
							metaField.getJavaType());
					continue;
				}
				// ��ͨ�ֶ���ֵ
				setFiledValue(vo, field, jsonObj.getString(key));
				// ***************�ǲ�����ֵ
			}
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
	 * Title: setFiledValue<��p>
	 * <p>
	 * Description: ��String����ת��Ϊ�ֶζ�Ӧ������������<��p>
	 * 
	 * @param vo
	 *            ʵ����
	 * @param field
	 *            �ֶ�
	 * @param value
	 *            ����ֵ
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void setFiledValue(Object vo, Field field, String value)
			throws IllegalArgumentException, IllegalAccessException {
		// �ֶ�����
		Class<?> type = field.getType();
		field.setAccessible(true);
		if ("".equals(value)) {
			value = null;
		}
		if (type == String.class) {
			field.set(vo, value);
		} else if (type == UFDate.class) {
			field.set(vo, value == null ? value : new UFDate(value));
		} else if (type == UFDateTime.class) {
			field.set(vo, value == null ? value : new UFDateTime(value));
		} else if (type == UFDouble.class) {
			field.set(vo, value == null ? value : new UFDouble(value));
		} else if (type == UFBoolean.class) {
			field.set(vo, value == null ? value : new UFBoolean(value));
		} else if (type == UFLiteralDate.class) {
			field.set(vo, value == null ? value : new UFLiteralDate(value));
		} else if (type == UFTime.class) {
			field.set(vo, value == null ? value : new UFTime(value));
		} else if (type == byte.class) {
			field.set(vo, value == null ? value : Byte.parseByte(value));
		} else if (type == short.class) {
			field.set(vo, value == null ? value : Short.parseShort(value));
		} else if (type == int.class) {
			field.set(vo, value == null ? value : Integer.parseInt(value));
		} else if (type == long.class) {
			field.set(vo, value == null ? value : Long.parseLong(value));
		} else if (type == float.class) {
			field.set(vo, value == null ? value : Float.parseFloat(value));
		} else if (type == double.class) {
			field.set(vo, value == null ? value : Double.parseDouble(value));
		} else if (type == boolean.class) {
			field.set(vo, value == null ? value : Boolean.parseBoolean(value));
		} else if (type == char.class) {
			field.set(vo, value == null ? value : value.charAt(0));
		} else if (type == Byte.class) {
			field.set(vo, value == null ? value : new Byte(value));
		} else if (type == Short.class) {
			field.set(vo, value == null ? value : new Short(value));
		} else if (type == Integer.class) {
			field.set(vo, value == null ? value : new Integer(value));
		} else if (type == Long.class) {
			field.set(vo, value == null ? value : new Long(value));
		} else if (type == Float.class) {
			field.set(vo, value == null ? value : new Float(value));
		} else if (type == Double.class) {
			field.set(vo, value == null ? value : new Double(value));
		} else if (type == Boolean.class) {
			field.set(vo, value == null ? value : new Boolean(value));
		} else if (type == Character.class) {
			field.set(vo, value == null ? value
					: new Character(value.charAt(0)));
		} else if (type == BigDecimal.class) {
			field.set(vo, value == null ? value : new BigDecimal(value));
		}
	}

	/**
	 * <p>
	 * Title: setMetaFiledValue<��p>
	 * <p>
	 * Description: voԪ�����ֶ���ֵ<��p>
	 * 
	 * @param vo
	 *            vo����
	 * @param key
	 *            �ֶ�
	 * @param value
	 *            ����ֵ
	 * @param javaType
	 *            Ԫ�����ֶ�����
	 */
	public void setMetaFiledValue(SuperVO vo, String key, String value,
			JavaType javaType) {
		if ("".equals(value)) {
			value = null;
		}
		if (javaType == JavaType.String) {
			vo.setAttributeValue(key, value);
		} else if (javaType == JavaType.UFDate) {
			vo.setAttributeValue(key, value == null ? value : new UFDate(value));
		} else if (javaType == JavaType.UFDateTime) {
			vo.setAttributeValue(key, value == null ? value : new UFDateTime(
					value));
		} else if (javaType == JavaType.UFDouble) {
			vo.setAttributeValue(key, value == null ? value : new UFDouble(
					value));
		} else if (javaType == JavaType.BigDecimal) {
			vo.setAttributeValue(key, value == null ? value : new BigDecimal(
					value));
		} else if (javaType == JavaType.Integer) {
			vo.setAttributeValue(key, value == null ? value
					: new Integer(value));
		} else if (javaType == JavaType.UFBoolean) {
			vo.setAttributeValue(key, value == null ? value : new UFBoolean(
					value));
		} else if (javaType == JavaType.UFLiteralDate) {
			vo.setAttributeValue(key, value == null ? value
					: new UFLiteralDate(value));
		} else if (javaType == JavaType.UFTime) {
			vo.setAttributeValue(key, value == null ? value : new UFTime(value));
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
	 * @param VOKey
	 *            ��ͷ���������key
	 * @throws BusinessException
	 */
	public void validateData(Map<String, String> validatedKeyName, JSON data,
			String VOKey) throws BusinessException {
		if (validatedKeyName == null || validatedKeyName.size() == 0)
			return;

		if (data == null)
			throw new BusinessException("�����쳣��" + validatedKeyName.toString()
					+ "�����ֶ����ݲ���Ϊ��");

		// У���ͷ����
		if (data instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) data;
			// ��Ҫ��У����ֶ�keys
			Set<String> keys = validatedKeyName.keySet();

			Iterator<String> itKeys = keys.iterator();

			while (itKeys.hasNext()) {
				// ��У���ֶ�key
				String key = itKeys.next();
				if ("def2".equals(key)
						&& "exhousetransferbillHVO".equals(VOKey)) {
					continue;
				}
				String value = jsonObj.getString(key);
				if (value == null || "".equals(value)) {
					// ��Ӧ�ֶ�������ʾ��
					String errorName = validatedKeyName.get(key);
					throw new BusinessException("��ͷ" + HVOKeyName.get(VOKey)
							+ "�����쳣����" + key + "��" + errorName + "����Ϊ��");
				}
			}
		}
		// У���������
		else if (data instanceof JSONArray) {
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
						// ��Ӧ�ֶ�������ʾ��
						String errorName = validatedKeyName.get(key);
						throw new BusinessException("����"
								+ BVOKeyName.get(VOKey) + "�����쳣����(" + (i + 1)
								+ ")��" + "��" + key + "��" + errorName + "����Ϊ��");
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

	public Map<String, Map<String, String>> gethValidatedKeyName() {
		return hValidatedKeyName;
	}

	public void sethValidatedKeyName(
			Map<String, Map<String, String>> hValidatedKeyName) {
		this.hValidatedKeyName = hValidatedKeyName;
	}

	public Map<String, Map<String, String>> getbValidatedKeyName() {
		return bValidatedKeyName;
	}

	public void setbValidatedKeyName(
			Map<String, Map<String, String>> bValidatedKeyName) {
		this.bValidatedKeyName = bValidatedKeyName;
	}

	public Map<String, String> getHVOKeyName() {
		return HVOKeyName;
	}

	public Map<String, String> getBVOKeyName() {
		return BVOKeyName;
	}

	public void setHVOKeyName(Map<String, String> hVOKeyName) {
		HVOKeyName = hVOKeyName;
	}

	public void setBVOKeyName(Map<String, String> bVOKeyName) {
		BVOKeyName = bVOKeyName;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public String getMetaEntityName(String entityName) {
		return entityName.substring(entityName.indexOf(".") + 1);
	}

	public abstract String getRefAttributePk(String attribute,
			String... conditions) throws BusinessException;

	public String getLastDayOfMonth(String yearMonth) {
		int year = Integer.parseInt(yearMonth.split("-")[0]); // ��
		int month = Integer.parseInt(yearMonth.split("-")[1]); // ��
		Calendar cal = Calendar.getInstance();
		// �������
		cal.set(Calendar.YEAR, year);
		// �����·�
		cal.set(Calendar.MONTH, month - 1);
		// ��ȡĳ���������
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		// �����������·ݵ��������
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// ��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
}
