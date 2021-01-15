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
 * 发票合同台账
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
		// TODO 自动生成的方法存根
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
			payvo.setCitycompany(map.get("citycompany"));	//城市公司
			payvo.setProjectname(map.get("projectname"));	//项目名称
			payvo.setContractnum(map.get("contractnum"));	//合同编号
			payvo.setContractname(map.get("contractname"));	//合同名称
			payvo.setBunit(map.get("bunit"));	//乙方单位
			payvo.setAczCompany(map.get("aczcompany"));	//出账公司
			payvo.setContractmoneyamount(tranUFDouble(map.get("contractmoneyamount")));	//合同动态金额
			payvo.setSigndate(getYearMonth(map.get("signdate")));	//签订日期
			payvo.setHandleman(map.get("handleman"));	//经办人
			payvo.setBudgetSubject(getBudgetSubject(budgetSubjectlist,map.get("pk_fct_ap")));	//预算科目
			payvo.setRequestmoneyamount(tranUFDouble(map.get("local_money")).add(tranUFDouble(map.get("def98"))));	//累计已请款
			payvo.setPaymoneyamount(tranUFDouble(map.get("slocal_money")).add(tranUFDouble(map.get("def98"))));	//累计已付款
			payvo.setInnvocemoneyamount(tranUFDouble(map.get("def61")).add(localcreditamount));	//累计已收发票(D)
			payvo.setNotaxmoneyamount(tranUFDouble(map.get("def62")).add(localdebitamount));	//不含税金额(E)
			payvo.setTaxmoneyamount(payvo.getInnvocemoneyamount().sub(payvo.getNotaxmoneyamount()));	//税额(F)
			if(!(payvo.getNotaxmoneyamount().compareTo(UFDouble.ZERO_DBL)==0))
			payvo.setTax(tranDecimal( payvo.getTaxmoneyamount().div(payvo.getNotaxmoneyamount()).doubleValue()));//税率
			list.add(payvo);
		}
		return list;
	}
	/**
	 * 插入临时表数据
	 * @throws BusinessException 
	 */
	public void insertTgyx_contract(ConditionVO[] conditionVOs) throws BusinessException{
		StringBuffer insertsql= new StringBuffer();
		insertsql.append("insert into tgyx_paycontract (pk_fct_ap,def61,def62,local_money,slocal_money,def98)  "+System.getProperty("line.separator"));
		insertsql.append("select f.pk_fct_ap,to_char(sum(to_number((case  when nvl(f.def61,'~')='~' then '0' else f.def61 end )))) def61,"+System.getProperty("line.separator"));//--(合同含税金额)
		insertsql.append("to_char(sum(to_number((case  when nvl(f.def62,'~')='~' then '0' else f.def62 end )))) def62, "+System.getProperty("line.separator"));//--(合同含税金额)
		insertsql.append("to_char(sum(a.local_money)) local_money, "+System.getProperty("line.separator"));//--已请款requestmoneyamount
	    insertsql.append("to_char(sum((case when s.pk_settlement is null then 0 else a.local_money end   ))) slocal_money , "+System.getProperty("line.separator"));//已付款
	    insertsql.append("to_char(sum(to_number((case  when nvl(f.def98,'~')='~' then '0' else f.def98 end )))) def98 "+System.getProperty("line.separator"));//+期初实付款（含税）
	    insertsql.append("from fct_ap f "); 
	    insertsql.append("inner join ap_paybill a on f.vbillcode=a.def5 "+System.getProperty("line.separator"));
	    insertsql.append("left join  cmp_settlement s on (s.pk_busibill=a.pk_paybill and s.Settlestatus='5') "+System.getProperty("line.separator"));// --结算
	    insertsql.append("left join fip_relation fi on fi.src_relationid=a.pk_paybill "+System.getProperty("line.separator"));//--凭证中间表
	    insertsql.append("left join gl_voucher gl on gl.pk_voucher=fi.des_relationid "+System.getProperty("line.separator"));//--凭证表
//	    insertsql.append("left join gl_detail gld on gld.pk_voucher=gl.pk_voucher  "+System.getProperty("line.separator"));//--凭证明细
//	    insertsql.append("left join bd_accasoa acs on (acs.pk_accasoa=gld.pk_accasoa and acs.dispname like '112303%') "+System.getProperty("line.separator"));//--会计科目
//	    insertsql.append("left join bd_accasoa acsb on (acsb.pk_accasoa=gld.pk_accasoa and acs.dispname like '6601%') "+System.getProperty("line.separator")); //--会计科目
	    insertsql.append("where  f.contype='营销类' and f.blatest='Y' and nvl(f.dr,0)=0 and nvl(a.dr,0)=0 "+System.getProperty("line.separator"));
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
	    insertsql.append(" group by f.pk_fct_ap"+System.getProperty("line.separator"));//  --合同单
        getBaseDAO().executeUpdate(insertsql.toString());
	}
	/**
	 * 得到预算科目
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
	 * 付款单表体成本科目map
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
	 * double转化为百分数
	 * @param dou
	 * @return
	 */
	public String tranDecimal(double dou){
		DecimalFormat df = new DecimalFormat("0%");
		   String r = df.format(dou);
		   return r;
	}
	/**
	 * 得到年月日
	 * @param str
	 * @return
	 */
	public String getYearMonth(String str){
		if(str!=null&& str.length()>0){
			String sdate=new SimpleDateFormat("yyyy年MM月dd").format(new UFDate(str).toDate());
			return sdate;
		}
		return null;
	}
	/**
	 * String转ufdouble
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
		sb.append("org.name citycompany,"+System.getProperty("line.separator"));//--城市公司
		sb.append("bd.project_name projectname,"+System.getProperty("line.separator"));//--项目名称
		sb.append("f.vbillcode contractnum,"+System.getProperty("line.separator"));//--合同编码
		sb.append("f.ctname contractname,"+System.getProperty("line.separator"));//--合同名称
		sb.append("cust.name bunit,"+System.getProperty("line.separator"));//--合同乙方
		sb.append("o.name aczCompany,"+System.getProperty("line.separator"));//--合同甲方
		sb.append("f.vdef9 contractmoneyamount,"+System.getProperty("line.separator"));//--合同动态金额金额
		sb.append("f.d_sign signdate,"+System.getProperty("line.separator"));//--合同签约日期
		sb.append("f.vdef12 handleman,"+System.getProperty("line.separator"));//--合同管理人
		sb.append(" ''     budgetsubject,"+System.getProperty("line.separator"));//--预算科目
		sb.append("t.local_money, ");// --已请款requestmoneyamount
		sb.append("t.slocal_money,  ");//已付款
		sb.append("f.pk_fct_ap ,"+System.getProperty("line.separator"));
		sb.append("t.def61,"+System.getProperty("line.separator"));//  --(合同含税金额)
		sb.append(" t.def62,"+System.getProperty("line.separator"));// --合同不含税金额
		sb.append(" t.def98"+System.getProperty("line.separator"));// --合同不含税金额
		//		sb.append(" t.localcreditamount   localcreditamount,"+System.getProperty("line.separator"));//--凭证贷方金额
//		sb.append("  t.localdebitamount  localdebitamount"+System.getProperty("line.separator"));//--凭证借方金额
		sb.append("from fct_ap f "+System.getProperty("line.separator"));//
		sb.append("inner join tgyx_paycontract t on t.pk_fct_ap=f.pk_fct_ap "+System.getProperty("line.separator"));// --付款单 
		sb.append("left join org_orgs o on o.pk_org=f.first"+System.getProperty("line.separator"));//--合同乙方
		sb.append("left join bd_cust_supplier cust on cust.pk_cust_sup=f.second "+System.getProperty("line.separator"));//--客商
		sb.append("left join bd_project bd on bd.pk_project=f.proname"+System.getProperty("line.separator"));// --项目表
		sb.append("left join org_orgs org on org.pk_org=f.vdef14 "+System.getProperty("line.separator"));//--组织表
		sb.append("where  f.contype='营销类';"+System.getProperty("line.separator"));//
         return sb.toString();
	}
	/**
	 * 获取所选城市公司字段查询条件
	 * @return
	 * @throws BusinessException
	 */
	public String getcondon() throws BusinessException{
		// 获取所有公司层次集合VO
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
		List<String> finallist=new ArrayList<>();//所有符合城市公司所选区域公司的下级公司
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
	 * 获取合同pk与凭证表（全部查出防止多次查询）
	 * @param pk_fct_ap
	 * @return
	 * @throws DAOException
	 */
public List<Map<String,String>> getgl_detailMoney() throws DAOException{
	StringBuffer sb=new StringBuffer();
	sb.append("select to_char(gld.localcreditamount) localmount,f.pk_fct_ap,'1' flag"+System.getProperty("line.separator"));//--组织本币贷发生额
	sb.append("from fct_ap f left join ap_paybill a on (f.vbillcode=a.def5 and f.blatest='Y')"+System.getProperty("line.separator"));
    sb.append("left join fip_relation fi on fi.src_relationid=a.pk_paybill "+System.getProperty("line.separator"));
    sb.append("left join gl_voucher gl on gl.pk_voucher=fi.des_relationid"+System.getProperty("line.separator"));
    sb.append("left join gl_detail gld on gld.pk_voucher=gl.pk_voucher  "+System.getProperty("line.separator"));
    sb.append("left join bd_accasoa acs on acs.pk_accasoa=gld.pk_accasoa  "+System.getProperty("line.separator"));
    sb.append("where  f.contype='营销类'   and acs.dispname  like '112303%'"+System.getProperty("line.separator"));
    sb.append("union "+System.getProperty("line.separator"));
    sb.append("select to_char(gld.localdebitamount ) localmount,f.pk_fct_ap,'2' flag"+System.getProperty("line.separator"));//--集团本币借发生额
    sb.append("from fct_ap f left join ap_paybill a on (f.vbillcode=a.def5 and f.blatest='Y')"+System.getProperty("line.separator"));
    sb.append("left join fip_relation fi on fi.src_relationid=a.pk_paybill "+System.getProperty("line.separator"));
    sb.append("left join gl_voucher gl on gl.pk_voucher=fi.des_relationid"+System.getProperty("line.separator"));
    sb.append("left join gl_detail gld on gld.pk_voucher=gl.pk_voucher  "+System.getProperty("line.separator"));
    sb.append("left join bd_accasoa acs on acs.pk_accasoa=gld.pk_accasoa  "+System.getProperty("line.separator"));
    sb.append("where  f.contype='营销类'   and acs.dispname  like '6601%'"+System.getProperty("line.separator"));
	List<Map<String,String>> listmap=(List<Map<String,String>>)getBaseDAO().executeQuery(sb.toString(), new MapListProcessor());
	return listmap;
}	
}
