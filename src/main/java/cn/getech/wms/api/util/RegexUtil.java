package cn.getech.wms.api.util;

import cn.hutool.core.bean.BeanUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具
 * @author huangjc
 * @date 2017年3月24日
 */
public final class RegexUtil {

	private RegexUtil() {
	}

	/**
	 * 是否是正整数（不包含0）
	 * @param str
	 * @return
	 */
	public static boolean IsIntNumber(String str) {
		String regex = "^\\+?[1-9][0-9]*$";
		return match(regex, str);
	}

	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 替换特殊字符
	 * @param str 源字符串
	 * @return
	 */
	public static Object replaceSpecialStr(Object obj) {
		if (obj != null &&  obj instanceof  String) {
			String repStr = "";
			Pattern p = Pattern.compile("[\t\r\n]");
			Matcher m = p.matcher((String) obj);
			repStr = m.replaceAll("");
			return repStr.trim();
		}
		return obj;
	}

	/**
	 * 替换身份证特殊字符
	 * @param IDNumber 身份证号
	 * @return
	 */
	public static String replaceIDNumber(String IDNumber) {
		String repStr = "";
		if (IDNumber != null) {
			Pattern p = Pattern.compile("[^\\dXx]");
			Matcher m = p.matcher(IDNumber);
			repStr = m.replaceAll("");
		}
		return repStr.trim().toUpperCase();
	}

	/**
	 * 校验是否身份证
	 * @param IDNumber 身份证号
	 * @return
	 */
	public static boolean isIDNumber(String IDNumber) {
		if (StringUtils.isEmpty(IDNumber)) {
			return false;
		}
		// 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
		String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
				"(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

		// 假设18位身份证号码:41000119910101123X  410001 19910101 123X
		// ^开头
		// [1-9] 第一位1-9中的一个      4
		// \\d{5} 五位数字           10001（前六位省市县地区）
		// (18|19|20)                19（现阶段可能取值范围18xx-20xx年）
		// \\d{2}                    91（年份）
		// ((0[1-9])|(10|11|12))     01（月份）
		// (([0-2][1-9])|10|20|30|31)01（日期）
		// \\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
		// [0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
		// $结尾

		// 假设15位身份证号码:410001910101123  410001 910101 123
		// ^开头
		// [1-9] 第一位1-9中的一个      4
		// \\d{5} 五位数字           10001（前六位省市县地区）
		// \\d{2}                    91（年份）
		// ((0[1-9])|(10|11|12))     01（月份）
		// (([0-2][1-9])|10|20|30|31)01（日期）
		// \\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
		// $结尾

		boolean matches = IDNumber.matches(regularExpression);

		//判断第18位校验值
		if (matches) {

			if (IDNumber.length() == 18) {
				try {
					char[] charArray = IDNumber.toCharArray();
					//前十七位加权因子
					int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
					//这是除以11后，可能产生的11位余数对应的验证码
					String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
					int sum = 0;
					for (int i = 0; i < idCardWi.length; i++) {
						int current = Integer.parseInt(String.valueOf(charArray[i]));
						int count = current * idCardWi[i];
						sum += count;
					}
					char idCardLast = charArray[17];
					int idCardMod = sum % 11;
					if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
						return true;
					} else {
						System.out.println("身份证[" + IDNumber + "]最后一位：["
								+ String.valueOf(idCardLast).toUpperCase() + "]错误，正确的应该是：["
								+ idCardY[idCardMod].toUpperCase() + "]");
						return false;
					}

				} catch (Exception e) {
					System.out.println("身份证[IDNumber=" + IDNumber + "]校验异常：" + e.getMessage());
					return false;
				}
			}

		}
		return matches;
	}
	
	/*	@Test
	public void test() {
		String str = "815840230964 ";
		System.out.println(RegexUtil.trimAll(str));
	}*/

	/**
	 * 去除字符串所有的空格
	 * @author zbc
	 * @since 2018年9月10日 下午5:20:02
	 * @param str
	 * @return
	 */
	public static String trimAll(String str) {
		if(StringUtils.isEmpty(str)){
			return str;
		}
		//\u00A0 的含义实际上是指不间断的空格, 实际上我们常用的空格应该的转义序列应该是\u0020
		String eStr = gbEncoding(str).replaceAll("\\\\u00a0", "");
		String dStr = decodeUnicode(eStr);
		return dStr.replaceAll("\\s", "");
	}

	public static String gbEncoding(final String gbString) { // gbString = "测试"
		char[] utfBytes = gbString.toCharArray(); // utfBytes = [测, 试]
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]); // 转换为16进制整型字符串
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	public static String decodeUnicode(final String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	public static String templateParse(Object obj, String template) {
		Map<String, Object> map = BeanUtil.beanToMap(obj);
		return templateMapParse(map, template);
	}

	public static String templateMapParse(Map<String, Object> map,String template) {
		//生成匹配模式的正则表达式
		String patternString = "\\$\\{(" + StringUtils.join(map.keySet(), "|") + ")\\}";

		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(template);

		//两个方法：appendReplacement, appendTail
		StringBuffer sb = new StringBuffer();
		String value = null;
		while (matcher.find()) {
			value = Objects.isNull(map.get(matcher.group(1))) ? "" : String.valueOf(map.get(matcher.group(1)));
			matcher.appendReplacement(sb, value);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
