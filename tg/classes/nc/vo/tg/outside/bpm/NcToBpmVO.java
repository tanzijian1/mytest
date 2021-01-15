package nc.vo.tg.outside.bpm;

import nc.vo.pub.SuperVO;

public class NcToBpmVO extends SuperVO {
	private static final long serialVersionUID = 1L;

	public String pk_busibill;// 当前单据pk
	public String approvaltype;// 业务请求类型 --“ProcessBack”： NC系统单据核对不通过，退回BPM系统流程;
								// “ProcessApproved”:NC系统付款结束通知BPM系统流程归档，此时OperationType只能为“none”
								// "back":推送bpm后，bpm审批前收回通知bpm
								// "delete":NC删单后通知bpm
								// "reject":集团财务退回BPM按钮使用
	public String operationtype;// NC逆向流程操作类型
								// --“back”：NC系统退回BPM单据到地区财务节点;“none”：流程审批通过
	public String taskid;// BPM主键
	public String command;// BPM流程审批意见
	public String desbill;// 目标单据
	public String comments;// 删除原因
	public String isOperInNC;// 是否在NC中操作，用于判断是接口异常调用删除还是NC前台调用

	public String getIsOperInNC() {
		return isOperInNC;
	}

	public void setIsOperInNC(String isOperInNC) {
		this.isOperInNC = isOperInNC;
	}

	public String getPk_busibill() {
		return pk_busibill;
	}

	public void setPk_busibill(String pk_busibill) {
		this.pk_busibill = pk_busibill;
	}

	public String getApprovaltype() {
		return approvaltype;
	}

	public void setApprovaltype(String approvaltype) {
		this.approvaltype = approvaltype;
	}

	public String getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDesbill() {
		return desbill;
	}

	public void setDesbill(String desbill) {
		this.desbill = desbill;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
