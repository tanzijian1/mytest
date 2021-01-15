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
		// TODO �Զ����ɵķ������
		return null;
	}

	/*
	 * ���˷�ʵ��ʱ������ݣ����ʷ����������ͣ����С������С��ʱ��г�ֻ����һ�����
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
					// TODO �Զ����ɵ� catch ��
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
						// TODO �Զ����ɵ� catch ��
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
						// TODO �Զ����ɵ� catch ��
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
	 * //���ʷ���������)
	 */
	public FinancingProVO setFinDate(FISchemeBVO vo, FinancingProVO coll) {
		// ������������
		coll.setScheduletype(vo.getDef13());
		// ����
		coll.setEstablishproject(vo.getVbdef1());
		// �����ϻ�
		coll.setBra_nchdate(vo.getVbdef2());
		// ʡ���ϻ�
		coll.setProvi_ncedate(vo.getVbdef3());
		// �����ϻ�
		coll.setHeadquartersdate(vo.getVbdef4());
		// ����ʱ��
		coll.setReplydate(vo.getVbdef5());
		// ��ͬǩ��ʱ��
		coll.setCostsigndate(vo.getVbdef6());
		return coll;

	}

	/*
	 * //���ʷ����������У�
	 */
	public FinancingProVO setNFinDate(NFISchemeBVO vo, FinancingProVO coll) {
		// ������������
		coll.setScheduletype(vo.getDef13());
		// --����
		coll.setNestablishproject(vo.getDef1());
		// ��ɾ���
		coll.setComplete(vo.getDef2());
		// �����
		coll.setReviewmeeting(vo.getDef3());
		// ����
		coll.setNreplydate(vo.getDef4());
		// �ʽ�����
		coll.setFunderapproval(vo.getDef5());
		// ����
		coll.setRecord(vo.getDef6());
		// ǩ��ͬ
		coll.setSigncontract(vo.getDef7());
		return coll;

	}

	/*
	 * //���ʷ����������г���
	 */
	public FinancingProVO setMarketDate(CapmarketBVO vo, FinancingProVO coll) {
		// ������������
		coll.setScheduletype(vo.getDef13());
		// ��ɾ���
		coll.setCapitalcomplete(vo.getDef1());
		// �ύ������
		coll.setSubmitexchange(vo.getDef2());
		// ����������ͨ��
		coll.setExchangepast(vo.getDef3());
		// �ļ����
		coll.setFilesealing(vo.getDef4());
		// �ύ֤���
		coll.setSubmitsfc(vo.getDef5());
		// ֤���ͨ��
		coll.setSfcpast(vo.getDef6());
		// ��ȡ����
		coll.setGainapproval(vo.getDef7());
		return coll;

	}

	/*
	 * ͨ��������ѯ��Ӧ�����ʷ�����ʵ��ʱ��
	 */

	public List<FISchemeBVO> getFISchemeBVO(String finCheme)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		// --�������ȣ����У�
		// --����
		sql.append(
				"select tgrz_fischeme_b.pk_scheme,(case when tgrz_fischeme_b.def13 ='ʵ�����' then '0' else '1' end) def13, tgrz_fischeme_b.vbdef1  ")
				// --�����ϻ�
				.append(",tgrz_fischeme_b.vbdef2  ")
				// --ʡ���ϻ�
				.append(",tgrz_fischeme_b.vbdef3  ")
				// --�����ϻ�
				.append(",tgrz_fischeme_b.vbdef4  ")
				// --����ʱ��
				.append(",tgrz_fischeme_b.vbdef5  ")
				// --��ͬǩ��
				.append(",tgrz_fischeme_b.vbdef6   from  tgrz_fischeme_b ")
				.append("where tgrz_fischeme_b.pk_scheme in ("
						+ finCheme
						+ ") and tgrz_fischeme_b.dr=0 and tgrz_fischeme_b.vbdef2<>'~' and tgrz_fischeme_b.def13 in ('ʵ�����','����') order by tgrz_fischeme_b.pk_scheme,tgrz_fischeme_b.vbdef2 ");
		List<FISchemeBVO> vos = (List<FISchemeBVO>) getBaseDAO().executeQuery(
				sql.toString(), new BeanListProcessor(FISchemeBVO.class));
		return vos;
	}

	/*
	 * ��ȡ���ʷ����������У���ʱ��ʱ��
	 */
	public List<NFISchemeBVO> getNFISchemeBVO(String finCheme)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();

		// --�������ȣ������У�
		// --����
		sql.append(
				"select tgrz_nfischeme_b.pk_scheme,(case when tgrz_nfischeme_b.def13 ='ʵ�����' then '0' else '1' end) def13,tgrz_nfischeme_b.def1  ")
				// --��ɾ���
				.append(",tgrz_nfischeme_b.def2  ")
				// --�����
				.append(",tgrz_nfischeme_b.def3  ")
				// --����
				.append(",tgrz_nfischeme_b.def4  ")
				// --�ʽ�����
				.append(",tgrz_nfischeme_b.def5  ")
				// --����
				.append(",tgrz_nfischeme_b.def6  ")
				// --ǩ��ͬ
				.append(",tgrz_nfischeme_b.def7   from  tgrz_nfischeme_b ")
				.append("where tgrz_nfischeme_b.pk_scheme in ("
						+ finCheme
						+ ") and tgrz_nfischeme_b.dr=0  and tgrz_nfischeme_b.def2<>'~' and tgrz_nfischeme_b.def13 in ('ʵ�����','����') order by tgrz_nfischeme_b.pk_scheme, tgrz_nfischeme_b.def2   ");
		List<NFISchemeBVO> vos = (List<NFISchemeBVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(NFISchemeBVO.class));
		return vos;
	}

	/*
	 * ��ȡ���ʷ����������У���ʱ��ʱ��
	 */
	public List<CapmarketBVO> getCapmarketBVO(String finCheme)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		// --�������ȣ��ʱ��г���
		// --��ɾ���
		sql.append(
				"select tgrz_capmarket_b.pk_scheme,(case when tgrz_capmarket_b.def13 ='ʵ�����' then '0' else '1' end) def13, tgrz_capmarket_b.def1  ")
				// --�ύ������
				.append(",tgrz_capmarket_b.def2  ")
				// --����������ͨ��
				.append(",tgrz_capmarket_b.def3  ")
				// --�ļ����
				.append(",tgrz_capmarket_b.def4  ")
				// --�ύ֤���
				.append(",tgrz_capmarket_b.def5  ")
				// --֤�������ͨ��
				.append(",tgrz_capmarket_b.def6  ")
				// --��ȡ����
				.append(",tgrz_capmarket_b.def7  from tgrz_capmarket_b ")
				.append("where tgrz_capmarket_b.pk_scheme in ("
						+ finCheme
						+ ") and tgrz_capmarket_b.dr=0  and tgrz_capmarket_b.def2<>'~' and tgrz_capmarket_b.def13 in ('ʵ�����','����') order by tgrz_capmarket_b.pk_scheme, tgrz_capmarket_b.def13  ");
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
