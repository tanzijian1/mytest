package nc.ui.tg.paymentrequest.action;

/**
 * ���鸶���ͬ��ť
 * @author Huangxj
 */
import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.sm.funcreg.FuncRegisterVO;

public class LinkPaybillContract extends NCAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7110064408625514231L;

	public LinkPaybillContract() {
		setBtnName("���鸶���ͬ");
		setCode("LinkcontractAction");
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
		// TODO �Զ����ɵķ������
		Object obj = getModel().getSelectedData();
		if (obj == null)
			throw new BusinessException("δѡ������");
		AggregatedValueObject aggvo = (AggregatedValueObject) obj;

		linkSRM(aggvo);

	}

	/**
	 * ����Ӧ��������SRM�ɹ���ͬ
	 * 
	 * @param aggvo
	 * @author hxj
	 * 
	 */
	private void linkSRM(AggregatedValueObject aggvo) throws Exception {
		String vbillcode = (String) aggvo.getParentVO().getAttributeValue(
				"def5");
		// ��Ӳ�����֯ά��2020-04-17-̸�ӽ�
		String pk_org = (String) aggvo.getParentVO()
				.getAttributeValue("pk_org");
		if ("".equals(vbillcode) || vbillcode == null) {
			throw new BusinessException("���ŵ��ݵ������Ϊ�գ���������");
		}

		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String pk = (String) query.executeQuery(
				"select pk_fct_ap from fct_ap where nvl(dr,0)=0 and vbillcode ='"
						+ vbillcode + "' and pk_org = '" + pk_org + "'",
				new ColumnProcessor());
		DefaultLinkData userdata = new DefaultLinkData();
		userdata.setBillIDs(new String[] { pk });
		FuncletInitData initdata = new FuncletInitData();
		initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
		initdata.setInitData(userdata);
		// ���ñ����鵥�ݵĽ�������
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue(
				"pk_tradetype");
		if (pk_tradetype == null) {
			pk_tradetype = (String) aggvo.getParentVO().getAttributeValue(
					"transtype");
		}
		String tradetype = null;
		if ("FN01-Cxx-003".equals(pk_tradetype)) {// �������뵥-��Ӧ��
			tradetype = "FCT1-Cxx-002";// SRM��Ӧ�������ͬ
		} else if ("FN01-Cxx-001".equals(pk_tradetype)) {// �������뵥��ͨ�ú�ͬ
			tradetype = "FCT1-Cxx-003";// EBSͨ��֧����ͬ
		} else if ("FN01-Cxx-002".equals(pk_tradetype)) {// �������뵥-�ɱ��Զ�����
			tradetype = "FCT1-Cxx-001";// EBS�ɱ������ͬ
		} else {
			tradetype = "FCT1-01";
		}
		BilltypeVO billType = PfDataCache.getBillType(tradetype);
		FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
				.getFuncRegisterVO(billType.getNodecode());
		FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
				.getEntranceUI(), registerVO, initdata, null, true, true);
	}

}
