package nc.ui.tg.paymentrequest.ace.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class PayrequestReference extends AbstractRefModel {
	public PayrequestReference() {
		super();
		init();
	}

	private void init() {
		// *��������������Ӧ����
		setFieldCode(new String[] { "billno", "def1", "def2", "def5", "def6" });
		setFieldName(new String[] { "����", "�������", "����", "�ɹ�Э�����",
				"�ɹ�Э������" });
		setHiddenFieldCode(new String[] { "pk_payreq" });
		setPkFieldCode("pk_payreq");
		setRefCodeField("billno");
		setRefNameField("billno");
		setTableName("tgfn_payrequest");
		setRefNodeName("�������뵥");
		setRefTitle("�������뵥");
		// setRefShowNameField("");
	}

	@Override
	protected String getEnvWherePart() {
		return "nvl(dr,0)=0";
	}
}
