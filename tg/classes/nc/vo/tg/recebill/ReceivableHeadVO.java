package nc.vo.tg.recebill;

import java.io.Serializable;
/**
 * 外系统应收单
 * @author lyq
 *
 */
public class ReceivableHeadVO implements Serializable{
		private String org;//财务组织
		private String dept;//经办人部门
		private String operator;//经办人
		private Integer objtype;//往来对象
		private String customer;//客户/供应商
		private String checktype;//票据类型
		private String srcsid;//外系统来源单据id
		private String srcbillcode;//外系统来源单据编号
		private String imagestatus;//影像状态
		private String imagecode;//影像编码
		private String contractcode;//合同编码
		private String contractname;//合同名称
		private String contractsubclass;//合同细类
		private String emergency;//紧急程度
		private String srcsysname;//外系统名称
		private float totalamount;//应收金额合计
		private String project;//项目
		private String creator;//创建人
		
		
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
