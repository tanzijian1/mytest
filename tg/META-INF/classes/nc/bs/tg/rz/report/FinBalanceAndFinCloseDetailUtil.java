
package nc.bs.tg.rz.report;

import java.util.Map;

import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

public class FinBalanceAndFinCloseDetailUtil extends ReportUtils {

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		// TODO �Զ����ɵķ������
		return null;
	}
	
	public String getCommonField(Map<String, String> queryValueMap) {
		StringBuffer  commomSql = new  StringBuffer();
		commomSql.append(" select distinct cdm_contract.contractcode contractcode, ");//�����ͬ���
		commomSql.append(" cdm_contract.pk_contract pk_contract, ");//�����ͬpk
		commomSql.append(" tgrz_projectdata.pk_projectdata pk_project, ");//��Ŀpk
		commomSql.append(" tgrz_projectdata.code projectcode, ");//��Ŀ����
		commomSql.append(" tgrz_projectdata.name projectname, ");//��Ŀ����
		commomSql.append(" tgrz_projectdata_c.pk_projectdata_c pk_periodization, ");//��������
		commomSql.append(" tgrz_projectdata_c.periodizationname periodizationname, ");//��������
		commomSql.append(" tgrz_projectdata.projectcorp projectcorp, ");//��Ŀ���ڹ�˾(��Ŀ��˾����)
		commomSql.append(" org_financeorg.pk_financeorg pk_financeorg, ");//��λid
		commomSql.append(" org_financeorg.name borrower, ");//�����(��λ)
		commomSql.append(" tgrz_fintype.pk_fintype, ");//��������
		commomSql.append(" tgrz_fintype.code fintypecode, ");//�������ͱ���tgrz_organization
		commomSql.append(" tgrz_fintype.name fintypename, ");//������������
		commomSql.append(" tgrz_organization.pk_organization pk_organization, ");//���ʻ���
		commomSql.append(" tgrz_organization.code organizationcode, ");//��������
		commomSql.append(" tgrz_organization.name organizationname, ");//��������
		commomSql.append(" tgrz_OrganizationType.pk_organizationtype pk_organizationtype, ");//���ʻ�������
		commomSql.append(" tgrz_OrganizationType.code organizationtypecode, ");//���ʻ�������
		commomSql.append(" tgrz_OrganizationType.name organizationtypename, ");//���ʻ�������
		commomSql.append(" tgrz_organization.branch branch, ");//��������
		commomSql.append(" tgrz_organization.subbranch subbranch, ");//����֧��
		commomSql.append(" decode(cdm_contract.b_jwrz, 'Y', '��', 'N', '��') isabroad, ");//�Ƿ�������
		commomSql.append(" cdm_contract.pk_htlv / 100 contratio, ");//��ͬ����
		commomSql.append(" cdm_contract.i_cwgwfl adviserratio, ");//������ʷ���
		commomSql.append(" nvl(cdm_contract.pk_htlv / 100, 0) + nvl(cdm_contract.i_cwgwfl, 0) comprehensiveratio,");//�ۺ�����=��ͬ����+������ʷ���
		commomSql.append(" cdm_contract.lxzffs paytype1name, ");//��Ϣ֧����ʽ
		commomSql.append(" cdm_contract.cwgwfzffs pk_paytype2, ");//�ƹ˷�֧����ʽ
		commomSql.append(" returnmode.name repaymenttypename, ");//���ʽ
		commomSql.append(" doc21.name mortgageepro, ");//��Ѻ��
		commomSql.append(" doc22.name mortgageerate, ");//��Ѻ��
		commomSql.append(" doc23.name mortgagee, ");//��ѺȨ��
		commomSql.append(" cdm_contract.memo remark, ");//��Ѻ������ע
		commomSql.append(" doc27.name pledgor1, ");//�����ˣ�һ��
		commomSql.append(" doc28.name equity1, ");//��Ĺ�Ȩ��һ��
		commomSql.append(" cdm_contract.def29 pledgeproportion1, ");//��Ѻ������һ��
		commomSql.append(" doc30.name pledgepeople1, ");//��ѺȨ�ˣ�һ��
		commomSql.append(" doc31.name pledgor2, ");//�����ˣ�����
		commomSql.append(" doc32.name equity2, ");//��Ĺ�Ȩ������
		commomSql.append(" cdm_contract.def33 pledgeproportion2, ");//��Ѻ����������
		commomSql.append(" doc34.name pledgepeople2, ");//��ѺȨ�ˣ�����
		commomSql.append(" doc35.name guarantor1, ");//�����ˣ�һ��
		commomSql.append(" doc36.name guarantor2, ");//�����ˣ�����
		commomSql.append(" doc37.name guarantor3, ");//�����ˣ�����
		commomSql.append(" doc39.name accountregulation, ");//�˻����
		commomSql.append(" doc40.name offsealcertificate, ");//����֤�չ���
		commomSql.append(" doc41.name moneyprecipitation, ");//�ʽ����
		commomSql.append(" cdm_contract.def42 capitalprerate, ");//�ʽ�������	
		commomSql.append(" cdm_contract.explain, ");//������������ explain
		commomSql.append(" cdm_contract.vdef13, ");
		commomSql.append(" cdm_contract.m_sjcwgwf payFinanceAmount, ");//�ۼ��Ѹ��ƹ˷ѽ��
		commomSql.append(" (cdm_contract.m_jhcwgf - cdm_contract.m_sjcwgwf) unpayFinanceAmount, ");//δ���ƹ˷ѽ��
		commomSql.append(" cdm_contract.contractamount fin_amount, ");//���ʽ��
		commomSql.append(" cdm_contract.remark contractremark, ");//�����ͬ��ע
		commomSql.append(" (case when substr(cdm_payrcpt.paydate, 1, 4) <= "+queryValueMap.get("busidate")+" then cdm_contract_exec.payamount else 0 end) payamount, ");//������(��ϸ)
//		commomSql.append(" (nvl(cdm_contract_exec.payamount,0) - nvl(l.repayamount,0)) fin_balance, ");
		commomSql.append(" cdm_contract_exec.payamount -NVL((select sum(NVL(cdm_repayrcpt_b.repayamount, 0))  from cdm_repayrcpt_b ");
		commomSql.append(" right join cdm_repayrcpt on cdm_repayrcpt_b.pk_repayrcpt=cdm_repayrcpt.pk_repayrcpt  ");
		commomSql.append(" and  cdm_repayrcpt.vbillstatus='1' where cdm_repayrcpt_b.pk_payrcpt =  ");
		commomSql.append(" cdm_payrcpt.pk_payrcpt   and cdm_repayrcpt_b.dr = 0 and substr(cdm_repayrcpt.repaydate,1,4) <= "+queryValueMap.get("busidate")+" ),0) fin_balance,  ");//�������(ȡ�ſ��˺�����)
		commomSql.append(" intrest.sumPayinterest  sumPayinterestAmount, ");//�ۼ��Ѹ���Ϣ���
		commomSql.append(" substr(cdm_contract.begindate, 0, 10) begindate, ");//��ͬ��ʼʱ��
		commomSql.append(" substr(cdm_contract.enddate, 0, 10) contractenddate, ");//��ͬ������
		commomSql.append(" substr(cdm_payplan.vdef2, 1, 10) enddate, ");//���ʵ�����
		commomSql.append(" substr(cdm_contract_exec.busidate, 0, 10) loandate, ");//�ſ�ʱ��
		commomSql.append(" l.loantatol_amount loantatol_amount ");//�ſ�����ϸ
		return commomSql.toString();
	}
	
	public String getLeftTable(Map<String, String> queryValueMap) {
		StringBuffer	tableSql =  new StringBuffer();
		tableSql.append(" from cdm_contract ");
		
		tableSql.append(" left join (select cdm_contract.pk_contract pk_contract,sum(nvl(cdm_contract_exec.payamount,0)) loantatol_amount,sum(nvl(cdm_contract_exec.repayamount, 0)) repayamount ");
		tableSql.append(" from cdm_contract ");
		tableSql.append(" left join cdm_contract_exec on cdm_contract.pk_contract = cdm_contract_exec.pk_contract ");
		tableSql.append(" where substr(cdm_contract_exec.busidate, 1, 4) <= "+queryValueMap.get("busidate")+" and cdm_contract.dr = 0 and cdm_contract_exec.dr = 0  group by cdm_contract.pk_contract ) l on cdm_contract.pk_contract = l.pk_contract ");//l
		
		tableSql.append(" left join (select sum(cdm_contract_exec.payinterest) sumPayinterest ,cdm_contract_exec.pk_contract pk_contract ");
		tableSql.append(" from cdm_contract_exec ");
		tableSql.append(" where cdm_contract_exec.dr = 0 ");
		tableSql.append(" and cdm_contract_exec.summary = 'FLX' ");
		tableSql.append(" and substr(cdm_contract_exec.busidate, 1, 4) <= "+queryValueMap.get("busidate")+" ");
		tableSql.append(" group by cdm_contract_exec.pk_contract ");
		tableSql.append(" ) intrest ");
		tableSql.append(" on cdm_contract.pk_contract = intrest.pk_contract ");//intrest�ۼ��Ѹ���Ϣ
		
		tableSql.append(" left join tgrz_projectdata on tgrz_projectdata.pk_projectdata = cdm_contract.pk_project and nvl(tgrz_projectdata.dr,0) = 0 ");
		tableSql.append(" left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract and cdm_contract_exec.summary = 'FBJ' and nvl(cdm_contract_exec.dr,0) = 0 ");
		tableSql.append(" left join tgrz_organization on tgrz_organization.pk_organization = cdm_contract.pk_rzjg and nvl(tgrz_organization.dr,0) = 0 ");
		tableSql.append(" left join tgrz_OrganizationType on tgrz_OrganizationType.pk_organizationtype = cdm_contract.pk_rzjglb and nvl(tgrz_OrganizationType.dr,0) = 0 ");
		tableSql.append(" left join tgrz_fintype on tgrz_fintype.pk_fintype = cdm_contract.pk_rzlx and nvl(tgrz_fintype.dr,0) = 0 ");
		tableSql.append(" left join tgrz_projectdata_c on tgrz_projectdata_c.pk_projectdata_c = cdm_contract.vdef3 and nvl(tgrz_projectdata_c.dr,0) = 0 ");
		tableSql.append(" left join org_financeorg on org_financeorg.pk_financeorg = cdm_contract.pk_debitorg and nvl(org_financeorg.dr,0) = 0 ");
		tableSql.append(" left join cdm_payrcpt on cdm_contract_exec.vbillno = cdm_payrcpt.vbillno and cdm_contract_exec.summary = 'FBJ' and nvl(cdm_payrcpt.dr,0) = 0  and cdm_payrcpt.vbillstatus='1' ");
		tableSql.append(" left join cdm_repayrcpt_b on cdm_repayrcpt_b.pk_payrcpt = cdm_payrcpt.pk_payrcpt and nvl(cdm_repayrcpt_b.dr,0) = 0 ");
		tableSql.append(" left join cdm_payplan on cdm_payplan.pk_contract = cdm_contract.pk_contract and cdm_payrcpt.pk_payplan = cdm_payplan.pk_payplan and nvl(cdm_payplan.dr,0) = 0 ");
		//���ʽ
		tableSql.append(" left join bd_defdoc returnmode  on returnmode.pk_defdoc=cdm_contract.vdef13  ");
		tableSql.append(" left join bd_defdoc doc21 on doc21.pk_defdoc = cdm_contract.def21 ");
		tableSql.append(" left join bd_defdoc doc22 on doc22.pk_defdoc = cdm_contract.def22 ");
		tableSql.append(" left join tgrz_organization doc23 on doc23.pk_organization = cdm_contract.def23 ");
		tableSql.append(" left join org_financeorg_v doc27 on doc27.pk_vid = cdm_contract.def27 ");
		tableSql.append(" left join org_financeorg_v doc28 on doc28.pk_vid = cdm_contract.def28 ");
		tableSql.append(" left join tgrz_organization doc30 on doc30.pk_organization = cdm_contract.def30 ");
		tableSql.append(" left join org_financeorg_v doc31 on doc31.pk_vid = cdm_contract.def31 ");
		tableSql.append(" left join org_financeorg_v doc32 on doc32.pk_vid = cdm_contract.def32 ");
		tableSql.append(" left join tgrz_organization doc34 on doc34.pk_organization = cdm_contract.def34 ");
		tableSql.append(" left join org_financeorg_v doc35 on doc35.pk_vid = cdm_contract.def35 ");
		tableSql.append(" left join org_financeorg_v doc36 on doc36.pk_vid = cdm_contract.def36 ");
		tableSql.append(" left join org_financeorg_v doc37 on doc37.pk_vid = cdm_contract.def37 ");
		tableSql.append(" left join bd_defdoc doc39 on doc39.pk_defdoc = cdm_contract.def39 ");
		tableSql.append(" left join bd_defdoc doc40 on doc40.pk_defdoc = cdm_contract.def40 ");
		tableSql.append(" left join bd_defdoc doc41 on doc41.pk_defdoc = cdm_contract.def41 ");
		return tableSql.toString();
	}
	
	public String getCommonField(){
		StringBuffer  commomSql = new  StringBuffer();
				//�����ͬ���
				commomSql.append("select distinct cdm_contract.contractcode  contractcode")
				//��ĿID
				.append(", tgrz_projectdata.pk_projectdata pk_project")
				//��Ŀ����
				.append(",tgrz_projectdata.code projectcode")
				//��Ŀ����
				.append(",tgrz_projectdata.name projectname")
				//��������
				.append(",tgrz_projectdata_c.pk_projectdata_c pk_periodization")
				//��������
				.append(",tgrz_projectdata_c.periodizationname periodizationname")
				//��Ŀ���ڹ�˾(��Ŀ��˾����)
				.append(",tgrz_projectdata.projectcorp projectcorp")
				
				//��λid
				.append(",org_financeorg.pk_financeorg pk_financeorg")
				//�����(��λ)
				.append(",org_financeorg.name borrower")
				//��������
				.append(",tgrz_fintype.pk_fintype")
				//�������ͱ���tgrz_organization
				.append(",tgrz_fintype.code fintypecode")
				//������������
				.append(",tgrz_fintype.name fintypename")
				//���ʻ���
				.append(",tgrz_organization.pk_organization pk_organization")
				//��������
				.append(",tgrz_organization.code organizationcode")
				//��������
				.append(",tgrz_organization.name organizationname")
				//���ʻ�������
				.append(",tgrz_OrganizationType.pk_organizationtype")
				//���ʻ������ͱ���
				.append(",tgrz_OrganizationType.code organizationtypecode")
				//���ʻ�����������
//				.append(",tgrz_OrganizationType.name pk_organizationtype")
				.append(",tgrz_OrganizationType.name organizationtypename")
				//��������
				.append(",tgrz_organization.branch branch")
				//����֧��
				.append(",tgrz_organization.subbranch subbranch")
				//�Ƿ�������
				.append(",decode(cdm_contract.b_jwrz,'Y','��','N','��') isabroad")
				
				//���
				//��ͬ����
				.append(",cdm_contract.pk_htlv/100 contratio")
				//������ʷ���
				.append(",cdm_contract.i_cwgwfl adviserratio")
				//�ۺ�����=��ͬ����+������ʷ���
				.append(",nvl(cdm_contract.pk_htlv/100,0)+nvl(cdm_contract.i_cwgwfl,0) comprehensiveratio")
				//��Ϣ֧����ʽ
				.append(",cdm_contract.lxzffs paytype1name")
				//������ʷ�֧����ʽ
				.append(",cdm_contract.cwgwfzffs pk_paytype2")
				// ���ʽ
				.append(",returnmode.name repaymenttypename ")
				//.append(",decode(cdm_contract.returnmode,'A','����һ�ν�Ϣ/��������','B','���ڼ�Ϣ/����һ�λ�����Ϣ','C','���ڸ�Ϣ/����һ�λ���','E','���ڻ�������Ϣ/����һ�ν���','F','���ڻ���/���ڸ�Ϣ') repaymenttypename")
				// ��Ѻ��.append(",cdm_contract.def21 mortgageepro  ")
				.append(",doc21.name mortgageepro ")
				//��Ѻ�� .append(",cdm_contract.def22 mortgageerate ")
				.append(",doc22.name mortgageerate ")
				//��ѺȨ��.append(",cdm_contract.def23 mortgagee ")
				.append(",doc23.name mortgagee ")
				//��Ѻ������ע 
				.append(",cdm_contract.memo  remark")
				//�����ˣ�һ��.append(",cdm_contract.def27 pledgor1 ")
				.append(",doc27.name  pledgor1 ")
				//��Ĺ�Ȩ��һ��
				.append(",doc28.name  equity1 ")
				//��Ѻ������һ��
				.append(",cdm_contract.def29 pledgeproportion1  ")
				//��ѺȨ�ˣ�һ��
				.append(",doc30.name  pledgepeople1 ")
				//�����ˣ�����
				.append(",doc31.name  pledgor2 ")
				//��Ĺ�Ȩ������
				.append(",doc32.name  equity2 ")
				//��Ѻ����������
				.append(",cdm_contract.def33  pledgeproportion2 ")
				//��ѺȨ�ˣ�����
				.append(",doc34.name  pledgepeople2 ")
				//�����ˣ�һ��
				.append(",doc35.name  guarantor1 ")
				//�����ˣ�����
				.append(",doc36.name  guarantor2 ")
				//�����ˣ�����
				.append(",doc37.name   guarantor3 ")
				//�˻����
				.append(",doc39.name  accountregulation ")
				//����֤�չ���
				.append(",doc40.name   offsealcertificate  ")
				//�ʽ����
				.append(",doc41.name  moneyprecipitation ")
				//�ʽ�������	
				.append(",cdm_contract.def42   capitalprerate ")
				//������������ explain
				.append(",cdm_contract.explain  ")
				 
				//�ۼ��Ѹ��ƹ˷ѽ��
				.append(",cdm_contract.m_sjcwgwf payFinanceAmount ")
				//δ���ƹ˷ѽ��
				.append(",(cdm_contract.m_jhcwgf-cdm_contract.m_sjcwgwf) unpayFinanceAmount ")
				
				//�ۼ��Ѹ���Ϣ 
				.append(",intrest.sumPayinterest  sumPayinterest ")
//				.append(",(select sum(cdm_contract_exec.payinterest) from cdm_contract_exec ")
//				.append("where cdm_contract.pk_contract =cdm_contract_exec.pk_contract and cdm_contract_exec.summary = 'FLX' ) sumPayinterest ")
				//�ۼ��Ѹ��ƹ˷ѽ��
				//.append(",cdm_contract.vdef7 payFinanceAmount")
				//������ϸ�ı�ע 	
				.append(",cdm_contract.remark contractremark ")
				//���ʽ��
				.append(",cdm_contract.contractamount fin_amount")
				//�ſ�ƻ���ÿ�ʷſ�������ʵ�������ͬҪ���ۼ�
				.append(",l.loantatol_amount loantatol_amount")
				//�������=�ۼƷſ�-�ѻ�����ŵ����ʵ�������ͬҪ���ۼ�
				.append(", cdm_contract_exec.payamount -NVL((select sum(NVL(cdm_repayrcpt_b.repayamount, 0))  from cdm_repayrcpt_b ")
				.append(" right join cdm_repayrcpt on cdm_repayrcpt_b.pk_repayrcpt=cdm_repayrcpt.pk_repayrcpt ")
				.append(" and  cdm_repayrcpt.vbillstatus='1' where cdm_repayrcpt_b.pk_payrcpt = ")
				.append(" cdm_payrcpt.pk_payrcpt   and cdm_repayrcpt_b.dr = 0 ),0) fin_balance ")
				//���ʵ�����
				.append(",substr(cdm_payplan.vdef2,0,10) enddate ")
				//��������
				.append(",substr(cdm_contract.enddate, 0, 10) contractenddate ")
				//�ſ�ʱ��
				.append(",substr(cdm_contract_exec.busidate, 0, 10) loandate ")
				//�ſ���
				.append(",cdm_contract_exec.payamount payamount")
				//������ʼ��
				//.append(",substr(cdm_payplan.paydate,0,10) begindate")
				.append(",substr(cdm_contract.begindate, 0, 10) begindate  ");
				return commomSql.toString();
		
	}
	/*
	 * ��ȡ���ӵ���صı����
	 */
	public  String getLeftTable(){
	StringBuffer	tableSql =  new StringBuffer();
	tableSql.append("from cdm_contract ")
		.append(" left join (select sum(cdm_contract_exec.payinterest) sumPayinterest, ")
		.append(" cdm_contract_exec.pk_contract pk_contract ")
		.append(" from cdm_contract_exec ")
		.append(" where cdm_contract_exec.dr = 0 ")
		.append(" and cdm_contract_exec.summary = 'FLX' ")
		.append(" group by cdm_contract_exec.pk_contract) intrest ")
		.append(" on cdm_contract.pk_contract = intrest.pk_contract ")
	.append(" left join (select cdm_contract.pk_contract pk_contract, ")
	.append(" sum(nvl(cdm_contract_exec.payamount, 0)) loantatol_amount ")
	.append(" from cdm_contract ")
	.append(" left join cdm_contract_exec ")
	.append(" on cdm_contract.pk_contract = ")
	.append(" cdm_contract_exec.pk_contract ")
	.append(" where cdm_contract.dr = 0 ")
	.append(" and cdm_contract_exec.dr = 0 ")
	.append(" group by cdm_contract.pk_contract) l ")
	.append(" on cdm_contract.pk_contract = l.pk_contract ")
		.append(" left  join tgrz_projectdata on tgrz_projectdata.pk_projectdata = cdm_contract.pk_project")
		.append(" left  join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract and cdm_contract_exec.summary='FBJ'")
		.append(" left  join tgrz_organization on tgrz_organization.pk_organization = cdm_contract.pk_rzjg")
		//.append(" left  join cdm_contract_exec  execlx   on  execlx.pk_contract = cdm_contract.pk_contract and execlx.summary='FLX' ")
		.append(" left  join tgrz_OrganizationType on tgrz_OrganizationType.pk_organizationtype = cdm_contract.pk_rzjglb")
		.append(" left  join tgrz_fintype on tgrz_fintype.pk_fintype = cdm_contract.pk_rzlx")
		.append(" left  join tgrz_projectdata_c on tgrz_projectdata_c.pk_projectdata_c = cdm_contract.vdef3")//
		.append(" left  join org_financeorg on org_financeorg.pk_financeorg = cdm_contract.pk_debitorg")
		.append(" left join cdm_payrcpt on cdm_contract_exec.vbillno=cdm_payrcpt.vbillno and cdm_contract_exec.summary='FBJ' and cdm_payrcpt.vbillstatus='1'  and cdm_payrcpt.dr= 0")
		
		//�������� �� cdm_contract_exec�ķſ��Ź���������ӱ�cdm_repayrcpt_b��
		//.append(" left  join cdm_repayrcpt_b on cdm_repayrcpt_b.pk_payrcpt=cdm_payrcpt.pk_payrcpt and cdm_repayrcpt_b.dr = 0 ")
		.append("  left join cdm_payplan on cdm_payplan.pk_contract = cdm_contract.pk_contract  and cdm_payrcpt.pk_payplan=cdm_payplan.pk_payplan ")
//		.append(" "+getByFinDeadlineSort()+" ")
		/*//���ݵ����պϲ�����������ۼƷſ��� start  
		.append(" left join (select contexec.pk_contract,substr(cdm_payplan.vdef2, 1, 10) enddate,sum(distinct contexec.payamount)mny from cdm_contract_exec contexec  ")
		.append(" inner join cdm_payplan on cdm_payplan.pk_contract = contexec.pk_contract where  contexec.summary = 'FBJ' ")   
		.append("group by contexec.pk_contract,substr(cdm_payplan.vdef2, 1, 10)) v on   cdm_payplan.pk_contract = v.pk_contract and  v.pk_contract =cdm_contract.pk_contract and substr(cdm_payplan.vdef2, 1, 10) = v.enddate    ")
		//����ƴ�ӷſ�ʱ���ֶ�
		.append("left join (select contexec.pk_contract,substr(cdm_payplan.vdef2, 1, 10) enddate, ")
		.append("contexec.busidate busidate,sum(distinct contexec.payamount) mny from cdm_contract_exec contexec ") 
		.append(" inner join cdm_payplan on cdm_payplan.pk_contract = contexec.pk_contract  where  contexec.summary = 'FBJ' ") 
		.append("group by contexec.pk_contract,contexec.busidate,substr(cdm_payplan.vdef2, 1, 10)) g on cdm_payplan.pk_contract = ") 
		.append(" g.pk_contract  and substr(cdm_payplan.vdef2,1,10) =g.enddate ") 
		 //���ݵ����գ�������������ͬ�ĵ�����ֻ��һ��ʱ����ʼ��ȥ��ͷ����
		.append("left   join   (select count(distinct substr(cdm_payplan.vdef2, 1, 10)) num,cdm_payplan.pk_contract from  cdm_payplan ")
		.append(" left join cdm_contract on  cdm_contract.pk_contract=cdm_payplan.pk_contract group by cdm_payplan.pk_contract ")
		.append("having count(distinct substr(cdm_payplan.vdef2, 1, 10))>=1 )p  on   p.pk_contract = cdm_contract.pk_contract ")
		//���ݵ����պϲ�������� 
		.append("left  join (select distinct substr(cdm_payplan.vdef2, 1, 10) enddate, sum(NVL(cdm_repayrcpt_b.repayamount, 0)) fin,cdm_repayrcpt.pk_contract ")
		.append("from cdm_repayrcpt_b right join cdm_repayrcpt on cdm_repayrcpt_b.pk_repayrcpt = cdm_repayrcpt.pk_repayrcpt ")
		.append("and cdm_repayrcpt.vbillstatus = '1' inner join  cdm_payplan on cdm_payplan.pk_contract = cdm_repayrcpt.pk_contract ")
		.append("right join cdm_payrcpt on cdm_repayrcpt_b.pk_payrcpt =  cdm_payrcpt.pk_payrcpt ")
		.append("and cdm_repayrcpt_b.dr = 0 group by cdm_repayrcpt.pk_contract,substr(cdm_payplan.vdef2, 1, 10) ")
		.append(")  f on cdm_payplan.pk_contract = f.pk_contract  and substr(cdm_payplan.vdef2, 1, 10) = f.enddate  ")
		//end
*/		.append(" left join bd_defdoc doc21 on doc21.pk_defdoc = cdm_contract.def21 ")
		.append(" left join bd_defdoc doc22 on doc22.pk_defdoc = cdm_contract.def22 ")
		.append(" left join tgrz_organization doc23 on doc23.pk_organization = cdm_contract.def23 ")
		//���ʽ
	   .append(" left join bd_defdoc returnmode  on returnmode.pk_defdoc=cdm_contract.vdef13  ")
		.append(" left join org_financeorg_v doc27 on doc27.pk_vid = cdm_contract.def27 ")
		.append(" left join org_financeorg_v doc28 on doc28.pk_vid = cdm_contract.def28 ")
		//.append(" left join bd_defdoc doc29 on doc29.pk_defdoc = cdm_contract.def29 ")
		.append(" left join tgrz_organization doc30 on doc30.pk_organization = cdm_contract.def30 ")
		.append(" left join org_financeorg_v doc31 on doc31.pk_vid = cdm_contract.def31 ")
		.append(" left join org_financeorg_v doc32 on doc32.pk_vid = cdm_contract.def32 ")
		//.append(" left join bd_defdoc doc33 on doc33.pk_defdoc = cdm_contract.def33 ")
		.append(" left join tgrz_organization doc34 on doc34.pk_organization = cdm_contract.def34 ")
		.append(" left join org_financeorg_v doc35 on doc35.pk_vid = cdm_contract.def35 ")
		.append(" left join org_financeorg_v doc36 on doc36.pk_vid = cdm_contract.def36 ")
		.append(" left join org_financeorg_v doc37 on doc37.pk_vid = cdm_contract.def37 ")
		
		.append(" left join bd_defdoc doc39 on doc39.pk_defdoc = cdm_contract.def39 ")
		.append(" left join bd_defdoc doc40 on doc40.pk_defdoc = cdm_contract.def40 ")
		.append(" left join bd_defdoc doc41 on doc41.pk_defdoc = cdm_contract.def41 ");
		//.append(" left join bd_defdoc doc42 on doc42.pk_defdoc = cdm_contract.def42 ");
	return tableSql.toString();
		
	}
	/*
	 * �ֻ�ȡ����sql
	 */
	public String getGroupBy(){
		StringBuffer groupSql =new StringBuffer();
		groupSql.append(" group by tgrz_projectdata.pk_projectdata,tgrz_projectdata.code ,tgrz_projectdata.name ,tgrz_projectdata_c.pk_projectdata_c , ")
		.append("tgrz_projectdata_c.periodizationname ,tgrz_projectdata.projectcorp ,cdm_contract.contractcode ,org_financeorg.pk_financeorg , ")
		.append("org_financeorg.name ,tgrz_fintype.pk_fintype,tgrz_fintype.code ,tgrz_fintype.name ,tgrz_organization.pk_organization , ")
		.append("tgrz_organization.code ,tgrz_organization.name ,tgrz_OrganizationType.pk_organizationtype,tgrz_OrganizationType.code , ")
		.append("tgrz_OrganizationType.name ,tgrz_organization.branch ,tgrz_organization.subbranch ,cdm_contract.b_jwrz ,cdm_contract.contractamount , ")
		.append("cdm_contract_exec.payamount ,cdm_contract_exec.leftrepayamount ,cdm_payplan.paydate ,cdm_payplan.vdef2 , ")
		//.append("v .mny, f.fin,cdm_contract.begindate,")//cdm_contract_exec.busidate,
		.append("cdm_contract.dzyw ,cdm_contract.dbtj ,cdm_contract.pk_htlv ,cdm_contract.i_cwgwfl,cdm_contract.m_sjcwgwf, ")
		.append("cdm_contract.m_jhcwgf,cdm_contract.lxzffs,cdm_contract_exec.payinterest,cdm_contract.cwgwfzffs,cdm_contract.returnmode, ")
	    .append("doc21.name,doc22.name,doc23.name,cdm_contract.memo,cdm_contract.remark, ")
	    .append("doc27.name,doc28.name,cdm_contract.def29,doc30.name, ")
	    .append("doc31.name,doc32.name,cdm_contract.def33, doc34.name, ")
	    .append("doc35.name,doc36.name,doc37.name,doc39.name, doc40.name, cdm_contract.pk_contract,cdm_payrcpt.pk_payrcpt, ")
	    .append("doc41.name, cdm_contract.def42,cdm_contract.explain,returnmode.name, ")
	    .append("out.enddate,out.loantatol_amount,out.fin_balace,out.loandate,out.begindate,out.contractenddate order   by contractcode ");
		return groupSql.toString();
	
	}
	
	/*
	 * �˷�����sql���ڰ����ʵ����ս��ۼƷſ���������ſ�ʱ�䡢������ʼʱ����з��飻
	 * ������sql�ɶ���ִ�в�ѯ
	 */
	public  String getByFinDeadlineSort(){
	StringBuffer	finSortSql=  new StringBuffer();
	
	finSortSql.append(" left join ( select d.pk_contract pk_contract,d.thisdate enddate,d.loadmny loantatol_amount,d.fin_balace fin_balace, ")
		.append("d.loandate loandate,(case when p.num> 1 then substr(cdm_payplan.paydate, 0, 10) else  substr(cdm_contract.begindate, 0, 10)  end) begindate ,cdm_contract.enddate contractenddate  from ( ")
		.append("select pk_contract,thisdate,sum (mny)loadmny,(sum (mny)-sum(NVL(fin,0)))fin_balace ,loandate from ( select distinct  g.vbillno,k.pk_contract,f.fin, ")
		.append("k.thisdate,g.mny,replace(LISTAGG(substr(g.busidate, 1, 4) || '��' || substr(g.busidate, 6, 2) || '��' || substr(g.busidate, 9, 2) || '��' || '�ſ�' || ")
		.append("trim(to_char(nvl(g.mny, 0) / 10000,'fm99999990.009999')) || '��Ԫ��') within  group(order by g.busidate) ")
		.append("over(partition by k.pk_contract, k.thisdate), '�����շſ�0.00��Ԫ','') loandate ")
		.append(" from (select LAG(v.enddate, 1, '1900-01-01') over(partition by v.pk_contract order by v.enddate) LASTDATE,v.enddate thisdate,v.pk_contract ")
		.append("from (select pk_contract,substr(vdef2, 1, 10) enddate��from cdm_payplan  where dr = 0 group by pk_contract, substr(vdef2, 1, 10)) v ) k ")
		.append("left join (select contexec.pk_contract,contexec.vbillno,  substr(cdm_payplan.vdef2, 1, 10) enddate, contexec.busidate  busidate, sum(contexec.payamount) mny  ")
		.append("from cdm_contract_exec contexec inner join cdm_payplan on cdm_payplan.pk_contract = contexec.pk_contract and cdm_payplan.dr=0 inner join cdm_payrcpt on cdm_payrcpt.pk_payplan =  cdm_payplan.pk_payplan and cdm_payrcpt.dr=0")
		.append("and cdm_payrcpt.pk_contract =   contexec.pk_contract and contexec.vbillno = cdm_payrcpt.vbillno where contexec.summary = 'FBJ' and contexec.dr=0  group by contexec.pk_contract, cdm_payplan.pk_payplan,contexec.vbillno,  ")
		.append(" contexec.busidate, substr(cdm_payplan.vdef2, 1, 10)) g on k.pk_contract = g.pk_contract  and k.LASTDATE < g.enddate and g.enddate <= k.thisdate ")
		.append("left  join (select cdm_payrcpt.vbillno, sum(nvl(cdm_repayrcpt_b.repayamount,0))fin,cdm_repayrcpt.pk_contract,substr(cdm_payplan.vdef2, 1, 10) enddate from cdm_repayrcpt  ")
		.append("left  join cdm_repayrcpt_b on cdm_repayrcpt_b.pk_repayrcpt= cdm_repayrcpt.pk_repayrcpt and cdm_repayrcpt_b.dr=0 ")
		.append("inner   join cdm_payplan on cdm_payplan.pk_contract=cdm_repayrcpt.pk_contract and cdm_payplan.dr=0 ")
		.append("inner join cdm_payrcpt on cdm_payrcpt.pk_payrcpt=cdm_repayrcpt_b.pk_payrcpt  and cdm_payrcpt.dr=0 and  cdm_payrcpt.pk_payplan = cdm_payplan.pk_payplan ")
		.append(" where cdm_repayrcpt.dr=0 and cdm_repayrcpt.vbillstatus=1    group by   cdm_payrcpt.vbillno, cdm_repayrcpt.pk_contract,substr(cdm_payplan.vdef2, 1, 10)   ")
		.append(")f on f.vbillno=g.vbillno where g.pk_contract is not null ) group   by   pk_contract,thisdate,loandate ) d left join cdm_payplan on  d.pk_contract=cdm_payplan.pk_contract ")
		.append("and   substr(cdm_payplan.vdef2,1,10)=d.thisdate and  cdm_payplan.dr=0 left  join cdm_contract  on cdm_payplan.pk_contract= cdm_contract.pk_contract ")
		.append("left   join   (select count(distinct substr(cdm_payplan.vdef2, 1, 10)) num, cdm_payplan.pk_contract from  cdm_payplan where  cdm_payplan.dr=0 group by cdm_payplan.pk_contract ")
		.append("having count(distinct substr(cdm_payplan.vdef2, 1, 10))>=1 )p on p.pk_contract = d.pk_contract ")
		.append("group   by   d.pk_contract,d.thisdate,d.loandate,d.loadmny,d.fin_balace,cdm_payplan.paydate,cdm_contract.begindate,p.num,cdm_contract.enddate ")
		.append("order by d.thisdate,cdm_payplan.paydate,begindate )out    on out.pk_contract=cdm_contract.pk_contract ");
		return finSortSql.toString();
	}
}
