package nc.vo.tg.outside;

import java.io.Serializable;
/**
 * 
 * @author lyq
 *	��ϵͳ����Ԥ�ᵥ��ͷ
 */
public class CostHeadVO implements Serializable {
	private String org;//������֯
	private String tradetype;//��������
	private String billdate;//��������
	private String srcid;//ebs����
	private String srcbillcode;//ebs����
	private String contractcode;//��ͬ���
	private String contractname;//��ͬ����
	private String contractversion;//��ͬ�汾
	private String billtype;//��������
	private String budget;//Ԥ������
	private String plate;//���
	private String platedetail;//��ҵ�����ϸ
	private float amount;//���
	private String operatororg;//�����˵�λ
	private String operator;//������
	private String operatordept;//�����˲���
	private String reason;//����
	private String creator;//�Ƶ���
    private String isdelete;//�Ƿ�ɾ��
    private String def29;//
    private String def30;//
    private String ispostcontract;//�Ƿ�����ͬ
	private String contractmoney;//��ͬ��̬���
	private String currency;//����
	private String isadvance;//�Ƿ��Ԥ��
	private String ncid;//nc����
	private String type;//nc����
	private String year;//Ԥ�����
	
	
	
	
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsadvance() {
		return isadvance;
	}

	public void setIsadvance(String isadvance) {
		this.isadvance = isadvance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getContractmoney() {
		return contractmoney;
	}

	public void setContractmoney(String contractmoney) {
		this.contractmoney = contractmoney;
	}

	public String getDef29() {
		return def29;
	}

	public String getIspostcontract() {
		return ispostcontract;
	}

	public void setIspostcontract(String ispostcontract) {
		this.ispostcontract = ispostcontract;
	}

	public void setDef29(String def29) {
		this.def29 = def29;
	}

	public String getDef30() {
		return def30;
	}

	public void setDef30(String def30) {
		this.def30 = def30;
	}

	public String getNcid() {
		return ncid;
	}

	public void setNcid(String ncid) {
		this.ncid = ncid;
	}
    private String isapprove;//�Ƿ�������
	public String getIsapprove() {
		return isapprove;
	}

	public void setIsapprove(String isapprove) {
		this.isapprove = isapprove;
	}
	
	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getTradetype() {
		return tradetype;
	}

	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}


	public String getSrcid() {
		return srcid;
	}

	public void setSrcid(String srcid) {
		this.srcid = srcid;
	}

	public String getSrcbillcode() {
		return srcbillcode;
	}

	public void setSrcbillcode(String srcbillcode) {
		this.srcbillcode = srcbillcode;
	}

	public String getContractcode() {
		return contractcode;
	}

	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}

	public String getContractname() {
		return contractname;
	}

	public void setContractname(String contractname) {
		this.contractname = contractname;
	}

	public String getContractversion() {
		return contractversion;
	}

	public void setContractversion(String contractversion) {
		this.contractversion = contractversion;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getPlatedetail() {
		return platedetail;
	}

	public void setPlatedetail(String platedetail) {
		this.platedetail = platedetail;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getOperatororg() {
		return operatororg;
	}

	public void setOperatororg(String operatororg) {
		this.operatororg = operatororg;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatordept() {
		return operatordept;
	}

	public void setOperatordept(String operatordept) {
		this.operatordept = operatordept;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
