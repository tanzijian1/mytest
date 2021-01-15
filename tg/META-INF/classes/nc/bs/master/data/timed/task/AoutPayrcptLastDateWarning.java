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
 * ��̨�Զ�������Ϣ�������,�ϼ�������,�������ĸ����˷ſ����ǰ������֪ͨ
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
		String title = "�ſ��������Ϣ";
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		List<Object[]> reflist = getWorkResult(bgwc);
		util.setRemsg(new TGInterestMessage());
		return util.executeTask(title, reflist);
	}
	
	
	public List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = null;
		
		
		try {
			msglist = execute();//������Ϣ�������
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
		logVO.setSrcsystem("NC�ڲ����ͷſ��Ԥ����Ϣ");
		logVO.setDesbill("�ſ��Ԥ����Ϣ");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());

		
		//��ȡ���з��������ĺ�ͬ��Ϣ
		List<Map<String, Object>> contractList = getContractVOs();
		int i = 0;
		String[] msgObj = new String[2];
		if(contractList!=null){
			if(contractList.size()>0){
				for (Map<String, Object> map : contractList) {
					if(map.get("paydate")!=null){
						Calendar cal = Calendar.getInstance();
						cal.setTime(new UFDate((String) map.get("paydate")).toDate());//������ʼʱ��
						cal.add(Calendar.MONTH, -3);//�������ڵ�ǰ������
						if(new UFDate().asBegin().compareTo(new UFDate(cal.getTime()).asBegin())==0){
							msgObj[0] = (String) map.get("contractcode");
							logVO.setPrimaryKey((String) map.get("contractcode"));
							logVO.setSrcparm(map.toString());
							try {
								pushMessage(map);//������Ϣ�������
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
			logVO.setErrmsg("û�ҵ������ͬ��ϸ��Ӧ�ſ�ƻ��ĵ��ڼ�¼");
			service.saveLog_RequiresNew(logVO);
		}
		
		return msglist;

	}



	private void pushMessage(Map<String, Object> map) throws Exception {
		 List<NCMessage>  ncMessageList=new  ArrayList<NCMessage>();
		 Set<String> users = new HashSet<String>();
		 String projectname = (String) map.get("projectname");//��Ŀ
		 String organizationname = (String) map.get("organizationname");//���ʻ���
		 String finname = (String) map.get("finname");//��������
		 String paydate = (String) map.get("paydate");//�ſ������
		 String payamount = map.get("payamount").toString();//���
		 String htmc = (String) map.get("htmc");//��ͬ����
		 
		 if(map.get("vdef4")!=null){
			 users.add((String) map.get("vdef4"));//�ϼ�������
		 }
		 if(map.get("def69")!=null){
			 users.add((String) map.get("def69"));//���������
		 }
		 if(map.get("def72")!=null){
			 users.add((String) map.get("def72"));//�������ĸ�����
		 }
		 if(users!=null){
			 if(users.size()>0){
				 for (String user : users){
					 NCMessage ncMessage = new NCMessage();
					 MessageVO messageVO = new MessageVO();
					 messageVO.setBillid(null);// ����ID
					 messageVO.setBilltype(null);// ��������
					 messageVO.setContent("��Ŀ��"+projectname+"��"+"\n���ʻ�����"+""+organizationname+"��"+"\n�������ͣ�"+""+finname+"��"+"\n�������ڣ�"+""+paydate+"���ڣ�"+"\n��"+payamount);// ����
					 messageVO.setContenttype("text/plain");// ���ݸ�ʽ
					 messageVO.setDestination("outbox");// ���λ��
					 messageVO.setDomainflag("CDM");// ������
					 messageVO.setDr(0);//
					 messageVO.setExpiration(null);// ��Ч��
					 // messageVO.setISACTIVED 1
					 messageVO.setIsdelete(UFBoolean.FALSE);// �Ƿ�ɾ��
					 messageVO.setIshandled(UFBoolean.FALSE);// �Ƿ��Ѵ���
					 messageVO.setIsread(UFBoolean.FALSE);// �Ƿ��Ѷ�
					 messageVO.setMsgsendpk(null);// ������Ϣ������
					 messageVO.setMsgsourcetype("worklist");// ��Ϣ��Դ����
					 messageVO.setMsgtype("nc");// ��Ϣ��������
					 messageVO.setPk_colorkey(null);
					 messageVO.setPk_group(AppContext.getInstance().getPkGroup());// ����
					 messageVO.setPk_message(null);// ����
					 messageVO.setPk_org(null);// ��֯
					 messageVO.setPriority(5);// ���ȼ�
					 messageVO.setReceipt(null);// ��ִ
					 messageVO.setReceiver(getPsnInfo(user)==null?null:getPsnInfo(user).get("cuserid"));//������
//					 messageVO.setReceiver("100112100000000004NC");//�����û�id
					 messageVO.setResendtimes(0);// �ط�����
					 messageVO.setSender("NC_USER0000000000000");// ������
					 messageVO.setSendstate(UFBoolean.TRUE);// �Ƿ��ͳɹ�
					 messageVO.setSendtime(new UFDateTime());// ����ʱ��
					 messageVO.setSubcolor("~");// ������ɫ
					 messageVO.setSubject(htmc+paydate+"����3���¼������ڣ�����д���ڷ�����");// ����
					 messageVO.setTs(null);// ʱ���
					 ncMessage.setMessage(messageVO);
					 ncMessageList.add(ncMessage);
				 }
				 
			 }
		 }
		MessageCenter.sendMessage(ncMessageList.toArray( new NCMessage[0]));
	}


	/**
	 * ��ȡ��ͬ�ֶ�
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getContractVOs() throws BusinessException {
		StringBuffer sql = new StringBuffer();

		sql.append(" select  ");
		sql.append(" cdm_contract.contractcode, ");//��ͬ���
		sql.append(" cdm_contract.htmc, ");//��ͬ����
		sql.append(" cdm_contract.vdef4, ");//�ϼ�������
		sql.append(" cdm_contract.def69, ");//���������
		sql.append(" cdm_contract.def72, ");//�������ĸ�����
		sql.append(" substr(cdm_payplan.vdef2,1,10) paydate, ");//���ſ�����
		sql.append(" nvl(cdm_payplan.payamount,0) payamount, ");//�ſ���
		sql.append(" tgrz_projectdata.name projectname, ");//��Ŀ����
		sql.append(" tgrz_organization.name organizationname, ");//���ʻ�������
		sql.append(" tgrz_fintype.name finname ");//������������
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
	 * ������Ա����pk��ȡ�û���Ϣ
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
