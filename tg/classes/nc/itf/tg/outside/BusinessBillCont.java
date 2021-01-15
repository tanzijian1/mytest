package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class BusinessBillCont {

	private static Map<String, String> billNameMap = null;

	public static final String DOC_01 = "org"; // ������֯��ѯ�ӿ�

	public static final String DOCNAME_01 = "������֯��ѯ"; // ������֯��ѯ�ӿ�

	public static final String DOC_02 = "del_receipt"; // �տɾ��

	public static final String DOCNAME_02 = "�տɾ��"; // �տɾ��

	public static final String DOC_03 = "statement_query"; // ������ˮ��ѯ
    
	public static final String DOC_10="busiPay";//��ҵ�˿�ӿ�
	
	public static final String DOCNAME_10="��ҵ�˿�ӿ�";
	
	public static final String DOC_11="busiRefWk";//�·Ǻ�ͬ�������д��ӿ�
	
	public static final String DOCNAME_11="�·Ǻ�ͬ�������д��ӿ�";//�·Ǻ�ͬ�������д��ӿ�
	
	public static final String DOC_12="busiHisWk";//��ʷ���ݲ�¼��д��ӿ�
	
	public static final String DOCNAME_12="��ʷ���ݲ�¼��д��ӿ�";
	
	public static final String DOC_13="busiInvWk";//��Ʊ����д��ӿ�
	
	public static final String DOCNAME_13="��Ʊ����д��ӿ�";
	
	public static final String DOCNAME_03 = "������ˮ��ѯ"; // ������ˮ��ѯ
	
	public static final String DOCNAME_04 = "BUSI-->NC�տ"; // BUSI-->NC�տ

	public static final String DOC_04 = "busiGat"; // BUSI-->NC�տ

	public static final String DOC_05 = "Income"; // BUSI-->NC�տ

	public static final String DOCNAME_05 = "BUSI-->NC��ҵ���빤��"; // BUSI-->NC�տ

	public static final String DOC_NAME = "��ʷ���ݲ�¼��д��ӿ�";

	public static final String SRCSYS = "��ҵ"; // ��Դϵͳ

	public static final String BUSIREC = "busiRec"; // ��ҵӦ�յ�

	public static final String BUSIREC_NAME = "��ҵӦ�յ�д��ӿ�"; // Ӧ�յ�д��ӿ�

	/**
	 * Ŀ��ҵ�񵥾����������� 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(DOC_01, DOCNAME_01);
			billNameMap.put(DOC_02, DOCNAME_02);
			billNameMap.put(DOC_10, DOCNAME_10);
			billNameMap.put(DOC_04, DOCNAME_04);
			billNameMap.put(DOC_05, DOCNAME_05);
			billNameMap.put(DOC_10, DOCNAME_10);
			billNameMap.put(DOC_11, DOCNAME_11);
			billNameMap.put(DOC_12, DOCNAME_12);
			billNameMap.put(DOC_13, DOCNAME_13);
			billNameMap.put(BUSIREC, BUSIREC_NAME);
		}
		return billNameMap;
	}
}
