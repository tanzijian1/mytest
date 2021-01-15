package nc.bs.tg.outside.salebpm.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pnt.vo.FileManageVO;
import nc.vo.pub.BusinessException;

public class QueryDocInfoUtils extends BPMBillUtils {
	static QueryDocInfoUtils utils;

	public static QueryDocInfoUtils getUtils() {
		if (utils == null) {
			utils = new QueryDocInfoUtils();
		}
		return utils;
	}

	/**
	 * 数据持久化，读取单行单列信息
	 * 
	 * @param sql
	 * @return
	 */
	public String getColumnValue(String sql) {
		// TODO 自动生成的方法存根
		String result = "";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 附件推送
	 * 
	 * @param billid
	 * @return
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> getFiles(String billid)
			throws BusinessException {
		List<FileManageVO> listVOs = getFileInfos(billid);
		List<Map<String, Object>> listMap = new ArrayList<>();
		if (listVOs != null && listVOs.size() > 0) {
			for (FileManageVO vo : listVOs) {
				Map<String, Object> fileMap = new HashMap<String, Object>();
				String sizeSql = "select filelength from sm_pub_filesystem where pk='"
						+ vo.getPk_filemanage() + "'";
				String namesql = "select filepath from sm_pub_filesystem where pk='"
						+ vo.getPk_filemanage() + "'";
				String file_id = vo.getDocument_name() + "&" + vo.getFile_id();
				String name = getColumnValue(namesql).substring(
						getColumnValue(namesql).lastIndexOf("/") + 1);
				fileMap.put("FileID", file_id);
				fileMap.put("Name", name);
				fileMap.put("Ext", name.substring(name.lastIndexOf(".")));
				fileMap.put("Size", getColumnValue(sizeSql) == null ? 0
						: getColumnValue(sizeSql));
				fileMap.put("AppKey", "NC");
				fileMap.put("AppKeyTicket", "DF6AF297");
				listMap.add(fileMap);
			}
			return listMap;
		}
		return null;
	}

	/**
	 * 读取附件信息
	 * 
	 * @param billid
	 * @return
	 * @throws BusinessException
	 */
	public List<FileManageVO> getFileInfos(String billid)
			throws BusinessException {
		String sql = "select b.* from bd_filemanage b, sm_pub_filesystem s "
				+ "where s.pk = b.pk_filemanage and s.isfolder = 'n' and  s.filepath like '"
				+ billid + "%'";
		List<FileManageVO> listVos = new ArrayList<FileManageVO>();
		listVos = (List<FileManageVO>) getBaseDAO().executeQuery(sql,
				new BeanListProcessor(FileManageVO.class));
		return listVos;

	}
	/**
	 * 查询流程名称
	 * @param pk_dataent
	 * @return
	 * @throws BusinessException
	 */
	public String getProcessname(String pk_dataent) throws BusinessException{
		String sql = "select bd_defdoc.name from org_bosmember left join bd_defdoc on "
				+ " org_bosmember.processname = bd_defdoc.pk_defdoc where "
				+ " org_bosmember.pk_org = '"+pk_dataent+"' and org_bosmember.pk_bos='10011210000000002C0S' and bd_defdoc.enablestate=2"
				+ " and bd_defdoc.pk_group='000112100000000005FD' and bd_defdoc.pk_defdoclist='1001121000000000058Z'";
		String name = (String)getBaseDAO().executeQuery(sql, new ColumnProcessor());
		return name;
	}
	/**
	 * 读取人员档案信息
	 * 
	 * @param pk_psndoc
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPsnInfo(String pk_psndoc)
			throws nc.vo.pub.BusinessException {
		String sql = "select pk_psndoc,code,name from bd_psndoc  where pk_psndoc = '"
				+ pk_psndoc + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 读取组织档案信息
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getOrgInfo(String pk_org)
			throws BusinessException {
		String sql = "select pk_org,code,name from org_orgs  where pk_org = '"
				+ pk_org + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 读取预算科目信息
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getBudgetInfo(String pk_obj)
			throws BusinessException {
		String sql = "select pk_obj , objcode code ,objname name from tb_budgetsub  where pk_obj = '"
				+ pk_obj + "' and nvl(dr,0) = 0 ";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 读取部门档案信息
	 * 
	 * @param pk_applicationdept
	 * @return
	 */
	public Map<String, String> getDeptInfo(String pk_dept)
			throws BusinessException {
		String sql = "select pk_dept,code,name from org_dept  where pk_dept = '"
				+ pk_dept + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 读取银行账号信息
	 * 
	 * @param pk_bankdoc
	 * @return
	 */
	public Map<String, String> getBankAcctInfo(String pk_bankaccbas)
			throws BusinessException {
		String sql = "select pk_bankdoc,accnum from bd_bankaccbas where pk_bankaccbas = '"+pk_bankaccbas+"'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 读取银行档案信息
	 * 
	 * @param pk_bankdoc
	 * @return
	 */
	public Map<String, String> getBankDoctInfo(String pk_bankaccbas)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select bd_bankdoc.name name ,bd_bankaccsub.accnum accnum ,bd_bankaccbas.code code from bd_bankdoc ");
		sql.append(" left join bd_bankaccbas on bd_bankdoc.pk_bankdoc = bd_bankaccbas.pk_bankdoc ");
		sql.append(" left join bd_bankaccsub on bd_bankaccsub.pk_bankaccbas = bd_bankaccbas.pk_bankaccbas ");
		sql.append(" where bd_bankaccsub.pk_bankaccsub = '"+pk_bankaccbas+"' ");
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql.toString(), new MapProcessor());
		return infoMap;
	}

	/**
	 * 读取客商档案信息
	 * 
	 * @param pk_applicationdept
	 * @return
	 */
	public Map<String, String> getCustInfo(String pk_cust_sup)
			throws BusinessException {
		String sql = "select pk_cust_sup,code,name from bd_cust_supplier where pk_cust_sup = '"
				+ pk_cust_sup + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 读取项目资料档案信息
	 * 
	 * @param pk_applicationdept
	 * @return
	 */
	public Map<String, String> getProjectDataInfo(String pk_project)
			throws BusinessException {
		String sql = "select srcid,code,name from tgrz_projectdata  where pk_projectdata = '"
				+ pk_project + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 读取自定义项档案信息
	 * 
	 * @param pk_defdoc
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDefdocInfo(String pk_defdoc)
			throws BusinessException {
		String sql = "select pk_defdoc,code,name from bd_defdoc  where pk_defdoc = '"
				+ pk_defdoc + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 获取域账号
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getRegionNameByPersonCode(String code) throws BusinessException{
		String sql = "select userprincipalname,code from employeeitem  where code = '"
				+ code + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO().executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 获取项目信息
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getProjectByPK(String pk_project) throws BusinessException{
		String sql = "select project_code,project_name,pk_project from bd_project b where (enablestate = 2) "
				+ "and (deletestate is null or deletestate <> 1) and (dr = 0) and pk_project = '"
				+ pk_project + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO().executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 获取结算信息
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getBalatypeByPK(String pk_balatype) throws BusinessException{
		String sql = "select code,name from bd_balatype where nvl(dr,0)=0 and pk_balatype = '"
				+ pk_balatype + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO().executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	public Map<String, String> getSupplierInfo(String pk_supplier) throws BusinessException{
		String sql = "select code,name from bd_supplier where nvl(dr,0)=0 and pk_supplier = '"
				+ pk_supplier + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO().executeQuery(sql, new MapProcessor());
		return infoMap;
	}

}
