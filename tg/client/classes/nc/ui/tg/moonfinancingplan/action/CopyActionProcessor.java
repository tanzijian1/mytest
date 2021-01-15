package nc.ui.tg.moonfinancingplan.action;

import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;
import nc.vo.uif2.LoginContext;

public class CopyActionProcessor implements
		ICopyActionProcessor<AggMoonFinancingPlan> {

	@Override
	public void processVOAfterCopy(AggMoonFinancingPlan paramT,
			LoginContext paramLoginContext) {
		//Ԫ���������˶�̬����
		//paramT.getParentVO().setAttributeValue("PrimaryKey", null);
		//paramT.getParentVO().setAttributeValue("Modifier", null);
		//paramT.getParentVO().setAttributeValue("Modifiedtime", null);
		//paramT.getParentVO().setAttributeValue("Creator", null);
		//paramT.getParentVO().setAttributeValue("Creationtime", null);
		//paramT.getParentVO().setAttributeValue("billno", null);
		paramT.getParentVO().setPrimaryKey(null);
		paramT.getParentVO().setModifier(null);
    	paramT.getParentVO().setModifiedtime(null);
    	paramT.getParentVO().setCreator(null);
    	paramT.getParentVO().setCreationtime(null);	
    	paramT.getParentVO().setBillno(null);	
		//TODO ������Ҫҵ���Լ����䴦�����
		String[] codes =paramT.getTableCodes();
		if (codes != null && codes.length>0) {
			for (int i = 0; i < codes.length; i++) {
				String tableCode = codes[i];
				 CircularlyAccessibleValueObject[] childVOs = 	paramT.getTableVO(tableCode);
				 for (CircularlyAccessibleValueObject childVO : childVOs) {
					 try {
						childVO.setPrimaryKey(null);
					} catch (BusinessException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
			}
		}
	}
}
