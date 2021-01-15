package nc.ui.tg.singleissue.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class SingleIssueRefModel extends AbstractRefModel{
	public SingleIssueRefModel(){
		super();
		setRefTitle("���ڷ������");
		setFieldCode(new String[]{"billno","name"});
		setFieldName(new String[]{"���ݺ�","����"});
		setHiddenFieldCode(new String[]{"pk_singleissue"});
		setTableName("sdfn_singleissue");
		setPkFieldCode("pk_singleissue");
		setRefCodeField("billno");
		setRefNameField("name");
	}

	@Override
	public String getWherePart() {
		String wherePart = super.getWherePart();
		if(null == wherePart || "".equals(wherePart.trim())){
			wherePart = " nvl(dr,0) = 0  and approvestatus=1";
		}else{
			wherePart += " and nvl(dr,0) = 0  and approvestatus=1";
		}
		return wherePart;
	}
	
}
