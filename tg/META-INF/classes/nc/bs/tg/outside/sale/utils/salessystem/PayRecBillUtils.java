package nc.bs.tg.outside.sale.utils.salessystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.sale.utils.receipt.ReceiptUtils;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PayRecBillUtils extends SaleBillUtils {
	static PayRecBillUtils utils;
	public static PayRecBillUtils newInstanceof(){
		if(utils==null){
			utils=new PayRecBillUtils();
		}
		return utils;
	}
	
	public String onSyncBill(HashMap<String, Object> value, String dectype)
			throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		JSONArray pjsonarray=(JSONArray)value.get("paybill");//ת�������
		JSONArray gjsonarray=(JSONArray)value.get("gatherbill");//ת�����տ
//		if(pjsonarray==null||pjsonarray.size()<1)throw new BusinessException("ת�����������Ϊ��");
//		if(gjsonarray==null||gjsonarray.size()<1)throw new BusinessException("ת�����տ����Ϊ��");
//		if(gjsonarray.size()!=pjsonarray.size())throw new BusinessException("ת�����տ�븶���Ŀ�������");
		List<HashMap<String, Object>> list=new ArrayList<>();
		if(pjsonarray!=null){
		for(int i=0;i<pjsonarray.size();i++){
		//���ø���ӿ�
		String pjsonstr=JSON.toJSONString(pjsonarray.getJSONObject(i));
	    HashMap<String, Object> pmap = JSON.parseObject(pjsonstr, HashMap.class);
		String prtn=PayBillUtils.getUtils().onSyncBill(pmap, "F3");//���ø���ӿ�
		HashMap<String, Object> prmap=JSON.parseObject(prtn, HashMap.class);
		list.add(prmap);
		  }
		}
		//�����տ�ӿ�
		if(gjsonarray!=null){
		for(int i=0;i<gjsonarray.size();i++){
		String gjsonstr=JSON.toJSONString(gjsonarray.getJSONObject(i));
		HashMap<String, Object> gmap = JSON.parseObject(gjsonstr, HashMap.class);
		String grtn=ReceiptUtils.getUtils().onSyncBill(gmap, "F7");//�����տ�ӿ�
		HashMap<String, Object> grmap=JSON.parseObject(grtn, HashMap.class);
		list.add(grmap);
		  }
		}
		if(list.size()>0){
			return JSON.toJSONString(list);
		}
		return null ;
	}
	
	
}
