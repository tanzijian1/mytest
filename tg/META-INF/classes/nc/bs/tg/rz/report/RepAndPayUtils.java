package nc.bs.tg.rz.report;

import java.util.Map;

import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

public class RepAndPayUtils  extends ReportUtils  {

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		// TODO �Զ����ɵķ������
		return null;
	}
   public String getRepAndPayField(){
	   StringBuffer fieldSql = new StringBuffer();
	   fieldSql.append("select distinct contractcode code,")//��ͬ���롢
	 //��Ŀ��������Ŀ���롢��Ŀ����
	.append("tgrz_projectdata.pk_projectdata pk_project,tgrz_projectdata.code projectcode,")
	.append("tgrz_projectdata.name projectname, ")  
	 //������������������
	.append("tgrz_projectdata_c.pk_projectdata_c pk_periodization, ")
	.append("tgrz_projectdata_c.periodizationname periodizationname, ")   
	 //��Ŀ��˾
	.append("tgrz_projectdata.projectcorp  projectcorp, ")  
	 //�����
	.append("(select org_financeorg.name from org_financeorg  ")  
	.append("where org_financeorg.pk_financeorg = cdm_contract.pk_debitorg)  borrower, ")  
	 //�����������������롢����
	.append("tgrz_fintype.pk_fintype pk_fintype,tgrz_fintype.code fintypecode,tgrz_fintype.name fintypename, ")  
	 //���ʻ������������롢����
	.append("tgrz_organization.pk_organization pk_organization,tgrz_organization.code organizationcode, ")  
	.append("tgrz_organization.name organizationname, ")  
	 //���ʽ��
	.append(" contractamount fin_amount,")
/*	//�ۼƷſ���
	.append("(select  sum(execfk.payamount) from  cdm_contract_exec execfk where execfk.pk_contract = cdm_contract.pk_contract")
	.append(" and execfk.summary = 'FBJ'  and substr(execfk.busidate, 0, 4) = 2019   )  loantatol_amount, ")
	//�ۼƻ�����
	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
	.append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) = 2019   )  contnote, ") //contnote
*/	//������ʼ�ա����ʵ�����
	.append("substr(begindate,0,10) begindate,substr(enddate,0,10)enddate, ")  
	 // ���ʽ
	.append("returnmode.name repaymenttypename,")
	//��ͬ���ʡ���Ϣ֧����ʽ����Ϣ֧����ʽ����
	.append("pk_htlv/100 contratio,lxzffs paytype1name,  ")  
	 //������ʷ��ʡ� ������ʷ���֧����ʽ
	.append("i_cwgwfl adviserratio,cwgwfzffs paytype2name, ");  
	
	   
	   return fieldSql.toString();
	   
   }
   /*
    * ��ȡ�����Ҫ����ƻ�����ֶ�
    */
    public String getYearPlanAmount(){
    	StringBuffer planSql= new StringBuffer();
    	//// N��/n+xʵ�ʻ����
    	planSql.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
    	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
    	.append("and substr(execbj.busidate, 0, 4) = v1.datatype and execbj.dr='0')repmonactualtotal_amount, ")
    	//N��/n+x��Ϣ���
    	.append("(select  sum(nvl(execlx.payinterest, 0)) from  cdm_contract_exec execlx ")
    	.append("where execlx.pk_contract = cdm_contract.pk_contract and execlx.summary = 'FLX' ")
    	.append("and substr(execlx.busidate, 0, 4) = v1.datatype and execlx.dr='0' )ipmonactualtotal_amount, ")
//    	//�ۼ��ѻ�����
//    	.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
//    	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
//    	.append("and substr(execbj.busidate, 0, 4) <= v1.datatype and execbj.dr='0') repaytotal_amount, ")
    	//�ƻ�������ʷ��ý��
    	.append(" (select sum(nvl(cdm_cwgwfzfjh.m_zfje, 0)) from cdm_cwgwfzfjh ")
    	.append("where cdm_cwgwfzfjh.pk_contract = cdm_contract.pk_contract ")
    	.append("and substr(cdm_cwgwfzfjh.d_zfsj, 0, 4) = v1.datatype and cdm_cwgwfzfjh.dr='0' ) advmonplantotal_amount, ")
    	//ʵ�ʲ�����ʷ��ý��
    	.append("(select sum(nvl(cdm_cwgwfzxqk.m_zfje, 0)) from  cdm_cwgwfzxqk ")
    	.append("where cdm_cwgwfzxqk.pk_contract = cdm_contract.pk_contract ")
    	.append("and substr(cdm_cwgwfzxqk.d_zfsj, 0, 4) = v1.datatype and cdm_cwgwfzxqk.dr='0'  )advmonactualtotal_amount, ")
//    	  // N��/n+xʵ�ʻ����N��/n+x��Ϣ���
//    	planSql.append("sum(nvl(execbj.repayamount,0))  repmonactualtotal_amount,sum(nvl(execlx.payinterest,0)) ipmonactualtotal_amount, ")
//    	  //�ƻ�������ʷ��ý�ʵ�ʲ�����ʷ��ý��
//    	.append("sum(nvl(cdm_cwgwfzfjh.m_zfje,0)) advmonplantotal_amount,sum(nvl(cdm_cwgwfzxqk.m_zfje,0)) advmonactualtotal_amount, ")
    	//���ڽ�������������
    	.append("v1.datatype datatype from cdm_contract ");
    	
		return planSql.toString();
    	
    }
    
    /*
     * ��ȡ�����Ҫ����ƻ�����ֶ� add by tjl 2020-06-03
     */
     public String getYearPlanAmount(Map<String, String> queryValueMap){
     	StringBuffer planSql= new StringBuffer();
     	//// N��/n+xʵ�ʻ����
     	planSql.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
     	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
     	.append("and substr(execbj.busidate, 0, 4) = v1.datatype and execbj.dr='0')repmonactualtotal_amount, ")
     	//N��/n+x��Ϣ���
     	.append("(select  sum(nvl(execlx.payinterest, 0)) from  cdm_contract_exec execlx ")
     	.append("where execlx.pk_contract = cdm_contract.pk_contract and execlx.summary = 'FLX' ")
     	.append("and substr(execlx.busidate, 0, 4) = v1.datatype and execlx.dr='0' )ipmonactualtotal_amount, ")
//     	//�ۼ��ѻ�����
//     	.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
//     	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
//     	.append("and substr(execbj.busidate, 0, 4) <= v1.datatype and execbj.dr='0') repaytotal_amount, ")
//     	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
//     	.append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  yeartatol_amount, ") //contnote
     	//�ƻ�������ʷ��ý��
     	.append(" (select sum(nvl(cdm_cwgwfzfjh.m_zfje, 0)) from cdm_cwgwfzfjh ")
     	.append("where cdm_cwgwfzfjh.pk_contract = cdm_contract.pk_contract ")
     	.append("and substr(cdm_cwgwfzfjh.d_zfsj, 0, 4) = v1.datatype and cdm_cwgwfzfjh.dr='0' ) advmonplantotal_amount, ")
     	//ʵ�ʲ�����ʷ��ý��
     	.append("(select sum(nvl(cdm_cwgwfzxqk.m_zfje, 0)) from  cdm_cwgwfzxqk ")
     	.append("where cdm_cwgwfzxqk.pk_contract = cdm_contract.pk_contract ")
     	.append("and substr(cdm_cwgwfzxqk.d_zfsj, 0, 4) = v1.datatype and cdm_cwgwfzxqk.dr='0'  )advmonactualtotal_amount, ")
//     	  // N��/n+xʵ�ʻ����N��/n+x��Ϣ���
//     	planSql.append("sum(nvl(execbj.repayamount,0))  repmonactualtotal_amount,sum(nvl(execlx.payinterest,0)) ipmonactualtotal_amount, ")
//     	  //�ƻ�������ʷ��ý�ʵ�ʲ�����ʷ��ý��
//     	.append("sum(nvl(cdm_cwgwfzfjh.m_zfje,0)) advmonplantotal_amount,sum(nvl(cdm_cwgwfzxqk.m_zfje,0)) advmonactualtotal_amount, ")
     	//���ڽ�������������
     	.append("v1.datatype datatype from cdm_contract ");
     	
 		return planSql.toString();
     	
     }
    
    /*
     * ��ȡ�¶���Ҫ����ƻ�����ֶ�
     */
     public String getMonthPlanAmount(){
     	StringBuffer planSql= new StringBuffer();
     	//// N��/n+xʵ�ʻ����
     	planSql.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
     	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
     	.append("and substr(execbj.busidate, 0, 7) = v1.datatype and execbj.dr='0')repmonactualtotal_amount, ")
     	//N��/n+x��Ϣ���
     	.append("(select  sum(nvl(execlx.payinterest, 0)) from  cdm_contract_exec execlx ")
     	.append("where execlx.pk_contract = cdm_contract.pk_contract and execlx.summary = 'FLX' ")
     	.append("and substr(execlx.busidate, 0, 7) = v1.datatype and execlx.dr='0')ipmonactualtotal_amount, ")
     	//�ƻ�������ʷ��ý��
     	.append(" (select sum(nvl(cdm_cwgwfzfjh.m_zfje, 0)) from cdm_cwgwfzfjh ")
     	.append("where cdm_cwgwfzfjh.pk_contract = cdm_contract.pk_contract ")
     	.append("and substr(cdm_cwgwfzfjh.d_zfsj, 0, 7) = v1.datatype and cdm_cwgwfzfjh.dr='0' ) advmonplantotal_amount, ")
     	//ʵ�ʲ�����ʷ��ý��
     	.append("(select sum(nvl(cdm_cwgwfzxqk.m_zfje, 0)) from  cdm_cwgwfzxqk ")
     	.append("where cdm_cwgwfzxqk.pk_contract = cdm_contract.pk_contract ")
     	.append("and substr(cdm_cwgwfzxqk.d_zfsj, 0, 7) = v1.datatype and cdm_cwgwfzxqk.dr='0' )advmonactualtotal_amount, ")
//     	  // N��/n+xʵ�ʻ����N��/n+x��Ϣ���
//     	planSql.append("sum(nvl(execbj.repayamount,0))  repmonactualtotal_amount,sum(nvl(execlx.payinterest,0)) ipmonactualtotal_amount, ")
//     	  //�ƻ�������ʷ��ý�ʵ�ʲ�����ʷ��ý��
//     	.append("sum(nvl(cdm_cwgwfzfjh.m_zfje,0)) advmonplantotal_amount,sum(nvl(cdm_cwgwfzxqk.m_zfje,0)) advmonactualtotal_amount, ")
     	//���ڽ�������������
     	.append("v1.datatype datatype from cdm_contract ");
     	
 		return planSql.toString();
     	
     }
   /*
    * ���ݻ����ƻ����ֽ������Ǻ�ͬԼ������̬��ȡ�ֶ�
    */
	public String getMothRepayField(Map<String, String> queryValueMap){
	String 	field="";
		if("��ͬԼ��".equals(queryValueMap.get("plan"))){
			field = " cdm_repayplan.preamount repmonplantotal_amount, "; //sum(nvl(
		}else if("�ֽ���".equals(queryValueMap.get("plan"))){
			field = "sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal_amount,";
		}
		return field;
	}
	/*
	 * ������������ر�
	 */
	public String getLinkTable(){
	StringBuffer	tableSql= new StringBuffer();
	    //��������Ŀ���ϱ�
      tableSql.append("left join tgrz_projectdata on cdm_contract.pk_project =tgrz_projectdata.pk_projectdata ")
	  //��Ŀ���ڱ�	
	.append("left join tgrz_projectdata_c on tgrz_projectdata_c.pk_projectdata_c=cdm_contract.vdef3  ")
	//��Ŀ��˾���Ʋ��գ�������֯��
	.append("left join org_financeorg on org_financeorg.pk_financeorg =cdm_contract.pk_xmgs  ")
	 //�������ͱ�
	.append("left join  tgrz_fintype on   tgrz_fintype.pk_fintype=cdm_contract.pk_rzlx  ")
	//���ʽ
	.append(" left join bd_defdoc returnmode  on returnmode.pk_defdoc=cdm_contract.vdef13  ")
	//���ʻ�����
	.append("left join tgrz_organization on tgrz_organization.pk_organization=cdm_contract.pk_rzjg ");
		
	return tableSql.toString();
		
	}

}
