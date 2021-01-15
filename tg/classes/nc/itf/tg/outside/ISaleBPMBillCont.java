package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class ISaleBPMBillCont {

	public static final String PROCESSNAME_F3_Cxx_011 = "�ڲ��ʽ������";
	public static final String PROCESSNAME_F3_Cxx_012 = "˰�ѽ������뵥";
	public static final String PROCESSNAME_F3_Cxx_016 = "ͳ��ͳ��-�ڲ��ſ";
	public static final String PROCESSNAME_F3_Cxx_017 = "ͳ��ͳ��-�ڲ����";
	public static final String PROCESSNAME_F3_Cxx_027 = "��������������";
	public static final String PROCESSNAME_267X_Cxx_001 = "����Ʊ�������̣��ѿأ�";
	
	public static final String PROCESSNAMETOBPM_F3_Cxx_011 = "�ڲ��ʽ����";
	public static final String PROCESSNAMETOBPM_F3_Cxx_012 = "˰���������";
	public static final String PROCESSNAMETOBPM_F3_Cxx_016= "ͳ��ͳ������";
	public static final String PROCESSNAMETOBPM_F3_Cxx_027= "���������ѳ�������";
	public static final String PROCESSNAMETOBPM_267X_Cxx_001= "����Ʊ�������̣��ѿأ�";//��Ʊ����
	

	private static Map<String, String> billNameMap = null;

	public static final String F3_Cxx_011 = "01"; // �ڲ��ʽ������
	public static final String F3_Cxx_012 = "02";// ˰�ѽ������뵥
	public static final String F3_Cxx_016 = "03";// ͳ��ͳ��-�ڲ��ſ
	public static final String F3_Cxx_017 = "04";// ͳ��ͳ��-�ڲ����
	public static final String D267X_Cxx_001 = "14";// ��Ʊ����
    
	public static final String BILL_05 = "05"; // ���÷ѱ���-�ز�
	public static final String BILLNAME_05 = "���÷ѱ���-�ز�";
	
	public static final String BILL_06 = "06";
	public static final String BILLNAME_06 = "���÷ѱ���-��ҵ";
	
	
	public static final String BILL_07 = "07"; // 
	public static final String BILLNAME_07 = "���÷ѱ���-��ҵ";
	
	public static final String BILL_08 = "08"; //
	public static final String BILLNAME_08 = "�Ǻ�ͬ�������-�ز�";
	
	public static final String BILL_09 = "09"; // 
	public static final String BILLNAME_09 = "�Ǻ�ͬ�������-��ҵ";
	
	public static final String BILL_10 = "10"; 
	public static final String BILLNAME_10 = "�Ǻ�ͬ�������-��ҵ";
	
	public static final String BILL_11 = "11"; 
	public static final String BILLNAME_11 = "��ͬ�������-�ز�";
	
	public static final String BILL_12 = "12"; 
	public static final String BILLNAME_12 = "��ͬ�������-�ز�";
	
	public static final String BILL_13 = "13"; 
	public static final String BILLNAME_13 = "��ͬ�������-��ҵ";
	
	public static final String BILL_15 = "15"; 
	public static final String BILLNAME_15 = "�ʽ����л�������";
	
	public static final String BILL_16 = "16"; 
	public static final String BILLNAME_16 = "�ʽ����ĳ���������Ϣ����";
	
	public static final String BILL_17 = "17"; 
	public static final String BILLNAME_17 = "�ʽ𲿲�����ʷ�";
	
	public static final String BILL_18 = "18"; 
	public static final String BILLNAME_18 = "�ʽ����ʷ�������";
	
	public static final String BILL_19 = "19"; 
	public static final String BILLNAME_19 = "����Ʊ�����������ʽ��ࣩ";
	
	//add by tjl 2020-05-26
	public static final String F3_Cxx_027 = "20";// ���������ѵ�
	public static final String BILL_22 = "22";//Ԥ�������
	public static final String BILLNAME_22_23 = "�������Ԥ����������";//Ԥ�������
	public static final String BILL_23 = "23";//Ԥ�������
	
	public static final String BILL_24 = "24";//�ʱ��г�����
	public static final String BILLNAME_24 = "�ʽ����л����������ʱ�ҵ��";
	
	//end
	
	/**
	 * Ŀ��ҵ�񵥾�����������
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(F3_Cxx_011, PROCESSNAME_F3_Cxx_011);
			billNameMap.put(F3_Cxx_012, PROCESSNAME_F3_Cxx_012);
			billNameMap.put(F3_Cxx_016, PROCESSNAME_F3_Cxx_016);
			billNameMap.put(F3_Cxx_017, PROCESSNAME_F3_Cxx_017);
			billNameMap.put(F3_Cxx_027, PROCESSNAME_F3_Cxx_027);//add by tjl 2020-05-26
			billNameMap.put(D267X_Cxx_001, PROCESSNAME_267X_Cxx_001);
			billNameMap.put(BILL_05, BILLNAME_05);
			billNameMap.put(BILL_06, BILLNAME_06);
			billNameMap.put(BILL_07, BILLNAME_07);
			billNameMap.put(BILL_08, BILLNAME_08);
			billNameMap.put(BILL_09, BILLNAME_09);
			billNameMap.put(BILL_10, BILLNAME_10);
			billNameMap.put(BILL_11, BILLNAME_11);
			billNameMap.put(BILL_12, BILLNAME_12);
			billNameMap.put(BILL_13, BILLNAME_13);
			billNameMap.put(BILL_15, BILLNAME_15);
			billNameMap.put(BILL_16, BILLNAME_16);
			billNameMap.put(BILL_17, BILLNAME_17);
			billNameMap.put(BILL_18, BILLNAME_18);
			billNameMap.put(BILL_19, BILLNAME_19);
			billNameMap.put(BILL_22, BILLNAME_22_23);
			billNameMap.put(BILL_23, BILLNAME_22_23);
			billNameMap.put(BILL_24, BILLNAME_24);
		}
		return billNameMap;
	}
}
