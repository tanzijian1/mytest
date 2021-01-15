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
 * ʱ�������ȡ��ҵ�շ�ϵͳҵ�����ͺ�ҵ��ϸ�൵��-2020-08-18
 * 
 * @author ̸�ӽ�
 * 
 */

public class InsertArchivesImpl implements IInsertArchives {

	public static final String WYSFDefaultOperator = "LLWYSF";// ��ҵ�շ�ϵͳ�Ƶ���
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
				logVO.setMsg("ͬ�������ɹ�:" + "NC�������ͱ���" + data.getString("nccode")
						+ ",��ҵ�շ�ϵͳ�������:" + data.getString("type")
						+ ",��ҵ�շ�ϵͳС����� : " + data.getString("value")
						+ ",�������� :" + data.getString("label"));
			} else {
				logVO.setResult(BillUtils.STATUS_FAILED);
				logVO.setMsg("ͬ������ʧ��:" + "NC�������ͱ���" + data.getString("nccode")
						+ ",��ҵ�շ�ϵͳ�������:" + data.getString("type")
						+ ",��ҵ�շ�ϵͳС����� : " + data.getString("value")
						+ ",�������� :" + data.getString("label"));
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
			String nccode = data.getString("nccode");// �����б����
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
							"����ʧ�ܣ�����NC�����Ǳ���������������value�Ƿ�Ϊ��");
				}

				// // �ϼ�����
				Map<String, String> map = getDefdocPranetPk(
						data.getString("parent_id"),
						getPkDefdocListByCode(data.getString("nccode")));
				if (null == map) {
					defdocVO.setPid(null);
				} else {
					defdocVO.setPid(map.get("pk_defdoc"));
				}
				// ��ҵ�շ�ϵͳ������Ŀ����id
				if (null != data.getString("id")
						&& !"".equals(data.getString("id"))) {
					defdocVO.setDef1(data.getString("id"));
				} else {
					throw new BusinessException("����ʧ�ܣ�����id�Ǳ���������������id�Ƿ�Ϊ��");
				}
				// ��ҵ�շ�ϵͳ��ĸ��Ŀ����id
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
								"����ʧ�ܣ����������Ǳ���������������label�Ƿ�Ϊ��");
					}
					if (StringUtils.isNotBlank(data.getString("value"))) {
						defdocVOs[0].setCode(data.getString("value"));
					} else {
						throw new BusinessException(
								"����ʧ�ܣ�����С��ı����Ǳ���������������value�Ƿ�Ϊ��");
					}

					if (StringUtils.isNotBlank(data.getString("delFlag"))) {
						defdocVOs[0].setEnablestate("0".equals(data
								.getString("delFlag")) ? 2 : 3);
					} else {
						throw new BusinessException(
								"����ʧ�ܣ������Ƿ�ɾ����ʶ�Ǳ���������������delFlag�Ƿ�Ϊ��");
					}

					defdocVOs[0].setMemo(null);
					defdocVOs[0].setCreator("#UAP#");
					defdocVOs[0].setModifier("#UAP#");
					// �ϼ�����
					if (null == map) {
						defdocVOs[0].setPid(null);
					} else {
						defdocVOs[0].setPid(map.get("pk_defdoc"));
					}
					// ������Ŀ����id
					if (null != data.getString("id")
							&& !"".equals(data.getString("id"))) {
						defdocVOs[0].setDef1(data.getString("id"));
					} else {
						throw new BusinessException("����ʧ�ܣ�����id�Ǳ���������������");
					}
					// ��ĸ��Ŀ����id
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
								"����ʧ�ܣ����������Ǳ���������������label�Ƿ�Ϊ��");
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
	 * ���ݡ��û����롿��ȡ����
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
	 * ��ȡ�ϼ��������ƺͱ���
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
	 * @param �Զ��嵵���б�code
	 * @param docCode
	 *            �Զ��嵵��code
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
	 * ���������շѵ�������-2020-09-16-tzj
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
				logVO.setMsg("ͬ�������ɹ�:" + "NC�������ͱ���SDLL008,�շ���Ŀ" + ",�շ���Ŀ����: "
						+ data.getString("projectCategory") + ",�շ���Ŀ���ͱ���: "
						+ data.getString("projectCategoryId") + ",�շ���Ŀ����: "
						+ data.getString("name"));
			} else {
				logVO.setResult(BillUtils.STATUS_FAILED);
				logVO.setMsg("ͬ������ʧ��:" + "NC�������ͱ���SDLL008,�շ���Ŀ" + ",�շ���Ŀ����: "
						+ data.getString("projectCategory") + ",�շ���Ŀ���ͱ���: "
						+ data.getString("projectCategoryId") + ",�շ���Ŀ����: "
						+ data.getString("name"));
			}
			BillUtils.getUtils().getBaseDAO().insertVO(logVO);
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	/**
	 * �����տ���Ŀ������������-2020-09-17-̸�ӽ�
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
				throw new BusinessException("����ʧ�ܣ�����NC�����Ǳ���������������sR�Ƿ�Ϊ��");
			}

			// �ϼ�����
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
			// ��ҵ�շ�ϵͳ������Ŀ����id
			if (null != data.getString("id")
					&& !"".equals(data.getString("id"))) {
				defdocVO.setDef1(data.getString("id"));
			} else {
				throw new BusinessException("����ʧ�ܣ�����id�Ǳ���������������id�Ƿ�Ϊ��");
			}
			// ��ҵ�շ�ϵͳ��ĸ��Ŀ����code
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
					throw new BusinessException("����ʧ�ܣ����������Ǳ���������������name�Ƿ�Ϊ��");
				}
				if (StringUtils.isNotBlank(data.getString("sR"))) {
					defdocVOs[0].setCode("SDLL"
							+ data.getString("projectCategoryId") + "00"
							+ data.getString("sR"));
				} else {
					throw new BusinessException(
							"����ʧ�ܣ�����С��ı����Ǳ���������������sR�Ƿ�Ϊ��");
				}

				if (StringUtils.isNotBlank(data
						.getString("projectCategoryDelFlag"))) {
					defdocVOs[0].setEnablestate("0".equals(data
							.getString("projectCategoryDelFlag")) ? 2 : 3);
				} else {
					throw new BusinessException(
							"����ʧ�ܣ������Ƿ�ɾ����ʶ�Ǳ���������������projectCategoryDelFlag�Ƿ�Ϊ��");
				}

				defdocVOs[0].setMemo(null);
				defdocVOs[0].setCreator("#UAP#");
				defdocVOs[0].setModifier("#UAP#");
				// �ϼ�����
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
				// ������Ŀ����id
				if (null != data.getString("id")
						&& !"".equals(data.getString("id"))) {
					defdocVOs[0].setDef1(data.getString("id"));
				} else {
					throw new BusinessException("����ʧ�ܣ�����id�Ǳ���������������");
				}
				// ��ĸ��Ŀ����id
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
					throw new BusinessException("����ʧ�ܣ����������Ǳ���������������name�Ƿ�Ϊ��");
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
