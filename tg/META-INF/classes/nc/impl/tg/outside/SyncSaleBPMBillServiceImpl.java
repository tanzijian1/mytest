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
		//�ѿط������-05:���÷ѱ���-�ز�;06:���÷ѱ���-��ҵ;07:���÷ѱ���-��ҵ;08:�Ǻ�ͬ�������-�ز�;09:�Ǻ�ͬ�������-��ҵ;10:�Ǻ�ͬ�������-��ҵ				
		//�ѿط������-11:��ͬ�������-�ز�;12:��ͬ�������-��ҵ;13:��ͬ�������-��ҵ;				
		String[] strs=new String[]{"05","06","07","08","09","10","11","12","13"};
		String[] strsSale = new String[]{"01","02","03","04","20"};
		String[] finSale = new String[]{"15","16","17","18","19"};
		String[] budget = new String[]{"sd01"};//�������� sd01
		String[] finII = new String[]{"24"};//���ʶ���
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
			//result = SyncSaleBPMBillStatesUtils.getUtils().onSyncBudgetBillState(json);//bpm����NC�������̵���
		}else if("22".equals(vo.getBilltypeName()) || "23".equals(vo.getBilltypeName())){//Ԥ�������,Ԥ�������
			result = SyncSaleBPMBillStatesUtils.getUtils().onSyncAdjustBillState(vo,json);
		}else if(listFinII.contains(vo.getBilltypeName())){
			result = SyncSaleBPMBillStatesUtils.getUtils().onSyncMarketBillState(vo);
		}
		return result;
	}
}
