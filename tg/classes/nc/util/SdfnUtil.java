package nc.util;

import java.util.ArrayList;
import java.util.List;

public class SdfnUtil {
	private static List<String> ABSList = new ArrayList<>(); 
	private static List<String> DomDebtList = new ArrayList<>(); 
	
	static{
		ABSList.add("资产证券化融资");
		ABSList.add("供应链ABS");
		ABSList.add("购房尾款ABS");
		ABSList.add("供应链ABN");
		DomDebtList.add("境内债");
		DomDebtList.add("公募债");
		DomDebtList.add("私募债");
	}
	
	public static List<String> getABSList(){
		return ABSList;
	}
	
	public static List<String> getDomDebtList(){
		return DomDebtList;
	}
}
