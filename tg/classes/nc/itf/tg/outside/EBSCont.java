package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class EBSCont {
	// ------------------��������-------------------------
	// TODO ���š���Ա����λ����HCM����,���ط����ӿ�

	private static Map<String, String> docNameMap = null;

	public static final String DOC_01 = "01"; // ��Ŀ����

	public static final String DOCNAME_01 = "��Ŀ����"; // ��Ŀ����

	public static final String DOC_02 = "02"; // ��Ŀ��˾

	public static final String DOCNAME_02 = "��Ŀ��˾"; // ��Ŀ��˾

	public static final String DOC_03 = "03"; // ��ͬ����

	public static final String DOCNAME_03 = "��ͬ����"; // ��ͬ����

	public static final String DOC_04 = "04"; // ���첿��

	public static final String DOCNAME_04 = "���첿��"; // ���첿��

	public static final String DOC_05 = "05"; // ������

	public static final String DOCNAME_05 = "������"; // ������

	public static final String DOC_06 = "06"; // �����λ

	public static final String DOCNAME_06 = "�����λ"; // �����λ

	public static final String DOC_07 = "07"; // �������

	public static final String DOCNAME_07 = "�������"; // �������

	public static final String DOC_08 = "08"; // ˰��

	public static final String DOCNAME_08 = "˰��"; // ˰��

	public static final String DOC_09 = "09"; // �а���ʽ

	public static final String DOCNAME_09 = "�а���ʽ"; // �а���ʽ

	public static final String DOC_10 = "10"; // ҵ̬

	public static final String DOCNAME_10 = "ҵ̬"; // ҵ̬

	public static final String DOC_11 = "11"; // ��������

	public static final String DOCNAME_11 = "��������"; // ��������

	public static final String DOC_12 = "12"; // Ԥ���Ŀ
	public static final String DOCNAME_12 = "Ԥ���Ŀ"; // Ԥ���Ŀ

	public static final String DOC_13 = "13"; // �ɱ���Ŀ
	public static final String DOCNAME_13 = "�ɱ���Ŀ"; // �ɱ���Ŀ

	public static final String DOC_14 = "14"; // ��֧��Ŀ
	public static final String DOCNAME_14 = "��֧��Ŀ"; // ��֧��Ŀ

	public static final String DOC_15 = "15"; // �����Ϣ
	public static final String DOCNAME_15 = "�����Ϣ"; // �����Ϣ

	public static final String DOC_16 = "16"; // ���е���
	public static final String DOCNAME_16 = "���е���"; // ���е���

	public static final String DOC_17 = "17"; // �����˻�
	public static final String DOCNAME_17 = "�����˻�"; // �����˻�

	public static final String DOC_18 = "18"; // ���㷽ʽ
	public static final String DOCNAME_18 = "���㷽ʽ"; // ���㷽ʽ

	public static final String DOC_19 = "19"; // ��������
	public static final String DOCNAME_19 = "��������"; // ��������

	public static final String DOC_20 = "20"; // ����¥��
	public static final String DOCNAME_20 = "����¥��"; // ����¥��

	public static final String DOC_21 = "21"; // ��Ӧ��
	public static final String DOCNAME_21 = "��Ӧ��"; // ��Ӧ��
	
	public static final String DOC_22 = "22"; // ���������˺�
	public static final String DOCNAME_22 = "���������˻�"; // ���������˺�

	/**
	 * Ŀ��ҵ�񵥾����������� 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getDocNameMap() {
		if (docNameMap == null) {
			docNameMap = new HashMap<String, String>();
			docNameMap.put(DOC_01, DOCNAME_01);
			docNameMap.put(DOC_02, DOCNAME_02);
			docNameMap.put(DOC_03, DOCNAME_03);
			docNameMap.put(DOC_04, DOCNAME_04);
			docNameMap.put(DOC_05, DOCNAME_05);
			docNameMap.put(DOC_06, DOCNAME_06);
			docNameMap.put(DOC_07, DOCNAME_07);
			docNameMap.put(DOC_08, DOCNAME_08);
			docNameMap.put(DOC_09, DOCNAME_09);
			docNameMap.put(DOC_10, DOCNAME_10);
			docNameMap.put(DOC_11, DOCNAME_11);
			docNameMap.put(DOC_12, DOCNAME_12);
			docNameMap.put(DOC_13, DOCNAME_13);
			docNameMap.put(DOC_14, DOCNAME_14);
			docNameMap.put(DOC_15, DOCNAME_15);
			docNameMap.put(DOC_16, DOCNAME_16);
			docNameMap.put(DOC_17, DOCNAME_17);
			docNameMap.put(DOC_18, DOCNAME_18);
			docNameMap.put(DOC_19, DOCNAME_19);
			docNameMap.put(DOC_20, DOCNAME_20);
			docNameMap.put(DOC_21, DOCNAME_21);
		}
		return docNameMap;
	}

	// ------------------�������-----------------------
	private static Map<String, String> billNameMap = null;

	public static final String BILL_01 = "01"; // �������뵥����ͬ������Ԥ��ռ�õ���

	public static final String BILLNAME_01 = "�������뵥"; // �������뵥����ͬ������Ԥ��ռ�õ���

	public static final String BILL_02 = "02"; // ���Ʊ

	public static final String BILLNAME_02 = "���Ʊ"; // ���Ʊ

	public static final String BILL_03 = "03"; // Ӧ����

	public static final String BILLNAME_03 = "Ӧ����"; // Ӧ����

	public static final String BILL_04 = "04"; // �������뵥->���

	public static final String BILLNAME_04 = "�������뵥"; // �������뵥->���

	public static final String BILL_05 = "05"; // �����ͬ

	public static final String BILLNAME_05 = "�����ͬ"; // �����ͬ

	public static final String BILL_06 = "06"; // �տ��ͬ

	public static final String BILLNAME_06 = "�տ��ͬ"; // �տ��ͬ

	public static final String BILL_07 = "07"; // ��֤�𹤵�

	public static final String BILLNAME_07 = "��֤�𹤵�"; // ��֤�𹤵�

	public static final String BILL_08 = "08"; // ���ط�̯����

	public static final String BILLNAME_08 = "���ط�̯����"; // ���ط�̯����

	public static final String BILL_09 = "09"; // ������Ϣ����˰����

	public static final String BILLNAME_09 = "������Ϣ����˰����"; // ������Ϣ����˰����

	// public static final String BILL_10 = "10"; // �ݹ�Ӧ������
	//
	// public static final String BILLNAME_10 = "�ݹ�Ӧ������"; // �ݹ�Ӧ������

	public static final String BILL_10 = "10"; // ������
	public static final String BILLNAME_10 = "������ϸ��,�ݹ�Ӧ������"; // ������

	public static final String BILL_11 = "11"; // ���˵�

	public static final String BILLNAME_11 = "���˵�"; // ���˵�

	public static final String BILL_12 = "12"; // Ӧ�յ�

	public static final String BILLNAME_12 = "Ӧ�յ�"; // Ӧ�յ�

	public static final String BILL_13 = "13"; // ���⹤��

	public static final String BILLNAME_13 = "���⹤��"; // ���⹤��

	public static final String Bill_14 = "14";// ֧�����ͬ

	public static final String BILLNAME_14 = "֧�����ͬ"; // ֧�����ͬ

	public static final String BILL_15 = "15";// ���Ʊ��Ʊ

	public static final String BILLNAME_15 = "���Ʊ"; // ���Ʊ��Ʊ

	public static final String BILL_16 = "16";// ��֤�𹤵�

	public static final String BILLNAME_16 = "��֤�𹤵�"; // ��֤�𹤵�

	public static final String BILL_17 = "17";// �����ͬ-�ɱ����ͬ

	public static final String BILLNAME_17 = "�����ͬ-�ɱ����ͬ"; // �����ͬ-�ɱ����ͬ

	public static final String BILL_18 = "18";// �����ͬ-�ɹ����Ϻ�ͬ��ebs�ɹ�Э�鵥��

	public static final String BILLNAME_18 = "�����ͬ-�ɹ����Ϻ�ͬ��ebs�ɹ�Э�鵥��"; // �����ͬ-�ɹ����Ϻ�ͬ��ebs�ɹ�Э�鵥��

	public static final String BILL_19 = "19";// ���ⵥ-����Ӧ�յ�

	public static final String BILLNAME_19 = "SRM-���ⵥ-->Ӧ�յ�"; // ���ⵥ-����Ӧ�յ�

	public static final String BILL_20 = "20";// SRM-��Ӧ���չ�Ӧ����Ͷ�걣֤��

	public static final String BILLNAME_20 = "SRM-��Ӧ���չ�Ӧ����Ͷ�걣֤��"; // SRM-��Ӧ���չ�Ӧ����Ͷ�걣֤��

	public static final String BILL_25 = "25";// EBSͨ��-Ӧ��������

	public static final String BILLNAME_25 = "EBSͨ��-Ӧ��������";

	public static final String BILL_21 = "21";// ͨ�ñ�����

	public static final String BILLNAME_21 = "ͨ�ñ�����"; // ͨ�ñ�����

	public static final String BILL_22 = "22";// SRMͶ�걣֤��Ӧ�յ�

	public static final String BILLNAME_22 = "SRM-Ͷ�걣֤��Ӧ�յ�"; // SRMͶ�걣֤��Ӧ�յ�

	public static final String BILL_23 = "23";// EBS�ɱ�-Ӧ�����븶�����뵥

	public static final String BILLNAME_23 = "EBS�ɱ�-Ӧ�����븶�����뵥"; // EBS�ɱ�-Ӧ�����븶�����뵥
	public static final String BILL_26 = "26";// �������Ʊ

	public static final String BILLNAME_26 = "�������Ʊ";
	/**
	 * 
	 */
	public static final String BILL_27 = "27";// srm��Ӧ���Թ�Ӧ�̶�����Ϣ��nc����Ʊ������

	public static final String BILLNAME_27 = "SRM��Ӧ���Թ�Ӧ�̶�����Ϣ����Ʊ������";
	// �ɱ�˰��Ӧ����-2020-05-25-̸�ӽ�
	public static final String BILL_28 = "28";// �ɱ�˰��Ӧ����-2020-05-25-̸�ӽ�
	public static final String BILLNAME_28 = "EBS->�ɱ�˰��Ӧ����";
	// �ɱ�ռԤ��Ӧ����-2020-05-26-̸�ӽ�
	public static final String BILL_29 = "29";
	public static final String BILLNAME_29 = "EBS->�ɱ�ռԤ��Ӧ����";
	// EBS�ɱ�-Ӧ�����븶�����뵥-2020-05-30-̸�ӽ�
	public static final String BILL_30 = "30";
	public static final String BILLNAME_30 = "EBS�ɱ�-Ӧ�����븶�����뵥";
	// ͨ��˰��Ӧ����-2020-06-08-̸�ӽ�
	public static final String BILL_31 = "31";
	public static final String BILLNAME_31 = "EBS->ͨ��˰��Ӧ����";
	// ͨ��ռԤ��Ӧ����-2020-05-26-̸�ӽ�
	public static final String BILL_32 = "32";
	public static final String BILLNAME_32 = "EBS->ͨ��ռԤ��Ӧ����";

	/**
	 * Ŀ��ҵ�񵥾����������� 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(BILL_01, BILLNAME_01);
			billNameMap.put(BILL_02, BILLNAME_02);
			billNameMap.put(BILL_03, BILLNAME_03);
			billNameMap.put(BILL_04, BILLNAME_04);
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
			billNameMap.put(BILL_20, BILLNAME_20);
			billNameMap.put(BILL_21, BILLNAME_21);
			billNameMap.put(BILL_22, BILLNAME_22);
			billNameMap.put(BILL_25, BILLNAME_25);
			billNameMap.put(BILL_26, BILLNAME_26);
			billNameMap.put(BILL_27, BILLNAME_27);
			billNameMap.put(BILL_28, BILLNAME_28);
			billNameMap.put(BILL_29, BILLNAME_29);
			billNameMap.put(BILL_30, BILLNAME_30);
			billNameMap.put(BILL_31, BILLNAME_31);
			billNameMap.put(BILL_32, BILLNAME_32);
		}
		return billNameMap;
	}

	// ��Դ��������ָ��NCҵ��
	private static Map<String, String> srcBillToDesBillMap = null;
	private static Map<String, String> srcBillNameMap = null;

	public static final String SRCBILL_01 = "01"; // EBS-ͨ�ú�ͬ->����Ԥ�ᵥ
	public static final String SRCBILLNAME_01 = "EBS-��ͬԤռ��->����Ԥռ��";
	public static final String SRCBILL_02 = "02"; // EBS-��ͬԤ�ᵥ->����Ԥ�ᵥ
	public static final String SRCBILLNAME_02 = "EBS-Ԥ��Ԥ�ᵥ->����Ԥ�ᵥ"; //
	public static final String SRCBILL_03 = "03"; // EBS-ͨ�ú�ͬ���->Ӧ����
	public static final String SRCBILLNAME_03 = "EBS-ͨ�ú�ͬ���->Ӧ����"; // EBS-ͨ�ú�ͬ���->Ӧ����
	public static final String SRCBILL_04 = "04"; // EBS-ͨ�ú�ͬ���->�������뵥(����)
	public static final String SRCBILLNAME_04 = "EBS-ͨ�ú�ͬ���->�������뵥";
	public static final String SRCBILL_05 = "05"; // EBS-Ӫ�������->Ӧ����
	public static final String SRCBILLNAME_05 = "EBS-Ӫ�������->Ӧ����";
	public static final String SRCBILL_06 = "06"; // EBS-Ӫ�������->�������뵥 (����)
	public static final String SRCBILLNAME_06 = "EBS-Ӫ�������->�������뵥";
	public static final String SRCBILL_07 = "07"; // EBS-��Ŀ��˾Ӷ�����->�������뵥 (����)
	public static final String SRCBILLNAME_07 = "EBS-��Ŀ��˾Ӷ�����->�������뵥";
	public static final String SRCBILL_08 = "08"; // EBSͨ��֧����ͬ->�����ͬ
	public static final String SRCBILLNAME_08 = "EBSͨ��֧����ͬ->�����ͬ";
	public static final String SRCBILL_09 = "09"; // SRM���˵�->Ӧ����
	public static final String SRCBILLNAME_09 = "SRM���˵�->Ӧ����";
	public static final String SRCBILL_10 = "10"; // EBS-��Ӧ����->�������뵥 (�ɱ�-��֤��)
	public static final String SRCBILLNAME_10 = "EBS-��Ӧ����->�������뵥";
	public static final String SRCBILL_11 = "11"; // EBS-��ͬ���->��֤�𹤵�
	public static final String SRCBILLNAME_11 = "EBS-��ͬ���->��֤�𹤵�";
	public static final String SRCBILL_12 = "12"; // EBS-��ͬ����->���ؿ��̯����
	public static final String SRCBILLNAME_12 = "EBS-��ͬ����->���ؿ��̯����";
	public static final String SRCBILL_13 = "13"; // EBS-��ͬ->������Ϣ����˰����
	public static final String SRCBILLNAME_13 = "EBS-��ͬ->������Ϣ����˰����";

	public static final String SRCBILL_14 = "14"; // EBS-��Ŀ�ܽ����->Ӧ����
	public static final String SRCBILLNAME_14 = "EBS-�ɱ����->Ӧ����";

	public static final String SRCBILL_15 = "15"; // EBS-��Ŀ�ܽ����->�������뵥 (�ɱ�)
	public static final String SRCBILLNAME_15 = "EBS-��Ŀ�ܽ����->�������뵥";

	// public static final String SRCBILL_16 = "16"; // EBS-��Ʊ����->�ɱ���ԪӦ����
	// public static final String SRCBILLNAME_16 = "EBS-��Ʊ����->�ɱ���ԪӦ����"; //
	// EBS-��Ʊ����->�ɱ���ԪӦ����

	public static final String SRCBILL_18 = "18"; // EBS-��Ӧ��ͬ->�տ��ͬ
	public static final String SRCBILLNAME_18 = "EBS-��Ӧ��ͬ->�տ��ͬ";

	public static final String SRCBILL_20 = "20"; // SRM-������->������
	public static final String SRCBILLNAME_20 = "SRM-������->������";

	public static final String SRCBILL_21 = "21"; // SRM-���˵�->���˵�
	public static final String SRCBILLNAME_21 = "SRM-���˵�->���˵�";

	public static final String SRCBILL_22 = "22"; // EBS-������->Ӧ����
	public static final String SRCBILLNAME_22 = "EBS-������->Ӧ����";

	public static final String SRCBILL_23 = "23"; // SRM-���ⵥ->Ӧ�յ�
	public static final String SRCBILLNAME_23 = "SRM-���ⵥ->Ӧ�յ�";

	public static final String SRCBILL_24 = "24"; // SRM-��ⵥ->Ӧ����
	public static final String SRCBILLNAME_24 = "SRM-��ⵥ->Ӧ����";

	public static final String SRCBILL_25 = "25"; // SRM-���ó��ⵥ->���⹤��
	public static final String SRCBILLNAME_25 = "SRM-����/���۳��ⵥ->���⹤��";

	public static final String SRCBILL_26 = "26"; // SRM-���Ʊ->���Ʊ����
	public static final String SRCBILLNAME_26 = "SRM-���Ʊ->���Ʊ����";

	public static final String SRCBILL_27 = "27"; // EBS-������->�������뵥(�ɱ�-�ڲ�����)
	public static final String SRCBILLNAME_27 = "EBS-������->�������뵥";

	public static final String SRCBILL_28 = "28"; // EBS-�ɱ�/Ӫ�����ͬ->�����ͬ
	public static final String SRCBILLNAME_28 = "EBS-�ɱ�/Ӫ�����ͬ->�����ͬ";

	public static final String SRCBILL_29 = "29"; // EBS-֧��ͨ�ú�ͬ->�����ͬ
	public static final String SRCBILLNAME_29 = "EBS-֧��ͨ�ú�ͬ->�����ͬ";
	public static final String SRCBILL_30 = "30"; // EBS-�����ͬ(�ز�)->�տ��ͬ
	public static final String SRCBILLNAME_30 = "EBS-�����ͬ(�ز�)->�տ��ͬ";
	public static final String SRCBILL_31 = "31"; // EBS-����ͨ�ú�ͬ->�տ��ͬ
	public static final String SRCBILLNAME_31 = "EBS-����ͨ�ú�ͬ->�տ��ͬ";
	public static final String SRCBILL_32 = "32"; // EBS��ͬ�����֤���ͬ->��֤�𹤵�
	public static final String SRCBILLNAME_32 = "EBS��ͬ�����֤���ͬ->��֤�𹤵�";

	public static final String SRCBILL_33 = "33"; // EBS
	public static final String SRCBILL_34 = "34"; // EBS
	public static final String SRCBILL_35 = "35"; // EBS
	public static final String SRCBILL_36 = "36"; // EBS

	public static final String SRCBILL_37 = "37"; // SRM-������->������ϸ�����ݹ�Ӧ������
	public static final String SRCBILLNAME_37 = "SRM-������->������ϸ�����ݹ�Ӧ������";

	public static final String SRCBILL_38 = "38"; // �ɱ�/Ӫ����ͬ->�����ͬ-�ɱ���ͬ
	public static final String SRCBILLNAME_38 = "EBS-�ɱ�/Ӫ����ͬ->�����ͬ-�ɱ���ͬ";

	public static final String SRCBILL_39 = "39"; // EBS-�ɹ�Э�鵥->�����ͬ-�ɹ����Ϻ�ͬ
	public static final String SRCBILLNAME_39 = "EBS-�ɹ�Э�鵥->�����ͬ-���Ϻ�ͬ";

	public static final String SRCBILL_40 = "40"; // ����Ʊ��Ʊ
	public static final String SRCBILLNAME_40 = "����Ʊ��Ʊ";

	public static final String SRCBILL_41 = "41"; // EBS-ͨ���ղ������->Ӧ����
	public static final String SRCBILLNAME_41 = "EBS-ͨ���ղ������->Ӧ����";

	public static final String SRCBILL_42 = "42"; // SRM-��Ӧ���չ�Ӧ����Ͷ�걣֤��
	public static final String SRCBILLNAME_42 = "SRM-��Ӧ���չ�Ӧ����Ͷ�걣֤��";
	public static final String SRCBILL_43 = "43";// Ӧ�����������
	public static final String SRCBILLNAME_43 = "Ӧ�����������";
	public static final String SRCBILL_45 = "45";
	public static final String SRCBILLNAME_45 = "EBS->Ӧ�������";

	public static final String SRCBILL_44 = "44";// EBSԤ��->����������Ԥ�����
	public static final String SRCBILLNAME_44 = "EBSԤ��->����������Ԥ�����";

	public static final String SRCBILL_46 = "46"; // ��������Ʊ��Ʊ
	public static final String SRCBILLNAME_46 = "��������Ʊ��Ʊ";

	public static final String SRCBILL_47 = "47"; // SRMͶ�걣֤��Ӧ�յ�
	public static final String SRCBILLNAME_47 = "SRMͶ�걣֤��Ӧ�յ�";
	/**
	 * srm��Ӧ���Թ�Ӧ�̶�����Ϣ��nc����Ʊ������ 2020-04-02-̸�ӽ�
	 */
	public static final String SRCBILL_48 = "48"; // SRM��Ӧ���Թ�Ӧ�̶�����Ϣ����Ʊ������
	public static final String SRCBILLNAME_48 = "SRM��Ӧ���Թ�Ӧ�̶�����Ϣ����Ʊ������";
	// �ɱ�˰��Ӧ����-2020-05-25-̸�ӽ�
	public static final String SRCBILL_49 = "49";
	public static final String SRCBILLNAME_49 = "EBS->�ɱ�˰��Ӧ����";
	// �ɱ�ռԤ��Ӧ����
	public static final String SRCBILL_50 = "50";
	public static final String SRCBILLNAME_50 = "EBS->�ɱ�ռԤ��Ӧ����";
	// �ɱ�Ӧ�����븶�����뵥�ϲ��ӿ�-2020-05-30-̸�ӽ�
	public static final String SRCBILL_51 = "51";
	public static final String SRCBILLNAME_51 = "EBS->�ɱ�Ӧ�����븶�����뵥";

	// ͨ��˰��Ӧ����-2020-06-08-̸�ӽ�
	public static final String SRCBILL_52 = "52";
	public static final String SRCBILLNAME_52 = "EBS->ͨ��˰��Ӧ����";
	// ͨ��ռԤ��Ӧ����-2020-06-08-̸�ӽ�
	public static final String SRCBILL_53 = "53";
	public static final String SRCBILLNAME_53 = "EBS->ͨ��ռԤ��Ӧ����";

	public static Map<String, String> getSrcBillToDesBillMap() {
		if (srcBillToDesBillMap == null) {
			srcBillToDesBillMap = new HashMap<String, String>();
			srcBillToDesBillMap.put(SRCBILL_01, BILL_01);// EBS-ͨ�ú�ͬ->����Ԥ�ᵥ
			srcBillToDesBillMap.put(SRCBILL_02, BILL_01);// EBS-��ͬԤ�ᵥ->����Ԥ�ᵥ
			srcBillToDesBillMap.put(SRCBILL_03, BILL_03);// EBS-ͨ�ú�ͬ���->Ӧ����
			srcBillToDesBillMap.put(SRCBILL_04, BILL_04);// EBS-ͨ�ú�ͬ���->�������뵥
			srcBillToDesBillMap.put(SRCBILL_05, BILL_03);// EBS-Ӫ�������->Ӧ����
			srcBillToDesBillMap.put(SRCBILL_06, BILL_04);// EBS-Ӫ�������->�������뵥
			srcBillToDesBillMap.put(SRCBILL_07, BILL_04);// EBS-��Ŀ��˾Ӷ�����->�������뵥
			srcBillToDesBillMap.put(SRCBILL_08, BILL_05);// EBS-������Ǻ�ͬ/�ɱ���ͬ->�����ͬ

			srcBillToDesBillMap.put(SRCBILL_09, BILL_03);// SRM���˵�->Ӧ����
			srcBillToDesBillMap.put(SRCBILL_10, BILL_04);// EBS-��Ӧ����->�������뵥
			srcBillToDesBillMap.put(SRCBILL_11, BILL_07);// EBS-E��ͬ���->��֤�𹤵�
			srcBillToDesBillMap.put(SRCBILL_12, BILL_08);// EBS-��ͬ����->���ؿ��̯����
			srcBillToDesBillMap.put(SRCBILL_13, BILL_09);// EBS-��ͬ->������Ϣ����˰����
			srcBillToDesBillMap.put(SRCBILL_14, BILL_03);// EBS-��Ŀ�ܽ����->Ӧ����
			srcBillToDesBillMap.put(SRCBILL_15, BILL_04);// EBS-��Ŀ�ܽ����->�������뵥
			// srcBillToDesBillMap.put(SRCBILL_16, BILL_03);// EBS-��Ʊ����->�ɱ���ԪӦ����
			// srcBillToDesBillMap.put(SRCBILL_17, BILL_05);// SRM-�ɹ���ͬ->�����ͬ
			srcBillToDesBillMap.put(SRCBILL_18, BILL_06);// EBS-��Ӧ��ͬ->�տ��ͬ
			// srcBillToDesBillMap.put(SRCBILL_19, BILL_05);// EBS-��Ӧ��ͬ->�����ͬ
			srcBillToDesBillMap.put(SRCBILL_20, BILL_10);// SRM-������->������
			srcBillToDesBillMap.put(SRCBILL_21, BILL_11);// SRM-���˵�->���˵�
			srcBillToDesBillMap.put(SRCBILL_22, BILL_03);// EBS-������->Ӧ����
			srcBillToDesBillMap.put(SRCBILL_23, BILL_19);// SRM-���ⵥ->Ӧ�յ�
			srcBillToDesBillMap.put(SRCBILL_24, BILL_03);// SRM-��ⵥ->Ӧ����
			srcBillToDesBillMap.put(SRCBILL_25, BILL_13);// SRM-���ó��ⵥ->���⹤��
			srcBillToDesBillMap.put(SRCBILL_26, BILL_02);// SRM-���Ʊ->���Ʊ����
			srcBillToDesBillMap.put(SRCBILL_27, BILL_04);// SEBS-������->�������뵥

			srcBillToDesBillMap.put(SRCBILL_28, BILL_04);// EBS-�ɱ�/Ӫ�����ͬ->�����ͬ
			srcBillToDesBillMap.put(SRCBILL_29, BILL_04);// EBS-֧��ͨ�ú�ͬ->�����ͬ
			srcBillToDesBillMap.put(SRCBILL_30, BILL_05);// EBS-�����ͬ(�ز�)->�տ��ͬ
			srcBillToDesBillMap.put(SRCBILL_31, BILL_06);// EBS-����ͨ�ú�ͬ->�տ��ͬ
			srcBillToDesBillMap.put(SRCBILL_32, BILL_16);// EBS��ͬ�����֤���ͬ->��֤�𹤵�

			srcBillToDesBillMap.put(SRCBILL_37, BILL_10);// SRM-������->������ϸ�����ݹ�Ӧ������
			srcBillToDesBillMap.put(SRCBILL_40, BILL_15);// ���Ʊ
			srcBillToDesBillMap.put(SRCBILL_38, BILL_05);// EBS-������Ǻ�ͬ/�ɱ���ͬ->�����ͬ-�ɱ���ͬ
			srcBillToDesBillMap.put(SRCBILL_39, BILL_05);// EBS-�ɹ�Э�鵥->�����ͬ-�ɹ����Ϻ�ͬ
			srcBillToDesBillMap.put(SRCBILL_41, BILL_03);// EBS-ͨ���ղ������->Ӧ����
			srcBillToDesBillMap.put(SRCBILL_42, BILL_20);// SRM-��Ӧ���չ�Ӧ����Ͷ�걣֤��
			srcBillToDesBillMap.put(SRCBILL_43, BILL_03);// �ɱ�Ӧ�����������

			srcBillToDesBillMap.put(SRCBILL_44, BILL_21);// EBSԤ��->����������Ԥ�����
			srcBillToDesBillMap.put(SRCBILL_45, BILL_25);// EBSͨ��->Ӧ���͸������뵥

			srcBillToDesBillMap.put(SRCBILL_46, BILL_26);// ����ϵͳ�������Ʊ
			srcBillToDesBillMap.put(SRCBILL_47, BILL_22);// SRMͶ�걣֤��Ӧ�յ�
			/**
			 * srm��Ӧ���Թ�Ӧ�̶�����Ϣ��nc����Ʊ������ 2020-04-02-̸�ӽ�
			 */
			srcBillToDesBillMap.put(SRCBILL_48, BILL_27);
			// �ɱ�˰��Ӧ����-2020-05-25-̸�ӽ�
			srcBillToDesBillMap.put(SRCBILL_49, BILL_28);
			// EBS->�ɱ�ռԤ��Ӧ����-2020-05-26
			srcBillToDesBillMap.put(SRCBILL_50, BILL_29);
			// EBS->�ɱ�Ӧ�����븶�����뵥-2020-05-30-̸�ӽ�
			srcBillToDesBillMap.put(SRCBILL_51, BILL_30);
			// EBS->ͨ��˰��Ӧ����-2020-06-08-̸�ӽ�
			srcBillToDesBillMap.put(SRCBILL_52, BILL_31);
			// EBS->ͨ��ռԤ��Ӧ����-2020-06-08-̸�ӽ�
			srcBillToDesBillMap.put(SRCBILL_53, BILL_32);
		}
		return srcBillToDesBillMap;
	}

	public static Map<String, String> getSrcBillNameMap() {
		if (srcBillNameMap == null) {
			srcBillNameMap = new HashMap<String, String>();
			srcBillNameMap.put(SRCBILL_01, SRCBILLNAME_01);
			srcBillNameMap.put(SRCBILL_02, SRCBILLNAME_02);
			srcBillNameMap.put(SRCBILL_03, SRCBILLNAME_03);
			srcBillNameMap.put(SRCBILL_04, SRCBILLNAME_04);
			srcBillNameMap.put(SRCBILL_05, SRCBILLNAME_05);
			srcBillNameMap.put(SRCBILL_06, SRCBILLNAME_06);
			srcBillNameMap.put(SRCBILL_07, SRCBILLNAME_07);
			srcBillNameMap.put(SRCBILL_08, SRCBILLNAME_08);
			srcBillNameMap.put(SRCBILL_09, SRCBILLNAME_09);
			srcBillNameMap.put(SRCBILL_10, SRCBILLNAME_10);
			srcBillNameMap.put(SRCBILL_11, SRCBILLNAME_11);
			srcBillNameMap.put(SRCBILL_12, SRCBILLNAME_12);
			srcBillNameMap.put(SRCBILL_13, SRCBILLNAME_13);
			srcBillNameMap.put(SRCBILL_14, SRCBILLNAME_14);
			srcBillNameMap.put(SRCBILL_15, SRCBILLNAME_15);
			// srcBillNameMap.put(SRCBILL_16, SRCBILLNAME_16);
			// srcBillNameMap.put(SRCBILL_17, SRCBILLNAME_17);
			srcBillNameMap.put(SRCBILL_18, SRCBILLNAME_18);
			// srcBillNameMap.put(SRCBILL_19, SRCBILLNAME_19);
			srcBillNameMap.put(SRCBILL_20, SRCBILLNAME_20);
			srcBillNameMap.put(SRCBILL_21, SRCBILLNAME_21);
			srcBillNameMap.put(SRCBILL_22, SRCBILLNAME_22);
			srcBillNameMap.put(SRCBILL_23, SRCBILLNAME_23);
			srcBillNameMap.put(SRCBILL_24, SRCBILLNAME_24);
			srcBillNameMap.put(SRCBILL_25, SRCBILLNAME_25);
			srcBillNameMap.put(SRCBILL_26, SRCBILLNAME_26);
			srcBillNameMap.put(SRCBILL_27, SRCBILLNAME_27);
			srcBillNameMap.put(SRCBILL_28, SRCBILLNAME_28);
			srcBillNameMap.put(SRCBILL_29, SRCBILLNAME_29);
			srcBillNameMap.put(SRCBILL_30, SRCBILLNAME_30);
			srcBillNameMap.put(SRCBILL_31, SRCBILLNAME_31);
			srcBillNameMap.put(SRCBILL_37, SRCBILLNAME_37);
			// srcBillNameMap.put(BILL_15, BILLNAME_15);
			srcBillNameMap.put(SRCBILL_38, SRCBILLNAME_38);
			srcBillNameMap.put(SRCBILL_39, SRCBILLNAME_39);
			srcBillNameMap.put(SRCBILL_32, SRCBILLNAME_32);
			srcBillNameMap.put(SRCBILL_40, SRCBILLNAME_40);

			srcBillNameMap.put(SRCBILL_41, SRCBILLNAME_41);
			srcBillNameMap.put(SRCBILL_43, SRCBILLNAME_43);

			srcBillNameMap.put(SRCBILL_44, SRCBILLNAME_44);

			srcBillNameMap.put(SRCBILL_45, SRCBILLNAME_45);

			srcBillNameMap.put(SRCBILL_46, SRCBILLNAME_46);
			srcBillNameMap.put(SRCBILL_47, SRCBILLNAME_47);
			srcBillNameMap.put(SRCBILL_48, SRCBILLNAME_48);
			srcBillNameMap.put(SRCBILL_49, SRCBILLNAME_49);
			srcBillNameMap.put(SRCBILL_50, SRCBILLNAME_50);
			srcBillNameMap.put(SRCBILL_51, SRCBILLNAME_51);
			srcBillNameMap.put(SRCBILL_52, SRCBILLNAME_52);
			srcBillNameMap.put(SRCBILL_53, SRCBILLNAME_53);
		}
		return srcBillNameMap;
	}

}
