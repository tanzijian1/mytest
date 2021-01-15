package nc.impl.pub.ace;

import nc.bs.tg.standard.ace.bp.AceStandardDeleteBP;
import nc.bs.tg.standard.ace.bp.AceStandardInsertBP;
import nc.bs.tg.standard.ace.bp.AceStandardUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.standard.AggStandardVO;

public abstract class AceStandardPubServiceImpl {
	// ����
	public AggStandardVO[] pubinsertBills(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggStandardVO> transferTool = new BillTransferTool<AggStandardVO>(
					clientFullVOs);
			// ����BP
			AceStandardInsertBP action = new AceStandardInsertBP();
			AggStandardVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceStandardDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggStandardVO[] pubupdateBills(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggStandardVO> transferTool = new BillTransferTool<AggStandardVO>(
					clientFullVOs);
			AceStandardUpdateBP bp = new AceStandardUpdateBP();
			AggStandardVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggStandardVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggStandardVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggStandardVO> query = new BillLazyQuery<AggStandardVO>(
					AggStandardVO.class);
			bills = query.query(queryScheme, null);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return bills;
	}

	/**
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

}