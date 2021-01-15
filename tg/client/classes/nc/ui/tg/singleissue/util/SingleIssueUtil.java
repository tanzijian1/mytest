package nc.ui.tg.singleissue.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.generator.IdGenerator;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pubapp.uif2app.event.card.CardPanelEvent;
import nc.util.SdfnUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;


/**
 * 单期发行情况工具类
 * @author wenjie
 *
 */
public class SingleIssueUtil {
	private static IUAPQueryBS query = null;

	public static IUAPQueryBS getQuery() {
		if (query == null) {
			query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return query;
	}
	/**
	 * 根据传入的编号生成下一个编号
	 * @param no 传入的编号
	 * @return
	 */
	public static String getRepaymentNo(String no){
		String repaymentNo = null;
		if(no == null){
			//为空，则初始化一个编号
			repaymentNo = "L001";
		}else{
			/**
			 * 截取数字部分，数字加1就是编码，如果位数不够就补0在数字前面
			 */
			String num = no.substring(1);
			int intValue = Integer.valueOf(num).intValue();
			intValue = intValue+1;
			String valueOf = String.valueOf(intValue);
			int bit = 3-valueOf.length();
			for (int i = 0; i < bit; i++) {
				valueOf = "0"+valueOf;
			}
			repaymentNo = "L"+valueOf;
		}
		return repaymentNo;
	}

	/**
	 * 计算还款日期和利息
	 * @param start 起始日
	 * @param finish 结束日
	 * @param rate 利率
	 * @param money 总金额
	 * @return
	 */
	public static List<Map<String,String>> getInterest(UFDate start,UFDate finish,UFDouble money,UFDouble rate){
		List<Map<String,String>> list = new ArrayList<>();
		if(start==null)return list ;
		if(finish==null)return list;
		if(money==null || money.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		if(rate==null || rate.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		start = start.asBegin();
		finish = finish.asBegin();
		int limit = finish.getYear()-start.getYear();
		for (int i = 0; i < limit; i++) {
			String date = (start.getYear()+i)+"-"+start.getMonth()+"-"+start.getDay();
			String repaydate = (start.getYear()+i+1)+"-"+start.getMonth()+"-"+start.getDay();
			Map<String, String> map = new HashMap<>();
			map.put("start", new UFDate(date).toString());
			map.put("repaydate",new UFDate(repaydate).toString());
			map.put("money", getInterestMoney(rate,money).toString());
			if(finish.getYear() == (start.getYear()+i+1)){
				//最后一年，还本
				map.put("capital", money.toString());
			}
			list.add(map);
		}
		return list;
	}
	/**
	 * 回售利息计算
	 * @param start
	 * @param finish
	 * @param money
	 * @param rate
	 * @param rerate 回售利率
	 * @param redate 回售日
	 * @param remoney 回售金额
	 * @return
	 */
	public static List<Map<String,String>> getInterest(
			UFDate start,UFDate finish,UFDouble money,UFDouble rate,
			UFDouble newrate,UFDate redate,UFDouble remoney){
		List<Map<String,String>> list = new ArrayList<>();
		if(start==null)return list ;
		if(finish==null)return list;
		if(money==null || money.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		if(rate==null || rate.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		if(redate==null)return list;
		if(newrate==null || newrate.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		if(remoney==null || remoney.compareTo(UFDouble.ZERO_DBL)<=0)return list;

		start = start.asBegin();
		finish = finish.asBegin();
		redate = redate.asBegin();
		int limit = finish.getYear()-start.getYear();
		for (int i = 0; i < limit; i++) {
			String date = (start.getYear()+i)+"-"+start.getMonth()+"-"+start.getDay();
			String repaydate = (start.getYear()+i+1)+"-"+start.getMonth()+"-"+start.getDay();
			Map<String, String> map = new HashMap<>();
			UFDate dateuf = new UFDate(date);
			UFDate repaydateuf = new UFDate(repaydate);
			map.put("start", dateuf.toString());
			map.put("repaydate",repaydateuf.toString());
			if(redate.compareTo(repaydateuf)<0){
				map.put("money", getInterestMoney(newrate,money.sub(remoney,2)).toString());
				if(redate.compareTo(dateuf)>0){
					Map<String, String> remap = new HashMap<>();
					remap.put("repaydate", redate.toString());
					remap.put("capital", remoney.toString());
					remap.put("money", null);
					remap.put("remark", "回售");
					list.add(remap);
				}
			}else{
				map.put("money", getInterestMoney(rate,money).toString());
				if(redate.compareTo(repaydateuf)==0){
					map.put("capital", remoney.toString());
				}
			}
			/*if(redate.compareTo(dateuf)<=0){
				map.put("money", getInterestMoney(newrate,money.sub(remoney,2)).toString());
			}else if(redate.compareTo(dateuf)>0){
				map.put("money", getInterestMoney(rate,money).toString());
				if(redate.compareTo(repaydateuf)<0){
					Map<String, String> remap = new HashMap<>();
					remap.put("repaydate", redate.toString());
					remap.put("capital", remoney.toString());
					remap.put("money", null);
					remap.put("remark", "回售");
					list.add(remap);
				}else if(redate.compareTo(repaydateuf)==0){
					map.put("capital", remoney.toString());
				}
			}*/
			if(finish.getYear() == (start.getYear()+i+1)){
				//最后一年，还本
				map.put("capital", money.sub(remoney,2).toString());
			}
			list.add(map);
		}
		return list;
	}
	/**
	 * 转售利息计算
	 * @param start
	 * @param finish
	 * @param money
	 * @param rate
	 * @param newrate 票面利率
	 * @param redate 回售日
	 * @param remoney 回售金额
	 * @param tudate 转售日期
	 * @param tumoney 转售金额
	 * @return
	 */
	public static List<Map<String,String>> getInterest(
			UFDate start,UFDate finish,UFDouble money,UFDouble rate,
			UFDouble newrate,UFDate redate,UFDouble remoney,
			UFDate tudate,UFDouble tumoney){
		List<Map<String,String>> list = new ArrayList<>();
		if(start==null)return list ;
		if(finish==null)return list;
		if(money==null || money.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		if(rate==null || rate.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		if(redate==null)return list;
		if(newrate==null || newrate.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		if(remoney==null || remoney.compareTo(UFDouble.ZERO_DBL)<=0)return list;
		if(tudate==null)return list;
		if(tumoney==null || tumoney.compareTo(UFDouble.ZERO_DBL)<=0)return list;

		tudate = tudate.asBegin();
		start = start.asBegin();
		finish = finish.asBegin();
		redate = redate.asBegin();
		//tudate转售日期一定在redate回售日期之后
		int limit = finish.getYear()-start.getYear();
		for (int i = 0; i < limit; i++) {
			String date = (start.getYear()+i)+"-"+start.getMonth()+"-"+start.getDay();
			String repaydate = (start.getYear()+i+1)+"-"+start.getMonth()+"-"+start.getDay();
			Map<String, String> map = new HashMap<>();
			UFDate dateuf = new UFDate(date);
			UFDate repaydateuf = new UFDate(repaydate);
			map.put("start", dateuf.toString());
			map.put("repaydate",repaydateuf.toString());
			if(redate.compareTo(repaydateuf)<0){
				map.put("money", getInterestMoney(newrate,money.sub(remoney,2)).toString());
				if(redate.compareTo(dateuf)>0){
					Map<String, String> remap = new HashMap<>();
					remap.put("repaydate", redate.toString());
					remap.put("capital", remoney.toString());
					remap.put("money", null);
					remap.put("remark", "回售");
					list.add(remap);
				}
			}else{
				map.put("money", getInterestMoney(rate,money).toString());
				if(redate.compareTo(repaydateuf)==0){
					map.put("capital", remoney.toString());
				}
			}
			if(tudate.compareTo(repaydateuf)<0){
				map.put("money", getInterestMoney(newrate,money.sub(remoney,2).add(tumoney,2)).toString());
				if(tudate.compareTo(dateuf)>0){
					Map<String, String> remap = new HashMap<>();
					remap.put("repaydate", tudate.toString());
					remap.put("capital", "-"+tumoney.toString());
					remap.put("money", null);
					remap.put("remark", "转售");
					list.add(remap);
				}
			}else{
				if(tudate.compareTo(repaydateuf)==0){
					map.put("capital", "-"+tumoney.toString());
				}
				
			}
			
//			if(tudate.compareTo(dateuf)<=0){//开始日期比转售日期要迟
//				map.put("money", getInterestMoney(newrate,money.sub(remoney,2).add(tumoney,2)).toString());
//			}else {
//				if(redate.compareTo(dateuf)<=0){//开始日期比回售日期要迟
//					map.put("money", getInterestMoney(newrate,money.sub(remoney,2)).toString());
//				}else{
//					//开始日期比回售日期要早
//					map.put("money", getInterestMoney(rate,money).toString());
//					if(redate.compareTo(repaydateuf)<0){
//						Map<String, String> remap = new HashMap<>();
//						remap.put("repaydate", redate.toString());
//						remap.put("capital", remoney.toString());
//						remap.put("money", null);
//						remap.put("remark", "回售");
//						list.add(remap);
//					}else if(redate.compareTo(repaydateuf)==0){
//						map.put("capital", remoney.toString());
//					}
//				}
//				//开始日期比转售日期要早
//				if(tudate.compareTo(repaydateuf)<0){
//					Map<String, String> remap = new HashMap<>();
//					remap.put("repaydate", tudate.toString());
//					remap.put("capital", "-"+tumoney.toString());
//					remap.put("money", null);
//					remap.put("remark", "转售");
//					list.add(remap);
//				}else if(tudate.compareTo(repaydateuf)==0){
//					map.put("capital", "-"+tumoney.toString());
//				}
//			}
			if(finish.getYear() == (start.getYear()+i+1)){
				//最后一年，还本
				map.put("capital", money.sub(remoney,2).add(tumoney,2).toString());
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 计算年利息
	 * @param rate
	 * @param money
	 * @return
	 */
	private static BigDecimal getInterestMoney(UFDouble rate,UFDouble money){
		//计算利息,(结息日-起始日)*（利率/一年的天数）*金额
		//(结息日-起始日)
		BigDecimal rateDecimal = new BigDecimal(rate.toString());
		BigDecimal moneyDecimal = new BigDecimal(money.toString());
		BigDecimal interest = rateDecimal.multiply(moneyDecimal);

		return interest.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 根据批文方案主键获取交易类型名称
	 * @param pk_appro
	 * @return
	 * @throws BusinessException
	 */
	public static String getBusiType(String pk_appro) throws BusinessException{
		if(pk_appro != null){
			String sql = "select t.name from tgrz_fintype t left join sdfn_approvalpro s on t.pk_fintype=s.def1 "
					+ " and nvl(s.dr,0) = 0 where s.pk_appro='"+pk_appro+"' and nvl(t.dr,0)=0";
			String busiType = (String)getQuery().executeQuery(sql, new ColumnProcessor());
			return busiType;
		}else{
			return null;
		}
	}

	/**
	 * 还铺还款计划
	 * @param e
	 */
	public static void writeRepayPlan(CardPanelEvent e){
		//开始日期,起息日
		UFDate start = (UFDate)e.getBillCardPanel().getHeadItem("def10").getValueObject();
		//结束日期，到期日
		UFDate finish = (UFDate)e.getBillCardPanel().getHeadItem("def12").getValueObject();
		//利率
		UFDouble rate = (UFDouble)e.getBillCardPanel().getHeadItem("def16").getValueObject();
		//发行总规模
		UFDouble money = (UFDouble)e.getBillCardPanel().getHeadItem("def5").getValueObject();
		//回售日期
		UFDate redate = (UFDate)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def1");
		//回售金额
		UFDouble remoney = (UFDouble)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def2");
		//转售日期
		UFDate tudate = (UFDate)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def6");
		//转售金额
		UFDouble tumoney = (UFDouble)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def7");
		//票面利率
		UFDouble newrate = (UFDouble)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def4");
		try {
			String pk_appro = (String)e.getBillCardPanel().getHeadItem("def1").getValueObject();
			String busiType = SingleIssueUtil.getBusiType(pk_appro);
			if(!SdfnUtil.getABSList().contains(busiType)){
				List<Map<String, String>> interestList = null;
				if(tudate!=null && tumoney!=null && newrate!=null){
					interestList = SingleIssueUtil.getInterest(start, finish, money, rate, newrate, redate, remoney, tudate, tumoney);
				}else if(redate!=null && newrate!=null && newrate!=null){
					interestList = SingleIssueUtil.getInterest(start, finish, money, rate, newrate, redate, remoney);
				}else{
					interestList = SingleIssueUtil.getInterest(start, finish, money, rate);
				}
				e.getBillCardPanel().getBillModel("pk_repayplan").clearBodyData();
				
				Map<String,String> mapPk = new HashMap<>();
				String pk_singleissue = (String)e.getBillCardPanel().getHeadItem("pk_singleissue").getValueObject();
				if(pk_singleissue != null){
					String sql = "select def1,pk_repayplan,ts from sdfn_repaymentplan where nvl(dr,0)=0"
							+ "	and pk_singleissue='"+pk_singleissue+"'";
					List<Map<String,String>> maplist = (List<Map<String,String>>)getQuery().executeQuery(sql, new MapListProcessor());
					if(maplist != null){
						for (Map<String, String> map : maplist) {
							mapPk.put(map.get("def1"), map.get("pk_repayplan")+","+map.get("ts"));
						}
					}
				}
				/**
				 * 计算出还款编号，先算原有还款计划的编号，再算回售和转售的还款编号
				 */
				String repayNo = null;
				for (Map<String, String> map : interestList) {
					if(!"回售".equals(map.get("remark")) && !"转售".equals(map.get("remark"))){
						repayNo = SingleIssueUtil.getRepaymentNo(repayNo);
						map.put("repayNo", repayNo);
					}
				}
				for (Map<String, String> map : interestList){
					if("回售".equals(map.get("remark")) || "转售".equals(map.get("remark"))){
						repayNo = SingleIssueUtil.getRepaymentNo(repayNo);
						map.put("repayNo", repayNo);
					}
				}
				String pk_repayplan = null;
				String ts = null;
				int rowNo = 0;
				for (Map<String, String> map : interestList) {
					rowNo+=10;
					e.getBillCardPanel().getBillModel("pk_repayplan").addLine();
					int row = e.getBillCardPanel().getBillModel("pk_repayplan").getRowCount();
					
					if(!mapPk.isEmpty()){//如果数据库查出有原来的还款计划，需要把pk和编码重新设置进去
						if(mapPk.get(map.get("repayNo")) == null){
							IdGenerator idtor = NCLocator.getInstance().lookup(
						             IdGenerator.class);
						        String pk_filemanage = idtor.generate();
							pk_repayplan = pk_filemanage;
						}else{
							pk_repayplan = mapPk.get(map.get("repayNo")).split(",")[0];
							ts = mapPk.get(map.get("repayNo")).split(",")[1];
						}
					}
					 
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(pk_repayplan,row-1,"pk_repayplan");//设置主键
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(rowNo, row-1, "rowno");//行号
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(map.get("repayNo"), row-1, "def1");//还款编码
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(map.get("repaydate"), row-1, "def2");//还款日
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(map.get("capital"), row-1, "def3");//本金
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(map.get("money"), row-1, "def4");//利息
					BigDecimal capital = map.get("capital") == null?BigDecimal.ZERO:new BigDecimal(map.get("capital"));
					BigDecimal smoney = map.get("money") == null?BigDecimal.ZERO:new BigDecimal(map.get("money"));
					BigDecimal sumMoney = capital.add(smoney);
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(sumMoney.toString(), row-1, "def5");//本息金额
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(ts, row-1, "ts");//设置ts
				}
			}
		} catch (BusinessException e1) {
			ExceptionUtils.wrappBusinessException(e1.getMessage());
		}
	}
}
