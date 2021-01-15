package nc.itf.tg.bpm;

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
public interface IPushBillToBpm {

	public AbstractBill pushBillToBPM(String billCode, AbstractBill bill)
			throws BusinessException;

}
