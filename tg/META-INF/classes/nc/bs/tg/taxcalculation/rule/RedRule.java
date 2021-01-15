package nc.bs.tg.taxcalculation.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;



public class RedRule implements IRule<AggTaxCalculationHead>{

	@Override
	public void process(AggTaxCalculationHead[] vos) {
		// TODO 自动生成的方法存根
		BaseDAO dao=new BaseDAO();
		String pk=null;
		for(AggTaxCalculationHead vo:vos){
			String billno=vo.getParentVO().getBillno();
			if(billno!=null){
				try {
					pk=(String)dao.executeQuery("select pk_taxcalhead from tgfn_taxcalhead where def1='"+billno+"' and nvl(dr,0)=0", new ColumnProcessor());
				     
				} catch (DAOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				if(pk!=null&&pk!=""){
					ExceptionUtils.wrappBusinessException("单据已作红冲，不允许反向操作");
				}
			}
		}
	}

}
