package nc.bs.tg.approvalpro.ace.rule;

import java.util.Comparator;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.ApprovalProVO;
import nc.vo.tg.approvalpro.ProgressCtrVO;
import nc.vo.tg.fischemepushstandard.FischemePushStandardCVO;
import nc.vo.trade.pub.IBillStatus;

import org.apache.commons.lang.StringUtils;
/**
 * 审批完成后往数据库回写进度和单期发行规模
 * @author wenjie
 *
 */
public class IssueWriteBackRule implements IRule<AggApprovalProVO>{
	BaseDAO baseDAO = null;
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	@Override
	public void process(AggApprovalProVO[] vos) {
		// TODO 自动生成的方法存根'
		AggApprovalProVO aggVO = vos[0];
		if(aggVO.getParentVO().getApprovestatus() == IBillStatus.CHECKPASS){
			try {
				ProgressCtrVO[] proVOs = (ProgressCtrVO[]) aggVO.getChildren(ProgressCtrVO.class);
				if(proVOs != null && proVOs.length>0){
					java.util.Arrays.sort(proVOs,new Comparator<ProgressCtrVO>(){
						@Override
						public int compare(ProgressCtrVO o1, ProgressCtrVO o2) {
							String rowno1 = o1.getRowno();
							String rowno2 = o2.getRowno();
							return Double.valueOf(rowno1).compareTo(Double.valueOf(rowno2));
						}
						
					});
				}else{
					return;
				}
				if(aggVO.getParentVO().getEmendenum() == null){
					this.writeBackPlanTime(aggVO.getParentVO(), proVOs);
				}else{
					this.writeBackProgress(proVOs);
				}
				getBaseDAO().updateVOArray(proVOs);
				aggVO.setChildren(ProgressCtrVO.class, proVOs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据上级领导审核完成时间加上资本市场融资方案推进标准天数，自动计算计划时间。
	 * @param aproVO 
	 * @throws DAOException 
	 */
	private ProgressCtrVO[] writeBackPlanTime(ApprovalProVO approVO, ProgressCtrVO[] proVOs) throws BusinessException{
		// TODO 自动生成的方法存根
		UFDateTime approvedate = approVO.getApprovedate();
		String sql = "select * from tg_fischemepushstandard_c where nvl(dr,0)=0";
		FischemePushStandardCVO queryVO = (FischemePushStandardCVO)getBaseDAO().executeQuery(sql, new BeanProcessor(FischemePushStandardCVO.class));
		proVOs[0].setDef2(approvedate.getDateTimeAfter(queryVO.getDef1()==null?0:Integer.valueOf(queryVO.getDef1())).toString().substring(0,11));
		proVOs[0].setDef3(approvedate.getDateTimeAfter(queryVO.getDef2()==null?0:Integer.valueOf(queryVO.getDef2())).toString().substring(0,11));
		proVOs[0].setDef4(approvedate.getDateTimeAfter(queryVO.getDef3()==null?0:Integer.valueOf(queryVO.getDef3())).toString().substring(0,11));
		proVOs[0].setDef5(approvedate.getDateTimeAfter(queryVO.getDef4()==null?0:Integer.valueOf(queryVO.getDef4())).toString().substring(0,11));
		proVOs[0].setDef6(approvedate.getDateTimeAfter(queryVO.getDef5()==null?0:Integer.valueOf(queryVO.getDef5())).toString().substring(0,11));
		proVOs[0].setDef7(approvedate.getDateTimeAfter(queryVO.getDef6()==null?0:Integer.valueOf(queryVO.getDef6())).toString().substring(0,11));
		proVOs[0].setDef8(approvedate.getDateTimeAfter(queryVO.getDef7()==null?0:Integer.valueOf(queryVO.getDef7())).toString().substring(0,11));
		return proVOs;
	}
	private ProgressCtrVO[] writeBackProgress(ProgressCtrVO[] proVOs) throws DAOException {
		//根据计划时间和实际时间算出天数
		if(StringUtils.isNotBlank(proVOs[0].getDef2()) && StringUtils.isNotBlank(proVOs[1].getDef2())){
			proVOs[2].setDef2(getValue(proVOs[0].getDef2(),proVOs[1].getDef2()));
		}
		if(StringUtils.isNotBlank(proVOs[0].getDef3()) && StringUtils.isNotBlank(proVOs[1].getDef3())){
			proVOs[2].setDef3(getValue(proVOs[0].getDef3(),proVOs[1].getDef3()));
		}
		if(StringUtils.isNotBlank(proVOs[0].getDef4()) && StringUtils.isNotBlank(proVOs[1].getDef4())){
			proVOs[2].setDef4(getValue(proVOs[0].getDef4(),proVOs[1].getDef4()));
		}
		if(StringUtils.isNotBlank(proVOs[0].getDef5()) && StringUtils.isNotBlank(proVOs[1].getDef5())){
			proVOs[2].setDef5(getValue(proVOs[0].getDef5(),proVOs[1].getDef5()));
		}
		if(StringUtils.isNotBlank(proVOs[0].getDef6()) && StringUtils.isNotBlank(proVOs[1].getDef6())){
			proVOs[2].setDef6(getValue(proVOs[0].getDef6(),proVOs[1].getDef6()));
		}
		if(StringUtils.isNotBlank(proVOs[0].getDef7()) && StringUtils.isNotBlank(proVOs[1].getDef7())){
			proVOs[2].setDef7(getValue(proVOs[0].getDef7(),proVOs[1].getDef7()));
		}
		if(StringUtils.isNotBlank(proVOs[0].getDef8()) && StringUtils.isNotBlank(proVOs[1].getDef8())){
			proVOs[2].setDef8(getValue(proVOs[0].getDef8(),proVOs[1].getDef8()));
		}
		return proVOs;
	}
	private String getValue(String def1, String def2) {
		UFDate date1 = new UFDate(def1).asBegin();
		UFDate date2 = new UFDate(def2).asBegin();
		return Integer.toString(date1.getDaysAfter(date2));
	}


}
