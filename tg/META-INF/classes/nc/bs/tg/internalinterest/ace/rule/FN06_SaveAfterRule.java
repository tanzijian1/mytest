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
		Internalinterest internalinterest = (Internalinterest) vos[0].getParent();//��ȡ���ݵı�ͷ��Ϣ
		
		String def4 = internalinterest.getDef4();//�Ƿ���ڷŴ���˾
		if(def4=="100112100000000005KE"||def4.equals("100112100000000005KE")){
			//�Ƕ��ڷŴ���˾
			String pk_org = internalinterest.getPk_org();
			Interbus[] interbuss = (Interbus[]) vos[0].getChildrenVO();//��ȡ���ݵı�����Ϣ
			internalinterest.setDef4("100112100000000005KG");//���ڷŴ���˾��Ϊ��
			
			
			for(int i=0;i<interbuss.length;i++){
				Interbus interbus = interbuss[i];
				String def1 = interbus.getDef1();//���� ����
				String def5 = interbus.getDef5();//���� ���ϼ�
				String uuid = interbus.getUuid_contact();//���� UUID
				interbus.setPk_internal(null);
				interbus.setDef1(pk_org);
				internalinterest.setPk_org(def1);
				internalinterest.setPk_org_v(getPk_vid(def1));
				internalinterest.setPk_internal(null);
				internalinterest.setBillno(null);
				internalinterest.setDef11(def5);//��ͷ ������ϼ�
				internalinterest.setUuid_contact(uuid);
				interbus.setDef2(def5);//���� ��Ϣ���=ԭ����Ľ��ϼ�
				interbus.setDef3(null);//���� ��̯�������ʽ���Ϣ���
				interbus.setDef4(null);//���� ��̯�ֳ����ѷ��зѽ��
				interbus.setDef7(def5);//���� Ӧ����Ϣ=ԭ����Ľ��ϼ�
				interbus.setDef8(null);//���� ���
				interbus.setPk_business_b(null);//���� �������
				AggInternalinterest aggInternalinterest = new AggInternalinterest();
				Interbus[] bodys = {interbus};
				aggInternalinterest.setParent(internalinterest);//���ñ�ͷ��Ϣ
				aggInternalinterest.setChildren(Interbus.class, bodys);//���ñ�����Ϣ
				AggInternalinterest[] aggs = {aggInternalinterest};
				IInternalInterestMaintain operator = NCLocator.getInstance()
						.lookup(IInternalInterestMaintain.class);
				try {
					operator.insert(aggs, vos);
				} catch (BusinessException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		
		}else if(def4=="100112100000000005KG"||def4.equals("100112100000000005KG")){
			//���Ƕ��ڷŴ���˾
			
		}
	}

	
	/*
	 * 
	 * ����"������֯"������ȡ"��֯�汾"����
	 * 
	 */
	public static String getPk_vid (String def1){
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String pk_vid = null;
		try {
			pk_vid = (String) bs.executeQuery("select pk_vid from org_orgs_v where pk_org = '"+def1+"'",new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return pk_vid;
	}


}
