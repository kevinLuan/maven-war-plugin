package org.apache.maven.plugin.combo;

import java.io.File;
import java.io.IOException;

public class ComboExecuteTest {
	public static void main(String[] args) throws IOException {
		String sourcePath = "/Users/kevin/workspace/activity_2014_10_15/src/main/webapp/WEB-INF/jsp/qiandao/FAQ/index.jsp";
		File sourceFile = new File(sourcePath);
		System.out.println(ComboExecute.comboScript(sourceFile));
		System.out.println(ComboExecute.comboLink(sourceFile));
	}
}
