package nc.ui.tg.taxcalculation.ace.handler;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;

public class TaxBodyAfterEditHandler implements IAppEventHandler<CardBodyAfterEditEvent>{

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		// TODO �Զ����ɵķ������
		int i=e.getBillCardPanel().getBillTable().getSelectedRow();
		if("def2".equals(e.getKey())){//���ڱ��
			String def2=(String)e.getBillCardPanel().getBodyItem("def2").getValueObject();
			e.getBillCardPanel().getBodyItem("def6").setValue(def2);
			e.getBillCardPanel().getBillModel("id_taxcalculationbody").setValueAt(def2, i, "def6");
		}else if("def6".equals(e.getKey())){//��������
			String def6=(String)e.getBillCardPanel().getBodyItem("def6").getValueObject();
			e.getBillCardPanel().getBodyItem("def2").setValue(def6);
			e.getBillCardPanel().getBillModel("id_taxcalculationbody").setValueAt(def6, i, "def2");
		}
	}

}
