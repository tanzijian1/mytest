package nc.bs.tg.tgrz_mortgageagreement.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

public class UnSaveBillRule implements IRule <AggMortgageAgreementVO>{

	@Override
	public void process(AggMortgageAgreementVO[] vos) {
		// TODO 自动生成的方法存根
		String sql="update tgrz_mortgageagreement set def19 = '~' , def20 = '~' where pk_moragreement ='"+vos[0].getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0";
		try {
			new BaseDAO().executeUpdate(sql);
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}


}
