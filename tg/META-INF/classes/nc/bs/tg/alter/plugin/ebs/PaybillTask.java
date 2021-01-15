package nc.bs.tg.alter.plugin.ebs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public class PaybillTask implements IBackgroundWorkPlugin{

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		// TODO 自动生成的方法存根
		PreAlertObject pobj=new PreAlertObject();
		try{
		BaseDAO dao=new BaseDAO();
		pobj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
		String sql="select src_itemid,money_de,a.pk_paybill as pk_paybill  from ap_payitem ai left join ap_paybill a on a. pk_paybill=ai.pk_paybill  "
				+ " left  join cmp_settlement t on  t.pk_busibill=a.pk_paybill where nvl(t.dr,0)=0 and nvl(ai.dr,0)=0 and t.settlestatus='5' and  ai.def30!='Y'";//查找结算已完成表体
		List<HashMap<String,Object>> listmap=(List<HashMap<String,Object>>)dao.executeQuery(sql, new MapListProcessor());
		for(HashMap<String, Object>  map:listmap){
			if(map.get("src_itemid")!=null){
			String num=(String)	dao.executeQuery("select def25 from tgfn_payrequest where pk_payreq =(select pk_payreq from tgfn_payreqbus where pk_business_b='"+map.get("src_itemid")+"')", new ColumnProcessor());
			UFDouble ufnum= num==null?UFDouble.ZERO_DBL:new UFDouble(num);//本次请款累计付款金额
			String applysql="select def47 from tgfn_payreqbus where pk_business_b='"+map.get("src_itemid")+"'";
			String money=(String)dao.executeQuery(applysql,new ColumnProcessor() );//付款单对应付款申请单未付金额
			UFDouble nopay=money==null ? UFDouble.ZERO_DBL:new UFDouble(money);
		    	UFDouble money_de= new UFDouble((BigDecimal)map.get("money_de"));
		    	UFDouble newvalue=new UFDouble(nopay).sub(money_de);//新表体未付金额
		    	UFDouble payprice=ufnum.add(money_de); //新本次请款累计付款金额
		    	dao.executeUpdate("update tgfn_payreqbus set def47='"+newvalue+"' where pk_business_b='"+map.get("src_itemid")+"'");
		    	dao.executeUpdate("update tgfn_payrequest set def25='"+payprice+"' where pk_payreq =(select pk_payreq from tgfn_payreqbus where pk_business_b='"+map.get("src_itemid")+"')");
		    	dao.executeUpdate("update ap_payitem set def30='Y' where src_itemid='"+map.get("src_itemid")+"'");
		}
	}
	List<Map<String, Object>> pricelistmap=(List<Map<String, Object>> )dao.executeQuery("select money,def25,pk_payreq from tgfn_payrequest",new MapListProcessor());
	for(Map<String,Object> map:pricelistmap){
	    UFDouble hprice=map.get("money")==null?UFDouble.ZERO_DBL:new UFDouble(String.valueOf(map.get("money")));//表头请款金额
	    UFDouble hdef25=map.get("def25")==null?UFDouble.ZERO_DBL:new UFDouble(String.valueOf(map.get("def25")));//表头本次请款累计付款金额
	    UFDouble def49 =hprice.sub(hdef25);//表头未付金额
		dao.executeUpdate("update tgfn_payrequest set def49='"+def49+"'  where pk_payreq='"+map.get("pk_payreq")+"'");
	}
		}catch (Exception e){
			throw new BusinessException(e.getMessage());
		}
		return pobj;
	}

}
