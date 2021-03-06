package nc.ui.tg.financingexpense.action;

import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.uif2.LoginContext;

public class CopyActionProcessor implements
		ICopyActionProcessor<AggFinancexpenseVO> {

	@Override
	public void processVOAfterCopy(AggFinancexpenseVO paramT,
			LoginContext paramLoginContext) {
		paramT.getParentVO().setPrimaryKey(null);
		paramT.getParentVO().setAttributeValue("modifier", null);
    	paramT.getParentVO().setAttributeValue("modifiedtime", null);
    	paramT.getParentVO().setAttributeValue("creator",null);
    	paramT.getParentVO().setAttributeValue("creationtime",null);		
    	paramT.getParentVO().setAttributeValue("billno",null);		
    	paramT.getParentVO().setAttributeValue("approvestatus",-1);		
    	paramT.getParentVO().setAttributeValue("dbilldate",new UFDate());
    	paramT.getParentVO().setAttributeValue("def21",null);//影像编码
    	paramT.getParentVO().setAttributeValue("def19",null);//影像编码
		//TODO 根据需要业务自己补充处理清空
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
