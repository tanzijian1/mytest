package nc.ui.tg.standard.ace.processor;

import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.standard.AggStandardVO;
import nc.vo.tg.standard.StandardVO;
import nc.vo.uif2.LoginContext;

public class CopyActionProcessor implements ICopyActionProcessor<AggStandardVO> {

	@Override
	public void processVOAfterCopy(AggStandardVO billVO, LoginContext context) {
		billVO.getParentVO().setPrimaryKey(null);
		billVO.getParentVO().setAttributeValue(StandardVO.CODE, null);
		billVO.getParentVO()
				.setAttributeValue(
						StandardVO.PERIODYEAR,
						String.valueOf(AppContext.getInstance().getBusiDate()
								.getYear()));
		billVO.getParentVO().setAttributeValue(StandardVO.ENABLESTATE, 2);
		billVO.getParentVO().setAttributeValue(StandardVO.CREATOR,
				AppContext.getInstance().getPkUser());
		billVO.getParentVO().setAttributeValue(StandardVO.CREATIONTIME,
				AppContext.getInstance().getBusiDate());
		billVO.getParentVO().setAttributeValue(StandardVO.MODIFIEDTIME, null);
		billVO.getParentVO().setAttributeValue(StandardVO.MODIFIEDTIME, null);

	}

}
