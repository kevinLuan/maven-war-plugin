package com.apache.maven.plugin.compress;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.war.util.StringUtils;

import com.ext.maven.plugin.compress.CompressUtils;
import com.ext.maven.plugin.parser.HtmlChunkCompress;
import com.ext.maven.plugin.parser.ScriptTagChunkCompress;
import com.ext.maven.plugin.parser.StyleTagChunkCompress;

public class HtmlChunkCompressTest {
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		 testJS3();
//		testJS();
//		testJS2();
//		String code= new ScriptTagChunkCompress().parserHtmlAndCompress(getScriptHTML());
//		System.out.println(code);
		String code= new ScriptTagChunkCompress().parserHtmlAndCompress(getScript2());
//		System.out.println("-------");
		System.out.println(code);
	}


	static String formartChar$(String code) {
		if (code.indexOf("$") != -1) {
			code = code.replaceAll("\\$", "\\\\\\$");
		}
		return code;
	}

	static String processZYChar(String code) {
		code = code.replaceAll("\\\"", "\\\\\"");
		code = code.replaceAll("\\\'", "\\\\\'");
		return code;
	}

	public static void testCSS() {
		HtmlChunkCompress chunkCompress = new StyleTagChunkCompress();
		System.out.println(chunkCompress.parserHtmlAndCompress(getHTMLCSS()));
	}

	public static void testJS2() {
		try {
			String code = CompressUtils.read(new File("/Users/kevin/workspace/ishopping_admin/src/main/webapp/admin/activity/integral_mall/index.jsp"));
			code = StringUtils.filterHTML(code, null);
			code = new ScriptTagChunkCompress().parserHtmlAndCompress(code);
			System.out.println(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testJS() {
		try {
			String code = CompressUtils.read(new File("/Users/kevin/workspace/ishopping_admin/src/main/webapp/admin/activity/fileList.jsp"));
			code = StringUtils.filterHTML(code, null);
			code = new ScriptTagChunkCompress().parserHtmlAndCompress(code);
			System.err.println(code);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testJS3() {
		try {
			String code = CompressUtils.read(new File("/Users/kevin/workspace/hd/src/main/webapp/WEB-INF/jsp/meizhuangjieBJ/index.jsp"));
			code = StringUtils.filterHTML(code, null);
			code = new ScriptTagChunkCompress().parserHtmlAndCompress(code);
			System.out.println(code);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getHTMLCSS() {
		return "<!DOCTYPE http://www.w3.org/TR/html4/loose.dtd'>\n" + "<html><meta content='width=640, target-densitydpi=320, user-scalable=no'name=\"viewport'/>\n" + "    <STYLE type='text/css'>\n" + "      .AA {border-radius: 5px;}\n</style>\n"
				+ "<link rel='stylesheet' type='text/css' href='http://style.css?v=1394947323346'/>" + "    <STYLE type='text/css'>\n" + "      .BB {border-radius: 5px;}\n</style>\n" + "<link rel='stylesheet' type='text/css' href='http://style_LSS.css'/>" + "<link>"
				+ "    <STYLE type='text/css'>\n" + "      .CC {border-radius: 5px;}\n</style>\n" + "<link rel='stylesheet' type='text/css' href='http://style_LSS.css'/>" + "<link>\n" + "<meta name = \"format-detection\" content = \"telephone=no\">\n" + "    <STYLE type='text/css'>\n"
				+ "      .EE {border-radius: 5px;}\n</style>\n" + "<link rel='stylesheet' type='text/css' href='http://style_LSS.css'/>" + "<link>" + "\n" + "</head>\n" + "<body>";
	}

	public static String getHTMLCSS_() {
		return "<!DOCTYPE http://www.w3.org/TR/html4/loose.dtd'>\n" + "<html><meta content='width=640, target-densitydpi=320, user-scalable=no'name=\"viewport'/>\n" + "    <STYLE type='text/css'>\n" + "      .inputText {border-radius: 5px;}\n</style>"
				+ "<link rel='stylesheet' type='text/css' href='http://style.css?v=1394947323346'/>" + "    <STYLE type='text/css'>\n" + "      .inputText {border-radius: 5px;}\n</style>" + "<link rel='stylesheet' type='text/css' href='http://style_LSS.css'/>" + "<link>" + "\n" + "</head>\n"
				+ "<body>";
	}

	public static String getScriptHTML() {
		String html = "<html><script type='text/javascript' src='http://cj.js'></script>\n" + "<script type='text/javascript' src='http://static.js'></script>\n" + "<SCRIPT type='text/javascript'>\n function $(id){alert(id)}"
				+ "</Script>\n<a>AAA</a><script type='text/javascript' src='http://OPEN.js'></script>" + "<script type='text/javascript' src='http://LSS.js'/>\n" + "<SCRIPT type='text/javascript'>\n function $$(ABC){alert(ABC)}" + "</Script>\n" + "<div>BBB</div>\n"
				+ "<SCRIPT type='text/javascript'>\n function $$(ABCD){alert(ABC)}" + "\n</Script>\n" + "<body><script type='text/javascript' src='http://body.end.js'/></html>\n\n";
		return html;
	}

	public static String getScriptHTML1() {
		String html = "<html><script type='text/javascript' src='http://cj.js'></script>\n" + "<script type='text/javascript' src='http://static.js'></script>\n" + "<SCRIPT type='text/javascript'>\n function $(id){\nalert(id);\n\nalert(id)}"
				+ "</Script>\n<a>AAA</a><script type='text/javascript' src='http://OPEN.js'></script>" + "<script type='text/javascript' src='http://LSS.js'/>\n" + "<SCRIPT type='text/javascript'>\n function $$(ABC){alert(ABC)}" + "</Script>\n" + "<div>BBB</div>\n"
				+ "<SCRIPT type='text/javascript'>\n function $$(ABCD){alert(ABC)}" + "\n</Script>\n" + "<body><script type='text/javascript' src='http://body.end.js'/>" + "<script type=\"text/javascript\">" + "function show(){" + "var img='<img src=\"/abc.gif\" />';" + "}" + "</script>"
				+ "</html>\n\n";
		return html;
	}
	public static String getScript(){
		return "<html><script type='text/javascript'>function a(){$.alert('abc');\n\n\n   \nalert('xcvxc');\n}</script></head><html>";
	}
	
	public static String getScript1(){
		return "<html><script type='text/javascript'>function a(){$.alert('abc');\n\n\n   \nalert('xcvxc');\n}</script>\n<script type='text/javascript'>function b(){$.alert('abc');\n\n\n   \nalert('xcvxc');\n}</script></head><html>";
	}
	
	public static String getScript2(){
		return "<html><script type='text/javascript'>function a(){$.alert('abc');\n\n\n   \nalert('xcvxc');\n}</script>\n<script type='text/javascript'>function b(){$.alert('abc');\n\n\n   \nalert('xcvxc');\n}</script></head>\n<body><script type='text/javascript'>function done(){$.alert('abc');\n\n\n   \nalert('xcvxc');\n}</script></body><html>";
	}
}
