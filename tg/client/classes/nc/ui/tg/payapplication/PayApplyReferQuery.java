package nc.ui.tg.payapplication;


import java.awt.Container;

import nc.ui.arap.bill.ArapBillUIUtil;
import nc.ui.arap.query.ArapBillQueryConditionDLGInitializer;
import nc.ui.arap.query.ArapDefQCVOProcessor;
import nc.ui.pub.pf.BillSourceVar;
import nc.ui.pubapp.billref.src.DefaultBillReferQuery;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.totalvo.MarAssistantDealer;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.querytemplate.TemplateInfo;


public class PayApplyReferQuery extends DefaultBillReferQuery {

	public PayApplyReferQuery(Container c, TemplateInfo info) {
		super(c, info);
	}

	@Override
	public void initQueryConditionDLG(QueryConditionDLGDelegator dlgDelegator) {
		this.setDefaultPk_org(dlgDelegator);
		this.initBodyRedundancyItem(dlgDelegator);

		// 主组织权限
		dlgDelegator.registerNeedPermissionOrgFieldCodes(new String[] { "pk_org" });
		dlgDelegator.setPowerEnable(true);
	}

	private void initBodyRedundancyItem(QueryConditionDLGDelegator dlgDelegator) {
		
	}

	
	private void setDefaultPk_org(QueryConditionDLGDelegator dlgDelegator) {
	}
}
