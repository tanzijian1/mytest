package nc.bs.tg.transferbill.ace.bp;

import nc.bs.tg.transferbill.plugin.bpplugin.TransferBillPluginPoint;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceTransferBillDeleteBP {

	public void delete(AggTransferBillHVO[] bills) {

		DeleteBPTemplate<AggTransferBillHVO> bp = new DeleteBPTemplate<AggTransferBillHVO>(
				TransferBillPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggTransferBillHVO> processer) {
		// TODO ǰ����
		IRule<AggTransferBillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggTransferBillHVO> processer) {
		// TODO �����
	}
}
