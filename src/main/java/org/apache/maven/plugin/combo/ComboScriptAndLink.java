package org.apache.maven.plugin.combo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.model.ScriptLinkTagEntity;
import org.apache.maven.plugin.model.TagEntity;
import org.apache.maven.plugin.war.util.StringUtils;
import org.apache.maven.plugin.war.util.TagParser;

public class ComboScriptAndLink {
	private static final String TAGS = "link|script";
	private static final String REGX_TAG1 = "(?i)<\\s*(" + TAGS + ")\\s+([^>]*)\\s*></(" + TAGS + ")>";
	private static Pattern PATTERN1 = Pattern.compile(REGX_TAG1);
	private static final String REGXP_TAG2 = "(?i)<\\s*(" + TAGS + ")\\s+([^>]*)\\s*/>";
	private static Pattern PATTERN2 = Pattern.compile(REGXP_TAG2);

	public static String comboCssJs(String html, List<TagEntity> cssList, List<TagEntity> jsList) {
		try {
			if (StringUtils.isNotEmpty(html)) {
				return process(html, cssList, jsList, PATTERN2, PATTERN1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}

	private static String process(String html, List<TagEntity> cssList, List<TagEntity> jsList, Pattern... patterns) {
		String code = html;
		for (int i = 0; i < patterns.length; i++) {
			Matcher matcher = patterns[i].matcher(code);
			StringBuffer sb = new StringBuffer(code.length());
			boolean result = matcher.find();
			while (result) {
				String src = matcher.group(0);
				ScriptLinkTagEntity target = parserTag(src);
				if (target.isIgnore() == false && target.getUrl() != null) {
					if (target.isLink() && target.isRemoteFile() == false) {
						cssList.add(target);
					} else if (target.isScript() && target.isRemoteFile() == false) {
						jsList.add(target);
					}
					if (target.getId() != null || target.isRemoteFile()) {
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

	public static String versionControl(String str) {
		if (str.matches("(?i).*v=(\\d+).*")) {
			return str.replaceAll("v=(\\d+)", "v=" + System.currentTimeMillis());
		}
		return str;
	}

	public static ScriptLinkTagEntity parserTag(String htmlTag) {
		ScriptLinkTagEntity entity = new ScriptLinkTagEntity();
		entity.setCode(htmlTag);
		try {
			if (StringUtils.isNotEmpty(htmlTag)) {
				TagParser tag = TagParser.parser(htmlTag);
				if ("script".equalsIgnoreCase(tag.getTagName())) {
					entity.setScript(true);
					entity.setUrl(tag.getAttr("src"));
				} else if ("link".equalsIgnoreCase(tag.getTagName())) {
					entity.setStyle(true);
					entity.setUrl(tag.getAttr("href"));
				}
				entity.setId(tag.getAttr("id"));
				entity.setIgnore("ignore".equalsIgnoreCase(String.valueOf(tag.getAttr("combo")).trim()));
				entity.setCompress(tag.getAttr("compress"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
}
