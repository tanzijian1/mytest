package nc.bs.tg.singleissue.ace.rule;

import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.cc.grpprotocol.AggGroupProtocolVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.IssueScaleVO;
import nc.vo.tg.approvalpro.ProgressCtrVO;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.BondResaleVO;
import nc.vo.tg.singleissue.GroupCreditVO;
import nc.vo.trade.pub.IBillStatus;
/**
 * ������д���ݵ����ķ������������
 * @author wenjie
 *
 */
public class WriteBack  implements IRule<AggSingleIssueVO>{
	/**
	 * Ԫ���ݳ־û���ѯ�ӿ�
	 * 
	 * @return
	 */
	IMDPersistenceQueryService mdQryService = null;
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}
	private BaseDAO baseDAO = null;
	private BaseDAO getBaseDAO(){
		if(baseDAO==null){
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	@Override
	public void process(AggSingleIssueVO[] vos) {
		// TODO �Զ����ɵķ������
		if(vos[0].getParentVO().getApprovestatus() == IBillStatus.CHECKPASS){
			//�汾��Ϊ���������ˣ����д����
			if(vos[0].getParentVO().getApproversionnum() == null){
				//��д�������ŵı������ö��
				writeBackToProtocol(vos);
			}
			//��д�����ķ����ĵ��ڹ�ģ
			writeBackToAppro(vos);
		}
	}
	private void writeBackToProtocol(AggSingleIssueVO[] vos){
		GroupCreditVO[] gvos = (GroupCreditVO[])vos[0].getChildren(GroupCreditVO.class);
		for (GroupCreditVO gvo : gvos) {
			if(gvo.getDef1() != null && "Y".equals(gvo.getDef3())){
				AggGroupProtocolVO aggvoPro;
				try {
					aggvoPro = (AggGroupProtocolVO)getBillVO(AggGroupProtocolVO.class,"nvl(dr,0) = 0 and pk_bankprotocol='"+gvo.getDef1()+"'");
					if(aggvoPro!=null){
						UFDouble curusdcdtlnamt = aggvoPro.getParentVO().getCurusdcdtlnamt();
						curusdcdtlnamt = curusdcdtlnamt==null?UFDouble.ZERO_DBL:curusdcdtlnamt;//�������Ŷ��
						UFDouble availcdtlnamt = aggvoPro.getParentVO().getAvailcdtlnamt();//δ�����Ŷ��
						availcdtlnamt = availcdtlnamt==null?UFDouble.ZERO_DBL:availcdtlnamt;
						
						UFDouble def4 = gvo.getDef4()==null?UFDouble.ZERO_DBL:new UFDouble(gvo.getDef4());
						//��д�������
						aggvoPro.getParentVO().setCurusdcdtlnamt(curusdcdtlnamt.add(def4,2));
						aggvoPro.getParentVO().setAvailcdtlnamt(availcdtlnamt.sub(def4, 2));
						getBaseDAO().updateVO(aggvoPro.getParentVO());
					}
				} catch (BusinessException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					ExceptionUtils.wrappBusinessException(e.getMessage());
				}
			}
		}
	}
	private void writeBackToAppro(AggSingleIssueVO[] vos){
		String pk_appro = vos[0].getParentVO().getDef1();
		BondResaleVO[] cvos = (BondResaleVO[]) vos[0].getChildren(BondResaleVO.class);
		if(pk_appro != null){
			try {
				AggApprovalProVO aggvo = (AggApprovalProVO)getBillVO(AggApprovalProVO.class,"nvl(dr,0) = 0 and pk_appro='"+pk_appro+"'");
				if(aggvo != null){
					String pk_issuescale = null;
					UFDouble old = UFDouble.ZERO_DBL;
					IssueScaleVO[] oldIssueVOs = (IssueScaleVO[]) aggvo.getChildren(IssueScaleVO.class);
					if(oldIssueVOs != null){
						for (IssueScaleVO vo : oldIssueVOs) {
							if(vos[0].getParentVO().getBillno().equals(vo.getDef1())){
								pk_issuescale = vo.getPk_issuescale();
								old = vo.getDef3()==null?old:new UFDouble(vo.getDef3());
							}
						}
					}
					//��д�����ķ������ġ����ڷ��й�ģ��ҳǩ����
					IssueScaleVO issueVO = new IssueScaleVO();
					issueVO.setDef1(vos[0].getParentVO().getBillno());//����
					issueVO.setDef2(vos[0].getParentVO().getDef32());//����ʱ��
					issueVO.setDef3(vos[0].getParentVO().getDef5());//���й�ģ
					issueVO.setDef5(vos[0].getParentVO().getName());//��������
					issueVO.setDef6(vos[0].getParentVO().getDef14());//������
					issueVO.setDef7(vos[0].getParentVO().getDef16());//����
					issueVO.setDef8(vos[0].getParentVO().getDef6());//���ȼ���ģ
					issueVO.setDef9(vos[0].getParentVO().getDef7());//�μ���ģ
					if(cvos!=null && cvos.length>0){
						issueVO.setDef10(cvos[0].getDef4());//��������
					}
					issueVO.setDr(0);
					issueVO.setPk_appro(pk_appro);
					
					/**
					 * ���뵥�ڷ��й�ģ����
					 */
					if(pk_issuescale == null){
						getBaseDAO().insertVO(issueVO);
					}else{
						issueVO.setPk_issuescale(pk_issuescale);
						getBaseDAO().updateVO(issueVO);
					}
					//
					
					String appro = aggvo.getParentVO().getDef19();//���Ķ��
					String singleIssue = vos[0].getParentVO().getDef5();//���ڷ��н��
					UFDouble approD = appro==null?UFDouble.ZERO_DBL:new UFDouble(appro);
					UFDouble singleIssueD = singleIssue==null?UFDouble.ZERO_DBL:new UFDouble(singleIssue);
					if(aggvo.getChildren(IssueScaleVO.class) != null){
						for (IssueScaleVO vo : (IssueScaleVO[])aggvo.getChildren(IssueScaleVO.class)) {
							UFDouble amount = vo.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef3());
							approD = approD.sub(amount,2);
						}
					}
					aggvo.getParentVO().setDef7(approD.sub(singleIssueD).add(old,2).toString());//����ʣ��ɷ��ж��
					getBaseDAO().updateVO(aggvo.getParentVO());//����ʣ��ɷ��ж��
					
					
					ProgressCtrVO[] pvo = (ProgressCtrVO[])aggvo.getChildren(ProgressCtrVO.class);
					if(pvo != null){
						for (ProgressCtrVO progressCtrVO : pvo) {
							if("ʵ��ʱ��".equals(progressCtrVO.getDef1())){
								if(!"Y".equals(progressCtrVO.getDef9())){
									progressCtrVO.setDef9("Y");
									getBaseDAO().updateVO(progressCtrVO);//����������ֶ�
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}
		}
	}
	
	/**
	 * ��ѯ���õ����ķ����ۺ�VO
	 */
	@SuppressWarnings("rawtypes")
	private AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, true,false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
	
}
