package com.ext.maven.plugin.compress;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;

import com.ext.maven.plugin.filter.Constants;
import com.ext.maven.plugin.filter.CopyJsp;

public class CodeCompress {
	public static String compressCSS(String code) {
		try {
			return CSSCompress.compress(code);
		} catch (Exception e) {
			Constants.error(code, e);
		}
		return code;
	}

	public static String compressJS(String code) {
		try {
			code = code.replaceAll("\\\\\\$", "\\$");
			code = JavaScriptCompress.compress(code);
		} catch (Exception ex) {
			if (CopyJsp.srcFile != null) {
				System.out.println("Copy.jsp.js[error]:" + CopyJsp.srcFile.getAbsolutePath());
			}
			Constants.error(code, ex);
		}
		return code;
	}

	public static void compressJs(File jsFile) {
		try {
			FileReader reader = new FileReader(jsFile);
			String code = JavaScriptCompress.compress(reader);
			FileUtils.writeStringToFile(jsFile, code);
		} catch (Exception e) {
			Constants.error(jsFile.getAbsolutePath(), e);
		}
	}

	public static void compressCss(File cssFile) {
		try {
			FileReader reader = new FileReader(cssFile);
			String code = CSSCompress.compress(reader);
			FileUtils.writeStringToFile(cssFile, code);
		} catch (Exception e) {
			Constants.error(cssFile.getAbsolutePath(), e);
		}
	}
}
