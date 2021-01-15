package nc.bs.tg.internalinterest.ace.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.IInternalInterestMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Interbus;
import nc.vo.tgfn.internalinterest.Internalinterest;

public class FN06_SaveAfterRule implements IRule<AggInternalinterest> {

	@Override
	public void process(AggInternalinterest[] vos) {
		Internalinterest internalinterest = (Internalinterest) vos[0].getParent();//获取单据的表头信息
		
		String def4 = internalinterest.getDef4();//是否对内放贷公司
		if(def4=="100112100000000005KE"||def4.equals("100112100000000005KE")){
			//是对内放贷公司
			String pk_org = internalinterest.getPk_org();
			Interbus[] interbuss = (Interbus[]) vos[0].getChildrenVO();//获取单据的表体信息
			internalinterest.setDef4("100112100000000005KG");//对内放贷公司设为否
			
			
			for(int i=0;i<interbuss.length;i++){
				Interbus interbus = interbuss[i];
				String def1 = interbus.getDef1();//表体 客商
				String def5 = interbus.getDef5();//表体 金额合计
				String uuid = interbus.getUuid_contact();//表体 UUID
				interbus.setPk_internal(null);
				interbus.setDef1(pk_org);
				internalinterest.setPk_org(def1);
				internalinterest.setPk_org_v(getPk_vid(def1));
				internalinterest.setPk_internal(null);
				internalinterest.setBillno(null);
				internalinterest.setDef11(def5);//表头 表体金额合计
				internalinterest.setUuid_contact(uuid);
				interbus.setDef2(def5);//表体 利息金额=原表体的金额合计
				interbus.setDef3(null);//表体 待摊分闲置资金利息金额
				interbus.setDef4(null);//表体 待摊分承销费发行费金额
				interbus.setDef7(def5);//表体 应付利息=原表体的金额合计
				interbus.setDef8(null);//表体 差额
				interbus.setPk_business_b(null);//表体 主键设空
				AggInternalinterest aggInternalinterest = new AggInternalinterest();
				Interbus[] bodys = {interbus};
				aggInternalinterest.setParent(internalinterest);//设置表头信息
				aggInternalinterest.setChildren(Interbus.class, bodys);//设置表体信息
				AggInternalinterest[] aggs = {aggInternalinterest};
				IInternalInterestMaintain operator = NCLocator.getInstance()
						.lookup(IInternalInterestMaintain.class);
				try {
					operator.insert(aggs, vos);
				} catch (BusinessException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		
		}else if(def4=="100112100000000005KG"||def4.equals("100112100000000005KG")){
			//不是对内放贷公司
			
		}
	}

	
	/*
	 * 
	 * 根据"财务组织"主键获取"组织版本"主键
	 * 
	 */
	public static String getPk_vid (String def1){
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String pk_vid = null;
		try {
			pk_vid = (String) bs.executeQuery("select pk_vid from org_orgs_v where pk_org = '"+def1+"'",new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return pk_vid;
	}


}
