package nc.ui.tg.changebill.action;

/**
 * 联查销项发票按钮
 * @author Huangxj
 */
import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.sm.funcreg.FuncRegisterVO;

public class SalesInvoiceAction extends NCAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7110064408625514231L;

	public SalesInvoiceAction() {
		setBtnName("联查销项发票");
		setCode("SalesInvoiceAction");
	}

	private BillManageModel model;

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		Object obj = getModel().getSelectedData();
		if (obj == null)
			throw new BusinessException("未选中数据");
		AggregatedValueObject aggvo = (AggregatedValueObject) obj;

		link(aggvo);

	}

	/**
	 * 
	 * @param aggvo
	 * @author hxj
	 * 
	 */
	private void link(AggregatedValueObject aggvo) throws Exception {
		String def2 = (String) aggvo.getParentVO().getAttributeValue(
				"def2");
		if ("".equals(def2) || def2 == null) {
			throw new BusinessException("该张单据的发票号为空，不能联查");
		}

		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String pk = (String) query.executeQuery(
				"select pk_outputtax from hzvat_outputtax_h where nvl(dr,0)=0 and fph ='"
						+ def2 + "'", new ColumnProcessor());
		if("".equals(pk)||pk==null){
			throw new BusinessException("该张单据无对应销项发票，不能联查");
		}
		DefaultLinkData userdata = new DefaultLinkData();
		userdata.setBillIDs(new String[] { pk });
		FuncletInitData initdata = new FuncletInitData();
		initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
		initdata.setInitData(userdata);
		// 设置被联查单据的交易类型
		BilltypeVO billType = PfDataCache.getBillType("HZ08");
		FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
				.getFuncRegisterVO(billType.getNodecode());
		FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
				.getEntranceUI(), registerVO, initdata, null, true, true);
	}

}
