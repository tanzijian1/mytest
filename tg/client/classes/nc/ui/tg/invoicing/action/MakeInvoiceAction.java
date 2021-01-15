package nc.ui.tg.invoicing.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.infotransform.IInfoTransformService;
import nc.itf.hzvat.IOutputtaxMaintain;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.hzvat.outputtax.AggOutputTaxHVO;
import nc.vo.hzvat.outputtax.OutputTaxBVO;
import nc.vo.hzvat.outputtax.OutputTaxHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.tgfn.invoicing.InvoicingBody;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONObject;

public class MakeInvoiceAction extends NCAction {

	private static final long serialVersionUID = 1L;

	public MakeInvoiceAction() {
		setBtnName("开票");
		setCode("MakeInvoiceAction");
	}

	private AbstractUIAppModel model = null;

	BaseDAO baseDAO = null;

	private nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor;

	private nc.ui.pubapp.uif2app.view.ShowUpableBillListView listview;

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor) {
		this.editor = editor;
	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillListView getListview() {
		return listview;
	}

	public void setListview(
			nc.ui.pubapp.uif2app.view.ShowUpableBillListView listview) {
		this.listview = listview;
	}

	public AbstractUIAppModel getModel() {
		return model;
	}

	public void setModel(AbstractUIAppModel model) {
		this.model = model;
		this.model.addAppEventListener(this);
	}

	@Override
	public void doAction(ActionEvent e) throws BusinessException {

		// 判断打开单据是否列表状态 true：列表
		StringBuffer sb = new StringBuffer();
		if (this.getListview().getBillListPanel().isShowing()) {
			Object[] objs = ((BillManageModel) getModel())
					.getSelectedOperaDatas();
			if (objs != null && objs.length > 0) {
				for (Object object : objs) {
					AggInvoicingHead aggvo = (AggInvoicingHead) object;
					if (!"Y".equals(aggvo.getParentVO().getDef47())) {
						JSONObject json = null;
						try {
							json = openInvoice(aggvo);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if (json != null) {
							if ("200".equals(json.getString("code"))) {

								ISyncEBSServcie approve = NCLocator
										.getInstance().lookup(
												ISyncEBSServcie.class);

								aggvo.getParentVO().setDef47("Y");// 开票标识
								aggvo.getParentVO()
										.setDef36(
												json.getString("totalAmountWithoutTax"));// 开票总金额（不含税）
								aggvo.getParentVO().setDef37(
										json.getString("totalTaxAmount"));// 开票总金额（税额）
								aggvo.getParentVO().setDef38(
										json.getString("totalAmount"));// 开票总金额（含税）
								aggvo.getParentVO().setDr(0);

								IVOPersistence ip = NCLocator.getInstance()
										.lookup(IVOPersistence.class);

								ip.updateVO(aggvo.getParentVO());

								approve.MakeInvoiceApproveUpdate(aggvo, json);

							} else {
								sb.append("【" + aggvo.getParentVO().getBillno()
										+ "】开票失败," + json.getString("msg"));
							}
						} else {
							throw new BusinessException("连接乐税服务器失败");
						}
					} else {
						sb.append("【" + aggvo.getParentVO().getBillno()
								+ "】已开票,");
					}
				}
			}
		} else {
			Object obj = getModel().getSelectedData();
			AggInvoicingHead aggvo = (AggInvoicingHead) obj;
			if (!"Y".equals(aggvo.getParentVO().getDef47())) {
				JSONObject json = null;
				try {
					json = openInvoice(aggvo);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (json != null) {
					if ("200".equals(json.getString("code"))) {
						ISyncEBSServcie approve = NCLocator.getInstance()
								.lookup(ISyncEBSServcie.class);

						aggvo.getParentVO().setDef47("Y");// 开票标识
						aggvo.getParentVO().setDef36(
								json.getString("totalAmountWithoutTax"));// 开票总金额（不含税）
						aggvo.getParentVO().setDef37(
								json.getString("totalTaxAmount"));// 开票总金额（税额）
						aggvo.getParentVO().setDef38(
								json.getString("totalAmount"));// 开票总金额（含税）
						aggvo.getParentVO().setDr(0);

						IVOPersistence ip = NCLocator.getInstance().lookup(
								IVOPersistence.class);

						ip.updateVO(aggvo.getParentVO());

						approve.MakeInvoiceApproveUpdate(aggvo, json);
					} else {
						sb.append("【" + aggvo.getParentVO().getBillno()
								+ "】开票失败," + json.getString("msg"));
					}
				} else {
					throw new BusinessException("连接乐税服务器失败");
				}
			} else {
				sb.append("【" + aggvo.getParentVO().getBillno() + "】已开票,");
			}
		}

		if ("".equals(sb) || sb == null) {
			MessageDialog.showHintDlg(editor, "提示", "开票成功");
		} else {
			MessageDialog.showHintDlg(editor, "提示", sb.toString());
		}

	}

	private JSONObject openInvoice(AggInvoicingHead aggvo) throws Exception {

		HashMap<String, Object> data = new HashMap<String, Object>();

		List<Object> token = getToken();

		data.put("appID", "NC");
		data.put("time", (long) token.get(0));
		data.put("token", (String) token.get(1));
		data.put("billGuid", aggvo.getParentVO().getPk_invoicing());
		data.put("billNo", aggvo.getParentVO().getBillno());
		data.put("sellMachineNo", "0");
		data.put("user", "");
		data.put("invType", 0);// 发票类型
		data.put("billType", aggvo.getParentVO().getBilltype());// 单据类型
		data.put("billSource", "NC");// 单据来源
		data.put("bizDate", aggvo.getParentVO().getBilldate() == null ? ""
				: aggvo.getParentVO().getBilldate().toString().substring(0, 10));// 业务日期
		data.put("buyName", querySupplierName(aggvo.getParentVO().getDef3()));// 购方名称
		data.put("buyTaxNo", aggvo.getParentVO().getDef4());// 购方纳税人识别号（业主身份证，企业统一信用代码）
		data.put("buyBankAccount", aggvo.getParentVO().getDef7() == null ? null
				: aggvo.getParentVO().getDef7() + aggvo.getParentVO().getDef8());// 购方开户行，开户账号
		data.put("buyAddrTel", aggvo.getParentVO().getDef5() == null ? null
				: aggvo.getParentVO().getDef5() + aggvo.getParentVO().getDef6());// 购方地址电话
		data.put("note", aggvo.getParentVO().getDef42());// 备注
		data.put("sellName", queryOrgName(aggvo.getParentVO().getDef10()));// 销方名称
		data.put("sellTaxNo", aggvo.getParentVO().getDef11());// 销方税号
		data.put("sellAddrTel", aggvo.getParentVO().getDef12() == null ? null
				: aggvo.getParentVO().getDef12().substring(0, 3)
						+ aggvo.getParentVO().getDef13());// 销方地址电话
		// data.put("sellTel", aggvo.getParentVO().getDef13());// 销方电话
		data.put("goodsVersion", 33.0);//
		data.put("confirmWin", 0);//
		data.put("printType", 0);//
		data.put("sellBankAccount",
				aggvo.getParentVO().getDef14() == null ? null : aggvo
						.getParentVO().getDef14()
						+ aggvo.getParentVO().getDef15());// 销方账号
		data.put("cashier", aggvo.getParentVO().getDef44());// 收款人 ----------
		data.put("checker", aggvo.getParentVO().getDef45());// 复核人 ----------
		data.put("invoicer", aggvo.getParentVO().getDef46());// 开票人 ----------
		data.put("sourceinvCode", "");// 源正数发票代码
		data.put("sourceinvNo", "");// 源正数发票号码
		data.put("negativeFlag", 0);// 值须为数值0（非红冲）或1（红冲），默认0
		data.put("negNativeNo", "");// 专用发票且是负数票时需要输入
		// data.put("isExtraBill", "");// 是否补票
		// data.put("serialNo", 0);// //开票机号

		// Double totalTaxAmount = 0.0;
		// Double totalAmount = 0.0;
		// Double totalAmountWithoutTax = 0.0;

		InvoicingBody[] invoicingBody = (InvoicingBody[]) aggvo.getChildrenVO();
		List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
		for (InvoicingBody inv : invoicingBody) {
			HashMap<String, Object> detail = new HashMap<String, Object>();
			detail.put("dtsGuid", inv.getPk_invbody());
			detail.put("goodsName", inv.getDef6());// 商品名称
			detail.put("standard", inv.getDef7());// 规格型号
			detail.put("unit", inv.getDef9());// 单位
			detail.put("number", inv.getDef10());// 数量
			detail.put(
					"taxPrice",
					new UFDouble(inv.getDef25()).div(
							new UFDouble(inv.getDef10())).toString());// 含税单价（最多12位小数）
			detail.put("withoutTaxPrice",
					new UFDouble(inv.getDef16()).toString());// 不含税单价（最多12位小数）
			detail.put("amountWithTax",
					new UFDouble(inv.getDef25()).doubleValue());// 含税金额（保留两位小数）
			detail.put("amountWithoutTax",
					new UFDouble(inv.getDef23()).doubleValue());// 不含税金额（保留两位小数）
			detail.put("rate",
					(new UFDouble(inv.getDef17()).doubleValue()) * 100);// 税率
			detail.put("taxAmount", new UFDouble(inv.getDef24()).doubleValue());// 税额
			detail.put("taxTypeCode", inv.getDef8());// 税收分类编码
			detail.put("discountFlag", 0);// 是否享受优惠（0：不享受；1：享受）
			detail.put("discountType", "");// 优惠政策类型
			detail.put("discountNote", "");// 优惠政策说明
			detail.put("discountFlage", 0);// 折扣标记
			detail.put("discountAmountWithoutTax", 0);// 折扣金额(不含税)
			detail.put("discountRate", 0);// 折扣率
			detail.put("discountTaxAmount", 0);// 折扣税额
			detail.put("taxZeroRate", "");// 零税率标志

			details.add(detail);
		}

		data.put("totalTaxAmount",
				new UFDouble(aggvo.getParentVO().getDef28()).doubleValue());// 合计税额（不传时，按明细税额累加）
		data.put("totalAmount",
				new UFDouble(aggvo.getParentVO().getDef29()).doubleValue());// 价税合计（不传时，按明细价税累加）
		data.put("totalAmountWithoutTax", new UFDouble(aggvo.getParentVO()
				.getDef27()).doubleValue());//
		// 不含税总额（不传时，按明细不含税金额累加）

		data.put("details", details);// 明细
		com.alibaba.fastjson.JSONObject jsonobject = null;
		try {
			IInfoTransformService service = NCLocator.getInstance().lookup(
					IInfoTransformService.class);

			String json = service.OpenInvoice(data);

			jsonobject = com.alibaba.fastjson.JSONObject.parseObject(json);

			if (json != null) {
				if ("200".equals(jsonobject.getString("code"))) {
					saveinvoice(aggvo, data, jsonobject);
				}
			} else {
				throw new BusinessException("连接乐税服务器失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonobject;
	}

	private void saveinvoice(AggInvoicingHead aggvo,
			HashMap<String, Object> data, JSONObject jsonobject)
			throws BusinessException {

		IOutputtaxMaintain service = NCLocator.getInstance().lookup(
				IOutputtaxMaintain.class);

		JSONObject object = jsonobject.getJSONObject("objects");

		// AggOutputTaxHVO[] aggvos = new AggOutputTaxHVO[arrays.size()];

		// for (int i = 0; i < aggvos.length; i++) {
		AggOutputTaxHVO aggOutputTaxhVO = new AggOutputTaxHVO();
		OutputTaxHVO outputTaxHVO = new OutputTaxHVO();
		// com.alibaba.fastjson.JSONObject object = arrays.getJSONObject(i);
		outputTaxHVO.setFpdm(object.getString("invCode"));
		outputTaxHVO.setFph(object.getString("invNo"));
		outputTaxHVO.setKprq(new UFDate(object.getString("invDate")));
		outputTaxHVO.setMoney_fp(new UFDouble(object.getString("totalAmount")));// 含税总额
		outputTaxHVO.setMoney_fpout(new UFDouble(object
				.getString("totalAmountWithoutTax")));// 不含税总额
		outputTaxHVO.setDef18(object.getString("totalTaxAmount"));// 税额

		outputTaxHVO.setPk_org(aggvo.getParentVO().getPk_org());
		outputTaxHVO.setPk_group(aggvo.getParentVO().getPk_group());
		// 税盆状态查询
		outputTaxHVO.setFplx(object.getString("invType"));// 0：增值税专用发票（当发票类型为0时，不允许开具零税率发票）
		// 2：增值税普通发票 51：增值税电子普通票
		// optjsonvo.setBillType(headvo.getPk_billtype());//发票类型
		// optjsonvo.setSellBankAccount(headvo.getDef6()+headvo.getDef7());//购方银行账号
		// optjsonvo.setSellAddrTel(headvo.getDef8()+headvo.getDef9());//购方地址电话
		// 购方信息1
		outputTaxHVO.setPk_kh(aggvo.getParentVO().getDef3());// 客户
		outputTaxHVO.setKhh(aggvo.getParentVO().getDef7());// 开户行
		outputTaxHVO.setYhzh(aggvo.getParentVO().getDef8());// 银行账号
		outputTaxHVO.setAdress(aggvo.getParentVO().getDef5());// 地址
		outputTaxHVO.setTelphone(aggvo.getParentVO().getDef6());// 购方电话
		outputTaxHVO.setNsrsbh(aggvo.getParentVO().getDef4());// 购方纳税人识别号（业主身份证，企业统一信用代码）

		// 销方信息
		outputTaxHVO.setKpzz(aggvo.getParentVO().getDef10());// 销方名称
																// --传相应编码，根据编码在NC中获取
		outputTaxHVO.setDef3(aggvo.getParentVO().getDef11());// 销方纳税人识别号
		outputTaxHVO.setDef6(aggvo.getParentVO().getDef14());// 销方开户行
		outputTaxHVO.setDef7(aggvo.getParentVO().getDef15());// 销方银行账号
		outputTaxHVO.setDef8(aggvo.getParentVO().getDef12());// 销方地址
		outputTaxHVO.setDef9(aggvo.getParentVO().getDef13());// 销方电话号码
		outputTaxHVO.setPk_jbr(aggvo.getParentVO().getDef39());// 经办人
		outputTaxHVO.setPk_jbbm(aggvo.getParentVO().getDef40());// 经办部门
		outputTaxHVO.setDbilldate(new UFDate(object.getString("invDate")));// 业务日期
		outputTaxHVO.setRmark(aggvo.getParentVO().getDef42());// 备注
		outputTaxHVO.setSkr(aggvo.getParentVO().getDef44());// 收款人
		outputTaxHVO.setFkr(aggvo.getParentVO().getDef46());// 复核人
		// aggOutputTaxHVO.getParentVO().setAttributeValue("def12",
		// optjsonvo.getPrintType() + "");// 打印类型
		// aggOutputTaxHVO.getParentVO().setAttributeValue("def13",
		// optjsonvo.getConfirmWin() + "");// 是否显示打印对话框
		// 外系统信息
		outputTaxHVO.setAttributeValue("def14", aggvo.getParentVO().getDef1());// 外系统id
		outputTaxHVO.setAttributeValue("def15", aggvo.getParentVO().getDef2());// 外系统单号
		outputTaxHVO.setAttributeValue("def16", "SRM");// 外系统名称
		outputTaxHVO.setDef17(aggvo.getParentVO().getDef45());// 开票人
		// aggOutputTaxHVO.getParentVO().setAttributeValue("src_taxbillcode",
		// optjsonvo.getSourceInvCode());
		// aggOutputTaxHVO.getParentVO().setAttributeValue("src_taxbillno",
		// optjsonvo.getSourceInvNo());

		// aggOutputTaxHVO.setPk_billtype("HZ08");
		// aggOutputTaxHVO.setVbillstatus(-1);//单据状态

		InvoicingBody[] invoicingBodys = (InvoicingBody[]) aggvo
				.getChildrenVO();

		List<OutputTaxBVO> taxBVOs = new ArrayList<>();
		for (InvoicingBody bvo : invoicingBodys) {
			OutputTaxBVO detailvo = new OutputTaxBVO();
			detailvo.setPk_product(bvo.getDef6());// 货物
			detailvo.setPk_dw(bvo.getDef9());// 单位
			detailvo.setNum(new UFDouble(bvo.getDef10()));// 数量
			detailvo.setPriceintax(new UFDouble(bvo.getDef19()));// 含税单价
			detailvo.setPriceouttax(new UFDouble(bvo.getDef16()));// 不含税单价
			detailvo.setMoneyintax(new UFDouble(bvo.getDef25()));// 含税金额（保留两位小数）
			detailvo.setMoneyouttax(new UFDouble(bvo.getDef23()));// 不含税金额（保留两位小数）
			detailvo.setTaxrate(new UFDouble(bvo.getDef17()));// 税率
			detailvo.setPk_vatinfo(null/* getPk_vatInfoByCode(bvo.getDef8()) */);// 税收分类编码
			detailvo.setMoneytax(new UFDouble(bvo.getDef21()));// 税额
			// detailvo.setDef1(String.valueOf(bvo.getDiscountFlag()));//是否享受优惠
			// detailvo.setDef2(bvo.getDiscountNote());//优惠政策说明
			// detailvo.setDef3(bvo.getDiscountType());//优惠政策类型
			// detailvo.setDef4(bvo.getTaxZeroRate());//零税率标志
			detailvo.setDef5(bvo.getDef7());// 规格型号
			taxBVOs.add(detailvo);
		}
		aggOutputTaxhVO.setParentVO(outputTaxHVO);
		aggOutputTaxhVO.setChildrenVO(taxBVOs.toArray(new OutputTaxBVO[0]));

		service.insert(new AggOutputTaxHVO[] { aggOutputTaxhVO }, null);

	}

	/**
	 * 根据【税收编码】获取PK
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getPk_vatInfoByCode(String code) throws BusinessException {
		String sql = "SELECT pk_vatinfo FROM hzvat_vatInfo WHERE hbcode = '"
				+ code + "' AND NVL(dr,0) = 0";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}

	public String queryOrgName(String pk) throws BusinessException {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select name from org_orgs where pk_org = '" + pk
				+ "' and nvl(dr,0) = 0";

		String name = (String) bs.executeQuery(sql, new ColumnProcessor());

		return name;
	}

	public String querySupplierName(String pk) throws BusinessException {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select name from bd_customer where pk_customer = '" + pk
				+ "' and nvl(dr,0) = 0";

		String name = (String) bs.executeQuery(sql, new ColumnProcessor());

		return name;
	}


	private List<Object> getToken() {
		List<Object> tokens = new ArrayList<>();
		Date date = new Date();
		String ticket = "NCf185f3a7-b6f9-402d-bc7e-2aa3acb74c3d"
				+ date.getTime();
		String ticketMD5 = DigestUtils.md5Hex(ticket);
		tokens.add(date.getTime());
		tokens.add(ticketMD5);
		return tokens;
	}

	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
}
