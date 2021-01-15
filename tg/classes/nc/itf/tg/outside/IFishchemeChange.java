package nc.itf.tg.outside;

import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;

public interface IFishchemeChange {
public	AggFISchemeversionHVO deleteVersionvo(AggFIScemeHVO aggvos);
public	AggFISchemeversionHVO addVersionvo(AggFIScemeHVO aggvos);
}
