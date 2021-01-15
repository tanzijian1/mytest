package nc.bs.tg.fischeme.ace.bp;

import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.bs.tmpub.version.VersionInsertBP;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;

/**
 * ��׼��������BP
 */
public class AceFischemeInsertBP {

	public AggFIScemeHVO[] insert(AggFIScemeHVO[] bills) {

		InsertBPTemplate<AggFIScemeHVO> bp = new InsertBPTemplate<AggFIScemeHVO>(
				FischemePluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		//�����Զ���Ӱ汾
		bills =bp.insert(bills);
/*		VersionInsertBP<AggFIScemeHVO, AggFISchemeversionHVO> verBp = new VersionInsertBP<AggFIScemeHVO, AggFISchemeversionHVO>();
		AggFIScemeHVO[] aggVerVO = verBp.insert(bills, null);*/
		return bills;

	}

	/**
	 * ���������
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggFIScemeHVO> processor) {
		// TODO ���������
		IRule<AggFIScemeHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("RZ05");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processor.addAfterRule(rule);
	}

	/**
	 * ����ǰ����
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggFIScemeHVO> processer) {
		// TODO ����ǰ����
		IRule<AggFIScemeHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCbilltype("RZ05");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setOrgItem("pk_org");
		processer.addBeforeRule(rule);
	}
}
