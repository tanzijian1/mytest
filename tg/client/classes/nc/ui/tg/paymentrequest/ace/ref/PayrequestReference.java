package nc.ui.tg.paymentrequest.ace.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class PayrequestReference extends AbstractRefModel {
	public PayrequestReference() {
		super();
		init();
	}

	private void init() {
		// *根据需求设置相应参数
		setFieldCode(new String[] { "billno", "def1", "def2", "def5", "def6" });
		setFieldName(new String[] { "编码", "请款主键", "请款单号", "采购协议编码",
				"采购协议名称" });
		setHiddenFieldCode(new String[] { "pk_payreq" });
		setPkFieldCode("pk_payreq");
		setRefCodeField("billno");
		setRefNameField("billno");
		setTableName("tgfn_payrequest");
		setRefNodeName("付款申请单");
		setRefTitle("付款申请单");
		// setRefShowNameField("");
	}

	@Override
	protected String getEnvWherePart() {
		return "nvl(dr,0)=0";
	}
}
