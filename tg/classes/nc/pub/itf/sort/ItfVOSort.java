package nc.pub.itf.sort;

import java.util.Comparator;

import nc.itf.tg.outside.ItfConstants;
import nc.vo.baseapp.itfformulacfg.FormulaCfgBVO;
/**
 * @Description: VO≈≈–Ú
 */
@SuppressWarnings("rawtypes")
public class ItfVOSort implements Comparator {

	private int direction = -1;
	public ItfVOSort(int direction){
		this.direction = direction;
	}
	
	@Override
	public int compare(Object vo1, Object vo2) {
		if(vo1 instanceof FormulaCfgBVO){
			FormulaCfgBVO bVO1 = (FormulaCfgBVO)vo1;
			FormulaCfgBVO bVO2 = (FormulaCfgBVO)vo2;
			Integer exe_order1 = 1;
			Integer exe_order2 = 1;
			if(ItfConstants.DIRECTION_WB2NC==direction){
				exe_order1 = (int)(bVO1.getAttributeValue("exe_order_wn")==null?1:bVO1.getAttributeValue("exe_order_wn"));
				exe_order2 = (int)(bVO2.getAttributeValue("exe_order_wn")==null?1:bVO2.getAttributeValue("exe_order_wn"));
				exe_order1 = exe_order1==null?1:exe_order1;
				exe_order2 = exe_order2==null?1:exe_order2;
			}else if(ItfConstants.DIRECTION_NC2WB==direction){
				exe_order1 = (int)(bVO1.getAttributeValue("exe_order_nw")==null?1:bVO1.getAttributeValue("exe_order_nw"));
				exe_order2 = (int)(bVO2.getAttributeValue("exe_order_nw")==null?1:bVO2.getAttributeValue("exe_order_nw"));
				exe_order1 = exe_order1==null?1:exe_order1;
				exe_order2 = exe_order2==null?1:exe_order2;
			}
			return exe_order1.compareTo(exe_order2);
		}
		return 0;
	}

}
