package nc.bs.tg.tartingbill.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.fip.relation.IFipRelation;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
import nc.vo.fip.relation.FipRelationVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tg.tartingbill.AggTartingBillVO;

public class TartingUnApproveDelGL implements IRule<AggTartingBillVO>{

	@Override
	public void process(AggTartingBillVO[] vos) {
		// TODO 自动生成的方法存根
    BaseDAO dao=new BaseDAO();
		// TODO 自动生成的方法存根
		for(AggregatedValueObject vo:vos){
		IFipRelation ip1 =NCLocator.getInstance().lookup(IFipRelation.class);
		FipRelationVO[] fipvos;
		try {
			String cuserid=(String)dao.executeQuery("select cuserid  from sm_user where user_name='SALE' ", new ColumnProcessor());
			if(cuserid.equals(vo.getParentVO().getAttributeValue("creator"))){			fipvos = ip1.queryByWhere(" src_relationid in ('"+vo.getParentVO().getPrimaryKey()+"') ");
		if(fipvos!=null&&fipvos.length>0){
		ip1.delete(fipvos);
		}
			}
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			ExceptionUtils.wrappException(e);
		 }
	  }
	
	}

}
