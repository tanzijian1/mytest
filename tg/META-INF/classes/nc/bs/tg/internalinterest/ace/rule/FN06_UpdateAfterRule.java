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
		// TODO �Զ����ɵķ������
		Internalinterest internalinterest = (Internalinterest) vos[0].getParent();//��ȡ���ݵı�ͷ��Ϣ
		String def11 = internalinterest.getDef11();//��ͷ ������ϼ�
		
		Interbus[] interbuss = (Interbus[]) vos[0].getChildrenVO();//��ȡ���ݵı�����Ϣ
		String[] pk_org = new String[interbuss.length];//���� ����
		String[] def5 = new String[interbuss.length];//���� �ϼƽ��
		String[] pk_vid = new String[interbuss.length];
		String[] uuid = new String[interbuss.length];
		String [][] bodyksInfo = new String[interbuss.length][3];//������Ϣ
		String [][] bodydef5Info = new String[interbuss.length][3];//������ϼ�
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		try {
			for (int i=0;i<interbuss.length;i++) {
				//��ȡ�����޸ı�Ҫ��Ϣ
				bodyksInfo[i][0] = interbuss[i].getDef1();
				bodyksInfo[i][1] = (String) bs.executeQuery("select pk_vid from org_orgs_v where pk_org = '"+ interbuss[i].getDef1() +"'",new ColumnProcessor());
				bodyksInfo[i][2] = interbuss[i].getUuid_contact();
				
				//��ȡ����ϼƽ���Ҫ��Ϣ
				String pk_internal = (String) bs.executeQuery("select pk_internal from tgfn_internalinterest where uuid_contact = '"+ interbuss[i].getUuid_contact() +"'",new ColumnProcessor());
				String pk_business_b =(String) bs.executeQuery("select pk_business_b from tgfn_interbus where pk_internal = '"+ pk_internal +"'",new ColumnProcessor());
				bodydef5Info[i][0] = interbuss[i].getDef5();
				bodydef5Info[i][1] = pk_internal;
				bodydef5Info[i][2] = pk_business_b;
			}
			change_BodyDef5(bodydef5Info,interbuss.length);//������Ľ��ϼƱ��޸�
			change_BodyKs(bodyksInfo,interbuss.length);//���̱��޸�
		} catch (BusinessException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		IVOPersistence ivo = NCLocator.getInstance().lookup(IVOPersistence.class);
	}
	
	
	/*
	 * ��a�����Ŀ��̱��޸�ʱ������b���ͷ�Ĳ�����֯����֯�汾
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
	 * ��a�����Ľ��ϼƱ��޸ģ�����b���ͷ�е�"������ϼ�"�ͱ����е�"��Ϣ���"��"���ϼ�"��"Ӧ����Ϣ"
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
