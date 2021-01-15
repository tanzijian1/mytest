package nc.bs.tg.rz.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.tg.fischeme.CapmarketBVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.fischeme.NFISchemeBVO;
import nc.vo.tg.rz.report.FinancingProVO;

import com.ufida.dataset.IContext;

public class FinancingProParentUtils extends ReportUtils {
	Map<String, FISchemeBVO> finShemeMap = new HashMap<String, FISchemeBVO>();
	Map<String, NFISchemeBVO> nFIShemeMap = new HashMap<String, NFISchemeBVO>();
	Map<String, CapmarketBVO> marketMap = new HashMap<String, CapmarketBVO>();
	Map<String, FinancingProVO> financingMap = new HashMap<String, FinancingProVO>();
	List<String> finSheme = new ArrayList<String>();
	List<String> nfinSheme = new ArrayList<String>();
	List<String> marketSheme = new ArrayList<String>();

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}

	/*
	 * 过滤非实际时间的数据，融资方案三种类型：银行、非银行、资本市场只存在一种情况
	 */
	public List<FinancingProVO> filterFinDate(HashSet<String> setScheme,
			Collection<FinancingProVO> colls) throws BusinessException {
		List<FinancingProVO> listVO = new ArrayList<FinancingProVO>();
		for (FinancingProVO co : colls) {
			financingMap.put(co.getPk_scheme(), co);
		}

		String finChemes = changeData(setScheme);
		List<FISchemeBVO> fISchemeVO = getFISchemeBVO(finChemes);
		for (FISchemeBVO vo : fISchemeVO) {
			FinancingProVO coll = financingMap.get(vo.getPk_scheme());
			if (finSheme.contains(vo.getPk_scheme())) {
				try {
					coll = (FinancingProVO) coll.clone();
				} catch (CloneNotSupportedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

			}
			FinancingProVO co = setFinDate(vo, coll);
			listVO.add(co);
			finSheme.add(vo.getPk_scheme());

		}
		finChemes = changeData(setScheme);
		List<NFISchemeBVO> nFISchemeBVO = getNFISchemeBVO(finChemes);
		for (NFISchemeBVO vo : nFISchemeBVO) {
			FinancingProVO coll = financingMap.get(vo.getPk_scheme());
			if (!finSheme.contains(vo.getPk_scheme())) {
				if (nfinSheme.contains(vo.getPk_scheme())) {
					try {
						coll = (FinancingProVO) coll.clone();
					} catch (CloneNotSupportedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}

				FinancingProVO co = setNFinDate(vo, coll);
				listVO.add(co);
				nfinSheme.add(vo.getPk_scheme());
			}
		}
		finChemes = changeData(setScheme);
		List<CapmarketBVO> capmarketBVO = getCapmarketBVO(finChemes);
		for (CapmarketBVO vo : capmarketBVO) {
			FinancingProVO coll = financingMap.get(vo.getPk_scheme());
			if (!(finSheme.contains(vo.getPk_scheme()) && nfinSheme.contains(vo
					.getPk_scheme()))) {
				if (marketSheme.contains(vo.getPk_scheme())) {
					try {
						coll = (FinancingProVO) coll.clone();
					} catch (CloneNotSupportedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}

				FinancingProVO co = setMarketDate(vo, coll);
				listVO.add(co);
				marketSheme.add(vo.getPk_scheme());
			}

		}
		for (String fin : finSheme) {
			financingMap.remove(fin);
		}
		for (String fin : nfinSheme) {
			financingMap.remove(fin);
		}
		for (String fin : marketSheme) {
			financingMap.remove(fin);
		}
		for (FinancingProVO vo : financingMap.values()) {
			listVO.add(vo);
		}
		financingMap.clear();
		finSheme.clear();
		nfinSheme.clear();
		marketSheme.clear();
		return listVO;
	}

	/*
	 * //融资方案（银行)
	 */
	public FinancingProVO setFinDate(FISchemeBVO vo, FinancingProVO coll) {
		// 方案进度类型
		coll.setScheduletype(vo.getDef13());
		// 立项
		coll.setEstablishproject(vo.getVbdef1());
		// 分行上会
		coll.setBra_nchdate(vo.getVbdef2());
		// 省行上会
		coll.setProvi_ncedate(vo.getVbdef3());
		// 总行上会
		coll.setHeadquartersdate(vo.getVbdef4());
		// 批复时间
		coll.setReplydate(vo.getVbdef5());
		// 合同签定时间
		coll.setCostsigndate(vo.getVbdef6());
		return coll;

	}

	/*
	 * //融资方案（非银行）
	 */
	public FinancingProVO setNFinDate(NFISchemeBVO vo, FinancingProVO coll) {
		// 方案进度类型
		coll.setScheduletype(vo.getDef13());
		// --立项
		coll.setNestablishproject(vo.getDef1());
		// 完成尽调
		coll.setComplete(vo.getDef2());
		// 评审会
		coll.setReviewmeeting(vo.getDef3());
		// 批复
		coll.setNreplydate(vo.getDef4());
		// 资金方批复
		coll.setFunderapproval(vo.getDef5());
		// 备案
		coll.setRecord(vo.getDef6());
		// 签合同
		coll.setSigncontract(vo.getDef7());
		return coll;

	}

	/*
	 * //融资方案（融资市场）
	 */
	public FinancingProVO setMarketDate(CapmarketBVO vo, FinancingProVO coll) {
		// 方案进度类型
		coll.setScheduletype(vo.getDef13());
		// 完成尽调
		coll.setCapitalcomplete(vo.getDef1());
		// 提交交易所
		coll.setSubmitexchange(vo.getDef2());
		// 交易所审批通过
		coll.setExchangepast(vo.getDef3());
		// 文件封卷
		coll.setFilesealing(vo.getDef4());
		// 提交证监会
		coll.setSubmitsfc(vo.getDef5());
		// 证监会通过
		coll.setSfcpast(vo.getDef6());
		// 获取批文
		coll.setGainapproval(vo.getDef7());
		return coll;

	}

	/*
	 * 通过主键查询对应的融资方案的实际时间
	 */

	public List<FISchemeBVO> getFISchemeBVO(String finCheme)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		// --方案进度（银行）
		// --立项
		sql.append(
				"select tgrz_fischeme_b.pk_scheme,(case when tgrz_fischeme_b.def13 ='实际完成' then '0' else '1' end) def13, tgrz_fischeme_b.vbdef1  ")
				// --分行上会
				.append(",tgrz_fischeme_b.vbdef2  ")
				// --省行上会
				.append(",tgrz_fischeme_b.vbdef3  ")
				// --总行上会
				.append(",tgrz_fischeme_b.vbdef4  ")
				// --批复时间
				.append(",tgrz_fischeme_b.vbdef5  ")
				// --合同签订
				.append(",tgrz_fischeme_b.vbdef6   from  tgrz_fischeme_b ")
				.append("where tgrz_fischeme_b.pk_scheme in ("
						+ finCheme
						+ ") and tgrz_fischeme_b.dr=0 and tgrz_fischeme_b.vbdef2<>'~' and tgrz_fischeme_b.def13 in ('实际完成','天数') order by tgrz_fischeme_b.pk_scheme,tgrz_fischeme_b.vbdef2 ");
		List<FISchemeBVO> vos = (List<FISchemeBVO>) getBaseDAO().executeQuery(
				sql.toString(), new BeanListProcessor(FISchemeBVO.class));
		return vos;
	}

	/*
	 * 获取融资方案（非银行）的时间时间
	 */
	public List<NFISchemeBVO> getNFISchemeBVO(String finCheme)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();

		// --方案进度（非银行）
		// --立项
		sql.append(
				"select tgrz_nfischeme_b.pk_scheme,(case when tgrz_nfischeme_b.def13 ='实际完成' then '0' else '1' end) def13,tgrz_nfischeme_b.def1  ")
				// --完成尽调
				.append(",tgrz_nfischeme_b.def2  ")
				// --评审会
				.append(",tgrz_nfischeme_b.def3  ")
				// --批复
				.append(",tgrz_nfischeme_b.def4  ")
				// --资金方批复
				.append(",tgrz_nfischeme_b.def5  ")
				// --备案
				.append(",tgrz_nfischeme_b.def6  ")
				// --签合同
				.append(",tgrz_nfischeme_b.def7   from  tgrz_nfischeme_b ")
				.append("where tgrz_nfischeme_b.pk_scheme in ("
						+ finCheme
						+ ") and tgrz_nfischeme_b.dr=0  and tgrz_nfischeme_b.def2<>'~' and tgrz_nfischeme_b.def13 in ('实际完成','天数') order by tgrz_nfischeme_b.pk_scheme, tgrz_nfischeme_b.def2   ");
		List<NFISchemeBVO> vos = (List<NFISchemeBVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(NFISchemeBVO.class));
		return vos;
	}

	/*
	 * 获取融资方案（非银行）的时间时间
	 */
	public List<CapmarketBVO> getCapmarketBVO(String finCheme)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		// --方案进度（资本市场）
		// --完成尽调
		sql.append(
				"select tgrz_capmarket_b.pk_scheme,(case when tgrz_capmarket_b.def13 ='实际完成' then '0' else '1' end) def13, tgrz_capmarket_b.def1  ")
				// --提交交易所
				.append(",tgrz_capmarket_b.def2  ")
				// --交易所审批通过
				.append(",tgrz_capmarket_b.def3  ")
				// --文件封卷
				.append(",tgrz_capmarket_b.def4  ")
				// --提交证监会
				.append(",tgrz_capmarket_b.def5  ")
				// --证监会审批通过
				.append(",tgrz_capmarket_b.def6  ")
				// --获取批文
				.append(",tgrz_capmarket_b.def7  from tgrz_capmarket_b ")
				.append("where tgrz_capmarket_b.pk_scheme in ("
						+ finCheme
						+ ") and tgrz_capmarket_b.dr=0  and tgrz_capmarket_b.def2<>'~' and tgrz_capmarket_b.def13 in ('实际完成','天数') order by tgrz_capmarket_b.pk_scheme, tgrz_capmarket_b.def13  ");
		List<CapmarketBVO> vos = (List<CapmarketBVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(CapmarketBVO.class));
		return vos;
	}

	public String changeData(HashSet<String> set) {
		StringBuffer pk = new StringBuffer();
		for (String key : set) {
			pk.append("'").append(key).append("',");
		}
		String last = pk.toString().substring(0, pk.length() - 1);
		return last;

	}
}
