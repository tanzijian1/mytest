package nc.impl.tg.bpm;

import nc.bs.dao.BaseDAO;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.itf.tg.outside.IUpdateVOService;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class UpdateVOServiceImpl implements IUpdateVOService{

	@Override
	public AggregatedValueObject UpdateVO(Class<?> class1,AggregatedValueObject billvo) throws Exception {
		if(AggFinancexpenseVO.class.isAssignableFrom(class1)){
			BillUpdate<AggFinancexpenseVO> update = new BillUpdate<AggFinancexpenseVO>();
			AggFinancexpenseVO[] aggvos = update.update(new AggFinancexpenseVO[]{(AggFinancexpenseVO) billvo}, new AggFinancexpenseVO[]{(AggFinancexpenseVO) billvo});
			return aggvos[0];
		}else if(AggRePayReceiptBankCreditVO.class.isAssignableFrom(class1)){
			BillUpdate<AggRePayReceiptBankCreditVO> update = new BillUpdate<AggRePayReceiptBankCreditVO>();
			AggRePayReceiptBankCreditVO[] aggvos = update.update(new AggRePayReceiptBankCreditVO[]{(AggRePayReceiptBankCreditVO) billvo}, new AggRePayReceiptBankCreditVO[]{(AggRePayReceiptBankCreditVO) billvo});
			return aggvos[0];
		}else if(AggAddTicket.class.isAssignableFrom(class1)){
			BillUpdate<AggAddTicket> update = new BillUpdate<AggAddTicket>();
			AggAddTicket[] aggvos = update.update(new AggAddTicket[]{(AggAddTicket) billvo}, new AggAddTicket[]{(AggAddTicket) billvo});
			return aggvos[0];
		}
		return billvo;
	}

}
