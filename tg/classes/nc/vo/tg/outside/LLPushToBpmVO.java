package nc.vo.tg.outside;
/**
 * �������ﵥ����BPM������VO
 * @author ln
 *
 */
public class LLPushToBpmVO {
	public String pk_busibill;// ��ǰ����pk
	public String taskid;// BPM����
	public String command;// BPM�����������
	public String desbill;// Ŀ�굥��

	public String getPk_busibill() {
		return pk_busibill;
	}

	public void setPk_busibill(String pk_busibill) {
		this.pk_busibill = pk_busibill;
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

}
