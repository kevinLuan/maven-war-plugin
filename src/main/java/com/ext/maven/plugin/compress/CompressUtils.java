package com.ext.maven.plugin.compress;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CompressUtils {

	public static void checkIllegalityWord(String code) {
		String[] gjz = new String[] { "int", "long", "char", "byte", "float", "double", "short" };
		for (int i = 0; i < gjz.length; i++) {
			if (code.matches("[\\s\\S]*[\t| |\n|\r|=]" + gjz[i] + "[\t| |\n|\r|=][\\s\\S]*")) {
				throw new SyntaxErrorException("syntax errors.exists illegality word[" + gjz[i] + "]");
			}
		}
	}

	public static void checkJsFileIllegalityWord(File file) throws IOException {
		String code = read(file);
		checkIllegalityWord(code);
	}

	public static String read(File srcFile) throws IOException {
		FileInputStream fis = null;
		DataInputStream dis = null;
		try {
			fis = new FileInputStream(srcFile);
			dis = new DataInputStream(fis);
			byte[] b = new byte[(int) srcFile.length()];
			dis.read(b, 0, b.length);
			return new String(b, "UTF-8");
		} catch (FileNotFoundException ex) {
			throw ex;
		} finally {
			if (dis != null) {
				dis.close();
			}
			dis = null;
			if (fis != null) {
				fis.close();
			}
		}
	}
}
