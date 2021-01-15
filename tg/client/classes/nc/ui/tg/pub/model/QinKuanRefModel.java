package nc.ui.tg.pub.model;

import nc.ui.bd.ref.AbstractRefModel;

public class QinKuanRefModel extends AbstractRefModel {
	public QinKuanRefModel() {
		setTableName("v_qingkuan");
		setRefTitle("��");
		setFieldName(new String[] { "����", "���" });
		setRefCodeField("billno");
		setFieldCode(new String[] { "billno", "money" });
		setHiddenFieldCode(new String[] { "pk_key", "contractnum" });
		setPkFieldCode("pk_key");
	}
}
