package nc.bs.tg.pub.rule;

import java.util.List;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.fip.relation.IFipRelation;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
import nc.vo.fip.relation.FipRelationVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;

public class UnApproveDeleteGLRule implements IRule{

	@Override
	public void process(Object[] vos) {
		// TODO 自动生成的方法存根
		BaseDAO dao=new BaseDAO();
		for(Object obj:vos){
			AggregatedValueObject vo=(AggregatedValueObject)obj;
		IFipRelation ip1 =NCLocator.getInstance().lookup(IFipRelation.class);
		
		FipRelationVO[] fipvos;
		try {
			String cuserid=(String)dao.executeQuery("select cuserid  from sm_user where user_name='SALE' ", new ColumnProcessor());
			if(cuserid.equals(vo.getParentVO().getAttributeValue("creator"))){
//			Object type= vo.getParentVO().getAttributeValue("pk_tradetype")==null?vo.getParentVO().getAttributeValue("transtype"):vo.getParentVO().getAttributeValue("pk_tradetype");
//			if(type==null)type=vo.getParentVO().getAttributeValue("billtype");
//			List<String> list =Arrays.asList(new String[]{"FN11-Cxx-01","FN11-Cxx-02",	"FN11-Cxx-03",	"FN11-Cxx-04",	"FN11-Cxx-05", 	"FN11-Cxx-06", 	"FN11-Cxx-07", "FN13-Cxx-02","FN13-Cxx-03"});
//			if(list.contains(type)){
			fipvos = ip1.queryByWhere("  src_relationid in ('"+vo.getParentVO().getPrimaryKey()+"') ");
		if(fipvos!=null&&fipvos.length>0){
		ip1.delete(fipvos);
		  }
//			}
			}
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			ExceptionUtils.wrappException(e);
		 }
	  }
	}
}
