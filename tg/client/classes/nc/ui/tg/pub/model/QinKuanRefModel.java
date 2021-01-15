package nc.ui.tg.pub.model;

import nc.ui.bd.ref.AbstractRefModel;

public class QinKuanRefModel extends AbstractRefModel {
	public QinKuanRefModel() {
		setTableName("v_qingkuan");
		setRefTitle("请款单");
		setFieldName(new String[] { "请款单号", "金额" });
		setRefCodeField("billno");
		setFieldCode(new String[] { "billno", "money" });
		setHiddenFieldCode(new String[] { "pk_key", "contractnum" });
		setPkFieldCode("pk_key");
	}
}
