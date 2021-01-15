package nc.bs.tg.rz.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.RepAndPayVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * ���Ϣ��
 * 
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����3:55:32
 */
public class RepAndPayDataUtils extends RepAndPayUtils {
	static RepAndPayDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// ��ѯ�����µ�ֵ
	Map<String, String> queryWhereMap = new HashMap<String, String>();// ��ѯ�����µ�sql
	public static RepAndPayDataUtils getUtils() {
		if (utils == null) {
			utils = new RepAndPayDataUtils();
		}
		return utils;
	}

	public RepAndPayDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(new RepAndPayVO()));
	}

	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			List<RepAndPayVO>vos = getRepAndPayListVOs();
			
			if (vos != null && vos.size() > 0) 
				cmpreportresults = transReportResult(vos);
		

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}
/*
 * ������ǰ��ѯ���ʮ�����·�
 */
	private String getThisMonthSql() {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("cyear");
		sql.append(" ( ");
		for(int j=0;j<=4;j++){
			for (int i = 1; i <= 12; i++) {
				sql.append("   select '" + (Integer.parseInt(year)+j ) + "-" + String.format("%02d", i)
						+ "' datatype,'" + (Integer.parseInt(year)+j )+ "��" + String.format("%02d", i) +"��"
						+ "' datatypename from dual ");
				if (i != 12) {
					sql.append(" union all  ");
				}
			}
			if (j != 4){
				sql.append(" union all  ");
			}
	}
			

		sql.append(" ) ");

		return sql.toString();

	}

	private String getNAddYearSql() {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("cyear");
		sql.append(" ( ");
		for (int i = 0; i <= 4; i++) {
			sql.append("   select '" + (Integer.parseInt(year) + i)
					+ "' datatype,'" + (Integer.parseInt(year) + i)+"��"
					+ "' datatypename from dual ");
			if (i != 4) {
				sql.append(" union all  ");
			}
		}

		sql.append(" ) ");

		return sql.toString();

	}
	private List<RepAndPayVO> getRepAndPayListVOs() throws BusinessException{
		
		StringBuffer sql = new StringBuffer();
		//��ȡ���Ϣ�������ѯ�ֶ�
		sql.append(" "+getRepAndPayField() +" ")
		
	//�ۼƷſ���
	.append("(select  sum(execfk.payamount) from  cdm_contract_exec execfk where execfk.pk_contract = cdm_contract.pk_contract")
	.append(" and execfk.summary = 'FBJ'  and substr(execfk.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  loantatol_amount, ")
	//�ۼƻ�����
	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
	.append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"   )  yeartatol_amount, ") //contnote
	
		//�����¶ȶȻ����ƻ����ֽ������Ǻ�ͬԼ������̬��ȡ�ֶ�
		.append(" "+getMothRepayField()+" ")
		//���ӻ�������
		//.append(" cdm_contract.vdef13 rePaycondition, ")
		// null repyearplantotal_amount,null repyearactualtotal_amount,null ipyearactualtotal_amount,null advyearplantotal_amount, ")
		//.append("null advyearactualtotal_amount
		//��ȡ���Ϣ��ؽ���ֶ�
		.append(""+ getMonthPlanAmount()+"")
		//��ȡ��ص����ӱ�
		.append(" "+getLinkTable()+" ")
		//������ǰ��ѯ���ʮ�����·�
		.append("inner join "+getThisMonthSql() +" v1 on 1=1 ")
		//�����¶ȶȻ����ƻ����ֽ������Ǻ�ͬԼ������̬��ȡ����
		//.append(""+getMonthRepayplanLink() +"")
		//.append("left join cdm_repayplan on cdm_repayplan.pk_contract = cdm_contract.pk_contract and substr(cdm_repayplan.repaydate,0,7) =datatype  ")
		//�����·ݻ�ȡ����ִ�б�������ʷѵ����ӱ�
		//.append(" "+ getMonthTable()+" "  )
		//��Ӳ�ѯ����
		.append(" "+getWhereSql()+ " ");
		//��ȡ�����ֶ�
		sql.append(" "+getGroupBy()+" ");
		
		/*
		 * ����
		 */
		
		sql.append("union all ")
	     //��ȡ���Ϣ�������ѯ�ֶ�
		.append(" "+getRepAndPayField() +"  ")
			//�ۼƷſ���
	.append("(select  sum(execfk.payamount) from  cdm_contract_exec execfk where execfk.pk_contract = cdm_contract.pk_contract")
	.append(" and execfk.summary = 'FBJ'  and substr(execfk.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  loantatol_amount, ")
	
	//�ۼƻ�����
	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
	.append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  yeartatol_amount, ") //contnote
		.append(" "+getYearRepayField()+" ")
		//���ӻ�������
		//.append(" cdm_contract.vdef13 rePaycondition, ")
		 //��ȡ���Ϣ��ؽ���ֶ�
		.append(""+ getYearPlanAmount()+"")
		 //��ȡ��ص����ӱ�
		.append(" "+getLinkTable()+" ")
		
		.append("inner join "+getNAddYearSql() +" v1 on 1=1 ")
		//.append(""+getYearRepayplanLink() +"")
		//.append("left join cdm_repayplan on cdm_repayplan.pk_contract = cdm_contract.pk_contract and substr(cdm_repayplan.repaydate,0,4) =datatype  ")
		//.append(" "+ getYearTable()+" "  )
		//��Ӳ�ѯ����
		.append(" "+getWhereSql()+ " ");
		//��ȡ�����ֶ�
		sql.append(" "+getGroupBy()+" ");
		List<RepAndPayVO> volist = (List<RepAndPayVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(RepAndPayVO.class));
		Set<String> keySet = new HashSet<String>();
		if(volist.size()>0){
			for (RepAndPayVO repAndPayVO : volist) {
				keySet.add(repAndPayVO.getCode());
			}
		}
		Map<String,Object> repayMap = null;
		repayMap = getContnote(keySet.toArray(new String[0]));
		//��ȡ�����������
		for(RepAndPayVO vo:volist){
//			UFDouble	fin_amount =UFDouble.ZERO_DBL; 
			vo.setContnote(repayMap.get(vo.getCode())==null?null:repayMap.get(vo.getCode()).toString());
//			UFDouble loantatol = vo.getLoantatol_amount();//�ۼƷſ�
//			UFDouble yeartatol = vo.getYeartatol_amount();//�ۼƻ���
//			vo.setContnote(contnote);
//			fin_amount = vo.getLoantatol_amount() != null ? new UFDouble(
//					vo.getLoantatol_amount()).sub(vo
//					.getContnote() != null ? new UFDouble(vo.getContnote())
//					: UFDouble.ZERO_DBL) : UFDouble.ZERO_DBL;
//					vo.setContnote(fin_amount.setScale(2, UFDouble.ROUND_CEILING).toString());		
		}
		return volist;
		
	}
	
	public Map<String, Object> getContnote(String[] keys) throws BusinessException{
		Map<String,Object> infoMap = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select cdm_contract.contractcode contractcode,sum(cdm_contract_exec.payamount) payamount,repay.repayamount,sum(nvl(cdm_contract_exec.payamount,0)) - nvl(repay.repayamount,0) contnote from cdm_contract ");
		sql.append(" left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract and cdm_contract_exec.dr = 0 ");
		sql.append(" left join ( ");
		sql.append(" select sum(NVL(cdm_repayrcpt_b.repayamount, 0)) repayamount,cdm_repayrcpt.pk_contract from cdm_repayrcpt ");
		sql.append(" left join  cdm_repayrcpt_b on cdm_repayrcpt.pk_repayrcpt = cdm_repayrcpt_b.pk_repayrcpt and cdm_repayrcpt_b.dr = 0  ");
		sql.append(" where cdm_repayrcpt.vbillstatus = 1 ");
		sql.append(" group by ");
		sql.append(" cdm_repayrcpt.pk_contract ");
		sql.append(" ) repay ");
		sql.append(" on cdm_contract.pk_contract = repay.pk_contract ");
		sql.append(" and  "+SQLUtil.buildSqlForIn(" cdm_contract.contractcode", keys ));
		sql.append(" group by ");
		sql.append(" cdm_contract.contractcode, ");
		sql.append(" repay.repayamount ");
		List<Map<String,Object>> listvo = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(
						sql.toString(),new MapListProcessor());
		if(listvo.size()>0){
			for (Map<String, Object> map : listvo) {
				infoMap.put(map.get("contractcode").toString(), map.get("contnote"));
			}
		}
		return infoMap;
	}
	
	/**
	 * ������Ȼ����ƻ����ֽ������Ǻ�ͬԼ������̬��ȡ�ֶ�
	 * 
	 * 
	 */
	private String getYearRepayField(){
		StringBuffer 	field=new StringBuffer();
		if("��ͬԼ��".equals(queryValueMap.get("plan"))){
			//N��/n+x��ƻ�������
			field.append("(select   sum(nvl(cdm_repayplan.preamount, 0)) from ")
			.append("cdm_repayplan where cdm_repayplan.pk_contract=cdm_contract.pk_contract   ")
			.append("and substr(cdm_repayplan.repaydate, 0, 4) = v1.datatype  and cdm_repayplan.dr='0' ) repmonplantotal_amount, ")
			//N��/n+x�ƻ���Ϣ���
			.append("(select sum(nvl(cdm_repayplan.preinterest, 0)) from  ")
			.append("cdm_repayplan where cdm_repayplan.pk_contract=cdm_contract.pk_contract  ")
			.append("and substr(cdm_repayplan.repaydate, 0, 4) = v1.datatype and cdm_repayplan.dr='0' ) ipmonplantotal_amount, ");
			/*field = " sum(nvl(cdm_repayplan.preamount, 0)) repmonplantotal_amount,"//N��/n+x��ƻ�������
				+ "sum(nvl(cdm_repayplan.preinterest, 0)) ipmonplantotal_amount, "; //N��/n+x�ƻ���Ϣ���
*/		}else if("�ֽ���".equals(queryValueMap.get("plan"))){
			//N��/n+x��ƻ�������
			field.append("(select  sum(nvl(cdm_cdjh.m_yjhbj, 0)) from  cdm_cdjh  ")
			.append("where  cdm_cdjh.pk_contract=cdm_contract.pk_contract  ")
			.append("and substr(cdm_cdjh.d_hkrq,0,4) =v1.datatype and cdm_cdjh.dr='0' ) repmonplantotal_amount,")
			//N��/n+x�ƻ���Ϣ���
			.append("(select  sum(nvl(cdm_cdjh.m_yjhlx, 0)) from  cdm_cdjh  ")
			.append("where  cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
			.append("and substr(cdm_cdjh.d_hkrq,0,4) =v1.datatype and cdm_cdjh.dr='0' ) ipmonplantotal_amount, ");
			/*field = "sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal_amount,"//N��/n+x��ƻ�������
					+ "sum(nvl(cdm_cdjh.m_yjhlx, 0)) ipmonplantotal_amount, ";//N��/n+x�ƻ���Ϣ���
*/		}
		return field.toString();
	}
	/**
	 * �����¶ȶȻ����ƻ����ֽ������Ǻ�ͬԼ������̬��ȡ�ֶ�
	 * 
	 * 
	 */
	private String getMothRepayField(){//�ƻ������𣬼ƻ�����Ϣ
	StringBuffer 	field=new StringBuffer();
		if("��ͬԼ��".equals(queryValueMap.get("plan"))){
			//N��/n+x��ƻ�������
			field.append("(select   sum(nvl(cdm_repayplan.preamount, 0)) from ")
			.append("cdm_repayplan where cdm_repayplan.pk_contract=cdm_contract.pk_contract   ")
			.append("and substr(cdm_repayplan.repaydate, 0, 7) = v1.datatype  and cdm_repayplan.dr='0' ) repmonplantotal_amount, ")
			//N��/n+x�ƻ���Ϣ���
			.append("(select sum(nvl(cdm_repayplan.preinterest, 0)) from  ")
			.append("cdm_repayplan where cdm_repayplan.pk_contract=cdm_contract.pk_contract  ")
			.append("and substr(cdm_repayplan.repaydate, 0, 7) = v1.datatype and cdm_repayplan.dr='0'  ) ipmonplantotal_amount, ");
			//field = " sum(nvl(cdm_repayplan.preamount, 0)) repmonplantotal_amount,"//N��/n+x��ƻ�������
				//+ "sum(nvl(cdm_repayplan.preinterest, 0)) ipmonplantotal_amount, "; //N��/n+x�ƻ���Ϣ���
		}else if("�ֽ���".equals(queryValueMap.get("plan"))){
			field.append("(select  sum(nvl(cdm_cdjh.m_yjhbj, 0)) from  cdm_cdjh  ")
			.append("where  cdm_cdjh.pk_contract=cdm_contract.pk_contract  ")
			.append("and substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype and cdm_cdjh.dr='0' ) repmonplantotal_amount,")
			//N��/n+x�ƻ���Ϣ���
			.append("(select  sum(nvl(cdm_cdjh.m_yjhlx, 0)) from  cdm_cdjh  ")
			.append("where  cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
			.append("and substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype and cdm_cdjh.dr='0' ) ipmonplantotal_amount, ");
			//field = "sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal_amount,"//N��/n+x��ƻ�������
					//+ "sum(nvl(cdm_cdjh.m_yjhlx, 0)) ipmonplantotal_amount, ";//N��/n+x�ƻ���Ϣ���
		}
		return field.toString();
	}
	
	/*
	 * ������ݻ�ȡ����ִ�б�������ʷѵ����ӱ�	
	 */
	private String getYearTable(){
		StringBuffer repAndPaySql=new StringBuffer();
		//���д����ͬ��ϸ���ݵĵ�ִ�б�������ڲ�ѯ������
		repAndPaySql.append("left join  cdm_contract_exec  execbj on execbj.pk_contract = cdm_contract.pk_contract ")
		.append("and  execbj.summary='HBJ' and substr(execbj.busidate,0,4) =  v1.datatype ")
		 //���д����ͬ��ϸ���ݵĵ�ִ�б�������ڲ�ѯ�ѻ�������
/*		.append("left join  cdm_contract_exec  execfk on execfk.pk_contract = cdm_contract.pk_contract and execfk.summary='FBJ' ")	
		.append(" and substr(execfk.busidate,0,4) = "+queryValueMap.get("cyear")+"  ")	*/
		//���д����ͬ��ϸ���ݵĵ�ִ�б�������ڲ�ѯ�ѻ���Ϣ
		.append("left join  cdm_contract_exec  execlx on execlx.pk_contract = cdm_contract.pk_contract and execlx.summary='FLX' ")	
		.append("and substr(execlx.busidate,0,4) = v1.datatype ")	
		 //�ƻ�������ʷ��ý��
		.append("left join cdm_cwgwfzfjh on    cdm_cwgwfzfjh.pk_contract= cdm_contract.pk_contract ")	
		.append("and  substr(cdm_cwgwfzfjh.d_zfsj,0,4) = v1.datatype  ")	
		 //ʵ�ʲ�����ʷ��ý��
		.append("left join cdm_cwgwfzxqk on    cdm_cwgwfzxqk.pk_contract= cdm_contract.pk_contract ")
		.append("and substr(cdm_cwgwfzxqk.d_zfsj,0,4)=  v1.datatype  ");
		return repAndPaySql.toString();
		
	}
	/*
	 * ��ȡ�����ֶ�
	 */
	public String getGroupBy(){
		StringBuffer groupSql =new StringBuffer();
		 	//
		 groupSql.append("group by contractcode,tgrz_projectdata.pk_projectdata ,tgrz_projectdata.code,tgrz_projectdata.name, ")
		.append("tgrz_projectdata_c.pk_projectdata_c,tgrz_projectdata_c.periodizationname,projectcorp,pk_debitorg, ")
		.append("tgrz_fintype.pk_fintype,tgrz_fintype.code,tgrz_fintype.name ,cdm_contract.vdef13, ")
		.append("tgrz_organization.code, tgrz_organization.pk_organization,tgrz_organization.name, ")
		.append("contractamount, begindate,enddate,returnmode,pk_htlv,lxzffs, cdm_contract.pk_contract,returnmode.name,cwgwfzffs,i_cwgwfl,v1.datatype ");//execfk.payamount,
		//.append("execbj.repayamount,execlx.payinterest,cdm_cwgwfzfjh.m_zfje,cdm_cwgwfzxqk.m_zfje,v1.datatype ");
		/* if(StringUtils.isNotBlank(queryValueMap.get("plan")))
			 groupSql.append(",cdm_repayplan.preamount ");*/
		return groupSql.toString();
		
		
	}
	/*
	 * ���ݽ����ѯģ��������Ĳ�ѯ����������֯sql���
	 */
	public  String getWhereSql(){
		StringBuffer whereSql= new StringBuffer();
	 whereSql.append("where '1'='1' and cdm_contract.dr=0 and  cdm_contract.contstatus='EXECUTING' ");
	 	//�����Ŀ��������
	 if(queryValueMap.get("pk_projectdata") != null){
			whereSql.append(" "+queryWhereMap.get("pk_projectdata")+"" );
		}
	 	//�����Ŀ����
	 if(queryValueMap.get("pk_projectdata_c") != null){
			whereSql.append(" "+queryWhereMap.get("pk_projectdata_c")+"" );
		}
	 	//��Ŀ��˾����
	 if(queryValueMap.get("orgname") != null){
			whereSql.append(" "+queryWhereMap.get("orgname")+"" );
		}
	 	//�����������
	 if(queryValueMap.get("pk_fintype") != null){
			whereSql.append(" "+queryWhereMap.get("pk_fintype")+"" );
		}
	 //�����
	 if(queryValueMap.get("borrower") != null){
			whereSql.append(" "+queryWhereMap.get("borrower")+"" );
		}
	 	//��ӻ���
	 if(queryValueMap.get("pk_organization") != null){
			whereSql.append(" "+queryWhereMap.get("pk_organization")+"" );
		}
		return whereSql.toString();
	}
	/**
	 * ��ʼ����ѯ������Ϣ
	 * 
	 * @param condVOs
	 */
	private void initQuery(ConditionVO[] condVOs) {
		queryValueMap.clear();
		queryWhereMap.clear();
		//List<String> filed =	Arrays.asList(new String []{"pk_projectdata","pk_projectdata_c"});
		for (ConditionVO condVO : condVOs) {
			queryValueMap.put(condVO.getFieldCode(), condVO.getValue());
			if(condVO.getSQLStr()==null){
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}else if("pk_projectdata".equals(condVO.getFieldCode())){
				//��Ŀ����
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace(condVO.getFieldCode(), "tgrz_projectdata."+condVO.getFieldCode()+""));
			}else if("pk_projectdata_c".equals(condVO.getFieldCode())){
				//��Ŀ����
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("pk_projectdata_c", "tgrz_projectdata_c.pk_projectdata_c"));
			}else if("orgname".equals(condVO.getFieldCode())){
				//��Ŀ��������
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("orgname", "cdm_contract.pk_xmgs"));
			}else if("borrower".equals(condVO.getFieldCode())){
				//�����
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("borrower", "cdm_contract.pk_debitorg"));
			}else if("pk_fintype".equals(condVO.getFieldCode())){
				//��������
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("pk_fintype", "tgrz_fintype.pk_fintype"));
			}else if("pk_organization".equals(condVO.getFieldCode())){
				//���ʻ���
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("pk_organization", "tgrz_organization.pk_organization"));
			}else{
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}
			
			
		}
	}
}
