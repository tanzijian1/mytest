package nc.bs.tg.outside.salebpm.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.bpm.IRecieveBpmMsg;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.tg.outside.ISyncIMGBillServcie;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceService;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKHeaderVO;
import nc.vo.erm.common.MessageVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BpmBXBillStatesUtils extends SaleBPMBillUtils {
	static BpmBXBillStatesUtils utils;

	public static BpmBXBillStatesUtils getUtils() {
		if (utils == null) {
			utils = new BpmBXBillStatesUtils();
		}
		return utils;
	}

	Map<String, String> acttionMap = null;

	public String onSyncBillState(String billtypename, String json)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		JSONObject jobj = JSON.parseObject(json);
		valid(jobj);
		String operator = jobj.getString("operator");
		if (operator == null || "".equals(operator)) {
			InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
			InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		} else {
			Map<String, String> userInfo = getUserInfo(operator);
			if (userInfo == null) {
				throw new BusinessException("操作员【" + operator
						+ "】未能在NC用户档案关联到,请联系系统管理员！");
			}
		}
		String bpmid = jobj.getString("bpmid");
		String billqueue = ISaleBPMBillCont.getBillNameMap().get(billtypename)
				+ ":" + bpmid;
		String action = jobj.getString("billstate");// 必录;?
													// 枚举：APPROVE:审核;UNAPPROVE:弃审;UNSAVE:撤回
		BXVO aggVO = getBillVO(BXVO.class,
				"nvl(dr,0)=0 and zyx30='" + jobj.getString("bpmid") + "'");
		// zyx30 bpmid
		try {
			if (aggVO == null)
				throw new BusinessException("NC系统bpm主键未有对应单据");
			//TODO20201027(审批状态为1与2直接成功返回)
			if(aggVO.getParentVO().getSpzt()==1||aggVO.getParentVO().getSpzt()==2){
				return "【" + billqueue + "】,"+"操作完成";
			}
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			HashMap paramMap = new HashMap();
			paramMap.put("flowdefpk", aggVO.getParentVO().getPrimaryKey());
			WorkflownoteVO worknoteVO = null;
			nc.vo.pubapp.pflow.PfUserObject userobjec = new nc.vo.pubapp.pflow.PfUserObject();
			userobjec.setBusinessCheckMap(new HashMap());
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			String maker = (String) aggVO.getParentVO().getAttributeValue(
					"creator");
			if ("APPROVE".equals(action)) {
				Map<String, String> map = (Map<String, String>) jobj
						.get("data");
				worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.SAVE,
						"264X", aggVO, null);
				BXVO newvo = aggVO;
				if (aggVO.getParentVO().getSpzt() == -1)
					throw new BusinessException("当前NC单据审批状态异常，请检查！");
				/**
				 * 先把消息优先级等数据更新到数据库再查下出来2020-04-14-谈子健-start
				 */
				IMDPersistenceService md = NCLocator.getInstance().lookup(
						IMDPersistenceService.class);
				newvo.getParentVO().setZyx46(map.get("billpriority"));// 紧急程度
				md.updateBillWithAttrs(new Object[] { newvo },
						new String[] { "zyx46" });
				newvo = getBillVO(BXVO.class,
						"nvl(dr,0)=0 and zyx30='" + jobj.getString("bpmid")
								+ "'");
				worknoteVO = iWorkflowMachine.checkWorkFlow(
						IPFActionName.APPROVE, "264X", newvo, null);
				worknoteVO.setChecknote("批准");
				worknoteVO.setApproveresult("Y");
				MessageVO[] messagevos1 = (MessageVO[]) getPfBusiAction()
						.processAction(IPFActionName.APPROVE, "264X",
								worknoteVO, newvo, null, null);

				if (messagevos1[0].getSuccessVO() != null) {
					BXVO newbxvo = approveUpdateVO(
							(BXVO) messagevos1[0].getSuccessVO(), map,
							billtypename);
					md.updateBillWithAttrs(
							new Object[] { newbxvo.getParentVO() },
							new String[] { "zyx12", "jsfs", "zyx15", "zyx18",
									"zyx17", "zyx14", JKHeaderVO.ZYX52,
									JKHeaderVO.ZYX53, JKHeaderVO.ZYX54,
									JKHeaderVO.CUSTACCOUNT });
					IRecieveBpmMsg msg = NCLocator.getInstance().lookup(
							IRecieveBpmMsg.class);
					msg.dealBalatypeMsg(newbxvo);
					getinvoiceByImg(newbxvo.getParentVO().getZyx16());
				}
			} else if ("UNAPPROVE".equals(action)) {// BPM退回操作：不清除taskid和影像编码
				BXVO newvo = aggVO;
				if (aggVO.getParentVO().getSpzt() != 3)
					throw new BusinessException("当前NC单据审批状态异常，请检查！");
				aggVO.getParentVO().setIsInterface("UNAPPROVE");// 是否接口
				aggVO.getParentVO().setZyx29("N");// 是否在BPM流程
				getPfBusiAction().processAction(IPFActionName.UNSAVE + maker,
						"264X", worknoteVO, newvo, null, null);
			} else if ("UNSAVE".equals(action)) {// BPM拒绝操作：清除taskid和影像编码
				String approve = aggVO.getParentVO().getApprover();
				MessageVO[] messagevos = null;
				BXVO newvo = aggVO;
				if (aggVO.getParentVO().getSpzt() != 3)
					throw new BusinessException("当前NC单据审批状态异常，请检查！");
				if (messagevos != null)
					newvo = (BXVO) messagevos[0].getSuccessVO();
				if (newvo != null) {
					// newvo.getParentVO().setZyx30(null);
					// newvo.getParentVO().setZyx29("N");
					newvo.getParentVO().setIsInterface("UNSAVE");// 是否接口
				}
				getPfBusiAction().processAction(IPFActionName.UNSAVE + maker,
						"264X", worknoteVO, newvo, null, eParam);

			}
		} catch (Exception e) {
			throw new BusinessException(
					"【" + billqueue + "】," + e.getMessage(), e);
		} finally {
			SaleBPMBillUtils.removeBillQueue(billqueue);
		}
		return "【" + billqueue + "】," + getActtionMap().get(action) + "操作完成!";
	}

	/**
	 * 流程操作MAP
	 * 
	 * @return
	 */
	public Map<String, String> getActtionMap() {
		if (acttionMap == null) {
			acttionMap = new HashMap<String, String>();
			acttionMap.put(IPFActionName.SAVE, "提交");
			acttionMap.put(IPFActionName.APPROVE, "审批");
			acttionMap.put(IPFActionName.UNAPPROVE, "弃审");
			acttionMap.put(IPFActionName.UNSAVE, "撤回");

		}
		return acttionMap;
	}

	public BXVO approveUpdateVO(BXVO aggvo, Map<String, String> map,
			String billtypename) throws DAOException {
		// 费控费用请款-11:合同费用请款-地产;12:合同费用请款-物业;13:合同费用请款-商业;
		String[] billtypenames = new String[] { "11", "12", "13" };
		List<String> list = Arrays.asList(billtypenames);
		JKBXHeaderVO hvo = aggvo.getParentVO();
		hvo.setZyx12(map.get("ispayalterinv"));// 是否先付款后补票
		hvo.setJsfs(getbd_balatypeBycode(map.get("balatype")));// 结算方式
		hvo.setAttributeValue("zyx46", map.get("billpriority"));// 单据优先级
		hvo.setZyx14(map.get("isaddannex"));// 是否补附件
		hvo.setZyx15(map.get("iscompletion"));// 已补全
		if (list.contains(billtypename)) {
			hvo.setZyx18(getregionstate("finapprove"));// 地区财务审批状态
			if (map.get("imgstate") != null)
				hvo.setZyx17(map.get("imgstate"));// 影像状态
		}
		hvo.setZyx53(map.get("MarginLevel"));// 保证金比例
		hvo.setDef65(map.get("ABSPaidInProportion"));// ABS实付比例
		hvo.setZyx54(map.get("ABSPaidInAmount"));// ABS实付金额
		Object obj = map.get("body");
		if (obj != null) {
			JSONArray bodyjson = (JSONArray) obj;
			for (int i = 0, j = bodyjson.size(); i < j; i++) {
				JSONObject json = bodyjson.getJSONObject(i);
				hvo.setZyx52(String.valueOf(json
						.get("JointBankNoOfBillReceiver")));// 收票方联行号
				hvo.setCustaccount(getCustaccount_accnum(String.valueOf(json
						.get("PayeeCompanyCode"))));// 实际收款方公司编号
			}
		}
		return aggvo;
	}

	public void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("操作状态不能为空");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPM业务单据主键不能为空");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("目标单据不能为空");
		if ("APPROVE".equals(jobj.getString("billstate"))) {
			if (jobj.getString("data") == null || jobj.getString("") == "")
				throw new BusinessException("传入信息DATA不能为空");
		}
	}

	public void getinvoiceByImg(String str) throws BusinessException {
		ISyncIMGBillServcie servcie = NCLocator.getInstance().lookup(
				ISyncIMGBillServcie.class);
		try {
			servcie.onSyncInv_RequiresNew(str);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public BXVO getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		NCObject[] nobjs = getMDQryService().queryBillOfNCObjectByCond(c,
				whereCondStr, false);

		if (nobjs == null || nobjs.length == 0) {
			throw new BusinessException("NC系统未能关联信息!");
		}
		return (BXVO) nobjs[0].getContainmentObject();// (BXVO) nobjs[0];
	}

	/**
	 * 客商账户银行账户
	 * 
	 * @param custaccount
	 * @return
	 */
	private String getCustaccount_accnum(String custaccount) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select b.pk_bankaccsub from bd_bankaccsub b where b.accnum = '"
				+ custaccount + "' AND NVL(dr,0)=0";
		String result = "";
		try {
			result = (String) bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}

}
