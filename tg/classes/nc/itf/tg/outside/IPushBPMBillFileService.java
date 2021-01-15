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
	 * ����bpm�鵵����(�б���)
	 * @param aggvo
	 * @param pk_settlement 
	 * @throws Exception
	 */
	public Map<String,Object> pushBPMBillFile(AggPayBillVO aggvo, String pk_settlement) throws Exception;
	/**
	 * ����BPM���ù鵵����
	 * @param vo
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> pushBillToBpm(NcToBpmVO vo, String pk_settlement) throws Exception;
	public Map<String, Object> pushBillToBpm_RequiresNew(NcToBpmVO vo, String pk_settlement) throws Exception;
	/**
	 * ������־��Ϣ
	 * @param vo
	 * @throws Exception
	 */
	public void saveLog_RequiresNew(SuperVO vo) throws Exception;

	/**
	 * NCɾ����֪ͨbpm
	 * @param aggvo
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> pushBPMBillDelete(AggPayBillVO aggvo) throws Exception;
	
	/**
	 * ����bpm�鵵����(û����)
	 * @param string
	 * @param pk_settlement 
	 * @throws Exception
	 */
	public Map<String, Object> pushBPMBillFileNone(String pk_paybill,
			String pk_settlement) throws Exception;
	
	/**
	 * ��������bpm�鵵����(û����)
	 * @param string
	 * @param pk_settlement 
	 * @throws Exception
	 */
	public Map<String, Object> pushBPMBillFileNone(Map<String, Object> map,
			String number) throws Exception;
	
	/**
	 * ��������bpm��˻�����,�ܾ�
	 * @param bill
	 * @param billcode
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public AggregatedValueObject pushToFinBpmFile(AbstractBill bill,String billcode, String pk_settlement) throws Exception;
	/**
	 * ����bpm��bpm����ǰ�ջ�֪ͨbpm
	 * @param aggvo
	 * @return
	 * @throws Exception
	 */
	public Object pushRetrieveBpmFile(AggPayBillVO aggvo) throws Exception;

	AggPayBillVO pushReturnBillToBpmInitiator(AggPayBillVO aggvo)
			throws Exception;

	

	/**
	 * ���ʵ���ɾ��֪ͨbpm
	 * @param aggvo
	 * @return
	 * @throws Exception 
	 */
	AbstractBill pushToFinBpmDeleteFile(AbstractBill bill, String billcode) throws Exception;
	
	/**
	 * ɾ�����˻�ʱ֪ͨBPM
	 * @param aggvo
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> pushBPMBillBackOrDelete(NcToBpmVO vo) throws Exception;
	
	/**
	 * �����ջ�֪ͨbpm
	 * @param proname bpm������
	 * @param bpmid bpm����
	 * @throws Exception
	 */
	public void pushRetrieveBpmFile(String proname,String bpmid) throws Exception;
}
