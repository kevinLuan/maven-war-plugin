package org.apache.maven.plugin.war.util;

public class StringUtilsTest {
	public static String getHTMLCSS() {
		return "" + "<% response.setHeader(\"Cache-Control\", \"nocache,no-store\");\n" + "<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'  'http://www.w3.org/TR/html4/loose.dtd'>\n" + "<html><meta content='width=640, target-densitydpi=320, user-scalable=no'name=\"viewport'/>\n"
				+ "    <STYLE type='text/css'>\n" + "      .inputText {     border-radius: 5px;\n" + "            box-sizing: border-box;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "<div style='width: 300px;margin: auto;'>\n";
	}

	public static String getHTML() {
		String html = "<script type='text/javascript' src='http://localhost/activity/resources/sign/js/cj.js?v=1394941448607'>\n" + "</script>\n<script type='text/javascript' src='http://static.koudai.com/h5/js/viewport.js'></script>\n" + "<SCRIPT type='text/javascript'>\n"
				+ "var _gaq = _gaq || [];\n" + "_gaq.push(['_setAccount', 'UA-41808505-1']);\n" + "_gaq.push(['_setDomainName', 'koudai.com']);\n" + "  _gaq.push(['_trackPageview']);\n" + "(function () {\n" + "var ga = document.createElement('script');\n" + "ga.type = 'text/javascript';\n"
				+ "ga.async = true;\n" + "ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n" + "var s = document.getElementsByTagName('script')[0];" + "s.parentNode.insertBefore(ga, s);" + " })();" + "$(this).show();" + "</script>\n"
				+ "<c:import url='../sign_common/subPush_js.jsp'></c:import>\n";
		return html;
	}

	public void test() {
		System.out.println(StringUtils.filterHTML("<img src=\"${imgURL }\"/><link href='http://www.bai.com/?a=sb&v=123456879&sdfs'>test-v=23-sdfs</link>", null));
		System.out.println(StringUtils.filterHTML("<img src=\"${imgURL }\"/><img src=\"/activity/resources/newSign/images/${themeDir}house.jpg\"/><link href='http://www.bai.com/?a=sb&v=123456879&sdfs'>test-v=23-sdfs</link>", "http://link.com"));

	}
}
