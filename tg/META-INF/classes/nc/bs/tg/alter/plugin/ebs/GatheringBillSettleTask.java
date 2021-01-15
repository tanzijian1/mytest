package nc.bs.tg.alter.plugin.ebs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.ra.common.UFDoubleUtil;
import nc.cmp.tools.UFDoubleUtils;
import nc.itf.fct.ar.IArMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.fct.ar.entity.AggCtArVO;
import nc.vo.fct.entity.CtArExecDetailVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 通用收入合同收款结算单收款完成之后更新收款合同执行情况页签
 * 
 * @author ln
 * 
 */
public class GatheringBillSettleTask implements IBackgroundWorkPlugin {

	IUAPQueryBS queryBS = null;
	IMDPersistenceQueryService mdQryService = null;
	IPFBusiAction pfBusiAction = null;
	BaseDAO baseDAO = null;

	private IUAPQueryBS getUAPQueryBS() {
		if (queryBS == null) {
			queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return queryBS;
	}

	private IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}

	private IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}
	
	private BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	@Override
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		// TODO 自动生成的方法存根
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String,String> pkMap = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT GA.PK_GATHERBILL,SETTLE.pk_settlement,GA.BILLNO,GA.DEF21,GA.MONEY, SETTLE.SETTLEDATE");
		sql.append("  FROM AR_GATHERBILL GA");
		sql.append("  LEFT JOIN CMP_SETTLEMENT SETTLE");
		sql.append("    ON SETTLE.PK_BUSIBILL = GA.PK_GATHERBILL");
		sql.append(" WHERE GA.APPROVESTATUS = 1");
		sql.append("   AND GA.BILLSTATUS = 8 ");
		sql.append("   AND GA.EFFECTSTATUS = 10");
		sql.append("   AND GA.SETTLEFLAG = 1");
		sql.append("   AND NVL(SETTLE.def2,'~') = '~'");
		sql.append("   AND GA.PK_TRADETYPE = 'F2-Cxx-008';");
		result = (List<Map<String, String>>) getUAPQueryBS().executeQuery(
				sql.toString(), new MapListProcessor());
		if (null == result || result.size() == 0)
			return null;
		List<String> sysnoList = new ArrayList<String>();// 合同编号
		for (Map<String, String> map : result) {
			String sysno = (map.get("def21") == null) ? null : map.get("def21")
					.toString();// 合同编号
			if (sysno != null && !"".equals(sysno)) {
				sysnoList.add(sysno);
			}
		}
		StringBuffer str = new StringBuffer();
		if (sysnoList.size() > 0) {
			for (int i = 0; i < sysnoList.size(); i++) {
				str.append("'" + sysnoList.get(i) + "'");
				if (i < sysnoList.size() - 1) {
					str.append(",");
				}
			}
			// 根据收款单相关的合同编号查询出所有收款合同
			List<AggCtArVO> list = (List<AggCtArVO>) getBillVO(
					AggCtArVO.class,
					"isnull(dr,0)=0 and blatest = 'Y' and pk_fct_ar in ("
							+ str.toString() + ")");
			// 过滤已删除的合同
			List<AggCtArVO> li = new ArrayList<AggCtArVO>();
			String primaryKey = null;
			for (AggCtArVO aggvo : list) {
				primaryKey = aggvo.getPrimaryKey();
				if (sysnoList.contains(primaryKey)) {
					li.add(aggvo);
				}
			}
			if (li != null && !li.isEmpty()) {
				HashMap<String, Object> eParam = new HashMap<String, Object>();
				CtArExecDetailVO[] execVOs = null;
				CtArExecDetailVO[] newexecVOs = null;
				CtArExecDetailVO execVO = new CtArExecDetailVO();
				List<CtArExecDetailVO> execList = null;
				UFDouble total = UFDouble.ZERO_DBL;
				UFDouble headMny = UFDouble.ZERO_DBL;
				String pk = null;
				String sysno = null;
				String billno = null;
				String settledate = null;
				String money = null;
				String crowno = null;
				String settlePK = null;
				String gatherPK = null;
				for (AggCtArVO aggvo : li) {
					pkMap = new HashMap<String,String>();
					execList = new ArrayList<CtArExecDetailVO>();
					pk = aggvo.getPrimaryKey();
					execVOs = aggvo.getCtArExecDetailVO();
					headMny = new UFDouble(aggvo.getParentVO().getVdef11());
					if (execVOs != null && execVOs.length > 0) {
						newexecVOs = new CtArExecDetailVO[execVOs.length + 1];
						for (int i = 0; i < execVOs.length; i++) {
							execList.add(execVOs[i]);
							execVOs[i].setStatus(VOStatus.UPDATED);
							newexecVOs[i] = execVOs[i];
							UFDouble amoney = new UFDouble(
									execVOs[i].getVbdef1());
							crowno = execVOs[i].getCrowno();
							total = UFDoubleUtils.add(total, amoney);
						}
						for (Map<String, String> map : result) {
							sysno = (map.get("def21") == null) ? null : map
									.get("def21").toString();// 合同编号
							if (sysno != null && !"".equals(sysno)
									&& sysnoList.contains(pk)  && sysno.equals(pk)) {
								settlePK = (map.get("pk_settlement") == null) ? null
										: map.get("pk_settlement").toString();//结算单主键
								gatherPK = (map.get("pk_gatherbill") == null) ? null
										: map.get("pk_gatherbill").toString();//收款单主键
								pkMap.put(gatherPK, settlePK);//用于更新Y标志
								billno = (map.get("billno") == null) ? null
										: map.get("billno").toString();// 收款单号
								settledate = (map.get("settledate") == null) ? null
										: map.get("settledate").toString();// 结算日期
								money = (map.get("money") == null) ? null
										: String.valueOf(map.get("money"));// 本次收款金额
								execVO.setPk_fct_ar(pk);
								execVO.setVbillcode(billno);
								execVO.setCrowno(String.valueOf(Integer.valueOf(crowno==null?"0":crowno)+10));
								execVO.setVbilldate(new UFDate(settledate));
								execVO.setVbdef1(money);
								execVO.setNorigpshamount(UFDoubleUtils.add(total,
										new UFDouble(money)));
								execVO.setCtrunarmny(UFDoubleUtils.sub(headMny,
										total));
								execVO.setStatus(VOStatus.NEW);
								newexecVOs[execVOs.length] = execVO;
								execList.add(execVO);// 可注释
								break;
							}
						}
					} else {
						newexecVOs = new CtArExecDetailVO[1];
						for (Map<String, String> map : result) {
							sysno = (map.get("def21") == null) ? null : map
									.get("def21").toString();// 合同编号
							if (sysno != null && !"".equals(sysno)
									&& sysnoList.contains(pk) && sysno.equals(pk)) {
								settlePK = (map.get("pk_settlement") == null) ? null
										: map.get("pk_settlement").toString();//结算单主键
								gatherPK = (map.get("pk_gatherbill") == null) ? null
										: map.get("pk_gatherbill").toString();//收款单主键
								pkMap.put(gatherPK, settlePK);//用于更新Y标志
								billno = (map.get("billno") == null) ? null
										: map.get("billno").toString();// 收款单号
								settledate = (map.get("settledate") == null) ? null
										: map.get("settledate").toString();// 结算日期
								money = (map.get("money") == null) ? null
										: String.valueOf(map.get("money"));// 本次收款金额
								execVO.setPk_fct_ar(pk);
								execVO.setVbillcode(billno);
								execVO.setCrowno(String.valueOf(Integer.valueOf(crowno==null?"0":crowno)+10));
								execVO.setVbilldate(new UFDate(settledate));
								execVO.setVbdef1(money);
								execVO.setNorigpshamount(new UFDouble(money));
								execVO.setCtrunarmny(UFDoubleUtils.sub(headMny,
										new UFDouble(money)));
								execVO.setStatus(VOStatus.NEW);
								newexecVOs[0] = execVO;
								break;
							}
						}

					}
					aggvo.setCtArExecDetailVO(newexecVOs);
					aggvo.getParentVO().setStatus(VOStatus.UPDATED);
//					getPfBusiAction().processAction(
//							"MODIFY", "FCT2", null,
//							aggvo, null, eParam);
					getBaseDAO().insertVO(execVO);
					getBaseDAO().executeUpdate("update CMP_SETTLEMENT set def2 = 'Y' where pk_settlement = '"+pkMap.get(gatherPK)+"'");
				}
			}
		}
		return null;
	}

	private List<AggCtArVO> getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false);
		List<AggCtArVO> aggvo = new ArrayList<AggCtArVO>();
		if (coll.size() > 0) {
			for (int i = 0; i < coll.size(); i++) {
				aggvo.add((AggCtArVO) coll.toArray()[i]);
			}
			return aggvo;
		} else {
			return null;
		}
	}

}
