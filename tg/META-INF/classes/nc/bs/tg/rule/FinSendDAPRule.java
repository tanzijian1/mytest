package nc.bs.tg.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
@SuppressWarnings("rawtypes")
public class FinSendDAPRule implements IRule {

	@Override
	public void process(Object[] vos) {
		completeContStatus((AbstractBill[])vos);
		
	}

	private void completeContStatus(AbstractBill[] vos){
		TGSendVoucherUtil  util =new TGSendVoucherUtil();
		for(AbstractBill vo: vos){
				try {
					util.addVoucher(vo);
				} catch (BusinessException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			
		}
	}
}
