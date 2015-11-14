package com.ext.maven.plugin.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ext.maven.plugin.compress.CodeCompress;
import com.ext.maven.plugin.filter.Constants;

public class StyleTagChunkCompress extends HtmlChunkCompress {

	public String parserHtmlAndCompress(String html) {
		try {
			String regex_ = "(?i)(<style\\s*type=['|\"]text/css['|\"]\\s*>)([\\s\\S]*)(</style>)";
			Pattern pattern = Pattern.compile(regex_);
			Matcher matcher = pattern.matcher(html);
			StringBuffer sb = new StringBuffer(html.length());
			if (matcher.find()) {
				int index = matcher.start(2);
				int end = matcher.end(2);
				int in = getStyleEndIndex(html, index);

				String cssChunk = matcher.group(2);
				List<HTMLChunkEntity> chunkList = new ArrayList<HTMLChunkEntity>();
				boolean isMuiltChunk = false;
				if (in > 0 && end > in) {
					isMuiltChunk = true;
					int firstChunkEndIndex = getStyleEndIndex(cssChunk, -1);
					chunkList.add(new HTMLChunkEntity(cssChunk.substring(0, firstChunkEndIndex), true));// script.body
					chunkList.add(new HTMLChunkEntity("</style>", false));// first.end
					cssChunk = cssChunk.substring(firstChunkEndIndex + "</style>".length());
					String lastJsBody = parserChildChunk(cssChunk, chunkList, end - firstChunkEndIndex);
					chunkList.add(new HTMLChunkEntity(lastJsBody, true));
					StringBuilder builder = new StringBuilder(html.length());
					for (int i = 0; i < chunkList.size(); i++) {
						if (chunkList.get(i).compressChunk) {
							builder.append(CodeCompress.compressCSS(chunkList.get(i).content));
						} else {
							builder.append(chunkList.get(i).content);
						}
					}
					cssChunk = builder.toString();
				}
				cssChunk = formartChar$(cssChunk);
				if (isMuiltChunk) {
					matcher.appendReplacement(sb, matcher.group(1) + cssChunk + matcher.group(3));
				} else {
					matcher.appendReplacement(sb, matcher.group(1) + CodeCompress.compressCSS(cssChunk) + matcher.group(3));
				}
			}
			matcher.appendTail(sb);
			return sb.toString();
		} catch (Exception ex) {
			Constants.error(html, ex);
		}
		return html;
	}

	private int getStyleEndIndex(String cssChunk, int index) {
		int firstChunkEndIndex = -1;
		if (index == -1) {
			if (cssChunk.matches("(?i)[\\s\\S]*</style>[\\s\\S]*")) {
				firstChunkEndIndex = cssChunk.toLowerCase().indexOf("</style>");
			}
		} else {
			if (cssChunk.matches("(?i)[\\s\\S]*</style>[\\s\\S]*")) {
				firstChunkEndIndex = cssChunk.toLowerCase().indexOf("</style>", index);
			}
		}
		return firstChunkEndIndex;
	}

	private int getStyleStartIndex(String cssChunk) {
		int firstChunkEndIndex = -1;
		if (cssChunk.matches("(?i)[\\s\\S]*(<style\\s*type=['|\"]text/css['|\"]\\s*>)[\\s\\S]*")) {
			firstChunkEndIndex = cssChunk.toLowerCase().indexOf("<style");
		}
		return firstChunkEndIndex;
	}

	private String parserChildChunk(String html, List<HTMLChunkEntity> chunkList, int endIndex) {
		String regxp_ = "(?i)([\\s|\\S]*)(<style\\s*type=['|\"]text/css['|\"]\\s*>)";
		Pattern pattern = Pattern.compile(regxp_);
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			String bodyString = matcher.group(1);
			int body_inner_start_script_index = getStyleStartIndex(bodyString);
			if (body_inner_start_script_index != -1) {// include child
														// script body
				bodyString = parserHtmlAndCompress(bodyString);
			}
			chunkList.add(new HTMLChunkEntity(bodyString, false));
			chunkList.add(new HTMLChunkEntity(matcher.group(2), false));
			if (endIndex > matcher.end(2)) {
				html = html.substring(matcher.end(2));
				int scriptEndIndex = getStyleEndIndex(html, -1);
				if (scriptEndIndex != -1) {
					chunkList.add(new HTMLChunkEntity(html.substring(0, scriptEndIndex), true));
					chunkList.add(new HTMLChunkEntity("</style>", false));
					html = html.substring(html.indexOf("</style>") + "</style>".length());
				}
				return parserChildChunk(html, chunkList, endIndex);
			}
		}
		return html;
	}
}
