package nc.bs.tg.fund.rz.report.cost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.dao.DAOException;
import nc.bs.tg.fund.rz.report.ReportConts;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.cost.MarketingManageAndAccrualVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.tgfp.report.ReportOrgVO;

import com.ufida.dataset.IContext;

/**
 * 营销、管理费用后补票及权责发生制明细表处理
 * 
 * @author Administrator
 */
public class MarketingManageAndAccrualUtils extends ReportUtils {

	static MarketingManageAndAccrualUtils utils;

	public static MarketingManageAndAccrualUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new MarketingManageAndAccrualUtils();
		}
		return utils;
	}

	public MarketingManageAndAccrualUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new MarketingManageAndAccrualVO()));
	}

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);

			List<MarketingManageAndAccrualVO> list = null;
			// 调用方法,获取最终的查询结果集
			list = getMarketingManageAndAccrualVO();

			if (list != null && list.size() > 0) {
				if (queryValueMap.get("region") != null) {
					// 增加最后区域筛选
					List<MarketingManageAndAccrualVO> finalcoll = new ArrayList<MarketingManageAndAccrualVO>();
					// 初始化序号为0
					int sequeNumGlobal = 0;
					for (MarketingManageAndAccrualVO marketingManageAndAccrualVO : list) {
						// 如果area有值,则认定为需显示查询数据
						// 每遍历一个元素，序号加1
						if (marketingManageAndAccrualVO.getRegion() != null) {
							++sequeNumGlobal;
							marketingManageAndAccrualVO
									.setSortnumglobal(new UFDouble(
											sequeNumGlobal));
							finalcoll.add(marketingManageAndAccrualVO);
						}
					}
					cmpreportresults = transReportResult(finalcoll);
				} else {
					cmpreportresults = transReportResult(list);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	/**
	 * 查询 营销、管理费用后补票及权责发生制明细数据
	 * 
	 * @return
	 */
	private List<MarketingManageAndAccrualVO> getMarketingManageAndAccrualVO()
			throws BusinessException {
		List<MarketingManageAndAccrualVO> allList = getMarketingManageAndAccrual();
		return allList == null || allList.size() == 0 ? new ArrayList<MarketingManageAndAccrualVO>()
				: allList;
	}

	private List<MarketingManageAndAccrualVO> getMarketingManageAndAccrual()
			throws BusinessException {
		// 存放最终结果集
		List<MarketingManageAndAccrualVO> finalVOList = new ArrayList<MarketingManageAndAccrualVO>();

		// 获取所有公司层次集合VO
		List<ReportOrgVO> orgVOs = ReportConts.getUtils().getOrgVOs();
		// 存放pk_org和pk_region的对应关系
		Map<String, String> regionMap = new HashMap<String, String>();
		Map<String, ArrayList<String>> orgsMap = new HashMap<String, ArrayList<String>>();
		for (ReportOrgVO reportOrgVO : orgVOs) {
			String pk_org = reportOrgVO.getPk_org();
			String pk_region = reportOrgVO.getPk_region();
			// 存放pk_org——pk_region
			regionMap.put(pk_org, pk_region);
			/**
			 * 从orgsMap中获取pk_region对应的list集合，如果集合为null则
			 * orgsMap要存储以当前pk_region为key，存放了当前pk_org的list为value的新键值对
			 * 如果不为null则说明当前orgsMap中已存在pk_region为key的键值对，只需要往list 中存入当前pk_org即可
			 */
			ArrayList<String> arrayList = orgsMap.get(pk_region);
			if (arrayList != null) {
				arrayList.add(pk_org);
			} else {
				ArrayList<String> arrayList2 = new ArrayList<String>();
				arrayList2.add(pk_org);
				orgsMap.put(pk_region, arrayList2);
			}
		}

		// 存放区域pk_region的数组
		String[] pk_regionArr = null;
		ArrayList<String> arrpk_orgs = new ArrayList<String>();

		// 区域公司,查询条件区域公司遍历找到其下属公司再作为查询条件
		if (queryValueMap.get("region") != null) {
			pk_regionArr = queryValueMap.get("region").split(",");
			for (String string : pk_regionArr) {
				arrpk_orgs.add(string);
				ArrayList<String> arrpk_org = orgsMap.get(string);
				if (arrpk_org != null) {
					arrpk_orgs.addAll(arrpk_org);
				}
			}
		}
		// 构造查询sql语句
		StringBuffer sql = QuerySQL(arrpk_orgs);

		// 调用封装的API进行数据库查询，获取经过处理的查询结果
//		@SuppressWarnings("unchecked")
		List<MarketingManageAndAccrualVO> resultVOList = (List<MarketingManageAndAccrualVO>) getBaseDAO()
				.executeQuery(
						sql.toString(),
						new BeanListProcessor(MarketingManageAndAccrualVO.class));
		Map<String, String> vsql = getVoucherSqlMap();
		//add by tjl 2020-07-01
//		List<MarketingManageAndAccrualVO> resultVOList = getAllBillVOList(arrpk_orgs);
		Map<String, ArrayList<String>> infoMap = new HashMap<String, ArrayList<String>>();
		Map<String,String> outsidenoMap = new HashMap<String,String>();
		
		if (resultVOList != null && resultVOList.size() > 0) {
			for (MarketingManageAndAccrualVO vo : resultVOList) {
				String billtype = vo.getBilltype();//业务单据类型
				String billid = vo.getBillid();//业务单据pk
				if(billtype.contains("264X")){
					billid = billid.split("_")[0];
					billtype = "264X";
				}else if(billtype.contains("F1")){
					billtype = "F1";//应付单
				}else if(billtype.contains("F3")){
					billtype = "F3";//付款单
				}else if(billtype.contains("267X")){
					billtype = "267X";//网报工单
				}
				if (!infoMap.containsKey(billtype)) {
					infoMap.put(billtype, new ArrayList<String>());
				}
				infoMap.get(billtype).add(billid);
			}
			finalVOList = getVOByRegion(resultVOList, regionMap);
			
		}
		
	
		
		
		//读取报销单数据
		Map<String,MarketingManageAndAccrualVO> bxMap = null;
		if(infoMap.get("264X")!=null){
			bxMap = getBxInfo(infoMap.get("264X").toArray(new String[0]));		
		}
		//读取应付单数据
		Map<String,MarketingManageAndAccrualVO> payableMap = null;
		if(infoMap.get("F1")!=null){
			payableMap = getPaybleInfo(infoMap.get("F1").toArray(new String[0]));		
		}
		//读取付款单数据
		Map<String,MarketingManageAndAccrualVO> payMap = null;
		if(infoMap.get("F3")!=null){
			payMap = getPayInfo(infoMap.get("F3").toArray(new String[0]));		
		}
		//读取网报工单数据
		Map<String,MarketingManageAndAccrualVO> addTickeyMap = null;
		if(infoMap.get("267X")!=null){
			addTickeyMap = getAddTickeyInfo(infoMap.get("267X").toArray(new String[0]));
		}
		//权责发生制Map
		Map<String,String> outsideMap = null;
		if(payableMap!=null){
			outsideMap = getOutsideMap(payableMap);
		}
		if(finalVOList!=null&&finalVOList.size()>0){
			for(MarketingManageAndAccrualVO marketVO:resultVOList){
				String billtype = marketVO.getBilltype();//业务单据类型
				String billid = marketVO.getBillid();//业务单据pk
				MarketingManageAndAccrualVO vo = null;
				if(billtype.contains("264X")){
					String pk_bxkey = billid.split("_")[0];
					vo = bxMap==null?null:bxMap.get(pk_bxkey);
				}else if(billtype.contains("F1")){
					vo =payableMap==null?null:payableMap.get(billid);
				}else if(billtype.contains("F3")){
					vo =payMap==null?null:payMap.get(billid);
				}else if(billtype.contains("267X")){
					vo =addTickeyMap==null?null:addTickeyMap.get(billid);
				}
				if(vo!=null){
					if (marketVO.getMerchantAssistantAccountingName() != null) {
						//迭代器遍历key中的客商辅助核算,如果符合,则将value放进vo中
						Iterator<Entry<String,String>> iter = vsql.entrySet().iterator();
						while(iter.hasNext()){
							Entry<String,String> map = iter.next();
							if(marketVO.getMerchantAssistantAccountingName().equals(map.getKey())){
								marketVO.setMerchantAssistantAccountingName(map.getValue());
							}
						}
					}
					marketVO.setOutsideno(vo.getOutsideno());//外系统单据号
					marketVO.setInvoiceAmount(vo.getInvoiceAmount());
					marketVO.setNotTakebackInvoiceAmount(marketVO
							.getPayAmount().sub(vo.getInvoiceAmount()));
					marketVO.setBillmaker(vo.getBillmaker()==null?null:vo.getBillmaker());
					marketVO.setBillno(vo.getBillno());
					marketVO.setContractCode(vo.getContractCode());
					marketVO.setContractName(vo.getContractName());
					marketVO.setContractRate(vo.getContractRate());
					if(billtype.contains("F3")){
						Iterator<Entry<String,String>> iter = outsideMap.entrySet().iterator();
						while(iter.hasNext()){
							Entry<String,String> map = iter.next();
							if(marketVO.getOutsideno().equals(map.getKey())){
								marketVO.setAccrualTag(map.getValue());
							}
						}
					}else{
						marketVO.setAccrualTag(vo.getAccrualTag());
					}
					marketVO.setPayTicketTag(vo.getPayTicketTag());
				}
				
			}
			
		}
		
		return finalVOList.size() > 0 ? finalVOList : null;
	}
	
	
	private Map<String, String> getOutsideMap(
			Map<String, MarketingManageAndAccrualVO> payableMap) {
		Iterator<Entry<String, MarketingManageAndAccrualVO>> iter = payableMap.entrySet().iterator();
		Map<String,String> outsideMap = new HashMap<String,String>();
		while(iter.hasNext()){
			Entry<String, MarketingManageAndAccrualVO> map = iter.next();
			outsideMap.put(map.getValue().getOutsideno(), map.getValue().getAccrualTag());
		}
		return outsideMap;
	}

	private Map<String, MarketingManageAndAccrualVO> getAddTickeyInfo(
			String[] keys) throws DAOException {
		Map<String,MarketingManageAndAccrualVO> infoMap = new HashMap<String,MarketingManageAndAccrualVO>();
		StringBuffer sqlpkb = new StringBuffer();
		sqlpkb.append(" select to_number((case ");
		sqlpkb.append(" when hz.costmoneyin is not null then ");
		sqlpkb.append(" hz.costmoneyin ");
		sqlpkb.append(" else ");
		sqlpkb.append("   0 ");
		sqlpkb.append("  end)) invoiceAmount, ");
		sqlpkb.append(" '' outsideno, ");//外系统单据号
		sqlpkb.append(" ap.pk_addbill billid, ");//单据pk
		sqlpkb.append("  to_number((case ");
		sqlpkb.append("  when hz.costmoneyin is not null then ");
		sqlpkb.append("   hz.costmoneyin ");
		sqlpkb.append("    else ");
		sqlpkb.append("     0 ");
		sqlpkb.append("     end)) as notTakebackInvoiceAmount, ");
		sqlpkb.append("  (select smn.user_name ");
		sqlpkb.append("     from sm_user smn ");
		sqlpkb.append("   where smn.cuserid = ap.billmaker ");
		sqlpkb.append("     and smn.dr = 0) as billmaker, ");
		sqlpkb.append(" ap.billno billno, ");
		sqlpkb.append("  ap.concode contractCode, ");
		sqlpkb.append(" ap.conname contractName, ");
		sqlpkb.append(" to_char(fct_ap.rate)||'%' as contractRate, ");
		sqlpkb.append(" '' accrualTag, ");
		sqlpkb.append(" '' payTicketTag ");
		sqlpkb.append(" from yer_fillbill ap ");
		sqlpkb.append(" left join hzvat_invoice_h hz ");
		sqlpkb.append(" on hz.def8 = ap.imagcode ");
		sqlpkb.append(" and hz.dr = 0 ");
		sqlpkb.append(" left join fct_ap ");
		sqlpkb.append(" on fct_ap.vbillcode = ap.concode ");
		sqlpkb.append(" and ctname = ap.conname ");
		sqlpkb.append(" and blatest = 'Y' ");
		sqlpkb.append(" where ap.dr = 0 ");
		sqlpkb.append(" and "+SQLUtil.buildSqlForIn(" ap.pk_addbill", keys));
		List<MarketingManageAndAccrualVO> listvob = (List<MarketingManageAndAccrualVO>) getBaseDAO()
				.executeQuery(
						sqlpkb.toString(),
						new BeanListProcessor(
								MarketingManageAndAccrualVO.class));
		
		if(listvob!=null&&listvob.size()>0){
			for(MarketingManageAndAccrualVO vo:listvob){
				infoMap.put(vo.getBillid(), vo);
			}	
		}
		
		return infoMap;
	}

	private Map<String, MarketingManageAndAccrualVO> getPayInfo(String[] keys) throws BusinessException {
		StringBuffer sqlpkb = new StringBuffer();
		Map<String,MarketingManageAndAccrualVO> infoMap = new HashMap<String,MarketingManageAndAccrualVO>();
		sqlpkb.append(
				" select to_number((case when hz.costmoneyin is not null then hz.costmoneyin else 0 end)) invoiceAmount,")
				.append("\r\n");
		sqlpkb.append(" ap.def2 outsideno, ");//外系统单据号
		sqlpkb.append(" ap.pk_paybill billid, ");//单据pk
		sqlpkb.append(
				" to_number((case when hz.costmoneyin is not null then hz.costmoneyin else 0 end)) as notTakebackInvoiceAmount, ")
				.append("\r\n");
		sqlpkb.append(
				" (select smn.user_name from sm_user smn where smn.cuserid = ap.billmaker and smn.dr = 0) as billmaker,ap.billno billno,ap.def5 contractCode,ap.def6 contractName,")
				.append("\r\n");
		sqlpkb.append(
				" to_char(fct_ap.rate)||'%' as contractRate,ap.def76 accrualTag,ap.def55 payTicketTag ")
				.append("\r\n");
		sqlpkb.append(
				" from ap_paybill ap  left join hzvat_invoice_h hz on hz.def8 = ap.def3  and hz.dr = 0 ")
				.append("\r\n");
		sqlpkb.append(
				" left join fct_ap on fct_ap.vbillcode = ap.def5 and ctname = ap.def6 and blatest = 'Y' ")
				.append("\r\n");
		sqlpkb.append(" where ap.dr = 0 ");
		sqlpkb.append(" and  "+SQLUtil.buildSqlForIn(" ap.pk_paybill", keys) );
		
		List<MarketingManageAndAccrualVO> listvo = (List<MarketingManageAndAccrualVO>) getBaseDAO()
				.executeQuery(
						sqlpkb.toString(),
						new BeanListProcessor(
								MarketingManageAndAccrualVO.class));
		if(listvo!=null&&listvo.size()>0){
			for(MarketingManageAndAccrualVO vo:listvo){
				infoMap.put(vo.getBillid(), vo);
			}	
		}
		return infoMap;
	}

	private Map<String, MarketingManageAndAccrualVO> getPaybleInfo(String[] keys) throws DAOException {
		Map<String,MarketingManageAndAccrualVO> infoMap = new HashMap<String,MarketingManageAndAccrualVO>();
		Map<String,String> outsideMap = new HashMap<String,String>();
		StringBuffer sqlpk = new StringBuffer();
		sqlpk.append(
				" select to_number((case when hz.costmoneyin is not null then hz.costmoneyin else 0 end)) invoiceAmount,")
				.append("\r\n");
		sqlpk.append(" ap.def2 outsideno, ");//外系统单据号
		sqlpk.append(" ap.pk_payablebill billid, ");//单据pk
		sqlpk.append(
				" to_number((case when hz.costmoneyin is not null then hz.costmoneyin else 0 end)) as notTakebackInvoiceAmount, ")
				.append("\r\n");
		sqlpk.append(
				" (select smn.user_name from sm_user smn where smn.cuserid = ap.billmaker and smn.dr = 0) as billmaker,ap.billno billno,ap.def5 contractCode,ap.def6 contractName,")
				.append("\r\n");
		sqlpk.append(
				" to_char(fct_ap.rate)||'%' as contractRate,ap.def50 accrualTag,ap.def55 payTicketTag ")
				.append("\r\n");
		sqlpk.append(
				" from ap_payablebill ap  left join hzvat_invoice_h hz on hz.def8 = ap.def3  and hz.dr = 0 ")
				.append("\r\n");
		sqlpk.append(
				" left join fct_ap on fct_ap.vbillcode = ap.def5 and ctname = ap.def6 and blatest = 'Y' ")
				.append("\r\n");
		sqlpk.append(" where nvl(ap.dr,0) = 0 ");
		sqlpk.append(" and  "+SQLUtil.buildSqlForIn(" ap.pk_payablebill", keys) );
		List<MarketingManageAndAccrualVO> listvo = (List<MarketingManageAndAccrualVO>) getBaseDAO()
				.executeQuery(
						sqlpk.toString(),
						new BeanListProcessor(
								MarketingManageAndAccrualVO.class));
		if(listvo!=null&&listvo.size()>0){
			for(MarketingManageAndAccrualVO vo:listvo){
				infoMap.put(vo.getBillid(), vo);
				outsideMap.put(vo.getOutsideno(), vo.getAccrualTag());
			}	
		}
		return infoMap;
	}
	
	

	private Map<String, MarketingManageAndAccrualVO> getBxInfo(String[] keys) throws DAOException {
		Map<String,MarketingManageAndAccrualVO> infoMap = new HashMap<String,MarketingManageAndAccrualVO>();

		StringBuffer sqlpk = new StringBuffer();
		sqlpk.append(
				" select to_number((case when hz.costmoneyin is not null and hz.costmoneyin <> '~' then hz.costmoneyin else 0 end)) invoiceAmount, ")
				.append("\r\n");
		sqlpk.append(" '' outsideno, ");//外系统单据号
		sqlpk.append(" er.pk_jkbx billid, ");//单据pk
		sqlpk.append(
				" to_number((case when hz.costmoneyin is not null and hz.costmoneyin <> '~' then hz.costmoneyin else 0 end)) as notTakebackInvoiceAmount, ")
				.append("\r\n");
		sqlpk.append(
				" smn.user_name billmaker,er.djbh billno,er.zyx2 contractCode,er.zyx9 contractName,(case when er.zyx8 <> '~' then to_char(er.zyx8)||'%' else '' end) contractRate, ")
				.append("\r\n");
		sqlpk.append(
				" (select name from bd_defdoc where bd_defdoc.pk_defdoc = er.zyx59 and bd_defdoc.dr = 0 ) payTicketTag,er.zyx48 accrualTag ")
				.append("\r\n");
		sqlpk.append(
				" from er_bxzb er left join hzvat_invoice_h hz on hz.def8 = er.zyx16 and hz.dr = 0 left join sm_user smn on smn.cuserid = er.operator ")
				.append("\r\n");
		sqlpk.append(" where er.dr = 0 " );
//		sqlpk.append(" and   er.pk_jkbx  = '" + billmakerb + "' ")
		sqlpk.append(" and  "+SQLUtil.buildSqlForIn(" er.pk_jkbx", keys ));
		sqlpk.append("  ").append("\r\n");
		List<MarketingManageAndAccrualVO> listvo = (List<MarketingManageAndAccrualVO>) getBaseDAO()
				.executeQuery(
						sqlpk.toString(),
						new BeanListProcessor(
								MarketingManageAndAccrualVO.class));
		if(listvo!=null&&listvo.size()>0){
			for(MarketingManageAndAccrualVO vo:listvo){
				infoMap.put(vo.getBillid(), vo);
			}	
		}
		return infoMap;
	}

	/**
	 * 通过客商辅助核算编码获取查询其名称的sql
	 * 
	 * @param merchantAssistantAccountingName
	 * @return
	 * @throws BusinessException 
	 */
	public Map<String, String> getVoucherSqlMap() throws BusinessException {
		StringBuffer sqlche = new StringBuffer();
		sqlche.append(" select distinct v2.name,v1.assid freevalueid from gl_detail ")
				.append("\r\n");
		sqlche.append(
				" inner join (select assid, substr(typevalue, 0, 20) asstype,trim(substr(typevalue, 21, length(typevalue))) assvalue ")
				.append("\r\n");
		sqlche.append(
				" from (select f.freevalueid assid, f.typevalue1,f.typevalue2,f.typevalue3,f.typevalue4,f.typevalue5,f.typevalue6,f.typevalue7,f.typevalue8,f.typevalue9 ")
				.append("\r\n");
		sqlche.append(
				" from gl_freevalue f ) T UNPIVOT(typevalue FOR gl_freevalue IN(typevalue1,typevalue2,typevalue3,typevalue4,typevalue5,typevalue6,typevalue7,typevalue8,typevalue9)) P ")
				.append("\r\n");
		sqlche.append(
				" where typevalue <> 'NN/A' and exists (select 1 from bd_accassitem where code = '0004' and substr(typevalue, 0, 20) =  bd_accassitem.pk_accassitem)) v1 ")
				.append("\r\n");
		sqlche.append(
				" on v1.assid = gl_detail.assid inner join bd_cust_supplier v2  on v2.pk_cust_sup = v1.assvalue ")
				.append("\r\n");
		sqlche.append(" ").append("\r\n");
		
		Map<String,String> map = new HashMap<String,String>();
		List<Map<String, String>> userInfo = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sqlche.toString(), new MapListProcessor());
		//将id和客商辅助核算名称做成键值对
		for (Map<String, String> map2 : userInfo) {
			map.put(map2.get("freevalueid"), map2.get("name"));
		}
		
		return map;
	}

	/**
	 * 构造查询sql
	 * 
	 * @param arrpk_orgs
	 * @return
	 */
	public StringBuffer QuerySQL(ArrayList<String> arrpk_orgs) {
		StringBuffer sql = new StringBuffer();
		// 报销单
//		sql.append(" select distinct p.* from (");
		sql.append("   select fip.src_billtype billtype, fip.src_relationid as billid, org.pk_fatherorg region,org.name outAccountCompany,org.pk_org pk_org,glh.year years,glh.period months,substr(glh.prepareddate, 9, 2) dates, ");
		sql.append("   bd.dispname courseTitle,gl.assid merchantAssistantAccountingName,glh.num certificateNumber,to_number(case when gl.debitamount is not null then gl.debitamount else 0 end) payAmount,gl.explanation certificateDigest,fip.src_relationid as billmaker,sm.user_name certificateMaker ");
		// sql.append("   gl.debitamount payAmount,to_number((case when hz.costmoneyin is not null and hz.costmoneyin <> '~'  then hz.costmoneyin else 0 end)) invoiceAmount, ");
		// sql.append("   to_number(gl.debitamount) - (case when to_number(hz.costmoneyin) is not null and to_number(hz.costmoneyin) <> '~'  then to_number(hz.costmoneyin) else 0 end) as notTakebackInvoiceAmount, ");
		// sql.append("   sm.user_name certificateMaker,smn.user_name billmaker,er.djbh billno,er.zyx2 contractCode,er.zyx9 contractName,to_char(er.zyx8) contractRate, ");
		// sql.append("   ( ");
		// sql.append("     case ");
		// sql.append("       gl.accountcode ");
		// sql.append("       when '112311' then '管理费用' ");
		// sql.append("       when '11230301' then '营销费用' ");
		// sql.append("       when '11230302' then '营销费用' ");
		// sql.append("     end ");
		// sql.append("   ) costType, ");
		// sql.append("   (select name from bd_defdoc where bd_defdoc.pk_defdoc = er.zyx59 and bd_defdoc.dr = 0) payTicketTag, ");
		// sql.append("   er.zyx48 accrualTag ");
		sql.append(" from ");
		sql.append("   gl_voucher glh ");
		sql.append("   left join gl_detail gl on glh.pk_voucher = gl.pk_voucher ");
		sql.append("   and gl.dr = 0 ");
		sql.append("   left join org_orgs org on gl.pk_org = org.pk_org ");
		sql.append("   and org.dr = 0 ");
		sql.append("   left join bd_accasoa bd on bd.pk_accasoa = gl.pk_accasoa ");
		sql.append("   and bd.dr = 0 ");
		sql.append("   left join org_accountingbook acc on acc.pk_accountingbook = gl.pk_accountingbook ");
		sql.append("   and acc.dr = 0 ");
		sql.append("   left join sm_user sm on sm.cuserid = glh.pk_prepared ");
		sql.append("   and sm.dr = 0 ");
		sql.append("   left join fip_relation fip on gl.pk_voucher = fip.des_relationid ");
		sql.append("   and fip.dr = 0 ");
		// sql.append("   left join er_bxzb er on fip.src_relationid like er.pk_jkbx||'%'  ");
		// sql.append("   and er.dr = 0 ");
		// sql.append("   left join hzvat_invoice_h hz on hz.def8 = er.zyx16 ");
		// sql.append("   and hz.dr = 0 left  join  sm_user smn on  smn.cuserid = er.operator ");
		sql.append(" where ");
		sql.append("   gl.accountcode in ('112311', '11230301', '11230302') ");
		sql.append("   and glh.period <> '00' ");
		sql.append("   and gl.debitamount <> 0 and gl.discardflagv <> 'Y' and gl.voucherkindv <> 255 and gl.pk_managerv = 'N/A' and gl.tempsaveflag <> 'Y' and gl.voucherkindv <> 5 ");
		sql.append("   and glh.dr = 0 ");
		sql.append("    and fip.src_billtype <> '~'  ");

		String[] kjperiod = queryValueMap.get("yeare").split(",");
		Set<String> kjyears = new HashSet<String>();
		List<String> kjmonths = new ArrayList<String>();
		// 会计期间
		if (queryValueMap.get("yeare") != null) {
			for (String yemonth : kjperiod) {
				String[] years = yemonth.split("-");
				kjyears.add(years[0]);
				kjmonths.add(years[1]);
			}
		}
		// 年
		if (kjyears != null && kjyears.size() > 0) {
			sql.append(
					" and glh.year in ("
							+ kjyears.toString().replace("[", "")
									.replace("]", "") + ")").append("\r\n");
		}
		// 月
		if (kjmonths != null && kjmonths.size() > 0) {
			sql.append(
					" and glh.period in ("
							+ kjmonths.toString().replace("[", "")
									.replace("]", "") + ")").append("\r\n");
		}
		// 科目
		if (queryValueMap.get("courseTitle") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("gl.pk_accasoa",
							queryValueMap.get("courseTitle").split(",")));
		}
		// 辅助核算
		if (queryValueMap.get("merchantAssistantAccountingName") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("gl.pk_accountingbook",
							queryValueMap
									.get("merchantAssistantAccountingName")
									.split(",")));
		}
		// 区域公司,查询条件区域公司遍历找到其下属公司再作为查询条件
		if (arrpk_orgs != null && arrpk_orgs.size() > 0) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("org.pk_org",
							arrpk_orgs.toArray(new String[arrpk_orgs.size()])));
		}

//		sql.append(" union all");
//		sql.append(" select org.pk_fatherorg region,org.name outAccountCompany,org.pk_org pk_org,glh.year years,glh.period months,substr(glh.prepareddate, 9, 2) dates, ");
//		sql.append("   bd.dispname courseTitle,gl.assid merchantAssistantAccountingName,glh.num certificateNumber,to_number(case when gl.debitamount is not null then gl.debitamount else 0 end) payAmount,gl.explanation certificateDigest,fip.src_relationid as billmaker, ");
//		// sql.append("   to_number((case when hz.costmoneyin is not null then  hz.costmoneyin else 0 end)) invoiceAmount, ");
//		// sql.append("   to_number(gl.debitamount) - to_number((case when hz.costmoneyin is not null then  hz.costmoneyin else 0 end)) as notTakebackInvoiceAmount, ");
//		sql.append("   (select sm.user_name from sm_user sm where sm.cuserid = glh.pk_prepared and sm.dr = 0) as certificateMaker ");
//		// sql.append("   (select smn.user_name from sm_user smn where smn.cuserid = ap.billmaker and smn.dr = 0) as billmaker, ");
//		// sql.append("   ap.billno billno,ap.def5 contractCode,ap.def6 contractName,to_char(fct_ap.rate) as contractRate, ");
//		// sql.append("   ( ");
//		// sql.append("     case ");
//		// sql.append("       gl.accountcode ");
//		// sql.append("       when '112311' then '管理费用' ");
//		// sql.append("       when '11230301' then '营销费用' ");
//		// sql.append("       when '11230302' then '营销费用' ");
//		// sql.append("     end ");
//		// sql.append("   ) costType, ");
//		// sql.append("   ap.def55 payTicketTag,ap.def50 accrualTag ");
//		sql.append(" from ");
//		sql.append("   gl_voucher glh ");
//		sql.append("   left join gl_detail gl on glh.pk_voucher = gl.pk_voucher ");
//		sql.append("   and gl.dr = 0 ");
//		sql.append("   left join org_orgs org on gl.pk_org = org.pk_org ");
//		sql.append("   and org.dr = 0 ");
//		sql.append("   left join bd_accasoa bd on bd.pk_accasoa = gl.pk_accasoa ");
//		sql.append("   and bd.dr = 0 ");
//		sql.append("   left join org_accountingbook acc on acc.pk_accountingbook = gl.pk_accountingbook ");
//		sql.append("   and acc.dr = 0 ");
//		sql.append("   left join sm_user sm on sm.cuserid = glh.pk_prepared ");
//		sql.append("   and sm.dr = 0 ");
//		sql.append("   left join fip_relation fip on glh.pk_voucher = fip.des_relationid ");
//		sql.append("   and fip.dr = 0 ");
//		// sql.append("   left join ap_payablebill ap on ap.pk_payablebill = fip.src_relationid ");
//		// sql.append("   and ap.dr = 0 ");
//		// sql.append("   left join hzvat_invoice_h hz on hz.def8 = ap.def3 ");
//		// sql.append("   and hz.dr = 0 left join fct_ap on fct_ap.vbillcode  = ap.def5 and ctname = ap.def6 and blatest = 'Y' ");
//		sql.append(" where ");
//		sql.append("   gl.accountcode in ('112311', '11230301', '11230302') ");
//		sql.append("   and glh.period <> '00' ");
//		sql.append("   and gl.debitamount <> 0 and gl.discardflagv <> 'Y' and gl.voucherkindv <> 255 and gl.pk_managerv = 'N/A' and gl.tempsaveflag <> 'Y' and gl.voucherkindv <> 5 ");
//		sql.append("   and glh.dr = 0 ");

//		// 年
//		if (kjyears != null && kjyears.size() > 0) {
//			sql.append(
//					" and glh.year in ("
//							+ kjyears.toString().replace("[", "")
//									.replace("]", "") + ")").append("\r\n");
//		}
//		// 月
//		if (kjmonths != null && kjmonths.size() > 0) {
//			sql.append(
//					" and glh.period in ("
//							+ kjmonths.toString().replace("[", "")
//									.replace("]", "") + ")").append("\r\n");
//		}
//		// 科目
//		if (queryValueMap.get("courseTitle") != null) {
//			sql.append(" and  "
//					+ SQLUtil.buildSqlForIn("gl.pk_accasoa",
//							queryValueMap.get("courseTitle").split(",")));
//		}
//		// 辅助核算
//		if (queryValueMap.get("merchantAssistantAccountingName") != null) {
//			sql.append(" and  "
//					+ SQLUtil.buildSqlForIn("gl.pk_accountingbook",
//							queryValueMap
//									.get("merchantAssistantAccountingName")
//									.split(",")));
//		}
//		// 区域公司,查询条件区域公司遍历找到其下属公司再作为查询条件
//		if (arrpk_orgs != null && arrpk_orgs.size() > 0) {
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn("org.pk_org",
//							arrpk_orgs.toArray(new String[arrpk_orgs.size()])));
//		}
//		sql.append("   ) p order by p.months,p.dates ");
//		System.out.println(sql);
		return sql;
	}

	/**
	 * 根据查询条件地区过滤结果集
	 * 
	 * @param regionMap
	 */
	@SuppressWarnings("unchecked")
	public List<MarketingManageAndAccrualVO> getVOByRegion(
			List<MarketingManageAndAccrualVO> resultVOList,
			Map<String, String> regionMap) throws BusinessException {

		// 存放区域pk_region的数组
		String[] pk_regionArr = null;
		// 存放区域pk_region对应的name
		Map<String, String> nameMap = new HashMap<String, String>();

		// 查询出输入条件的区域PK与区域名称name对应关系
		if (queryValueMap.get("region") != null) {
			pk_regionArr = queryValueMap.get("region").split(",");
			StringBuilder sqlstr = new StringBuilder();
			sqlstr.append("select name,pk_org from org_orgs where org_orgs.dr = 0");
			sqlstr.append(" and "
					+ SQLUtil.buildSqlForIn(" org_orgs.pk_org ", pk_regionArr));
			List<Map<String, String>> value = (List<Map<String, String>>) getBaseDAO()
					.executeQuery(sqlstr.toString(), new MapListProcessor());
			for (Map<String, String> map : value) {
				nameMap.put(map.get("pk_org"), map.get("name"));
			}
		}

		Iterator<MarketingManageAndAccrualVO> iterator = resultVOList
				.iterator();
		// 初始化序号为0
		int sequeNumGlobal = 0;
		while (iterator.hasNext()) {
			MarketingManageAndAccrualVO marketingManageAndAccrualVO = iterator
					.next();
			// 从VO中获取公司的pk_org
			String pk_org = marketingManageAndAccrualVO.getPk_org();
			// 根据公司的pk_org从regionMap中获取区域的pk_region
			String pk_region = regionMap.get(pk_org);
			if ("~".equals(pk_region)) {
				iterator.remove();
			} else {
				++sequeNumGlobal;
				marketingManageAndAccrualVO.setSortnumglobal(new UFDouble(
						sequeNumGlobal));
				// 如果查询条件中area（区域）为null的话，nameMap是没有值的，所以这里new一个map来存放pk_region（区域）对应name（名称）
				Map<String, String> region_nameMap = new HashMap<String, String>();
				if (!"~".equals(pk_region)) {
					// 当nameMap有值时则查询条件中指定了区域PK，前面已经查询出区域pk对应的name名称
					if (nameMap.size() > 0) {
						// 根据区域的pk_region从nameMap中获取区域的name
						String name = nameMap.get(pk_region);
						marketingManageAndAccrualVO.setRegion(name);
					} else {
						/**
						 * 当nameMap为空时则说明查询条件中没有区域pk，
						 * 需要从查询结果costLedgerVOs中查找相应的区域pk
						 * 根据区域pk来查询区域名称name，查出来后set进costLedgerVO
						 * ，并且存入region_nameMap中，
						 * 避免重复查询区域名称（多个公司的pk_org可能对应一个区域pk）
						 */
						if (region_nameMap.get(pk_region) != null) {
							marketingManageAndAccrualVO
									.setRegion(region_nameMap.get(pk_region));
						} else {
							String sql_region = "select name,pk_org from org_orgs where org_orgs.dr = 0 and pk_org = '"
									+ pk_region + "'";
							Map<String, String> value = (Map<String, String>) getBaseDAO()
									.executeQuery(sql_region,
											new MapProcessor());

							marketingManageAndAccrualVO
									.setRegion(value == null ? null : value
											.get("name"));
							region_nameMap.put(
									value == null ? null : value.get("pk_org"),
									value == null ? null : value.get("name"));
						}
					}
				} else {
					marketingManageAndAccrualVO.setRegion("");
				}
			}
		}
		return resultVOList;
	}

//	/**
//	 * 查询 营销、管理费用后补票及权责发生制明细数据
//	 * 
//	 * @return
//	 */
//	private List<MarketingManageAndAccrualVO> getMainMarketingManageAndAccrualVO()
//			throws BusinessException {
//		List<MarketingManageAndAccrualVO> marketMainList = getMarketingManageAndAccrualVOList();
//		Set<String> keySet = new HashSet<String>();
//		List<InvLedgerVO> integrationList = new ArrayList<InvLedgerVO>();
//		// 应付单信息map
//		Map<String, List<Map<String, Object>>> payableMap = null;
//		// 网报工单信息map
//		Map<String, List<Map<String, Object>>> actualMap = null;
//		// 报销单信息map
//		Map<String, List<Map<String, Object>>> bxzbMap = null;
//		// 付款单信息map
//		Map<String, List<Map<String, Object>>> paybillMap = null;
//		if (marketMainList.size() > 0) {
//			for (MarketingManageAndAccrualVO vo : invMainList) {
//				keySet.add(vo.getImagecode());
//			}
//		}
//		return null;
//	}
//

private List<MarketingManageAndAccrualVO> getPayableBillList(
			ArrayList<String> arrpk_orgs) {
	
	StringBuffer sql = new StringBuffer();
	
	sql.append(" select org.pk_fatherorg region,org.name outAccountCompany,org.pk_org pk_org,glh.year years,glh.period months,substr(glh.prepareddate, 9, 2) dates, ");
	sql.append("   bd.dispname courseTitle,gl.assid merchantAssistantAccountingName,glh.num certificateNumber,to_number(case when gl.debitamount is not null then gl.debitamount else 0 end) payAmount,gl.explanation certificateDigest,fip.src_relationid as billmaker, ");
	// sql.append("   to_number((case when hz.costmoneyin is not null then  hz.costmoneyin else 0 end)) invoiceAmount, ");
	// sql.append("   to_number(gl.debitamount) - to_number((case when hz.costmoneyin is not null then  hz.costmoneyin else 0 end)) as notTakebackInvoiceAmount, ");
	sql.append("   (select sm.user_name from sm_user sm where sm.cuserid = glh.pk_prepared and sm.dr = 0) as certificateMaker ");
	// sql.append("   (select smn.user_name from sm_user smn where smn.cuserid = ap.billmaker and smn.dr = 0) as billmaker, ");
	// sql.append("   ap.billno billno,ap.def5 contractCode,ap.def6 contractName,to_char(fct_ap.rate) as contractRate, ");
	// sql.append("   ( ");
	// sql.append("     case ");
	// sql.append("       gl.accountcode ");
	// sql.append("       when '112311' then '管理费用' ");
	// sql.append("       when '11230301' then '营销费用' ");
	// sql.append("       when '11230302' then '营销费用' ");
	// sql.append("     end ");
	// sql.append("   ) costType, ");
	// sql.append("   ap.def55 payTicketTag,ap.def50 accrualTag ");
	sql.append(" from ");
	sql.append("   gl_voucher glh ");
	sql.append("   left join gl_detail gl on glh.pk_voucher = gl.pk_voucher ");
	sql.append("   and gl.dr = 0 ");
	sql.append("   left join org_orgs org on gl.pk_org = org.pk_org ");
	sql.append("   and org.dr = 0 ");
	sql.append("   left join bd_accasoa bd on bd.pk_accasoa = gl.pk_accasoa ");
	sql.append("   and bd.dr = 0 ");
	sql.append("   left join org_accountingbook acc on acc.pk_accountingbook = gl.pk_accountingbook ");
	sql.append("   and acc.dr = 0 ");
	sql.append("   left join sm_user sm on sm.cuserid = glh.pk_prepared ");
	sql.append("   and sm.dr = 0 ");
	sql.append("   left join fip_relation fip on glh.pk_voucher = fip.des_relationid ");
	sql.append("   and fip.dr = 0 ");
	// sql.append("   left join ap_payablebill ap on ap.pk_payablebill = fip.src_relationid ");
	// sql.append("   and ap.dr = 0 ");
	// sql.append("   left join hzvat_invoice_h hz on hz.def8 = ap.def3 ");
	// sql.append("   and hz.dr = 0 left join fct_ap on fct_ap.vbillcode  = ap.def5 and ctname = ap.def6 and blatest = 'Y' ");
	sql.append(" where ");
	sql.append("   gl.accountcode in ('112311', '11230301', '11230302') ");
	sql.append("   and glh.period <> '00' ");
	sql.append("   and gl.debitamount <> 0 and gl.discardflagv <> 'Y' and gl.voucherkindv <> 255 and gl.pk_managerv = 'N/A' and gl.tempsaveflag <> 'Y' and gl.voucherkindv <> 5 ");
	sql.append("   and glh.dr = 0 ");

	
//	String[] kjperiod = queryValueMap.get("yeare").split(",");
	Set<String> kjyears = new HashSet<String>();
	List<String> kjmonths = new ArrayList<String>();
	// 年
	if (kjyears != null && kjyears.size() > 0) {
		sql.append(
				" and glh.year in ("
						+ kjyears.toString().replace("[", "")
								.replace("]", "") + ")").append("\r\n");
	}
	// 月
	if (kjmonths != null && kjmonths.size() > 0) {
		sql.append(
				" and glh.period in ("
						+ kjmonths.toString().replace("[", "")
								.replace("]", "") + ")").append("\r\n");
	}
	// 科目
	if (queryValueMap.get("courseTitle") != null) {
		sql.append(" and  "
				+ SQLUtil.buildSqlForIn("gl.pk_accasoa",
						queryValueMap.get("courseTitle").split(",")));
	}
	// 辅助核算
	if (queryValueMap.get("merchantAssistantAccountingName") != null) {
		sql.append(" and  "
				+ SQLUtil.buildSqlForIn("gl.pk_accountingbook",
						queryValueMap
								.get("merchantAssistantAccountingName")
								.split(",")));
	}
	// 区域公司,查询条件区域公司遍历找到其下属公司再作为查询条件
	if (arrpk_orgs != null && arrpk_orgs.size() > 0) {
		sql.append(" and "
				+ SQLUtil.buildSqlForIn("org.pk_org",
						arrpk_orgs.toArray(new String[arrpk_orgs.size()])));
	}
		return null;
	}

private List<MarketingManageAndAccrualVO> getBXBillList(ArrayList<String> arrpk_orgs) throws BusinessException {
	StringBuffer sql = new StringBuffer();
	// 报销单

	String[] kjperiod = queryValueMap.get("yeare").split(",");
	Set<String> kjyears = new HashSet<String>();
	List<String> kjmonths = new ArrayList<String>();
	// 会计期间
	if (queryValueMap.get("yeare") != null) {
		for (String yemonth : kjperiod) {
			String[] years = yemonth.split("-");
			kjyears.add(years[0]);
			kjmonths.add(years[1]);
		}
	}
	// 年
	if (kjyears != null && kjyears.size() > 0) {
		sql.append(
				" and glh.year in ("
						+ kjyears.toString().replace("[", "")
								.replace("]", "") + ")").append("\r\n");
	}
	// 月
	if (kjmonths != null && kjmonths.size() > 0) {
		sql.append(
				" and glh.period in ("
						+ kjmonths.toString().replace("[", "")
								.replace("]", "") + ")").append("\r\n");
	}
	// 科目
	if (queryValueMap.get("courseTitle") != null) {
		sql.append(" and  "
				+ SQLUtil.buildSqlForIn("gl.pk_accasoa",
						queryValueMap.get("courseTitle").split(",")));
	}
	// 辅助核算
	if (queryValueMap.get("merchantAssistantAccountingName") != null) {
		sql.append(" and  "
				+ SQLUtil.buildSqlForIn("gl.pk_accountingbook",
						queryValueMap
								.get("merchantAssistantAccountingName")
								.split(",")));
	}
	// 区域公司,查询条件区域公司遍历找到其下属公司再作为查询条件
	if (arrpk_orgs != null && arrpk_orgs.size() > 0) {
		sql.append(" and "
				+ SQLUtil.buildSqlForIn("org.pk_org",
						arrpk_orgs.toArray(new String[arrpk_orgs.size()])));
	}
	List<MarketingManageAndAccrualVO> list = (List<MarketingManageAndAccrualVO>) getBaseDAO().executeQuery(sql.toString(), new BeanListProcessor(MarketingManageAndAccrualVO.class));
	return list;
}

}
