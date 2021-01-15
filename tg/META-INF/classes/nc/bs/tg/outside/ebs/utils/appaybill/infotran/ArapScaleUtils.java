package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.sf.allocate.AggAllocateVO;


public class ArapScaleUtils {
	 private static Map<String, ScaleUtils> map =
		      new HashMap<String, ScaleUtils>();

		  private ArapScaleUtils() {
		    //
		  }

		  public static ScaleUtils getScaleUtils(AggPayableBillVO aggvo
				  ) {
		    String pk_group =
		       (String) aggvo.getParentVO().getAttributeValue("pk_group");
		    ScaleUtils ret = null;
		    synchronized (ArapScaleUtils.map) {
		      ret = ArapScaleUtils.map.get(pk_group);
		      if (ret == null) {
		        ret = new ScaleUtils(pk_group);
		         map.put(pk_group, ret);
		      }
		    }
		    return ret;
		  }
		  
		  public static ScaleUtils getScaleUtils(CircularlyAccessibleValueObject headvo
				  ) {
		    String pk_group =
		       (String) headvo.getAttributeValue("pk_group");
		    ScaleUtils ret = null;
		    synchronized (ArapScaleUtils.map) {
		      ret = ArapScaleUtils.map.get(pk_group);
		      if (ret == null) {
		        ret = new ScaleUtils(pk_group);
		         map.put(pk_group, ret);
		      }
		    }
		    return ret;
		  }
		  
		  /**
		   * 资金下拨单合计
		   * @param AggAllocateVO
		   * @param headAmountField
		   * @param bodyAmountField
		   */
		public static void calcTotalAmountByFields(AggAllocateVO aggvo, String headAmountField,
					String bodyAmountField,List list) {
			//先置为空，这样如果表体均为空，则合计也为空
			UFDouble totalAmount = null;
		
			for (int i = 0; i < list.size(); i++) {
//				UFDouble bodyAmount = (UFDouble) billModel.getValueAt(i, bodyAmountField);
				UFDouble bodyAmount	=(UFDouble) aggvo.getChildrenVO()[i].getAttributeValue(bodyAmountField);
				if (bodyAmount != null) {
					if(totalAmount == null) {
						totalAmount = UFDouble.ZERO_DBL;
					}
					totalAmount = totalAmount.add(bodyAmount);
				}
			}
			
			aggvo.getParentVO().setAttributeValue(headAmountField, totalAmount);
		}

}
