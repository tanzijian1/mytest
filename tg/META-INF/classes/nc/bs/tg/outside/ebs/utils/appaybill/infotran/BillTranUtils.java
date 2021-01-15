package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.arap.bill.ArapBillCalUtil;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.service.TimeService;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.cmp.bill.util.SysInit;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.arap.sysinit.SysinitConst;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.tg.outside.appaybill.PayBillHeaderVO;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class BillTranUtils extends EBSBillUtils {
	Map<String, String> tradetypeMap = null;

	public Map<String, String> getTradetypeMap() {
		if (tradetypeMap == null) {
			tradetypeMap = new HashMap<String, String>();
			tradetypeMap.put(EBSCont.SRCBILL_14, "F1-Cxx-001");// EBS-��Ŀ�ܽ����->Ӧ����
																// ���ɱ�Ӧ������
			tradetypeMap.put(EBSCont.SRCBILL_09, "F1-Cxx-003");// SRM���˵�->Ӧ����
			tradetypeMap.put(EBSCont.SRCBILL_22, "F1-Cxx-006");// EBS-������->Ӧ����
			tradetypeMap.put(EBSCont.SRCBILL_03, "F1-Cxx-004");// EBS-ͨ�ú�ͬ���->Ӧ����
			tradetypeMap.put(EBSCont.SRCBILL_05, "F1-Cxx-005");// EBS-Ӫ�������->Ӧ����
			tradetypeMap.put(EBSCont.SRCBILL_41, "F1-Cxx-007");// EBS-ͨ���ղ������->Ӧ����
			tradetypeMap.put(EBSCont.SRCBILL_49, "F1-Cxx-010");// EBS-�ɱ�˰��Ӧ����
			tradetypeMap.put(EBSCont.SRCBILL_50, "F1-Cxx-011");// EBS-�ɱ�ռԤ��Ӧ����
		}
		return tradetypeMap;
	}

	/**
	 * �����Ϣ����
	 * 
	 * @throws BusinessException
	 */
	protected void calculate(AggPayableBillVO billvo, String key, int row)
			throws BusinessException {

		if (billvo.getChildrenVO() != null && billvo.getChildrenVO().length > 0) {
			PayableBillItemVO itemVO = (PayableBillItemVO) billvo
					.getChildrenVO()[row];
			String currType = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_CURRTYPE);
			String pk_org = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_ORG);
			String pk_group = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_GROUP);
			String pk_currtype = CurrencyRateUtilHelper.getInstance()
					.getLocalCurrtypeByOrgID(pk_org);

			// ScaleUtils scale = new ScaleUtils(pk_group);
			// IDataSetForCal data = new ArapDataDataSet(billvo, row,
			// new RelationItemForCal_Debit());
			// Calculator tool = new Calculator(data, scale);
			Condition cond = new Condition();
			if (pk_currtype == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("2006pub_0", "02006pub-0552"));
			}
			CurrtypeVO currTypeVo = CurrtypeQuery.getInstance().getCurrtypeVO(
					pk_currtype);
			String destCurrType = currTypeVo.getPk_currtype();
			cond.setCalOrigCurr(true);
			if ((IBillFieldGet.LOCAL_MONEY_CR.equals(key) || IBillFieldGet.LOCAL_MONEY_DE
					.equals(key)) && !destCurrType.equals(currType)) {// ��֯��ǰ����Ϊ��֯����
				cond.setCalOrigCurr(false);
			}
			cond.setIsCalLocalCurr(true);
			cond.setIsChgPriceOrDiscount(false);
			cond.setIsFixNchangerate(false);
			cond.setIsFixNqtunitrate(false);
			Object buysellflag = itemVO
					.getAttributeValue(IBillFieldGet.BUYSELLFLAG);
			boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
					.equals(buysellflag)
					|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
							.equals(buysellflag);
			cond.setInternational(isInternational);

			String AP21 = SysInit.getParaString(pk_org, SysinitConst.AP21);
			cond.setIsTaxOrNet(SysinitConst.ARAP21_TAX.equals(AP21));

			cond.setGroupLocalCurrencyEnable(ArapBillCalUtil
					.isUseGroupMoney(pk_group));
			cond.setGlobalLocalCurrencyEnable(ArapBillCalUtil
					.isUseGlobalMoney());
			cond.setOrigCurToGlobalMoney(ArapBillCalUtil
					.isOrigCurToGlobalMoney());
			cond.setOrigCurToGroupMoney(ArapBillCalUtil
					.isOrigCurToGroupMoney(pk_group));
			calulateTax(itemVO);
			calulateBalance(itemVO);
		}

	}

	/**
	 * ��Ϣת��
	 * 
	 * @param srcdata
	 * @param srctype
	 * @return
	 */
	public AggPayableBillVO onTranBill(JSONObject srcdata, String srctype)
			throws BusinessException {
		JSON jsonhead = (JSON) srcdata.get("headInfo");// ��ϵͳ��Դ��ͷ����
		String jsonbody = srcdata.getString("bodyInfos");// ��ϵͳ��Դ��������
		if (srcdata == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + srcdata);
		}

		PayBillHeaderVO headvo = JSONObject.toJavaObject(jsonhead,
				PayBillHeaderVO.class);// �ⲿϵͳ��Ϣ
		List<PayBillItemVO> bodylist = JSON.parseArray(jsonbody,
				PayBillItemVO.class);// �ⲿϵͳ��Ϣ

		// �����Ƿ��ж�Ӧ�Ĳɹ�Э��
		/*
		 * if ("09".equals(srctype)) {
		 * 
		 * AggCtApVO ApVO = (AggCtApVO) getBillVO(AggCtApVO.class,
		 * "isnull(dr,0)=0 and vbillcode = '" + headvo.getPurchaseno() +
		 * "' and ctname = '" + headvo.getPurchasename() + "'");
		 * 
		 * if (ApVO == null) { throw new BusinessException("����ͬ��Э�鵽NC��Э����룺" +
		 * headvo.getPurchaseno() + "��Э�����ƣ�" + headvo.getPurchasename()); }
		 * 
		 * }
		 */
		AggPayableBillVO billVO = onDefaultValue(headvo, bodylist, srctype);
		// ������Ϣת��

		UFDouble money = UFDouble.ZERO_DBL;// ���
		UFDouble local_money = UFDouble.ZERO_DBL;// ��֯���
		UFDouble group_money = UFDouble.ZERO_DBL;// ���Ž��
		UFDouble global_money = UFDouble.ZERO_DBL;// ȫ�ֽ��
		// ��ϸ��Ϣת��
		List<PayableBillItemVO> blists = new ArrayList<PayableBillItemVO>();
		if (bodylist != null && bodylist.size() > 0) {
			for (int row = 0; row < bodylist.size(); row++) {

				try {
					PayBillItemVO bodyvo = bodylist.get(row);
					PayableBillItemVO itmevo = (PayableBillItemVO) billVO
							.getChildrenVO()[row];

					setItemVO((PayableBillVO) billVO.getParentVO(), itmevo,
							bodyvo);

					calculate(billVO, IBillFieldGet.MONEY_CR, row);
					// itmevo.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR,
					// bodyvo.getLocal_tax_cr());// ˰��
					calculate(billVO, IBillFieldGet.LOCAL_TAX_CR, row);
					money = money.add(itmevo.getMoney_cr());// ���
					local_money = local_money.add(itmevo.getLocal_money_cr());// ��֯���
					group_money = group_money.add(itmevo.getGroupcrebit());// ���Ž��
					global_money = global_money.add(itmevo.getGlobalcrebit());// ȫ�ֽ��
					blists.add(itmevo);
				} catch (Exception e) {
					throw new BusinessException("��[" + (row + 1) + "],"
							+ e.getMessage(), e);
				}
			}
		}

		billVO.getParentVO().setAttributeValue(PayableBillVO.MONEY, money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.LOCAL_MONEY,
				local_money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.GROUPLOCAL,
				group_money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.GLOBALLOCAL,
				global_money);

		// SRM���ܸ�����Ϣ
		if ("09".equals(srctype)) {
			JSONArray JSarr = srcdata.getJSONArray("attachment");
			if (JSarr != null) {

				for (int j = 0; j < JSarr.size(); j++) {
					JSONObject JSObj = (JSONObject) JSarr.get(j);

					String srmno = JSObj.getString("fileno");
					String srmid = JSObj.getString("fileID");
					String srmtype = JSObj.getString("filetype");
					String att_name = JSObj.getString("att_name");
					String att_address = JSObj.getString("att_address");

					String select_sql = "select count(1) from attachment where nvl(dr,0) = 0 and srmno = '"
							+ srmno + "' and srmid = '"+srmid+"'";

					int num = (int) getBaseDAO().executeQuery(select_sql,
							new ColumnProcessor());

					if (num == 0) {

						String att_sql = "insert into attachment values('"
								+ srmno + "','" + srmid + "','" + srmtype
								+ "','" + att_name + "','" + att_address
								+ "',0)";

						getBaseDAO().executeUpdate(att_sql);
					}
				}
			}
		}
		return billVO;
	}

	protected AggPayableBillVO onDefaultValue(PayBillHeaderVO headvo,
			List<PayBillItemVO> bodylist, String srctype)
			throws BusinessException {
		AggPayableBillVO aggvo = new AggPayableBillVO();
		PayableBillVO hvo = new PayableBillVO();
		String billdate = headvo.getBilldate();
		if (billdate == null || "".equals(billdate)) {
			throw new BusinessException("�������ڲ���Ϊ��!");
		}

		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// ��ǰʱ��

		hvo.setAttributeValue(PayableBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// ����
		hvo.setAttributeValue(PayableBillVO.PK_ORG,
				getPk_orgByCode(headvo.getOrg()));// Ӧ��������֯->NCҵ��Ԫ����
		hvo.setAttributeValue(PayableBillVO.BILLMAKER, getSaleUserID());// �Ƶ���
		hvo.setAttributeValue(PayableBillVO.CREATOR, hvo.getBillmaker());// ������
		hvo.setAttributeValue(PayableBillVO.MODIFIEDTIME, new UFDate());// ����޸�ʱ��
		hvo.setAttributeValue(PayableBillVO.MODIFIER, "1001ZZ100000001MRF59");// ����޸���
		hvo.setAttributeValue(PayableBillVO.CREATIONTIME, currTime);// ����ʱ��
		hvo.setAttributeValue(PayableBillVO.PK_BILLTYPE, IBillFieldGet.F1);// �������ͱ���
		hvo.setAttributeValue(PayableBillVO.BILLCLASS, IBillFieldGet.YF);// ���ݴ���
		hvo.setAttributeValue(PayableBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ��������ϵͳ
		hvo.setAttributeValue(PayableBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ������Դϵͳ
		// if ("41".equals(srctype)) {
		// hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, );// Ӧ������code
		// }
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, IBillFieldGet.D1);// Ӧ������code
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// ����״̬
		// ��������
		BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetypeMap().get(
				srctype));
		if (billTypeVo == null) {
			throw new BusinessException("��"
					+ EBSCont.getSrcBillNameMap().get(srctype)
					+ "����صĽ�������δ����,����ϵϵͳ����Ա!");
		}

		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// Ӧ������
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, getTradetypeMap()
				.get(srctype));// Ӧ������
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// �ڳ���־
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// �Ƿ����
		hvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// ���ݻ�����
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// ���ݻ���ڼ�
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);
		setHeaderVO(hvo, headvo);

		aggvo.setParentVO(hvo);
		PayableBillItemVO[] itemVOs = new PayableBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayableBillItemVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// �к�
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
		IArapBillPubQueryService servie = NCLocator.getInstance().lookup(
				IArapBillPubQueryService.class);
		servie.getDefaultVO(aggvo, true);

		return aggvo;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected abstract void setHeaderVO(PayableBillVO hvo,
			PayBillHeaderVO headvo) throws BusinessException;

	/**
	 * ��ϸ��Ϣ
	 * 
	 * @param parentVO
	 * @param itmevo
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected abstract void setItemVO(PayableBillVO parentVO,
			PayableBillItemVO itmevo, PayBillItemVO bodyvo)
			throws BusinessException;

	/**
	 * ���������Ϣ
	 * 
	 * @param itemVO
	 */
	protected void calulateBalance(PayableBillItemVO itemVO) {
		boolean direction = (Integer) itemVO
				.getAttributeValue(IBillFieldGet.DIRECTION) == Direction.CREDIT.VALUE
				.intValue();
		String local_money = direction ? "local_money_cr" : "local_money_de";
		String money = direction ? "money_cr" : "money_de";
		String quantity = direction ? "quantity_cr" : "quantity_de";
		String group_money = direction ? "groupcrebit" : "groupdebit";
		String global_money = direction ? "globalcrebit" : "globaldebit";
		String money_bal = "money_bal";
		String local_notax = direction ? BaseItemVO.LOCAL_NOTAX_CR
				: BaseItemVO.LOCAL_NOTAX_DE;
		String group_notax = direction ? IBillFieldGet.GROUPNOTAX_CRE
				: IBillFieldGet.GROUPNOTAX_DE;
		String global_notax = direction ? IBillFieldGet.GLOBALNOTAX_CRE
				: IBillFieldGet.GLOBALNOTAX_DE;
		String group_tax = direction ? IBillFieldGet.GROUPTAX_CRE
				: IBillFieldGet.GROUPTAX_DE;
		String global_tax = direction ? IBillFieldGet.GLOBALTAX_CRE
				: IBillFieldGet.GLOBALTAX_DE;

		itemVO.setAttributeValue(money_bal,
				itemVO.getAttributeValue(money) == null ? UFDouble.ZERO_DBL
						: itemVO.getAttributeValue(money));
		itemVO.setAttributeValue(IBillFieldGet.LOCAL_MONEY_BAL, itemVO
				.getAttributeValue(local_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(local_money));

		itemVO.setAttributeValue(IBillFieldGet.GROUPBALANCE, itemVO
				.getAttributeValue(group_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(group_money));

		itemVO.setAttributeValue(IBillFieldGet.GLOBALBALANCE, itemVO
				.getAttributeValue(global_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(global_money));
		itemVO.setAttributeValue(IBillFieldGet.QUANTITY_BAL, itemVO
				.getAttributeValue(quantity) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(quantity));
		itemVO.setAttributeValue(IBillFieldGet.OCCUPATIONMNY, itemVO
				.getAttributeValue(money_bal) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(money_bal));

		String caltaxmny_key = BillEnumCollection.TaxType.TAXOUT.VALUE
				.equals(itemVO.getAttributeValue(BaseItemVO.TAXTYPE)) ? local_notax
				: local_money;
		itemVO.setAttributeValue(IBillFieldGet.CALTAXMNY, itemVO
				.getAttributeValue(caltaxmny_key) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(caltaxmny_key));

		itemVO.setAttributeValue(
				group_tax,
				MathTool.sub(
						itemVO.getAttributeValue(group_money) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(group_money),
						(UFDouble) itemVO.getAttributeValue(group_notax) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(group_notax)));

		itemVO.setAttributeValue(
				global_tax,
				MathTool.sub(
						itemVO.getAttributeValue(global_money) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(global_money),
						(UFDouble) itemVO.getAttributeValue(global_notax) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(global_notax)));
	}

	/***
	 * ������˰�����Ϣ
	 * 
	 * @param itemVO
	 */
	protected void calulateTax(PayableBillItemVO itemVO) {
		Object buysellflag = itemVO.getBuysellflag();
		boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
				.equals(buysellflag)
				|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
						.equals((buysellflag));
		UFDouble grouptaxmny = itemVO.getGroupcrebit();
		UFDouble groupnotaxmny = itemVO.getGroupnotax_cre();
		itemVO.setGrouptax_cre(MathTool.sub(grouptaxmny, groupnotaxmny));

		UFDouble globaltaxmny = itemVO.getGlobalcrebit();
		UFDouble globalnotaxmny = itemVO.getGlobalnotax_cre();
		itemVO.setGlobaltax_cre(MathTool.sub(globaltaxmny, globalnotaxmny));
		if (isInternational) {
			itemVO.setGlobalnotax_cre(itemVO.getGlobalcrebit());
			itemVO.setGroupnotax_cre(itemVO.getGroupcrebit());
			itemVO.setLocal_notax_cr(itemVO.getLocal_money_cr());
			itemVO.setNotax_cr(itemVO.getMoney_cr());
			itemVO.setCaltaxmny(itemVO.getLocal_notax_cr());
		}
	}

}
