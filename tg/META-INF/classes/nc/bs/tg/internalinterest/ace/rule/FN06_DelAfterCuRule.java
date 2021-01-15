package nc.bs.tg.internalinterest.ace.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IVOPersistence;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Interbus;
import nc.vo.tgfn.internalinterest.Internalinterest;

public class FN06_DelAfterCuRule implements IRule<AggInternalinterest>{

	@Override
	public void process(AggInternalinterest[] vos) {
Internalinterest inter = (Internalinterest) vos[0].getParent();//��ȡ���ݵı�ͷ��Ϣ
		
		String def4 = inter.getDef4();//�Ƿ���ڷŴ���˾
		if("Y".equals(def4)){
			//�Ƕ��ڷŴ���˾
			Interbus[] interbuss = (Interbus[]) vos[0].getChildrenVO();//��ȡ���ݵı�����Ϣ
			for(int i=0;i<interbuss.length;i++){
				Interbus interbus = interbuss[i];
				String uuid = interbus.getUuid_contact();
				IVOPersistence bs = NCLocator.getInstance().lookup(IVOPersistence.class);
				try {
					bs.deleteByClause(Internalinterest.class, " uuid_contact ='"+uuid+"'");
					bs.deleteByClause(Interbus.class, " uuid_contact ='"+uuid+"'");
				} catch (BusinessException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			}
	}

	

}
