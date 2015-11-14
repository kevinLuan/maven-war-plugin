package com.ext.maven.plugin.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ext.maven.plugin.compress.CodeCompress;
import com.ext.maven.plugin.filter.Constants;
import com.ext.maven.plugin.filter.CopyJsp;

public class ScriptTagChunkCompress_old extends HtmlChunkCompress {
	public String parserHtmlAndCompress(String html) {

		String debug_error_data = null;
		try {
			String regxp_ = "(?i)(<script\\s*type=['|\"]text/javascript['|\"]\\s*>)([\\s|\\S]*)(</script>)";
			Pattern pattern = Pattern.compile(regxp_);
			Matcher matcher = pattern.matcher(html);
			StringBuffer sb = new StringBuffer(html.length());
			if (matcher.find()) {
				int index = matcher.start(2);
				int end = matcher.end(2);
				int in = getScriptEndIndex(html, index);

				String jsChunk = matcher.group(2);
				List<HTMLChunkEntity> chunkList = new ArrayList<HTMLChunkEntity>();
				boolean isMuiltChunk = false;
				if (in > 0 && end > in) {
					isMuiltChunk = true;
					int firstChunkEndIndex = getScriptEndIndex(jsChunk, -1);
					chunkList.add(new HTMLChunkEntity(jsChunk.substring(0, firstChunkEndIndex), true));// script.body
					chunkList.add(new HTMLChunkEntity("</script>", false));// first.end
					jsChunk = jsChunk.substring(firstChunkEndIndex + "</script>".length());
					String lastJsBody = parserChildChunk(jsChunk, chunkList, end - firstChunkEndIndex);
					chunkList.add(new HTMLChunkEntity(lastJsBody, isScriptStartTag(lastJsBody)));
					StringBuilder builder = new StringBuilder(html.length());
					for (int i = 0; i < chunkList.size(); i++) {
						if (chunkList.get(i).compressChunk) {
							if(isExclusion(chunkList.get(i).content)){
								builder.append(chunkList.get(i).content);
							}else{
								builder.append(CodeCompress.compressJS(chunkList.get(i).content));
							}
						} else {
							builder.append(chunkList.get(i).content);
						}
					}
					jsChunk = builder.toString();
				}
				if (isMuiltChunk) {
					String out = matcher.group(1) + jsChunk + matcher.group(3);
					debug_error_data = out;
					out = formartChar$(out);
					out=processZYChar(out);
					matcher.appendReplacement(sb, out);
				} else {
					if(isExclusion(jsChunk)==false){
						jsChunk=CodeCompress.compressJS(jsChunk);
					}
					String out = matcher.group(1) + jsChunk + matcher.group(3);
					debug_error_data = out;
					out = formartChar$(out);
					out=processZYChar(out);
					matcher.appendReplacement(sb, out);
				}
			}
			matcher.appendTail(sb);
			return sb.toString();
		} catch (Exception ex) {
			if (CopyJsp.srcFile != null) {
				Constants.error("Copy.jsp.js[error]:" + CopyJsp.srcFile.getAbsolutePath());
			}
			Constants.error(debug_error_data, ex);
		}
		return html;
	}
	
	private String processZYChar(String code){
		code=code.replaceAll("\\\"", "\\\\\"");
		code=code.replaceAll("\\\'", "\\\\\'");
		return code;
	}
	private boolean isScriptStartTag(String lastJsBody) {
		if (lastJsBody.replaceAll("\n|\r|\t", "").trim().matches("(?i)<script [\\s\\S]*")) {
			return false;
		}
		return true;
	}

	private static int getScriptEndIndex(String jsChunk, int index) {
		int firstChunkEndIndex = -1;
		if (index == -1) {
			if (jsChunk.matches("(?i)[\\s\\S]*</script>[\\s\\S]*")) {
				firstChunkEndIndex = jsChunk.toLowerCase().indexOf("</script>");
			}
		} else {
			if (jsChunk.matches("(?i)[\\s\\S]*</script>[\\s\\S]*")) {
				firstChunkEndIndex = jsChunk.toLowerCase().indexOf("</script>", index);
			}
		}
		return firstChunkEndIndex;
	}

	private int getScriptStartIndex(String jsChunk) {
		int firstChunkEndIndex = -1;
		if (jsChunk.matches("(?i)[\\s\\S]*(<script\\s*type=['|\"]text/javascript['|\"]\\s*>)[\\s\\S]*")) {
			firstChunkEndIndex = jsChunk.toLowerCase().indexOf("<script");
		}
		return firstChunkEndIndex;
	}

	private String parserChildChunk(String html, List<HTMLChunkEntity> chunkList, int endIndex) {
		String regxp_ = "(?i)([\\s|\\S]*)(<script\\s*type=['|\"]text/javascript['|\"]\\s*>)";
		Pattern pattern = Pattern.compile(regxp_);
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			String bodyString = matcher.group(1);
			int body_inner_start_script_index = getScriptStartIndex(bodyString);
			if (body_inner_start_script_index != -1) {// include child
														// script body
				bodyString = parserHtmlAndCompress(bodyString);
			}
			chunkList.add(new HTMLChunkEntity(bodyString, false));
			chunkList.add(new HTMLChunkEntity(matcher.group(2), false));
			if (endIndex > matcher.end(2)) {
				html = html.substring(matcher.end(2));
				int scriptEndIndex = getScriptEndIndex(html, -1);
				if (scriptEndIndex != -1) {
					chunkList.add(new HTMLChunkEntity(html.substring(0, scriptEndIndex), true));
					chunkList.add(new HTMLChunkEntity("</script>", false));
					html = html.substring(html.indexOf("</script>") + "</script>".length());
				}
				return parserChildChunk(html, chunkList, endIndex);
			}
		}
		return html;
	}
}
