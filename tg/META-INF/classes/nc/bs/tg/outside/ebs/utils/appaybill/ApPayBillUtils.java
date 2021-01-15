package nc.bs.tg.outside.ebs.utils.appaybill;

import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.pub.ImgQryUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.CollectContTranBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.ContTranBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.CostTranBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.MarketTranBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.MaterialTranBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.ReconciliationTranBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.ISyncIMGBillServcie;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.tg.outside.OutsideLogVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Ӧ����������
 * 
 * @author ASUS
 * 
 */
public class ApPayBillUtils extends EBSBillUtils {
	static ApPayBillUtils utils;

	public static ApPayBillUtils getUtils() {
		if (utils == null) {
			utils = new ApPayBillUtils();
		}
		return utils;
	}

	/**
	 * Ӧ��������
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String srctype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		// ��ϵͳ��Ϣ
		JSONObject jsonData = (JSONObject) value.get("data");// ������
		JSONObject jsonhead = (JSONObject) jsonData.get("headInfo");// ��ϵͳ��Դ��ͷ����
		String srcid = jsonhead.getString("ebsid");// EBSҵ�񵥾�ID
		String srcno = jsonhead.getString("ebsbillno");// EBSҵ�񵥾ݵ��ݺ�
		// contcode contname
		/**
		 * �ɱ���ͬnc����ebs��Ϣʱ����������Ӧ�����븶�����뵥������У��Ӧ���к�ͬ 2020-03-10-̸�ӽ� -start
		 * ͨ��Ӧ������������sectype3��41��Ҫ���ж�
		 */
		// if (/*EBSCont.SRCBILL_14.equals(srctype)|| */
		// EBSCont.SRCBILL_03.equals(srctype)
		// || EBSCont.SRCBILL_41.equals(srctype)) {
		// String contractcode = jsonhead.getString("contcode");// ��ͬ����
		// if (contractcode == null || "".equals(contractcode)) {
		// throw new BusinessException("��ͬ���벻��Ϊ��!");
		// }
		// String contractname = jsonhead.getString("contname");// ��ͬ����
		// if (contractname == null || "".equals(contractname)) {
		// throw new BusinessException("��ͬ���Ʋ���Ϊ��!");
		// }
		// InspectionContract(contractcode, contractname);
		// }
		/**
		 * nc����ebs��Ϣʱ����������Ӧ�����븶�����뵥������У��Ӧ���к�ͬ 2020-03-10-̸�ӽ� -end
		 */
		String billqueue = EBSCont.getSrcBillNameMap().get(srctype) + ":"
				+ srcid;
		String billkey = EBSCont.getSrcBillNameMap().get(srctype) + ":" + srcno;
		AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
				AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '" + srcid
						+ "'");
		if (!"09".equals(srctype)) {
			if (aggVO != null) {
				throw new BusinessException("��"
						+ billkey
						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
			}
		}
		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			AggPayableBillVO billvo = onTranBill(jsonData, srctype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVE", "F1", null,
					billvo, null, null);
			AggPayableBillVO[] billvos = (AggPayableBillVO[]) obj;
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayableBillVO.BILLNO));
			// ������ϵͳ���ŵ���Ӱ��ϵͳ�������ص����ݻ�д����NC�ľ���Ʊ���Ѿ������ݷ��ص���NCӦ��������ϵͳ

			if (StringUtils.isNotBlank((String) billvos[0].getParentVO()
					.getAttributeValue("def3"))) {
				if ("F1-Cxx-001".equals(billvos[0].getParentVO()
						.getAttributeValue("pk_tradetype"))
						|| "F1-Cxx-003".equals(billvos[0].getParentVO()
								.getAttributeValue("pk_tradetype"))) {

					ISyncIMGBillServcie servcie = NCLocator.getInstance()
							.lookup(ISyncIMGBillServcie.class);
					try {
						servcie.onSyncInv_RequiresNew((String) billvos[0]
								.getParentVO().getAttributeValue("def3"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param srctype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggPayableBillVO onTranBill(JSONObject srcdata, String srctype)
			throws BusinessException {
		AggPayableBillVO aggvo = null;
		BaseDAO dao=new BaseDAO();
		if (EBSCont.SRCBILL_03.equals(srctype)
				|| EBSCont.SRCBILL_52.equals(srctype)
				|| EBSCont.SRCBILL_53.equals(srctype)) {// EBS-ͨ�ú�ͬ���->Ӧ����
			aggvo = ContTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		} else if (EBSCont.SRCBILL_05.equals(srctype)) {// EBS-Ӫ�������->Ӧ����
			aggvo = MarketTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		} else if (EBSCont.SRCBILL_09.equals(srctype)) {// SRM���˵�->Ӧ����
			aggvo = ReconciliationTranBillUtils.getUtils().onTranBill(srcdata,
					srctype);
		} else if (EBSCont.SRCBILL_14.equals(srctype)
				|| EBSCont.SRCBILL_49.equals(srctype)) {// EBS-��Ŀ�ܽ����->Ӧ����
			aggvo = CostTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		} else if (EBSCont.SRCBILL_22.equals(srctype)) { // EBS-������->Ӧ����
			aggvo = MaterialTranBillUtils.getUtils().onTranBill(srcdata,
					srctype);
		} else if (EBSCont.SRCBILL_41.equals(srctype)) { // EBS-ͨ���ղ������->Ӧ����
			aggvo = CollectContTranBillUtils.getUtils().onTranBill(srcdata,
					srctype);
		} else if (EBSCont.SRCBILL_49.equals(srctype)) { // EBS-�ɱ�˰��Ӧ����
			aggvo = CostTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		} else if (EBSCont.SRCBILL_50.equals(srctype)) { // EBS-�ɱ�ռԤ��Ӧ����
			aggvo = CostTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		}
      //TODO20200825-��Ӻ�ͬ����Ϊ��У��
		String ifCONTRACT= OutsideUtils.getOutsideInfo("ifCONTRACT");
		if(!("Y".equals(ifCONTRACT))){
		String def5=aggvo.getHeadVO().getDef5();//��ͬ���룬��ͷdef5��ͬ���ƣ���ͷdef6
		String def6=aggvo.getHeadVO().getDef6();
		if(def5==null||def5.length()<1||def6==null||def6.length()<1)throw new BusinessException("��ͬ������ͬ���Ʋ���Ϊ��");
		if(def5!=null&&def6!=null){
			String pk=(String)dao.executeQuery("select pk_fct_ap from fct_ap f where (f.ctname='"+def6+"' or f.vbillcode='"+def5+"')", new ColumnProcessor());
		     if(pk==null||pk.length()<1){throw new BusinessException("��ͬ����������ֶ���NCϵͳ������");}
		}
		}
		return aggvo;
	}

	/**
	 * Ӧ���������������Ӧ����״̬
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBillUpAndDown(HashMap<String, Object> value,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		String resultMsg = "";
		// ��ϵͳ��Ϣ
		JSONObject jsonData = (JSONObject) value.get("data");// ������
		String srcid = jsonData.getString("ebsid");// EBSҵ�񵥾�ID
		if (srcid == null || "".equals(srcid)) {
			throw new BusinessException("EBSҵ�񵥾�ID:ebsid����Ϊ��!");
		}
		String srcno = jsonData.getString("ebsbillno");// EBSҵ�񵥾ݵ��ݺ�
		if (srcno == null || "".equals(srcno)) {
			throw new BusinessException("EBSҵ�񵥾ݵ��ݺ�:ebsbillno����Ϊ��!");
		}
		String actionAfterSuspend = jsonData.getString("auditstate");// �������״̬
		if (actionAfterSuspend == null || "".equals(actionAfterSuspend)) {
			throw new BusinessException("�������״̬:auditstate����Ϊ��!");
		}
		String billqueue = EBSCont.getSrcBillNameMap().get(srctype) + ":"
				+ srcid;
		String billkey = EBSCont.getSrcBillNameMap().get(srctype) + ":" + srcno;
		AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
				AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '" + srcid
						+ "'");
		try {
			if (aggVO == null) {
				throw new BusinessException("��" + billkey + "��,NCδ�ҵ���Ӧ�����ҵ�񵥾�");
			}
			EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���

			// ��ѯ��ӦӦ�����ĵ����������״̬
			String auditstate = (String) aggVO.getParentVO().getAttributeValue(
					"def33");
			// ���Ӧ�����ĵ�����������״̬�ǹ���
			String code = queryCodebyPk(auditstate);
			if ("suspended".equals(code)) {
				BaseDAO dao = new BaseDAO();
				PayableBillVO vo = (PayableBillVO) aggVO.getParentVO();
				// ������������״̬
				if (StringUtils.isNotBlank(actionAfterSuspend)) {
					String pk_auditstate = getAuditstateByCode(actionAfterSuspend);
					if (pk_auditstate == null) {
						throw new BusinessException("������������״̬ ��"
								+ actionAfterSuspend + "��δ����NC���������������Ϣ!");
					}
					vo.setDef52(pk_auditstate);// �Զ�����52 �������
				}
				dao.updateVO(vo);
				resultMsg = "������������״̬�����������״̬NC���³ɹ�!";
			}
			return resultMsg;

		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

	}

	// ����������ѯ�Զ��嵵��code
	private String queryCodebyPk(String pk) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("select d.code from bd_defdoc d where d.pk_defdoc  = '"
				+ pk + "' and d.enablestate = 2 and dr = 0  ");

		String code = (String) dao.executeQuery(query.toString(),
				new ColumnProcessor());
		return code;
	}

	private void InspectionContract(String contractcode, String contractname)
			throws BusinessException {

		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("select a.pk_fct_ap from fct_ap a  where a.vbillcode = '"
				+ contractcode + "' or a.ctname = '" + contractname
				+ "'  and a.blatest = 'Y' and dr = 0  ");

		String pk_fct_ap = (String) dao.executeQuery(query.toString(),
				new ColumnProcessor());

		if ("".equals(pk_fct_ap) || pk_fct_ap == null) {
			throw new BusinessException("����ͬ����ͬ��nc����ͬ���룺" + contractcode
					+ "����ͬ���ƣ�" + contractname + "");
		}
	}
}
