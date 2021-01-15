package nc.bs.tg.rzreportbi.ace.bp;

import nc.bs.tg.rzreportbi.plugin.bpplugin.RZreportBIPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;

/**
 * �޸ı����BP
 * 
 */
public class AceRZreportBIUpdateBP {

	public AggRZreportBIVO[] update(AggRZreportBIVO[] bills,
			AggRZreportBIVO[] originBills) {
		// �����޸�ģ��
		UpdateBPTemplate<AggRZreportBIVO> bp = new UpdateBPTemplate<AggRZreportBIVO>(
				RZreportBIPluginPoint.UPDATE);
		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggRZreportBIVO> processer) {
		// TODO �����
		IRule<AggRZreportBIVO> rule = null;

	}

	private void addBeforeRule(CompareAroundProcesser<AggRZreportBIVO> processer) {
		// TODO ǰ����
		IRule<AggRZreportBIVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
	}

}
