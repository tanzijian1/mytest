package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.md.persist.framework.IMDPersistenceService;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class DiscontinuationAction extends BillShareAction {
	public DiscontinuationAction() {
		super();
		setCode("Discontinuation");
		setBtnName("Õ£”√");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object value = getModel().getSelectedData();
		try {
			AbstractBill aggVO = (AbstractBill) value;
			aggVO.getParentVO().setAttributeValue("enablestate", 3);
			aggVO.getParentVO().setStatus(VOStatus.UPDATED);
			NCLocator
					.getInstance()
					.lookup(IMDPersistenceService.class)
					.updateBillWithAttrs(new Object[] { value },
							new String[] { "enablestate" });
			getModel().update(new Object[] { value });
		} catch (Exception e1) {
			throw new BusinessException(e1.getMessage());
		}

	}

	protected boolean isActionEnable() {
		if (!super.isActionEnable()) {
			return super.isActionEnable();
		}
		boolean enable = false;
		Object value = ((AbstractBill) getModel().getSelectedData())
				.getParentVO().getAttributeValue("enablestate");
		if ("2".equals(String.valueOf(value))) {
			return true;
		}

		return enable;
	}
}
