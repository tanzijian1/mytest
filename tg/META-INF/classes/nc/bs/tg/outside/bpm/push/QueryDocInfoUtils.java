package nc.bs.tg.outside.bpm.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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
	 * @throws BusinessException 
	 */
	public String getColumnValue(String sql) throws BusinessException {
		// TODO 自动生成的方法存根
		String result = "";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if(StringUtils.isBlank(result)){
				throw new BusinessException("读取url为空");
			}
		} catch (BusinessException e) {
			throw new BusinessException("读取url失败："+e.getMessage());
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
	 * 获取请款单号
	 * 
	 * @param pk_psndoc
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPayReqNo(String pk)
			throws nc.vo.pub.BusinessException {
		String sql = "select billno,contractnum from v_qingkuan  where id = '"
				+ pk + "'";
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
		String sql = "select pk_org,code,name,def11 from org_orgs  where nvl(dr,0) = 0 and enablestate = 2 and pk_org = '"
				+ pk_org + "'";
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
	 * nc与bpm支付方式对照
	 * @param ncCode
	 * @return
	 */
	public String payTypeInfo(String ncCode){
		String bpmCode = null;
		if("001".equals(ncCode)){//自动划账
			bpmCode = "14";
		}else if("002".equals(ncCode)){//转账
			bpmCode = "12";
		}
		return bpmCode;
	}
	
	/**
	 * 银行贷款合同明细
	 * 
	 * @param pk_defdoc
	 * @return flowMsg   flowMsg[0]:流程类别编码  flowMsg[1]:流程类别名称
	 * @throws BusinessException 
	 */
	public Map<String, String> getContract(String pk_contract) throws BusinessException {
		String sql = "select b.contractcode code,b.htmc name from cdm_contract b where b.pk_contract = '"
				+ pk_contract + "'";
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

}
