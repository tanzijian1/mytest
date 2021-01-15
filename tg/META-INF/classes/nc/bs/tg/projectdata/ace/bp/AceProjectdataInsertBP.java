package nc.bs.tg.projectdata.ace.bp;

import nc.bs.tg.projectdata.plugin.bpplugin.ProjectdataPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.tg.bd.pub.IBillNbcrCodeConst;
import nc.vo.tg.projectdata.AggProjectDataVO;
import nc.vo.tg.projectdata.ProjectDataVO;

/**
 * ��׼��������BP
 */
public class AceProjectdataInsertBP {

	public AggProjectDataVO[] insert(AggProjectDataVO[] bills) {

		InsertBPTemplate<AggProjectDataVO> bp = new InsertBPTemplate<AggProjectDataVO>(
				ProjectdataPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * ���������
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggProjectDataVO> processor) {
		// TODO ���������
		IRule<AggProjectDataVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCbilltype(IBillNbcrCodeConst.NBCR_PROJECTDATA);
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem(ProjectDataVO.CODE);
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem(ProjectDataVO.PK_GROUP);
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setOrgItem(ProjectDataVO.PK_GROUP);
		processor.addAfterRule(rule);
	}

	/**
	 * ����ǰ����
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggProjectDataVO> processer) {
		// TODO ����ǰ����
		IRule<AggProjectDataVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setCbilltype(IBillNbcrCodeConst.NBCR_PROJECTDATA);
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setCodeItem(ProjectDataVO.CODE);
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setGroupItem(ProjectDataVO.PK_GROUP);
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setOrgItem(ProjectDataVO.PK_GROUP);
		processer.addBeforeRule(rule);
	}
}
