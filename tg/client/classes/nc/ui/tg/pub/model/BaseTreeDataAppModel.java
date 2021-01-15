package nc.ui.tg.pub.model;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uif2.BusinessExceptionAdapter;
import nc.itf.org.IOrgConst;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.vo.BillCodeContext;
import nc.ui.tg.datatype.ace.serviceproxy.AceDataTypeMaintainProxy;
import nc.ui.tg.fintype.ace.serviceproxy.AceFinTypeMaintainProxy;
import nc.ui.tg.organizationtype.ace.serviceproxy.AceOrganizationTypeMaintainProxy;
import nc.vo.pub.BusinessException;

public class BaseTreeDataAppModel extends
		nc.ui.pubapp.uif2app.model.HierachicalDataAppModel {

	private String newBillCode;// 新增数据所产生的单据号
	private BillCodeContext billCodeContext;
	private IBillcodeManage billCodeServicer;
	private String coderule;

	public BaseTreeDataAppModel() {
		super();
	}

	public String getNewDateBillCode() {
		return newBillCode;
	}

	public void setNewDateBillCode(String newDateBillCode) {
		this.newBillCode = newDateBillCode;
	}

	public void initBillCodeContext() {
		try {
			billCodeContext = getBillCodeServicer().getBillCodeContext(
					coderule, IOrgConst.GLOBEORG, IOrgConst.GLOBEORG);
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			throw new BusinessExceptionAdapter(e);
		}
	}

	public void generateNewBillCode() {
		try {
			newBillCode = getBillCodeServicer().getPreBillCode_RequiresNew(
					coderule, getContext().getPk_group(),
					getContext().getPk_org());
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			throw new BusinessExceptionAdapter(e);
		}
	}

	public void rollbackBillCode() {
		try {
			if (newBillCode != null) {
				getBillCodeServicer().rollbackPreBillCode(coderule,
						getContext().getPk_group(), getContext().getPk_org(),
						newBillCode);
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
			throw new BusinessExceptionAdapter(e);
		}
	}

	public BillCodeContext getBillCodeContext() {
		return billCodeContext;
	}

	public void setBillCodeContext(BillCodeContext billCodeContext) {
		this.billCodeContext = billCodeContext;
	}

	private IBillcodeManage getBillCodeServicer() {
		if (billCodeServicer == null)
			billCodeServicer = NCLocator.getInstance().lookup(
					IBillcodeManage.class);
		return billCodeServicer;
	}

	public String getCoderule() {
		return coderule;
	}

	public void setCoderule(String coderule) {
		this.coderule = coderule;
	}

	public Object enableTreeVO(Object vo) throws BusinessException {
		if (getService() instanceof AceOrganizationTypeMaintainProxy) {
			return ((AceOrganizationTypeMaintainProxy) getService())
					.enableTreeVO(vo);
		} else if (getService() instanceof AceFinTypeMaintainProxy) {
			return ((AceFinTypeMaintainProxy) getService()).enableTreeVO(vo);
		} else if (getService() instanceof AceDataTypeMaintainProxy) {
			return ((AceDataTypeMaintainProxy) getService()).enableTreeVO(vo);
		}

		return null;
	}
}
