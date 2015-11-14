package org.apache.maven.plugin.combo;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.model.TagEntity;
import org.apache.maven.plugin.war.util.MD5Util;

import com.ext.maven.plugin.filter.Constants;

public class ComboUtils {
	public static String getMd5File(List<TagEntity> jsList) {
		StringBuilder builder = new StringBuilder(200);
		for (int i = 0; i < jsList.size(); i++) {
			builder.append("url:" + jsList.get(i).getUrl() + ",length:" + jsList.get(i).getFile().length() + "|");
		}
		return MD5Util.getMD5(builder.toString());
	}

	public static String getProjectPath() {
		String projectPath = Constants.getProperty(Constants.BASE_DIR);
		return projectPath;
	}

	public static String getMD5PATH() {
		return getProjectPath() + md5RelativePath();
	}

	public static String md5RelativePath() {
		String relativePath=Constants.getProperty(Constants.COMBO_MD5_PATH);
		if(relativePath!=null){
			return relativePath;
		}
		return "";
	}

	public static String getAbsolutePath(File file, String url) {
		if (url.startsWith("/")) {
			return getProjectPath() + url;
		} else {
			if (url.startsWith("./")) {
				return file.getParentFile().getAbsolutePath() + url.substring(1);
			} else if (url.startsWith("../")) {
				file = file.getParentFile();
				do {
					url = url.substring(3);
					file = file.getParentFile();
				} while (url.startsWith("../"));
				return file.getAbsolutePath() + "/" + url;
			} else if (url.startsWith("http://") || url.startsWith("https://")) {
				return url;
			} else {
				return file.getParent() + "/" + url;
			}
		}
	}

	public static String getComboJs(String url) {
		return "<script src=\"" + url + "\" charset=\"utf-8\"></script>";
	}

	public static String getComboCss(String url) {
		return "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + url + "\" charset=\"utf-8\"/>";
	}

	public static String versionControl(String str) {
		if (str.matches("(?i).*v=(\\d+).*")) {
			return str.replaceAll("v=(\\d+)", "v=" + System.currentTimeMillis());
		}
		return str;
	}

	public static String getPrefixDescription(TagEntity tagEntity) {
		return getPrefixDescription(tagEntity.getUrl());
	}

	public static String getPrefixDescription(String file) {
		return "/*file:" + file + "*/";
	}
}
