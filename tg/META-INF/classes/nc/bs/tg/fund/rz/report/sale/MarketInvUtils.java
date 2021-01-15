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
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.sale.MarketInvVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.tgfp.report.ReportOrgVO;

import com.ufida.dataset.IContext;

/**
 * 营销合同发票付款台账
 * @author yy
 *
 */
public class MarketInvUtils extends ReportUtils{
	static MarketInvUtils utils;
 public static MarketInvUtils getMarketInvUtils(){
	 if(utils==null){
	 utils=new  MarketInvUtils();
	 }
	 return utils;
 }
 public MarketInvUtils(){
	 setKeys(BeanHelper.getInstance().getPropertiesAry(new MarketInvVO()));
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
			List<MarketInvVO> list = null;
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

	
	public List<MarketInvVO> getVoList(ConditionVO[] conditionVOs) throws BusinessException{
		List<Map<String,String>> maplist=(List<Map<String, String>>)getBaseDAO().executeQuery(getSql(conditionVOs), new MapListProcessor());
		List<MarketInvVO> listvo=new ArrayList<MarketInvVO>();
		List<Map<String, String>> budgetSubjectmap=getBudgetSubjectmap();
		List<Map<String,String>> qkmoneylist=getqkmoney();
		for(Map<String,String > map:maplist){
			MarketInvVO vo=new MarketInvVO();
			vo.setAczCompany(map.get("aczcompany"));
			vo.setBudgetSubject(getBudgetSubject(budgetSubjectmap, map.get("pk_payablebill")));
			vo.setBunit(map.get("bunit"));
			vo.setCitycompany(map.get("citycompany"));
			vo.setContractmoneyamount(tranUFDouble(map.get("contractmoneyamount")));
			vo.setContractname(map.get("contractname"));
			vo.setContractnum(map.get("contractnum"));
			vo.setGlnum(map.get("glnum"));
			vo.setHandleman(map.get("handleman"));
			vo.setInvoiceNum(map.get("invoicenum"));
			vo.setInvoiceSumamount(tranUFDouble(map.get("invoicesumamount")));
			vo.setNoTaxMoneyamount(tranUFDouble(map.get("notaxmoneyamount")));
			vo.setProjectname(map.get("projectname"));
			vo.setQkBill(map.get("qkbill"));
			vo.setBillno(map.get("billno"));
			vo.setQzfla(map.get("qzfla"));
			vo.setRzmonth(getYearMonth(map.get("rzmonth")));
			vo.setSigndate(getYearMonth(map.get("signdate")));
			vo.setTaxMoneyamount(tranUFDouble(map.get("taxmoneyamount")));
			vo.setTaxrat(tranDecimal(tranUFDouble(map.get("taxrat")).doubleValue()));
             
			if(qkmoneylist!=null){
				for(Map<String,String> qkmap:qkmoneylist){
					if(qkmap.containsValue(map.get("def1"))){
						vo.setQkmoneyamount(tranUFDouble(qkmap.get("def16")));//付款金额
						vo.setFkmoneyamount(tranUFDouble(qkmap.get("pdef16")));//请款金额
					}
					
				}
			}
			listvo.add(vo);
		}
		return listvo;
	}
	/**
	 * 根据应付单def1找到对应金额
	 * @param billno
	 * @return
	 * @throws DAOException 
	 */
	private List<Map<String,String>> getqkmoney() throws DAOException{
		String sql="select to_char(sum(to_number((case  when nvl(ap.def16,'~')='~' then '0' else ap.def16 end )))) def16,a.def1, "
				+" to_char(sum((case  when t.settlestatus=5 then to_number((case  when nvl(ap.def16,'~')='~' then '0' else ap.def16 end ))else 0 end)))  pdef16"
				 +" from ap_payablebill  a   left join ap_paybill  ap on a.def1=ap.def1 left join cmp_settlement t on t.pk_busibill=ap.pk_paybill group by a.def1";

		List<Map<String,String>> listmap=(List<Map<String,String>>)getBaseDAO().executeQuery(sql, new MapListProcessor());
		return listmap;
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
	 * double转化为百分数
	 * @param dou
	 * @return
	 */
	public String tranDecimal(double dou){
		
		DecimalFormat df = new DecimalFormat("0%");
		   String r = df.format(dou/100);
		   return r;
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
	public String  getSql(ConditionVO[] conditionVOs) throws BusinessException{
		StringBuffer sb=new StringBuffer();
		sb.append("select distinct "+System.getProperty("line.separator"));//
		sb.append("org.name citycompany,"+System.getProperty("line.separator"));//--城市公司
		sb.append("bd.project_name projectname,"+System.getProperty("line.separator"));//--项目名称
		sb.append("f.vbillcode contractnum,"+System.getProperty("line.separator"));//--合同编码
		sb.append("f.ctname contractname,"+System.getProperty("line.separator"));//--合同名称
		sb.append("cust.name bunit,"+System.getProperty("line.separator"));//--合同乙方
		sb.append("o.name aczCompany,"+System.getProperty("line.separator"));//--合同甲方
		sb.append("f.vdef11 contractmoneyamount,"+System.getProperty("line.separator"));//--合同动态金额税额
		sb.append("f.d_sign signdate,"+System.getProperty("line.separator"));//--合同签约金额
		sb.append("f.vdef12 handleman,"+System.getProperty("line.separator"));//--合同管理人
		sb.append(" a.pk_payablebill,"+System.getProperty("line.separator"));//--预算科目
		sb.append(" a.billno ,"+System.getProperty("line.separator"));//--单据号
		sb.append(" a.def1 ,"+System.getProperty("line.separator"));//--外系统主键
		sb.append("a.def2 qkBill,"+System.getProperty("line.separator"));//请款单号
		sb.append("h.fph invoicenum,"+System.getProperty("line.separator"));//发票号
		sb.append("to_char(h.costmoneyin) taxMoneyamount,"+System.getProperty("line.separator"));//含税金额
		sb.append("to_char(h.costmoney) noTaxMoneyamount, "+System.getProperty("line.separator"));//不含税金额
		sb.append("to_char(hb.taxrate) taxrat,"+System.getProperty("line.separator"));//税率
		sb.append("h.def5 invoiceSumamount,"+System.getProperty("line.separator"));//税额
		sb.append("to_char(gl.prepareddate) rzmonth,"+System.getProperty("line.separator"));//--凭证日期
		sb.append("to_char((case when nvl(gl.num,0)<>0 then '记-'||lpad(gl.num,4,'0') else '' end)) glnum,"+System.getProperty("line.separator"));//--凭证号
		sb.append("a.def50 qzfla "+System.getProperty("line.separator"));//--应付单票据权责发生制
		sb.append(" from ap_payablebill  a "+System.getProperty("line.separator"));//
		sb.append("left join fct_ap f on (f.vbillcode=a.def5 and f.blatest='Y') "+System.getProperty("line.separator"));// --付款合同
		sb.append("left join hzvat_invoice_h h on h.def8=a.def3 "+System.getProperty("line.separator"));//--发票表头
		sb.append("left join org_orgs o on o.pk_org=f.first "+System.getProperty("line.separator"));//--甲方 
		sb.append("left join bd_cust_supplier cust on cust.pk_cust_sup=f.second "+System.getProperty("line.separator"));//--乙方 
	    sb.append("left join org_orgs org on org.pk_org=f.vdef14 "+System.getProperty("line.separator"));// --组织表
		sb.append("left join bd_project bd on (bd.pk_project=f.proname and bd.enablestate=2) "+System.getProperty("line.separator"));// --项目表
		sb.append("left join hzvat_invoice_b hb on hb.pk_invoice_h=h.pk_invoice_h "+System.getProperty("line.separator"));// --发票表体
		sb.append("left join fip_relation fip on fip.src_relationid=a.pk_payablebill "+System.getProperty("line.separator"));// --凭证中间表
		sb.append("left join gl_voucher gl on gl.pk_voucher=fip.des_relationid "+System.getProperty("line.separator"));//--凭证
//		sb.append("left join fct_execution_b fb on fb.pk_fct_ap=f.pk_fct_ap "+System.getProperty("line.separator"));// --合同执行情况表体
		sb.append("where  nvl(f.dr,0)=0 and nvl(a.dr,0)=0 and nvl(bd.dr,0)=0 and nvl(hb.dr,0)=0 and nvl(fip.dr,0)=0 and nvl(gl.dr,0)=0");
        sb.append(" and f.contype='营销类'");
        
        
        if (conditionVOs != null && conditionVOs.length > 0) {
        	List<ConditionVO> list=new ArrayList<>();
        	List<ConditionVO> vdef14list=new ArrayList<>();
        	for(int i=0;i<conditionVOs.length;i++){
        		if(!("vdef14".equals(conditionVOs[i].getFieldCode()))){
        		list.add(conditionVOs[i]);
        		}else if("vdef14".equals(conditionVOs[i].getFieldCode())){
        		String vdef14con=	getcondon();
        		if(vdef14con!=null&&vdef14con.length()>0){
        			sb.append("and  "+vdef14con);
        		  }
        		}
        	}
			String wheresql = new ConditionVO().getWhereSQL(list.toArray(new ConditionVO[0]));
			if (wheresql != null && !"".equals(wheresql)) {
				sb.append(" and "
					+ new ConditionVO().getWhereSQL(conditionVOs));
				}
			}	
		//		sb.append("and a.billno='D12020060900000442' ");
		return sb.toString();
		
	}
	/**
	 * 得到预算科目
	 * @param listmap
	 */
	public String getBudgetSubject(List<Map<String, String>> listmap,String pk_payablebill){
		String result=null;
		if(listmap!=null){
		for(Map<String, String> map:listmap){
			if(map.containsValue(pk_payablebill)){
			result=map.get("budgetsubject");
			break;
			}
		}
		}
		return result;
	}
	/**
	 * 应付单表体成本科目map
	 * @return
	 * @throws DAOException
	 */
   public List<Map<String, String>> getBudgetSubjectmap() throws DAOException{
	   StringBuffer sb=new StringBuffer();
	   sb.append("select pk_payablebill,LISTAGG(bd.name,',') WITHIN GROUP (ORDER BY 	bd.name) AS budgetSubject ");
	   sb.append(" from ap_payableitem ai left join bd_defdoc bd on bd.pk_defdoc=ai.def7  group by ai.pk_payablebill ");
	  List<Map<String, String>> listmap=(List<Map<String, String>>)getBaseDAO().executeQuery(sb.toString(), new MapListProcessor());
      
	  return listmap;
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
}
