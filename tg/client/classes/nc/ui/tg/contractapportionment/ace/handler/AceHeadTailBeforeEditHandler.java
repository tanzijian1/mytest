package nc.ui.tg.contractapportionment.ace.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.ui.arap.viewhandler.AbstractBillHandler;
import nc.ui.bd.ref.model.PsndocDefaultNCRefModel;
import nc.ui.bd.ref.model.PsndocDefaultRefModel;
import nc.ui.fipub.ref.FiRefModelUtil;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.vo.arap.basebill.BaseBillVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFLiteralDate;

public class AceHeadTailBeforeEditHandler extends AbstractBillHandler<CardHeadTailBeforeEditEvent>{
	public static final List<String> escapes = Arrays.asList(new String[] {
			IBillFieldGet.PU_PSNDOC, IBillFieldGet.SO_PSNDOC,"conflwperson",
			IBillFieldGet.PK_DEPTID,"conflwdept",
			IBillFieldGet.SO_DEPTID,IBillFieldGet.SO_DEPTID_V,IBillFieldGet.PU_DEPTID,IBillFieldGet.PU_DEPTID_V});
	
	@Override
	protected Collection<String> getFilterKey() {
		// TODO 自动生成的方法存根
		return escapes;
	}

	@Override
	protected void handle() {
		String key =  getKey();
		List<String> busiorgFields = BaseBillVO.BUSIORG_FIELD_LIST;
		BillCardPanel bcp = getBillCardPanel();
		BillItem billItem = bcp.getHeadItem(key);
		BillItem billDateItem = bcp.getHeadItem(IBillFieldGet.BILLDATE);
		BillItem isinitItem = bcp.getHeadItem("pk_org");
		boolean isinit = false;
		
		
		if("conflwperson".equals(key)){
			//设置业务员参照过滤委托核算组织
			UIRefPane refpane = (UIRefPane)billItem.getComponent();
			FiRefModelUtil.setFiRelation(refpane);
			refpane.getRefModel().setPk_org((String)getHeadValue(IBillFieldGet.PK_ORG));
			FiRefModelUtil.setFilter(refpane);
			//设置人员参照面板中离职人员checkBox组件不显示，并且给参照设置当前单据日期
			filterLeavePowerShowAndUI(billItem,billDateItem,isinit,false);
		}else if(IBillFieldGet.PK_DEPTID.equals(key) 
			||"conflwdept".equals(key) ){
			UIRefPane refpane = (UIRefPane)billItem.getComponent();
			FiRefModelUtil.setFiRelation(refpane);
			refpane.getRefModel().setPk_org((String)getHeadValue(IBillFieldGet.PK_ORG));
			FiRefModelUtil.setFilter(refpane);
		}
		
		
	}	
	
	public void filterLeavePowerShowAndUI(BillItem billItem , BillItem billDateItem,boolean isinit, boolean showLeavePowerAndUI){
		//期初要显示
		if(isinit){
			return;
		}
		if (billItem != null && billItem.getComponent() instanceof UIRefPane && ((UIRefPane) billItem.getComponent()).getRefModel() != null) {
			UIRefPane refPane = (UIRefPane) billItem.getComponent();
			if (refPane.getRefModel() instanceof PsndocDefaultNCRefModel) {
				PsndocDefaultRefModel psndocDefaultRefModel = (PsndocDefaultRefModel) refPane.getRefModel();
				psndocDefaultRefModel.setLeavePowerUI(showLeavePowerAndUI);
				psndocDefaultRefModel.setLeavePower(showLeavePowerAndUI);
				
				if (!(showLeavePowerAndUI)) {
					psndocDefaultRefModel.setUiControlComponentClassName(null);
				}
				if(billDateItem != null  ){
					if(billDateItem.getValueObject() !=  null && billDateItem.getValueObject() instanceof UFDate){
						UFDate billDate = (UFDate)billDateItem.getValueObject(); 
						UFLiteralDate nowDate = new UFLiteralDate(billDate.toDate());
						//设置业务日期过滤在此日期范围内在职的人员。
						psndocDefaultRefModel.setNowDate(nowDate);
					}
				}
				psndocDefaultRefModel.reset();
			}
		}
	}


}
