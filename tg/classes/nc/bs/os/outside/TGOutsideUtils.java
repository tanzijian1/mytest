package nc.bs.os.outside;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;

public class TGOutsideUtils {
	static TGOutsideUtils utils = null;
	IUAPQueryBS queryBS = null;

	public static TGOutsideUtils getUtils() {
		if (utils == null) {
			utils = new TGOutsideUtils();
		}
		return utils;
	}

	public IUAPQueryBS getQueryBS() {
		if (queryBS == null) {
			queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return queryBS;
	}

	/**
	 * 读取调用外部交互系统相关信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getOutsidInfo(String code) throws BusinessException {
		String sql = "select d.memo  from bd_defdoc d  "
				+ " where d.pk_defdoclist =(select l.pk_defdoclist from bd_defdoclist l where l.code = 'TGOUT' ) "
				+ " and d.code = '" + code + "' ";
		return (String) getQueryBS().executeQuery(sql, new ColumnProcessor());
	}

	/**
	 * 读取来源系统对应的实现类/密钥【发布接口】
	 * 
	 * @param system
	 * @param method
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getReleaseImplInfo(String system, String method)
			throws BusinessException {
		String sql = "select v.code,v.name,v.memo classname from bd_defdoc v  "
				+ "  where pk_defdoc in (  SELECT d.pk_defdoc FROM bd_defdoc d where "
				+ "  d.pk_defdoclist =(select l.pk_defdoclist from bd_defdoclist l where l.code = 'TGIMPL' )"
				+ "  start with  d.code = 'RELEASE' connect by prior d.pk_defdoc = d.pid) "
				+ " and v.def1 = '" + system + "' and v.def2 = '" + method
				+ "' and v.dr = 0";
		return (Map<String, String>) getQueryBS().executeQuery(sql,
				new MapProcessor());
	}

	/**
	 * 读取来源系统对应的实现类/密钥【调用接口】
	 * 
	 * @param system
	 * @param method
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getCallImplInfo(String system, String method)
			throws BusinessException {
		String sql = "select v.code,v.name,v.memo classname from bd_defdoc v  "
				+ "  where pk_defdoc in (  SELECT d.pk_defdoc FROM bd_defdoc d where "
				+ "  d.pk_defdoclist =(select l.pk_defdoclist from bd_defdoclist l where l.code = 'TGIMPL' )"
				+ "  start with  d.code = 'CALL' connect by prior d.pk_defdoc = d.pid) "
				+ " and v.def1 = '" + system + "' and v.def2 = '" + method
				+ "' and v.dr = 0";
		return (Map<String, String>) getQueryBS().executeQuery(sql,
				new MapProcessor());
	}
}
