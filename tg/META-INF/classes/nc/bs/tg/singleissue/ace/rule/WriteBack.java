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
 * 保存后回写数据到批文方案和授信情况
 * @author wenjie
 *
 */
public class WriteBack  implements IRule<AggSingleIssueVO>{
	/**
	 * 元数据持久化查询接口
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
		// TODO 自动生成的方法存根
		if(vos[0].getParentVO().getApprovestatus() == IBillStatus.CHECKPASS){
			//版本号为空则初次审核，需回写数据
			if(vos[0].getParentVO().getApproversionnum() == null){
				//回写集团授信的本期已用额度
				writeBackToProtocol(vos);
			}
			//回写到批文方案的单期规模
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
						curusdcdtlnamt = curusdcdtlnamt==null?UFDouble.ZERO_DBL:curusdcdtlnamt;//已用授信额度
						UFDouble availcdtlnamt = aggvoPro.getParentVO().getAvailcdtlnamt();//未用授信额度
						availcdtlnamt = availcdtlnamt==null?UFDouble.ZERO_DBL:availcdtlnamt;
						
						UFDouble def4 = gvo.getDef4()==null?UFDouble.ZERO_DBL:new UFDouble(gvo.getDef4());
						//回写授信情况
						aggvoPro.getParentVO().setCurusdcdtlnamt(curusdcdtlnamt.add(def4,2));
						aggvoPro.getParentVO().setAvailcdtlnamt(availcdtlnamt.sub(def4, 2));
						getBaseDAO().updateVO(aggvoPro.getParentVO());
					}
				} catch (BusinessException e) {
					// TODO 自动生成的 catch 块
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
					//回写【批文方案】的【单期发行规模】页签数据
					IssueScaleVO issueVO = new IssueScaleVO();
					issueVO.setDef1(vos[0].getParentVO().getBillno());//单号
					issueVO.setDef2(vos[0].getParentVO().getDef32());//发行时间
					issueVO.setDef3(vos[0].getParentVO().getDef5());//发行规模
					issueVO.setDef5(vos[0].getParentVO().getName());//单期名称
					issueVO.setDef6(vos[0].getParentVO().getDef14());//到期日
					issueVO.setDef7(vos[0].getParentVO().getDef16());//利率
					issueVO.setDef8(vos[0].getParentVO().getDef6());//优先级规模
					issueVO.setDef9(vos[0].getParentVO().getDef7());//次级规模
					if(cvos!=null && cvos.length>0){
						issueVO.setDef10(cvos[0].getDef4());//当期利率
					}
					issueVO.setDr(0);
					issueVO.setPk_appro(pk_appro);
					
					/**
					 * 插入单期发行规模数据
					 */
					if(pk_issuescale == null){
						getBaseDAO().insertVO(issueVO);
					}else{
						issueVO.setPk_issuescale(pk_issuescale);
						getBaseDAO().updateVO(issueVO);
					}
					//
					
					String appro = aggvo.getParentVO().getDef19();//批文额度
					String singleIssue = vos[0].getParentVO().getDef5();//单期发行金额
					UFDouble approD = appro==null?UFDouble.ZERO_DBL:new UFDouble(appro);
					UFDouble singleIssueD = singleIssue==null?UFDouble.ZERO_DBL:new UFDouble(singleIssue);
					if(aggvo.getChildren(IssueScaleVO.class) != null){
						for (IssueScaleVO vo : (IssueScaleVO[])aggvo.getChildren(IssueScaleVO.class)) {
							UFDouble amount = vo.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef3());
							approD = approD.sub(amount,2);
						}
					}
					aggvo.getParentVO().setDef7(approD.sub(singleIssueD).add(old,2).toString());//设置剩余可发行额度
					getBaseDAO().updateVO(aggvo.getParentVO());//更新剩余可发行额度
					
					
					ProgressCtrVO[] pvo = (ProgressCtrVO[])aggvo.getChildren(ProgressCtrVO.class);
					if(pvo != null){
						for (ProgressCtrVO progressCtrVO : pvo) {
							if("实际时间".equals(progressCtrVO.getDef1())){
								if(!"Y".equals(progressCtrVO.getDef9())){
									progressCtrVO.setDef9("Y");
									getBaseDAO().updateVO(progressCtrVO);//更新已提款字段
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
	 * 查询引用的批文方案聚合VO
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
