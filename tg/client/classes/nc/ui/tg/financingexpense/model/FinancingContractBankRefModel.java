package nc.ui.tg.financingexpense.model;

import nc.ui.cdm.contract.ref.ContractRefModel;
import nc.vo.pub.contract.ContractTypeEnum;
import nc.vo.pub.contract.ContractVO;

public class FinancingContractBankRefModel extends ContractRefModel {
	public FinancingContractBankRefModel() {
		super();
	}

	public String getRefTitle() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3615pub_0","03615pub-0019")/*@res "���д����ͬ"*/;
	}

	@Override
	public String getRefNodeName() {
		return getRefTitle();
	}

	@Override
	public int getDefaultFieldCount() {
		return 8;
	}

	@Override
	public String getTableName() {
		return "cdm_contract";
	}

	@Override
	public String[] getFieldCode() {
		String[] fieldCodes = new String[] {"htmc",ContractVO.CONTRACTCODE,
				ContractVO.DEBITCORPNAME,
				ContractVO.PK_CREDITBANK,
				ContractVO.PK_CURRTYPE,
				ContractVO.CONTRACTAMOUNT + FORVIEW,
				ContractVO.PAYAMOUNT + FORVIEW,
				ContractVO.REPAYAMOUNT + FORVIEW,
				ContractVO.BEGINDATE};
		return fieldCodes;
	}

	@Override
	public String[] getFieldName() {
		String[] fieldNames = new String[0];
		fieldNames = new String[] { "��ͬ����",
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0001133")/*@res "��ͬ���"*/, 
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3615pub_0","03615pub-0002")/*@res "��λ"*/, 
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3615pub_0","03615pub-0003")/*@res "��������"*/, 
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0001110")/*@res "��ͬ����"*/, 
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3615pub_0","03615pub-0020")/*@res "��ͬ���"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3615pub_0","03615pub-0021")/*@res "�ѷű���"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3615pub_0","03615pub-0022")/*@res "�ѻ�����"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0003900")/*@res "��ʼ����"*/};
		return fieldNames;
	}
	
	@Override
	public String[][] getFormulas() {
		String[][] formulas = new String[][] {
				{ "begindate", "begindate->left(begindate,10)" },
				{ "enddate", "enddate->left(enddate,10)" },
				{ "pk_creditbank","getMLCValue(\"bd_bankdoc\",\"name\",\"pk_bankdoc\",pk_creditbank)"},
				{ "pk_currtype", "getMLCValue(\"bd_currtype\",\"name\",\"pk_currtype\",pk_currtype)" }
				 };
		return formulas;
	}

	@Override
	protected String getEnvWherePart() {
		setContractType(ContractTypeEnum.BANKCREDIT.toStringValue());
		return super.getEnvWherePart();
	}

	@Override
	public boolean isCacheEnabled() {
		//V65�޸�Ч�����⣬���˲�������Ϊ �ڴ漶�����
		return true;
	}
	
	@Override
	public String getRefShowNameField() {
		// TODO �Զ����ɵķ������
		return "htmc";
	}
}
