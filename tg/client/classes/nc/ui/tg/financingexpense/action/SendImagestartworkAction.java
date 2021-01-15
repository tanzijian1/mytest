package nc.ui.tg.financingexpense.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.image.IGuoXinImage;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.cmp.util.StringUtils;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;

public class SendImagestartworkAction extends NCAction {
	public SendImagestartworkAction() {
		setBtnName("推影像待办");
		setCode("sendImagestartworkAction");
	}

	private BillManageModel model = null;

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
		AggFinancexpenseVO vo = (AggFinancexpenseVO) obj;
		ISqlThread push = NCLocator.getInstance().lookup(ISqlThread.class);
		push.pushImageAction(vo);
		IMDPersistenceQueryService md = NCLocator.getInstance().lookup(
				IMDPersistenceQueryService.class);
		NCObject nobj = md.queryBillOfNCObjectByPK(AggFinancexpenseVO.class, vo
				.getParentVO().getPk_finexpense());
		if (nobj != null) {
			getModel().update(nobj.getContainmentObject());
		}
	}

}
