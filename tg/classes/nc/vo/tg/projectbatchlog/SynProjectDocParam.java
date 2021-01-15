package nc.vo.tg.projectbatchlog;

public class SynProjectDocParam {
	private Integer actioncode;
	private String billtype;
	private String epscode;
	private String pk_projectbatchlog;
	private ProjectDocParam[] data;

	public SynProjectDocParam() {
		super();
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public ProjectDocParam[] getData() {
		return data;
	}

	public void setData(ProjectDocParam[] data) {
		this.data = data;
	}

	public String getEpscode() {
		return epscode;
	}

	public void setEpscode(String epscode) {
		this.epscode = epscode;
	}

	public Integer getActioncode() {
		return actioncode;
	}

	public void setActioncode(Integer actioncode) {
		this.actioncode = actioncode;
	}

	public String getPk_projectbatchlog() {
		return pk_projectbatchlog;
	}

	public void setPk_projectbatchlog(String pk_projectbatchlog) {
		this.pk_projectbatchlog = pk_projectbatchlog;
	}

}
