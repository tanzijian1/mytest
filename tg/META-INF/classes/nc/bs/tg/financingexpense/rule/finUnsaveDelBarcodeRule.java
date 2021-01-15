package nc.bs.tg.financingexpense.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;

public class finUnsaveDelBarcodeRule implements IRule<AggFinancexpenseVO> {

	@Override
	public void process(AggFinancexpenseVO[] vos) {
		// TODO �Զ����ɵķ������
		for(AggFinancexpenseVO vo:vos){
			FinancexpenseVO hvo=vo.getParentVO();
			if(!("Y".equals(hvo.getAttributeValue("def47")))){
				vo.getParentVO().setAttributeValue("def21", null);
				vo.getParentVO().setAttributeValue("def20", null);
				vo.getParentVO().setAttributeValue("def19", null);
				hvo.setDef21(null);//���Ӱ�����
				hvo.setDef20(null);//���bpm��ַ
				hvo.setDef19(null);//���bpmid
			}
			hvo.setDef47(null);//���bpm�ջر�ʶ
		}
	}

}
