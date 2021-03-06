package nc.ui.tg.salaryfundaccure.action;

import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.uif2.LoginContext;

public class CopyActionProcessor implements
		ICopyActionProcessor<AggSalaryfundaccure> {

	@Override
	public void processVOAfterCopy(AggSalaryfundaccure paramT,
			LoginContext paramLoginContext) {
		paramT.getParentVO().setPrimaryKey(null);
		paramT.getParentVO().setAttributeValue("modifier", null);
    	paramT.getParentVO().setAttributeValue("modifiedtime", null);
    	paramT.getParentVO().setAttributeValue("creator", null);
    	paramT.getParentVO().setAttributeValue("creationtime", null);		
		String[] codes =paramT.getTableCodes();
		if (codes != null && codes.length>0) {
			for (int i = 0; i < codes.length; i++) {
				String tableCode = codes[i];
				 CircularlyAccessibleValueObject[] childVOs = 	paramT.getTableVO(tableCode);
				 for (CircularlyAccessibleValueObject childVO : childVOs) {
					 try {
						childVO.setPrimaryKey(null);
					} catch (BusinessException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
		}
	}
}
