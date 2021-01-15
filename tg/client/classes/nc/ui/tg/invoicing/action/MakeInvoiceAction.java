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
		setBtnName("��Ʊ");
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

		// �жϴ򿪵����Ƿ��б�״̬ true���б�
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

								aggvo.getParentVO().setDef47("Y");// ��Ʊ��ʶ
								aggvo.getParentVO()
										.setDef36(
												json.getString("totalAmountWithoutTax"));// ��Ʊ�ܽ�����˰��
								aggvo.getParentVO().setDef37(
										json.getString("totalTaxAmount"));// ��Ʊ�ܽ�˰�
								aggvo.getParentVO().setDef38(
										json.getString("totalAmount"));// ��Ʊ�ܽ���˰��
								aggvo.getParentVO().setDr(0);

								IVOPersistence ip = NCLocator.getInstance()
										.lookup(IVOPersistence.class);

								ip.updateVO(aggvo.getParentVO());

								approve.MakeInvoiceApproveUpdate(aggvo, json);

							} else {
								sb.append("��" + aggvo.getParentVO().getBillno()
										+ "����Ʊʧ��," + json.getString("msg"));
							}
						} else {
							throw new BusinessException("������˰������ʧ��");
						}
					} else {
						sb.append("��" + aggvo.getParentVO().getBillno()
								+ "���ѿ�Ʊ,");
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

						aggvo.getParentVO().setDef47("Y");// ��Ʊ��ʶ
						aggvo.getParentVO().setDef36(
								json.getString("totalAmountWithoutTax"));// ��Ʊ�ܽ�����˰��
						aggvo.getParentVO().setDef37(
								json.getString("totalTaxAmount"));// ��Ʊ�ܽ�˰�
						aggvo.getParentVO().setDef38(
								json.getString("totalAmount"));// ��Ʊ�ܽ���˰��
						aggvo.getParentVO().setDr(0);

						IVOPersistence ip = NCLocator.getInstance().lookup(
								IVOPersistence.class);

						ip.updateVO(aggvo.getParentVO());

						approve.MakeInvoiceApproveUpdate(aggvo, json);
					} else {
						sb.append("��" + aggvo.getParentVO().getBillno()
								+ "����Ʊʧ��," + json.getString("msg"));
					}
				} else {
					throw new BusinessException("������˰������ʧ��");
				}
			} else {
				sb.append("��" + aggvo.getParentVO().getBillno() + "���ѿ�Ʊ,");
			}
		}

		if ("".equals(sb) || sb == null) {
			MessageDialog.showHintDlg(editor, "��ʾ", "��Ʊ�ɹ�");
		} else {
			MessageDialog.showHintDlg(editor, "��ʾ", sb.toString());
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
		data.put("invType", 0);// ��Ʊ����
		data.put("billType", aggvo.getParentVO().getBilltype());// ��������
		data.put("billSource", "NC");// ������Դ
		data.put("bizDate", aggvo.getParentVO().getBilldate() == null ? ""
				: aggvo.getParentVO().getBilldate().toString().substring(0, 10));// ҵ������
		data.put("buyName", querySupplierName(aggvo.getParentVO().getDef3()));// ��������
		data.put("buyTaxNo", aggvo.getParentVO().getDef4());// ������˰��ʶ��ţ�ҵ�����֤����ҵͳһ���ô��룩
		data.put("buyBankAccount", aggvo.getParentVO().getDef7() == null ? null
				: aggvo.getParentVO().getDef7() + aggvo.getParentVO().getDef8());// ���������У������˺�
		data.put("buyAddrTel", aggvo.getParentVO().getDef5() == null ? null
				: aggvo.getParentVO().getDef5() + aggvo.getParentVO().getDef6());// ������ַ�绰
		data.put("note", aggvo.getParentVO().getDef42());// ��ע
		data.put("sellName", queryOrgName(aggvo.getParentVO().getDef10()));// ��������
		data.put("sellTaxNo", aggvo.getParentVO().getDef11());// ����˰��
		data.put("sellAddrTel", aggvo.getParentVO().getDef12() == null ? null
				: aggvo.getParentVO().getDef12().substring(0, 3)
						+ aggvo.getParentVO().getDef13());// ������ַ�绰
		// data.put("sellTel", aggvo.getParentVO().getDef13());// �����绰
		data.put("goodsVersion", 33.0);//
		data.put("confirmWin", 0);//
		data.put("printType", 0);//
		data.put("sellBankAccount",
				aggvo.getParentVO().getDef14() == null ? null : aggvo
						.getParentVO().getDef14()
						+ aggvo.getParentVO().getDef15());// �����˺�
		data.put("cashier", aggvo.getParentVO().getDef44());// �տ��� ----------
		data.put("checker", aggvo.getParentVO().getDef45());// ������ ----------
		data.put("invoicer", aggvo.getParentVO().getDef46());// ��Ʊ�� ----------
		data.put("sourceinvCode", "");// Դ������Ʊ����
		data.put("sourceinvNo", "");// Դ������Ʊ����
		data.put("negativeFlag", 0);// ֵ��Ϊ��ֵ0���Ǻ�壩��1����壩��Ĭ��0
		data.put("negNativeNo", "");// ר�÷�Ʊ���Ǹ���Ʊʱ��Ҫ����
		// data.put("isExtraBill", "");// �Ƿ�Ʊ
		// data.put("serialNo", 0);// //��Ʊ����

		// Double totalTaxAmount = 0.0;
		// Double totalAmount = 0.0;
		// Double totalAmountWithoutTax = 0.0;

		InvoicingBody[] invoicingBody = (InvoicingBody[]) aggvo.getChildrenVO();
		List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
		for (InvoicingBody inv : invoicingBody) {
			HashMap<String, Object> detail = new HashMap<String, Object>();
			detail.put("dtsGuid", inv.getPk_invbody());
			detail.put("goodsName", inv.getDef6());// ��Ʒ����
			detail.put("standard", inv.getDef7());// ����ͺ�
			detail.put("unit", inv.getDef9());// ��λ
			detail.put("number", inv.getDef10());// ����
			detail.put(
					"taxPrice",
					new UFDouble(inv.getDef25()).div(
							new UFDouble(inv.getDef10())).toString());// ��˰���ۣ����12λС����
			detail.put("withoutTaxPrice",
					new UFDouble(inv.getDef16()).toString());// ����˰���ۣ����12λС����
			detail.put("amountWithTax",
					new UFDouble(inv.getDef25()).doubleValue());// ��˰��������λС����
			detail.put("amountWithoutTax",
					new UFDouble(inv.getDef23()).doubleValue());// ����˰��������λС����
			detail.put("rate",
					(new UFDouble(inv.getDef17()).doubleValue()) * 100);// ˰��
			detail.put("taxAmount", new UFDouble(inv.getDef24()).doubleValue());// ˰��
			detail.put("taxTypeCode", inv.getDef8());// ˰�շ������
			detail.put("discountFlag", 0);// �Ƿ������Żݣ�0�������ܣ�1�����ܣ�
			detail.put("discountType", "");// �Ż���������
			detail.put("discountNote", "");// �Ż�����˵��
			detail.put("discountFlage", 0);// �ۿ۱��
			detail.put("discountAmountWithoutTax", 0);// �ۿ۽��(����˰)
			detail.put("discountRate", 0);// �ۿ���
			detail.put("discountTaxAmount", 0);// �ۿ�˰��
			detail.put("taxZeroRate", "");// ��˰�ʱ�־

			details.add(detail);
		}

		data.put("totalTaxAmount",
				new UFDouble(aggvo.getParentVO().getDef28()).doubleValue());// �ϼ�˰�����ʱ������ϸ˰���ۼӣ�
		data.put("totalAmount",
				new UFDouble(aggvo.getParentVO().getDef29()).doubleValue());// ��˰�ϼƣ�����ʱ������ϸ��˰�ۼӣ�
		data.put("totalAmountWithoutTax", new UFDouble(aggvo.getParentVO()
				.getDef27()).doubleValue());//
		// ����˰�ܶ����ʱ������ϸ����˰����ۼӣ�

		data.put("details", details);// ��ϸ
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
				throw new BusinessException("������˰������ʧ��");
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
		outputTaxHVO.setMoney_fp(new UFDouble(object.getString("totalAmount")));// ��˰�ܶ�
		outputTaxHVO.setMoney_fpout(new UFDouble(object
				.getString("totalAmountWithoutTax")));// ����˰�ܶ�
		outputTaxHVO.setDef18(object.getString("totalTaxAmount"));// ˰��

		outputTaxHVO.setPk_org(aggvo.getParentVO().getPk_org());
		outputTaxHVO.setPk_group(aggvo.getParentVO().getPk_group());
		// ˰��״̬��ѯ
		outputTaxHVO.setFplx(object.getString("invType"));// 0����ֵ˰ר�÷�Ʊ������Ʊ����Ϊ0ʱ������������˰�ʷ�Ʊ��
		// 2����ֵ˰��ͨ��Ʊ 51����ֵ˰������ͨƱ
		// optjsonvo.setBillType(headvo.getPk_billtype());//��Ʊ����
		// optjsonvo.setSellBankAccount(headvo.getDef6()+headvo.getDef7());//���������˺�
		// optjsonvo.setSellAddrTel(headvo.getDef8()+headvo.getDef9());//������ַ�绰
		// ������Ϣ1
		outputTaxHVO.setPk_kh(aggvo.getParentVO().getDef3());// �ͻ�
		outputTaxHVO.setKhh(aggvo.getParentVO().getDef7());// ������
		outputTaxHVO.setYhzh(aggvo.getParentVO().getDef8());// �����˺�
		outputTaxHVO.setAdress(aggvo.getParentVO().getDef5());// ��ַ
		outputTaxHVO.setTelphone(aggvo.getParentVO().getDef6());// �����绰
		outputTaxHVO.setNsrsbh(aggvo.getParentVO().getDef4());// ������˰��ʶ��ţ�ҵ�����֤����ҵͳһ���ô��룩

		// ������Ϣ
		outputTaxHVO.setKpzz(aggvo.getParentVO().getDef10());// ��������
																// --����Ӧ���룬���ݱ�����NC�л�ȡ
		outputTaxHVO.setDef3(aggvo.getParentVO().getDef11());// ������˰��ʶ���
		outputTaxHVO.setDef6(aggvo.getParentVO().getDef14());// ����������
		outputTaxHVO.setDef7(aggvo.getParentVO().getDef15());// ���������˺�
		outputTaxHVO.setDef8(aggvo.getParentVO().getDef12());// ������ַ
		outputTaxHVO.setDef9(aggvo.getParentVO().getDef13());// �����绰����
		outputTaxHVO.setPk_jbr(aggvo.getParentVO().getDef39());// ������
		outputTaxHVO.setPk_jbbm(aggvo.getParentVO().getDef40());// ���첿��
		outputTaxHVO.setDbilldate(new UFDate(object.getString("invDate")));// ҵ������
		outputTaxHVO.setRmark(aggvo.getParentVO().getDef42());// ��ע
		outputTaxHVO.setSkr(aggvo.getParentVO().getDef44());// �տ���
		outputTaxHVO.setFkr(aggvo.getParentVO().getDef46());// ������
		// aggOutputTaxHVO.getParentVO().setAttributeValue("def12",
		// optjsonvo.getPrintType() + "");// ��ӡ����
		// aggOutputTaxHVO.getParentVO().setAttributeValue("def13",
		// optjsonvo.getConfirmWin() + "");// �Ƿ���ʾ��ӡ�Ի���
		// ��ϵͳ��Ϣ
		outputTaxHVO.setAttributeValue("def14", aggvo.getParentVO().getDef1());// ��ϵͳid
		outputTaxHVO.setAttributeValue("def15", aggvo.getParentVO().getDef2());// ��ϵͳ����
		outputTaxHVO.setAttributeValue("def16", "SRM");// ��ϵͳ����
		outputTaxHVO.setDef17(aggvo.getParentVO().getDef45());// ��Ʊ��
		// aggOutputTaxHVO.getParentVO().setAttributeValue("src_taxbillcode",
		// optjsonvo.getSourceInvCode());
		// aggOutputTaxHVO.getParentVO().setAttributeValue("src_taxbillno",
		// optjsonvo.getSourceInvNo());

		// aggOutputTaxHVO.setPk_billtype("HZ08");
		// aggOutputTaxHVO.setVbillstatus(-1);//����״̬

		InvoicingBody[] invoicingBodys = (InvoicingBody[]) aggvo
				.getChildrenVO();

		List<OutputTaxBVO> taxBVOs = new ArrayList<>();
		for (InvoicingBody bvo : invoicingBodys) {
			OutputTaxBVO detailvo = new OutputTaxBVO();
			detailvo.setPk_product(bvo.getDef6());// ����
			detailvo.setPk_dw(bvo.getDef9());// ��λ
			detailvo.setNum(new UFDouble(bvo.getDef10()));// ����
			detailvo.setPriceintax(new UFDouble(bvo.getDef19()));// ��˰����
			detailvo.setPriceouttax(new UFDouble(bvo.getDef16()));// ����˰����
			detailvo.setMoneyintax(new UFDouble(bvo.getDef25()));// ��˰��������λС����
			detailvo.setMoneyouttax(new UFDouble(bvo.getDef23()));// ����˰��������λС����
			detailvo.setTaxrate(new UFDouble(bvo.getDef17()));// ˰��
			detailvo.setPk_vatinfo(null/* getPk_vatInfoByCode(bvo.getDef8()) */);// ˰�շ������
			detailvo.setMoneytax(new UFDouble(bvo.getDef21()));// ˰��
			// detailvo.setDef1(String.valueOf(bvo.getDiscountFlag()));//�Ƿ������Ż�
			// detailvo.setDef2(bvo.getDiscountNote());//�Ż�����˵��
			// detailvo.setDef3(bvo.getDiscountType());//�Ż���������
			// detailvo.setDef4(bvo.getTaxZeroRate());//��˰�ʱ�־
			detailvo.setDef5(bvo.getDef7());// ����ͺ�
			taxBVOs.add(detailvo);
		}
		aggOutputTaxhVO.setParentVO(outputTaxHVO);
		aggOutputTaxhVO.setChildrenVO(taxBVOs.toArray(new OutputTaxBVO[0]));

		service.insert(new AggOutputTaxHVO[] { aggOutputTaxhVO }, null);

	}

	/**
	 * ���ݡ�˰�ձ��롿��ȡPK
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
	 * ���ݿ�־û�
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
