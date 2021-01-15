package nc.bs.tg.exhousetransferbill.ace.bp;

import nc.bs.tg.exhousetransferbill.plugin.bpplugin.ExHouseTransferBillPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;


/**
 * ��׼����ɾ��BP
 */
public class AceExHouseTransferBillDeleteBP {

	public void delete(AggExhousetransferbillHVO[] bills) {

		DeleteBPTemplate<AggExhousetransferbillHVO> bp = new DeleteBPTemplate<AggExhousetransferbillHVO>(
				ExHouseTransferBillPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggExhousetransferbillHVO> processer) {
		// TODO ǰ����
		IRule<AggExhousetransferbillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggExhousetransferbillHVO> processer) {
		// TODO �����
	}
}
