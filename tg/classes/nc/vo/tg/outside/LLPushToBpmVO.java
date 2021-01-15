package nc.vo.tg.outside;
/**
 * 用于邻里单据与BPM交互的VO
 * @author ln
 *
 */
public class LLPushToBpmVO {
	public String pk_busibill;// 当前单据pk
	public String taskid;// BPM主键
	public String command;// BPM流程审批意见
	public String desbill;// 目标单据

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
