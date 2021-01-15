package nc.ui.tg.approvalpro.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class ApprovalProRefModel extends AbstractRefModel{
	public ApprovalProRefModel(){
		super();
		setRefTitle("批文方案");
		setFieldCode(new String[]{"billno","name"});
		setFieldName(new String[]{"单据号","标题"});
		setHiddenFieldCode(new String[]{"pk_appro"});
		setTableName("sdfn_approvalpro");
		setPkFieldCode("pk_appro");
		setRefCodeField("billno");
		setRefNameField("name");
	}

	@Override
	public String getWherePart() {
		// TODO 自动生成的方法存根
		String wherePart = super.getWherePart();
		if(null == wherePart || "".equals(wherePart.trim())){
			wherePart = " nvl(dr,0) = 0 and nvl(def16,'N') <> 'Y' and approvestatus=1";
		}else{
			wherePart += " and nvl(dr,0) = 0 and nvl(def16,'N') <> 'Y'  and approvestatus=1";
		}
		return wherePart;
	}
	
	
}
