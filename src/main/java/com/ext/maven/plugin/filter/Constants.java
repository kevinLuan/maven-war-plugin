package com.ext.maven.plugin.filter;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.maven.plugin.war.util.Logger;
import org.apache.maven.plugin.war.util.StringUtils;

public class Constants {

	private static final String CDN_BASE_URL = "cdn.base.url";
	/**
	 * 压缩JSP页面中的代码块
	 */
	public static final String HTML_COMPRESS_CODE = "html.compress.code";

	/**
	 * #Jsp中Script.src,Style.href,img.src
	 * <p>
	 * 1.增加CDN链接
	 * </p>
	 * <p>
	 * 2.v=\d+版本控制
	 * </p>
	 * */
	private static final String JSP_FILTER = "jsp.filter";

	/* JSP中CSS代码块压缩 */
	private static final String JSP_CSS_CHUNK_FILTER = "jsp.css.chunk.compress.filter";
	/* JSP.JS代码块 */
	private static final String JSP_JS_CHUNK_FILTER = "jsp.js.chunk.compress.filter";

	/* #CSS文件压缩过滤 */
	private static final String CSS_FILE_COMPRESS_FILTER = "css.file.compress.filter";
	/* #Js文件压缩过滤 */
	private static final String JS_FILE_COMPRESS_FILTER = "js.file.compress.filter";
	private static final String FILE_CHINESE_NATIVE2ASCII = "file.chinese.native2ascii";

	private static final String MATCHES_DELETE_FILES = "matches.delete.files";
	public static final String EXCLUDE_JSP_JS_COMPRESS = "@exclude.compress";
	public static final String COMBO_PATH = "combo.path";
	public static final String COMBO_MD5_PATH = "combo.md5.path";
	/* 解析JSP中JS,CSS引用源文件中MD5 */
	public static final String JSP_USE_MD5_FILTER = "jsp.file.md5.filter";
	/**
	 * JSP head 代码注入
	 */
	public static final String HEAD_INJECT_CODE="head.inject.code";
	/**
	 * 匹配注入PATH
	 */
	public static final String HEAD_INJECT_PATH="head.inject.path";
	// basedir:/Users/kevin/workspace/hd
	public static String BASE_DIR = "base_dir";
	// outputDirectory:/Users/kevin/workspace/hd/target
	public static String OUTPUT_DIRECTORY = "outputDirectory";
	// warName:hd
	public static String WAR_NAME = "warName";
	private static Properties PROPERTIES = new Properties();
	/**
	 * 跳过文件
	 */
	private static final String SKIP_FILE = "skip.file";
	public static boolean isInitConf = false;

	public static void put(String key, String value) {
		Logger.info("[" + key + "]:\t" + value);
		PROPERTIES.put(key, value);
	}

	public static boolean isSupportNative2ascii(File file) {
		String fileType = PROPERTIES.getProperty(FILE_CHINESE_NATIVE2ASCII);
		if (fileType != null) {
			for (String type : fileType.split(",")) {
				if (file.getName().endsWith("." + type)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void init(String packageConf) {
		try {
			Constants.isInitConf = true;
			Properties properties = new Properties();
			Reader reader = new FileReader(packageConf);
			properties.load(reader);
			Enumeration<Object> enumeration = properties.keys();
			Logger.info("-------------------------init.config.start-------------------------");
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				Constants.put(key, properties.getProperty(key));
			}
			Logger.info("-------------------------init.config.done-------------------------");
		} catch (Exception ex) {
			Logger.error("init(" + packageConf + ")", ex);
			System.exit(-1);
		}
	}

	/**
	 * 匹配删除文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isMatchesDeleteFile(File file) {
		return isMatchesPath(file, MATCHES_DELETE_FILES);
	}

	/**
	 * 是否匹配JSP
	 * 
	 * @param file
	 * @param context
	 * @return
	 */
	public static boolean isMatchesVersionCDN(File file) {
		return isMatchesPath(file, JSP_FILTER);
	}

	/**
	 * 是否支持JSP.css代码块压缩
	 * 
	 * @param file
	 * @param context
	 * @return
	 */
	public static boolean isMatchesJsp_CssChunkCompress(File file) {
		return isMatchesPath(file, JSP_CSS_CHUNK_FILTER);
	}

	/**
	 * 是否匹配JSP.JS 代码块压缩
	 * 
	 * @param file
	 * @param context
	 * @return
	 */
	public static boolean isMatchesJsp_JsChunkCompress(File file) {
		return isMatchesPath(file, JSP_JS_CHUNK_FILTER);
	}

	/**
	 * 是否匹配CSS文件压缩
	 * 
	 * @param file
	 * @param context
	 * @return
	 */
	public static boolean isMatchesCssCompress(File file) {
		return isMatchesPath(file, CSS_FILE_COMPRESS_FILTER);
	}

	/**
	 * 是否匹配JS文件压缩
	 * 
	 * @param file
	 * @param context
	 * @return
	 */
	public static boolean isMatchesJsFileCompress(File file) {
		return isMatchesPath(file, JS_FILE_COMPRESS_FILTER);
	}

	private static boolean isMatchesPath(File file, String key) {
		if (PROPERTIES.containsKey(key)) {
			String configPath = PROPERTIES.getProperty(key);
			if (StringUtils.isNotEmpty(configPath)) {
				String[] paths = configPath.split("\\|");
				for (int i = 0; i < paths.length; i++) {
					if (paths[i].indexOf(".") != -1) {
						paths[i] = paths[i].replaceAll("\\.", "\\\\\\.");
					}
					if (paths[i].indexOf("*") != -1) {
						paths[i] = paths[i].replaceAll("\\*", ".*");
					}
					if (file.getAbsolutePath().matches(PROPERTIES.getProperty(BASE_DIR) + paths[i])) {
						return true;
					} else if (file.getAbsolutePath().startsWith(PROPERTIES.getProperty(BASE_DIR) + paths[i])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String getCdnBaseURL() {
		if (PROPERTIES.containsKey(CDN_BASE_URL)) {
			return PROPERTIES.getProperty(CDN_BASE_URL);
		} else {
			return "http://cdn.net";
		}
	}

	public static void error(Object object, Exception ex) {
		System.err.println(object);
		ex.printStackTrace();
	}

	public static void error(Object object) {
		System.err.println(object);
	}

	/***
	 * 匹配HTML压缩
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isMatchesHTMLCompress(File source) {
		return isMatchesPath(source, HTML_COMPRESS_CODE);
	}

	public static String getProperty(String key) {
		return PROPERTIES.getProperty(key);
	}

	public static boolean isMatchesCombo(File source) {
		return isMatchesPath(source, COMBO_PATH);
	}

	public static boolean isMatchesMD5FileName(File source) {
		return isMatchesPath(source, JSP_USE_MD5_FILTER);
	}
	
	public static boolean isSkip(File source) {
	    return isMatchesPath(source, SKIP_FILE);
	}
	/**
	 * 是否匹配head注入
	 * @param source
	 * @return
	 */
	public static boolean isMatchesHeadInjectCode(File source){
	  return isMatchesPath(source, HEAD_INJECT_PATH);
	}
}
