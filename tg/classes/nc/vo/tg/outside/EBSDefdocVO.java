package nc.vo.tg.outside;

import java.io.Serializable;

import nc.vo.pub.SuperVO;

public class EBSDefdocVO extends SuperVO implements Serializable{
	
	public String ebs_id;//EBS�Զ��嵵��id
	public String code;//�Զ��嵵������
	public String name;//�Զ��嵵������
	public String ebs_parent_id;//EBS�ϼ�����id
	public String parent_code;//�ϼ���������
	public String parent_name;//�ϼ���������
	public String memo;//��ע
	public String enablestate;//����״̬
	
	
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
	public String getEbs_id() {
		return ebs_id;
	}
	public void setEbs_id(String ebs_id) {
		this.ebs_id = ebs_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEbs_parent_id() {
		return ebs_parent_id;
	}
	public void setEbs_parent_id(String ebs_parent_id) {
		this.ebs_parent_id = ebs_parent_id;
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

	
}
