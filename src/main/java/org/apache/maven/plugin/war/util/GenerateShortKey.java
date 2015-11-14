package org.apache.maven.plugin.war.util;

public class GenerateShortKey {
	private static final String sign = "8e2e2df910f844bdbcb531170c75b246";
	private static final String[] CHARS = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
			"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", };

	public static String shortStr(String input) {
		String hex = MD5Util.getMD5(sign + input);
		StringBuilder builder = new StringBuilder(24);
		for (int i = 0; i < 4; i++) {
			String str = hex.substring(i * 8, i * 8 + 8);
			long lHexLong = 0x3FFFFFFF & Long.parseLong(str, 16);
			for (int j = 0; j < 6; j++) {
				long index = CHARS.length - 1 & lHexLong;
				builder.append(CHARS[(int) index]);
				lHexLong = lHexLong >> 5;
			}
		}
		return builder.substring(0, 8);
	}
}
