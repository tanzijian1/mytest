package nc.vo.tg.outside;

import java.io.Serializable;

public class SaleResultVO implements Serializable {
	public String rsmstate;
	public String msg;
	public String data;

	public String getRsmstate() {
		return rsmstate;
	}

	public void setRsmstate(String rsmstate) {
		this.rsmstate = rsmstate;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
