package nc.bs.tg.alter.plugin.ebs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.fip.operatinglogs.OperatingLogVO;
import nc.vo.org.AccountingBookVO;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.voucher.fip.CombinschemeVO;

public class SalePayRecMergeTask implements IBackgroundWorkPlugin {
private BaseDAO dao=new BaseDAO();
ISqlThread service=NCLocator.getInstance().lookup(ISqlThread.class);
	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		// TODO 自动生成的方法存根
		OutsideLogVO ologvo=new OutsideLogVO();
		ologvo.setDesbill("转备案生成凭证");
		ologvo.setResult("0");
		ISqlThread sql=NCLocator.getInstance().lookup(ISqlThread.class);
		try{
		Map<String, ArrayList<AccountingBookVO>> bookMap =VoucherTaskUtils.newInstance().getOrgInSetofbook();
		LinkedHashMap<String, List<OperatingLogVO>> normalMap =getNormalRuleLogMap(bookMap);
		List<String[]> msgobjs= onMergerVoucher(normalMap);
		ologvo.setSrcparm(JSON.toJSONString(msgobjs));
		}catch(Exception e){
			ologvo.setErrmsg(e.getMessage());
			throw new BusinessException(e.getMessage());
		}finally{
			sql.billInsert_RequiresNew(ologvo);
		}
		return null;
		}
	
	/**
	 * 常规操作处理
	 * 
	 * @param bookMap
	 * @return
	 * @throws BusinessException
	 */
	private LinkedHashMap<String, List<OperatingLogVO>> getNormalRuleLogMap(
			Map<String, ArrayList<AccountingBookVO>> bookMap)
			throws BusinessException {
		// 3.凭证处理操作信息
	 OperatingLogVO[] logVOs =VoucherTaskUtils.newInstance().getRealTimeOperatingLogVOs();
	 if (logVOs != null && logVOs.length > 0) {
			ArrayList<String> locklist = new ArrayList<String>();// 加锁，防止多线程冲突
			try {
				LinkedHashMap<String, List<OperatingLogVO>> ruleLogMap = new LinkedHashMap<String, List<OperatingLogVO>>();
				S1: for (OperatingLogVO logvo : logVOs) {
					// 获取分组规则
					String key=getGroupRuleKey(logvo);
					
					if (key == null) {
						continue S1;
					}
					
					// 按规则分组信息
					if (ruleLogMap.get(key) == null){
						ruleLogMap.put(key, new ArrayList<OperatingLogVO>());
					}
					
					ruleLogMap.get(key).add(logvo);

				}
				return ruleLogMap;
				}catch(Exception e){
					throw new BusinessException(e.getMessage(), e);
				}
	 }
	 return null;
	}	
	
public String getGroupRuleKey(OperatingLogVO logvo) throws DAOException{
	String key=null;
	if("F2-Cxx-001".equals(logvo.getSrc_billtype())){
	   key=(String)dao.executeQuery("select def39 from ar_gatherbill where pk_gatherbill='"+logvo.getSrc_relationid()+"'", new ColumnProcessor());
	}else if("F3-Cxx-001".equals(logvo.getSrc_billtype())){
		   key=(String)dao.executeQuery("select def80 from ap_paybill where pk_paybill='"+logvo.getSrc_relationid()+"'", new ColumnProcessor());
	}
	return key;
}
/**
 * 得到最后审批人
 * @param volist
 * @return
 * @throws DAOException
 */
public String getuser(List<OperatingLogVO> volist) throws DAOException{
	String pk=null;//收款单pk
    for(OperatingLogVO vo:volist){
    	if("F2-Cxx-001".equals(vo.getSrc_billtype())){
    		pk=vo.getSrc_relationid();
    	}
    }
   String approver=(String) dao.executeQuery("select approver  from ar_gatherbill where pk_gatherbill='"+pk+"'", new ColumnProcessor());
	return approver;
}
/**
 * 合并凭证处理结果
 * 
 * @param ruleLogMap
 * @return
 * @throws BusinessException
 */
private List<String[]> onMergerVoucher(LinkedHashMap<String, List<OperatingLogVO>> ruleLoglistMap) 
		throws BusinessException {
	String[] msgObj = new String[9];
	String 	vouchermsg =null;
	String voucherdate=null;
	String msg=null;
	String operation = "0";//操作成功标志
	List<String[]> msgList = new ArrayList<>();
	String  pk_combinscheme=(String)dao.executeQuery("select pk_combinscheme from gl_combinscheme where code='YC001'",new ColumnProcessor());
	if(pk_combinscheme==null||pk_combinscheme.length()<1)throw new BusinessException("合并规则为空");
	for (String key : ruleLoglistMap.keySet()) {
		try{
		List<OperatingLogVO> volist=ruleLoglistMap.get(key);
		String approver=getuser(volist);//收款单最后审批人为凭证制单人
		service.genMergeGL_RequiresNew(volist, pk_combinscheme,approver);
		Map<String, String> voucherMap = VoucherTaskUtils.newInstance().getVoucherMsg(volist);
	 	vouchermsg = voucherMap.get("mergevoucher");
		 voucherdate = voucherMap
				.get("mergevoucherdate");
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			msg = e.getClass().getName()+e.getMessage();
		}
		msgObj[0] = pk_combinscheme;// 合并规则
		msgObj[1] = vouchermsg;// 凭证信息
		msgObj[2] = voucherdate;// 生成时间
		msgObj[7] = operation;// 操作(合并)结果
		msgObj[8] = msg;// 失败反馈
		msgList.add(msgObj);
	}
	return msgList;
}
public void getData() throws DAOException{
	String sql=new String();
	List<PayBillVO>  payvolist=(List<PayBillVO>)dao.executeQuery("select * from ap_paybill ap where  nvl(ap.def80,'~')!='~' and nvl(ap.dr,0)=0 and ap.approvestatus ='1' and ap.pk_tradetype='F3-Cxx-001' ", new BeanListProcessor(PayBillVO.class));
	for(PayBillVO pbillvo:payvolist){
		String def80=pbillvo.getDef80();//转备案收付款单唯一标识
		GatheringBillVO gbillvo=(GatheringBillVO)dao.executeQuery("select * from ar_gatherbill ar where ar.pk_tradetype='F2-Cxx-001' and nvl(ar.dr,0)=0 and ar.approvestatus ='1' and def39='"+def80+"'", new BeanProcessor(GatheringBillVO.class));
		boolean ggl=checkgl(gbillvo.getPk_gatherbill());
		boolean pgl=checkgl(pbillvo.getPk_paybill());
		if(ggl&&pgl)continue;
		
	}
}
/**
 * 是否是正式凭证（true为是）
 * @param pk
 * @return
 * @throws DAOException
 */
public boolean checkgl(String pk) throws DAOException{
	 Integer voucherkind =(Integer)dao.executeQuery("select from gl_voucher inner join fip_relation on fip_relation.des_relationid=gl_voucher.pk_voucher  where fip_relation.src_relationid='"+pk+"'", new ColumnProcessor());
	return voucherkind==2;
}


}
