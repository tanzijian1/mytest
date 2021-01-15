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
		// ͨ�õ���
		if ("F1-Cxx-004".equals(pk_tradetype)
				|| "F1-Cxx-007".equals(pk_tradetype)) {
			// ͨ�û�д
			if (aggvo.getParentVO().getAttributeValue("def1") == null) {
				new BusinessException("EBS����Ϊ��");
			}
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);
			String returnjson = null;
			String def52 = (String) aggvo.getParentVO().getAttributeValue(
					"def52");
			// �������������ж�2020-02-19 tjl
			String def52code = queryCodebyPk(def52);
			if ("suspendedRejected".equals(def52code)) {
				MessageDialog.showHintDlg(null, "��ʾ", "�õ����ѹ����������ݲ������˻�!");
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
		// �ɱ�����
		else if ("F1-Cxx-001".equals(pk_tradetype)) {
			String returnjson = null;
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);
			String def52 = (String) aggvo.getParentVO().getAttributeValue(
					"def52");
			// �������������ж�2019-12-23-̸�ӽ�
			String def52code = queryCodebyPk(def52);
			if ("suspendedRejected".equals(def52code)) {
				MessageDialog.showHintDlg(null, "��ʾ", "�õ����ѹ����������ݲ������˻�!");
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
		// ռԤ��+��˰��Ӧ�����˻���ebs(�ɱ�)
		/**
		 * F1-Cxx-010,�ɱ�˰��Ӧ���� F1-Cxx-011,�ɱ�ռԤ��Ӧ���� F1-Cxx-012,ͨ��˰��Ӧ����
		 * F1-Cxx-013,ͨ��ռԤ��Ӧ����
		 */
		else if ("F1-Cxx-010".equals(pk_tradetype)
				|| "F1-Cxx-011".equals(pk_tradetype)) {
			String returnjson = null;
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);
			String def52 = (String) aggvo.getParentVO().getAttributeValue(
					"def52");
			// �������������ж�2019-12-23-̸�ӽ�
			String def52code = queryCodebyPk(def52);
			if ("suspendedRejected".equals(def52code)) {
				MessageDialog.showHintDlg(null, "��ʾ", "�õ����ѹ����������ݲ������˻�!");
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

		}// ռԤ��+��˰��Ӧ�����˻���ebs(ͨ��)
		else if ("F1-Cxx-012".equals(pk_tradetype)
				|| "F1-Cxx-013".equals(pk_tradetype)) {
			String returnjson = null;
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);
			HashMap eParam = new HashMap();
			String def52 = (String) aggvo.getParentVO().getAttributeValue(
					"def52");
			// �������������ж�2019-12-23-̸�ӽ�
			String def52code = queryCodebyPk(def52);
			if ("suspendedRejected".equals(def52code)) {
				MessageDialog.showHintDlg(null, "��ʾ", "�õ����ѹ����������ݲ������˻�!");
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

	// ����������ѯ�Զ��嵵��code
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
		String ifpay = null;// �Ƿ���ת���������YΪ�ǣ�
		String zflag = null;// (ת���� )
		List<AggregatedValueObject> listbillvo = null;// ����Эͬ����VO
		if ("F3-Cxx-001".equals(object.getParentVO().getAttributeValue(
				"pk_tradetype"))
				|| "F2-Cxx-001".equals(object.getParentVO().getAttributeValue(
						"pk_tradetype"))
				&& cuserid.equals(((AggregatedValueObject) obj).getParentVO()
						.getAttributeValue("billmaker"))) {
			// ת�����ո�����ɾ��Эͬ���ݣ�
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
			// �߽ű�ɾ������ת�����ո��
			if (listbillvo != null) {
				for (AggregatedValueObject aobj : listbillvo) {
					deletepayrecBillVo(aobj);
				}
			}
		}
		// TODO��ת�����ո��ɾ��
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

			// �����Զ�����53����ԭ��
			// �ɱ��������뵥
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
		// TODOת����
		if ("F3-Cxx-001".equals(((AggregatedValueObject) obj).getParentVO()
				.getAttributeValue("pk_tradetype"))
				|| "F2-Cxx-001".equals(((AggregatedValueObject) obj)
						.getParentVO().getAttributeValue("pk_tradetype"))
				&& cuserid.equals(((AggregatedValueObject) obj).getParentVO()
						.getAttributeValue("billmaker"))) {

			if (!(((Integer) ((AggregatedValueObject) obj).getParentVO()
					.getAttributeValue("approvestatus")) == -1 || ((Integer) ((AggregatedValueObject) obj)
					.getParentVO().getAttributeValue("approvestatus")) == 3))
				throw new BusinessException("��������״̬����Ϊ�ύ̬������̬");

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
					mapdata.put("ncfkDate", pvo.getDef82());// �ſ�����
					mapdata.put("ncgobackJe", pvo.getMoney() == null ? null
							: pvo.getMoney().toString());// Nc��д���
					mapdata.put("command", msg);// NC�����������
					mapdata.put("adaccount", opration);// �û�AD��¼�˺�
					mapdata.put("ncDocumentNo", pvo.getBillno());// NCƱ�ݺ�
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
					// mapdata.put("ncfkDate", pvo.getDef82());//�ſ�����(�տ����Ҫ)
					mapdata.put("ncgobackJe", pvo.getMoney() == null ? null
							: pvo.getMoney().toString());// Nc��д���
					mapdata.put("command", msg);// NC�����������
					mapdata.put("adaccount", opration);// �û�AD��¼�˺�
					mapdata.put("ncDocumentNo", pvo.getBillno());// NCƱ�ݺ�

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
			datamap.put("tfid", zflag);// ת����ID,NC��ת�����ո���Ψһ��ʶ
			datamap.put("nowithdraw", "0");// 0Ϊ�˻�,1Ϊ���˻�
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
			Object num = query.executeQuery(sql, new ColumnProcessor());// ƾ֤��
			String isnum = num == null ? "0" : "1";
			String isshr = pvo.getApprovestatus() == 1 ? "1" : "0";
			String tradetype = "0";
			// if(st((String)aggvo.getParentVO().getAttributeValue(""))){
			HashMap<String, Object> mapdata = new HashMap<String, Object>();
			mapdata.put("vouchid", pvo.getDef1());// ����ϵͳ����ID
			mapdata.put("generateCredentials", isnum);// �Ƿ�����ƾ֤
			mapdata.put("ncDocumentNo", pvo.getBillno());// NCƱ�ݺ�
			mapdata.put("ncDocumentnumber", num);// ƾ֤��
			mapdata.put("shr", isshr);// ����Ƿ�ɹ�
			if ("F3-Cxx-001".equals(pvo.getPk_tradetype())) {
				tradetype = "1";
			}
			mapdata.put("isDj", tradetype);// �Ƿ�Ϊת������ݣ�1Ϊ�ǣ�
			sendgl.saleSendGL_RequiresNew(mapdata);
		} else if (obj instanceof AggGatheringBillVO
				&& cuserid.equals(((AggGatheringBillVO) obj).getParentVO()
						.getAttributeValue("billmaker")) && ifpay == null
				&& zflag == null) {
			AggGatheringBillVO aggvo = (AggGatheringBillVO) obj;
			GatheringBillVO gvo = (GatheringBillVO) aggvo.getParentVO();
			String gathersql = "select g.num from ar_gatherbill a inner join fip_relation f on f.src_relationid=a.pk_gatherbill  inner join  gl_voucher  g on g.pk_voucher =f.des_relationid where   pk_gatherbill ='"
					+ gvo.getAttributeValue("pk_gatherbill") + "'";
			Object num = query.executeQuery(gathersql, new ColumnProcessor());// ƾ֤��
			String isnum = num == null ? "0" : "1";
			String tradetype = "0";
			String isshr = gvo.getApprovestatus() == 1 ? "1" : "0";
			HashMap<String, Object> mapdata = new HashMap<String, Object>();
			mapdata.put("vouchid", gvo.getDef1());// ����ϵͳ����ID
			mapdata.put("generateCredentials", isnum);// �Ƿ�����ƾ֤��1Ϊ�ǣ�
			mapdata.put("ncDocumentNo", gvo.getBillno());// NCƱ�ݺ�
			mapdata.put("ncDocumentnumber", num);// ƾ֤��
			mapdata.put("shr", isshr);// ����Ƿ�ɹ���1Ϊ�ɹ���
			if ("F2-Cxx-001".equals(gvo.getPk_tradetype())) {
				tradetype = "1";
			}
			mapdata.put("isDj", tradetype);// �Ƿ�Ϊת������ݣ�1Ϊ�ǣ�
			sendgl.saleSendGL_RequiresNew(mapdata);
		}

		return true;
	}

	/**
	 * �õ�ת�����ո�����е��ݣ������Լ���
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
								"ת�������ݹ�����������״̬����Ϊ�ύ̬������̬,����Ϊ" + hvo.getBillno());
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
						throw new BusinessException("ת�������ݹ������ݱ���Ϊ�ύ̬������̬,����Ϊ"
								+ hvo.getBillno());
					}
				}
			}
		}
		return list;
	}

	/**
	 * �õ��տ������˻��˺�
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
	 * ɾ���ո���
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
					null, (AggPayBillVO) obj, null, eParam);// ɾ�����
		}

		if (obj instanceof AggGatheringBillVO) {
			getPfBusiAction().processAction(IPFActionName.DEL_DELETE, "D2",
					null, (AggGatheringBillVO) obj, null, eParam);// ɾ�����
		}
	}

	/**
	 * ɾ��������ebs�������뵥
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
					"UNSAVEBILL", "FN01", null, aggvo, null, eParam);// �ջظ������뵥
		} else {
			billvos = new AggPayrequest[] { aggvo };
		}
		billvos = (AggPayrequest[]) getPfBusiAction().processAction(
				IPFActionName.DEL_DELETE, "FN01", null, billvos[0], null,
				eParam);// ɾ���������뵥
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
		// TODO �Զ����ɵķ������
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
					"F2-Cxx-SY001", null, billvo, null, null);// ɾ����ҵ�տ
			srcid = (String) billvo.getParentVO().getAttributeValue("def1");
			billno = (String) billvo.getParentVO().getAttributeValue("billno");
		} else if ("FPD".equals(type)) {
			AggChangeBillHVO billvo = (AggChangeBillHVO) obj;
			getPfBusiAction().processAction(IPFActionName.DEL_DELETE,
					"FN11-Cxx-SY002", null, billvo, null, null);// ɾ����ҵ�տ
			srcid = (String) billvo.getParentVO().getAttributeValue("def1");
			billno = (String) billvo.getParentVO().getAttributeValue("billno");
		}
		// ��־
		logVO.setSrcsystem("��ҵ");
		logVO.setExedate(new UFDateTime().toString());
		logVO.setDesbill("��ҵ���ݻ���");
		logVO.setTrantype(type);

		logVO.setBusinessno(srcid);
		logVO.setBillno(billno);

		String date = new UFDateTime().toString();
		Map<String, Object> postdata = new HashMap<String, Object>();
		postdata.put("billType", type);// ��������
		List<Map<String, Object>> bodylist = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> bodyMap = new HashMap<String, Object>();
		bodyMap.put("billId", srcid);// ��ϵͳid
		bodyMap.put("djh", billno);// nc���ݺ�
		bodyMap.put("createTime", date);// �ӿڵ���ʱ��
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
