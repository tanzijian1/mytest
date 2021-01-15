package nc.ui.tg.singleissue.action;

import java.awt.event.ActionEvent;
import java.util.Comparator;

import nc.bs.framework.common.NCLocator;
import nc.itf.pubapp.pub.smart.IBillQueryService;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.ui.ml.NCLangRes;
import nc.ui.pubapp.uif2app.actions.RefreshSingleAction;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.RepaymentPlanVO;

public class CardRefreshAction extends RefreshSingleAction {
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object obj = this.model.getSelectedData();
		if (obj != null) {
			AbstractBill oldVO = (AbstractBill) obj;
			String pk = oldVO.getParentVO().getPrimaryKey();
			IBillQueryService billQuery =
					NCLocator.getInstance().lookup(IBillQueryService.class);
			AggSingleIssueVO newVO =
					(AggSingleIssueVO)billQuery.querySingleBillByPk(oldVO.getClass(), pk);
			// 单据被删除之后应该回到列表界面再刷新
			if (newVO == null) {
				// 数据已经被删除
				throw new BusinessException(NCLangRes.getInstance().getStrByID(
						"uif2", "RefreshSingleAction-000000")/*数据已经被删除，请返回列表界面！*/);
			}
			RepaymentPlanVO[] pvos = (RepaymentPlanVO[])newVO.getChildren(RepaymentPlanVO.class);
			java.util.Arrays.sort(pvos, new Comparator<RepaymentPlanVO>(){
				@Override
				public int compare(RepaymentPlanVO o1, RepaymentPlanVO o2) {
					// TODO 自动生成的方法存根
					if(o1.getDef2() == null && o2.getDef2()!=null){
						return 1;
					}
					if(o2.getDef2() == null && o1.getDef2()!=null){
						return -1;
					}
					if(o1.getDef2() == null && o2.getDef2() == null){
						return 0;
					}
					UFDate date1 = new UFDate(o1.getDef2());
					UFDate date2 = new UFDate(o2.getDef2());
					return date1.compareTo(date2);
				}});
			this.model.directlyUpdate(newVO);
		}
		this.showQueryInfo();
	}
	private IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}
}
