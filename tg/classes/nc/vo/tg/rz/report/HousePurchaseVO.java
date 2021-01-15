package nc.vo.tg.rz.report;

import nc.vo.pub.lang.UFDouble;

/**
 * 购房尾款VO
 * @author wenjie
 *
 */
public class HousePurchaseVO {
	private String projectID;//项目ID
	private String projectFCODE;//项目FCODE
	private String citycorp;//城市公司
	private String orderID;//订单ID
	private String projectname;//项目名称
	private String bigprojectname;//大项目名称
	private String buildingname;//楼栋名称
	private String unit;//单元
	private String room;//房间
	private String producttype;//产品类型
	private String structurearea;//建筑面积
	private String insidespace;//套内面积
	private String subscriptionno;//认购证号
	private String agencycompany;//代理公司
	private String customername;//客户名称
	private String phonenumber;//电话号码
	private String status;//状态
	private String subscriptiondate;//认购日期
	private String contractdate;//签约日期
	private String unsigneddays;//超期未签约天数
	private UFDouble installmentamount;//按揭金额
	private UFDouble reserveamount;//公积金
	private String paymentmethod;//付款方式
	private UFDouble contractamount;//合同总价
	private UFDouble fitmentamount;//装修总价
	private String businessdate;//业务办理日期
	private String fundname;//款项名称
	private String paymentdate;//付款期限
	private String exceeddays;//超期付款天数
	private UFDouble receivableamount;//应收金额
	private UFDouble receivedamount;//已收金额
	private String receiptdate;//实际收款日期
	private UFDouble remainingamount;//余额
	private String installmentbank;//按揭银行
	private String installmentlimit;//按揭年限
	private String address;//地址
	private String clientele1;//客户1
	private String mobile1;//手机1
	private String officephone1;//办公电话1
	private String homephone1;//住宅电话1
	private String address1;//地址1
	private String clientele2;//客户2    
	private String mobile2;//手机2    
	private String officephone2;//办公电话2  
	private String homephone2;//住宅电话2  
	private String address2;//地址2    
	private String clientele3;//客户3
	private String clientele4;//客户4
	private String operater;//业务员
	private String correlateoperater;//相关业务员
	private String iscollect;//是否收齐
	private String notreceived;//未收齐资料
	private String installmentstatus;//按揭状态
	private String completedate;//完成日期
	private String predictinstalldate;//预计按揭完成日期
	private String predictreceiptdate;//预计收款日期
	private String isloan;//是否放款
	private String notsignreason;//超期未签约原因
	private String predicsigndate;//预计签约日期
	private String approver;//审核人
	private String housetype;//购房类型
	private String roomspecialstatus;//房间特殊状态
	private String contractcode;//合同编号
	private String contractrecord;//合同备案号
	private String contractregiststatus;//合同登记情况
	private String contractregistdate;//合同登记办理日期
	private UFDouble paidamount;//已付借款
	private UFDouble debtamount;//借款欠款
	private UFDouble supplementamount;//补充协议价
	private UFDouble paidfitmentamount;//已付装修款
	private String contracttype;//合同类型
	private String presellcarddate;//取得预售证日期
	private String carportdate;//车位确权日期
	private String netsigndate;//网签日期
	private String loandate;//达到待放款时间
	private String installmentdate;//按揭放款时间
	private String sentdate;//资料齐已送件日期
	private String follower;//跟进人
	private String operatorgroup;//业务员组别
	private String funddetail;//款项进程明细
	private String transactdate;//进程办理日期
	private String accumulationstatus;//公积金状态
	private String roomid;//ROOMID
	private String qkreason;//QKREASON
	private String lastpaymentdate;//最后一次付款时间
	private String idcard;//身份证号码
	private String salemodel;//项目销售模式
	private String istransact;//是否办理特殊业务
	private UFDouble firstpayamount;//首付金额
	private String firstpayscale;//首付比例
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	public String getProjectFCODE() {
		return projectFCODE;
	}
	public void setProjectFCODE(String projectFCODE) {
		this.projectFCODE = projectFCODE;
	}
	public String getCitycorp() {
		return citycorp;
	}
	public void setCitycorp(String citycorp) {
		this.citycorp = citycorp;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getBigprojectname() {
		return bigprojectname;
	}
	public void setBigprojectname(String bigprojectname) {
		this.bigprojectname = bigprojectname;
	}
	public String getBuildingname() {
		return buildingname;
	}
	public void setBuildingname(String buildingname) {
		this.buildingname = buildingname;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getProducttype() {
		return producttype;
	}
	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}
	public String getStructurearea() {
		return structurearea;
	}
	public void setStructurearea(String structurearea) {
		this.structurearea = structurearea;
	}
	public String getInsidespace() {
		return insidespace;
	}
	public void setInsidespace(String insidespace) {
		this.insidespace = insidespace;
	}
	public String getSubscriptionno() {
		return subscriptionno;
	}
	public void setSubscriptionno(String subscriptionno) {
		this.subscriptionno = subscriptionno;
	}
	public String getAgencycompany() {
		return agencycompany;
	}
	public void setAgencycompany(String agencycompany) {
		this.agencycompany = agencycompany;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubscriptiondate() {
		return subscriptiondate;
	}
	public void setSubscriptiondate(String subscriptiondate) {
		this.subscriptiondate = subscriptiondate;
	}
	public String getContractdate() {
		return contractdate;
	}
	public void setContractdate(String contractdate) {
		this.contractdate = contractdate;
	}
	public String getUnsigneddays() {
		return unsigneddays;
	}
	public void setUnsigneddays(String unsigneddays) {
		this.unsigneddays = unsigneddays;
	}
	public UFDouble getInstallmentamount() {
		return installmentamount;
	}
	public void setInstallmentamount(UFDouble installmentamount) {
		this.installmentamount = installmentamount;
	}
	public UFDouble getReserveamount() {
		return reserveamount;
	}
	public void setReserveamount(UFDouble reserveamount) {
		this.reserveamount = reserveamount;
	}
	public String getPaymentmethod() {
		return paymentmethod;
	}
	public void setPaymentmethod(String paymentmethod) {
		this.paymentmethod = paymentmethod;
	}
	public UFDouble getContractamount() {
		return contractamount;
	}
	public void setContractamount(UFDouble contractamount) {
		this.contractamount = contractamount;
	}
	public UFDouble getFitmentamount() {
		return fitmentamount;
	}
	public void setFitmentamount(UFDouble fitmentamount) {
		this.fitmentamount = fitmentamount;
	}
	public String getBusinessdate() {
		return businessdate;
	}
	public void setBusinessdate(String businessdate) {
		this.businessdate = businessdate;
	}
	public String getFundname() {
		return fundname;
	}
	public void setFundname(String fundname) {
		this.fundname = fundname;
	}
	public String getPaymentdate() {
		return paymentdate;
	}
	public void setPaymentdate(String paymentdate) {
		this.paymentdate = paymentdate;
	}
	public String getExceeddays() {
		return exceeddays;
	}
	public void setExceeddays(String exceeddays) {
		this.exceeddays = exceeddays;
	}
	public UFDouble getReceivableamount() {
		return receivableamount;
	}
	public void setReceivableamount(UFDouble receivableamount) {
		this.receivableamount = receivableamount;
	}
	public UFDouble getReceivedamount() {
		return receivedamount;
	}
	public void setReceivedamount(UFDouble receivedamount) {
		this.receivedamount = receivedamount;
	}
	public String getReceiptdate() {
		return receiptdate;
	}
	public void setReceiptdate(String receiptdate) {
		this.receiptdate = receiptdate;
	}
	public UFDouble getRemainingamount() {
		return remainingamount;
	}
	public void setRemainingamount(UFDouble remainingamount) {
		this.remainingamount = remainingamount;
	}
	public String getInstallmentbank() {
		return installmentbank;
	}
	public void setInstallmentbank(String installmentbank) {
		this.installmentbank = installmentbank;
	}
	public String getInstallmentlimit() {
		return installmentlimit;
	}
	public void setInstallmentlimit(String installmentlimit) {
		this.installmentlimit = installmentlimit;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getClientele1() {
		return clientele1;
	}
	public void setClientele1(String clientele1) {
		this.clientele1 = clientele1;
	}
	public String getMobile1() {
		return mobile1;
	}
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}
	public String getOfficephone1() {
		return officephone1;
	}
	public void setOfficephone1(String officephone1) {
		this.officephone1 = officephone1;
	}
	public String getHomephone1() {
		return homephone1;
	}
	public void setHomephone1(String homephone1) {
		this.homephone1 = homephone1;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getClientele2() {
		return clientele2;
	}
	public void setClientele2(String clientele2) {
		this.clientele2 = clientele2;
	}
	public String getMobile2() {
		return mobile2;
	}
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	public String getOfficephone2() {
		return officephone2;
	}
	public void setOfficephone2(String officephone2) {
		this.officephone2 = officephone2;
	}
	public String getHomephone2() {
		return homephone2;
	}
	public void setHomephone2(String homephone2) {
		this.homephone2 = homephone2;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getClientele3() {
		return clientele3;
	}
	public void setClientele3(String clientele3) {
		this.clientele3 = clientele3;
	}
	public String getClientele4() {
		return clientele4;
	}
	public void setClientele4(String clientele4) {
		this.clientele4 = clientele4;
	}
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public String getCorrelateoperater() {
		return correlateoperater;
	}
	public void setCorrelateoperater(String correlateoperater) {
		this.correlateoperater = correlateoperater;
	}
	public String getIscollect() {
		return iscollect;
	}
	public void setIscollect(String iscollect) {
		this.iscollect = iscollect;
	}
	public String getNotreceived() {
		return notreceived;
	}
	public void setNotreceived(String notreceived) {
		this.notreceived = notreceived;
	}
	public String getInstallmentstatus() {
		return installmentstatus;
	}
	public void setInstallmentstatus(String installmentstatus) {
		this.installmentstatus = installmentstatus;
	}
	public String getCompletedate() {
		return completedate;
	}
	public void setCompletedate(String completedate) {
		this.completedate = completedate;
	}
	public String getPredictinstalldate() {
		return predictinstalldate;
	}
	public void setPredictinstalldate(String predictinstalldate) {
		this.predictinstalldate = predictinstalldate;
	}
	public String getPredictreceiptdate() {
		return predictreceiptdate;
	}
	public void setPredictreceiptdate(String predictreceiptdate) {
		this.predictreceiptdate = predictreceiptdate;
	}
	public String getIsloan() {
		return isloan;
	}
	public void setIsloan(String isloan) {
		this.isloan = isloan;
	}
	public String getNotsignreason() {
		return notsignreason;
	}
	public void setNotsignreason(String notsignreason) {
		this.notsignreason = notsignreason;
	}
	public String getPredicsigndate() {
		return predicsigndate;
	}
	public void setPredicsigndate(String predicsigndate) {
		this.predicsigndate = predicsigndate;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getHousetype() {
		return housetype;
	}
	public void setHousetype(String housetype) {
		this.housetype = housetype;
	}
	public String getRoomspecialstatus() {
		return roomspecialstatus;
	}
	public void setRoomspecialstatus(String roomspecialstatus) {
		this.roomspecialstatus = roomspecialstatus;
	}
	public String getContractcode() {
		return contractcode;
	}
	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}
	public String getContractrecord() {
		return contractrecord;
	}
	public void setContractrecord(String contractrecord) {
		this.contractrecord = contractrecord;
	}
	public String getContractregiststatus() {
		return contractregiststatus;
	}
	public void setContractregiststatus(String contractregiststatus) {
		this.contractregiststatus = contractregiststatus;
	}
	public String getContractregistdate() {
		return contractregistdate;
	}
	public void setContractregistdate(String contractregistdate) {
		this.contractregistdate = contractregistdate;
	}
	public UFDouble getPaidamount() {
		return paidamount;
	}
	public void setPaidamount(UFDouble paidamount) {
		this.paidamount = paidamount;
	}
	public UFDouble getDebtamount() {
		return debtamount;
	}
	public void setDebtamount(UFDouble debtamount) {
		this.debtamount = debtamount;
	}
	public UFDouble getSupplementamount() {
		return supplementamount;
	}
	public void setSupplementamount(UFDouble supplementamount) {
		this.supplementamount = supplementamount;
	}
	public UFDouble getPaidfitmentamount() {
		return paidfitmentamount;
	}
	public void setPaidfitmentamount(UFDouble paidfitmentamount) {
		this.paidfitmentamount = paidfitmentamount;
	}
	public String getContracttype() {
		return contracttype;
	}
	public void setContracttype(String contracttype) {
		this.contracttype = contracttype;
	}
	public String getPresellcarddate() {
		return presellcarddate;
	}
	public void setPresellcarddate(String presellcarddate) {
		this.presellcarddate = presellcarddate;
	}
	public String getCarportdate() {
		return carportdate;
	}
	public void setCarportdate(String carportdate) {
		this.carportdate = carportdate;
	}
	public String getNetsigndate() {
		return netsigndate;
	}
	public void setNetsigndate(String netsigndate) {
		this.netsigndate = netsigndate;
	}
	public String getLoandate() {
		return loandate;
	}
	public void setLoandate(String loandate) {
		this.loandate = loandate;
	}
	public String getInstallmentdate() {
		return installmentdate;
	}
	public void setInstallmentdate(String installmentdate) {
		this.installmentdate = installmentdate;
	}
	public String getSentdate() {
		return sentdate;
	}
	public void setSentdate(String sentdate) {
		this.sentdate = sentdate;
	}
	public String getFollower() {
		return follower;
	}
	public void setFollower(String follower) {
		this.follower = follower;
	}
	public String getOperatorgroup() {
		return operatorgroup;
	}
	public void setOperatorgroup(String operatorgroup) {
		this.operatorgroup = operatorgroup;
	}
	public String getFunddetail() {
		return funddetail;
	}
	public void setFunddetail(String funddetail) {
		this.funddetail = funddetail;
	}
	public String getTransactdate() {
		return transactdate;
	}
	public void setTransactdate(String transactdate) {
		this.transactdate = transactdate;
	}
	public String getAccumulationstatus() {
		return accumulationstatus;
	}
	public void setAccumulationstatus(String accumulationstatus) {
		this.accumulationstatus = accumulationstatus;
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getQkreason() {
		return qkreason;
	}
	public void setQkreason(String qkreason) {
		this.qkreason = qkreason;
	}
	public String getLastpaymentdate() {
		return lastpaymentdate;
	}
	public void setLastpaymentdate(String lastpaymentdate) {
		this.lastpaymentdate = lastpaymentdate;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getSalemodel() {
		return salemodel;
	}
	public void setSalemodel(String salemodel) {
		this.salemodel = salemodel;
	}
	public String getIstransact() {
		return istransact;
	}
	public void setIstransact(String istransact) {
		this.istransact = istransact;
	}
	public UFDouble getFirstpayamount() {
		return firstpayamount;
	}
	public void setFirstpayamount(UFDouble firstpayamount) {
		this.firstpayamount = firstpayamount;
	}
	public String getFirstpayscale() {
		return firstpayscale;
	}
	public void setFirstpayscale(String firstpayscale) {
		this.firstpayscale = firstpayscale;
	}
	
}
