package nc.bs.tg.outside.sale.utils.exhousetransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.tgfn.exhousetransferbill.ExhousetransferbillBVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * ����ת�˵�(FN19)
 * @author ln
 *
 */
public class ExHouseTransferBillUtils extends SaleBillUtils {
	static ExHouseTransferBillUtils utils;

	public static ExHouseTransferBillUtils getUtils() {
		if (utils == null) {
			utils = new ExHouseTransferBillUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		// ����json����,��ȡ��ͷ����
		JSONObject headJson = (JSONObject) value.get("headInfo");
		// Saleid ��ʵ�ʴ�����Ϣλ�ý��б��
		String saleid = headJson.getString("def1");// ����ϵͳҵ�񵥾�ID
		String saleno = headJson.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�

		// ����ϵͳҵ�񵥾�ID������ϵͳҵ�񵥾ݵ��ݺ������п��ƺ���־���
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		// �������ϵͳҵ�񵥾�IDΨһ��
		AggExhousetransferbillHVO aggVO = (AggExhousetransferbillHVO) getBillVO(
				AggExhousetransferbillHVO.class, "isnull(dr,0)=0 and def1 = '" + saleid
						+ "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String) aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}

		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			// ת������ΪVO
			//*********************************
			ExHouseTransferBillConvertor convertor = new ExHouseTransferBillConvertor();
			//Ĭ�ϼ���PK
			convertor.setDefaultGroup("000112100000000005FD");
			// ���ñ���key������ӳ��
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("exhousetransferbillBVO", "����ת�˵�����");
			convertor.setBVOKeyName(bVOKeyName);
			
			// ���ñ�ͷkey������ӳ��
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("exhousetransferbillHVO", "����ת�˵���ͷ");
			convertor.setHVOKeyName(hVOKeyName);
			
			//��ͷ��ֵУ���ֶ�
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hExhousetransferbillHVOKeyName= new HashMap<String, String>();
			hExhousetransferbillHVOKeyName.put("def1", "����ϵͳ����ID");
			hExhousetransferbillHVOKeyName.put("def2", "����ϵͳ����");
			hExhousetransferbillHVOKeyName.put("def3", "Ӱ�����");
			hExhousetransferbillHVOKeyName.put("def4", "Ӱ��״̬");
			hExhousetransferbillHVOKeyName.put("billdate", "��������");
			hExhousetransferbillHVOKeyName.put("pk_org", "������֯");
			hValidatedKeyName.put("exhousetransferbillHVO", hExhousetransferbillHVOKeyName);
			
			//�����ֵУ���ֶ�
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> bExhousetransferbillBVOKeyName = new HashMap<String, String>();
//			bExhousetransferbillBVOKeyName.put("scomment", "ժҪ");
			bExhousetransferbillBVOKeyName.put("def1", "������Ŀ");
//			bExhousetransferbillBVOKeyName.put("def2", "���򷿼�");
			bExhousetransferbillBVOKeyName.put("def3", "��������");
			bExhousetransferbillBVOKeyName.put("def4", "��������");
			bExhousetransferbillBVOKeyName.put("def5", "ҵ̬");
			bExhousetransferbillBVOKeyName.put("def6", "��˰��ʽ");
			bExhousetransferbillBVOKeyName.put("def7", "˰��");
			bExhousetransferbillBVOKeyName.put("def8", "˰��");
			bExhousetransferbillBVOKeyName.put("def9", "������ҽ�����˰��");
			bValidatedKeyName.put("exhousetransferbillBVO", bExhousetransferbillBVOKeyName);
			
			//���������ֶ�
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("exhousetransferbillHVO-pk_org");
			refKeys.add("exhousetransferbillHVO-billmaker");
			
			convertor.sethValidatedKeyName(hValidatedKeyName);
			convertor.setbValidatedKeyName(bValidatedKeyName);
			convertor.setRefKeys(refKeys);
			
			//*********************************
			AggExhousetransferbillHVO billvo = (AggExhousetransferbillHVO) convertor.castToBill(value, AggExhousetransferbillHVO.class,aggVO);
			
			//����NCĬ����Ϣ
//			billvo.getParentVO().setTranstype("FN19");
			billvo.getParentVO().setBilltype("FN19");
			billvo.getParentVO().setBillstatus(-1);
			billvo.getParentVO().setApprovestatus(-1);
			billvo.getParentVO().setEffectstatus(0);
			billvo.getParentVO().setDef10(getUserByPsondoc(billvo.getParentVO().getDef10()));//��������
			billvo.getParentVO().setTs(new UFDateTime());
			billvo.getParentVO().setDef5(headJson.getString("def5"));//
			billvo.getParentVO().setDef6(headJson.getString("def6"));//
			billvo.getParentVO().setDef7(headJson.getString("def7"));//
			billvo.getParentVO().setDef8(headJson.getString("def8"));//
			billvo.getParentVO().setDef9(headJson.getString("def9"));//
			billvo.getParentVO().setDef15(headJson.getString("def15"));//��������
			
			// ***********��ͷ������ֵ
			billvo.getParentVO().setPk_org(convertor.getRefAttributePk(
					"exhousetransferbillHVO-pk_org",
					billvo.getParentVO().getPk_org())); // ������֯
			billvo.getParentVO().setDef11(getBd_cust_supplier(billvo.getParentVO().getDef11()));//������
			billvo.getParentVO().setBillmaker(getSaleUserID());; // �Ƶ��ˣ��û���
			// ***********��ͷ������ֵ
			//�������ת��
			SuperVO[] bvos = (SuperVO[]) billvo.getChildrenVO();
			for(int i = 0 ; i < bvos.length ; i++){
				String def1 = getPk_projectByCode(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def1")));
				billvo.getChildrenVO()[i].setAttributeValue("def1", def1);
				
				String def2 = getPk_projectByCode(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def2")));
				billvo.getChildrenVO()[i].setAttributeValue("def2", def2);
				
				String def3 = getPk_defdocByCode(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def3")));
				billvo.getChildrenVO()[i].setAttributeValue("def3", def3);
				
				String def4 = getPk_defdocByCode(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def4")));
				billvo.getChildrenVO()[i].setAttributeValue("def4", def4);
				
				String def5 = getPk_defdocByName(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def5")));
				billvo.getChildrenVO()[i].setAttributeValue("def5", def5);
				
				String def6 = getPk_defdocByName(String.valueOf(billvo.getChildrenVO()[i].getAttributeValue("def6")));
				billvo.getChildrenVO()[i].setAttributeValue("def6", def6);
			}
			
			HashMap<String, String> eParam = new HashMap<String, String>();
			// �־û�����VO
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN19", null, billvo,
					null, eParam);
			//**************************
			
			AggExhousetransferbillHVO[] billvos = (AggExhousetransferbillHVO[]) obj;
			billvos[0].getParentVO().setTranstypepk(getRefAttributePk("transtype", billvos[0].getParentVO().getTranstype()));
			obj = getPfBusiAction().processAction("SAVE", "FN19", null, billvos[0],
					null, eParam);
			billvos = (AggExhousetransferbillHVO[]) obj;
			getPfBusiAction().processAction("APPROVE", "FN19", null, billvos[0],
					null, eParam);
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
			//**************************
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}
	}

}
