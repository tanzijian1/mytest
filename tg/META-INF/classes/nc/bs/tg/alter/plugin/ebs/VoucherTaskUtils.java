package nc.bs.tg.alter.plugin.ebs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.itf.gl.fip.IVoucherSumRuleQryService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.fip.operatinglogs.OperatingFlagEmu;
import nc.vo.fip.operatinglogs.OperatingLogVO;
import nc.vo.fip.pub.SqlTools;
import nc.vo.fip.relation.FipRelationVO;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.org.AccountingBookVO;
import nc.vo.pub.BusinessException;

public class VoucherTaskUtils {
	static VoucherTaskUtils utils;
	
	public static VoucherTaskUtils newInstance(){
		if(utils==null){
			utils=new VoucherTaskUtils();
		}
		return utils;
	}
	private BaseDAO baseDAO;
	public BaseDAO getBaseDAO() {
		if(baseDAO==null){
			baseDAO=new BaseDAO();
		}
		return baseDAO;
	}
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	/**
	 * 查询基准
	 * 
	 * @param pk_group
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, ArrayList<AccountingBookVO>> getOrgInSetofbook()
			throws BusinessException {
		String condition = " enablestate = 2 and dr = 0 ";
		Collection<AccountingBookVO> coll = getBaseDAO().retrieveByClause(
				AccountingBookVO.class, condition);

		Map<String, ArrayList<AccountingBookVO>> bookMap = new HashMap<String, ArrayList<AccountingBookVO>>();
		if (coll != null && coll.size() > 0) {
			for (AccountingBookVO bookVO : coll) {
				if (!bookMap.containsKey(bookVO.getPk_setofbook())) {
					bookMap.put(bookVO.getPk_setofbook(),
							new ArrayList<AccountingBookVO>());
				}
				bookMap.get(bookVO.getPk_setofbook()).add(bookVO);
			}

		}

		return bookMap;

	}
	

	/**
	 * 获得临时凭证对应的处理状态VO
	 * 
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	public OperatingLogVO[] getRealTimeOperatingLogVOs()
			throws BusinessException {
		
			String wherepart = "isnull(dr,0)=0   and isnull(opmessage, '~') = '~' and operateflag = "
					+ OperatingFlagEmu.FLAG_saved
					+ " and (src_billtype  = 'F3-Cxx-001' OR src_billtype  = 'F2-Cxx-001')";
			Collection<OperatingLogVO> logvos = getBaseDAO().retrieveByClause(
					OperatingLogVO.class, wherepart);

			List<OperatingLogVO> volist = new ArrayList<OperatingLogVO>();

			if (logvos != null && logvos.size() > 0) {
				volist.addAll(logvos);
			}
			return volist == null || volist.size() == 0 ? null : volist
					.toArray(new OperatingLogVO[0]);
	}
	IVoucherSumRuleQryService qryService;
	/**
	 * 查询合并规则信息
	 * 
	 * @return
	 */
	public IVoucherSumRuleQryService getQryService() {
		if (qryService == null) {
			qryService = NCLocator.getInstance().lookup(
					IVoucherSumRuleQryService.class);
		}
		return qryService;
	}
	
	/**
	 * 读取凭证信息
	 * 
	 * @param volist
	 * @return
	 * @throws BusinessException
	 * @throws DAOException
	 */
	public Map<String, String> getVoucherMsg(List<OperatingLogVO> volist)
			throws DAOException, BusinessException {
		List<String> pklist = new ArrayList<String>();
		Map<String, String> result = new HashMap<String, String>();

		for (OperatingLogVO vo : volist) {
			pklist.add(vo.getSrc_relationid());
		}
		Collection<FipRelationVO> coll = getBaseDAO().retrieveByClause(
				FipRelationVO.class,
				SqlTools.getInStr(FipRelationVO.SRC_RELATIONID, pklist, true));

		pklist = new ArrayList<String>();
		if (coll != null && coll.size() > 0) {
			for (FipRelationVO vo : coll) {
				pklist.add(vo.getDes_relationid());
			}

			String sql = "select c.prepareddate  ,bd_vouchertype.name||' '||c.num voucherno  from gl_voucher c "
					+ " inner join bd_vouchertype on bd_vouchertype.pk_vouchertype = c.pk_vouchertype "
					+ " where "
					+ SqlTools.getInStr(VoucherVO.PK_VOUCHER, pklist, true);
			List<Map<String, String>> voucherresult = (List<Map<String, String>>) getBaseDAO()
					.executeQuery(sql, new MapListProcessor());
			String mergevoucher = "";
			String mergevoucherdate = "";
			if (voucherresult != null && voucherresult.size() > 0) {
				for (int i$ = 0; i$ < voucherresult.size(); i$++) {
					Map<String, String> map = voucherresult.get(i$);
					mergevoucher += map.get("voucherno");
					mergevoucherdate += map.get("prepareddate");
					if (i$ < (voucherresult.size() - 1)) {
						mergevoucher += ",";
						mergevoucherdate += ",";
					}

				}

			}
		
			result.put("mergevoucher", mergevoucher);// 凭证信息
			result.put("mergevoucherdate", mergevoucherdate);//生成时间

		}

		return result;
	}
}
