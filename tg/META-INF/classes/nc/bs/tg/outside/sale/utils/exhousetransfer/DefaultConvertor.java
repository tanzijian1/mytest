/**
 * <p>Title: DefaultConvertor.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月2日 下午4:02:39

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
 * Description: <／p>
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
	private String defaultGroup;
	// 参照类型字段名集合
	private List<String> refKeys = new ArrayList<String>();
	// 需要校验数据的表头字段，key为英文字段名，value为字段对应异常中文提示
	private Map<String, String> hValidatedKeyName = new HashMap<String, String>();
	// 需要校验数据的表体字段，key为英文字段名，value为字段对应异常中文提示
	private Map<String, String> bValidatedKeyName = new HashMap<String, String>();
	// 集团字段名
	private String groupKey = "pk_group";

	public AggregatedValueObject castToBill(HashMap<String, Object> value,
			Class<?> aggVOClass) throws BusinessException {

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

		// 校验表头必填字段
		validateData(hValidatedKeyName, headObj);

		// 聚合VO
		AbstractBill aggVO = null;
		try {
			aggVO = (AbstractBill) aggVOClass.newInstance();
			// 单据元数据
			IBillMeta billMeta = aggVO.getMetaData();

			IVOMeta parentMeta = billMeta.getParent();
			// 得到表头的class
			Class<?> parentClass = billMeta.getVOClass(parentMeta);

			// 填充表头VO数据
			CircularlyAccessibleValueObject hvo = (CircularlyAccessibleValueObject) parentClass
					.newInstance();
			hvo = (CircularlyAccessibleValueObject) castToVO(headObj,
					parentClass);

			// 设置默认集团信息
			if (headObj.getString(groupKey) == null
					|| "".equals(headObj.getString(groupKey)))
				hvo.setAttributeValue(groupKey, defaultGroup);
			aggVO.setParentVO(hvo);

			IVOMeta childrenMeta[] = billMeta.getChildren();
			if (childrenMeta != null && childrenMeta.length != 0) {
				JSON bodyObj = (JSON) value.get(bodyKey);
				// 校验表体必填字段
				validateData(bValidatedKeyName, bodyObj);
				// 多表体的name:class映射信息
				Map<String, Class<?>> bvoNameClassMap = new HashMap<String, Class<?>>();
				// 根据表体元数据得到每一个表体的name:class映射信息
				for (int i = 0; i < childrenMeta.length; i++) {

					String bvoEntityName = childrenMeta[i].getEntityName();
					// 表体vo类名（首字母小写）
					String bvoName = bvoEntityName.substring(bvoEntityName
							.indexOf(".") + 1);
					// 表体voClass
					Class<?> bvoClass = billMeta.getVOClass(childrenMeta[i]);
					bvoNameClassMap.put(bvoName, bvoClass);
				}
				// 多表体数据转换处理
				if (bodyObj instanceof JSONObject) {

					JSONObject bJSONObj = (JSONObject) bodyObj;
					if (bJSONObj != null && bJSONObj.size() > 0) {
						// 传入的表体keys
						Set<String> bvoKeyNames = bJSONObj.keySet();
						Iterator<String> bvoKeyNameIterator = bvoKeyNames
								.iterator();

						while (bvoKeyNameIterator.hasNext()) {
							// 传入的表体key
							String bvoKeyName = bvoKeyNameIterator.next();

							// 判断表体元数据是否包含有该key的表体
							if (bvoNameClassMap.containsKey(bvoKeyName)) {
								// 表体数据
								JSONArray bvoArray = new JSONArray();
								bvoArray = bJSONObj.getJSONArray(bvoKeyName);

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
			errorMsg = "服务器异常，请联系系统管理员进行检查";
			throw new BusinessException(errorMsg, e);

		} catch (IllegalAccessException e) {
			errorMsg = "服务器异常，请联系系统管理员进行检查";
			throw new BusinessException(errorMsg, e);

		} catch (SecurityException e) {
			errorMsg = "服务器异常，请联系系统管理员进行检查";
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
		if(jsonObj == null || jsonObj.size() == 0)
			return null;
		
		SuperVO vo = (SuperVO) VOClass.newInstance();
		IVOMeta voMeta = vo.getMetaData();

		Set<String> keysSet = jsonObj.keySet();
		// 请求数据参数名数组
		Object[] keys = keysSet.toArray();

		// 特殊类型字段与数据值映射
		Map<String, Object> spacialTypeValueMap = new HashMap<String, Object>();

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
				throw new BusinessException("单据中没有与之对应的数据字段【" + key
						+ "】，请检查请求参数");
			} else {

				// 若字段属性是参照类型，将转换为参照的pk值
				if (refKeys.contains(key)) {
					// 参照属性编码值转换为PK值
					String pkValue = getRefAttributePk(key,
							jsonObj.getString(key));
					spacialTypeValueMap.put(key, pkValue);
					jsonObj.remove(key);
					continue;
				}
				try {
					field = VOClass.getField(key);
					// vo中没有该字段，则是元数据类型字段，保存字段与值得映射
				} catch (NoSuchFieldException e) {

					//属性类型
					JavaType javaType = metaField.getJavaType();
					//请求参数值
					String jsonValue = jsonObj.getString(key);
					//目标类型值
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
				// 得到字段的类型
				Class<?> typeClass = field.getType();

				// 判断字段 类型是否属于自定义类型
				if (typeClass == UFDate.class) {
					// 数据值保存给变量mapValue
					spacialTypeValueMap.put(key,
							new UFDate(jsonObj.getString(key)));
					// 从jsonObject中移除特殊字段类型数据，
					// 避免JSON.toJavaObject(jsonObj, VOClass)无法转换
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

		// 设置特殊类型值给VO
		Iterator<String> valueKey = spacialTypeValueMap.keySet().iterator();
		while (valueKey.hasNext()) {
			String key = valueKey.next();
			vo.setAttributeValue(key, spacialTypeValueMap.get(key));
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
	 * Title: validateData<／p>
	 * <p>
	 * Description: 校验必填字段数据，根据指定的hValidatedKeyName，
	 * bValidatedKeyName表头表体字段进行空值校验。如有其它所需校验可自行重写方法<／p>
	 * 
	 * @param validatedKeyName
	 *            校验字段key
	 * @param data
	 *            JSON数据对象
	 * @throws BusinessException
	 */
	public void validateData(Map<String, String> validatedKeyName, JSON data)
			throws BusinessException {
		if (validatedKeyName == null || validatedKeyName.size() == 0)
			return;

		if (data == null)
			throw new BusinessException("数据异常，" + validatedKeyName.toString()
					+ "不能为空");

		if (data instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) data;
			// 需要被校验的字段keys
			Set<String> keys = validatedKeyName.keySet();

			Iterator<String> itKeys = keys.iterator();

			while (itKeys.hasNext()) {
				// 被校验字段key
				String key = itKeys.next();

				String value = jsonObj.getString(key);
				if (value == null || "".equals(value)) {
					// 对应中文字段提示名
					String errorName = validatedKeyName.get(key);
					throw new BusinessException("数据异常，【" + key + "】"
							+ errorName + "不能为空");
				}
			}
		} else if (data instanceof JSONArray) {
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
						// 对应中文字段提示名
						String errorName = validatedKeyName.get(key);
						throw new BusinessException("数据异常，第(" + (i + 1) + ")行"
								+ "【" + key + "】" + errorName + "不能为空");
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
