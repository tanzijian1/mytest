package nc.impl.tg.outside;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.bd.defdoc.IDefdocService;
import nc.itf.tg.IProjectdataMaintain;
import nc.itf.tg.outside.IInsertArchives;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OSImplLogVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 时代邻里获取物业收费系统业务类型和业务细类档案-2020-08-18
 * 
 * @author 谈子健
 * 
 */

public class InsertArchivesImpl implements IInsertArchives {

	public static final String WYSFDefaultOperator = "LLWYSF";// 物业收费系统制单人
	BaseDAO baseDAO = null;
	IMDPersistenceQueryService queryServcie = null;
	IProjectdataMaintain maintain = null;
	ISyncEBSServcie ebsService = null;

	@Override
	public void insertArchives_RequiresNew(JSONObject data)
			throws BusinessException {
		OSImplLogVO logVO = new OSImplLogVO();
		String code;
		try {
			code = OutsideUtils.getOutsideInfo("WYSF001").trim();
			logVO.setSrcparm(data.toJSONString());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setSrcsystem(WYSFDefaultOperator);
			logVO.setDessystem("NC");
			logVO.setMethod(code);
			logVO.setDr(new Integer(0));
			logVO.setResult(BillUtils.STATUS_SUCCESS);
			DefdocVO[] saveDate = setDate(data);
			if (saveDate != null) {
				logVO.setMsg("同步档案成功:" + "NC档案类型编码" + data.getString("nccode")
						+ ",物业收费系统大类编码:" + data.getString("type")
						+ ",物业收费系统小类编码 : " + data.getString("value")
						+ ",档案内容 :" + data.getString("label"));
			} else {
				logVO.setResult(BillUtils.STATUS_FAILED);
				logVO.setMsg("同步档案失败:" + "NC档案类型编码" + data.getString("nccode")
						+ ",物业收费系统大类编码:" + data.getString("type")
						+ ",物业收费系统小类编码 : " + data.getString("value")
						+ ",档案内容 :" + data.getString("label"));
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
				defdocVO.setEnablestate("0".equals(data.getString("delFlag")) ? 2
						: 3);
				defdocVO.setDr(0);
				defdocVO.setStatus(VOStatus.NEW);
				defdocVO.setCreator(getUserIDByCode(WYSFDefaultOperator));
				defdocVO.setModifier("#UAP#");
				defdocVO.setDatatype(1);
				if (StringUtils.isNotBlank(data.getString("value"))) {
					defdocVO.setCode(data.getString("value"));
				} else {
					throw new BusinessException(
							"操作失败，档案NC编码是必填项，请检查参数设置value是否为空");
				}

				// // 上级档案
				Map<String, String> map = getDefdocPranetPk(
						data.getString("parent_id"),
						getPkDefdocListByCode(data.getString("nccode")));
				if (null == map) {
					defdocVO.setPid(null);
				} else {
					defdocVO.setPid(map.get("pk_defdoc"));
				}
				// 物业收费系统传子项目档案id
				if (null != data.getString("id")
						&& !"".equals(data.getString("id"))) {
					defdocVO.setDef1(data.getString("id"));
				} else {
					throw new BusinessException("操作失败，档案id是必填项，请检查参数设置id是否为空");
				}
				// 物业收费系统传母项目档案id
				if (null != data.getString("parent_id")
						&& !"".equals(data.getString("parent_id"))) {
					defdocVO.setDef2(data.getString("parent_id"));
				} else {
					defdocVO.setDef2(null);
				}
				Collection<DefdocVO> docVO = getBaseDAO().retrieveByClause(
						DefdocVO.class,
						"(pk_defdoclist = '"
								+ getPkDefdocListByCode(data
										.getString("nccode"))
								+ "' and isnull(dr,0)=0 and def1 = '"
								+ data.getString("id") + "') ");
				if (docVO != null && docVO.size() > 0) {
					DefdocVO[] defdocVOs = docVO.toArray(new DefdocVO[0]);
					defdocVOs[0].setStatus(VOStatus.UPDATED);
					if (StringUtils.isNotBlank(data.getString("label"))) {
						defdocVOs[0].setName(data.getString("label"));
					} else {
						throw new BusinessException(
								"操作失败，档案名称是必填项，请检查参数设置label是否为空");
					}
					if (StringUtils.isNotBlank(data.getString("value"))) {
						defdocVOs[0].setCode(data.getString("value"));
					} else {
						throw new BusinessException(
								"操作失败，档案小类的编码是必填项，请检查参数设置value是否为空");
					}

					if (StringUtils.isNotBlank(data.getString("delFlag"))) {
						defdocVOs[0].setEnablestate("0".equals(data
								.getString("delFlag")) ? 2 : 3);
					} else {
						throw new BusinessException(
								"操作失败，档案是否删除标识是必填项，请检查参数设置delFlag是否为空");
					}

					defdocVOs[0].setMemo(null);
					defdocVOs[0].setCreator("#UAP#");
					defdocVOs[0].setModifier("#UAP#");
					// 上级档案
					if (null == map) {
						defdocVOs[0].setPid(null);
					} else {
						defdocVOs[0].setPid(map.get("pk_defdoc"));
					}
					// 传子项目档案id
					if (null != data.getString("id")
							&& !"".equals(data.getString("id"))) {
						defdocVOs[0].setDef1(data.getString("id"));
					} else {
						throw new BusinessException("操作失败，档案id是必填项，请检查参数设置");
					}
					// 传母项目档案id
					if (null != data.getString("parent_id")
							&& !"".equals(data.getString("parent_id"))) {
						defdocVOs[0].setDef2(data.getString("parent_id"));
					} else {
						defdocVOs[0].setDef2(null);
					}
					docVOs = defdocService.updateDefdocs(
							"000112100000000005FD", defdocVOs);
				} else {
					if (StringUtils.isNotBlank(data.getString("label"))) {
						defdocVO.setName(data.getString("label"));
					} else {
						throw new BusinessException(
								"操作失败，档案名称是必填项，请检查参数设置label是否为空");
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

	/**
	 * 获取上级档案名称和编码
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getDefdocPranetPk(String parnet_id, String code)
			throws BusinessException {
		String sql = "select pk_defdoc from bd_defdoc where def1 = '"
				+ parnet_id + "'  and pk_defdoclist = '" + code
				+ "' and isnull(dr,0)=0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
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

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public IMDPersistenceQueryService getQueryServcie() {
		if (queryServcie == null) {
			queryServcie = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return queryServcie;
	}

	public IProjectdataMaintain getMaintain() {
		if (maintain == null) {
			maintain = NCLocator.getInstance().lookup(
					IProjectdataMaintain.class);
		}
		return maintain;
	}

	/**
	 * 档案插入收费档案档案-2020-09-16-tzj
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	@Override
	public void insertChaProjectArchives_RequiresNew(JSONObject data,
			HashMap<String, String> parentData) throws BusinessException {
		OSImplLogVO logVO = new OSImplLogVO();
		String code = "";
		try {
			code = OutsideUtils.getOutsideInfo("WYSF003").trim();
			logVO.setSrcparm(data.toJSONString());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setSrcsystem(WYSFDefaultOperator);
			logVO.setDessystem("NC");
			logVO.setMethod(code);
			logVO.setDr(new Integer(0));
			logVO.setResult(BillUtils.STATUS_SUCCESS);
			DefdocVO[] saveDate = setChaProjectData(data, parentData);
			if (saveDate != null) {
				logVO.setMsg("同步档案成功:" + "NC档案类型编码SDLL008,收费项目" + ",收费项目类型: "
						+ data.getString("projectCategory") + ",收费项目类型编码: "
						+ data.getString("projectCategoryId") + ",收费项目名称: "
						+ data.getString("name"));
			} else {
				logVO.setResult(BillUtils.STATUS_FAILED);
				logVO.setMsg("同步档案失败:" + "NC档案类型编码SDLL008,收费项目" + ",收费项目类型: "
						+ data.getString("projectCategory") + ",收费项目类型编码: "
						+ data.getString("projectCategoryId") + ",收费项目名称: "
						+ data.getString("name"));
			}
			BillUtils.getUtils().getBaseDAO().insertVO(logVO);
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	/**
	 * 设置收款项目档案名称内容-2020-09-17-谈子健
	 * 
	 * @param date
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private DefdocVO[] setChaProjectData(JSONObject data,
			HashMap<String, String> parentData) throws BusinessException {
		DefdocVO[] docVOs = null;
		try {
			DefdocVO defdocVO = new DefdocVO();
			IDefdocService defdocService = NCLocator.getInstance().lookup(
					IDefdocService.class);
			defdocVO.setPk_group("000112100000000005FD");
			defdocVO.setPk_org("000112100000000005FD");
			defdocVO.setMemo(null);
			defdocVO.setEnablestate("0".equals(data
					.getString("projectCategoryDelFlag")) ? 2 : 3);
			defdocVO.setDr(0);
			defdocVO.setStatus(VOStatus.NEW);
			defdocVO.setCreator(getUserIDByCode(WYSFDefaultOperator));
			defdocVO.setModifier("#UAP#");
			defdocVO.setDatatype(1);
			if (StringUtils.isNotBlank(data.getString("sR"))) {
				defdocVO.setCode("SDLL" + data.getString("projectCategoryId")
						+ "00" + data.getString("sR"));
			} else {
				throw new BusinessException("操作失败，档案NC编码是必填项，请检查参数设置sR是否为空");
			}

			// 上级档案
			if (parentData != null) {
				if (parentData.containsKey(data.getString("projectCategoryId"))) {
					String parent_id = parentData.get(data
							.getString("projectCategoryId"));
					if (null == parent_id || "".equals(parent_id)) {
						defdocVO.setPid(null);
					} else {
						defdocVO.setPid(parent_id);
					}
				}
			}
			// 物业收费系统传子项目档案id
			if (null != data.getString("id")
					&& !"".equals(data.getString("id"))) {
				defdocVO.setDef1(data.getString("id"));
			} else {
				throw new BusinessException("操作失败，档案id是必填项，请检查参数设置id是否为空");
			}
			// 物业收费系统传母项目档案code
			if (null != data.getString("projectCategoryId")
					&& !"".equals(data.getString("projectCategoryId"))) {
				defdocVO.setDef2(data.getString("projectCategoryId"));
			} else {
				defdocVO.setDef2(null);
			}
			Collection<DefdocVO> docVO = getBaseDAO().retrieveByClause(
					DefdocVO.class,
					"(pk_defdoclist = '" + getPkDefdocListByCode("SDLL008")
							+ "' and isnull(dr,0)=0 and def1 = '"
							+ data.getString("id") + "') ");
			if (docVO != null && docVO.size() > 0) {
				DefdocVO[] defdocVOs = docVO.toArray(new DefdocVO[0]);
				defdocVOs[0].setStatus(VOStatus.UPDATED);
				if (StringUtils.isNotBlank(data.getString("name"))) {
					defdocVOs[0].setName(data.getString("name"));
				} else {
					throw new BusinessException("操作失败，档案名称是必填项，请检查参数设置name是否为空");
				}
				if (StringUtils.isNotBlank(data.getString("sR"))) {
					defdocVOs[0].setCode("SDLL"
							+ data.getString("projectCategoryId") + "00"
							+ data.getString("sR"));
				} else {
					throw new BusinessException(
							"操作失败，档案小类的编码是必填项，请检查参数设置sR是否为空");
				}

				if (StringUtils.isNotBlank(data
						.getString("projectCategoryDelFlag"))) {
					defdocVOs[0].setEnablestate("0".equals(data
							.getString("projectCategoryDelFlag")) ? 2 : 3);
				} else {
					throw new BusinessException(
							"操作失败，档案是否删除标识是必填项，请检查参数设置projectCategoryDelFlag是否为空");
				}

				defdocVOs[0].setMemo(null);
				defdocVOs[0].setCreator("#UAP#");
				defdocVOs[0].setModifier("#UAP#");
				// 上级档案
				if (parentData != null) {
					if (parentData.containsKey(data
							.getString("projectCategoryId"))) {
						String parent_id = parentData.get(data
								.getString("projectCategoryId"));
						if (null == parent_id || "".equals(parent_id)) {
							defdocVOs[0].setPid(null);
						} else {
							defdocVOs[0].setPid(parent_id);
						}
					}
				}
				// 传子项目档案id
				if (null != data.getString("id")
						&& !"".equals(data.getString("id"))) {
					defdocVOs[0].setDef1(data.getString("id"));
				} else {
					throw new BusinessException("操作失败，档案id是必填项，请检查参数设置");
				}
				// 传母项目档案id
				if (null != data.getString("projectCategoryId")
						&& !"".equals(data.getString("projectCategoryId"))) {
					defdocVOs[0].setDef2(data.getString("projectCategoryId"));
				} else {
					defdocVOs[0].setDef2(null);
				}
				docVOs = defdocService.updateDefdocs("000112100000000005FD",
						defdocVOs);
			} else {
				if (StringUtils.isNotBlank(data.getString("name"))) {
					defdocVO.setName(data.getString("name"));
				} else {
					throw new BusinessException("操作失败，档案名称是必填项，请检查参数设置name是否为空");
				}
				defdocVO.setPk_defdoclist(getPkDefdocListByCode("SDLL008"));
				docVOs = defdocService.insertDefdocs("000112100000000005FD",
						new DefdocVO[] { defdocVO });
			}
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return docVOs;

	}
}
