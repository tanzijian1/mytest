package nc.ui.tg.pub.listener;

import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.FuncletInitData;
import nc.itf.pubapp.pub.smart.IBillQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.uif2.UIState;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * �򿪽�������������Listener��
 * 
 * @author
 */
public class BillInitDataListener extends
		nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener {
	public void initData(FuncletInitData data) {
		// ���һ���ڵ��Ѿ��򿪲������ڱ༭����, Ĭ�ϲ��ٴ�����������
		if (UIState.EDIT.equals(this.getModel().getUiState())
				|| UIState.ADD.equals(this.getModel().getUiState())) {
			return;
		}
		if (null == data) {
			this.getModel().initModel(null);
			return;
		}
		if (ILinkType.LINK_TYPE_QUERY == data.getInitType()) {
			Object iLinkQueryData = data.getInitData();
			ILinkQueryData[] datas = null;
			if (iLinkQueryData.getClass().isArray()) {
				datas = (ILinkQueryData[]) iLinkQueryData;
			} else {
				datas = new ILinkQueryData[] { (ILinkQueryData) iLinkQueryData };
			}
			String[] pks = new String[datas.length];
			for (int i = 0; i < pks.length; i++)
				pks[i] = datas[i].getBillID();
			IBillQueryService servcie = NCLocator.getInstance().lookup(
					IBillQueryService.class);

			AbstractBill[] objs = null;
			try {
				objs = servcie.queryAbstractBillsByPks(
						Class.forName(getVoClassName()), pks);
			} catch (ClassNotFoundException e) {

			}
			if (objs == null) {
				MessageDialog
						.showErrorDlg(getModel().getContext().getEntranceUI(),
								null,
								nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
										.getStrByID("pubapp_0", "0pubapp-0337")/*
																				 * @
																				 * res
																				 * "û�в鿴�õ��ݵ�Ȩ��"
																				 */);
			}
			getModel().initModel(objs);
		} else {
			super.initData(data);
		}
	}
}
