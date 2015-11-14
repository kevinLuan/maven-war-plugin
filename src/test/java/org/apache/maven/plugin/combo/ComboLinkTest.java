package org.apache.maven.plugin.combo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ComboLinkTest {
	public static void main(String[] args) throws IOException {
		String sourcePath = "/Users/kevin/workspace/activity/target/activity/resources/credits_mall/css/app.css";
		File sourceFile = new File(sourcePath);
		String code = FileUtils.readFileToString(sourceFile);
		String resString = ComboLink.processImportUrl(code, sourceFile);
		System.out.println(resString);
	}

	public static void processCssURL() {
		String sourcePath = "/Users/kevin/workspace/activity/src/main/webapp/WEB-INF/jsp/qiandao/v3/index.jsp";
		File sourceFile = new File(sourcePath);
		String resString = ComboLink.processCssURL(":url('../images/writeClose.png?v=0707131717') sd", sourceFile);
		System.out.println(resString);
		resString = ComboLink.processCssURL(" div:first-child{background:url(../images/buttonLeft.png?v=0114153307) no-repeat center", sourceFile);
		System.out.println(resString);

	}
}
