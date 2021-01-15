package nc.impl.tg;

import nc.impl.pub.ace.AcePaymentRequestPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.paymentrequest.Payablepage;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.itf.tg.IPaymentRequestMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ISuperVO;

public class PaymentRequestMaintainImpl extends AcePaymentRequestPubServiceImpl
		implements IPaymentRequestMaintain {

	@Override
	public void delete(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		//É¾³ýÓ¦¸¶µ¥Ò³Ç©vo
				for(AggPayrequest aggvo:clientFullVOs){
					aggvo.setChildren(Payablepage.class, null);
				}
				for(AggPayrequest aggvo:originBills){
					aggvo.setChildren(Payablepage.class, null);
					}
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggPayrequest[] insert(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		//É¾³ýÓ¦¸¶µ¥Ò³Ç©vo
		for(AggPayrequest aggvo:clientFullVOs){
			aggvo.setChildren(Payablepage.class, null);
		}
		for(AggPayrequest aggvo:originBills){
			aggvo.setChildren(Payablepage.class, null);
			}
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggPayrequest[] update(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException { 
		//É¾³ýÓ¦¸¶µ¥Ò³Ç©vo
		for(AggPayrequest aggvo:clientFullVOs){
			aggvo.setChildren(Payablepage.class, null);
		}
		for(AggPayrequest aggvo:originBills){
			aggvo.setChildren(Payablepage.class, null);
			}
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggPayrequest[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggPayrequest[] save(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		//É¾³ýÓ¦¸¶µ¥Ò³Ç©vo
		for(AggPayrequest aggvo:clientFullVOs){
			aggvo.setChildren(Payablepage.class, null);
		}
		for(AggPayrequest aggvo:originBills){
			aggvo.setChildren(Payablepage.class, null);
			}
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggPayrequest[] unsave(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		//É¾³ýÓ¦¸¶µ¥Ò³Ç©vo
		for(AggPayrequest aggvo:clientFullVOs){
			aggvo.setChildren(Payablepage.class, null);
		}
		for(AggPayrequest aggvo:originBills){
			aggvo.setChildren(Payablepage.class, null);
			}
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggPayrequest[] approve(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		//É¾³ýÓ¦¸¶µ¥Ò³Ç©vo
		for(AggPayrequest aggvo:clientFullVOs){
			aggvo.setChildren(Payablepage.class, null);
		}
		for(AggPayrequest aggvo:originBills){
			aggvo.setChildren(Payablepage.class, null);
			}
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggPayrequest[] unapprove(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		//É¾³ýÓ¦¸¶µ¥Ò³Ç©vo
		for(AggPayrequest aggvo:clientFullVOs){
			aggvo.setChildren(Payablepage.class, null);
		}
		for(AggPayrequest aggvo:originBills){
			aggvo.setChildren(Payablepage.class, null);
			}
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
