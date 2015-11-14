package org.apache.maven.plugin.combo;

import java.io.File;

public class ComboUtilsTest {
	public static void main(String[] args) {
		String path = "/Users/kevin/workspace/activity/src/main/webapp/WEB-INF/jsp/qiandao/v3/index.jsp";
		System.out.println(ComboUtils.getAbsolutePath(new File(path), "/test/test.js"));
		System.out.println(ComboUtils.getAbsolutePath(new File(path), "./test.js"));
		System.out.println(ComboUtils.getAbsolutePath(new File(path), "../test.js"));
		System.out.println(ComboUtils.getAbsolutePath(new File(path), "../../../test.js"));
	}
}
