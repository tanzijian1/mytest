package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

public class BaseDiscontinuationAction extends BaseShareAction {
	public BaseDiscontinuationAction() {
		super();
		setCode("Discontinuation");
		setBtnName("Õ£”√");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object value = getModel().getSelectedData();
		try {
			SuperVO vo = (SuperVO) value;
			vo.setAttributeValue("enablestate", 3);
			vo.setStatus(VOStatus.UPDATED);
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
		if ("2".equals(String.valueOf(value))) {
			return true;
		}

		return enable;
	}
}
