package nc.itf.tg.outside;

import java.util.Map;

import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.outside.bpm.NcToBpmVO;

public interface IPushBPMBillFileService {

	/**
	 * 推送bpm归档方法(有报错)
	 * @param aggvo
	 * @param pk_settlement 
	 * @throws Exception
	 */
	public Map<String,Object> pushBPMBillFile(AggPayBillVO aggvo, String pk_settlement) throws Exception;
	/**
	 * 推送BPM公用归档方法
	 * @param vo
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> pushBillToBpm(NcToBpmVO vo, String pk_settlement) throws Exception;
	public Map<String, Object> pushBillToBpm_RequiresNew(NcToBpmVO vo, String pk_settlement) throws Exception;
	/**
	 * 保存日志信息
	 * @param vo
	 * @throws Exception
	 */
	public void saveLog_RequiresNew(SuperVO vo) throws Exception;

	/**
	 * NC删单后通知bpm
	 * @param aggvo
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> pushBPMBillDelete(AggPayBillVO aggvo) throws Exception;
	
	/**
	 * 推送bpm归档方法(没报错)
	 * @param string
	 * @param pk_settlement 
	 * @throws Exception
	 */
	public Map<String, Object> pushBPMBillFileNone(String pk_paybill,
			String pk_settlement) throws Exception;
	
	/**
	 * 推送融资bpm归档方法(没报错)
	 * @param string
	 * @param pk_settlement 
	 * @throws Exception
	 */
	public Map<String, Object> pushBPMBillFileNone(Map<String, Object> map,
			String number) throws Exception;
	
	/**
	 * 融资推送bpm审核或弃审,拒绝
	 * @param bill
	 * @param billcode
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public AggregatedValueObject pushToFinBpmFile(AbstractBill bill,String billcode, String pk_settlement) throws Exception;
	/**
	 * 推送bpm后，bpm审批前收回通知bpm
	 * @param aggvo
	 * @return
	 * @throws Exception
	 */
	public Object pushRetrieveBpmFile(AggPayBillVO aggvo) throws Exception;

	AggPayBillVO pushReturnBillToBpmInitiator(AggPayBillVO aggvo)
			throws Exception;

	

	/**
	 * 融资单据删除通知bpm
	 * @param aggvo
	 * @return
	 * @throws Exception 
	 */
	AbstractBill pushToFinBpmDeleteFile(AbstractBill bill, String billcode) throws Exception;
	
	/**
	 * 删除或退回时通知BPM
	 * @param aggvo
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> pushBPMBillBackOrDelete(NcToBpmVO vo) throws Exception;
	
	/**
	 * 主动收回通知bpm
	 * @param proname bpm流程名
	 * @param bpmid bpm主键
	 * @throws Exception
	 */
	public void pushRetrieveBpmFile(String proname,String bpmid) throws Exception;
}
