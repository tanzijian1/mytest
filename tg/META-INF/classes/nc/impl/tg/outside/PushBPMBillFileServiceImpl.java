package nc.impl.tg.outside;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.tg.outside.salebpm.utils.SaleNoteBPMUtils;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.outside.bpm.NcToBpmVO;

public class PushBPMBillFileServiceImpl implements IPushBPMBillFileService{

	@Override
	public Map<String,Object> pushBPMBillFile(AggPayBillVO aggvo,String pk_settlement) throws Exception {
		Map<String,Object> map = SaleNoteBPMUtils.getUtils().pushBillToBpm(aggvo,pk_settlement);
		return map;
	}
	
	
	@Override
	public Map<String,Object> pushBPMBillFileNone(String pk_paybill,String pk_settlement) throws Exception {
		Map<String,Object> map = SaleNoteBPMUtils.getUtils().pushToBpmFileNone(pk_paybill,pk_settlement);
		return map;
	}

	@Override
	public Map<String, Object> pushBillToBpm(NcToBpmVO vo, String pk_settlement)
			throws Exception {
		Map<String,Object> map = SaleNoteBPMUtils.getUtils().pushToBpm(vo,pk_settlement);
		return map;
	}

	@Override
	public void saveLog_RequiresNew(SuperVO vo) throws Exception {
		BaseDAO dao = new BaseDAO();
		dao.insertVO(vo);
	}
	
	@Override
	public Map<String, Object> pushBPMBillDelete(AggPayBillVO aggvo)
			throws Exception {
		Map<String,Object> map = SaleNoteBPMUtils.getUtils().pushToBPMBillDelete(aggvo);
		return map;
	}


	@Override
	public AggregatedValueObject pushToFinBpmFile(AbstractBill bill,
			String billcode, String pk_settlement) throws Exception {
		AggregatedValueObject aggvo = SaleNoteBPMUtils.getUtils().pushToFinBpmFile(bill,billcode,pk_settlement);
		return aggvo;
	}


	@Override
	public Object pushRetrieveBpmFile(AggPayBillVO aggvo)
			throws Exception {
		Object object = SaleNoteBPMUtils.getUtils().pushRetrieveBpmFile(aggvo);
		return object;
	}
	
	@Override
	public AggPayBillVO pushReturnBillToBpmInitiator(AggPayBillVO aggvo)
			throws Exception {
		aggvo = SaleNoteBPMUtils.getUtils().pushReturnBillToBpmInitiator(aggvo);
		return aggvo;
	}


	@Override
	public AbstractBill pushToFinBpmDeleteFile(AbstractBill bill,
			String billcode) throws Exception {
		bill = SaleNoteBPMUtils.getUtils().pushToFinBpmDeleteFile(bill,billcode);
		return bill;
	}


	@Override
	public Map<String, Object> pushBPMBillFileNone(Map<String, Object> map,
			String number) throws Exception {
		Map<String,Object> map1 = SaleNoteBPMUtils.getUtils().pushBPMBillFileNone(map,number);
		return map1;
	}


	@Override
	public Map<String, Object> pushBPMBillBackOrDelete(NcToBpmVO vo)
			throws Exception {
		Map<String,Object> map = SaleNoteBPMUtils.getUtils().informBpmBackOrDelete(vo);
		return map;
	}


	@Override
	public Map<String, Object> pushBillToBpm_RequiresNew(NcToBpmVO vo,
			String pk_settlement) throws Exception {
		Map<String,Object> map = SaleNoteBPMUtils.getUtils().pushToBpm(vo,pk_settlement);
		return map;
	}


	@Override
	public void pushRetrieveBpmFile(String proname, String bpmid)
			throws Exception {
		SaleNoteBPMUtils.getUtils().pushRetrieveBpmFile(proname,bpmid);
	}
}
