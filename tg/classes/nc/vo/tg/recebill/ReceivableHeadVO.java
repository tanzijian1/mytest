package nc.vo.tg.recebill;

import java.io.Serializable;
/**
 * ��ϵͳӦ�յ�
 * @author lyq
 *
 */
public class ReceivableHeadVO implements Serializable{
		private String org;//������֯
		private String dept;//�����˲���
		private String operator;//������
		private Integer objtype;//��������
		private String customer;//�ͻ�/��Ӧ��
		private String checktype;//Ʊ������
		private String srcsid;//��ϵͳ��Դ����id
		private String srcbillcode;//��ϵͳ��Դ���ݱ��
		private String imagestatus;//Ӱ��״̬
		private String imagecode;//Ӱ�����
		private String contractcode;//��ͬ����
		private String contractname;//��ͬ����
		private String contractsubclass;//��ͬϸ��
		private String emergency;//�����̶�
		private String srcsysname;//��ϵͳ����
		private float totalamount;//Ӧ�ս��ϼ�
		private String project;//��Ŀ
		private String creator;//������
		
		
		public String getCreator() {
			return creator;
		}
		public void setCreator(String creator) {
			this.creator = creator;
		}
		public String getOrg() {
			return org;
		}
		public void setOrg(String org) {
			this.org = org;
		}
		public String getDept() {
			return dept;
		}
		public void setDept(String dept) {
			this.dept = dept;
		}
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
		}
		public Integer getObjtype() {
			return objtype;
		}
		public void setObjtype(Integer objtype) {
			this.objtype = objtype;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		public String getChecktype() {
			return checktype;
		}
		public void setChecktype(String checktype) {
			this.checktype = checktype;
		}
		public String getSrcsid() {
			return srcsid;
		}
		public void setSrcsid(String srcsid) {
			this.srcsid = srcsid;
		}
		public String getSrcbillcode() {
			return srcbillcode;
		}
		public void setSrcbillcode(String srcbillcode) {
			this.srcbillcode = srcbillcode;
		}
		public String getImagestatus() {
			return imagestatus;
		}
		public void setImagestatus(String imagestatus) {
			this.imagestatus = imagestatus;
		}
		public String getImagecode() {
			return imagecode;
		}
		public void setImagecode(String imagecode) {
			this.imagecode = imagecode;
		}
		public String getContractcode() {
			return contractcode;
		}
		public void setContractcode(String contractcode) {
			this.contractcode = contractcode;
		}
		public String getContractname() {
			return contractname;
		}
		public void setContractname(String contractname) {
			this.contractname = contractname;
		}
		public String getContractsubclass() {
			return contractsubclass;
		}
		public void setContractsubclass(String contractsubclass) {
			this.contractsubclass = contractsubclass;
		}
		public String getEmergency() {
			return emergency;
		}
		public void setEmergency(String emergency) {
			this.emergency = emergency;
		}
		public String getSrcsysname() {
			return srcsysname;
		}
		public void setSrcsysname(String srcsysname) {
			this.srcsysname = srcsysname;
		}
		public float getTotalamount() {
			return totalamount;
		}
		public void setTotalamount(float totalamount) {
			this.totalamount = totalamount;
		}
		public String getProject() {
			return project;
		}
		public void setProject(String project) {
			this.project = project;
		}
		
		
		
		
		
}
