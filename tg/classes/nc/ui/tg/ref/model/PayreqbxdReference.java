package nc.ui.tg.ref.model;

import nc.ui.bd.ref.AbstractRefModel;

public class PayreqbxdReference extends AbstractRefModel {
	public PayreqbxdReference() {
		super();
		init();
	}

	private void init() {
		// *��������������Ӧ����
		setFieldCode(new String[] { "billno", "srcbillno", "transtype", "pkey", "srcbillid" });
		setFieldName(new String[] { "���ݱ��", "��ϵͳ���", "��������", "��������", "��ϵͳid" });
		setHiddenFieldCode(new String[] { "pkey" });
		setPkFieldCode("pkey");
		setRefCodeField("billno");
		setRefNameField("billno");
		setTableName("tg_payreqbxd");
		setRefNodeName("�������뵥������");
		setRefTitle("�������뵥������");
		// setRefShowNameField("");
	}

	@Override
	protected String getEnvWherePart() {
		return "nvl(dr,0)=0";
	}
}