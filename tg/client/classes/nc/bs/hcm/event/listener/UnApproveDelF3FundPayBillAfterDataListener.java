package nc.bs.hcm.event.listener;

import java.util.Collection;
import java.util.List;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.BusinessEvent.BusinessUserObj;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.uif.pub.IUifService;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class UnApproveDelF3FundPayBillAfterDataListener extends EBSBillUtils
		implements IBusinessListener{

	@Override
	public void doAction(IBusinessEvent event) throws BusinessException {
		Object obj = ((BusinessUserObj) event.getUserObject()).getUserObj();
		if (obj == null) {
			return;
		}
		AggregatedValueObject[] aggPayBillVOs = (AggregatedValueObject[]) obj;

		try {
			for (AggregatedValueObject aggregatedValueObject : aggPayBillVOs) {
				AggPayBillVO billVO = (AggPayBillVO) aggregatedValueObject;
				PayBillVO headVo = (PayBillVO) billVO.getParentVO();
				String pkTradeType = headVo.getPk_tradetype();
				//�ж��Ƿ�Ϊ�籣����������𸶿
				if("F3-Cxx-030".equals(pkTradeType) || "F3-Cxx-031".equals(pkTradeType)){
					List<AggPayableBillVO> billVOs = (List<AggPayableBillVO>) NCLocator
							.getInstance()
							.lookup(IMDPersistenceQueryService.class)
							.queryBillOfVOByCond(
									AggPayableBillVO.class,
									"nvl(dr,0)=0 and def2 ='"
											+ aggregatedValueObject.getParentVO()
													.getAttributeValue("def2")
													.toString() + "'", false);
					
					if(billVOs.size() >0){
						throw new BusinessException("�õ��ݴ������ε��ݣ���ɾ�����ε��ݺ���ȡ������");
					}
					
					/*for (AggPayableBillVO billvo : billVOs) {
						NCLocator.getInstance().lookup(IUifService.class)
								.deleteBill(billvo);
					}*/
				}
			}
		} catch (Exception e) {
			Logger.error(e);
			throw new BusinessException("ȡ������ʧ�ܣ�" + e.getMessage());
		}
	}
}
