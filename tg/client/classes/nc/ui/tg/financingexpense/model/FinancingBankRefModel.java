package nc.ui.tg.financingexpense.model;

import nc.ui.bd.ref.AbstractRefModel;

public class FinancingBankRefModel extends AbstractRefModel{

	public FinancingBankRefModel() {
		// TODO 自动生成的构造函数存根
		
		setRefTitle("银行贷款合同");

		setFieldCode(new String[] { "contractcode", "debitcorpname", "pk_currtype", "contractamount", "payamount", "repayamount"});
		setFieldName(new String[] {"合同编号","借款公司","合同币种","合同金额","已放金额","已还本金",});

		setHiddenFieldCode(new String[] { "pk_contract"});
		setPkFieldCode("pk_contract");
//		setRefCodeField("pk_czgs");
//		setRefNameField("分摊金额");
		setTableName("cdm_contract");
		setWherePart(" isnull(dr,0) = 0  and contracttype = 'BANKCREDIT' ");
		
		

	}

}
