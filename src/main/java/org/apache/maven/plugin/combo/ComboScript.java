package org.apache.maven.plugin.combo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.model.TagEntity;
import org.apache.maven.plugin.war.util.StringUtils;

public class ComboScript {
	private static final String REGX_TAG1 = "(?i)<\\s*script\\s+([^>]*)\\s*></script>";
	private static Pattern PATTERN1 = Pattern.compile(REGX_TAG1);
	private static final String REGXP_TAG2 = "(?i)<\\s*script\\s+([^>]*)\\s*/>";
	private static Pattern PATTERN2 = Pattern.compile(REGXP_TAG2);

	public static String comboScript(String html, List<TagEntity> jsList) {
		try {
			if (StringUtils.isNotEmpty(html)) {
				return process(html, jsList, PATTERN2, PATTERN1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}

	private static String process(String html, List<TagEntity> jsList, Pattern... patterns) {
		String code = html;
		for (int i = 0; i < patterns.length; i++) {
			Matcher matcher = patterns[i].matcher(code);
			StringBuffer sb = new StringBuffer(code.length());
			boolean result = matcher.find();
			while (result) {
				String src = matcher.group(0);
				TagEntity target = ComboScriptAndLink.parserTag(src);
				if (target.isIgnore()==false && target.getUrl() != null) {
					if (target.isScript() && target.isRemoteFile() == false) {
						jsList.add(target);
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
}
