package nc.impl.pub.ace;

import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardInsertBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardUpdateBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardDeleteBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardSendApproveBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardUnSendApproveBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardApproveBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceFischemepushstandardPubServiceImpl {
	// ����
	public AggFischemePushStandardHVO[] pubinsertBills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggFischemePushStandardHVO> transferTool = new BillTransferTool<AggFischemePushStandardHVO>(
					clientFullVOs);
			// ����BP
			AceFischemepushstandardInsertBP action = new AceFischemepushstandardInsertBP();
			AggFischemePushStandardHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceFischemepushstandardDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggFischemePushStandardHVO[] pubupdateBills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggFischemePushStandardHVO> transferTool = new BillTransferTool<AggFischemePushStandardHVO>(
					clientFullVOs);
			AceFischemepushstandardUpdateBP bp = new AceFischemepushstandardUpdateBP();
			AggFischemePushStandardHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFischemePushStandardHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggFischemePushStandardHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFischemePushStandardHVO> query = new BillLazyQuery<AggFischemePushStandardHVO>(
					AggFischemePushStandardHVO.class);
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

	// �ύ
	public AggFischemePushStandardHVO[] pubsendapprovebills(
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills)
			throws BusinessException {
		AceFischemepushstandardSendApproveBP bp = new AceFischemepushstandardSendApproveBP();
		AggFischemePushStandardHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggFischemePushStandardHVO[] pubunsendapprovebills(
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills)
			throws BusinessException {
		AceFischemepushstandardUnSendApproveBP bp = new AceFischemepushstandardUnSendApproveBP();
		AggFischemePushStandardHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggFischemePushStandardHVO[] pubapprovebills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFischemepushstandardApproveBP bp = new AceFischemepushstandardApproveBP();
		AggFischemePushStandardHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggFischemePushStandardHVO[] pubunapprovebills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFischemepushstandardUnApproveBP bp = new AceFischemepushstandardUnApproveBP();
		AggFischemePushStandardHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}