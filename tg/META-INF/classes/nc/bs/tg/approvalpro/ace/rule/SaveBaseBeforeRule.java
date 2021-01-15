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
		// TODO �Զ����ɵķ������
		//��֤��ʵ���˳��
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
			//def2��ɾ�����def3�ύ��������def4����������ͨ����def5�ļ����def6�ύ֤��ᣬdef7֤�������ͨ����def8��ȡ����
			if(bvo.getDef3()!=null && bvo.getDef2()==null){
				ExceptionUtils.wrappBusinessException("���ύ��������ʵ��ʱ����ֵ������ɾ�����ʵ��ʱ�������ֵ");
			}
			if(bvo.getDef4()!=null && bvo.getDef3()==null){
				ExceptionUtils.wrappBusinessException("������������ͨ����ʵ��ʱ����ֵ�����ύ��������ʵ��ʱ�������ֵ");
			}
			if(bvo.getDef5()!=null && bvo.getDef4()==null){
				ExceptionUtils.wrappBusinessException("���ļ����ʵ��ʱ����ֵ��������������ͨ����ʵ��ʱ�������ֵ");
			}
			
			String pk_fintype = aggvo.getParentVO().getDef1();
			String nameBypk = null;
			try {
				nameBypk = this.getNameBypk(pk_fintype);
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			if(SdfnUtil.getABSList().contains(nameBypk)){
				if(bvo.getDef8()!=null && bvo.getDef5()==null){
					ExceptionUtils.wrappBusinessException("ҵ������ΪABS������ȡ���ġ�ʵ��ʱ����ֵ�����ļ����ʵ��ʱ�������ֵ");
				}
			}else{
				if(bvo.getDef6()!=null && bvo.getDef5()==null){
					ExceptionUtils.wrappBusinessException("ҵ������Ϊծȯ�����ύ֤��᡿ʵ��ʱ����ֵ�����ļ����ʵ��ʱ�������ֵ");
				}
				if(bvo.getDef7()!=null && bvo.getDef6()==null){
					ExceptionUtils.wrappBusinessException("ҵ������Ϊծȯ����֤�������ͨ����ʵ��ʱ����ֵ�����ύ֤��᡿ʵ��ʱ�������ֵ");
				}
				if(bvo.getDef8()!=null && bvo.getDef7()==null){
					ExceptionUtils.wrappBusinessException("ҵ������Ϊծȯ������ȡ���ġ�ʵ��ʱ����ֵ����֤�������ͨ����ʵ��ʱ�������ֵ");
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
