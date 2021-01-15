package nc.bs.tg.outside.recbill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.outside.ReceivableBodyVO;
import nc.vo.tg.outside.ReceivableHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RecbillUtils extends BillUtils implements ITGSyncService {
	public static final String WYSFDefaultOperator = "LLWYSF";// 物业收费系统制单人
	public static final String SRMDefaultOperator = "LLSRM";// 物业收费系统制单人
	private HashMap<String, HashMap<String, String>> m_bankaccnum = new HashMap<String, HashMap<String, String>>();// 使用权账号
	
	static RecbillUtils utils;
	String wysfUserid = null;// 物业收费系统操作用户

	public static RecbillUtils getUtils() {
		if (utils == null) {
			utils = new RecbillUtils();
		}
		return utils;
	}

	/**
	 * 应收单
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		if (methodname.contains("物业收费")) {
			InvocationInfoProxy.getInstance().setUserId(
					getUserIDByCode(WYSFDefaultOperator));
			// 设置用户码
			InvocationInfoProxy.getInstance().setUserCode(WYSFDefaultOperator);
		}
		if (methodname.contains("SRM")) {
			InvocationInfoProxy.getInstance().setUserId(
					getUserIDByCode(SRMDefaultOperator));
			// 设置用户码
			InvocationInfoProxy.getInstance().setUserCode(SRMDefaultOperator);
		}

		// 处理表单信息
		JSONObject jsonData = (JSONObject) info.get("data");// 表单数据
		String jsonhead = jsonData.getString("headInfo");// 外系统来源表头数据
		String jsonbody = jsonData.getString("bodyInfo");// 外系统来源表体数据
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("表单数据为空，请检查！json:" + jsonData);
		}
		// 转换json
		ReceivableHeadVO headVO = JSONObject.parseObject(jsonhead,
				ReceivableHeadVO.class);
		List<ReceivableBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				ReceivableBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查！json:" + jsonData);
		}
		String srcid = headVO.getSrcid();// 外系统业务单据ID
		String srcno = headVO.getSrcbillno();// 外系统业务单据单据号
		Map<String, String> resultInfo = new HashMap<String, String>();

		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggReceivableBillVO aggVO = (AggReceivableBillVO) getBillVO(
					AggReceivableBillVO.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");

			if (aggVO != null) {
				throw new BusinessException("【来源系统单据号:"
						+ srcno
						+ "】,【来源系统单据主键:"
						+ srcid
						+ "】NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								ReceivableBillVO.BILLNO) + "】,请勿重复上传!");
			}

			AggReceivableBillVO billvo = onTranBill(headVO, bodyVOs);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			String pk_tradetype = (String) billvo.getParentVO()
					.getAttributeValue("pk_tradetype");

			WorkflownoteVO worknoteVO = NCLocator.getInstance()
					.lookup(IWorkflowMachine.class)
					.checkWorkFlow("SAVE", pk_tradetype, billvo, eParam);

			Object obj = (AggReceivableBillVO[]) getPfBusiAction()
					.processAction("SAVE", pk_tradetype, worknoteVO, billvo,
							null, null);
			AggReceivableBillVO[] billvos = (AggReceivableBillVO[]) obj;
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(ReceivableBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	/**
	 * 读取物业收费系统操作员默认用户
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getWysfUserID() throws BusinessException {
		if (wysfUserid == null) {
			String sql = "select cuserid from sm_user  where user_code = '"
					+ DefaultOperator + "'";
			wysfUserid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
		return wysfUserid;
	}

	protected AggReceivableBillVO onTranBill(ReceivableHeadVO headVO,
			List<ReceivableBodyVO> bodyVOs) throws BusinessException {
		return null;
	}

	/**
	 * 根据【用户编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getUserPkByCode(String code) {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据编码或名称找客商
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getcustomer(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_customer from bd_customer where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}

	/**
	 * 根据【公司编码】获取财务组织
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	/**
	 * 根据【公司编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_orgByCode(String code) {
		String sql = "select pk_org from org_orgs where (code='" + code
				+ "' or name = '" + code + "') and dr=0 and enablestate=2 ";
		String pk_org = null;
		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 预算科目查询
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, String>> getBudgetsubNameByCode(String itemcode)
			throws BusinessException {
		List<Map<String, String>> result = null;
		StringBuffer query = new StringBuffer();
//		query.append("select t.objname as itemname, t1.objname as itemtype  ");
//		query.append("  from tb_budgetsub t  ");
//		query.append(" inner join tb_budgetsub t1  ");
//		query.append("    on t.pk_parent = t1.pk_obj  ");
//		query.append(" where t.objcode = '" + itemcode + "'  ");
//		query.append("   and nvl(t.dr, 0) = 0  ");
//		query.append("   and t.enablestate = 2  ");
		query.append("select pk_defdoc,pid from bd_defdoc where def1 = '"+itemcode+"'");

		try {
			result = (List<Map<String, String>>) getBaseDAO().executeQuery(
					query.toString(), new MapListProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据编码或名称找客商
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getCustomerBySupplierCode(String suppliercode)
			throws BusinessException {
		String pk_customer = "";
		StringBuffer query = new StringBuffer();
		query.append("select c.pk_customer  ");
		query.append("  from bd_supplier b  ");
		query.append(" inner join bd_customer c  ");
		query.append("    on b.corcustomer = c.pk_customer  ");
		query.append(" where b.code = '" + suppliercode + "'  ");
		query.append("   and b.enablestate = 2  ");
		query.append("   and b.dr = 0  ");
		query.append("   and c.enablestate = 2  ");
		query.append("   and c.dr = 0;  ");
		pk_customer = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_customer;
	}
	
	/**
	 * 使用权账户
	 * 
	 * @param pk_cust
	 * @param accnum
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, String> getBankaccnumInfo(String pk_org,
			String accnum) throws BusinessException {
		String key = pk_org + "&" + accnum;
		if (m_bankaccnum.get(key) == null) {
			String sql = "SELECT  bd_bankaccsub.accnum, "
					+ " bd_bankaccsub.accname, "
					+ " bd_bankaccsub.pk_bankaccsub AS pk_bankaccsub, "
					+ " bd_bankaccsub.pk_bankaccbas AS pk_bankaccbas,"
					+ " bd_bankdoc.code bankcode,"
					+ " bd_bankdoc.name bankname "
					+ "  from bd_bankaccbas "
					+ " INNER JOIN bd_bankaccsub ON bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
					+ " INNER JOIN bd_bankdoc on bd_bankaccbas.pk_bankdoc = bd_bankdoc.pk_bankdoc "
					+ " where (bd_bankaccbas.accclass = 2)  and bd_bankaccbas.enablestate = 2 and (bd_bankaccsub.acctype = 0 or bd_bankaccsub.acctype = 3) "
					+ " and exists  (select 1  from bd_bankaccuse   where pk_org = '"
					+ pk_org
					+ "'  and bd_bankaccuse. enablestate = 2 and bd_bankaccsub.pk_bankaccsub = bd_bankaccuse.pk_bankaccsub )  "
					+ " AND bd_bankaccsub.accnum ='" + accnum + "' ";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_bankaccnum.put(key, info);
		}
		return m_bankaccnum.get(key);
	}
}
