package nc.bs.tg.outside.archives;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.bd.defdoc.IDefdocService;
import nc.itf.tg.outside.IInsertArchives;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OSImplLogVO;
import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 时代邻里获取合同类型自定义档案
 * 
 */
public class AutoSysncEBSContractTypeDetail extends AutoSysncEBSContract {

	/**
	 */
	@SuppressWarnings("unchecked")
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		OSImplLogVO logVO = new OSImplLogVO();
		JSONObject data = null;
		try {
			List<Map<String, String>> result = (List<Map<String, String>>) getEBSContract(
					"cux_contract_xl_v", "appstest");
			JSONArray jsonArray = JSONArray.parseArray(JSON
					.toJSONString(result));
			if (jsonArray != null && jsonArray.size() > 0) {
				try {
					for (int row = 0; row < jsonArray.size(); row++) {
						data = JSONObject.parseObject(jsonArray.get(row)
								.toString());
						data.put("nccode", "SDLL016");
						insertArchives_RequiresNew(data);
					}
				} catch (Exception e) {
					logVO.setSrcparm(result.toString());
					logVO.setExedate(new UFDateTime().toString());
					logVO.setSrcsystem(EBSDefaultOperator);
					logVO.setDessystem("NC");
					logVO.setMethod("AutoSysncEBSContractType");
					logVO.setDr(new Integer(0));
					logVO.setMsg("同步EBS合同类型档案失败:" + "NC档案类型编码"
							+ data.getString("nccode") + ",code:"
							+ data.getString("code") + ",name : "
							+ data.getString("name") + e.getMessage()
							+ "错误信息堆栈信息:" + e);
					logVO.setResult(BillUtils.STATUS_FAILED);
					BillUtils.getUtils().getBaseDAO().insertVO(logVO);
				}
			}
		} catch (Exception e) {
			logVO.setResult(BillUtils.STATUS_FAILED);
			logVO.setMsg(e.getMessage());
			BillUtils.getUtils().getBaseDAO().insertVO(logVO);
			Logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void insertArchives_RequiresNew(JSONObject data)
			throws BusinessException {
		OSImplLogVO logVO = new OSImplLogVO();
		try {
			logVO.setSrcparm(data.toJSONString());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setSrcsystem(EBSDefaultOperator);
			logVO.setDessystem("NC");
			logVO.setMethod("AutoSysncEBSContractType");
			logVO.setDr(new Integer(0));
			logVO.setResult(BillUtils.STATUS_SUCCESS);
			DefdocVO[] saveDate = setDate(data);
			if (saveDate != null) {
				logVO.setMsg("同步档案成功:" + "NC档案类型编码" + data.getString("nccode")
						+ ",code:" + data.getString("code") + ",name : "
						+ data.getString("name"));
			} else {
				logVO.setResult(BillUtils.STATUS_FAILED);
				logVO.setMsg("同步档案成功:" + "NC档案类型编码" + data.getString("nccode")
						+ ",code:" + data.getString("code") + ",name : "
						+ data.getString("name"));
			}
			BillUtils.getUtils().getBaseDAO().insertVO(logVO);
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private DefdocVO[] setDate(JSONObject data) throws BusinessException {
		DefdocVO[] docVOs;
		try {
			DefdocVO defdocVO = new DefdocVO();
			docVOs = null;
			String nccode = data.getString("nccode");// 档案列表编码
			if (nccode != null && !"".equals(nccode)) {
				IDefdocService defdocService = NCLocator.getInstance().lookup(
						IDefdocService.class);
				defdocVO.setPk_group("000112100000000005FD");
				defdocVO.setPk_org("000112100000000005FD");
				defdocVO.setMemo(null);
				defdocVO.setEnablestate(2);
				defdocVO.setDr(0);
				defdocVO.setStatus(VOStatus.NEW);
				defdocVO.setCreator(getUserIDByCode(EBSDefaultOperator));
				defdocVO.setModifier("#UAP#");
				defdocVO.setDatatype(1);
				if (StringUtils.isNotBlank(data.getString("code"))) {
					defdocVO.setCode(data.getString("code"));
				} else {
					throw new BusinessException(
							"操作失败，档案NC编码是必填项，请检查参数设置code是否为空");
				}
				Collection<DefdocVO> docVO = getBaseDAO().retrieveByClause(
						DefdocVO.class,
						"(pk_defdoclist = '"
								+ getPkDefdocListByCode(data
										.getString("nccode"))
								+ "' and isnull(dr,0)=0 and code = '"
								+ data.getString("code") + "') ");
				if (docVO != null && docVO.size() > 0) {
					DefdocVO[] defdocVOs = docVO.toArray(new DefdocVO[0]);
					defdocVOs[0].setStatus(VOStatus.UPDATED);
					if (StringUtils.isNotBlank(data.getString("name"))) {
						defdocVOs[0].setName(data.getString("name"));
					} else {
						throw new BusinessException(
								"操作失败，档案名称是必填项，请检查参数设置label是否为空");
					}
					if (StringUtils.isNotBlank(data.getString("code"))) {
						defdocVOs[0].setCode(data.getString("code"));
					} else {
						throw new BusinessException(
								"操作失败，档案小类的编码是必填项，请检查参数设置value是否为空");
					}
					defdocVOs[0].setMemo(null);
					defdocVOs[0].setCreator("#UAP#");
					defdocVOs[0].setModifier("#UAP#");
					docVOs = defdocService.updateDefdocs(
							"000112100000000005FD", defdocVOs);
				} else {
					if (StringUtils.isNotBlank(data.getString("name"))) {
						defdocVO.setName(data.getString("name"));
					} else {
						throw new BusinessException(
								"操作失败，档案名称是必填项，请检查参数设置name是否为空");
					}
					defdocVO.setPk_defdoclist(getPkDefdocListByCode(data
							.getString("nccode")));
					docVOs = defdocService
							.insertDefdocs("000112100000000005FD",
									new DefdocVO[] { defdocVO });
				}
			}
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return docVOs;

	}

}
