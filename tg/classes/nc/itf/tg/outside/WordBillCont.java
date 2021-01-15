package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class WordBillCont {

	private static Map<String, String> billNameMap = null;

	public static final String DOC_01 = "01"; // �û�Ȩ��

	public static final String DOCNAME_01 = "�û�Ȩ��"; // �û�Ȩ��

	public static final String DOC_02 = "02"; // ������֯

	public static final String DOCNAME_02 = "������֯"; // ������֯

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

		}
		return billNameMap;
	}
}
