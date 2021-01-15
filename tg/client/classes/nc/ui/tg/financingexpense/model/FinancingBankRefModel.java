package nc.ui.tg.financingexpense.model;

import nc.ui.bd.ref.AbstractRefModel;

public class FinancingBankRefModel extends AbstractRefModel{

	public FinancingBankRefModel() {
		// TODO �Զ����ɵĹ��캯�����
		
		setRefTitle("���д����ͬ");

		setFieldCode(new String[] { "contractcode", "debitcorpname", "pk_currtype", "contractamount", "payamount", "repayamount"});
		setFieldName(new String[] {"��ͬ���","��˾","��ͬ����","��ͬ���","�ѷŽ��","�ѻ�����",});

		setHiddenFieldCode(new String[] { "pk_contract"});
		setPkFieldCode("pk_contract");
//		setRefCodeField("pk_czgs");
//		setRefNameField("��̯���");
		setTableName("cdm_contract");
		setWherePart(" isnull(dr,0) = 0  and contracttype = 'BANKCREDIT' ");
		
		

	}

}
