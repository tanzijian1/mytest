package nc.util;

import java.util.ArrayList;
import java.util.List;

public class SdfnUtil {
	private static List<String> ABSList = new ArrayList<>(); 
	private static List<String> DomDebtList = new ArrayList<>(); 
	
	static{
		ABSList.add("�ʲ�֤ȯ������");
		ABSList.add("��Ӧ��ABS");
		ABSList.add("����β��ABS");
		ABSList.add("��Ӧ��ABN");
		DomDebtList.add("����ծ");
		DomDebtList.add("��ļծ");
		DomDebtList.add("˽ļծ");
	}
	
	public static List<String> getABSList(){
		return ABSList;
	}
	
	public static List<String> getDomDebtList(){
		return DomDebtList;
	}
}
