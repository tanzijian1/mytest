package nc.bs.tg.fund.rz.report.sale;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.tg.fund.rz.report.ReportConts;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.sale.SaleContractInvPayVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.tgfp.report.ReportOrgVO;
import nc.vo.uap.pf.PFBusinessException;

import com.ufida.dataset.IContext;
/**
 * ��Ʊ��̨ͬ��
 */
public class SaleInvPayUtils extends ReportUtils{
	static SaleInvPayUtils utils;
	public static SaleInvPayUtils getSaleInvPayUtils() throws PFBusinessException{
		if(utils==null){
			utils=new SaleInvPayUtils();
		}
		return utils;
	}
	public SaleInvPayUtils() throws PFBusinessException{
		 setKeys(BeanHelper.getInstance().getPropertiesAry(new SaleContractInvPayVO()));
		 StringBuffer columnSQL = new StringBuffer();
			columnSQL.append("pk_fct_ap char(20), ");
			columnSQL.append("def61 varchar(150), ");
			columnSQL.append("def62 varchar(150), ");
			columnSQL.append("local_money varchar(150), ");
			columnSQL.append("slocal_money varchar(150), ");
			columnSQL.append(" def98  varchar(150) ");
//			columnSQL.append("localcreditamount varchar(150), ");
//			columnSQL.append("localdebitamount varchar(150) ");
			SQLUtil.createTempTable("tgyx_paycontract",
					columnSQL.toString(), null);
	}
	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		// TODO �Զ����ɵķ������
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			List<SaleContractInvPayVO> list = null;
			list =getVoList(conditionVOs); 
			
			if (list.size() == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			
			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}
	public List<SaleContractInvPayVO> getVoList(ConditionVO[] conditionVOs) throws BusinessException{
		insertTgyx_contract(conditionVOs);
		List<Map<String,String>> maplist=(List<Map<String, String>>)getBaseDAO().executeQuery(getSql(), new MapListProcessor());
		List<SaleContractInvPayVO> list=new ArrayList<SaleContractInvPayVO>();
		List<Map<String,String>> gllistmap=getgl_detailMoney();
		List<Map<String, String>> budgetSubjectlist=	getBudgetSubjectmap();
		for(Map<String,String> map:maplist){
			UFDouble localcreditamount=UFDouble.ZERO_DBL;
			UFDouble localdebitamount=UFDouble.ZERO_DBL;
			if(gllistmap!=null){
				for(Map<String,String> glmapi:gllistmap){
				if(map.get("pk_fct_ap")!=null||map.get("pk_fct_ap").length()>0){
				if(map.get("pk_fct_ap").equals(glmapi.get("pk_fct_ap"))){
					 if("1".equals(glmapi.get("flag"))){
						 localcreditamount=localcreditamount.add(tranUFDouble(glmapi.get("localmount")));
					 }else if("2".equals(glmapi.get("flag"))){
						 localdebitamount=localdebitamount.add(tranUFDouble(glmapi.get("localmount")));
					 }
				  }
				 }
				}
			}
			SaleContractInvPayVO payvo=new SaleContractInvPayVO();        
			payvo.setCitycompany(map.get("citycompany"));	//���й�˾
			payvo.setProjectname(map.get("projectname"));	//��Ŀ����
			payvo.setContractnum(map.get("contractnum"));	//��ͬ���
			payvo.setContractname(map.get("contractname"));	//��ͬ����
			payvo.setBunit(map.get("bunit"));	//�ҷ���λ
			payvo.setAczCompany(map.get("aczcompany"));	//���˹�˾
			payvo.setContractmoneyamount(tranUFDouble(map.get("contractmoneyamount")));	//��ͬ��̬���
			payvo.setSigndate(getYearMonth(map.get("signdate")));	//ǩ������
			payvo.setHandleman(map.get("handleman"));	//������
			payvo.setBudgetSubject(getBudgetSubject(budgetSubjectlist,map.get("pk_fct_ap")));	//Ԥ���Ŀ
			payvo.setRequestmoneyamount(tranUFDouble(map.get("local_money")).add(tranUFDouble(map.get("def98"))));	//�ۼ������
			payvo.setPaymoneyamount(tranUFDouble(map.get("slocal_money")).add(tranUFDouble(map.get("def98"))));	//�ۼ��Ѹ���
			payvo.setInnvocemoneyamount(tranUFDouble(map.get("def61")).add(localcreditamount));	//�ۼ����շ�Ʊ(D)
			payvo.setNotaxmoneyamount(tranUFDouble(map.get("def62")).add(localdebitamount));	//����˰���(E)
			payvo.setTaxmoneyamount(payvo.getInnvocemoneyamount().sub(payvo.getNotaxmoneyamount()));	//˰��(F)
			if(!(payvo.getNotaxmoneyamount().compareTo(UFDouble.ZERO_DBL)==0))
			payvo.setTax(tranDecimal( payvo.getTaxmoneyamount().div(payvo.getNotaxmoneyamount()).doubleValue()));//˰��
			list.add(payvo);
		}
		return list;
	}
	/**
	 * ������ʱ������
	 * @throws BusinessException 
	 */
	public void insertTgyx_contract(ConditionVO[] conditionVOs) throws BusinessException{
		StringBuffer insertsql= new StringBuffer();
		insertsql.append("insert into tgyx_paycontract (pk_fct_ap,def61,def62,local_money,slocal_money,def98)  "+System.getProperty("line.separator"));
		insertsql.append("select f.pk_fct_ap,to_char(sum(to_number((case  when nvl(f.def61,'~')='~' then '0' else f.def61 end )))) def61,"+System.getProperty("line.separator"));//--(��ͬ��˰���)
		insertsql.append("to_char(sum(to_number((case  when nvl(f.def62,'~')='~' then '0' else f.def62 end )))) def62, "+System.getProperty("line.separator"));//--(��ͬ��˰���)
		insertsql.append("to_char(sum(a.local_money)) local_money, "+System.getProperty("line.separator"));//--�����requestmoneyamount
	    insertsql.append("to_char(sum((case when s.pk_settlement is null then 0 else a.local_money end   ))) slocal_money , "+System.getProperty("line.separator"));//�Ѹ���
	    insertsql.append("to_char(sum(to_number((case  when nvl(f.def98,'~')='~' then '0' else f.def98 end )))) def98 "+System.getProperty("line.separator"));//+�ڳ�ʵ�����˰��
	    insertsql.append("from fct_ap f "); 
	    insertsql.append("inner join ap_paybill a on f.vbillcode=a.def5 "+System.getProperty("line.separator"));
	    insertsql.append("left join  cmp_settlement s on (s.pk_busibill=a.pk_paybill and s.Settlestatus='5') "+System.getProperty("line.separator"));// --����
	    insertsql.append("left join fip_relation fi on fi.src_relationid=a.pk_paybill "+System.getProperty("line.separator"));//--ƾ֤�м��
	    insertsql.append("left join gl_voucher gl on gl.pk_voucher=fi.des_relationid "+System.getProperty("line.separator"));//--ƾ֤��
//	    insertsql.append("left join gl_detail gld on gld.pk_voucher=gl.pk_voucher  "+System.getProperty("line.separator"));//--ƾ֤��ϸ
//	    insertsql.append("left join bd_accasoa acs on (acs.pk_accasoa=gld.pk_accasoa and acs.dispname like '112303%') "+System.getProperty("line.separator"));//--��ƿ�Ŀ
//	    insertsql.append("left join bd_accasoa acsb on (acsb.pk_accasoa=gld.pk_accasoa and acs.dispname like '6601%') "+System.getProperty("line.separator")); //--��ƿ�Ŀ
	    insertsql.append("where  f.contype='Ӫ����' and f.blatest='Y' and nvl(f.dr,0)=0 and nvl(a.dr,0)=0 "+System.getProperty("line.separator"));
	    if (conditionVOs != null && conditionVOs.length > 0) {
        	List<ConditionVO> list=new ArrayList<>();
        	List<ConditionVO> vdef14list=new ArrayList<>();
        	for(int i=0;i<conditionVOs.length;i++){
        		if(!("vdef14".equals(conditionVOs[i].getFieldCode()))){
        		list.add(conditionVOs[i]);
        		}else if("vdef14".equals(conditionVOs[i].getFieldCode())){
        			String vdef14cond=getcondon();
        			if(vdef14cond!=null&&vdef14cond.length()>0){
        			insertsql.append("and  "+vdef14cond);
        			}
        		}
        	}
			String wheresql = new ConditionVO().getWhereSQL(list.toArray(new ConditionVO[0]));
			if (wheresql != null && !"".equals(wheresql)) {
				insertsql.append(" and "
					+ new ConditionVO().getWhereSQL(conditionVOs));
				}
			}		
	    insertsql.append(" group by f.pk_fct_ap"+System.getProperty("line.separator"));//  --��ͬ��
        getBaseDAO().executeUpdate(insertsql.toString());
	}
	/**
	 * �õ�Ԥ���Ŀ
	 * @param listmap
	 */
	public String getBudgetSubject(List<Map<String, String>> listmap,String pk_fct_ap){
		String result=null;
		if(listmap!=null){
		for(Map<String, String> map:listmap){
			if(map.containsValue(pk_fct_ap)){
			result=map.get("budgetsubject");
			break;
			}
		}
		}
		return result;
	}
	/**
	 * �������ɱ���Ŀmap
	 * @return
	 * @throws DAOException
	 */
   public List<Map<String, String>> getBudgetSubjectmap() throws DAOException{
	   StringBuffer sb=new StringBuffer();
       sb.append(" select  pk_fct_ap,LISTAGG(name,',') WITHIN GROUP (ORDER BY  name ) AS budgetSubject from ");
	   sb.append(" (select distinct f.pk_fct_ap, bd.name from fct_ap f ");
	   sb.append("  inner join ap_paybill a on f.vbillcode=a.def5 ");
	   sb.append("   left join  ap_payitem ai on ai.pk_paybill=a.pk_paybill ");
	   sb.append(" left join bd_defdoc bd on bd.pk_defdoc=ai.def9   ) group by pk_fct_ap");
	   List<Map<String, String>> listmap=(List<Map<String, String>>)getBaseDAO().executeQuery(sb.toString(), new MapListProcessor());
	  return listmap;
   }
	/**
	 * doubleת��Ϊ�ٷ���
	 * @param dou
	 * @return
	 */
	public String tranDecimal(double dou){
		DecimalFormat df = new DecimalFormat("0%");
		   String r = df.format(dou);
		   return r;
	}
	/**
	 * �õ�������
	 * @param str
	 * @return
	 */
	public String getYearMonth(String str){
		if(str!=null&& str.length()>0){
			String sdate=new SimpleDateFormat("yyyy��MM��dd").format(new UFDate(str).toDate());
			return sdate;
		}
		return null;
	}
	/**
	 * Stringתufdouble
	 * @param str
	 * @return
	 */
	public UFDouble tranUFDouble(String str){
		if(str!=null&&str.length()>0){
			return new UFDouble(str);
		}
		return UFDouble.ZERO_DBL;
	}
	public String getSql(){
		StringBuffer sb=new StringBuffer();
		sb.append("select"+System.getProperty("line.separator"));// 
		sb.append("org.name citycompany,"+System.getProperty("line.separator"));//--���й�˾
		sb.append("bd.project_name projectname,"+System.getProperty("line.separator"));//--��Ŀ����
		sb.append("f.vbillcode contractnum,"+System.getProperty("line.separator"));//--��ͬ����
		sb.append("f.ctname contractname,"+System.getProperty("line.separator"));//--��ͬ����
		sb.append("cust.name bunit,"+System.getProperty("line.separator"));//--��ͬ�ҷ�
		sb.append("o.name aczCompany,"+System.getProperty("line.separator"));//--��ͬ�׷�
		sb.append("f.vdef9 contractmoneyamount,"+System.getProperty("line.separator"));//--��ͬ��̬�����
		sb.append("f.d_sign signdate,"+System.getProperty("line.separator"));//--��ͬǩԼ����
		sb.append("f.vdef12 handleman,"+System.getProperty("line.separator"));//--��ͬ������
		sb.append(" ''     budgetsubject,"+System.getProperty("line.separator"));//--Ԥ���Ŀ
		sb.append("t.local_money, ");// --�����requestmoneyamount
		sb.append("t.slocal_money,  ");//�Ѹ���
		sb.append("f.pk_fct_ap ,"+System.getProperty("line.separator"));
		sb.append("t.def61,"+System.getProperty("line.separator"));//  --(��ͬ��˰���)
		sb.append(" t.def62,"+System.getProperty("line.separator"));// --��ͬ����˰���
		sb.append(" t.def98"+System.getProperty("line.separator"));// --��ͬ����˰���
		//		sb.append(" t.localcreditamount   localcreditamount,"+System.getProperty("line.separator"));//--ƾ֤�������
//		sb.append("  t.localdebitamount  localdebitamount"+System.getProperty("line.separator"));//--ƾ֤�跽���
		sb.append("from fct_ap f "+System.getProperty("line.separator"));//
		sb.append("inner join tgyx_paycontract t on t.pk_fct_ap=f.pk_fct_ap "+System.getProperty("line.separator"));// --��� 
		sb.append("left join org_orgs o on o.pk_org=f.first"+System.getProperty("line.separator"));//--��ͬ�ҷ�
		sb.append("left join bd_cust_supplier cust on cust.pk_cust_sup=f.second "+System.getProperty("line.separator"));//--����
		sb.append("left join bd_project bd on bd.pk_project=f.proname"+System.getProperty("line.separator"));// --��Ŀ��
		sb.append("left join org_orgs org on org.pk_org=f.vdef14 "+System.getProperty("line.separator"));//--��֯��
		sb.append("where  f.contype='Ӫ����';"+System.getProperty("line.separator"));//
         return sb.toString();
	}
	/**
	 * ��ȡ��ѡ���й�˾�ֶβ�ѯ����
	 * @return
	 * @throws BusinessException
	 */
	public String getcondon() throws BusinessException{
		// ��ȡ���й�˾��μ���VO
		List<ReportOrgVO> orgVOs = ReportConts.getUtils().getOrgVOs();
		HashMap<String, List<String>> maplist=new HashMap<String, List<String>>();
		for(ReportOrgVO reportvo:orgVOs){
			if(maplist.get(reportvo.getPk_region())!=null){
				maplist.get(reportvo.getPk_region()).add(reportvo.getPk_org());
			}else{
				List<String> list=new ArrayList<String>();
				list.add(reportvo.getPk_org());
				maplist.put(reportvo.getPk_region(), list);
			}
		}
		String vdef14str=queryValueMap.get("vdef14");
		List<String> finallist=new ArrayList<>();//���з��ϳ��й�˾��ѡ����˾���¼���˾
		if(vdef14str!=null&&vdef14str.length()>0){
			String[] vdef14s= vdef14str.split(",");
			for(String vdef14:vdef14s){
				finallist.add(vdef14);
				if(maplist.get(vdef14)!=null&&maplist.size()>0){
				   finallist.addAll(maplist.get(vdef14));
				}
			}
		}
		if(finallist.size()<1){return null;}
	return 	nc.vo.pf.pub.util.SQLUtil.buildSqlForIn("vdef14", finallist.toArray(new String[0]));

	}	
	/**
	 * ��ȡ��ͬpk��ƾ֤��ȫ�������ֹ��β�ѯ��
	 * @param pk_fct_ap
	 * @return
	 * @throws DAOException
	 */
public List<Map<String,String>> getgl_detailMoney() throws DAOException{
	StringBuffer sb=new StringBuffer();
	sb.append("select to_char(gld.localcreditamount) localmount,f.pk_fct_ap,'1' flag"+System.getProperty("line.separator"));//--��֯���Ҵ�������
	sb.append("from fct_ap f left join ap_paybill a on (f.vbillcode=a.def5 and f.blatest='Y')"+System.getProperty("line.separator"));
    sb.append("left join fip_relation fi on fi.src_relationid=a.pk_paybill "+System.getProperty("line.separator"));
    sb.append("left join gl_voucher gl on gl.pk_voucher=fi.des_relationid"+System.getProperty("line.separator"));
    sb.append("left join gl_detail gld on gld.pk_voucher=gl.pk_voucher  "+System.getProperty("line.separator"));
    sb.append("left join bd_accasoa acs on acs.pk_accasoa=gld.pk_accasoa  "+System.getProperty("line.separator"));
    sb.append("where  f.contype='Ӫ����'   and acs.dispname  like '112303%'"+System.getProperty("line.separator"));
    sb.append("union "+System.getProperty("line.separator"));
    sb.append("select to_char(gld.localdebitamount ) localmount,f.pk_fct_ap,'2' flag"+System.getProperty("line.separator"));//--���ű��ҽ跢����
    sb.append("from fct_ap f left join ap_paybill a on (f.vbillcode=a.def5 and f.blatest='Y')"+System.getProperty("line.separator"));
    sb.append("left join fip_relation fi on fi.src_relationid=a.pk_paybill "+System.getProperty("line.separator"));
    sb.append("left join gl_voucher gl on gl.pk_voucher=fi.des_relationid"+System.getProperty("line.separator"));
    sb.append("left join gl_detail gld on gld.pk_voucher=gl.pk_voucher  "+System.getProperty("line.separator"));
    sb.append("left join bd_accasoa acs on acs.pk_accasoa=gld.pk_accasoa  "+System.getProperty("line.separator"));
    sb.append("where  f.contype='Ӫ����'   and acs.dispname  like '6601%'"+System.getProperty("line.separator"));
	List<Map<String,String>> listmap=(List<Map<String,String>>)getBaseDAO().executeQuery(sb.toString(), new MapListProcessor());
	return listmap;
}	
}
