package nc.itf.tg.bpm;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * @param formData
 *            ����Ϣ
 * @param processname
 *            ��������
 * @param deptid
 *            ����id
 * @param billid
 *            ����pk �������Բο�nc.bs.er.exp.cmd.ExpPushCmd.execute()����
 * @author lyq
 * 
 */
public interface IPushBillToBpm {

	public AbstractBill pushBillToBPM(String billCode, AbstractBill bill)
			throws BusinessException;

}
