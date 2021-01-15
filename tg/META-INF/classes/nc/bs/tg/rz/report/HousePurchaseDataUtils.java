package nc.bs.tg.rz.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.HousePurchaseVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

public class HousePurchaseDataUtils extends ReportUtils{
	private static HousePurchaseDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// ��ѯ�����µ�ֵ
	Map<String, String> queryWhereMap = new HashMap<String, String>();// ��ѯ�����µ�sql
	public static HousePurchaseDataUtils getUtils() throws BusinessException {
		if(utils==null){
			utils = new HousePurchaseDataUtils();
		}
		return utils;
	}
	
	public HousePurchaseDataUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance()
				.getPropertiesAry(new HousePurchaseVO()));
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
			List<HousePurchaseVO> list = getHousePurchaseVOs();
			if(list != null && list.size() > 0){
				cmpreportresults = transReportResult(list);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return generateDateset(cmpreportresults, getKeys());
	}
	
	private List<HousePurchaseVO> getHousePurchaseVOs() throws BusinessException {
		String citycorp = queryValueMap.get("���й�˾");
		List<String> cityCorpList = this.getCityCorpList(citycorp);
		String projectname = queryValueMap.get("��Ŀ����");
		List<String> projectNameList = this.getProjectNameList(projectname);
		String bigprojectname = queryValueMap.get("����Ŀ����");
		String buildingname = queryValueMap.get("¥������");
		String unit = queryValueMap.get("��Ԫ");
		String room = queryValueMap.get("����");
		StringBuilder sql = new StringBuilder();
		sql.append("select ���й�˾ 		as 	citycorp, ");
		sql.append("	��Ŀ����			as	projectname, ");
		sql.append("	����Ŀ����		as  bigprojectname, ");
		sql.append("	¥������			as  buildingname, ");
		sql.append("	��Ԫ				as  unit, ");
		sql.append("	����				as  room, ");
		sql.append("	��Ʒ����			as 	producttype, ");
		sql.append("	�������			as  structurearea, ");
		sql.append("	�������			as  insidespace, ");
		sql.append("	�ͻ�����			as  customername,");
		sql.append("	״̬				as  status, ");
		sql.append("	to_char(�Ϲ�����,'YYYY-MM-DD')			as  subscriptiondate, ");
		sql.append("	to_char(ǩԼ����,'YYYY-MM-DD')			as	contractdate, ");
		sql.append("	���ҽ��			as  installmentamount, ");
		sql.append("	������			as  reserveamount, ");
		sql.append("	���ʽ			as  paymentmethod, ");
		sql.append("	��ͬ�ܼ�			as  contractamount,");
		sql.append("	�׸����			as  firstpayamount,");
		sql.append("	�׸�����			as  firstpayscale,");
		sql.append("	Ӧ�ս��			as  receivableamount, ");
		sql.append("	���ս��			as  receivedamount, ");
		sql.append("	���				as  remainingamount,");
		sql.append("	��������			as  installmentbank, ");
		sql.append("	��������			as  installmentlimit,");
		sql.append("	����״̬			as  installmentstatus, ");
		sql.append("	������״̬		as  accumulationstatus, ");
		sql.append("	to_char(�������,'YYYY-MM-DD')			as  completedate, ");
		sql.append("	�Ƿ�ſ�			as  isloan,");
		sql.append("	��������			as  housetype, ");
		sql.append("	��ͬ���			as  contractcode, ");
		sql.append("	��ͬ������		as  contractrecord, ");
		sql.append("	��ͬ�Ǽ���� 		as  contractregiststatus,");
		sql.append("	���֤����		as	idcard, ");
		sql.append("	to_char(ȡ��Ԥ��֤����,'YYYY-MM-DD')	as  presellcarddate,");
		sql.append("	to_char(��ǩ����,'YYYY-MM-DD')			as  netsigndate");
		sql.append(" 	from sdc.v_sales_capital_fee@link_sale");
		sql.append(" where "
				+ SQLUtil.buildSqlForIn(
						" ���й�˾ ",
						cityCorpList.toArray(new String[0])));
		sql.append(" and "
				+ SQLUtil.buildSqlForIn(
						" ��Ŀ���� ",
						projectNameList.toArray(new String[0])));
		if(bigprojectname != null){
			sql.append(" and ����Ŀ���� = '"+bigprojectname+"'");
		}
		if(buildingname != null){
			sql.append(" and ¥������ = '"+buildingname+"'");
		}
		if(unit != null){
			sql.append(" and ��Ԫ = '"+unit+"'");
		}
		if(room != null){
			sql.append(" and ���� = '"+room+"'");
		}
		List<HousePurchaseVO> vos = (List<HousePurchaseVO>)getBaseDAO().executeQuery(sql.toString(),new BeanListProcessor(HousePurchaseVO.class));
		return vos;
	}

	private void initQuery(ConditionVO[] condVOs) {
		queryValueMap.clear();
		queryWhereMap.clear();
		for (ConditionVO condVO : condVOs) {
			if (condVO.getValue() != null && !"".equals(condVO.getValue())) {
				if (condVO.getDataType() == ConditionVO.DATE) {
					String[] dates = condVO.getValue().split("@@");
					queryValueMap.put(condVO.getFieldCode() + "_begin",
							new UFDate(dates[0]).asBegin().toString());
					queryValueMap.put(condVO.getFieldCode() + "_end",
							new UFDate(dates[dates.length - 1]).asEnd()
									.toString());

				} else {
					queryValueMap
							.put(condVO.getFieldCode(),
									condVO.getValue().replace("(", "")
											.replace(")", "").replace("'", ""));
				}
			}
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
		}
	}
	
	private List<String> getCityCorpList(String citycorp) throws BusinessException{
		String[] arr = citycorp.split(",");
		ArrayList<String> list = new ArrayList<>();
		String buildSqlForIn = SQLUtil.buildSqlForIn(" pk_org ", arr);
		String sql = "select name from org_orgs where "+buildSqlForIn;
		List<Map<String,String>> maplist = (List<Map<String,String>>)getBaseDAO().executeQuery(sql, new MapListProcessor());
		for (Map<String, String> map : maplist) {
			if(map.get("name") != null){
				list.add(map.get("name"));
			}
		}
		return list;
	}
	
	private List<String> getProjectNameList(String projectname) throws BusinessException{
		String[] arr = projectname.split(",");
		ArrayList<String> list = new ArrayList<>();
		String buildSqlForIn = SQLUtil.buildSqlForIn(" pk_projectdata ", arr);
		String sql = "select name from tgrz_projectdata  where"+buildSqlForIn;
		List<Map<String,String>> maplist = (List<Map<String,String>>)getBaseDAO().executeQuery(sql, new MapListProcessor());
		for (Map<String, String> map : maplist) {
			if(map.get("name") != null){
				list.add(map.get("name"));
			}
		}
		return list;
	}
}
