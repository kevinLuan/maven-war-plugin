package org.apache.maven.plugin.war.util;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeaJsTagProcessUtils {
  private static final String SEA_ENVIRONMENT = "\\$\\{\\s*requestScope\\s*\\[\\s*['\"]sea.env['\"]\\s*\\]\\s*\\}\\s*";
  private static final String regxp = "(?i)<\\s*(tags:sea_js)\\s+([^>]*)\\s*>";
  private static Pattern SEA_JS_PATTERN = Pattern.compile(regxp);

  public static String processSeaJsUrlMd5(String html, String host, File file) {
    try {
      Matcher matcher = SEA_JS_PATTERN.matcher(html);
      StringBuffer sb = new StringBuffer(html.length());
      while (matcher.find()) {
        String group = matcher.group(0);
        boolean isEnd = group.endsWith("/>");
        JspTagParser parser = new JspTagParser(group);
        parser.parser();
        String absUrl = parser.getAttr("base_url") + parser.getAttr("module_alias");
        String md5Name = getSeaJsMd5Value(absUrl, host, file, parser);
        Logger.debug("absUrl:" + absUrl);
        parser.getProperty().put("isMd5Process", "true");
        if (md5Name != null) {
          parser.getProperty().put("module_alias", md5Name);
        }
        group = parser.formatTag(isEnd);
        matcher.appendReplacement(sb, group);
      }
      matcher.appendTail(sb);
      return sb.toString();
    } catch (Exception ex) {
      Logger.error("processSeaJsUrlMd5(" + html + "," + host + "," + file.getAbsolutePath() + ")", ex);
    }
    return html;
  }

  private static String getSeaJsMd5Value(String url, String host, File file, JspTagParser parser) throws IOException {
    if (url != null) {
      url = (url.replaceFirst(SEA_ENVIRONMENT, "dist"));
      String base_url= parser.getAttr("base_url").replaceFirst(SEA_ENVIRONMENT, "dist");
      if (url.startsWith("/")) {
        if(StringUtils.isNotEmpty(host)){
          parser.getProperty().put("base_url", host+base_url);  
        }else{
          parser.getProperty().put("base_url", base_url);
        }
        
        if (file != null) {
          Logger.debug("formatSeaJsEnvURL(" + url + "," + host + "." + file.getAbsolutePath() + ")");
          return JspMd5ProcessUtils.getMd5Value(url, file);
        }
      }
    }
    return null;
  }
}
