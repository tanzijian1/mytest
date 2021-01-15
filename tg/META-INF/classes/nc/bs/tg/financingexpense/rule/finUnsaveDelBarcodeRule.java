package nc.bs.tg.financingexpense.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;

public class finUnsaveDelBarcodeRule implements IRule<AggFinancexpenseVO> {

	@Override
	public void process(AggFinancexpenseVO[] vos) {
		// TODO 自动生成的方法存根
		for(AggFinancexpenseVO vo:vos){
			FinancexpenseVO hvo=vo.getParentVO();
			if(!("Y".equals(hvo.getAttributeValue("def47")))){
				vo.getParentVO().setAttributeValue("def21", null);
				vo.getParentVO().setAttributeValue("def20", null);
				vo.getParentVO().setAttributeValue("def19", null);
				hvo.setDef21(null);//清除影像编码
				hvo.setDef20(null);//清除bpm地址
				hvo.setDef19(null);//清除bpmid
			}
			hvo.setDef47(null);//清除bpm收回标识
		}
	}

}
