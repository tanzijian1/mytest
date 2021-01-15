/**
 * <p>Title: DefaultConvertor.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月2日 下午4:02:39

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
 * 创建时间：2019年9月2日 下午4:02:39  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：DefaultConvertor.java  
 * 类说明：  
 */

/**
 * <p>
 * Title: DefaultConvertor<／p>
 * 
 * <p>
 * Description:JSON数据转换NC单据类 <／p>
 * 
 * <p>
 * Company: hanzhi<／p>
 * 
 * @author bobo
 * 
 * @date 2019年9月2日 下午4:02:39
 */

public abstract class DefaultConvertor {

	// 请求参数表头键
	private String headKey = "headInfo";
	// 请求参数表体键
	private String bodyKey = "itemInfo";
	// 默认集团PK值
	private String defaultGroup = null;
	// 参照类型字段名集合
	private List<String> refKeys = new ArrayList<String>();
	// 表头key与名称映射，作检验日志输出、同名参照字段区分
	private Map<String, String> HVOKeyName = new HashMap<String, String>();

	// 表体key与名称映射，作检验日志输出、同名参照字段区分
	private Map<String, String> BVOKeyName = new HashMap<String, String>();

	// 需要校验数据的表头字段，key为表头名，value为字段对应异常中文提示
	private Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
	// 需要校验数据的表体字段，key为表体名，value为字段对应异常中文提示
	private Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
	// 集团字段名
	private String groupKey = "pk_group";

	public AggregatedValueObject castToBill(HashMap<String, Object> value,
			Class<?> aggVOClass, AbstractBill billVO) throws BusinessException {

		String errorMsg = null;
		if (value == null || value.size() <= 0) {
			errorMsg = "数据为空，请检查请求参数";
			throw new BusinessException(errorMsg);
		}

		// 获取表头的数据
		JSONObject headObj = (JSONObject) value.get(headKey);

		if (headObj == null || headObj.size() <= 0) {
			errorMsg = "数据异常，必须有表头数据";
			throw new BusinessException(errorMsg);
		}

		// 聚合VO
		AbstractBill aggVO = null;
		try {
			aggVO = (AbstractBill) aggVOClass.newInstance();

			// 单据元数据
			IBillMeta billMeta = aggVO.getMetaData();

			IVOMeta parentMeta = billMeta.getParent();
			// 得到表头的class
			Class<?> parentClass = billMeta.getVOClass(parentMeta);

			// 表头元数据名称
			String hVOKey = getMetaEntityName(parentMeta.getEntityName());
			// 校验表头必填字段
			validateData(hValidatedKeyName.get(hVOKey), headObj, hVOKey);

			// 填充表头VO数据
			CircularlyAccessibleValueObject hvo = (CircularlyAccessibleValueObject) parentClass
					.newInstance();
			if (billVO != null) {
				hvo = billVO.getParentVO();
			}

			hvo = (CircularlyAccessibleValueObject) castToVO(headObj,
					(SuperVO) hvo);

			// 设置默认集团信息
			if (headObj.getString(groupKey) == null
					|| "".equals(headObj.getString(groupKey)))
				hvo.setAttributeValue(groupKey, defaultGroup);
			aggVO.setParentVO(hvo);

			IVOMeta childrenMeta[] = billMeta.getChildren();

			// 有表体
			if (childrenMeta != null && childrenMeta.length != 0) {
				JSON bodyObj = (JSON) value.get(bodyKey);

				// 多表体数据转换处理
				if (bodyObj instanceof JSONObject) {

					// 多表体的name:class映射信息
					Map<String, Class<?>> bvoNameClassMap = new HashMap<String, Class<?>>();
					// 根据表体元数据得到每一个表体的name:class映射信息
					for (int i = 0; i < childrenMeta.length; i++) {

						// 元数据实体名称
						String bvoName = getMetaEntityName(childrenMeta[i]
								.getEntityName());

						// 只获取请求的表体class
						if (BVOKeyName.containsKey(bvoName)) {
							// 表体voClass
							Class<?> bvoClass = billMeta
									.getVOClass(childrenMeta[i]);
							bvoNameClassMap.put(bvoName, bvoClass);
						}
					}

					JSONObject bJSONObj = (JSONObject) bodyObj;
					if (bJSONObj != null && bJSONObj.size() > 0) {
						// 传入的表体keys
						Set<String> bvoKeyNames = bJSONObj.keySet();
						Iterator<String> bvoKeyNamesIterator = bvoKeyNames
								.iterator();

						while (bvoKeyNamesIterator.hasNext()) {
							// 传入的表体key
							String bvoKeyName = bvoKeyNamesIterator.next();

							// 判断表体元数据是否包含有该key的表体
							if (bvoNameClassMap.containsKey(bvoKeyName)) {
								// 表体数据
								JSONArray bvoArray = new JSONArray();
								bvoArray = bJSONObj.getJSONArray(bvoKeyName);

								// 校验表体必填字段
								validateData(bValidatedKeyName.get(bvoKeyName),
										bvoArray, bvoKeyName);

								// 清理表体
								clearJSONArray(bvoArray);
								// 填充表体VO数据
								if (bvoArray != null && bvoArray.size() > 0) {
									// 定义实际的BVO[]数组大小
									SuperVO[] bvos = (SuperVO[]) Array
											.newInstance(SuperVO.class,
													bvoArray.size());

									// 将JSONObject[] 转换为bean表体数组
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
				// 单表体数据转换
				else if (bodyObj instanceof JSONArray) {
					JSONArray bvoArray = (JSONArray) bodyObj;
					String tempBEntityName = childrenMeta[0].getEntityName();
					String bVOKey = tempBEntityName.substring(tempBEntityName
							.indexOf(".") + 1);
					// 校验表体必填字段
					validateData(bValidatedKeyName.get(bVOKey), bvoArray,
							bVOKey);

					// 清理表体数组空元素
					clearJSONArray(bvoArray);
					// 填充表体VO数据
					if (bvoArray != null && bvoArray.size() > 0) {
						// 定义实际的BVO[]数组大小
						SuperVO[] bvos = (SuperVO[]) Array.newInstance(
								SuperVO.class, bvoArray.size());

						// 将JSONObject[] 转换为bean表体数组
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
			errorMsg = "服务器异常，请联系系统管理员";
			throw new BusinessException(errorMsg, e);

		} catch (IllegalAccessException e) {
			errorMsg = "服务器异常，请联系系统管理员";
			throw new BusinessException(errorMsg, e);

		} catch (SecurityException e) {
			errorMsg = "服务器异常，请联系系统管理员";
			throw new BusinessException(errorMsg, e);
		}

		return (AggregatedValueObject) aggVO;

	}

	/**
	 * <p>
	 * Title: castToVO<／p>
	 * <p>
	 * Description: 将JSONObject对象转换为VO对象<／p>
	 * 
	 * @param jsonObj
	 *            JSONObject 数据对象
	 * @param VOClass
	 * @return VO对象
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws BusinessException
	 */
	public SuperVO castToVO(JSONObject jsonObj, Class<?> VOClass)
			throws InstantiationException, SecurityException,
			IllegalAccessException, BusinessException {

		// 无数据返回null
		if (jsonObj == null || jsonObj.size() == 0)
			return null;

		SuperVO vo = (SuperVO) VOClass.newInstance();
		IVOMeta voMeta = vo.getMetaData();

		Set<String> keysSet = jsonObj.keySet();
		// 请求数据参数名数组
		Object[] keys = keysSet.toArray();

		// 特殊类型字段与数据值映射
		// Map<String, Object> spacialTypeValueMap = new HashMap<String,
		// Object>();

		// 循环转换参数值，生成vo对象
		for (int i = 0; i < keys.length; i++) {
			// 请求参数名
			String key = keys[i].toString();
			// vo普通字段属性
			Field field = null;
			// vo元数据属性
			IAttributeMeta metaField = null;
			metaField = voMeta.getAttribute(key);

			// 找不到该字段属性，抛出异常
			if (metaField == null) {
				continue;
			} else {
				// 若字段属性是参照类型，将转换为参照的pk值
				// *****参照字段名称在不同表体中存在，且不同表体字段有些不是参照，则用，实体名称-字段区分
				String entityName = getMetaEntityName(voMeta.getEntityName());
				String outputtime = null;
				// 产值时间做判断进行修改2019-11-08-谈子健-start
				if ("OutPutBVO".equals(entityName)
						&& "outputtime".equals(key.toString())) {
					String yearMonth = jsonObj.getString(key).trim();
					outputtime = getLastDayOfMonth(yearMonth);

				}
				// 产值时间做判断进行修改2019-11-08-谈子健-end
				String voRefKey = entityName + "-" + key; // 实体名称-字段
				// *****参照字段名称在不同表体中存在，且不同表体字段有些不是参照，则用，实体名称-字段区分
				if (refKeys.contains(voRefKey)) {

					// 参照直接设值，不同参照会有不同的查询条件，无法通用设值，
					// 得外部调用者拿到编码，然后实现不同的参照查询和调用
					try {
						field = VOClass.getDeclaredField(key);
					} catch (NoSuchFieldException e) {
						// vo中没有该字段，则是元数据类型字段,元数据字段设值
						setMetaFiledValue(vo, key, jsonObj.getString(key),
								metaField.getJavaType());
						continue;
					}
					// 参照属性编码值转换为PK值
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
				// ***************非参照设值
				try {
					field = VOClass.getDeclaredField(key);
				} catch (NoSuchFieldException e) {
					// vo中没有该字段，则是元数据类型字段,元数据字段设值
					setMetaFiledValue(vo, key, jsonObj.getString(key),
							metaField.getJavaType());
					continue;
				}
				// 普通字段设值
				if (outputtime != null) {
					setFiledValue(vo, field, outputtime);
				} else {
					setFiledValue(vo, field, jsonObj.getString(key));
				}

				// ***************非参照设值
			}
		}

		return vo;

	}

	/**
	 * <p>
	 * Title: castToVO<／p>
	 * <p>
	 * Description: 将JSONObject对象转换为VO对象<／p>
	 * 
	 * @param jsonObj
	 *            JSONObject 数据对象
	 * @param VOClass
	 * @return VO对象
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws BusinessException
	 */
	public SuperVO castToVO(JSONObject jsonObj, SuperVO vo)
			throws InstantiationException, SecurityException,
			IllegalAccessException, BusinessException {

		// 无数据返回null
		if (jsonObj == null || jsonObj.size() == 0)
			return null;

		IVOMeta voMeta = vo.getMetaData();

		Set<String> keysSet = jsonObj.keySet();
		// 请求数据参数名数组
		Object[] keys = keysSet.toArray();

		// 特殊类型字段与数据值映射
		// Map<String, Object> spacialTypeValueMap = new HashMap<String,
		// Object>();

		// 循环转换参数值，生成vo对象
		for (int i = 0; i < keys.length; i++) {
			// 请求参数名
			String key = keys[i].toString();
			// vo普通字段属性
			Field field = null;
			// vo元数据属性
			IAttributeMeta metaField = null;
			metaField = voMeta.getAttribute(key);

			// 找不到该字段属性，抛出异常
			if (metaField == null) {
				continue;
				// throw new BusinessException("单据中【" + voMeta.getLabel()
				// + "】没有数据字段【" + key + "】，请检查请求参数");
			} else {
				// 若字段属性是参照类型，将转换为参照的pk值
				// *****参照字段名称在不同表体中存在，且不同表体字段有些不是参照，则用，实体名称-字段区分
				String entityName = getMetaEntityName(voMeta.getEntityName());
				String voRefKey = entityName + "-" + key; // 实体名称-字段
				// *****参照字段名称在不同表体中存在，且不同表体字段有些不是参照，则用，实体名称-字段区分
				if (refKeys.contains(voRefKey)) {

					// 参照直接设值，不同参照会有不同的查询条件，无法通用设值，
					// 得外部调用者拿到编码，然后实现不同的参照查询和调用
					try {
						field = vo.getClass().getDeclaredField(key);
					} catch (NoSuchFieldException e) {
						// vo中没有该字段，则是元数据类型字段,元数据字段设值
						setMetaFiledValue(vo, key, jsonObj.getString(key),
								metaField.getJavaType());
						continue;
					}
					// 参照属性编码值转换为PK值
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
				// ***************非参照设值
				try {
					field = vo.getClass().getDeclaredField(key);
				} catch (NoSuchFieldException e) {
					// vo中没有该字段，则是元数据类型字段,元数据字段设值
					setMetaFiledValue(vo, key, jsonObj.getString(key),
							metaField.getJavaType());
					continue;
				}
				// 普通字段设值
				setFiledValue(vo, field, jsonObj.getString(key));
				// ***************非参照设值
			}
		}

		return vo;

	}

	/**
	 * <p>
	 * Title: clearJSONArray<／p>
	 * <p>
	 * Description: 清除JSON数组的空元素<／p>
	 * 
	 * @param array
	 *            JSON数组
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
	 * Title: setFiledValue<／p>
	 * <p>
	 * Description: 将String数据转换为字段对应数据类型数据<／p>
	 * 
	 * @param vo
	 *            实体类
	 * @param field
	 *            字段
	 * @param value
	 *            数据值
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void setFiledValue(Object vo, Field field, String value)
			throws IllegalArgumentException, IllegalAccessException {
		// 字段类型
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
	 * Title: setMetaFiledValue<／p>
	 * <p>
	 * Description: vo元数据字段设值<／p>
	 * 
	 * @param vo
	 *            vo对象
	 * @param key
	 *            字段
	 * @param value
	 *            数据值
	 * @param javaType
	 *            元数据字段类型
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
	 * Title: validateData<／p>
	 * <p>
	 * Description: 校验必填字段数据，根据指定的hValidatedKeyName，
	 * bValidatedKeyName表头表体字段进行空值校验。如有其它所需校验可自行重写方法<／p>
	 * 
	 * @param validatedKeyName
	 *            校验字段key
	 * @param data
	 *            JSON数据对象
	 * @param VOKey
	 *            表头或表体名称key
	 * @throws BusinessException
	 */
	public void validateData(Map<String, String> validatedKeyName, JSON data,
			String VOKey) throws BusinessException {
		if (validatedKeyName == null || validatedKeyName.size() == 0)
			return;

		if (data == null)
			throw new BusinessException("数据异常，" + validatedKeyName.toString()
					+ "以上字段数据不能为空");

		// 校验表头数据
		if (data instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) data;
			// 需要被校验的字段keys
			Set<String> keys = validatedKeyName.keySet();

			Iterator<String> itKeys = keys.iterator();

			while (itKeys.hasNext()) {
				// 被校验字段key
				String key = itKeys.next();
				if ("def2".equals(key)
						&& "exhousetransferbillHVO".equals(VOKey)) {
					continue;
				}
				String value = jsonObj.getString(key);
				if (value == null || "".equals(value)) {
					// 对应字段中文提示名
					String errorName = validatedKeyName.get(key);
					throw new BusinessException("表头" + HVOKeyName.get(VOKey)
							+ "数据异常，【" + key + "】" + errorName + "不能为空");
				}
			}
		}
		// 校验表体数据
		else if (data instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) data;

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				// 需要被校验的字段keys
				Set<String> keys = validatedKeyName.keySet();

				Iterator<String> itKeys = keys.iterator();

				while (itKeys.hasNext()) {
					// 被校验字段key
					String key = itKeys.next();

					String value = jsonObj.getString(key);
					if (value == null || "".equals(value)) {
						// 对应字段中文提示名
						String errorName = validatedKeyName.get(key);
						throw new BusinessException("表体"
								+ BVOKeyName.get(VOKey) + "数据异常，第(" + (i + 1)
								+ ")行" + "【" + key + "】" + errorName + "不能为空");
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
		int year = Integer.parseInt(yearMonth.split("-")[0]); // 年
		int month = Integer.parseInt(yearMonth.split("-")[1]); // 月
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
}
