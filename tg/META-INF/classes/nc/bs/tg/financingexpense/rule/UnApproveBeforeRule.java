package nc.bs.tg.financingexpense.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class UnApproveBeforeRule implements IRule<AggFinancexpenseVO>{

	@Override
	public void process(AggFinancexpenseVO[] vos) {
		// TODO �Զ����ɵķ������
		String sql="select bill_no from cmp_paybill where pk_upbill ='"+vos[0].getParentVO().getPrimaryKey()+"' and dr=0";
		try {
			String billno=(String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
			if(billno!=null){
				ExceptionUtils.wrappBusinessException("�õ��ݴ������ε���,�봦��!��"+billno+"��");
			}
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException(e.getMessage());

		}
	}

}
