package nc.bs.tg.pub.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.BusinessException;
import nc.vo.uap.busibean.exception.BusiBeanException;

/**
 * 
 * @author huangxj
 * 
 *         �����ַ�����Util
 * 
 */
public class SpecialSymbols {

	/**
	 * �ж��Ƿ��������ַ�
	 * 
	 * @param str
	 * @return trueΪ������falseΪ������
	 */
	public static boolean isSpecialChar(String str) {
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * �жϲ�ѯ�������Ƿ��������ַ���ͷ������������ַ���ͷ�򷵻�true�����򷵻�false
	 * 
	 * @param value
	 * @return
	 * @see {@link #getQueryRegex()}
	 * @see {@link #DEFAULT_QUERY_REGEX}
	 */
	public String specialSymbols(String value) throws BusinessException {

		if (value == null || "".equals(value)) {
			throw new BusinessException("�����ַ�У�鴫���������Ϊ��");
		}

		// Pattern pattern = Pattern.compile(getQueryRegex());
		// Matcher matcher = pattern.matcher(value);

		char[] specialSymbols = OutsideUtils.getOutsideInfo("specialSymbols").toCharArray();

		String specia = "";
		for (int i = 0; i < specialSymbols.length; i++) {
			char c = specialSymbols[i];
			if (value.indexOf(c) != -1) {
				specia += c;
			}
		}

		return specia;
	}

	
}
