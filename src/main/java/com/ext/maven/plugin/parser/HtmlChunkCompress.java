package com.ext.maven.plugin.parser;

import com.ext.maven.plugin.filter.Constants;

public abstract class HtmlChunkCompress {
	public abstract String parserHtmlAndCompress(String html);

	String formartChar$(String code) {
		if (code.indexOf("$") != -1) {
			code = code.replaceAll("\\$", "\\\\\\$");
		}
		return code;
	}
	/**
	 * 排除压缩处理块
	 * @param code
	 * @return
	 */
	protected boolean isExclusion(String code) {
		return code.indexOf(Constants.EXCLUDE_JSP_JS_COMPRESS) != -1;
	}

	class HTMLChunkEntity {
		public HTMLChunkEntity(String content, boolean compressChunk) {
			this.compressChunk = compressChunk;
			this.content = content;
		}

		protected final boolean compressChunk;
		protected final String content;
	}
}
