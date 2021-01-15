package nc.bs.tg.financingexpense.rule;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.cdm.contractbankcredit.CwgwfzxqkBVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class UnApproveAfterRule implements IRule<AggFinancexpenseVO> {
	private  BaseDAO  dao=null;

	@Override
	public void process(AggFinancexpenseVO[] vos) {
		// TODO �Զ����ɵķ������
		for( AggFinancexpenseVO  vo:vos){
			if(!"Y".equals(vo.getParentVO().getAttributeValue("def17"))){
				dellFinDate(vo);
			}
		}
		
	}
	
	private void  dellFinDate(AggFinancexpenseVO finVO){
		//���������
		UFDouble applyamount=	(UFDouble) finVO.getParentVO().getAttributeValue("applyamount");
			//���λ
		String contract=	(String) finVO.getParentVO().getAttributeValue("def4");
		String pk_payer=	(String) finVO.getParentVO().getAttributeValue("pk_payer");
		String pk_finexpense=    (String) finVO.getParentVO().getAttributeValue("pk_finexpense");
		
		StringBuffer  sql  =new  StringBuffer();
		
		sql.append("  cdm_cwgwfzxqk.pk_contract ='"+contract+"' ")
		//.append("(select pk_contract  from cdm_contract where cdm_contract.contractcode='"+contract+"') ")
		.append("and  cdm_cwgwfzxqk.rowno='"+pk_finexpense+"' ");
		try {
			 getBaseDao().deleteByClause(CwgwfzxqkBVO.class, sql.toString());
					
		} catch (DAOException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		
	}
	private BaseDAO  getBaseDao(){
		if(dao==null)
			 dao =new BaseDAO();
		return dao;
		
	}

}
