package nc.impl.tg.outside;

import java.util.Arrays;
import java.util.List;

import nc.bs.tg.outside.salebpm.utils.BpmAddBillSatesUtils;
import nc.bs.tg.outside.salebpm.utils.BpmBXBillStatesUtils;
import nc.bs.tg.outside.salebpm.utils.SyncSaleBPMBillStatesUtils;
import nc.itf.tg.outside.ISyncSaleBPMBillServcie;
import nc.vo.tg.outside.BPMBillStateParaVO;

public class SyncSaleBPMBillServiceImpl implements ISyncSaleBPMBillServcie {

	@Override
	public String onSyncBillState_RequiresNew(BPMBillStateParaVO vo,String json)
			throws Exception {
		//费控费用请款-05:差旅费报销-地产;06:差旅费报销-物业;07:差旅费报销-商业;08:非合同费用请款-地产;09:非合同费用请款-物业;10:非合同费用请款-商业				
		//费控费用请款-11:合同费用请款-地产;12:合同费用请款-物业;13:合同费用请款-商业;				
		String[] strs=new String[]{"05","06","07","08","09","10","11","12","13"};
		String[] strsSale = new String[]{"01","02","03","04","20"};
		String[] finSale = new String[]{"15","16","17","18","19"};
		String[] budget = new String[]{"sd01"};//盖章资料 sd01
		String[] finII = new String[]{"24"};//融资二期
		List<String> listSale = Arrays.asList(strsSale);
		List<String> list=Arrays.asList(strs);
		List<String> listFinSale=Arrays.asList(finSale);
		List<String> listBudget = Arrays.asList(budget);
		List<String> listFinII = Arrays.asList(finII);
		String result=null;
		if(list.contains(vo.getBilltypeName())){
			result =BpmBXBillStatesUtils.getUtils().onSyncBillState(vo.getBilltypeName(), json);
		}else if(listSale.contains(vo.getBilltypeName())){
			result = SyncSaleBPMBillStatesUtils.getUtils().onSyncBillState(vo);
		}else if("14".equals(vo.getBilltypeName())){
			result = BpmAddBillSatesUtils.getUtils().onSyncBillState(vo);
		}else if(listFinSale.contains(vo.getBilltypeName())){
			result = SyncSaleBPMBillStatesUtils.getUtils().onSyncFinBillState(vo,json);
		}else if(listBudget.contains(vo.getBilltypeName())){
			//result = SyncSaleBPMBillStatesUtils.getUtils().onSyncBudgetBillState(json);//bpm推生NC盖章流程单据
		}else if("22".equals(vo.getBilltypeName()) || "23".equals(vo.getBilltypeName())){//预算调整单,预算调剂单
			result = SyncSaleBPMBillStatesUtils.getUtils().onSyncAdjustBillState(vo,json);
		}else if(listFinII.contains(vo.getBilltypeName())){
			result = SyncSaleBPMBillStatesUtils.getUtils().onSyncMarketBillState(vo);
		}
		return result;
	}
}
