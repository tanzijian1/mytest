package nc.ui.tg.taxcalculation.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITaxCalculationMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.gl.voucherlist.ListUi;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.model.IAppModelEx;
import nc.ui.pubapp.uif2app.query2.model.ModelDataRefresher;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.vo.tgfn.taxcalculation.TaxCalculationBody;
import nc.vo.tgfn.taxcalculation.TaxCalculationHead;

public class RedAction extends NCAction{
	private BillManageModel model;
    private ShowUpableBillForm billform;
	public ShowUpableBillForm getBillform() {
		return billform;
	}
	public void setBillform(ShowUpableBillForm billform) {
		this.billform = billform;
	}
	public BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
		this.model = model;
	}
	public RedAction(){
    	setBtnName("红冲");
    	setCode("redAction");
    } 
	private String pk_taxcalhead;
	public String getPk_taxcalhead() {
		return pk_taxcalhead;
	}
	public void setPk_taxcalhead(String pk_taxcalhead) {
		this.pk_taxcalhead = pk_taxcalhead;
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		int i=MessageDialog.showOkCancelDlg((Container) e.getSource(), "询问", "是否继续红冲");
         if(i!=1){
        	 return;
         }
         IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object obj=getModel().getSelectedData();
		if(obj==null)throw new BusinessException("未选中数据");
		AggTaxCalculationHead aggvo=(AggTaxCalculationHead)obj;
		TaxCalculationHead hvo=aggvo.getParentVO();
         if(hvo.getApprovestatus()!=1){
        	 throw new BusinessException("单据状态必须为审批通过");
         }
				 CircularlyAccessibleValueObject[] bvos=aggvo.getChildrenVO();
		  if(bvos!=null){
			  String taxfee=(String)bvos[0].getAttributeValue("def14");//应交税费
			  if(taxfee!=null){
				  if(Double.valueOf(taxfee)<0){
					  throw new BusinessException("金额必须为正数");
				  }
			  }
		  }
		  String rbillno=(String)bs.executeQuery("select billno from tgfn_taxcalhead where def1='"+hvo.getBillno()+"'", new ColumnProcessor());
		  if(rbillno!=null&&rbillno!=""){
				throw new BusinessException("该单已进行红冲");
			}
		  IMDPersistenceQueryService query=NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		  NCObject  nobj= query.queryBillOfNCObjectByPK(AggTaxCalculationHead.class, hvo.getPk_taxcalhead());
		if(nobj!=null){
			AggTaxCalculationHead naggvo=(AggTaxCalculationHead)nobj.getContainmentObject();
			TaxCalculationHead nhvo=naggvo.getParentVO();
			nhvo.setPk_taxcalhead(null);
			nhvo.setBillno(null);
			nhvo.setApprovestatus(-1);
			nhvo.setApprover(null);
			nhvo.setBillstatus(null);
//			nhvo.setApprovedate(null);
//			nhvo.setApprovenote(null);
			nhvo.setBilldate(null);
			nhvo.setDef1(hvo.getBillno());
			CircularlyAccessibleValueObject[] nbvos=naggvo.getAllChildrenVO();
			for(CircularlyAccessibleValueObject cv:nbvos){
				TaxCalculationBody bvo=(TaxCalculationBody)cv;
				bvo.setPk_taxcalbody(null);
				if(bvo.getDef14()!=null&&bvo.getDef14()!=""){
					if(Double.valueOf(bvo.getDef14())>0){
					Double d=-Double.valueOf(bvo.getDef14());
					bvo.setDef14(d.toString());
					}
				}
			}
//			getModel().setAppUiState(AppUiState.COPY_ADD);
//			getModel().add(naggvo);
			if (this.model instanceof IAppModelEx) {
				this.model.setOtherUiState(UIState.COPY_ADD);
				((IAppModelEx) this.model).setAppUiState(AppUiState.COPY_ADD);
			} else {
				this.model.setUiState(UIState.ADD);
			}
			this.billform.setValue(naggvo);
			this.billform.setBodyStatusNew();
//			getBillform().getBillCardPanel().setBillValueVO(naggvo);
//			AggTaxCalculationHead o=(AggTaxCalculationHead)getModel().getSelectedData();
//             o.setParentVO(naggvo.getParentVO());
//             o.setChildrenVO(naggvo.getAllChildrenVO());
			//			model.setUiState(UIState.ADD);
//			Object ooo=model.getSelectedData();
//			ITaxCalculationMaintain mt=  NCLocator.getInstance().lookup(ITaxCalculationMaintain.class);
//		     mt.insert(new AggTaxCalculationHead[]{naggvo}, new AggTaxCalculationHead[]{naggvo});
		}
				 
		
		}
 
}
