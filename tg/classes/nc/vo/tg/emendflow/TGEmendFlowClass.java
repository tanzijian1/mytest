package nc.vo.tg.emendflow;

import nc.bs.pub.pf.IEmenedFlow;
import nc.vo.tg.enumeration.OperateStatus;

public class TGEmendFlowClass implements IEmenedFlow {

	@Override
	public String getDisplayName(Enum e) {
		return e.toString();
	}

	@Override
	public Enum[] getEnums() {
		Enum[] enums = new Enum[1];
		enums[0] = OperateStatus.Emend;
		return enums;
	}

	@Override
	public int getIntValue(Enum e) {
		return ((OperateStatus) e).getIntValue();
	}

}
