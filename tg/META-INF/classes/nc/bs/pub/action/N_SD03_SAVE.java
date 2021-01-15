package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.bs.tg.approvalpro.plugin.bpplugin.ApprovalproPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IApprovalproMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.ProgressCtrVO;

public class N_SD03_SAVE extends AbstractPfAction<AggApprovalProVO> {

	protected CompareAroundProcesser<AggApprovalProVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggApprovalProVO> processor = new CompareAroundProcesser<AggApprovalProVO>(
				ApprovalproPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggApprovalProVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggApprovalProVO[] processBP(Object userObj,
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills) {
		IApprovalproMaintain operator = NCLocator.getInstance().lookup(
				IApprovalproMaintain.class);
		AggApprovalProVO[] bills = null;
		try {
			//def2完成尽调，def3提交交易所，def4交易所审批通过，def5文件封卷，def6提交证监会，def7证监会审批通过，def8获取批文
			ProgressCtrVO[] children = (ProgressCtrVO[])clientFullVOs[0].getChildren(ProgressCtrVO.class);
			for (ProgressCtrVO pvo : children) {
				if("实际时间".equals(pvo.getDef1())){
					String billid = clientFullVOs[0].getPrimaryKey();
					/*if(pvo.getDef2() != null){
						Integer filesNum = this.getFilesNum(billid+"/9.进度成果文件-完成尽调");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("请上传【完成尽调】相关附件（成果清单）！");
						}
					}
					if(pvo.getDef4() != null){
						Integer filesNum = this.getFilesNum(billid+"/10.进度成果文件-交易所审批通过");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("请上传【交易所审批通过】相关附件（成果清单）！");
						}
					}
					if(pvo.getDef5() != null){
						Integer filesNum = this.getFilesNum(billid+"/11.进度成果文件-文件封卷");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("请上传【文件封卷】相关附件（成果清单）！");
						}
					}
					if(pvo.getDef7() != null){
						Integer filesNum = this.getFilesNum(billid+"/12.进度成果文件-证监会审批通过");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("请上传【证监会审批通过】相关附件（成果清单）！");
						}
					}
					if(pvo.getDef8() != null){
						Integer filesNum = this.getFilesNum(billid+"/13.进度成果文件-获取批文");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("请上传【获取批文】相关附件（成果清单）！");
						}
					}*/
					if(pvo.getDef3() != null){
						Integer filesNum = this.getFilesNum(billid+"/1.提交交易所");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("请上传【提交交易所】相关附件（成果清单）！");
						}
					}
					if(pvo.getDef5() != null){
						Integer filesNum = this.getFilesNum(billid+"/2.文件封卷");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("请上传【文件封卷】相关附件（成果清单）！");
						}
					}
					if(pvo.getDef8() != null){
						Integer filesNum = this.getFilesNum(billid+"/3.获取批文");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("请上传【获取批文】相关附件（成果清单）！");
						}
					}
				}
			}
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
	/**
	 * 读取附件数量
	 * 
	 * @param billid
	 * @return
	 * @throws BusinessException
	 */
	private Integer getFilesNum(String billid)
			throws BusinessException {
		String sql = "select count(1) from sm_pub_filesystem where filepath like '"+billid+"%' and isfolder = 'n'";
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Integer num = (Integer) bs.executeQuery(sql,new ColumnProcessor());
		return num;
	}
}
