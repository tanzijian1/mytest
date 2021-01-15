/**
 * <p>Title: ConvertBill.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��8��29�� ����10:58:01

 * @version 1.0
 */

package nc.bs.tg.outside.sale.utils.convertor;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;

/**
 * ����ʱ�䣺2019��8��29�� ����10:58:01  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * �ļ����ƣ�ConvertBill.java  
 * ��˵����  JSON����ת�����ݹ�����
 */

/**
 * <p>
 * Title: ConvertBill<��p>
 * 
 * <p>
 * Description: ͨ��JSON->Bill����ת����<��p>
 * 
 * <p>
 * Company: hanzhi<��p>
 * 
 * @author bobo
 * 
 * @date 2019��8��29�� ����10:58:01
 */

public class BillConvertor extends DefaultConvertor {

	/**
	 * <p>
	 * Title: getRefAttributePk<��p>
	 * <p>
	 * Description: <��p>
	 * 
	 * @param refDoc
	 *            VO����Ԫ���ݲ���doc
	 * @param code
	 *            �������-Ĭ��Ϊ����
	 * @return ����pk
	 */
	public String getRefAttributePk(String refDoc, String code) {
		if (code == null || "".equals(code))
			return null;
		String pkValue = null;
		String sql = null;
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			if ("user".equals(refDoc)) {
				sql = "select cuserid from sm_user where user_code = ?";
				SQLParameter parameter = new SQLParameter();
				parameter.addParam(code);

				pkValue = (String) iUAPQueryBS.executeQuery(sql, parameter,
						new ColumnProcessor());
			} else if ("financeorg".equals(refDoc)) {
				sql = "select pk_financeorg from org_financeorg where code = ?";
				SQLParameter parameter = new SQLParameter();
				parameter.addParam(code);

				pkValue = (String) iUAPQueryBS.executeQuery(sql, parameter,
						new ColumnProcessor());
			} else if ("summary".equals(refDoc)) {
				sql = "select pk_summary from fipub_summary where code = ?";
				SQLParameter parameter = new SQLParameter();
				parameter.addParam(code);

				pkValue = (String) iUAPQueryBS.executeQuery(sql, parameter,
						new ColumnProcessor());

			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return pkValue;
	}

}
