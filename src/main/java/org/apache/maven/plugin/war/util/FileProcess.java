package org.apache.maven.plugin.war.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileProcess {
	public static void chineseTransformToUncode(File file) throws IOException {
		String code = FileUtils.readFileToString(file, "UTF-8");
		code = StringUtils.chineseToUnicode(code);
		FileUtils.write(file, code, "UTF-8");
	}

	public static void writeFile(File file, String code) throws IOException {
		code = StringUtils.chineseToUnicode(code);
		file.getParentFile().mkdirs();
		FileUtils.write(file, code, "UTF-8");
	}
}
