package nc.vo.tg.outside;

import java.io.Serializable;
import java.util.Map;

public class CallImplInfoVO implements Serializable{
	private String className;// �ӿ�ִ����
	private String urls;// ���õ�ַ
	private String token;// ��Կ
	private String postdata;// ������Ϣ
	Map<String, Object> other;// ������Ϣ
	String dessystem;// Ŀ��ϵͳ
	String isrequiresnew;// �Ƿ������߳�

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
