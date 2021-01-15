package nc.bs.tg.fischeme.ace.bp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import nc.bs.framework.common.NCLocator;
import nc.bs.tg.fischeme.ace.rule.FischemeChangeAfterRule;
import nc.bs.tg.fischeme.ace.rule.ToProjectdataRule;
import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.bs.tmpub.version.VersionInsertBP;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.IFischemeMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;
import nc.vo.tg.projectdata.ProjectDataVVO;

/**
 * 修改保存的BP
 * 
 */
public class AceFischemeChangeBP {
public static AggFIScemeHVO[] aggvos;
	public AggFIScemeHVO[] change(AggFIScemeHVO[] bills,
			AggFIScemeHVO[] originBills) {
		
//		 调用修改模板
		UpdateBPTemplate<AggFIScemeHVO> bp = new UpdateBPTemplate<AggFIScemeHVO>(
				FischemePluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		//return bp.update(bills, originBills);
		//修改自动添加版本
		
		
		ArrayList<AggFIScemeHVO> list=new ArrayList<AggFIScemeHVO>();
		for(AggFIScemeHVO vo:bills){
			((FIScemeHVO)vo.getParent()).setAttributeValue("approvestatus","-1");
			((FIScemeHVO)vo.getParent()).setAttributeValue("emendenum","0");
		}
		bills =bp.update(bills, originBills);
		
		this.aggvos=bills;
/*		for(AggFIScemeHVO aggvo:bills){
			if(("Y").equals(((FIScemeHVO)aggvo.getParent()).getAttributeValue("vdef10"))){
		VersionInsertBP<AggFIScemeHVO, AggFISchemeversionHVO> verBp = new VersionInsertBP<AggFIScemeHVO, AggFISchemeversionHVO>();
		AggFIScemeHVO[] aggVerVO = verBp.insert(new AggFIScemeHVO[]{aggvo}, null);
		list.addAll(Arrays.asList(aggVerVO));
		}
		}*/
		return bills;
	}
   
	private void addAfterRule(CompareAroundProcesser<AggFIScemeHVO> processer) {
		// TODO 后规则
		IRule<AggFIScemeHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("RZ05");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
		.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
		.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);
		ToProjectdataRule rulea=new ToProjectdataRule();
		processer.addAfterRule(rulea);
	}

	private void addBeforeRule(CompareAroundProcesser<AggFIScemeHVO> processer) {
		// TODO 前规则
		IRule<AggFIScemeHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggFIScemeHVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
		.setCbilltype("RZ05");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
		.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
		.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
		.setOrgItem("pk_org");
		FischemeChangeAfterRule rule1=new FischemeChangeAfterRule();
		processer.addBeforeRule(rule1);
		processer.addBeforeRule(ruleCom);
	}

}

