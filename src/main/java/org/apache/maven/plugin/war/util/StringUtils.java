package org.apache.maven.plugin.war.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符处理工具
 * 
 * @author SHOUSHEN LUAN
 * 
 */
public class StringUtils {

	public static String chineseToUnicode(String str) {
		if (null == str)
			return null;
		char[] arrChar = str.trim().toCharArray();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < arrChar.length; i++) {
			if ((0x7F < arrChar[i] || 0xa > arrChar[i]) && 9 != arrChar[i])
				buf.append("\\u").append(Integer.toHexString((int) arrChar[i]));
			else
				buf.append(arrChar[i]);
		}
		return buf.toString();
	}

	/**
	 * 过滤特殊字符
	 * 
	 * @param text
	 *            需要处理的字符串
	 * @return
	 */
	public static String cleanInvalidXmlChars(String text) {
		if (null == text) {
			return " ";
		}
		if (text.indexOf("&") != -1 && text.indexOf("&amp;") == -1) {
			text = text.replaceAll("&", "&amp;");
		}
		/*
		 * 放过中文及全角字符
		 * \\u3000-\\u301e\\ufe10-\\ufe19\\ufe30-\\ufe44\\ufe50-\\ufe6b
		 * \\uff01-\\uffee
		 */
		String re = "[^\\x0D\\x20-\\xD7FF\\xE000-\\xFFFD\\x10000-x10FFFF\\u4e00-\\u9fa5\\u3000-\\u301e\\ufe10-\\ufe19\\ufe30-\\ufe44\\ufe50-\\ufe6b\\uff01-\\uffee]";
		return text.replaceAll(re, "");
	}

	/**
	 * 完成对指定字符串的奇偶位互换 如: 12AB return 21BA
	 * 
	 * @param str指定字符串
	 * @return 奇偶位互换后的字符串
	 */
	public static String swapEvenOdd(String str) {
		if (str == null || str.length() == 0)
			return str;
		int i = 0;
		StringBuffer sBuffer = new StringBuffer(str.length());
		int iLength = str.length();
		float fLength = str.length();
		boolean isO = true;
		if (iLength / 2 != fLength / 2) {
			str += "x";
			isO = false;
		}
		for (i = 0; i < fLength / 2; i++) {
			sBuffer.append(str.substring(2 * i + 1, 2 * i + 2));
			sBuffer.append(str.substring(2 * i, 2 * i + 1));
		}
		String res = sBuffer.toString();
		if (!isO) {
			res = res.substring(0, iLength - 1) + res.substring(iLength, iLength + 1);
		}
		return res;
	}

	/**
	 * 截取传入字符从制定长度到制定长度字符
	 * 
	 * @param str
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public static String substring(String str, int beginIndex, int endIndex) {
		if (isNotEmpty(str)) {
			if (str.length() > endIndex && str.length() > beginIndex) {
				return str.substring(beginIndex, endIndex);
			}
		}
		return str;
	}

	/**
	 * 截取字符指定长度
	 * 
	 * @param str
	 *            字符
	 * @param length
	 *            长度（从前面截取）
	 * @return
	 */
	public static String subStringByLength(String str, int length) {
		if (isNotEmpty(str) && str.length() > length) {
			return str.substring(0, length);
		}
		return str;
	}

	/**
	 * 截取字符指定长度
	 * 
	 * @param str
	 *            字符
	 * @param length
	 *            长度（从前面截取）
	 * @param lastAppendStr
	 *            如果需要截取的话讲在之后追加字符
	 * @return
	 */
	public static String subStringByLength(String str, int length, String lastAppendStr) {
		if (isNotEmpty(str) && str.length() > length) {
			return str.substring(0, length) + lastAppendStr;
		}
		return str;
	}

	/**
	 * 截取制定长度字符
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String intercept(String str, int length) {
		if (isNotEmpty(str)) {
			if (str.length() > length) {
				return str.substring(0, length);
			}
		}
		return str;
	}

	/**
	 * 剔除回车|换行|回车并换行|字表符
	 * 
	 * @param str
	 * @return
	 */
	public static String clearInvalid(String str) {
		if (!isNotEmpty(str)) {
			return str.replaceAll("\r\n|\r|\n|\t", "");
		} else {
			return str;
		}
	}

	public static boolean isEmpty(String str) {
		return !isNotEmpty(str);
	}

	public static boolean isNotEmpty(String str) {
		if (str != null && str.trim().length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String... str) {
		for (int i = 0; i < str.length; i++) {
			if (isNotEmpty(str[i])) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 剔除字符中所有空格
	 * 
	 * @param str
	 * @return
	 */
	public final static String clearSapce(String str) {
		Pattern p = Pattern.compile("\\s*");
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	/**
	 * 判断是否是中文
	 * 
	 * @param chat
	 * @return
	 */
	public final static boolean isChinese(char chat) {
		return (Character.toString(chat).matches("[\\u4E00-\\u9FA5]+"));
	}

	/**
	 * 判断字符是否为字母A-Z a-z
	 * 
	 * @param chat
	 * @return
	 */
	public final static boolean isA_Z(char chat) {
		if (chat >= 65 && chat <= 90) {// 小写字符
			return true;
		} else if (chat >= 97 && chat <= 122) {// 大写字符
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param chat
	 * @return
	 */
	public final static boolean isNum(char chat) {
		return chat > 47 && chat < 58;
	}

	/**
	 * 清除无效字符:清除除：字母,数字,中文,_以外的字符
	 * 
	 * @param fileName
	 * @return
	 */
	public static String clearValidChat(String fileName) {
		String reg = "[^a-z|A-Z|0-9|_|\\u4E00-\\u9FA5|\\.]+";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find())
			return matcher.replaceAll("");
		return fileName;
	}

	private static final String tags = "link|script|img";
	private static final String regxp = "(?i)<\\s*(" + tags + ")\\s+([^>]*)\\s*>";
	private static Pattern pattern = Pattern.compile(regxp);

	public static String filterHTML(String html, String baseURL) {
		try {
			if (isNotEmpty(html)) {
				Matcher matcher = pattern.matcher(html);
				StringBuffer sb = new StringBuffer(html.length());
				boolean result = matcher.find();
				while (result) {
					if (isNotEmpty(baseURL)) {
						String src = matcher.group(0);
						String target = formatRegexChar$(appendBaseUrl(src, baseURL));
						target = versionControl(target);
						if (target.equals(src) == false) {
							matcher.appendReplacement(sb, target);
						}
					} else {
						String src = formatRegexChar$(matcher.group(0));
						String target = versionControl(src);
						if (target.equals(src) == false) {
							matcher.appendReplacement(sb, target);
						}
					}
					result = matcher.find();
				}
				matcher.appendTail(sb);
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}

	private static String formatRegexChar$(String str) {
		if (str.indexOf("$") != -1) {
			return str.replaceAll("\\$", "\\\\\\$");
		}
		return str;
	}

	private static String versionControl(String str) {
		if (str.matches("(?i).*v=(\\d+).*")) {
			return str.replaceAll("v=(\\d+)", "v=" + System.currentTimeMillis());
		}
		return str;
	}

	private static final String REGEX_URL = "(?i)<\\s*.*?\\s+(src|href)=[\"'](.*?)[\"'].*?\\s*/?>";
	private static final Pattern pattern_replace = Pattern.compile(REGEX_URL);

	private static String appendBaseUrl(String htmlTag, String baseURL) {
		try {
			if (isNotEmpty(htmlTag, baseURL)) {
				Matcher matcher = pattern_replace.matcher(htmlTag);
				StringBuffer sb = new StringBuffer(htmlTag.length() + baseURL.length());
				boolean result = matcher.find();
				while (result) {
					String matcherSrc = matcher.group(0);
					String url = matcher.group(2);
					if (url.startsWith("/")) {
						matcherSrc = matcherSrc.replace(url, baseURL + url);
						matcherSrc = versionControl(matcherSrc);
						matcherSrc = formatRegexChar$(matcherSrc);
						matcher.appendReplacement(sb, matcherSrc);
					}
					result = matcher.find();
				}
				matcher.appendTail(sb);
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlTag;
	}

	public static boolean isExistsNotEmpty(String... strs) {
		for (int i = 0; i < strs.length; i++) {
			if (StringUtils.isNotEmpty(strs[i])) {
				return true;
			}
		}
		return false;
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}
}
