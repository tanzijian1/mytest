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
 *         特殊字符检验Util
 * 
 */
public class SpecialSymbols {

	/**
	 * 判断是否含有特殊字符
	 * 
	 * @param str
	 * @return true为包含，false为不包含
	 */
	public static boolean isSpecialChar(String str) {
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 判断查询参数中是否以特殊字符开头，如果以特殊字符开头则返回true，否则返回false
	 * 
	 * @param value
	 * @return
	 * @see {@link #getQueryRegex()}
	 * @see {@link #DEFAULT_QUERY_REGEX}
	 */
	public String specialSymbols(String value) throws BusinessException {

		if (value == null || "".equals(value)) {
			throw new BusinessException("特殊字符校验传入参数不能为空");
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
