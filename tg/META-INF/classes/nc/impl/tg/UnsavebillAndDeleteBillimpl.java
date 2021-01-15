package nc.impl.tg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.itf.tools.HttpBusinessUtils;
import nc.bs.tg.outside.infotransform.IInfoTransformService;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.IUnsavebillAndDeleteBill;
import nc.itf.tg.outside.OutsideUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.outside.BusinessBillLogVO;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class UnsavebillAndDeleteBillimpl implements IUnsavebillAndDeleteBill {

	IPFBusiAction pfBusiAction = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	@Override
	public void UnsavebillAndDelete_RequiresNew(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException {
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue(
				"pk_tradetype");
		// 通用单据
		if ("F1-Cxx-004".equals(pk_tradetype)
				|| "F1-Cxx-007".equals(pk_tradetype)) {
			// 通用回写
			if (aggvo.getParentVO().getAttributeValue("def1") == null) {
				new BusinessException("EBS主键为空");
			}
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);
			String returnjson = null;
			String def52 = (String) aggvo.getParentVO().getAttributeValue(
					"def52");
			// 挂起后结束单据判断2020-02-19 tjl
			String def52code = queryCodebyPk(def52);
			if ("suspendedRejected".equals(def52code)) {
				MessageDialog.showHintDlg(null, "提示", "该单据已挂起后结束单据不允许退回!");
			}
			returnjson = service.pushEBSthPayablerequest(aggvo, aggVO);
			// String param = JSON.toJSONString(datamap);
			// String returndata = connection(address, "&req=" + param);
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			if (!("S".equals(flag))) {
				throw new BusinessException("EBS exception: "
						+ jobj.getString("msg"));
			}
		}
		// 成本单据
		else if ("F1-Cxx-001".equals(pk_tradetype)) {
			String returnjson = null;
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);
			String def52 = (String) aggvo.getParentVO().getAttributeValue(
					"def52");
			// 挂起后结束单据判断2019-12-23-谈子健
			String def52code = queryCodebyPk(def52);
			if ("suspendedRejected".equals(def52code)) {
				MessageDialog.showHintDlg(null, "提示", "该单据已挂起后结束单据不允许退回!");
			}
			returnjson = service.costPushEBSPayablerequest(aggvo, aggVO);
			if (returnjson != null) {
				JSONObject jobj = JSON.parseObject(returnjson);
				String flag = jobj.getString("code");
				if (!("S".equals(flag))) {
					throw new BusinessException("EBS exception: "
							+ jobj.getString("msg"));
				}
			}

		}
		// 占预算+扣税差应付单退回推ebs(成本)
		/**
		 * F1-Cxx-010,成本税差应付单 F1-Cxx-011,成本占预算应付单 F1-Cxx-012,通用税差应付单
		 * F1-Cxx-013,通用占预算应付单
		 */
		else if ("F1-Cxx-010".equals(pk_tradetype)
				|| "F1-Cxx-011".equals(pk_tradetype)) {
			String returnjson = null;
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);
			String def52 = (String) aggvo.getParentVO().getAttributeValue(
					"def52");
			// 挂起后结束单据判断2019-12-23-谈子健
			String def52code = queryCodebyPk(def52);
			if ("suspendedRejected".equals(def52code)) {
				MessageDialog.showHintDlg(null, "提示", "该单据已挂起后结束单据不允许退回!");
			}

			returnjson = service.CostBudgetTaxdifferencePushEbs(aggvo, "N");
			if (returnjson != null) {
				JSONObject jobj = JSON.parseObject(returnjson);
				String flag = jobj.getString("code");
				if (!("S".equals(flag))) {
					throw new BusinessException("EBS exception: "
							+ jobj.getString("msg"));
				}
			}

		}// 占预算+扣税差应付单退回推ebs(通用)
		else if ("F1-Cxx-012".equals(pk_tradetype)
				|| "F1-Cxx-013".equals(pk_tradetype)) {
			String returnjson = null;
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);
			HashMap eParam = new HashMap();
			String def52 = (String) aggvo.getParentVO().getAttributeValue(
					"def52");
			// 挂起后结束单据判断2019-12-23-谈子健
			String def52code = queryCodebyPk(def52);
			if ("suspendedRejected".equals(def52code)) {
				MessageDialog.showHintDlg(null, "提示", "该单据已挂起后结束单据不允许退回!");
			}

			returnjson = service.TranBudgetTaxdifferencePushEbs(aggvo, "N");
			if (returnjson != null) {
				JSONObject jobj = JSON.parseObject(returnjson);
				String flag = jobj.getString("code");
				if (!("S".equals(flag))) {
					throw new BusinessException("EBS exception: "
							+ jobj.getString("msg"));
				}
			}

		}

	}

	// 根据主键查询自定义档案code
	private String queryCodebyPk(String pk) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ pk + "' and d.enablestate = 2 and dr = 0  ");

		String code = (String) getQuery().executeQuery(query.toString(),
				new ColumnProcessor());
		return code;
	}

	IUAPQueryBS query = null;

	public IUAPQueryBS getQuery() {
		if (query == null) {
			query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return query;
	}

	@Override
	public Boolean BackAndDeletePaybill_RequiresNew(Object obj, String msg)
			throws BusinessException {

		ISqlThread sendgl = NCLocator.getInstance().lookup(ISqlThread.class);

		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		String cuserid = (String) query.executeQuery(
				"select cuserid  from sm_user where user_name='SALE' ",
				new ColumnProcessor());

		AggregatedValueObject object = (AggregatedValueObject) obj;
		String ifpay = null;// 是否是转备案付款单（Y为是）
		String zflag = null;// (转备案 )
		List<AggregatedValueObject> listbillvo = null;// 所有协同单据VO
		if ("F3-Cxx-001".equals(object.getParentVO().getAttributeValue(
				"pk_tradetype"))
				|| "F2-Cxx-001".equals(object.getParentVO().getAttributeValue(
						"pk_tradetype"))
				&& cuserid.equals(((AggregatedValueObject) obj).getParentVO()
						.getAttributeValue("billmaker"))) {
			// 转备案收付单（删除协同单据）
			if (obj instanceof AggPayBillVO) {
				ifpay = "Y";
				zflag = (String) ((AggregatedValueObject) obj).getParentVO()
						.getAttributeValue("def80");
				listbillvo = getCoordinateVOS((String) ((AggregatedValueObject) obj)
						.getParentVO().getAttributeValue("def80"));
			} else if (obj instanceof AggGatheringBillVO) {
				ifpay = "N";
				zflag = (String) ((AggregatedValueObject) obj).getParentVO()
						.getAttributeValue("def39");
				listbillvo = getCoordinateVOS((String) ((AggregatedValueObject) obj)
						.getParentVO().getAttributeValue("def39"));
			}
			// 走脚本删除关联转备案收付款单
			if (listbillvo != null) {
				for (AggregatedValueObject aobj : listbillvo) {
					deletepayrecBillVo(aobj);
				}
			}
		}
		// TODO非转备案收付款单删除
		if ((obj instanceof AggPayBillVO || obj instanceof AggGatheringBillVO)
				&& cuserid.equals(object.getParentVO().getAttributeValue(
						"creator")) && ifpay == null && zflag == null) {
			deletepayrecBillVo(obj);

		}

		if (obj instanceof AggPayBillVO
				&& cuserid.equals(((AggPayBillVO) obj).getParentVO()
						.getAttributeValue("billmaker")) && ifpay == null
				&& zflag == null) {
			// if (obj instanceof AggPayBillVO
			// && cuserid.equals(((AggPayBillVO) obj).getParentVO()
			// .getAttributeValue("billmaker"))) {

			nc.vo.arap.pay.AggPayBillVO aggpayvo = (nc.vo.arap.pay.AggPayBillVO) obj;

			// 设置自定义项53回退原因
			// 成本付款申请单
			aggpayvo.getParentVO().setAttributeValue("def77", msg);

			String opsql = "select bd_psndoc.code from bd_psndoc,sm_user where nvl(bd_psndoc.dr, 0) = 0 and nvl(sm_user.dr, 0) = 0 and "
					+ "bd_psndoc.pk_psndoc = sm_user.pk_psndoc and cuserid ='"
					+ AppContext.getInstance().getPkUser() + "'";

			String opration = (String) query.executeQuery(opsql,
					new ColumnProcessor());

			HashMap<String, Object> mapdata = new HashMap<String, Object>();
			mapdata.put("vouchID",
					aggpayvo.getParentVO().getAttributeValue("def1"));
			mapdata.put("rzBank", "");
			mapdata.put("issuccess", 0);
			mapdata.put("adaccount", opration);
			mapdata.put("command", msg);

			sendgl.saleBack(mapdata);
		}
		// ========end========
		// TODO转备案
		if ("F3-Cxx-001".equals(((AggregatedValueObject) obj).getParentVO()
				.getAttributeValue("pk_tradetype"))
				|| "F2-Cxx-001".equals(((AggregatedValueObject) obj)
						.getParentVO().getAttributeValue("pk_tradetype"))
				&& cuserid.equals(((AggregatedValueObject) obj).getParentVO()
						.getAttributeValue("billmaker"))) {

			if (!(((Integer) ((AggregatedValueObject) obj).getParentVO()
					.getAttributeValue("approvestatus")) == -1 || ((Integer) ((AggregatedValueObject) obj)
					.getParentVO().getAttributeValue("approvestatus")) == 3))
				throw new BusinessException("单据审批状态必须为提交态或自由态");

			List<Map<String, Object>> listheaders = new ArrayList<>();
			for (AggregatedValueObject aobj : listbillvo) {
				Map<String, Object> mapdata = new HashMap<>();
				if (aobj instanceof AggPayBillVO) {
					AggPayBillVO billvo = (AggPayBillVO) aobj;
					PayBillVO pvo = (PayBillVO) billvo.getParentVO();
					String opsql = "select bd_psndoc.code from bd_psndoc,sm_user where nvl(bd_psndoc.dr, 0) = 0 and nvl(sm_user.dr, 0) = 0 and "
							+ "bd_psndoc.pk_psndoc = sm_user.pk_psndoc and cuserid ='"
							+ AppContext.getInstance().getPkUser() + "'";

					String opration = (String) query.executeQuery(opsql,
							new ColumnProcessor());
					mapdata.put("voucherId", pvo.getDef1());//
					mapdata.put("ncfkDate", pvo.getDef82());// 放款日期
					mapdata.put("ncgobackJe", pvo.getMoney() == null ? null
							: pvo.getMoney().toString());// Nc回写金额
					mapdata.put("command", msg);// NC传回审批意见
					mapdata.put("adaccount", opration);// 用户AD登录账号
					mapdata.put("ncDocumentNo", pvo.getBillno());// NC票据号
					List<Map<String, Object>> itemvos = new ArrayList<>();
					if (billvo.getBodyVOs() != null) {
						for (CircularlyAccessibleValueObject bobj : billvo
								.getChildrenVO()) {
							Map<String, Object> mobj = new HashMap<>();
							PayBillItemVO bvo = (PayBillItemVO) bobj;
							mobj.put("ncoutflowaccount",
									getAccnum(bvo.getRecaccount()));
							mobj.put("ncgobackJe",
									bvo.getLocal_money_de() == null ? null
											: bvo.getLocal_money_de()
													.toString());
							mobj.put("voucherId", pvo.getDef1());
							mobj.put("orderId", bvo.getDef53());
							itemvos.add(mobj);
						}
					}
					mapdata.put("bodys", itemvos);
					listheaders.add(mapdata);
				} else if (aobj instanceof AggGatheringBillVO) {
					AggGatheringBillVO billvo = (AggGatheringBillVO) aobj;
					GatheringBillVO pvo = billvo.getHeadVO();
					String opsql = "select bd_psndoc.code from bd_psndoc,sm_user where nvl(bd_psndoc.dr, 0) = 0 and nvl(sm_user.dr, 0) = 0 and "
							+ "bd_psndoc.pk_psndoc = sm_user.pk_psndoc and cuserid ='"
							+ AppContext.getInstance().getPkUser() + "'";

					String opration = (String) query.executeQuery(opsql,
							new ColumnProcessor());
					mapdata.put("voucherId", pvo.getDef1());//
					// mapdata.put("ncfkDate", pvo.getDef82());//放款日期(收款单不需要)
					mapdata.put("ncgobackJe", pvo.getMoney() == null ? null
							: pvo.getMoney().toString());// Nc回写金额
					mapdata.put("command", msg);// NC传回审批意见
					mapdata.put("adaccount", opration);// 用户AD登录账号
					mapdata.put("ncDocumentNo", pvo.getBillno());// NC票据号

					List<Map<String, Object>> itemvos = new ArrayList<>();
					if (billvo.getChildrenVO() != null) {
						for (CircularlyAccessibleValueObject bobj : billvo
								.getChildrenVO()) {
							Map<String, Object> mobj = new HashMap<>();
							GatheringBillItemVO bvo = (GatheringBillItemVO) bobj;
							mobj.put("ncoutflowaccount",
									getAccnum(bvo.getRecaccount()));
							mobj.put("ncgobackJe",
									bvo.getMoney_cr() == null ? null : bvo
											.getMoney_cr().toString());
							mobj.put("voucherId", pvo.getDef1());
							mobj.put("orderId", bvo.getDef10());
							itemvos.add(mobj);
						}
					}
					mapdata.put("bodys", itemvos);
					listheaders.add(mapdata);
				}

			}

			Map<String, Object> datamap = new HashMap<String, Object>();
			datamap.put("tfid", zflag);// 转备案ID,NC的转备案收付款唯一标识
			datamap.put("nowithdraw", "0");// 0为退回,1为不退回
			datamap.put("headers", listheaders);
			sendgl.sendBackpayrec__RequiresNew(datamap);
		}

		if (obj instanceof AggPayBillVO
				&& cuserid.equals(((AggPayBillVO) obj).getParentVO()
						.getAttributeValue("billmaker")) && ifpay == null
				&& zflag == null) {
			nc.vo.arap.pay.AggPayBillVO aggvo = (nc.vo.arap.pay.AggPayBillVO) obj;
			PayBillVO pvo = (PayBillVO) aggvo.getParentVO();
			String sql = "select g.num from ap_paybill a inner join fip_relation f on f.src_relationid=a.pk_paybill inner join  gl_voucher  g on g.pk_voucher =f.des_relationid  where pk_paybill='"
					+ pvo.getAttributeValue("pk_paybill") + "'";
			Object num = query.executeQuery(sql, new ColumnProcessor());// 凭证号
			String isnum = num == null ? "0" : "1";
			String isshr = pvo.getApprovestatus() == 1 ? "1" : "0";
			String tradetype = "0";
			// if(st((String)aggvo.getParentVO().getAttributeValue(""))){
			HashMap<String, Object> mapdata = new HashMap<String, Object>();
			mapdata.put("vouchid", pvo.getDef1());// 销售系统单据ID
			mapdata.put("generateCredentials", isnum);// 是否生成凭证
			mapdata.put("ncDocumentNo", pvo.getBillno());// NC票据号
			mapdata.put("ncDocumentnumber", num);// 凭证号
			mapdata.put("shr", isshr);// 审核是否成功
			if ("F3-Cxx-001".equals(pvo.getPk_tradetype())) {
				tradetype = "1";
			}
			mapdata.put("isDj", tradetype);// 是否为转备案款单据（1为是）
			sendgl.saleSendGL_RequiresNew(mapdata);
		} else if (obj instanceof AggGatheringBillVO
				&& cuserid.equals(((AggGatheringBillVO) obj).getParentVO()
						.getAttributeValue("billmaker")) && ifpay == null
				&& zflag == null) {
			AggGatheringBillVO aggvo = (AggGatheringBillVO) obj;
			GatheringBillVO gvo = (GatheringBillVO) aggvo.getParentVO();
			String gathersql = "select g.num from ar_gatherbill a inner join fip_relation f on f.src_relationid=a.pk_gatherbill  inner join  gl_voucher  g on g.pk_voucher =f.des_relationid where   pk_gatherbill ='"
					+ gvo.getAttributeValue("pk_gatherbill") + "'";
			Object num = query.executeQuery(gathersql, new ColumnProcessor());// 凭证号
			String isnum = num == null ? "0" : "1";
			String tradetype = "0";
			String isshr = gvo.getApprovestatus() == 1 ? "1" : "0";
			HashMap<String, Object> mapdata = new HashMap<String, Object>();
			mapdata.put("vouchid", gvo.getDef1());// 销售系统单据ID
			mapdata.put("generateCredentials", isnum);// 是否生成凭证（1为是）
			mapdata.put("ncDocumentNo", gvo.getBillno());// NC票据号
			mapdata.put("ncDocumentnumber", num);// 凭证号
			mapdata.put("shr", isshr);// 审核是否成功（1为成功）
			if ("F2-Cxx-001".equals(gvo.getPk_tradetype())) {
				tradetype = "1";
			}
			mapdata.put("isDj", tradetype);// 是否为转备案款单据（1为是）
			sendgl.saleSendGL_RequiresNew(mapdata);
		}

		return true;
	}

	/**
	 * 得到转备案收付款单所有单据（包括自己）
	 * 
	 * @param def80
	 * @param cla
	 * @return
	 * @throws BusinessException
	 */
	public List<AggregatedValueObject> getCoordinateVOS(String def80)
			throws BusinessException {
		IMDPersistenceQueryService service = NCLocator.getInstance().lookup(
				IMDPersistenceQueryService.class);
		service.queryBillOfVOByCond(AggPayBillVO.class,
				" nvl(dr,0)=0 and def80='" + def80 + "'", true, true);
		NCObject[] nobjs = service.queryBillOfNCObjectByCond(
				AggPayBillVO.class, " nvl(dr,0)=0 and def80='" + def80 + "'",
				false);
		List<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
		if (nobjs != null) {
			for (NCObject nobj : nobjs) {
				if (nobj != null) {
					AggPayBillVO aggvo = (AggPayBillVO) nobj
							.getContainmentObject();
					list.add(aggvo);
					PayBillVO hvo = (PayBillVO) aggvo.getParentVO();
					if (!(hvo.getApprovestatus() == -1 || hvo
							.getApprovestatus() == 3)) {
						throw new BusinessException(
								"转备案单据关联单据审批状态必须为提交态或自由态,单号为" + hvo.getBillno());
					}
				}
			}
		}
		NCObject[] gnobjs = service.queryBillOfNCObjectByCond(
				AggGatheringBillVO.class, " nvl(dr,0)=0 and def39='" + def80
						+ "'", true);
		Collection<?> col = service.queryBillOfVOByCond(
				AggGatheringBillVO.class, " nvl(dr,0)=0 and def39='" + def80
						+ "'", true, true);
		if (gnobjs != null) {
			for (NCObject nobj : gnobjs) {
				if (nobj != null) {
					AggGatheringBillVO aggvo = (AggGatheringBillVO) nobj
							.getContainmentObject();
					List<GatheringBillItemVO> itembvos = (List<GatheringBillItemVO>) getQuery()
							.executeQuery(
									"select * from ar_gatheritem where pk_gatherbill =(select ar_gatherbill.pk_gatherbill from ar_gatherbill where billno='"
											+ aggvo.getHeadVO().getBillno()
											+ "')",
									new BeanListProcessor(
											GatheringBillItemVO.class));
					aggvo.setChildrenVO(itembvos
							.toArray(new GatheringBillItemVO[0]));
					list.add(aggvo);
					GatheringBillVO hvo = aggvo.getHeadVO();
					if (!(hvo.getApprovestatus() == -1 || hvo
							.getApprovestatus() == 3)) {
						throw new BusinessException("转备案单据关联单据必须为提交态或自由态,单号为"
								+ hvo.getBillno());
					}
				}
			}
		}
		return list;
	}

	/**
	 * 得到收款银行账户账号
	 * 
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	public String getAccnum(String pk) throws BusinessException {
		String accnum = (String) getQuery().executeQuery(
				"select accnum from bd_bankaccsub  where   pk_bankaccsub ='"
						+ pk + "'", new ColumnProcessor());
		return accnum;
	}

	/**
	 * 删除收付单
	 * 
	 * @param obj
	 * @throws BusinessException
	 */
	public void deletepayrecBillVo(Object obj) throws BusinessException {
		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		if (obj instanceof AggPayBillVO) {
			getPfBusiAction().processAction(IPFActionName.DEL_DELETE, "F3",
					null, (AggPayBillVO) obj, null, eParam);// 删除付款单
		}

		if (obj instanceof AggGatheringBillVO) {
			getPfBusiAction().processAction(IPFActionName.DEL_DELETE, "D2",
					null, (AggGatheringBillVO) obj, null, eParam);// 删除付款单
		}
	}

	/**
	 * 删除并推送ebs付款申请单
	 * 
	 * @param obj
	 * @param msg
	 * @throws BusinessException
	 */
	public void BackAndDeletePayreq_RequiresNew(AggPayrequest aggvo,
			int approvestatus) throws BusinessException {

		IInfoTransformService service = NCLocator.getInstance().lookup(
				IInfoTransformService.class);
		AggPayrequest[] billvos = null;
		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		String oldUserId = "";
		oldUserId = InvocationInfoProxy.getInstance().getUserId();
		String billmaker = (String) aggvo.getParent().getAttributeValue(
				"billmaker");
		InvocationInfoProxy.getInstance().setUserId(billmaker);
		if (approvestatus == 3) {
			billvos = (AggPayrequest[]) getPfBusiAction().processAction(
					"UNSAVEBILL", "FN01", null, aggvo, null, eParam);// 收回付款申请单
		} else {
			billvos = new AggPayrequest[] { aggvo };
		}
		billvos = (AggPayrequest[]) getPfBusiAction().processAction(
				IPFActionName.DEL_DELETE, "FN01", null, billvos[0], null,
				eParam);// 删除付款申请单
		String returnjson = null;
		if ("FN01-Cxx-001".equals(aggvo.getParentVO().getTranstype())) {
			returnjson = service.pushEBSthPayrequest(aggvo);
			if (returnjson != null) {
				JSONObject jobj = JSON.parseObject(returnjson);
				String flag = jobj.getString("code");
				if (!("S".equals(flag))) {
					throw new BusinessException("EBS exception: "
							+ jobj.getString("msg"));
				}
			}

			// }
			// getModel().initModel(null);
			InvocationInfoProxy.getInstance().setUserId(oldUserId);
		} else if ("FN01-Cxx-005".equals(aggvo.getParentVO().getTranstype())) {
			try {
				returnjson = service.costPushEBSthPayrequest(aggvo);

				if (returnjson != null) {
					JSONObject jobj = JSON.parseObject(returnjson);
					String flag = jobj.getString("code");
					if (!("S".equals(flag))) {
						throw new BusinessException("EBS exception: "
								+ jobj.getString("msg"));
					}
				}
				InvocationInfoProxy.getInstance().setUserId(oldUserId);
			} catch (Exception e1) {
				throw new BusinessException(e1.getMessage());
			}

		} else if ("FN01-Cxx-003".equals(aggvo.getParentVO().getTranstype())) {
			try {
				returnjson = service.pushSRMthPayrequest(aggvo);

				if (returnjson != null) {
					JSONObject jobj = JSON.parseObject(returnjson);
					String flag = jobj.getString("code");
					if (!("S".equals(flag))) {
						throw new BusinessException("EBS exception: "
								+ jobj.getString("msg"));
					}
				}
				InvocationInfoProxy.getInstance().setUserId(oldUserId);
			} catch (Exception e1) {
				throw new BusinessException(e1.getMessage());
			}

		}
	}

	@Override
	public void SaleBackDelete_RequiresNew(AggregatedValueObject obj,
			String billType, HashMap<String, Object> map)
			throws BusinessException {
		// TODO 自动生成的方法存根
		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		ISqlThread service = NCLocator.getInstance().lookup(ISqlThread.class);
		IPFBusiAction pfaction = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		pfaction.processAction(IPFActionName.DEL_DELETE, billType, null, obj,
				null, eParam);
		service.saleSendGL_RequiresNew(map);
	}

	@Override
	public void BackAndDeleteGatherBill_RequiresNew(Object obj, String type)
			throws BusinessException {
		

		BusinessBillLogVO logVO = new BusinessBillLogVO();
		String srcid = null;
		String billno = null;
		if ("SKD".equals(type)) {
			AggGatheringBillVO billvo = (AggGatheringBillVO) obj;
			getPfBusiAction().processAction(IPFActionName.DEL_DELETE,
					"F2-Cxx-SY001", null, billvo, null, null);// 删除商业收款单
			srcid = (String) billvo.getParentVO().getAttributeValue("def1");
			billno = (String) billvo.getParentVO().getAttributeValue("billno");
		} else if ("FPD".equals(type)) {
			AggChangeBillHVO billvo = (AggChangeBillHVO) obj;
			getPfBusiAction().processAction(IPFActionName.DEL_DELETE,
					"FN11-Cxx-SY002", null, billvo, null, null);// 删除商业收款单
			srcid = (String) billvo.getParentVO().getAttributeValue("def1");
			billno = (String) billvo.getParentVO().getAttributeValue("billno");
		}
		// 日志
		logVO.setSrcsystem("商业");
		logVO.setExedate(new UFDateTime().toString());
		logVO.setDesbill("商业单据回退");
		logVO.setTrantype(type);

		logVO.setBusinessno(srcid);
		logVO.setBillno(billno);

		String date = new UFDateTime().toString();
		Map<String, Object> postdata = new HashMap<String, Object>();
		postdata.put("billType", type);// 单据类型
		List<Map<String, Object>> bodylist = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> bodyMap = new HashMap<String, Object>();
		bodyMap.put("billId", srcid);// 外系统id
		bodyMap.put("djh", billno);// nc单据号
		bodyMap.put("createTime", date);// 接口调用时间
		bodylist.add(bodyMap);
		postdata.put("bills", bodylist);

		String json = JSON.toJSONString(postdata);

		String url = OutsideUtils.getOutsideInfo("BUSINESS03");

		try {
			HttpBusinessUtils.getUtils().connection(url, json, logVO);

		} catch (Exception e1) {
			throw new BusinessException(e1.getMessage());
		}

	}
}
