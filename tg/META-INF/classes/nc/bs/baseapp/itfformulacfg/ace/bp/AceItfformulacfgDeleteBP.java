package nc.bs.baseapp.itfformulacfg.ace.bp;

import nc.bs.baseapp.itfformulacfg.plugin.bpplugin.ItfformulacfgPluginPoint;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;

/**
 * @Description:ɾ��bp
 * @version with NC V6.5
 */
public class AceItfformulacfgDeleteBP {

	public void delete(AggFormulaCfgVO[] bills) {

		DeleteBPTemplate<AggFormulaCfgVO> bp = new DeleteBPTemplate<AggFormulaCfgVO>(ItfformulacfgPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggFormulaCfgVO> processer) {
		// TODO ǰ����
		IRule<AggFormulaCfgVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggFormulaCfgVO> processer) {
		// TODO �����

	}
}
