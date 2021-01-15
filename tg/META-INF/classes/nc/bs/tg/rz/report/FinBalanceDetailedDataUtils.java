package nc.bs.tg.rz.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.rz.report.TGReprotContst;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinBalanceDetailedVO;

import com.ufida.dataset.IContext;

/**
 * ���������ϸ��
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����3:46:56
 */
public class FinBalanceDetailedDataUtils extends
		FinBalanceAndFinCloseDetailUtil {
	static FinBalanceDetailedDataUtils utils;
	IMDPersistenceQueryService mdQryService = null;
	Map<String, String> queryValueMap = new HashMap<String, String>();// ��ѯ�����µ�ֵ
	Map<String, String> queryWhereMap = new HashMap<String, String>();// ��ѯ�����µ�sql

	public static FinBalanceDetailedDataUtils getUtils() {
		if (utils == null) {
			utils = new FinBalanceDetailedDataUtils();
		}
		return utils;
	}

	public FinBalanceDetailedDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinBalanceDetailedVO()));
	}

	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;
		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(TGReprotContst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			FinBalanceDetailedVO[] vos = getMortageListVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	private FinBalanceDetailedVO[] getMortageListVOs() throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append("" + getCommonField(queryValueMap) + " ")

				// �ſ�ʱ��=xx��xx��xx�շſ�xxԪ
				/*
				 * .append(",substr(cdm_contract_exec.busidate, 0, 4) || '��' " +
				 * "||substr(cdm_contract_exec.busidate, 6, 2) || '��' " +
				 * "||substr(cdm_contract_exec.busidate, 9, 2) || '�շſ�' " +
				 * "||to_char((nvl(cdm_contract_exec.payamount, 0) / 10000),'fm99999990.009999')||'��Ԫ' loandate"
				 * )
				 */
				// .append(" from cdm_contract")
				// .append(" left  join tgrz_projectdata on tgrz_projectdata.pk_projectdata = cdm_contract.pk_project")
				// .append(" left  join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract")
				// .append(" left  join tgrz_organization on tgrz_organization.pk_organization = cdm_contract.pk_rzjg")
				// .append(" left  join tgrz_OrganizationType on tgrz_OrganizationType.pk_organizationtype = cdm_contract.pk_rzjglb")
				// .append(" left  join tgrz_fintype on tgrz_fintype.pk_fintype = cdm_contract.pk_rzlx")
				// .append(" left  join tgrz_projectdata_c on tgrz_projectdata_c.pk_projectdata_c = cdm_contract.vdef3")//
				// .append(" left  join org_financeorg on org_financeorg.pk_financeorg = cdm_contract.pk_debitorg")
				.append("" + getLeftTable(queryValueMap) + " ")
				// .append(" where cdm_contract_exec.leftrepayamount>0 and cdm_contract_exec.pk_billtypecode = '36FE' ")//36FE��ʾ�ſ��
				.append(" where cdm_contract.contstatus in ('EXECUTING','FINISHED')   ")
				.append(" and cdm_contract_exec.payamount -   ")
				.append(" NVL((select sum(NVL(cdm_repayrcpt_b.repayamount, 0))   ")
				.append(" from cdm_repayrcpt_b   ")
				.append(" right join cdm_repayrcpt   ")
				.append(" on cdm_repayrcpt_b.pk_repayrcpt =   ")
				.append(" cdm_repayrcpt.pk_repayrcpt   ")
				.append(" and cdm_repayrcpt.vbillstatus = '1'   ")
				.append(" where cdm_repayrcpt_b.pk_payrcpt =   ")
				.append(" cdm_payrcpt.pk_payrcpt   ")
				.append(" and cdm_repayrcpt_b.dr = 0   ")
				.append(" and substr(cdm_repayrcpt.repaydate, 1, 4) <= '"+queryValueMap.get("busidate")+"'),   ")
				.append(" 0) >0   ")
				.append(" and nvl(cdm_contract.dr,0) = 0 ")
				.append(" and substr(cdm_contract_exec.busidate, 1, 4) <= '"
						+ queryValueMap.get("busidate") + "' ")
				.append(" and cdm_contract.pk_contract in "
						+ " (select pk_contract from cdm_contract_exec where substr(cdm_contract_exec.busidate, 1, 4) <= '"+queryValueMap.get("busidate")+"' group by pk_contract   "
						+ " having sum(nvl(payamount, 0)) > sum(nvl(repayamount, 0))) ");
		/*
		 * if(queryValueMap.get("busidate")!=null){//���
		 * sqlBuff.append(" and substr(cdm_contract_exec.busidate,0,4) in "
		 * +queryValueMap.get("busidate")+""); }
		 */
		if (queryValueMap.get("pk_project") != null) {// ��Ŀ����
			/*
			 * sqlBuff.append(" and " +
			 * SQLUtil.buildSqlForIn("tgrz_projectdata.name",
			 * queryValueMap.get("projectname").split(",")));
			 */
			/*
			 * sqlBuff.append(" and tgrz_projectdata.pk_projectdata in "+
			 * queryValueMap.get("pk_project")+"");
			 */
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata.pk_projectdata",
							queryValueMap.get("pk_project").split(",")));
		}
		if (queryValueMap.get("pk_periodization") != null) {// ��������
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata_c.pk_projectdata_c",
							queryValueMap.get("pk_periodization").split(",")));
		}
		if (queryValueMap.get("projectcorp") != null) {// ��Ŀ��˾����
			sqlBuff.append(" and "
					+ buildSqlForIn(
							"tgrz_projectdata.projectcorp",
							queryValueMap.get("projectcorp").split(",")));
		}
		if (queryValueMap.get("pk_borrower") != null) {// �����
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"org_financeorg.pk_financeorg",
							queryValueMap.get("pk_borrower").split(",")));
		}
		if (queryValueMap.get("pk_fintype") != null) {// ��������
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_fintype.pk_fintype",
							queryValueMap.get("pk_fintype").split(",")));
		}
		if (queryValueMap.get("pk_organization") != null) {// ���ʻ���
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_organization.pk_organization",
							queryValueMap.get("pk_organization").split(",")));
		}
		if (queryValueMap.get("pk_organizationtype") != null) {// ���ʻ������
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_OrganizationType.pk_organizationtype",
							queryValueMap.get("pk_organizationtype").split(",")));
		}
		if (queryValueMap.get("isabroad") != null) {// �Ƿ�������
			sqlBuff.append(" and nvl(cdm_contract.b_jwrz,'N') = '"
					+ queryValueMap.get("isabroad") + "'");
		}
		if (queryValueMap.get("repaymenttypename") != null) {// ���ʽ
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"returnmode.pk_defdoc",
							queryValueMap.get("repaymenttypename").split(",")));
		}
		if (queryValueMap.get("mortgageepro") != null) {// ��Ѻ��
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"doc21.pk_defdoc",
							queryValueMap.get("mortgageepro").split(",")));
		}
		if (queryValueMap.get("mortgageerate") != null) {// ��Ѻ��
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"doc22.pk_defdoc",
							queryValueMap.get("mortgageerate").split(",")));
		}
		if (queryValueMap.get("accountregulation") != null) {// �˻����
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"doc39.pk_defdoc",
							queryValueMap.get("accountregulation").split(",")));
		}
		if (queryValueMap.get("offsealcertificate") != null) {// ����֤�չ���
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"doc40.pk_defdoc",
							queryValueMap.get("offsealcertificate").split(",")));
		}
		if (queryValueMap.get("moneyprecipitation") != null) {// �����ʽ�
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"doc41.pk_defdoc",
							queryValueMap.get("moneyprecipitation").split(",")));
		}
		
		if (queryValueMap.get("begindate") != null) {// ������ʼ��
			sqlBuff.append(" and substr(cdm_contract.begindate, 0, 10) >= '"+queryValueMap.get("begindate").substring(0,11)+"' ");
		}else {
			sqlBuff.append(" and substr(cdm_contract.begindate, 0, 10)  >= '1970-01-01' ");
		}
		if (queryValueMap.get("enddate") != null) {// ���ʵ�����
			sqlBuff.append(" and substr(cdm_payplan.vdef2, 1, 10) <= '"+queryValueMap.get("enddate").substring(0,11)+"' ");
		}else {
			sqlBuff.append(" and substr(cdm_payplan.vdef2, 1, 10) <= '9999-01-01' ");
		}
		if (queryValueMap.get("contratio") != null) {// ��ͬ����(%)
			sqlBuff.append(" and cdm_contract.pk_htlv >= '"+queryValueMap.get("contratio")+"' ");
		}
		if (queryValueMap.get("adviserratio") != null) {// �ƹ˷�����(%)
			sqlBuff.append(" and cdm_contract.i_cwgwfl*100 >= '"+queryValueMap.get("adviserratio")+"' ");
		}
		if (queryValueMap.get("comprehensiveratio") != null) {// �ۺ�����(%)
			sqlBuff.append(" and (nvl(cdm_contract.pk_htlv / 100, 0) + nvl(cdm_contract.i_cwgwfl, 0))*100 >= '"+queryValueMap.get("comprehensiveratio")+"' ");
		}
		
		sqlBuff.append(" and cdm_contract.dr=0 order   by contractcode,loandate,begindate");
		// sqlBuff.append(" "+ getGroupBy()+" ");
		Collection<FinBalanceDetailedVO> coll = (Collection<FinBalanceDetailedVO>) getBaseDAO()
				.executeQuery(sqlBuff.toString(),
						new BeanListProcessor(FinBalanceDetailedVO.class));
		ArrayList<FinBalanceDetailedVO> list = (ArrayList<FinBalanceDetailedVO>) coll;
		// FinBalanceDetailedVO[] vos = coll.toArray(new
		// FinBalanceDetailedVO[0]);
		int j = 0;
		Map<String, Integer> sotnumMap = new HashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			String key = (list.get(i).getContractcode()
					+ list.get(i).getPk_project() + list.get(i)
					.getProjectname());
			if (!sotnumMap.containsKey(key)) {
				j++;
				sotnumMap.put(key, j);

			}
			list.get(i).setSortnumglobal(new UFDouble(sotnumMap.get(key)));
		}
		// int i = 1;
		// for (FinBalanceDetailedVO finBalanceDetailedVO : coll) {
		// String sortnum = Integer.toString(i);
		// finBalanceDetailedVO.setSortnum(sortnum);
		// }

		return list != null && list.size() > 0 ? list
				.toArray(new FinBalanceDetailedVO[0]) : null;
	}

	/**
	 * ��ʼ����ѯ������Ϣ
	 * 
	 * @param condVOs
	 */
	private void initQuery(ConditionVO[] condVOs) {
		queryValueMap.clear();
		queryWhereMap.clear();
		for (ConditionVO condVO : condVOs) {
			if(condVO.getSQLStr()==null){
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}else{
			queryValueMap.put(condVO.getFieldCode(),
					condVO.getValue().replace("(", "").replace(")", "")
							.replace("'", ""));
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}
		}
	}
	
	/**
	 * ��ȡҵ�񵥾ݾۺ�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public List<AggregatedValueObject> getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		List<AggregatedValueObject> coll = (List<AggregatedValueObject>) getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return coll;
		} else {
			return null;
		}
	}

	/**
	 * Ԫ���ݳ־û���ѯ�ӿ�
	 * 
	 * @return
	 */
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}
}
