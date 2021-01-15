package nc.bs.tg.approvalpro.ace.rule;

import java.util.Comparator;

import nc.bs.dao.BaseDAO;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.util.SdfnUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.ProgressCtrVO;

public class SaveBaseBeforeRule implements IRule<AggApprovalProVO>{

	@Override
	public void process(AggApprovalProVO[] vos) {
		// TODO 自动生成的方法存根
		//保证子实体的顺序
		AggApprovalProVO aggvo = vos[0];
		ProgressCtrVO[] bvos = (ProgressCtrVO[])aggvo.getChildren(ProgressCtrVO.class);
		if(bvos != null && bvos.length>0){
			java.util.Arrays.sort(bvos,new Comparator<ProgressCtrVO>(){
				@Override
				public int compare(ProgressCtrVO o1, ProgressCtrVO o2) {
					String rowno1 = o1.getRowno();
					String rowno2 = o2.getRowno();
					return Double.valueOf(rowno1).compareTo(Double.valueOf(rowno2));
				}});
			ProgressCtrVO bvo = bvos[1];
			//def2完成尽调，def3提交交易所，def4交易所审批通过，def5文件封卷，def6提交证监会，def7证监会审批通过，def8获取批文
			if(bvo.getDef3()!=null && bvo.getDef2()==null){
				ExceptionUtils.wrappBusinessException("【提交交易所】实际时间有值，【完成尽调】实际时间必须有值");
			}
			if(bvo.getDef4()!=null && bvo.getDef3()==null){
				ExceptionUtils.wrappBusinessException("【交易所审批通过】实际时间有值，【提交交易所】实际时间必须有值");
			}
			if(bvo.getDef5()!=null && bvo.getDef4()==null){
				ExceptionUtils.wrappBusinessException("【文件封卷】实际时间有值，【交易所审批通过】实际时间必须有值");
			}
			
			String pk_fintype = aggvo.getParentVO().getDef1();
			String nameBypk = null;
			try {
				nameBypk = this.getNameBypk(pk_fintype);
			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			if(SdfnUtil.getABSList().contains(nameBypk)){
				if(bvo.getDef8()!=null && bvo.getDef5()==null){
					ExceptionUtils.wrappBusinessException("业务类型为ABS，【获取批文】实际时间有值，【文件封卷】实际时间必须有值");
				}
			}else{
				if(bvo.getDef6()!=null && bvo.getDef5()==null){
					ExceptionUtils.wrappBusinessException("业务类型为债券，【提交证监会】实际时间有值，【文件封卷】实际时间必须有值");
				}
				if(bvo.getDef7()!=null && bvo.getDef6()==null){
					ExceptionUtils.wrappBusinessException("业务类型为债券，【证监会审批通过】实际时间有值，【提交证监会】实际时间必须有值");
				}
				if(bvo.getDef8()!=null && bvo.getDef7()==null){
					ExceptionUtils.wrappBusinessException("业务类型为债券，【获取批文】实际时间有值，【证监会审批通过】实际时间必须有值");
				}
			}
		}
	}
	private String getNameBypk(String pk_fintype) throws BusinessException{
		String sql ="select name from tgrz_fintype where pk_fintype='"+pk_fintype+"' and nvl(dr,0)=0";
		String result = (String)new BaseDAO().executeQuery(sql, new ColumnProcessor());
		return result;
	}
}
