package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.md.persist.framework.IMDPersistenceService;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

public class BaseEnablestateAction extends BaseShareAction {
	public BaseEnablestateAction() {
		super();
		setCode("Enablestate");
		setBtnName("∆Ù”√");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object value = getModel().getSelectedData();
		try {
			SuperVO vo = (SuperVO) value;
			vo.setAttributeValue("enablestate", 2);
			vo.setStatus(VOStatus.UPDATED);
			NCLocator
					.getInstance()
					.lookup(IMDPersistenceService.class)
					.updateBillWithAttrs(new Object[] { vo },
							new String[] { "enablestate" });
			getModel().updateLine(getModel().getSelectedIndex(), vo);
		} catch (Exception e1) {
			throw new BusinessException(e1.getMessage());
		}

	}

	protected boolean isActionEnable() {
		if (!super.isActionEnable()) {
			return super.isActionEnable();
		}
		boolean enable = false;
		Object value = ((SuperVO) getModel().getSelectedData())
				.getAttributeValue("enablestate");
		if (value == null || "3".equals(String.valueOf(value))) {
			return true;
		}

		return enable;
	}
}
