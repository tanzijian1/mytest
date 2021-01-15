package nc.ui.tg.ref.model;

import nc.ui.bd.ref.AbstractRefModel;

public class PayreqbxdReference extends AbstractRefModel {
	public PayreqbxdReference() {
		super();
		init();
	}

	private void init() {
		// *根据需求设置相应参数
		setFieldCode(new String[] { "billno", "srcbillno", "transtype", "pkey", "srcbillid" });
		setFieldName(new String[] { "单据编号", "外系统编号", "交易类型", "单据主键", "外系统id" });
		setHiddenFieldCode(new String[] { "pkey" });
		setPkFieldCode("pkey");
		setRefCodeField("billno");
		setRefNameField("billno");
		setTableName("tg_payreqbxd");
		setRefNodeName("付款申请单报销单");
		setRefTitle("付款申请单报销单");
		// setRefShowNameField("");
	}

	@Override
	protected String getEnvWherePart() {
		return "nvl(dr,0)=0";
	}
}