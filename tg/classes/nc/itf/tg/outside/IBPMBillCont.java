package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class IBPMBillCont {

	public static final String PROCESSNAME_RZ06_01 = "�ʽ𲿲�����ʷ�";
	public static final String PROCESSNAME_RZ06_02 = "�ʽ����ʷ�������";
	public static final String PROCESSNAME_36FF_01 = "�ʽ����л�������";
	public static final String PROCESSNAME_36FF_02 = "�ʽ����ĳ���������Ϣ����";
	public static final String PROCESSNAME_SD08 = "�ʽ����л����������ʱ�ҵ��";
	public static final String PROCESSNAME_36FA = "�ʽ����Ĵ����ͬ������������ࣩ";
	public static final String PROCESSNAME_RZ04 = "����Э��";

	private static Map<String, String> billNameMap = null;

	public static final String BILL_RZ06_01 = "01"; // �ʽ𲿲�����ʷ�
	public static final String BILL_RZ06_02 = "02";// �ʽ����ʷ�������
	public static final String BILL_36FF_01 = "03";// �ʽ����л�������
	public static final String BILL_36FF_02 = "04";// "�ʽ����ĳ���������Ϣ����
	public static final String BILL_36FA = "05";// �ʽ����Ĵ����ͬ������������ࣩ
	public static final String BILL_RZ04 = "06";// ����Э��
	public static final String BILL_SD08 = "07";// �ʽ����л�������

	/**
	 * Ŀ��ҵ�񵥾�����������
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(BILL_RZ06_01, PROCESSNAME_RZ06_01);
			billNameMap.put(BILL_RZ06_02, PROCESSNAME_RZ06_02);
			billNameMap.put(BILL_36FF_01, PROCESSNAME_36FF_01);
			billNameMap.put(BILL_36FF_02, PROCESSNAME_36FF_02);
			billNameMap.put(BILL_36FA, PROCESSNAME_36FA);
			billNameMap.put(BILL_RZ04, PROCESSNAME_36FA);
			billNameMap.put(BILL_SD08, PROCESSNAME_SD08);
		}
		return billNameMap;
	}
}
