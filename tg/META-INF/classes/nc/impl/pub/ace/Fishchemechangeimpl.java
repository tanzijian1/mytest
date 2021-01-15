package nc.impl.pub.ace;

import nc.bs.tg.fischeme.ace.bp.AceFischemeChangeBP;
import nc.bs.tmpub.version.plugin.bpplugin.VersionPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.tmpub.version.VersionVOConvert;
import nc.itf.tg.outside.IFishchemeChange;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;

public class Fishchemechangeimpl implements IFishchemeChange {
public static AggFIScemeHVO[] aggvos=AceFischemeChangeBP.aggvos;
private DeleteBPTemplate<AggFISchemeversionHVO> deleteBp = new DeleteBPTemplate<AggFISchemeversionHVO>(VersionPluginPoint.DELETE);
	@Override
	public AggFISchemeversionHVO deleteVersionvo(AggFIScemeHVO aggvos) {
		// TODO 自动生成的方法存根
		VersionVOConvert<AggFIScemeHVO, AggFISchemeversionHVO> convert = new VersionVOConvert<AggFIScemeHVO, AggFISchemeversionHVO>();
		AggFISchemeversionHVO e = convert.convertBillToVersion(aggvos);
		deleteBp.delete(new AggFISchemeversionHVO[]{e});
		return e;
		}
	@Override
	public AggFISchemeversionHVO addVersionvo(AggFIScemeHVO aggvos) {
	InsertBPTemplate<AggFISchemeversionHVO> insertBp = new InsertBPTemplate<AggFISchemeversionHVO>(VersionPluginPoint.INSERT);
	VersionVOConvert<AggFIScemeHVO, AggFISchemeversionHVO> convert = new VersionVOConvert<AggFIScemeHVO, AggFISchemeversionHVO>();
	AggFISchemeversionHVO e = convert.convertBillToVersion(aggvos);
	insertBp.insert(new AggFISchemeversionHVO[]{e});
	// TODO 自动生成的方法存根
	return null;
}

}
