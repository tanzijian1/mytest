package nc.bs.tg.abnormaltaxtransfer.ace.bp;

import nc.bs.tg.abnormaltaxtransfer.plugin.bpplugin.AbnormalTaxTransferPluginPoint;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceAbnormalTaxTransferDeleteBP {

	public void delete(AggAbTaxTransferHVO[] bills) {

		DeleteBPTemplate<AggAbTaxTransferHVO> bp = new DeleteBPTemplate<AggAbTaxTransferHVO>(
				AbnormalTaxTransferPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggAbTaxTransferHVO> processer) {
		// TODO ǰ����
		IRule<AggAbTaxTransferHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggAbTaxTransferHVO> processer) {
		// TODO �����

	}
}
