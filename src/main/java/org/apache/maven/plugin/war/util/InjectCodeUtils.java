package org.apache.maven.plugin.war.util;

import com.ext.maven.plugin.filter.Constants;

public class InjectCodeUtils {
  private static final String HEAD = "</head>";

  public static String injectCode(String html) {
    String injectCode = Constants.getProperty(Constants.HEAD_INJECT_CODE);
    if (StringUtils.isNotEmpty(injectCode)) {
      int index = html.indexOf(HEAD);
      if (index > 0) {
        String start = html.substring(0, index);
        return start + injectCode + html.substring(index);
      }
    }
    return html;
  }

}
