package nc.vo.tg.outside;

import nc.vo.bd.defdoc.DefdocVO;

public class LLDefdocVO extends DefdocVO {
	private String srcid;// 外系统来源单据id
	private String parent_id;
	private String parent_code;
	private String parent_name;

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public String getSrcid() {
		return srcid;
	}

	public void setSrcid(String srcid) {
		this.srcid = srcid;
	}

}
