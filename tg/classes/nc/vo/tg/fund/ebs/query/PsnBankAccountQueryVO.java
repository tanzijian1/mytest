package nc.vo.tg.fund.ebs.query;

/**
 * 人员编号 ： PERSON_NUMBER 个人开户行名称 ： FVENDOR_OPEN_BANK_NAME 个人开户行编码 ：
 * FVENDOR_OPEN_BANK_CODE 个人银行账户 ： fvendor_bank_code 个人账户户名 ：
 * fbank_account_name 最后更新时间：TS  nc_key_id nc主键  
 * 
 * @author lyq
 * 
 */
public class PsnBankAccountQueryVO {
	public String person_number;
	public String fvendor_open_bank_name;
	public String fvendor_open_bank_code;
	public String fvendor_bank_code;
	public String fbank_account_name;
	public String ts;
	public String nc_key_id;
	public String enable_flag;

	
	
	
	public String getEnable_flag() {
		return enable_flag;
	}

	public void setEnable_flag(String enable_flag) {
		this.enable_flag = enable_flag;
	}

	public String getNc_key_id() {
		return nc_key_id;
	}

	public void setNc_key_id(String nc_key_id) {
		this.nc_key_id = nc_key_id;
	}

	public String getPerson_number() {
		return person_number;
	}

	public void setPerson_number(String person_number) {
		this.person_number = person_number;
	}

	public String getFvendor_open_bank_name() {
		return fvendor_open_bank_name;
	}

	public void setFvendor_open_bank_name(String fvendor_open_bank_name) {
		this.fvendor_open_bank_name = fvendor_open_bank_name;
	}

	public String getFvendor_open_bank_code() {
		return fvendor_open_bank_code;
	}

	public void setFvendor_open_bank_code(String fvendor_open_bank_code) {
		this.fvendor_open_bank_code = fvendor_open_bank_code;
	}

	public String getFvendor_bank_code() {
		return fvendor_bank_code;
	}

	public void setFvendor_bank_code(String fvendor_bank_code) {
		this.fvendor_bank_code = fvendor_bank_code;
	}

	public String getFbank_account_name() {
		return fbank_account_name;
	}

	public void setFbank_account_name(String fbank_account_name) {
		this.fbank_account_name = fbank_account_name;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

}
