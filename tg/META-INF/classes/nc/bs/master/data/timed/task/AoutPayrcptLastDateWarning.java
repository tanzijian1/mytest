package nc.bs.master.data.timed.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.bs.tg.alter.result.TGInterestMessage;
import nc.bs.tg.outside.salebpm.utils.SyncSaleBPMBillStatesUtils;
import nc.bs.uap.lock.PKLock;
import nc.itf.cdm.contractbankcredit.IContractBankCreditService;
import nc.itf.tg.outside.ISaveLogService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.outside.OutsideLogVO;

//import nc.bs.tg.alter.plugin.ebs.result.EBSAlterMessage;

/**
 * 后台自动发送信息贷后管理,上级审批人,二级中心负责人放款到期日前三个月通知
 * 
 * @author tjl 2019-09-23
 * 
 */
public class AoutPayrcptLastDateWarning implements IBackgroundWorkPlugin {
	BaseDAO baseDAO = null;
	IContractBankCreditService maintain = null;

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		String title = "放款到期提醒信息";
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		List<Object[]> reflist = getWorkResult(bgwc);
		util.setRemsg(new TGInterestMessage());
		return util.executeTask(title, reflist);
	}
	
	
	public List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = null;
		
		
		try {
			msglist = execute();//推送消息中心入口
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		PKLock.getInstance().releaseDynamicLocks();
		bgwc.setLogStr(msglist.toString());
		return msglist;
	}

	private List<Object[]> execute() throws Exception {
		List<Object[]> msglist = new ArrayList<Object[]>();
		ISaveLogService service = NCLocator.getInstance().lookup(
				ISaveLogService.class);
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("NC内部推送放款到期预警信息");
		logVO.setDesbill("放款到期预警信息");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());

		
		//获取所有符合条件的合同信息
		List<Map<String, Object>> contractList = getContractVOs();
		int i = 0;
		String[] msgObj = new String[2];
		if(contractList!=null){
			if(contractList.size()>0){
				for (Map<String, Object> map : contractList) {
					if(map.get("paydate")!=null){
						Calendar cal = Calendar.getInstance();
						cal.setTime(new UFDate((String) map.get("paydate")).toDate());//设置起始时间
						cal.add(Calendar.MONTH, -3);//给定日期的前三个月
						if(new UFDate().asBegin().compareTo(new UFDate(cal.getTime()).asBegin())==0){
							msgObj[0] = (String) map.get("contractcode");
							logVO.setPrimaryKey((String) map.get("contractcode"));
							logVO.setSrcparm(map.toString());
							try {
								pushMessage(map);//推送消息中心入口
								i++;
							} catch (Exception e) {
								Logger.error(e.getMessage(), e);
								logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
								//add by tjl 2020-05-08
								logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
										.getFullStackTrace(e));
								msgObj[1] = e.getMessage();
							}finally{
								msglist.add(msgObj);
								service.saveLog_RequiresNew(logVO);
							}
						}else{
							continue;
						}
					}
				}
			}
		}
		if(i==0&&logVO.getErrmsg()!=null){
			logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
			logVO.setErrmsg("没找到贷款合同明细对应放款计划的到期记录");
			service.saveLog_RequiresNew(logVO);
		}
		
		return msglist;

	}



	private void pushMessage(Map<String, Object> map) throws Exception {
		 List<NCMessage>  ncMessageList=new  ArrayList<NCMessage>();
		 Set<String> users = new HashSet<String>();
		 String projectname = (String) map.get("projectname");//项目
		 String organizationname = (String) map.get("organizationname");//融资机构
		 String finname = (String) map.get("finname");//融资类型
		 String paydate = (String) map.get("paydate");//放款到期日期
		 String payamount = map.get("payamount").toString();//金额
		 String htmc = (String) map.get("htmc");//合同名称
		 
		 if(map.get("vdef4")!=null){
			 users.add((String) map.get("vdef4"));//上级审批人
		 }
		 if(map.get("def69")!=null){
			 users.add((String) map.get("def69"));//贷后管理人
		 }
		 if(map.get("def72")!=null){
			 users.add((String) map.get("def72"));//二级中心负责人
		 }
		 if(users!=null){
			 if(users.size()>0){
				 for (String user : users){
					 NCMessage ncMessage = new NCMessage();
					 MessageVO messageVO = new MessageVO();
					 messageVO.setBillid(null);// 单据ID
					 messageVO.setBilltype(null);// 单据类型
					 messageVO.setContent("项目："+projectname+"，"+"\n融资机构："+""+organizationname+"，"+"\n融资类型："+""+finname+"，"+"\n到期日期："+""+paydate+"到期，"+"\n金额："+payamount);// 内容
					 messageVO.setContenttype("text/plain");// 内容格式
					 messageVO.setDestination("outbox");// 存放位置
					 messageVO.setDomainflag("CDM");// 领域标记
					 messageVO.setDr(0);//
					 messageVO.setExpiration(null);// 有效期
					 // messageVO.setISACTIVED 1
					 messageVO.setIsdelete(UFBoolean.FALSE);// 是否被删除
					 messageVO.setIshandled(UFBoolean.FALSE);// 是否已处理
					 messageVO.setIsread(UFBoolean.FALSE);// 是否已读
					 messageVO.setMsgsendpk(null);// 发送消息的主键
					 messageVO.setMsgsourcetype("worklist");// 消息来源类型
					 messageVO.setMsgtype("nc");// 消息发送类型
					 messageVO.setPk_colorkey(null);
					 messageVO.setPk_group(AppContext.getInstance().getPkGroup());// 集团
					 messageVO.setPk_message(null);// 主键
					 messageVO.setPk_org(null);// 组织
					 messageVO.setPriority(5);// 优先级
					 messageVO.setReceipt(null);// 回执
					 messageVO.setReceiver(getPsnInfo(user)==null?null:getPsnInfo(user).get("cuserid"));//接收人
//					 messageVO.setReceiver("100112100000000004NC");//测试用户id
					 messageVO.setResendtimes(0);// 重发次数
					 messageVO.setSender("NC_USER0000000000000");// 发送人
					 messageVO.setSendstate(UFBoolean.TRUE);// 是否发送成功
					 messageVO.setSendtime(new UFDateTime());// 发送时间
					 messageVO.setSubcolor("~");// 标题颜色
					 messageVO.setSubject(htmc+paydate+"还有3个月即将到期，请填写到期方案。");// 标题
					 messageVO.setTs(null);// 时间戳
					 ncMessage.setMessage(messageVO);
					 ncMessageList.add(ncMessage);
				 }
				 
			 }
		 }
		MessageCenter.sendMessage(ncMessageList.toArray( new NCMessage[0]));
	}


	/**
	 * 获取合同字段
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getContractVOs() throws BusinessException {
		StringBuffer sql = new StringBuffer();

		sql.append(" select  ");
		sql.append(" cdm_contract.contractcode, ");//合同编号
		sql.append(" cdm_contract.htmc, ");//合同名称
		sql.append(" cdm_contract.vdef4, ");//上级审批人
		sql.append(" cdm_contract.def69, ");//贷后管理人
		sql.append(" cdm_contract.def72, ");//二级中心负责人
		sql.append(" substr(cdm_payplan.vdef2,1,10) paydate, ");//最后放款日期
		sql.append(" nvl(cdm_payplan.payamount,0) payamount, ");//放款金额
		sql.append(" tgrz_projectdata.name projectname, ");//项目名称
		sql.append(" tgrz_organization.name organizationname, ");//融资机构名称
		sql.append(" tgrz_fintype.name finname ");//融资类型名称
		sql.append(" from cdm_payplan  ");
		sql.append(" left join cdm_contract on cdm_contract.pk_contract = cdm_payplan.pk_contract and cdm_contract.dr = 0 ");
		sql.append(" left join tgrz_projectdata on cdm_contract.pk_project = tgrz_projectdata.pk_projectdata and tgrz_projectdata.dr = 0 ");
		sql.append(" left join tgrz_organization on  cdm_contract.pk_rzjg = tgrz_organization.pk_organization and tgrz_organization.dr = 0 ");
		sql.append(" left join tgrz_fintype on cdm_contract.pk_rzlx = tgrz_fintype.pk_fintype and tgrz_fintype.dr = 0 ");
		sql.append(" where cdm_payplan.dr = 0  ");
		sql.append(" and cdm_contract.contstatus in ('EXECUTING','FINISHED')  ");
//		sql.append(" and cdm_contract.contractcode = 'DK202003080433'  ");
		
		List<Map<String, Object>> infoMap = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		return infoMap;
	}
	
	/**
	 * 根据人员档案pk读取用户信息
	 * 
	 * @param pk_psndoc
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPsnInfo(String pk_psndoc)
			throws nc.vo.pub.BusinessException {
		String sql = "select cuserid,user_code,user_name from sm_user  where pk_psndoc = '"
				+ pk_psndoc + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}


	public IContractBankCreditService getMaintain() {
		if (maintain == null) {
			maintain = NCLocator.getInstance().lookup(
					IContractBankCreditService.class);
		}
		return maintain;
	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	

	

}
