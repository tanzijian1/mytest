package nc.impl.pub.ace;

import nc.bs.tg.capitalmarketrepay.ace.bp.AceCapitalmarketrepayInsertBP;
import nc.bs.tg.capitalmarketrepay.ace.bp.AceCapitalmarketrepayUpdateBP;
import nc.bs.tg.capitalmarketrepay.ace.bp.AceCapitalmarketrepayDeleteBP;
import nc.bs.tg.capitalmarketrepay.ace.bp.AceCapitalmarketrepaySendApproveBP;
import nc.bs.tg.capitalmarketrepay.ace.bp.AceCapitalmarketrepayUnSendApproveBP;
import nc.bs.tg.capitalmarketrepay.ace.bp.AceCapitalmarketrepayApproveBP;
import nc.bs.tg.capitalmarketrepay.ace.bp.AceCapitalmarketrepayUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pub.smart.MakeTimeSetter;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepaleyBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceCapitalmarketrepayPubServiceImpl {
	// 新增
	public AggMarketRepalayVO[] pubinsertBills(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggMarketRepalayVO> transferTool = new BillTransferTool<AggMarketRepalayVO>(
					clientFullVOs);
			// 调用BP
			AceCapitalmarketrepayInsertBP action = new AceCapitalmarketrepayInsertBP();
			AggMarketRepalayVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceCapitalmarketrepayDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggMarketRepalayVO[] pubupdateBills(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggMarketRepalayVO> transferTool = new BillTransferTool<AggMarketRepalayVO>(
					clientFullVOs);
			AceCapitalmarketrepayUpdateBP bp = new AceCapitalmarketrepayUpdateBP();
			AggMarketRepalayVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggMarketRepalayVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggMarketRepalayVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggMarketRepalayVO> query = new BillLazyQuery<AggMarketRepalayVO>(
					AggMarketRepalayVO.class);
			query.setOrderAttribute(MarketRepaleyBVO.class,new String[]{"def2","def3"});
			bills = query.query(queryScheme, null);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return bills;
	}

	/**
	 * 由子类实现，查询之前对queryScheme进行加工，加入自己的逻辑
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// 查询之前对queryScheme进行加工，加入自己的逻辑
	}

	// 提交
	public AggMarketRepalayVO[] pubsendapprovebills(
			AggMarketRepalayVO[] clientFullVOs, AggMarketRepalayVO[] originBills)
			throws BusinessException {
		AceCapitalmarketrepaySendApproveBP bp = new AceCapitalmarketrepaySendApproveBP();
		AggMarketRepalayVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggMarketRepalayVO[] pubunsendapprovebills(
			AggMarketRepalayVO[] clientFullVOs, AggMarketRepalayVO[] originBills)
			throws BusinessException {
		AceCapitalmarketrepayUnSendApproveBP bp = new AceCapitalmarketrepayUnSendApproveBP();
		AggMarketRepalayVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggMarketRepalayVO[] pubapprovebills(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCapitalmarketrepayApproveBP bp = new AceCapitalmarketrepayApproveBP();
		AggMarketRepalayVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggMarketRepalayVO[] pubunapprovebills(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCapitalmarketrepayUnApproveBP bp = new AceCapitalmarketrepayUnApproveBP();
		AggMarketRepalayVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}