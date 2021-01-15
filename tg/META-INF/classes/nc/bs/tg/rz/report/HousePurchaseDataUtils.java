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
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql
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
		String citycorp = queryValueMap.get("城市公司");
		List<String> cityCorpList = this.getCityCorpList(citycorp);
		String projectname = queryValueMap.get("项目名称");
		List<String> projectNameList = this.getProjectNameList(projectname);
		String bigprojectname = queryValueMap.get("大项目名称");
		String buildingname = queryValueMap.get("楼栋名称");
		String unit = queryValueMap.get("单元");
		String room = queryValueMap.get("房间");
		StringBuilder sql = new StringBuilder();
		sql.append("select 城市公司 		as 	citycorp, ");
		sql.append("	项目名称			as	projectname, ");
		sql.append("	大项目名称		as  bigprojectname, ");
		sql.append("	楼栋名称			as  buildingname, ");
		sql.append("	单元				as  unit, ");
		sql.append("	房间				as  room, ");
		sql.append("	产品类型			as 	producttype, ");
		sql.append("	建筑面积			as  structurearea, ");
		sql.append("	套内面积			as  insidespace, ");
		sql.append("	客户名称			as  customername,");
		sql.append("	状态				as  status, ");
		sql.append("	to_char(认购日期,'YYYY-MM-DD')			as  subscriptiondate, ");
		sql.append("	to_char(签约日期,'YYYY-MM-DD')			as	contractdate, ");
		sql.append("	按揭金额			as  installmentamount, ");
		sql.append("	公积金			as  reserveamount, ");
		sql.append("	付款方式			as  paymentmethod, ");
		sql.append("	合同总价			as  contractamount,");
		sql.append("	首付金额			as  firstpayamount,");
		sql.append("	首付比例			as  firstpayscale,");
		sql.append("	应收金额			as  receivableamount, ");
		sql.append("	已收金额			as  receivedamount, ");
		sql.append("	余额				as  remainingamount,");
		sql.append("	按揭银行			as  installmentbank, ");
		sql.append("	按揭年限			as  installmentlimit,");
		sql.append("	按揭状态			as  installmentstatus, ");
		sql.append("	公积金状态		as  accumulationstatus, ");
		sql.append("	to_char(完成日期,'YYYY-MM-DD')			as  completedate, ");
		sql.append("	是否放款			as  isloan,");
		sql.append("	购房类型			as  housetype, ");
		sql.append("	合同编号			as  contractcode, ");
		sql.append("	合同备案号		as  contractrecord, ");
		sql.append("	合同登记情况 		as  contractregiststatus,");
		sql.append("	身份证号码		as	idcard, ");
		sql.append("	to_char(取得预售证日期,'YYYY-MM-DD')	as  presellcarddate,");
		sql.append("	to_char(网签日期,'YYYY-MM-DD')			as  netsigndate");
		sql.append(" 	from sdc.v_sales_capital_fee@link_sale");
		sql.append(" where "
				+ SQLUtil.buildSqlForIn(
						" 城市公司 ",
						cityCorpList.toArray(new String[0])));
		sql.append(" and "
				+ SQLUtil.buildSqlForIn(
						" 项目名称 ",
						projectNameList.toArray(new String[0])));
		if(bigprojectname != null){
			sql.append(" and 大项目名称 = '"+bigprojectname+"'");
		}
		if(buildingname != null){
			sql.append(" and 楼栋名称 = '"+buildingname+"'");
		}
		if(unit != null){
			sql.append(" and 单元 = '"+unit+"'");
		}
		if(room != null){
			sql.append(" and 房间 = '"+room+"'");
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
