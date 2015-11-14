package org.apache.maven.plugin.combo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.model.TagEntity;
import org.apache.maven.plugin.war.util.FileProcess;

import com.ext.maven.plugin.compress.CSSCompress;
import com.ext.maven.plugin.compress.CompressUtils;
import com.ext.maven.plugin.compress.JavaScriptCompress;
import com.ext.maven.plugin.filter.Constants;

public class ComboExecute {
	/**
	 * 组合JS资源请求
	 * 
	 * @param sourceFile
	 * @return
	 * @throws IOException
	 */
	public static String comboScript(File sourceFile) throws IOException {
		String code = CompressUtils.read(sourceFile);
		return comboScript(code, sourceFile);
	}

	public static String comboScript(String code, File sourceFile) throws IOException {
		List<TagEntity> jsList = new ArrayList<TagEntity>();
		code = ComboScript.comboScript(code, jsList);
		int insertJsIndex = processAbsolutePath(jsList, code, sourceFile);
		String fileName = ComboUtils.getMd5File(jsList);
		StringBuilder builder = new StringBuilder(20000);
		for (int i = 0; i < jsList.size(); i++) {
			String jsCode = CompressUtils.read(jsList.get(i).getFile());
			if (jsList.get(i).isJsCompressFile() == false) {
				jsCode = JavaScriptCompress.compress(jsCode);
			}
			builder.append(ComboUtils.getPrefixDescription(jsList.get(i))).append(jsProcess(jsCode)).append("\n");
		}
		FileProcess.writeFile(new File(ComboUtils.getMD5PATH() + "/" + fileName + ".js"), builder.toString());
		if (jsList.size() == 0) {
			return code;
		}
		if (insertJsIndex == -1) {
			insertJsIndex = code.indexOf("</body>");
		}
		return insertScriptTag(insertJsIndex, code, fileName + ".js");

	}

	private static String jsProcess(String jsCode) {
		if(jsCode.trim().startsWith("(function()")){
			return "!"+jsCode;
		}
		return jsCode;
	}

	/**
	 * 将合并标签的路径解析出绝对路径
	 * 
	 * @param jsList
	 * @param code
	 * @param sourceFile
	 * @return 生成合并后的标签插入位置
	 */
	private static int processAbsolutePath(List<TagEntity> jsList, String code, File sourceFile) {
		int insertJsIndex = -1;
		for (int i = 0; i < jsList.size();) {
			if (jsList.get(i).getId() != null) {
				insertJsIndex = code.indexOf(jsList.get(i).getCode());
				// 排除掉带有ID不能压缩的标签
				jsList.remove(i);
			} else {
				String thisURL = jsList.get(i).getJsURL();
				jsList.get(i).setFile(new File(ComboUtils.getAbsolutePath(sourceFile, thisURL)));
				i++;
			}
		}
		return insertJsIndex;
	}

	/**
	 * 组合Css资源请求合并
	 * 
	 * @param sourceFile
	 * @return
	 * @throws IOException
	 */
	public static String comboLink(File sourceFile) throws IOException {
		String code = CompressUtils.read(sourceFile);
		return comboLink(code, sourceFile);
	}

	public static String comboLink(String code, File sourceFile) throws IOException {
		List<TagEntity> linkList = new ArrayList<TagEntity>();
		code = ComboLink.comboLink(code, linkList);
		int insertLinkIndex = -1;
		for (int i = 0; i < linkList.size(); i++) {
			String thisURL = linkList.get(i).getCssURL();
			linkList.get(i).setFile(new File(ComboUtils.getAbsolutePath(sourceFile, thisURL)));
		}
		String fileName = ComboUtils.getMd5File(linkList);
		StringBuilder builder = new StringBuilder(20000);
		for (int i = 0; i < linkList.size(); i++) {
			File file = linkList.get(i).getFile();
			String cssCode = CompressUtils.read(file);
			cssCode = ComboLink.processCssURL(cssCode, file);
			ComboLink.processImportUrl(cssCode, file);
			if (linkList.get(i).isCssCompressFile() == false) {
				cssCode = CSSCompress.compress(cssCode);
			}
			builder.append(ComboUtils.getPrefixDescription(linkList.get(i))).append(cssCode).append("\n\n");
		}
		FileProcess.writeFile(new File(ComboUtils.getMD5PATH() + "/" + fileName + ".css"), builder.toString());
		if (linkList.size() == 0) {
			return code;
		}
		if (insertLinkIndex == -1) {
			insertLinkIndex = code.indexOf("</head>");
		}
		return insertLinkTag(insertLinkIndex, code, fileName + ".css");
	}

	private static String insertScriptTag(int insertIndex, String code, String filePath) {
		String url = Constants.getCdnBaseURL() + ComboUtils.md5RelativePath();
		return code.substring(0, insertIndex) + ComboUtils.getComboJs(url + "/" + filePath) + code.substring(insertIndex);
	}

	private static String insertLinkTag(int insertIndex, String code, String filePath) {
		String url = Constants.getCdnBaseURL() + ComboUtils.md5RelativePath();
		return code.substring(0, insertIndex) + ComboUtils.getComboCss(url + "/" + filePath) + code.substring(insertIndex);
	}
}
