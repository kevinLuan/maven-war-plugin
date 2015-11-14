package com.ext.maven.plugin.compress;

import java.io.IOException;

import com.ext.maven.plugin.filter.Constants;

public class HTMLCompress {
	public static final String START_TOKEN = "<!--COMPRESS.CODE.START-->";
	public static final String END_TOKEN = "<!--COMPRESS.CODE.END-->";
	private static CSSCompress cssCompress = new CSSCompress();

	@SuppressWarnings("static-access")
	public static String compressCode(String html){
		try {
			int startIndex = html.indexOf(START_TOKEN);
			if (startIndex >= 0) {
				int endIndex = html.indexOf(END_TOKEN, startIndex);
				if (endIndex == -1) {
					String startCode = html.substring(0, startIndex);
					String body = html.substring(startIndex
							+ START_TOKEN.length());
					html = startCode + cssCompress.compress(body);
					return html;
				} else {
					String startCode = html.substring(0, startIndex);
					String endCode = html.substring(endIndex
							+ END_TOKEN.length());
					String body = html.substring(
							startIndex + START_TOKEN.length(), endIndex);
					html = startCode + cssCompress.compress(body);
					return html + compressCode(endCode);
				}
			}
		} catch (IOException ex) {
			Constants.error(html, ex);
		}
		return html;
	}
}
