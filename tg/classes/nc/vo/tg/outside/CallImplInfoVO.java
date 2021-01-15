package nc.vo.tg.outside;

import java.io.Serializable;
import java.util.Map;

public class CallImplInfoVO implements Serializable{
	private String className;// 接口执行类
	private String urls;// 调用地址
	private String token;// 密钥
	private String postdata;// 传参信息
	Map<String, Object> other;// 其它信息
	String dessystem;// 目标系统
	String isrequiresnew;// 是否走子线程

	public String getIsrequiresnew() {
		return isrequiresnew;
	}

	public void setIsrequiresnew(String isrequiresnew) {
		this.isrequiresnew = isrequiresnew;
	}

	public String getDessystem() {
		return dessystem;
	}

	public void setDessystem(String dessystem) {
		this.dessystem = dessystem;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPostdata() {
		return postdata;
	}

	public void setPostdata(String postdata) {
		this.postdata = postdata;
	}

	public Map<String, Object> getOther() {
		return other;
	}

	public void setOther(Map<String, Object> other) {
		this.other = other;
	}
}
