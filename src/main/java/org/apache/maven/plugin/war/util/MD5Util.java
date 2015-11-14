package org.apache.maven.plugin.war.util;

import java.security.MessageDigest;

public final class MD5Util {
	protected final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(input.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	protected static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	protected static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static void main(String[] args) {
		System.out.println("40664f04f401384867f7730815ac6a12");
		System.out.println(getMD5("98:D6:BB:49:E4:02"));
		
	}

}
