package nc.bs.tg.approvalpro.ace.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.IssueScaleVO;
import nc.vo.tg.approvalpro.ProgressCtrVO;

public class UnApproveRule implements IRule<AggApprovalProVO>{

	@Override
	public void process(AggApprovalProVO[] vos) {
		// TODO �Զ����ɵķ������
		for (AggApprovalProVO aggvo : vos) {
			IssueScaleVO[] ivos = (IssueScaleVO[])aggvo.getChildren(IssueScaleVO.class);
			if(ivos != null && ivos.length>0){
					ExceptionUtils.wrappBusinessException("�����ķ����ѱ����ã�����ȡ����ˣ�ֻ�ܱ������");
			}
			ProgressCtrVO[] pvos = (ProgressCtrVO[])aggvo.getChildren(ProgressCtrVO.class);
			if(pvos != null && pvos.length>0){
				for (ProgressCtrVO pvo : pvos) {
					if("�ƻ�ʱ��".equals(pvo.getDef1())){
						pvo.setDef2(null);
						pvo.setDef3(null);
						pvo.setDef4(null);
						pvo.setDef5(null);
						pvo.setDef6(null);
						pvo.setDef7(null);
						pvo.setDef8(null);
					}
				}
				ProgressCtrVO[] cbvos = new ProgressCtrVO[3];
				for (ProgressCtrVO bvo : pvos) {
					if("�ƻ�ʱ��".equals(bvo.getDef1()))cbvos[0] = bvo;
					if("ʵ��ʱ��".equals(bvo.getDef1()))cbvos[1] = bvo;
					if("����".equals(bvo.getDef1()))cbvos[2] = bvo;
				}
				pvos[0] = cbvos[0];
				pvos[1] = cbvos[1];
				pvos[2] = cbvos[2];
			}
		}
	}

}
