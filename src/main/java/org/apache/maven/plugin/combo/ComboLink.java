package org.apache.maven.plugin.combo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.model.TagEntity;
import org.apache.maven.plugin.war.util.StringUtils;

import com.ext.maven.plugin.filter.Constants;

public class ComboLink {
	private static final String REGX_TAG1 = "(?i)<\\s*link\\s+([^>]*)\\s*></link>";
	private static Pattern PATTERN1 = Pattern.compile(REGX_TAG1);
	private static final String REGXP_TAG2 = "(?i)<\\s*link\\s+([^>]*)\\s*/>";
	private static Pattern PATTERN2 = Pattern.compile(REGXP_TAG2);

	public static String comboLink(String html, List<TagEntity> cssList) {
		try {
			if (StringUtils.isNotEmpty(html)) {
				return process(html, cssList, PATTERN2, PATTERN1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}

	private static String process(String html, List<TagEntity> cssList, Pattern... patterns) {
		String code = html;
		for (int i = 0; i < patterns.length; i++) {
			Matcher matcher = patterns[i].matcher(code);
			StringBuffer sb = new StringBuffer(code.length());
			boolean result = matcher.find();
			while (result) {
				String src = matcher.group(0);
				TagEntity target = ComboScriptAndLink.parserTag(src);
				if (target.isIgnore()==false && target.getUrl() != null) {
					if (target.isLink() && target.isRemoteFile() == false) {
						cssList.add(target);
					}
					if (target.getUrl() == null || target.getId() != null || target.isRemoteFile()) {
						matcher.appendReplacement(sb, src);
					} else {
						matcher.appendReplacement(sb, "");
					}
				}
				result = matcher.find();
			}
			matcher.appendTail(sb);
			code = sb.toString();
		}
		return code;
	}

	protected static String versionControl(String str) {
		if (str.matches("(?i).*v=(\\d+).*")) {
			return str.replaceAll("v=(\\d+)", "v=" + System.currentTimeMillis());
		}
		return str;
	}

	private static final String REGEX_CSS_URL = "url\\([\"']?(.*?)[\"']?\\)";
	private static final Pattern PATTERN_CSS_URL = Pattern.compile(REGEX_CSS_URL);

	/**
	 * 处理Css文件中的URL引用相对路径处理
	 * 
	 * @param code
	 * @param filePath
	 * @return
	 */
	public static String processCssURL(String code, File filePath) {
		Matcher matcher = PATTERN_CSS_URL.matcher(code);
		StringBuffer sb = new StringBuffer(code.length());
		while (matcher.find()) {
			String url = matcher.group(1);
			/*处理.css中background:url(data:image/png;base64,iVBORxxxxx*/
			if(url.startsWith("data:image/")){
				continue;
			}
			url = ComboUtils.getAbsolutePath(filePath, url);
			if (url.startsWith(ComboUtils.getProjectPath())) {
				url =Constants.getCdnBaseURL()+ url.substring(ComboUtils.getProjectPath().length());
			}
			// TODO 需要将得到的绝对路径处理成可以远程访问的路径即可
			matcher.appendReplacement(sb, "url('" + url + "')");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static final String REGEX_CSS_IMPORT = "@import\\s+url\\([\"']?(.*?)[\"']?\\)";
	private static final Pattern PATTERN_IMPORT_URL = Pattern.compile(REGEX_CSS_IMPORT);

	public static String processImportUrl(String code, File file) throws IOException {
		Matcher matcher = PATTERN_IMPORT_URL.matcher(code);
		StringBuffer sb = new StringBuffer(code.length());
		while (matcher.find()) {
			String relative = matcher.group(1);
			String url = ComboUtils.getAbsolutePath(file, relative);
			url = TagEntity.parserCssURL(url);
			File importFile = new File(url);
			String includeCss = FileUtils.readFileToString(importFile);
			includeCss = processCssURL(includeCss, importFile);
			includeCss = processImportUrl(includeCss, importFile);
			// include import css
			includeCss = ComboUtils.getPrefixDescription(relative) + includeCss;
			matcher.appendReplacement(sb, includeCss);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
