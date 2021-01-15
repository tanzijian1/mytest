package nc.impl.pub.ace;

import nc.bs.tg.projectdata.ace.bp.AceProjectdataInsertBP;
import nc.bs.tg.projectdata.ace.bp.AceProjectdataUpdateBP;
import nc.bs.tg.projectdata.ace.bp.AceProjectdataDeleteBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.projectdata.AggProjectDataVO;

public abstract class AceProjectdataPubServiceImpl {
	// ����
	public AggProjectDataVO[] pubinsertBills(AggProjectDataVO[] vos)
			throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggProjectDataVO> transferTool = new BillTransferTool<AggProjectDataVO>(
					vos);
			AggProjectDataVO[] mergedVO = transferTool.getClientFullInfoBill();

			// ����BP
			AceProjectdataInsertBP action = new AceProjectdataInsertBP();
			AggProjectDataVO[] retvos = action.insert(mergedVO);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggProjectDataVO[] vos) throws BusinessException {
		try {
			// ���� �Ƚ�ts
			BillTransferTool<AggProjectDataVO> transferTool = new BillTransferTool<AggProjectDataVO>(
					vos);
			AggProjectDataVO[] fullBills = transferTool.getClientFullInfoBill();
			AceProjectdataDeleteBP deleteBP = new AceProjectdataDeleteBP();
			deleteBP.delete(fullBills);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggProjectDataVO[] pubupdateBills(AggProjectDataVO[] vos)
			throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggProjectDataVO> transTool = new BillTransferTool<AggProjectDataVO>(
					vos);
			// ��ȫǰ̨VO
			AggProjectDataVO[] fullBills = transTool.getClientFullInfoBill();
			// ����޸�ǰvo
			AggProjectDataVO[] originBills = transTool.getOriginBills();
			// ����BP
			AceProjectdataUpdateBP bp = new AceProjectdataUpdateBP();
			AggProjectDataVO[] retBills = bp.update(fullBills, originBills);
			// ���췵������
			retBills = transTool.getBillForToClient(retBills);
			return retBills;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggProjectDataVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggProjectDataVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggProjectDataVO> query = new BillLazyQuery<AggProjectDataVO>(
					AggProjectDataVO.class);
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