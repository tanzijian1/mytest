package nc.itf.tg.bpm;

import nc.vo.ep.bx.JKBXVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * @param formData
 *            表单信息
 * @param processname
 *            流程名称
 * @param deptid
 *            部门id
 * @param billid
 *            单据pk 参数可以参考nc.bs.er.exp.cmd.ExpPushCmd.execute()方法
 * @author lyq
 * 
 */
public interface IPushSaleBillToBpm {

	public AbstractBill pushBillToBPM(String billCode, AbstractBill bill)
			throws BusinessException;
	/**
	 * 网报请款单提交推送BPM审批
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public JKBXVO pushBXBillToBPM(JKBXVO vo) throws BusinessException;

}
