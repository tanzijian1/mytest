package nc.bs.tg.addticket.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tgfp.pub.voucher.FPAbstractReflector;

public class AddticketWorkReflectorimpl extends FPAbstractReflector<AggAddTicket> {
	
	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_ticket";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals("RZ30");
	}

	@Override
	public Class<AggAddTicket> getBillClass() {
		// TODO �Զ����ɵķ������
		return AggAddTicket.class;
	}

	

}
