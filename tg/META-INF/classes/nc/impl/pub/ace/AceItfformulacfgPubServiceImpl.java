package nc.impl.pub.ace;

import nc.bs.baseapp.itfformulacfg.ace.bp.AceItfformulacfgInsertBP;
import nc.bs.baseapp.itfformulacfg.ace.bp.AceItfformulacfgUpdateBP;
import nc.bs.baseapp.itfformulacfg.ace.bp.AceItfformulacfgDeleteBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;

/**
 * @Description:
 * @version with NC V6.5
 */
public abstract class AceItfformulacfgPubServiceImpl {
	// ����
	public AggFormulaCfgVO[] pubinsertBills(AggFormulaCfgVO[] vos) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggFormulaCfgVO> transferTool = new BillTransferTool<AggFormulaCfgVO>(vos);
			AggFormulaCfgVO[] mergedVO = transferTool.getClientFullInfoBill();

			// ����BP
			AceItfformulacfgInsertBP action = new AceItfformulacfgInsertBP();
			AggFormulaCfgVO[] retvos = action.insert(mergedVO);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggFormulaCfgVO[] vos) throws BusinessException {
		try {
			// ���� �Ƚ�ts
			BillTransferTool<AggFormulaCfgVO> transferTool = new BillTransferTool<AggFormulaCfgVO>(vos);
			AggFormulaCfgVO[] fullBills = transferTool.getClientFullInfoBill();
			AceItfformulacfgDeleteBP deleteBP = new AceItfformulacfgDeleteBP();
			deleteBP.delete(fullBills);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggFormulaCfgVO[] pubupdateBills(AggFormulaCfgVO[] vos) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggFormulaCfgVO> transTool = new BillTransferTool<AggFormulaCfgVO>(vos);
			// ��ȫǰ̨VO
			AggFormulaCfgVO[] fullBills = transTool.getClientFullInfoBill();
			// ����޸�ǰvo
			AggFormulaCfgVO[] originBills = transTool.getOriginBills();
			// ����BP
			AceItfformulacfgUpdateBP bp = new AceItfformulacfgUpdateBP();
			AggFormulaCfgVO[] retBills = bp.update(fullBills, originBills);
			// ���췵������
			retBills = transTool.getBillForToClient(retBills);
			return retBills;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFormulaCfgVO[] pubquerybills(IQueryScheme queryScheme) throws BusinessException {
		AggFormulaCfgVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFormulaCfgVO> query = new BillLazyQuery<AggFormulaCfgVO>(AggFormulaCfgVO.class);
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