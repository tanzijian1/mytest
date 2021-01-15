package nc.bs.tg.organization.ace.bp;

import nc.bs.tg.organization.plugin.bpplugin.OrganizationPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.organization.AggOrganizationVO;


/**
 * 标准单据删除BP
 */
public class AceOrganizationDeleteBP {

	public void delete(AggOrganizationVO[] bills) {

		DeleteBPTemplate<AggOrganizationVO> bp = new DeleteBPTemplate<AggOrganizationVO>(
				OrganizationPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggOrganizationVO> processer) {
		// TODO 前规则
		IRule<AggOrganizationVO> rule = null;
		// rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		// processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggOrganizationVO> processer) {
		// TODO 后规则

	}
}
