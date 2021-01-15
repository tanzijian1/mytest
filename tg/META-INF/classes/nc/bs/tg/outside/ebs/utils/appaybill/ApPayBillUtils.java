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
 * 应付单保存类
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
	 * 应付单保存
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

		// 外系统信息
		JSONObject jsonData = (JSONObject) value.get("data");// 表单数据
		JSONObject jsonhead = (JSONObject) jsonData.get("headInfo");// 外系统来源表头数据
		String srcid = jsonhead.getString("ebsid");// EBS业务单据ID
		String srcno = jsonhead.getString("ebsbillno");// EBS业务单据单据号
		// contcode contname
		/**
		 * 成本合同nc接收ebs信息时，包括生成应付单与付款申请单，需先校验应先有合同 2020-03-10-谈子健 -start
		 * 通用应付单两个类型sectype3和41都要加判断
		 */
		// if (/*EBSCont.SRCBILL_14.equals(srctype)|| */
		// EBSCont.SRCBILL_03.equals(srctype)
		// || EBSCont.SRCBILL_41.equals(srctype)) {
		// String contractcode = jsonhead.getString("contcode");// 合同编码
		// if (contractcode == null || "".equals(contractcode)) {
		// throw new BusinessException("合同编码不能为空!");
		// }
		// String contractname = jsonhead.getString("contname");// 合同名称
		// if (contractname == null || "".equals(contractname)) {
		// throw new BusinessException("合同名称不能为空!");
		// }
		// InspectionContract(contractcode, contractname);
		// }
		/**
		 * nc接收ebs信息时，包括生成应付单与付款申请单，需先校验应先有合同 2020-03-10-谈子健 -end
		 */
		String billqueue = EBSCont.getSrcBillNameMap().get(srctype) + ":"
				+ srcid;
		String billkey = EBSCont.getSrcBillNameMap().get(srctype) + ":" + srcno;
		AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
				AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '" + srcid
						+ "'");
		if (!"09".equals(srctype)) {
			if (aggVO != null) {
				throw new BusinessException("【"
						+ billkey
						+ "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "】,请勿重复上传!");
			}
		}
		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
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
			// 根据外系统单号调用影像系统，将返回的数据回写单到NC的镜像发票，已经将数据返回调用NC应付单的外系统

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
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

	}

	/**
	 * 将来源信息转换成NC信息
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
				|| EBSCont.SRCBILL_53.equals(srctype)) {// EBS-通用合同请款->应付单
			aggvo = ContTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		} else if (EBSCont.SRCBILL_05.equals(srctype)) {// EBS-营销费请款->应付单
			aggvo = MarketTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		} else if (EBSCont.SRCBILL_09.equals(srctype)) {// SRM对账单->应付单
			aggvo = ReconciliationTranBillUtils.getUtils().onTranBill(srcdata,
					srctype);
		} else if (EBSCont.SRCBILL_14.equals(srctype)
				|| EBSCont.SRCBILL_49.equals(srctype)) {// EBS-项目周建请款->应付单
			aggvo = CostTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		} else if (EBSCont.SRCBILL_22.equals(srctype)) { // EBS-材料请款单->应付单
			aggvo = MaterialTranBillUtils.getUtils().onTranBill(srcdata,
					srctype);
		} else if (EBSCont.SRCBILL_41.equals(srctype)) { // EBS-通用收并购请款->应付单
			aggvo = CollectContTranBillUtils.getUtils().onTranBill(srcdata,
					srctype);
		} else if (EBSCont.SRCBILL_49.equals(srctype)) { // EBS-成本税差应付单
			aggvo = CostTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		} else if (EBSCont.SRCBILL_50.equals(srctype)) { // EBS-成本占预算应付单
			aggvo = CostTranBillUtils.getUtils().onTranBill(srcdata, srctype);
		}
      //TODO20200825-添加合同不能为空校验
		String ifCONTRACT= OutsideUtils.getOutsideInfo("ifCONTRACT");
		if(!("Y".equals(ifCONTRACT))){
		String def5=aggvo.getHeadVO().getDef5();//合同编码，表头def5合同名称，表头def6
		String def6=aggvo.getHeadVO().getDef6();
		if(def5==null||def5.length()<1||def6==null||def6.length()<1)throw new BusinessException("合同编码或合同名称不能为空");
		if(def5!=null&&def6!=null){
			String pk=(String)dao.executeQuery("select pk_fct_ap from fct_ap f where (f.ctname='"+def6+"' or f.vbillcode='"+def5+"')", new ColumnProcessor());
		     if(pk==null||pk.length()<1){throw new BusinessException("合同编码或名字字段在NC系统不存在");}
		}
		}
		return aggvo;
	}

	/**
	 * 应付单挂起后结束变更应付单状态
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
		// 外系统信息
		JSONObject jsonData = (JSONObject) value.get("data");// 表单数据
		String srcid = jsonData.getString("ebsid");// EBS业务单据ID
		if (srcid == null || "".equals(srcid)) {
			throw new BusinessException("EBS业务单据ID:ebsid不能为空!");
		}
		String srcno = jsonData.getString("ebsbillno");// EBS业务单据单据号
		if (srcno == null || "".equals(srcno)) {
			throw new BusinessException("EBS业务单据单据号:ebsbillno不能为空!");
		}
		String actionAfterSuspend = jsonData.getString("auditstate");// 挂起后动作状态
		if (actionAfterSuspend == null || "".equals(actionAfterSuspend)) {
			throw new BusinessException("挂起后动作状态:auditstate不能为空!");
		}
		String billqueue = EBSCont.getSrcBillNameMap().get(srctype) + ":"
				+ srcid;
		String billkey = EBSCont.getSrcBillNameMap().get(srctype) + ":" + srcno;
		AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
				AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '" + srcid
						+ "'");
		try {
			if (aggVO == null) {
				throw new BusinessException("【" + billkey + "】,NC未找到对应挂起的业务单据");
			}
			EBSBillUtils.addBillQueue(billqueue);// 增加队列处理

			// 查询对应应付单的地区财务审核状态
			String auditstate = (String) aggVO.getParentVO().getAttributeValue(
					"def33");
			// 如果应付单的地区财务审批状态是挂起
			String code = queryCodebyPk(auditstate);
			if ("suspended".equals(code)) {
				BaseDAO dao = new BaseDAO();
				PayableBillVO vo = (PayableBillVO) aggVO.getParentVO();
				// 地区财务审批状态
				if (StringUtils.isNotBlank(actionAfterSuspend)) {
					String pk_auditstate = getAuditstateByCode(actionAfterSuspend);
					if (pk_auditstate == null) {
						throw new BusinessException("地区财务审批状态 【"
								+ actionAfterSuspend + "】未能在NC档案关联到相关信息!");
					}
					vo.setDef52(pk_auditstate);// 自定义项52 挂起后动作
				}
				dao.updateVO(vo);
				resultMsg = "地区财务审批状态挂起后挂起后动作状态NC更新成功!";
			}
			return resultMsg;

		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

	}

	// 根据主键查询自定义档案code
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
			throw new BusinessException("请先同步合同到nc，合同编码：" + contractcode
					+ "，合同名称：" + contractname + "");
		}
	}
}
