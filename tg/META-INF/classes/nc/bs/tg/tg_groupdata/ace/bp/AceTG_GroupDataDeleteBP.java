package nc.bs.tg.tg_groupdata.ace.bp;

import nc.bs.tg.tg_groupdata.plugin.bpplugin.TG_GroupDataPluginPoint;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceTG_GroupDataDeleteBP {

	public void delete(AggGroupDataVO[] bills) {

		DeleteBPTemplate<AggGroupDataVO> bp = new DeleteBPTemplate<AggGroupDataVO>(
				TG_GroupDataPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggGroupDataVO> processer) {
		// TODO ǰ����
		IRule<AggGroupDataVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggGroupDataVO> processer) {
		// TODO �����

	}
}
