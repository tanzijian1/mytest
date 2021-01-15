package nc.vo.tg.outside;

import java.io.Serializable;

public class SaleProjectVO implements Serializable{
  private String epscode;
  private String project_code;
  private String project_name;
  private String enablestate;
  private String memo;
  private String def2;
public String getEpscode() {
	return epscode;
}
public void setEpscode(String epscode) {
	this.epscode = epscode;
}
public String getProject_code() {
	return project_code;
}
public void setProject_code(String project_code) {
	this.project_code = project_code;
}
public String getProject_name() {
	return project_name;
}
public void setProject_name(String project_name) {
	this.project_name = project_name;
}
public String getEnablestate() {
	return enablestate;
}
public void setEnablestate(String enablestate) {
	this.enablestate = enablestate;
}
public String getMemo() {
	return memo;
}
public void setMemo(String memo) {
	this.memo = memo;
}
public String getDef2() {
	return def2;
}
public void setDef2(String def2) {
	this.def2 = def2;
}
}
