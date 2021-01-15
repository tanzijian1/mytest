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
 * ���ڷ������������
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
	 * ���ݴ���ı��������һ�����
	 * @param no ����ı��
	 * @return
	 */
	public static String getRepaymentNo(String no){
		String repaymentNo = null;
		if(no == null){
			//Ϊ�գ����ʼ��һ�����
			repaymentNo = "L001";
		}else{
			/**
			 * ��ȡ���ֲ��֣����ּ�1���Ǳ��룬���λ�������Ͳ�0������ǰ��
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
	 * ���㻹�����ں���Ϣ
	 * @param start ��ʼ��
	 * @param finish ������
	 * @param rate ����
	 * @param money �ܽ��
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
				//���һ�꣬����
				map.put("capital", money.toString());
			}
			list.add(map);
		}
		return list;
	}
	/**
	 * ������Ϣ����
	 * @param start
	 * @param finish
	 * @param money
	 * @param rate
	 * @param rerate ��������
	 * @param redate ������
	 * @param remoney ���۽��
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
					remap.put("remark", "����");
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
					remap.put("remark", "����");
					list.add(remap);
				}else if(redate.compareTo(repaydateuf)==0){
					map.put("capital", remoney.toString());
				}
			}*/
			if(finish.getYear() == (start.getYear()+i+1)){
				//���һ�꣬����
				map.put("capital", money.sub(remoney,2).toString());
			}
			list.add(map);
		}
		return list;
	}
	/**
	 * ת����Ϣ����
	 * @param start
	 * @param finish
	 * @param money
	 * @param rate
	 * @param newrate Ʊ������
	 * @param redate ������
	 * @param remoney ���۽��
	 * @param tudate ת������
	 * @param tumoney ת�۽��
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
		//tudateת������һ����redate��������֮��
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
					remap.put("remark", "����");
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
					remap.put("remark", "ת��");
					list.add(remap);
				}
			}else{
				if(tudate.compareTo(repaydateuf)==0){
					map.put("capital", "-"+tumoney.toString());
				}
				
			}
			
//			if(tudate.compareTo(dateuf)<=0){//��ʼ���ڱ�ת������Ҫ��
//				map.put("money", getInterestMoney(newrate,money.sub(remoney,2).add(tumoney,2)).toString());
//			}else {
//				if(redate.compareTo(dateuf)<=0){//��ʼ���ڱȻ�������Ҫ��
//					map.put("money", getInterestMoney(newrate,money.sub(remoney,2)).toString());
//				}else{
//					//��ʼ���ڱȻ�������Ҫ��
//					map.put("money", getInterestMoney(rate,money).toString());
//					if(redate.compareTo(repaydateuf)<0){
//						Map<String, String> remap = new HashMap<>();
//						remap.put("repaydate", redate.toString());
//						remap.put("capital", remoney.toString());
//						remap.put("money", null);
//						remap.put("remark", "����");
//						list.add(remap);
//					}else if(redate.compareTo(repaydateuf)==0){
//						map.put("capital", remoney.toString());
//					}
//				}
//				//��ʼ���ڱ�ת������Ҫ��
//				if(tudate.compareTo(repaydateuf)<0){
//					Map<String, String> remap = new HashMap<>();
//					remap.put("repaydate", tudate.toString());
//					remap.put("capital", "-"+tumoney.toString());
//					remap.put("money", null);
//					remap.put("remark", "ת��");
//					list.add(remap);
//				}else if(tudate.compareTo(repaydateuf)==0){
//					map.put("capital", "-"+tumoney.toString());
//				}
//			}
			if(finish.getYear() == (start.getYear()+i+1)){
				//���һ�꣬����
				map.put("capital", money.sub(remoney,2).add(tumoney,2).toString());
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * ��������Ϣ
	 * @param rate
	 * @param money
	 * @return
	 */
	private static BigDecimal getInterestMoney(UFDouble rate,UFDouble money){
		//������Ϣ,(��Ϣ��-��ʼ��)*������/һ���������*���
		//(��Ϣ��-��ʼ��)
		BigDecimal rateDecimal = new BigDecimal(rate.toString());
		BigDecimal moneyDecimal = new BigDecimal(money.toString());
		BigDecimal interest = rateDecimal.multiply(moneyDecimal);

		return interest.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * �������ķ���������ȡ������������
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
	 * ���̻���ƻ�
	 * @param e
	 */
	public static void writeRepayPlan(CardPanelEvent e){
		//��ʼ����,��Ϣ��
		UFDate start = (UFDate)e.getBillCardPanel().getHeadItem("def10").getValueObject();
		//�������ڣ�������
		UFDate finish = (UFDate)e.getBillCardPanel().getHeadItem("def12").getValueObject();
		//����
		UFDouble rate = (UFDouble)e.getBillCardPanel().getHeadItem("def16").getValueObject();
		//�����ܹ�ģ
		UFDouble money = (UFDouble)e.getBillCardPanel().getHeadItem("def5").getValueObject();
		//��������
		UFDate redate = (UFDate)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def1");
		//���۽��
		UFDouble remoney = (UFDouble)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def2");
		//ת������
		UFDate tudate = (UFDate)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def6");
		//ת�۽��
		UFDouble tumoney = (UFDouble)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def7");
		//Ʊ������
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
				 * ����������ţ�����ԭ�л���ƻ��ı�ţ�������ۺ�ת�۵Ļ�����
				 */
				String repayNo = null;
				for (Map<String, String> map : interestList) {
					if(!"����".equals(map.get("remark")) && !"ת��".equals(map.get("remark"))){
						repayNo = SingleIssueUtil.getRepaymentNo(repayNo);
						map.put("repayNo", repayNo);
					}
				}
				for (Map<String, String> map : interestList){
					if("����".equals(map.get("remark")) || "ת��".equals(map.get("remark"))){
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
					
					if(!mapPk.isEmpty()){//������ݿ�����ԭ���Ļ���ƻ�����Ҫ��pk�ͱ����������ý�ȥ
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
					 
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(pk_repayplan,row-1,"pk_repayplan");//��������
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(rowNo, row-1, "rowno");//�к�
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(map.get("repayNo"), row-1, "def1");//�������
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(map.get("repaydate"), row-1, "def2");//������
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(map.get("capital"), row-1, "def3");//����
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(map.get("money"), row-1, "def4");//��Ϣ
					BigDecimal capital = map.get("capital") == null?BigDecimal.ZERO:new BigDecimal(map.get("capital"));
					BigDecimal smoney = map.get("money") == null?BigDecimal.ZERO:new BigDecimal(map.get("money"));
					BigDecimal sumMoney = capital.add(smoney);
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(sumMoney.toString(), row-1, "def5");//��Ϣ���
					e.getBillCardPanel().getBillModel("pk_repayplan").setValueAt(ts, row-1, "ts");//����ts
				}
			}
		} catch (BusinessException e1) {
			ExceptionUtils.wrappBusinessException(e1.getMessage());
		}
	}
}
