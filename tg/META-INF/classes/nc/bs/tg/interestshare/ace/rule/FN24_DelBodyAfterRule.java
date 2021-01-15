package nc.bs.tg.interestshare.ace.rule;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.tgfn.interestshare.IntshareHead;
import nc.vo.tgfn.internalinterest.Internalinterest;

public class FN24_DelBodyAfterRule implements IRule<AggIntshareHead>{

	@Override
	public void process(AggIntshareHead[] vos) {
		// TODO 自动生成的方法存根
		BaseDAO dao=new BaseDAO();
		IntshareHead hvo = (IntshareHead) vos[0].getParent();//获取单据的表头信息
		String billno=hvo.getBillno();
		try {
			List<String> hpks=(List<String>)dao.executeQuery("select pk_intsharehead from tgfn_intsharehead  where def10='"+billno+"'", new ColumnListProcessor());
			for(String  pk:hpks){
				 dao.executeUpdate("delete from tgfn_intsharehead where pk_intsharehead='"+pk+"'");
				 dao.executeUpdate("delete from tg_intsharebody where pk_intsharehead='"+pk+"'");
			 }
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}


	}

}
