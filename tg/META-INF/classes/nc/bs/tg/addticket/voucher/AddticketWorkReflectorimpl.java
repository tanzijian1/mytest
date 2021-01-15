package nc.bs.tg.addticket.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tgfp.pub.voucher.FPAbstractReflector;

public class AddticketWorkReflectorimpl extends FPAbstractReflector<AggAddTicket> {
	
	@Override
	public String getPKFieldName() {
		// TODO 自动生成的方法存根
		return "pk_ticket";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO 自动生成的方法存根
		return relationInfoVO.getPk_billtype().equals("RZ30");
	}

	@Override
	public Class<AggAddTicket> getBillClass() {
		// TODO 自动生成的方法存根
		return AggAddTicket.class;
	}

	

}
