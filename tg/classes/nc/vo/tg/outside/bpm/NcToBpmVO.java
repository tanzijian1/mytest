package nc.vo.tg.outside.bpm;

import nc.vo.pub.SuperVO;

public class NcToBpmVO extends SuperVO {
	private static final long serialVersionUID = 1L;

	public String pk_busibill;// ��ǰ����pk
	public String approvaltype;// ҵ���������� --��ProcessBack���� NCϵͳ���ݺ˶Բ�ͨ�����˻�BPMϵͳ����;
								// ��ProcessApproved��:NCϵͳ�������֪ͨBPMϵͳ���̹鵵����ʱOperationTypeֻ��Ϊ��none��
								// "back":����bpm��bpm����ǰ�ջ�֪ͨbpm
								// "delete":NCɾ����֪ͨbpm
								// "reject":���Ų����˻�BPM��ťʹ��
	public String operationtype;// NC�������̲�������
								// --��back����NCϵͳ�˻�BPM���ݵ���������ڵ�;��none������������ͨ��
	public String taskid;// BPM����
	public String command;// BPM�����������
	public String desbill;// Ŀ�굥��
	public String comments;// ɾ��ԭ��
	public String isOperInNC;// �Ƿ���NC�в����������ж��ǽӿ��쳣����ɾ������NCǰ̨����

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
