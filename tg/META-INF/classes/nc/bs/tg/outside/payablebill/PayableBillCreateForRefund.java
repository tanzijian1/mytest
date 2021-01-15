package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ObjType;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * SRM系统推送应付单数据到NC财务系统接口
 * 
 * @author ASUS
 * 
 */
public class PayableBillCreateForRefund extends PayableBillUtils {
	/**
	 * 邻里-SRM保证金退款
	 */
	@Override
	protected String getTradetype() {
		return "F1-Cxx-LL06";
	}

	public String getDefaultoperator() {
		return "LLBPM";
	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("org"))) {
			throw new BusinessException("财务组织不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("保证金单主键不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("保证金单单据号不可为空");
		}
		if (StringUtils.isBlank(head.getString("bpmid"))) {
			throw new BusinessException("BPM主键不可为空");
		}
		if (StringUtils.isBlank(head.getString("imgno"))) {
			throw new BusinessException("影像编码不可为空");
		}
		// if (StringUtils.isBlank(head.getString("imgstate"))) {
		// throw new BusinessException("影像状态不可为空");
		// }
		if (StringUtils.isBlank(head.getString("psndoc"))) {
			throw new BusinessException("业务员不可为空");
		}

		if (StringUtils.isBlank(head.getString("psnmail"))) {
			throw new BusinessException("业务员邮箱不可为空");
		}

		if (StringUtils.isBlank(head.getString("isforfeiture"))) {
			throw new BusinessException("是否罚没不可为空");
		}

		if (StringUtils.isBlank(head.getString("currtype"))) {
			throw new BusinessException("币种不可为空");
		}

	}

	@Override
	protected void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);

		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("org"));
		if (orgInfo == null) {
			throw new BusinessException("出账公司[" + head.getString("org")
					+ "]未能在NC关联到相关信息");
		}
		hvo.setAttributeValue(PayableBillVO.PK_ORG, orgInfo.get("pk_org"));// 应付财务组织->NC业务单元编码

		// bpmid
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// 自定义项2
																	// 外系统单据号->付款申请号
		hvo.setAttributeValue("def3", head.getString("imgno"));// 自定义项3
																// 影像编码->影像编码
		hvo.setAttributeValue("def4", head.getString("imgstate"));// 自定义项4
																	// 影像状态->影像状态

		hvo.setAttributeValue("def13", head.getString("def13"));// 自定义项13 备注

		String currtype = head.getString("currtype");// 币种
		HashMap<String, String> currtypeInfo = DocInfoQryUtils.getUtils()
				.getCurrtypeInfo(currtype);
		if (currtypeInfo == null) {
			throw new BusinessException("币种[" + currtype + "]未能在NC档案关联到相关信息!");
		}
		hvo.setPk_currtype(currtypeInfo.get("pk_currtype"));

		String psndoc = head.getString("psndoc");// 人员
		hvo.setAttributeValue(PayableBillVO.DEF11, psndoc);
		hvo.setAttributeValue(PayableBillVO.DEF24, head.getString("psnmail"));// 业务员邮箱/自定义项24

		hvo.setAttributeValue(PayableBillVO.DEF13,
				"Y".equals(head.getString("isforfeiture")) ? "Y" : "N");// 是否罚没/def13s

		String supplier = head.getString("supplier");
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				supplier, hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("供应商[" + supplier + "]未能在NC档案关联到相关信息!");
		}

		hvo.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// 供应商

	}

	@Override
	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// 所属集团
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// 单据类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// 交易类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// 交易类型ID
		itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
				headVO.getBilldate());// 单据日期
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// 起算日期
		itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// 部门
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// 部 门
		itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// 业务员

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE,
				ObjType.SUPPLIER.VALUE);// 往来对象

		itemVO.setAttributeValue(PayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// 方向
		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// 挂起标志
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// 购销类型
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// 三角贸易
		itemVO.setAttributeValue(PayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// 币种
		itemVO.setAttributeValue(PayableBillItemVO.RATE, headVO.getRate());// 组织本币汇率
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// 收货国

		itemVO.setAttributeValue(IBillFieldGet.SUPPLIER, headVO.getSupplier());// 供应商

		String balatype = body.getString("balatype");// 结算方式
		String pk_balatype = DocInfoQryUtils.getUtils()
				.getBalatypeKey(balatype);//
		if (pk_balatype == null) {
			throw new BusinessException("结算方式[" + balatype + "]未能在NC档案关联到相关信息!");
		}

		itemVO.setAttributeValue("pk_balatype", pk_balatype);// 结算方式

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, UFDouble.ZERO_DBL);// 税率
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, body
				.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr")));// 借方原币金额 //
		// 价税合计
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// 本币金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR,
				itemVO.getMoney_cr());// // 组织本币无税金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR,
				UFDouble.ZERO_DBL);// 税额

		String moneytype = body.getString("fundtype");// 款项类型
		itemVO.setDef10(moneytype);

		// 收款银行账户
		Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
				.getSupplierAccnumInfo(itemVO.getSupplier(),
						body.getString("recaccount"));
		if (recaccountInfo == null) {
			throw new BusinessException("收款银行账户["
					+ body.getString("recaccount") + "]未能在NC档案关联到相关信息!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
				recaccountInfo.get("pk_bankaccsub"));// 收款银行账户

		itemVO.setAttributeValue("def30", recaccountInfo.get("bankname"));// 开户行名称/自定义项28
		itemVO.setAttributeValue("def29", recaccountInfo.get("bankcode"));// 开户行编码/自定义项25

		// 付银行账户
		Map<String, String> payaccountInfo = DocInfoQryUtils.getUtils()
				.getBankaccnumInfo(itemVO.getPk_org(),
						body.getString("payaccount"));
		if (payaccountInfo == null) {
			throw new BusinessException("付款银行账户["
					+ body.getString("payaccount") + "]未能在NC档案关联到相关信息!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.PAYACCOUNT,
				recaccountInfo.get("pk_bankaccsub"));// 付款银行账户

		String bondtype = body.getString("bondtype");// 招标保证金类型
		itemVO.setAttributeValue(PayableBillItemVO.DEF11, bondtype);// 招标保证金类型
		itemVO.setAttributeValue(PayableBillItemVO.DEF43,
				body.getString("bondmoney"));// 投标保证金金额

		itemVO.setAttributeValue(PayableBillItemVO.DEF44,
				body.getString("forfeituremoney"));// 罚没金额
		itemVO.setAttributeValue(PayableBillItemVO.DEF42,
				body.getString("forfeiturenote"));// 罚没原因

		String budgetsub = body.getString("budgetsub");// 预算科目
		HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
				.getBudgetsubInfo(budgetsub);
		if (budgetsubInfo == null) {
			throw new BusinessException("预算科目[" + budgetsub
					+ "]未能在NC档案关联到相关信息!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.DEF1,
				budgetsubInfo.get("pk_obj"));//
	}

	/**
	 * 检验行信息必录
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		if (StringUtils.isBlank(body.getString("balatype"))) {
			throw new BusinessException("结算方式不可为空");
		}
		if (StringUtils.isBlank(body.getString("budgetsub"))) {
			throw new BusinessException("预算科目不可为空");
		}
		if (StringUtils.isBlank(body.getString("recaccount"))) {
			throw new BusinessException("收款银行账户不可为空");
		}
		if (StringUtils.isBlank(body.getString("payaccount"))) {
			throw new BusinessException("付款银行账户不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("退保证金金额金额不可为空");
		}
		if (StringUtils.isBlank(body.getString("fundtype"))) {
			throw new BusinessException("款项类型不可为空");
		}

		if (StringUtils.isBlank(body.getString("bondtype"))) {
			throw new BusinessException("招标保证金类型不可为空");
		}
		if (StringUtils.isBlank(body.getString("bondmoney"))) {
			throw new BusinessException("投标保证金金额不可为空");
		}

	}

}
