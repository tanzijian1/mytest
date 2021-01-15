package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class SaleBillCont {

	private static Map<String, String> billNameMap = null;

	public static final String BILLTYPE_FN11 = "FN11"; // ��Ʊ��

	public static final String BILLNAME_FN11 = "��Ʊ��"; // ��Ʊ��

	public static final String BILLTYPE_FN13 = "FN13"; // ��̯����

	public static final String BILLNAME_FN13 = "��̯����"; // ��̯����

	public static final String BILLTYPE_FN15 = "FN15"; // ��ת��ת�˵�

	public static final String BILLNAME_FN15 = "��ת��ת�˵�"; // ��ת��ת�˵�

	public static final String BILLTYPE_FN16 = "FN16"; // ̢������

	public static final String BILLNAME_FN16 = "̢������"; // ̢������

	public static final String BILLTYPE_FN17 = "FN17"; // ̢�������

	public static final String BILLNAME_FN17 = "̢�������"; // ̢�������

	public static final String BILLTYPE_FN18 = "FN18"; // ������Ʊ��

	public static final String BILLNAME_FN18 = "���վݵ�"; // ������Ʊ��
	
	public static final String BILLTYPE_FN19 = "FN19"; // ����ת�˵�

	public static final String BILLNAME_FN19 = "����ת�˵�"; // ����ת�˵�

	public static final String BILLTYPE_FN3 = "FN3"; // ��Ӧ�̸��
	
	public static final String BILLNAME_FN3 = "��Ӧ�̸��"; // ��Ӧ�̸��

	public static final String BILLTYPE_F3 = "F3"; // ��Ӧ�̸��
	
	public static final String BILLNAME_F3 = "��Ӧ�̸��"; // ��Ӧ�̸��

	public static final String BILLTYPE_36S4 = "36S4"; // ���˽���

	public static final String BILLNAME_36S4 = "���˽���"; // ���˽���

	public static final String BILLTYPE_F7 = "F7"; // �տ

	public static final String BILLNAME_F7 = "�տ"; // �տ
	
	public static final String BILLTYPE_FN20 = "FN20"; // �˿-->�ͻ�������Ϣ

	public static final String BILLNAME_FN20 = "�ͻ�������Ϣ"; // �˿-->�ͻ�������Ϣ
	
	public static final String BILLtype_4D10="4D10";//��Ŀ-����
	
	public static final String BillNAME_4D10="��Ŀ-���ŵ���";//��Ŀ-����
	
	public static final String BILLtype_F3F7="F3F7";//ת�����ո���
	
	public static final String BillNAME_F3F7="ת�����ո���";//ת�����ո���
	/**
	 * Ŀ��ҵ�񵥾����������� 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(BILLTYPE_FN11, BILLNAME_FN11);
			billNameMap.put(BILLTYPE_FN13, BILLNAME_FN13);
			billNameMap.put(BILLTYPE_FN15, BILLNAME_FN15);
			billNameMap.put(BILLTYPE_FN16, BILLNAME_FN16);
			billNameMap.put(BILLTYPE_FN17, BILLNAME_FN17);
			billNameMap.put(BILLTYPE_FN18, BILLNAME_FN18);
			billNameMap.put(BILLTYPE_FN19, BILLNAME_FN19);
			billNameMap.put(BILLTYPE_36S4, BILLNAME_36S4);
			billNameMap.put(BILLTYPE_FN3, BILLNAME_FN3);
			billNameMap.put(BILLTYPE_F3, BILLNAME_F3);
			billNameMap.put(BILLTYPE_F7, BILLNAME_F7);
			billNameMap.put(BILLTYPE_FN20, BILLNAME_FN20);
			billNameMap.put(BILLtype_4D10, BillNAME_4D10);
			billNameMap.put(BILLtype_F3F7, BillNAME_F3F7);
		}
		return billNameMap;
	}
}
