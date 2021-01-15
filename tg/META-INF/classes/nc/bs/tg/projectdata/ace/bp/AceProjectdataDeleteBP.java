package nc.bs.tg.projectdata.ace.bp;

import nc.bs.tg.projectdata.plugin.bpplugin.ProjectdataPluginPoint;
import nc.vo.tg.projectdata.AggProjectDataVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceProjectdataDeleteBP {

	public void delete(AggProjectDataVO[] bills) {

		DeleteBPTemplate<AggProjectDataVO> bp = new DeleteBPTemplate<AggProjectDataVO>(
				ProjectdataPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggProjectDataVO> processer) {
		// TODO ǰ����
		IRule<AggProjectDataVO> rule = null;
		// rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		// processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggProjectDataVO> processer) {
		// TODO �����

	}
}
