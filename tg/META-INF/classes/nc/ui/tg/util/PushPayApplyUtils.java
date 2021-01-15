package nc.ui.tg.util;

import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IPfExchangeService;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Payrequest;

public class PushPayApplyUtils {

	static PushPayApplyUtils utils = null;

	public static PushPayApplyUtils getUtils() {
		if (utils == null) {
			utils = new PushPayApplyUtils();
		}
		return utils;
	}

	BaseDAO dao = null;
	IMDPersistenceQueryService persistenceQueryService = null;
	private IPfExchangeService ips = null;
	public static final String File_NAME = "name";// 文件上传名称
	public static final String File_SIZE = "size";// 文件上传大小
	public static final String File_TYPE = "type";// 文件上传类型
	public static final String File_URL = "url";// 文件上传地址
	public static final String File_NCBILLNO = "ncBillNo";// 文件上传来源单据号
	
	public BaseDAO getQueryBS() {
		if (dao == null) {
			dao = NCLocator.getInstance().lookup(BaseDAO.class);
		}

		return dao;
	}
	
	public IMDPersistenceQueryService getPersistenceQueryService() {
		if (persistenceQueryService == null) {
			persistenceQueryService = NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		}

		return persistenceQueryService;
	}
	
	
	
	
	/**
	 * 获取付款申请单聚合vo
	 */
	public AggPayrequest[] getAggVO(String pk){
		BillQuery<AggPayrequest> billquery = new BillQuery(AggPayrequest.class);
		AggPayrequest[] aggvo=billquery.query(new String[] {pk});
		return aggvo;
	}
	
	/**
	 * 推付款申请单
	 * @throws BusinessException 
	 */
	
	public void pushPayApplyBill(AggPayrequest aggvo) {
		IPFBusiAction iPFBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		
		Payrequest headvo = aggvo.getParentVO();
		InvocationInfoProxy.getInstance().setGroupId(
				(String) headvo.getAttributeValue("pk_group"));
		try {
			AggPayBillVO[] vo = (AggPayBillVO[]) getPfExchangeService()
					.runChangeDataAryNeedClassify("FN01", "F3",
							new AggPayrequest[] { aggvo }, null, 2);
			Boolean istrue = vo == null || vo.length == 0;
			
			if (istrue) {
				throw new BusinessException("付款申请单["
						+ headvo.getAttributeValue("pk_billtype")
						+ "]转换付款单异常 [未转换成功]!");
			}
			//表头默认值设置
			
			for (AggPayBillVO payvo : vo) {
				//表头默认值设置
				payvo.getParent().setAttributeValue("billstatus", -1);
				payvo.getParent().setAttributeValue("isflowbill", UFBoolean.FALSE);
				payvo.getParent().setAttributeValue("isforce", UFBoolean.FALSE);
				payvo.getParent().setAttributeValue("isfromindependent", UFBoolean.FALSE);
				payvo.getParent().setAttributeValue("ismandatepay", UFBoolean.FALSE);
				payvo.getParent().setAttributeValue("isnetpayready", UFBoolean.FALSE);
				payvo.getParent().setAttributeValue("isonlinepay", UFBoolean.FALSE);
				payvo.getParent().setAttributeValue("isreded", UFBoolean.FALSE);
				payvo.getParent().setAttributeValue("objtype", 1);
				payvo.getParent().setAttributeValue("sddreversalflag", UFBoolean.FALSE);
				payvo.getParent().setAttributeValue("pk_balatype",headvo.getPk_balatype());
				if("FN01-Cxx-001".equals(aggvo.getParent().getAttributeValue("transtype"))){
					payvo.getParent().setAttributeValue("pk_tradetype", "F3-Cxx-007");
					payvo.getParent().setAttributeValue("pk_tradetypeid", "1001ZZ100000001MY7TL");
				}
				if("FN01-Cxx-002".equals(aggvo.getParent().getAttributeValue("transtype"))){
					payvo.getParent().setAttributeValue("pk_tradetype", "F3-Cxx-004");
					payvo.getParent().setAttributeValue("pk_tradetypeid", "1001ZZ100000001MX7P0");
				}
				if("FN01-Cxx-003".equals(aggvo.getParent().getAttributeValue("transtype"))){
					payvo.getParent().setAttributeValue("pk_tradetype", "F3-Cxx-005");
					payvo.getParent().setAttributeValue("pk_tradetypeid", "1001ZZ100000001MXL0O");
				}
				
				//表体默认值设置
				for (PayBillItemVO bvo : payvo.getBodyVOs()){
					bvo.setAttributeValue("isdiscount", UFBoolean.FALSE);
					bvo.setAttributeValue("objtype", 1);
					bvo.setAttributeValue("pk_currtype", "1002Z0100000000001K1");
					bvo.setAttributeValue("pk_fiorg", bvo.getAttributeValue("pk_org"));
					bvo.setAttributeValue("pk_fiorg_v", bvo.getAttributeValue("pk_org_v"));
					bvo.setAttributeValue("pk_payitem", "ID_INDEX0");
					bvo.setAttributeValue("rowno", 0);
					bvo.setAttributeValue("sett_org", bvo.getAttributeValue("pk_org"));
					bvo.setAttributeValue("sett_org_v", bvo.getAttributeValue("pk_org_v"));
				}
				
				iPFBusiAction.processAction("SAVE", "F3", null,
						payvo, null, null);
			}
			
			/*BillAggVO[] savedBill = null;
			// 推式生成付款结算单
			savedBill = (BillAggVO[]) iIplatFormEntry.processAction("SAVE", "F5", null,
					vo[0], null, null);
			
			// 重查数据，有时候保存规则返回的vo不是准确的数据，有可能有缺失
			ICmpPayBillPubQueryService cmpquery = NCLocator.getInstance()
					.lookup(ICmpPayBillPubQueryService.class);
			// 对付款结算单做审批操作
			BillAggVO[] billaggvo = cmpquery
					.findBillByPrimaryKey(new String[] { (String) savedBill[0]
							.getParentVO().getAttributeValue("pk_paybill") });
			iIplatFormEntry.processAction("APPROVE", "F5", null, billaggvo[0], null, null);*/
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		
	}
	
	/**
	 * VO转换服务类
	 * 
	 * @return
	 */
	public IPfExchangeService getPfExchangeService() {
		if (ips == null) {
			ips = NCLocator.getInstance().lookup(IPfExchangeService.class);
		}
		return ips;
	}
}
