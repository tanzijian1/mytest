package nc.bs.tg.internalinterest.ace.rule;

import org.apache.poi.ss.formula.functions.Count;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Interbus;
import nc.vo.tgfn.internalinterest.Internalinterest;

public class FN06_UpdateAfterRule  implements IRule<AggInternalinterest> {

	@Override
	public void process(AggInternalinterest[] vos) {
		// TODO 自动生成的方法存根
		Internalinterest internalinterest = (Internalinterest) vos[0].getParent();//获取单据的表头信息
		String def11 = internalinterest.getDef11();//表头 表体金额合计
		
		Interbus[] interbuss = (Interbus[]) vos[0].getChildrenVO();//获取单据的表体信息
		String[] pk_org = new String[interbuss.length];//表体 客商
		String[] def5 = new String[interbuss.length];//表体 合计金额
		String[] pk_vid = new String[interbuss.length];
		String[] uuid = new String[interbuss.length];
		String [][] bodyksInfo = new String[interbuss.length][3];//客商信息
		String [][] bodydef5Info = new String[interbuss.length][3];//表体金额合计
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		try {
			for (int i=0;i<interbuss.length;i++) {
				//获取客商修改必要信息
				bodyksInfo[i][0] = interbuss[i].getDef1();
				bodyksInfo[i][1] = (String) bs.executeQuery("select pk_vid from org_orgs_v where pk_org = '"+ interbuss[i].getDef1() +"'",new ColumnProcessor());
				bodyksInfo[i][2] = interbuss[i].getUuid_contact();
				
				//获取表体合计金额必要信息
				String pk_internal = (String) bs.executeQuery("select pk_internal from tgfn_internalinterest where uuid_contact = '"+ interbuss[i].getUuid_contact() +"'",new ColumnProcessor());
				String pk_business_b =(String) bs.executeQuery("select pk_business_b from tgfn_interbus where pk_internal = '"+ pk_internal +"'",new ColumnProcessor());
				bodydef5Info[i][0] = interbuss[i].getDef5();
				bodydef5Info[i][1] = pk_internal;
				bodydef5Info[i][2] = pk_business_b;
			}
			change_BodyDef5(bodydef5Info,interbuss.length);//当表体的金额合计被修改
			change_BodyKs(bodyksInfo,interbuss.length);//客商被修改
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		IVOPersistence ivo = NCLocator.getInstance().lookup(IVOPersistence.class);
	}
	
	
	/*
	 * 当a表表体的客商被修改时，更新b表表头的财务组织和组织版本
	 */
	public void change_BodyKs(String [][] bodyksInfo,int num){
		BaseDAO dao = new BaseDAO();
		try {
			for(int i=0;i<num;i++){
				String sql = "update tgfn_internalinterest t set t.pk_org = '"+ bodyksInfo[i][0] +"',t.pk_org_v='"+ bodyksInfo[i][1] +"' where t.uuid_contact='"+ bodyksInfo[i][2] +"'";
				dao.executeUpdate(sql);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 当a表表体的金额合计被修改，更新b表表头中的"表体金额合计"和表体中的"利息金额"、"金额合计"、"应付利息"
	 */
	public void change_BodyDef5(String [][] bodydef5Info,int num){
		BaseDAO dao = new BaseDAO();
		try {
			for(int i=0;i<num;i++){
				String sql1 = "update tgfn_internalinterest set def11 = '"+bodydef5Info[i][0] +"' where pk_internal ='"+bodydef5Info[i][1]+"'";
				String sql2 = "update tgfn_interbus set def2 ='"+bodydef5Info[i][0] +"',def5='"+bodydef5Info[i][0] +"',def7='"+bodydef5Info[i][0] +"' where pk_business_b='"+ bodydef5Info[i][2] +"'";
				dao.executeUpdate(sql1);
				dao.executeUpdate(sql2);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
}
