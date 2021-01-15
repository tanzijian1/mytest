package nc.bs.tg.projectdata.ace.bp;

import nc.bs.tg.projectdata.plugin.bpplugin.ProjectdataPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.projectdata.AggProjectDataVO;

/**
 * �޸ı����BP
 * 
 */
public class AceProjectdataUpdateBP {

	public AggProjectDataVO[] update(AggProjectDataVO[] bills,
			AggProjectDataVO[] originBills) {
		// �����޸�ģ��
		UpdateBPTemplate<AggProjectDataVO> bp = new UpdateBPTemplate<AggProjectDataVO>(
				ProjectdataPluginPoint.UPDATE);
		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggProjectDataVO> processer) {
		// TODO �����
		IRule<AggProjectDataVO> rule = null;

	}

	private void addBeforeRule(CompareAroundProcesser<AggProjectDataVO> processer) {
		// TODO ǰ����
		IRule<AggProjectDataVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
	}

}
