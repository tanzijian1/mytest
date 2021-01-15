package nc.ui.tg.renamechangebill.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillListView;
import nc.ui.tgfp.pub.BillLinkQueryVoucherUtils;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tmpub.util.ArrayUtil;

public class LinkQryVoucherAction extends NCAction{
	
	private static final long serialVersionUID = 1L;
	
	private BillManageModel model = null;
	private ShowUpableBillListView listview;
	private nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor;

	public LinkQryVoucherAction() {
		super.setCode("LinkQryVoucher");
		super.setBtnName("联查凭证");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		AggregatedValueObject[] selectvos = getSelectedAggVOs();
		if (ArrayUtil.isNull(selectvos)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("3607mng_0", "03607mng-0002")/*
																			 * @res
																			 * "当前没有选中单据！"
																			 */);
		}

		if (selectvos.length > 1) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("3607mng_0", "03607mng-0003")/*
																			 * @res
																			 * "请选择一条单据进行操作！"
																			 */);
		}

		AggregatedValueObject aggvo = selectvos[0];
		String billtype = String.valueOf(aggvo.getParentVO().getAttributeValue("billtype"));

		String pk_group = (String) aggvo.getParentVO().getAttributeValue(
				"pk_group");
		String pk_org = (String) aggvo.getParentVO()
				.getAttributeValue("pk_org");
		String primarykey = (String) aggvo.getParentVO().getPrimaryKey();
		BillLinkQueryVoucherUtils.getUtils().onLinkQryVoucher(
				getModel().getContext().getEntranceUI(), billtype, pk_group,
				pk_org, primarykey);

	}

	/*
	 * 获取选择VOs的聚合aggvos
	 * 
	 * @return
	 */
	private AggregatedValueObject[] getSelectedAggVOs() {
		Object[] value = null;
		// 判断显示什么界面
		if (getListview().isShowing()) {
			value = (getListview()).getModel().getSelectedOperaDatas();
		} else if (getEditor().isShowing()) {
			value = new Object[1];
			value[0] = ((BillForm) editor).getModel().getSelectedData();
		}

		if (null == value || value.length == 0) {
			return null;
		}
		AggregatedValueObject[] aggs = new AggregatedValueObject[value.length];
		System.arraycopy(value, 0, aggs, 0, aggs.length);
		return aggs;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}

	public ShowUpableBillListView getListview() {
		return listview;
	}

	public void setListview(ShowUpableBillListView listview) {
		this.listview = listview;
	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor) {
		this.editor = editor;
	}

}
