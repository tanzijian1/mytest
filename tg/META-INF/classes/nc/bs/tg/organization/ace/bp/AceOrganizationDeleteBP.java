package nc.bs.tg.organization.ace.bp;

import nc.bs.tg.organization.plugin.bpplugin.OrganizationPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.organization.AggOrganizationVO;


/**
 * ��׼����ɾ��BP
 */
public class AceOrganizationDeleteBP {

	public void delete(AggOrganizationVO[] bills) {

		DeleteBPTemplate<AggOrganizationVO> bp = new DeleteBPTemplate<AggOrganizationVO>(
				OrganizationPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggOrganizationVO> processer) {
		// TODO ǰ����
		IRule<AggOrganizationVO> rule = null;
		// rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		// processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggOrganizationVO> processer) {
		// TODO �����

	}
}
