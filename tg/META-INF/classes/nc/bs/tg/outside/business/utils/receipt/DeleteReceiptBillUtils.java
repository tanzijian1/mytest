package nc.bs.tg.outside.business.utils.receipt;

/**
 * �տ������ɾ��
 * @author huangxj
 * 
 */

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.itf.tg.outside.BusinessBillCont;
import nc.itf.tg.outside.EBSCont;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.BusinessBillLogVO;
import uap.json.JSONObject;

public class DeleteReceiptBillUtils extends BusinessBillUtils {

	static DeleteReceiptBillUtils utils;

	public static DeleteReceiptBillUtils getUtils() {
		if (utils == null) {
			utils = new DeleteReceiptBillUtils();
		}
		return utils;
	}

	/**
	 * �տ������
	 * 
	 * @param value
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String getValue(JSONObject value, String srctype)
			throws BusinessException {

		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// �����û���
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		
		ResultVO resultVO = new ResultVO();
		String rsInfo = "";

		// ��־��¼VO
		BusinessBillLogVO logVO = new BusinessBillLogVO();
		logVO.setSrcsystem(BusinessBillCont.SRCSYS);
		logVO.setSrcparm(value.toString());
		logVO.setExedate(new UFDateTime().toString());
		logVO.setResult(STATUS_SUCCESS);
		logVO.setOperator(OperatorName);
		logVO.setDesbill(BusinessBillCont.getBillNameMap().get(srctype));
		logVO.setTrantype(srctype);
		logVO.setBusinessno(value.getString("srcno"));
		resultVO.setBillid(value.getString("srcid"));
		// ���ز�
		String result = null;
		try {
			// ִ��ɾ��
			result = execute(value, srctype);
			logVO.setErrmsg("�����ɹ�");
			logVO.setBillno(result);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
			resultVO.setMsg("�����ɹ�");
		} catch (Exception e) {
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("srcbillno", value.getString("srcno"));
			dataMap.put(
					"msg",
					"ɾ��ʧ��"
							+ org.apache.commons.lang.exception.ExceptionUtils
									.getFullStackTrace(e));
			logVO.setErrmsg(JSON.toJSONString(dataMap));
			Logger.error(e.getMessage(), e);
			logVO.setResult(STATUS_FAILED);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("�����쳣��" + e.getMessage());
		} finally {
			rsInfo = net.sf.json.JSONObject.fromObject(resultVO).toString();
			try {
				getBaseDAO().insertVO(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return rsInfo;
	}

	/**
	 * ִ��ɾ������
	 * 
	 * @param value
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	private String execute(JSONObject value, String srctype)
			throws BusinessException {
		String srcno = value.getString("srcno");
		String billqueue = EBSCont.getBillNameMap().get(srctype) + ":" + srcno;
		addBillQueue(billqueue);// ���Ӷ��д���

		// ��ѯ��Ӧ���տ
		AggGatheringBillVO aggVO = (AggGatheringBillVO) getBillVO(
				AggGatheringBillVO.class, "isnull(dr,0)=0  and def2 = '" + srcno
						+ "'");
		if (aggVO == null) {
			throw new BusinessException(srcno + "�˵����ѱ�ɾ������û��NC����");
		}
		GatheringBillVO headvo = (GatheringBillVO) aggVO.getParent();
		// �����������̬�㲻����ɾ������
		if (headvo.getApprovestatus() != -1 && headvo.getApprovestatus() != 3) {
			throw new BusinessException(srcno + "�˵����������в���ɾ��������ɾ������NCϵͳȡ������");
		}

		try {
			getPfBusiAction().processAction(
					"DELETE", "F2", null, aggVO, null, null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			removeBillQueue(billqueue);// �Ƴ�����
		}
		return headvo.getBillno();
	}
}
