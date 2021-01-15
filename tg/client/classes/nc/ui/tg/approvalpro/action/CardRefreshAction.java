package nc.ui.tg.approvalpro.action;

import java.awt.event.ActionEvent;
import java.util.Comparator;

import nc.bs.framework.common.NCLocator;
import nc.itf.pubapp.pub.smart.IBillQueryService;
import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.uif2app.actions.RefreshSingleAction;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.ProgressCtrVO;

public class CardRefreshAction extends RefreshSingleAction {
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object obj = this.model.getSelectedData();
		if (obj != null) {
			AbstractBill oldVO = (AbstractBill) obj;
			String pk = oldVO.getParentVO().getPrimaryKey();
			IBillQueryService billQuery =
					NCLocator.getInstance().lookup(IBillQueryService.class);
			AggApprovalProVO newVO =
					(AggApprovalProVO)billQuery.querySingleBillByPk(oldVO.getClass(), pk);
			// ���ݱ�ɾ��֮��Ӧ�ûص��б������ˢ��
			if (newVO == null) {
				// �����Ѿ���ɾ��
				throw new BusinessException(NCLangRes.getInstance().getStrByID(
						"uif2", "RefreshSingleAction-000000")/*�����Ѿ���ɾ�����뷵���б���棡*/);
			}
			ProgressCtrVO[] pvos = (ProgressCtrVO[])newVO.getChildren(ProgressCtrVO.class);
			if(pvos != null){
				java.util.Arrays.sort(pvos, new Comparator<ProgressCtrVO>(){
					@Override
					public int compare(ProgressCtrVO o1, ProgressCtrVO o2) {
						// TODO �Զ����ɵķ������
						String rowno1 = o1.getRowno();
						String rowno2 = o2.getRowno();
						return Double.valueOf(rowno1).compareTo(Double.valueOf(rowno2));
					}});
			}
			this.model.directlyUpdate(newVO);
		}
		this.showQueryInfo();
	}
}
