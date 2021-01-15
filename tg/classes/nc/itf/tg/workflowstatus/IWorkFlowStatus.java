package nc.itf.tg.workflowstatus;

import nc.vo.ep.bx.JKBXVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public interface IWorkFlowStatus {
   public boolean queryActInsByPrceInsPK_RequiresNew(AggregatedValueObject aggvo,
			String type, String billtype) throws Exception ;
   /**
    * 收回调用BPM和影像删除接口
    * @param vo
    * @return
    * @throws BusinessException
    */
   public JKBXVO recall_RequiresNew(JKBXVO vo) throws BusinessException ;
}
