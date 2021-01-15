package nc.ui.tg.standard.ace.interceptor;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.standard.StandardVO;

public class StandardInterceptor implements
		nc.ui.uif2.actions.ActionInterceptor {
	nc.ui.pubapp.uif2app.view.ShowUpableBillForm showUpComponent;

	@Override
	public boolean beforeDoAction(Action action, ActionEvent event) {
		try {
			UIRefPane refpane = (UIRefPane) showUpComponent.getBillCardPanel()
					.getHeadItem(StandardVO.PK_FINTYPE).getComponent();
			String pk = (String) showUpComponent.getBillCardPanel()
					.getHeadItem(StandardVO.PK_STANDARD).getValueObject();
			String pk_fintype = refpane.getRefPK();
			String fintype = (String) refpane.getRefModel().getRefNameValue();
			UFBoolean def1 = UFBoolean.valueOf(getShowUpComponent().getBillCardPanel().getHeadItem(StandardVO.DEF1).getValueObject().toString());
			UFBoolean def2 = UFBoolean.valueOf(getShowUpComponent().getBillCardPanel().getHeadItem(StandardVO.DEF2).getValueObject().toString());
			UFBoolean def3 = UFBoolean.valueOf(getShowUpComponent().getBillCardPanel().getHeadItem(StandardVO.DEF3).getValueObject().toString());
			UFBoolean def4 = UFBoolean.valueOf(getShowUpComponent().getBillCardPanel().getHeadItem(StandardVO.DEF4).getValueObject().toString());
			if ("收并购融资".equals(fintype)) {
				int cardnum = 0;
				if ("Y".equals(def1.toString())) {
					cardnum += 1;
				}
				if ("Y".equals(def2.toString())) {
					cardnum += 1;
				}
				if ("Y".equals(def3.toString())) {
					cardnum += 1;
				}
				if ("Y".equals(def4.toString())) {
					cardnum += 1;
				}
				if (cardnum < 3) {
					throw new BusinessException("融资类型为【" + fintype
							+ "】至少需要勾选三证!");
				}
			}

			String periodyear = ((Integer) showUpComponent.getBillCardPanel()
					.getHeadItem(StandardVO.PERIODYEAR).getValueObject())
					.toString();
			IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String sql = "select count(1) from tgrz_standard where dr = 0 and pk_fintype = '"
					+ pk_fintype
					+ "' and periodyear = '"
					+ periodyear
					+ "' and pk_standard<>'" + pk + "'";
			Object obj = bs.executeQuery(sql, new ColumnProcessor());
			if (!"0".equals(String.valueOf(obj))) {
				throw new BusinessException("" + periodyear + "年,融资类型为【"
						+ fintype + "】的融资标准已存在,请勿重复录入!");
			}
		} catch (BusinessException e) {
			ShowStatusBarMsgUtil.showDataPermissionDlg(showUpComponent, "提示",
					e.getMessage());
			return false;
		}

		return true;
	}

	@Override
	public void afterDoActionSuccessed(Action action, ActionEvent e) {

	}

	@Override
	public boolean afterDoActionFailed(Action action, ActionEvent e,
			Throwable ex) {
		return true;
	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getShowUpComponent() {
		return showUpComponent;
	}

	public void setShowUpComponent(
			nc.ui.pubapp.uif2app.view.ShowUpableBillForm showUpComponent) {
		this.showUpComponent = showUpComponent;
	}

}
