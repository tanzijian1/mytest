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
		// TODO �ڴ˴�������ǰ�����
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
			//def2��ɾ�����def3�ύ��������def4����������ͨ����def5�ļ����def6�ύ֤��ᣬdef7֤�������ͨ����def8��ȡ����
			ProgressCtrVO[] children = (ProgressCtrVO[])clientFullVOs[0].getChildren(ProgressCtrVO.class);
			for (ProgressCtrVO pvo : children) {
				if("ʵ��ʱ��".equals(pvo.getDef1())){
					String billid = clientFullVOs[0].getPrimaryKey();
					/*if(pvo.getDef2() != null){
						Integer filesNum = this.getFilesNum(billid+"/9.���ȳɹ��ļ�-��ɾ���");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("���ϴ�����ɾ�������ظ������ɹ��嵥����");
						}
					}
					if(pvo.getDef4() != null){
						Integer filesNum = this.getFilesNum(billid+"/10.���ȳɹ��ļ�-����������ͨ��");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("���ϴ�������������ͨ������ظ������ɹ��嵥����");
						}
					}
					if(pvo.getDef5() != null){
						Integer filesNum = this.getFilesNum(billid+"/11.���ȳɹ��ļ�-�ļ����");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("���ϴ����ļ������ظ������ɹ��嵥����");
						}
					}
					if(pvo.getDef7() != null){
						Integer filesNum = this.getFilesNum(billid+"/12.���ȳɹ��ļ�-֤�������ͨ��");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("���ϴ���֤�������ͨ������ظ������ɹ��嵥����");
						}
					}
					if(pvo.getDef8() != null){
						Integer filesNum = this.getFilesNum(billid+"/13.���ȳɹ��ļ�-��ȡ����");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("���ϴ�����ȡ���ġ���ظ������ɹ��嵥����");
						}
					}*/
					if(pvo.getDef3() != null){
						Integer filesNum = this.getFilesNum(billid+"/1.�ύ������");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("���ϴ����ύ����������ظ������ɹ��嵥����");
						}
					}
					if(pvo.getDef5() != null){
						Integer filesNum = this.getFilesNum(billid+"/2.�ļ����");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("���ϴ����ļ������ظ������ɹ��嵥����");
						}
					}
					if(pvo.getDef8() != null){
						Integer filesNum = this.getFilesNum(billid+"/3.��ȡ����");
						if(filesNum==null || filesNum==0){
							throw new BusinessException("���ϴ�����ȡ���ġ���ظ������ɹ��嵥����");
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
	 * ��ȡ��������
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
