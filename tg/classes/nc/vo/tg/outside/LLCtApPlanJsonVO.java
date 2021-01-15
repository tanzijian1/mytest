package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApPlanJsonVO implements Serializable {
	private String pk_ebs;// ebs主键
	private String proceedtype;// 款项类型
	private String paymentdate;// 计划付款日期
	private String paymentratio;// 计划付款比例
	private String paymentamount;// 计划付款金额
	private String paymentcondition;// 付款条件
	private String charactertype;// 款项性质
	private String lineamountam;// 累计请款金额
	private String paidamount;// 累计已付款金额
	private String offsetamount;// 抵冲金额
	private String fapplynumber;// 抵冲单号
	private String followup;// 后续处理
	private String expecteddate;// 预计退回/解付日期
	private String returnconditions;// 退回/解付条件
	private String refunddate;// 收到退款日期
	private String refundamount;// 退款金额
	private String refundamountsum;// 退款总额

	public String getRefundamountsum() {
		return refundamountsum;
	}

	public void setRefundamountsum(String refundamountsum) {
		this.refundamountsum = refundamountsum;
	}

	public String getPk_ebs() {
		return pk_ebs;
	}

	public void setPk_ebs(String pk_ebs) {
		this.pk_ebs = pk_ebs;
	}

	public String getProceedtype() {
		return proceedtype;
	}

	public void setProceedtype(String proceedtype) {
		this.proceedtype = proceedtype;
	}

	public String getPaymentdate() {
		return paymentdate;
	}

	public void setPaymentdate(String paymentdate) {
		this.paymentdate = paymentdate;
	}

	public String getPaymentratio() {
		return paymentratio;
	}

	public void setPaymentratio(String paymentratio) {
		this.paymentratio = paymentratio;
	}

	public String getPaymentamount() {
		return paymentamount;
	}

	public void setPaymentamount(String paymentamount) {
		this.paymentamount = paymentamount;
	}

	public String getPaymentcondition() {
		return paymentcondition;
	}

	public void setPaymentcondition(String paymentcondition) {
		this.paymentcondition = paymentcondition;
	}

	public String getCharactertype() {
		return charactertype;
	}

	public void setCharactertype(String charactertype) {
		this.charactertype = charactertype;
	}

	public String getLineamountam() {
		return lineamountam;
	}

	public void setLineamountam(String lineamountam) {
		this.lineamountam = lineamountam;
	}

	public String getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(String paidamount) {
		this.paidamount = paidamount;
	}

	public String getOffsetamount() {
		return offsetamount;
	}

	public void setOffsetamount(String offsetamount) {
		this.offsetamount = offsetamount;
	}

	public String getFapplynumber() {
		return fapplynumber;
	}

	public void setFapplynumber(String fapplynumber) {
		this.fapplynumber = fapplynumber;
	}

	public String getFollowup() {
		return followup;
	}

	public void setFollowup(String followup) {
		this.followup = followup;
	}

	public String getExpecteddate() {
		return expecteddate;
	}

	public void setExpecteddate(String expecteddate) {
		this.expecteddate = expecteddate;
	}

	public String getReturnconditions() {
		return returnconditions;
	}

	public void setReturnconditions(String returnconditions) {
		this.returnconditions = returnconditions;
	}

	public String getRefunddate() {
		return refunddate;
	}

	public void setRefunddate(String refunddate) {
		this.refunddate = refunddate;
	}

	public String getRefundamount() {
		return refundamount;
	}

	public void setRefundamount(String refundamount) {
		this.refundamount = refundamount;
	}

}
