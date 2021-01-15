package nc.bs.tg.outside.archives;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.bd.defdoc.IDefdocService;
import nc.itf.tg.outside.IInsertArchives;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OSImplLogVO;

import org.apache.commons.lang.StringUtils;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 时代邻里获取物业收费系统业务类型和业务细类档案-2020-08-18
 * 
 * @author 谈子健
 * 
 */
public class AoutSysncWYSFChaProjectData extends AoutSysncWYSFData {
	public static final String WYSFDefaultOperator = "LLWYSF";// 物业收费系统制单人
	private IInsertArchives insertArchives = null;

	public IInsertArchives getInsertArchives() {
		if (insertArchives == null) {
			insertArchives = NCLocator.getInstance().lookup(
					IInsertArchives.class);
		}
		return insertArchives;
	}

	private BaseDAO baseDAO = null;

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		OSImplLogVO logVO = new OSImplLogVO();

		try {
			String syscode = OutsideUtils.getOutsideInfo("WYSF-SYSTEM").trim();
			String key = OutsideUtils.getOutsideInfo("WYSF-KEY").trim();
			String token = "";
			String tokenkey = "";
			if ("nc".equals(syscode)) {
				Date date = new Date();
				SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
				String time = formater.format(date);
				tokenkey = time + key;
				token = MD5Util.getMD5(tokenkey).toUpperCase();
			}

			String code = OutsideUtils.getOutsideInfo("WYSF003").trim();
			String params = "code=" + code + "&syscode=" + syscode + "&token="
					+ token;
			String address = OutsideUtils.getOutsideInfo("WYSF-URL").trim();
			String urls = address + "/method?" + params;
			String returnjson = Httpconnectionutil.newinstance().connection(
					urls, null);
			HashMap<String, Object> info = JSON.parseObject(returnjson,
					HashMap.class);

			JSONArray jsonData = (JSONArray) info.get("data");// 表单数据
			HashMap<String, String> hashMap = new HashMap<>();// 收费项目类型
			if ("S".equals(info.get("code"))) {
				// 获取父级节点
				if (jsonData != null && jsonData.size() > 0) {
					for (int row = 0; row < jsonData.size(); row++) {
						JSONObject data = jsonData.getJSONObject(row);
						String projectCategoryId = (String) data
								.get("projectCategoryId");
						String projectCategory = (String) data
								.get("projectCategory");
						hashMap.put(projectCategoryId, projectCategory);
					}
				}
				/**
				 * 插入父级节点-2020-09-17-谈子健
				 */
				HashMap<String, String> parentData = setParentChaProjectData(hashMap);

				/**
				 * 插入收款项目名称档案-2020-09-17-谈子健
				 */
				JSONObject data = null;
				if (jsonData != null && jsonData.size() > 0) {
					for (int row = 0; row < jsonData.size(); row++) {
						try {
							data = jsonData.getJSONObject(row);
							getInsertArchives()
									.insertChaProjectArchives_RequiresNew(data,
											parentData);
						} catch (Exception e) {
							logVO.setSrcparm(data.toJSONString());
							logVO.setExedate(new UFDateTime().toString());
							logVO.setSrcsystem(WYSFDefaultOperator);
							logVO.setDessystem("NC");
							logVO.setMethod(code);
							logVO.setDr(new Integer(0));
							logVO.setMsg("同步档案失败:" + "NC档案类型编码SDLL008,收费项目"
									+ ",收费项目类型: "
									+ data.getString("projectCategory")
									+ ",收费项目类型编码: "
									+ data.getString("projectCategoryId")
									+ ",收费项目名称: " + data.getString("name")
									+ "报错信息: " + e.getMessage() + "错误信息堆栈信息:"
									+ e);
							logVO.setResult(BillUtils.STATUS_FAILED);
							BillUtils.getUtils().getBaseDAO().insertVO(logVO);
						}
					}
				}

			} else {
				logVO.setSrcparm(returnjson);
				logVO.setExedate(new UFDateTime().toString());
				logVO.setSrcsystem(WYSFDefaultOperator);
				logVO.setDessystem("NC");
				logVO.setMethod(code);
				logVO.setDr(new Integer(0));
				logVO.setResult(BillUtils.STATUS_FAILED);
				logVO.setMsg(info.get("msg").toString() + ",加密前token:"
						+ tokenkey + ",加密后token:" + token + ",URl地址:" + urls);
				BillUtils.getUtils().getBaseDAO().insertVO(logVO);
			}
		} catch (Exception e) {
			logVO.setResult(BillUtils.STATUS_FAILED);
			logVO.setMsg(e.getMessage());
			BillUtils.getUtils().getBaseDAO().insertVO(logVO);
			Logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 设置父级节点档案(收费项目类型)
	 * 
	 * @param data
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, String> setParentChaProjectData(
			HashMap<String, String> hashMap) throws BusinessException {
		HashMap<String, String> parentMap = new HashMap<String, String>();
		DefdocVO[] docVOs = null;
		IDefdocService defdocService = NCLocator.getInstance().lookup(
				IDefdocService.class);
		for (Entry<String, String> entry : hashMap.entrySet()) {

			Collection<DefdocVO> docVO = getBaseDAO().retrieveByClause(
					DefdocVO.class,
					"(pk_defdoclist = '" + getPkDefdocListByCode("SDLL008")
							+ "' and isnull(dr,0)=0 and code = '"
							+ entry.getKey() + "') ");
			if (docVO != null && docVO.size() > 0) {
				DefdocVO[] defdocVOs = docVO.toArray(new DefdocVO[0]);
				defdocVOs[0].setStatus(VOStatus.UPDATED);
				if (StringUtils.isNotBlank(entry.getValue())) {
					defdocVOs[0].setName(entry.getValue());
				} else {
					throw new BusinessException(
							"操作失败，档案名称是必填项，请检查参数设置projectCategory是否为空");
				}
				if (StringUtils.isNotBlank(entry.getKey())) {
					defdocVOs[0].setCode(entry.getKey());
				} else {
					throw new BusinessException(
							"操作失败，档案小类的编码是必填项，请检查参数设置projectCategoryId是否为空");
				}

				defdocVOs[0].setEnablestate(2);
				defdocVOs[0].setMemo(null);
				defdocVOs[0].setCreator("#UAP#");
				defdocVOs[0].setModifier("#UAP#");

				docVOs = defdocService.updateDefdocs("000112100000000005FD",
						defdocVOs);
				String pk_defdoc = docVOs[0].getPk_defdoc();
				parentMap.put(entry.getKey(), pk_defdoc);
			} else {
				DefdocVO defdocVO = new DefdocVO();
				defdocVO.setPk_group("000112100000000005FD");
				defdocVO.setPk_org("000112100000000005FD");
				defdocVO.setMemo(null);
				defdocVO.setEnablestate(2);
				defdocVO.setDr(0);
				defdocVO.setStatus(VOStatus.NEW);
				defdocVO.setCreator(getUserIDByCode(WYSFDefaultOperator));
				defdocVO.setModifier("#UAP#");
				defdocVO.setDatatype(1);
				if (StringUtils.isNotBlank(entry.getKey())) {
					defdocVO.setCode(entry.getKey());
				} else {
					throw new BusinessException(
							"操作失败，档案NC编码是必填项，请检查参数设置projectCategoryId是否为空");
				}

				if (StringUtils.isNotBlank(entry.getValue())) {
					defdocVO.setName(entry.getValue());
				} else {
					throw new BusinessException(
							"操作失败，档案名称是必填项，请检查参数设置projectCategory是否为空");
				}
				defdocVO.setPk_defdoclist(getPkDefdocListByCode("SDLL008"));
				docVOs = defdocService.insertDefdocs("000112100000000005FD",
						new DefdocVO[] { defdocVO });
				String pk_defdoc = docVOs[0].getPk_defdoc();
				parentMap.put(entry.getKey(), pk_defdoc);
			}
		}

		return parentMap;

	}

	/**
	 * 
	 * 
	 * @param 自定义档案列表code
	 * @param docCode
	 *            自定义档案code
	 * @return
	 * @throws BusinessException
	 */
	public String getPkDefdocListByCode(String code) throws BusinessException {
		String sql = "select c.pk_defdoclist from bd_defdoclist c where  c.code='"
				+ code + "' and nvl(dr,0) = 0";
		String result = null;
		result = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
		return result;
	}

	/**
	 * 根据【用户编码】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getUserIDByCode(String code) throws BusinessException {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}
}
