package nc.vo.tg.outside;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;

public class EBSResultVO implements Serializable {
	public String rsmstate;
	public String msg;
	public Object data;


	
	

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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
