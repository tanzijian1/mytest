package nc.bs.tg.outside.img.utils.contractcostcashout;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.img.utils.IMGBillUtils;
import nc.itf.tg.outside.IMGBillCont;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CcCashoutUtils extends IMGBillUtils {
	static CcCashoutUtils utils;

	public static CcCashoutUtils getUtils() {
		if (utils == null) {
			utils = new CcCashoutUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getIMGUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		// ����json����,��ȡ��ͷ����
		JSONObject jsonInfo = (JSONObject) value.get("Info");
		// ���ݺţ���ȡΨһ�ֶ�ֵ
		String imgNo = jsonInfo.getString("djbh");// Ӱ��ϵͳҵ�񵥾ݺ�

		// Ӱ��ϵͳҵ�񵥾ݵ��ݺ������п��ƺ���־���
		String billkey = IMGBillCont.getBillNameMap().get(billtype) + ":"
				+ imgNo;
		/*String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;*/

		if(jsonInfo.getString("zyx25") == null || "".equals(jsonInfo.getString("zyx25"))){
			throw new BusinessException("��Ʊ��α״̬����Ϊ��");
		}
		if(jsonInfo.getString("djbh") == null || "".equals(jsonInfo.getString("djbh"))){
			throw new BusinessException("���ݱ�Ų���Ϊ��");
		}
		
		// ���Ӱ��ϵͳҵ�񵥾��Ƿ����**************
		BXVO aggVO = (BXVO) getBillVO(
				BXVO.class, "isnull(dr,0)=0 and djbh = '" + imgNo
						+ "'");
		
		if (aggVO == null) {
			throw new BusinessException("��" + billkey + "��,NC�����ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ imgNo + "��,����ȷ�ϵ����Ƿ����");
		}

		BPMBillUtils.addBillQueue(billkey);// ���Ӷ��д���***************
		try {
			JSONObject jsonObj = (JSONObject) value.get("Info");
			if(jsonObj == null || jsonObj.size() == 0){
				throw new BusinessException("������Ϣ�����ڣ�����ȷ����Ϣ");
			}
			BXHeaderVO vo = (BXHeaderVO) aggVO.getParentVO();
			vo.setZyx14(jsonObj.getString("zyx14")); // ָ�����������
			vo.setZyx15(jsonObj.getString("zyx15")); // ָ���������ԭ��
			vo.setZyx18(jsonObj.getString("zyx18")); // ��ƱOCRʶ����
			vo.setZyx25(jsonObj.getString("zyx25")); // ��Ʊ��α״̬
			vo.setZyx25(jsonObj.getString("zyx30")); // ��Ʊ��αʧ��ԭ��
			int flag = getBaseDAO().updateVO(vo);
			
			if(flag == 0){
				throw new BusinessException("����ʧ�ܣ����鵥����Ϣ"); 
			}
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("billid", vo.getPrimaryKey());
			dataMap.put("billno", vo.getDjbh());
			//**************************
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billkey);
		}
	}
}
