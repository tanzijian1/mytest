package nc.ui.tg.singleissue.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class RepaymentPlanRefModel  extends AbstractRefModel{
	private String pk_singleissue;
	public RepaymentPlanRefModel(){
		super();
		setRefTitle("还款计划");
		setFieldCode(new String[]{"def2","def1"});
		setFieldName(new String[]{"还款日期","还款编号"});
		setHiddenFieldCode(new String[]{"pk_repayplan"});
		setTableName("sdfn_repaymentplan");
		setPkFieldCode("pk_repayplan");
		setRefCodeField("def2");
		setRefNameField("def1");
	}
	@Override
	public String getWherePart() {
		String wherePart = super.getWherePart();
		if(null == wherePart || "".equals(wherePart.trim())){
			wherePart = " nvl(dr,0) = 0 and pk_singleissue='"+getPk_singleissue()+"'";
		}else{
			wherePart += " and nvl(dr,0) = 0 and pk_singleissue='"+getPk_singleissue()+"'";
		}
		return wherePart;
	}
	public String getPk_singleissue() {
		return pk_singleissue;
	}
	public void setPk_singleissue(String pk_singleissue) {
		this.pk_singleissue = pk_singleissue;
	}
	
}
