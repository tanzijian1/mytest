package nc.bs.tg.internalinterest.ace.rule;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Internalinterest;

public class FN06_SaveBeforeRule implements IRule<AggInternalinterest> {

	@Override
	public void process(AggInternalinterest[] vos) {
//		Interbus[] interbuss = (Interbus[]) vos[0].getChildrenVO();//获取单据的表体信息
//		for(int i=0;i<interbuss.length;i++){
//			String uuid = UUID.randomUUID().toString().replaceAll("-","");
//			interbuss[i].setUuid_contact(uuid);
//		}
		BaseDAO dao=new BaseDAO();
		try {
			Internalinterest internalinterest = (Internalinterest) vos[0].getParent();//获取单据的表头信息
			String billno=internalinterest.billno;
//			Collection<AggInternalinterest> coll=NCLocator.getInstance().lookup(IMDPersistenceQueryService.class).queryBillOfVOByCond(AggInternalinterest.class, "uuid_contact='"+hpk+"'", false);
		List<String> hpks=(List<String>)dao.executeQuery("select pk_internal from tgfn_internalinterest  where uuid_contact='"+billno+"'", new ColumnListProcessor());
			if(hpks!=null&&hpks.size()>0){
//			AggInternalinterest[] aggvos=coll.toArray(new AggInternalinterest[0]);
//			 IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
				for(String  pk:hpks){
					 dao.executeUpdate("delete from tgfn_interbus where pk_internal='"+pk+"'");
					 dao.executeUpdate("delete from tgfn_internalinterest where pk_internal='"+pk+"'");
				 }
			}
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
	}

}
