package nc.bs.tg.outside.archives;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import nc.impl.am.db.processor.ListMapProcessor;
import nc.itf.bd.defdoc.IDefdocService;
import nc.itf.tg.outside.IInsertArchives;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
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
public abstract class AutoSysncEBSContract implements IBackgroundWorkPlugin {
	public static final String EBSDefaultOperator = "EBS";// EBS用户
	private IInsertArchives insertArchives = null;
	private BaseDAO baseDAO = null;

	public IInsertArchives getInsertArchives() {
		if (insertArchives == null) {
			insertArchives = NCLocator.getInstance().lookup(
					IInsertArchives.class);
		}
		return insertArchives;
	}

	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		String title = bgwc.getAlertTypeName();
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new WYSFAlterMessage());
		List<Object[]> reflist;
		try {
			reflist = getWorkResult(bgwc);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return util.getUtil().executeTask(title, e.getMessage());
		}
		return util.executeTask(title, reflist);

	}

	protected abstract List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException;

	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
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
	 * 
	 * @param 自定义档案列表code
	 * 
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
	 * 读取视图信息
	 * 
	 * @param contcode
	 * @return
	 * @throws BusinessException
	 */
	public List<Map<String, String>> getEBSContract(String table, String db)
			throws BusinessException {
		String sql = "select LOOKUP_CODE code,DESCRIPTION name from " + table + " @" + db;
		List<Map<String, String>> info = (List<Map<String, String>>) getBaseDAO().executeQuery(
				sql, new MapListProcessor());
		return info;
	}
}
