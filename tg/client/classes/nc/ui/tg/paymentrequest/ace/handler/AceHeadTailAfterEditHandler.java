package nc.ui.tg.paymentrequest.ace.handler;

import com.borland.dx.dataset.Column;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.pub.BusinessException;
/*
 * ���ݸ��������˻�����֧����Ϣ
 * gwj
 * 2019��9��6��17:46:46
 */
public class AceHeadTailAfterEditHandler implements IAppEventHandler<CardHeadTailAfterEditEvent>{

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		// TODO �Զ����ɵķ������
		BillCardPanel panel = e.getBillCardPanel();
		if(e.getKey().equals("payaccount")){
			String payaccount = (String) panel.getHeadItem("payaccount").getValueObject();
			IUAPQueryBS  bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String result = null;
			try {
				result = (String)bs.executeQuery("select name from bd_bankaccsub  where pk_bankaccsub ='"+payaccount+"'and nvl(dr,0)=0", new ColumnProcessor());
				panel.setHeadItem("def13", result);
			} catch (BusinessException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}
	}

	
}